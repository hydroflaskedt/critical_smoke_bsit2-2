package demo.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    @Autowired
    CartItemRepository cartRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    userRepository userRepo;

    @Autowired
    VoucherService voucherService;

    @Autowired
    SessionService sessionService;

    @GetMapping("/checkout")
    public String checkoutPage(
        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {
        sessionService.restoreSession(request, session);

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        sessionService.addUserToModel(model, request, session);

        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        double total = cartItems.stream().mapToDouble(CartItem::getGamePrice).sum();

        String appliedVoucher = (String) session.getAttribute("appliedVoucher");
        Integer discountPercent = (Integer) session.getAttribute("discountPercent");

        double discountedTotal = total;
        if (discountPercent != null && discountPercent > 0) {
            discountedTotal = total * (1 - discountPercent / 100.0);
        }

        List<Voucher> vouchers = voucherService.getUnusedVouchers(userId);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("discountedTotal", discountedTotal);
        model.addAttribute("appliedVoucher", appliedVoucher);
        model.addAttribute("discountPercent", discountPercent != null ? discountPercent : 0);
        model.addAttribute("vouchers", vouchers);

        return "checkout";
    }

    @PostMapping("/checkout/apply-voucher")
    public String applyVoucher(
        @RequestParam String voucherCode,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        int discount = voucherService.applyVoucher(userId, voucherCode.trim());
        if (discount > 0) {
            session.setAttribute("appliedVoucher", voucherCode.trim());
            session.setAttribute("discountPercent", discount);
        }

        return "redirect:/checkout";
    }

    @GetMapping("/checkout/remove-voucher")
    public String removeVoucher(HttpSession session) {
        session.removeAttribute("appliedVoucher");
        session.removeAttribute("discountPercent");
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(
        @RequestParam(value = "paymentMethod", defaultValue = "gcash") String paymentMethod,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty()) return "redirect:/cart";

        // Calculate totals
        double originalTotal = cartItems.stream().mapToDouble(CartItem::getGamePrice).sum();
        String appliedVoucher = (String) session.getAttribute("appliedVoucher");
        Integer discountPercent = (Integer) session.getAttribute("discountPercent");
        double finalTotal = originalTotal;
        if (discountPercent != null && discountPercent > 0) {
            finalTotal = originalTotal * (1 - discountPercent / 100.0);
        }

        // Save orders (skip duplicates)
        List<Order> existingOrders = orderRepo.findByUserId(userId);
        for (CartItem item : cartItems) {
            boolean alreadyOwned = existingOrders.stream()
                .anyMatch(o -> o.getGameId().equals(item.getGameId()));
            if (!alreadyOwned) {
                Order order = new Order();
                order.setUserId(userId);
                order.setGameId(item.getGameId());
                order.setGameTitle(item.getGameTitle());
                order.setGamePrice(item.getGamePrice());
                order.setCoverImage(item.getCoverImage());
                orderRepo.save(order);
            }
        }

        // Mark voucher used
        if (appliedVoucher != null) {
            voucherService.markUsed(userId, appliedVoucher);
        }

        // Stash receipt info for the success/library page
        session.setAttribute("checkoutSuccess", true);
        session.setAttribute("receiptDiscountPercent", discountPercent != null ? discountPercent : 0);
        session.setAttribute("receiptOriginalTotal", originalTotal);
        session.setAttribute("receiptFinalTotal", finalTotal);

        // Clear cart and voucher
        session.removeAttribute("appliedVoucher");
        session.removeAttribute("discountPercent");
        cartRepo.deleteAll(cartItems);

        // Go straight to library — modal shown there
        return "redirect:/library";
    }
}