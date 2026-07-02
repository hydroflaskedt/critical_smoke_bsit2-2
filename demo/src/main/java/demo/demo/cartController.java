package demo.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class cartController {

    @Autowired
    userRepository repo;

    @Autowired
    SessionService sessionService;

    @Autowired
    GameRepository gameRepo;

    @Autowired
    CartItemRepository cartRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    VoucherService voucherService;

    @GetMapping("/cart")
    public String cart(
        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {
        sessionService.restoreSession(request, session);
        sessionService.addUserToModel(model, request, session);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            List<CartItem> cartItems = cartRepo.findByUserId(userId);

            // Remove cart items for deleted games
            List<CartItem> itemsToDelete = new ArrayList<>();
            for (CartItem item : cartItems) {
                Game game = gameRepo.findById(item.getGameId()).orElse(null);
                if (game != null && game.isDeleted()) {
                    itemsToDelete.add(item);
                }
            }
            for (CartItem item : itemsToDelete) {
                cartRepo.delete(item);
            }

            // Refresh after deletion
            cartItems = cartRepo.findByUserId(userId);

            double total = 0.0;
            for (CartItem item : cartItems) {
                if (item.getGamePrice() != null) {
                    total += item.getGamePrice();
                }
            }

            // Voucher from session
            String appliedVoucher = (String) session.getAttribute("appliedVoucher");
            Integer discountPercent = (Integer) session.getAttribute("discountPercent");

            double discountedTotal = total;
            if (discountPercent != null && discountPercent > 0) {
                discountedTotal = total * (1 - discountPercent / 100.0);
            }

            // One-time error message then clear it
            String voucherError = (String) session.getAttribute("voucherError");
            session.removeAttribute("voucherError");

            // Unused vouchers for the dropdown
            List<Voucher> vouchers = voucherService.getUnusedVouchers(userId);

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("total", total);
            model.addAttribute("discountedTotal", discountedTotal);
            model.addAttribute("appliedVoucher", appliedVoucher);
            model.addAttribute("discountPercent", discountPercent != null ? discountPercent : 0);
            model.addAttribute("vouchers", vouchers);
            model.addAttribute("voucherError", voucherError);
        }

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(
        @RequestParam Long gameId,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth";
        }

        Game game = gameRepo.findById(gameId).orElse(null);
        if (game == null) {
            return "redirect:/";
        }

        // Check if already in cart
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        boolean alreadyInCart = cartItems.stream()
            .anyMatch(item -> item.getGameId().equals(gameId));

        if (alreadyInCart) {
            session.setAttribute("cartError", "already_in_cart");
            return "redirect:/game/" + gameId;
        }

        // Check if already purchased
        List<Order> orders = orderRepo.findByUserId(userId);
        boolean alreadyPurchased = orders.stream()
            .anyMatch(order -> order.getGameId().equals(gameId));

        if (alreadyPurchased) {
            session.setAttribute("cartError", "already_purchased");
            return "redirect:/game/" + gameId;
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setGameId(gameId);
        cartItem.setGameTitle(game.getTitle());
        cartItem.setGamePrice(game.getPrice());
        cartItem.setCoverImage(game.getCoverImage());
        cartRepo.save(cartItem);

        session.setAttribute("cartSuccess", "added_to_cart");
        return "redirect:/game/" + gameId;
    }

    // Buy Now: add to cart (if not already there) then go straight to checkout
    @PostMapping("/cart/buy-now")
    public String buyNow(
        @RequestParam Long gameId,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        // Check if already purchased
        List<Order> orders = orderRepo.findByUserId(userId);
        boolean alreadyPurchased = orders.stream()
            .anyMatch(order -> order.getGameId().equals(gameId));
        if (alreadyPurchased) {
            session.setAttribute("cartError", "already_purchased");
            return "redirect:/game/" + gameId;
        }

        // Add to cart if not already there
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        boolean alreadyInCart = cartItems.stream()
            .anyMatch(item -> item.getGameId().equals(gameId));

        if (!alreadyInCart) {
            Game game = gameRepo.findById(gameId).orElse(null);
            if (game != null) {
                CartItem cartItem = new CartItem();
                cartItem.setUserId(userId);
                cartItem.setGameId(gameId);
                cartItem.setGameTitle(game.getTitle());
                cartItem.setGamePrice(game.getPrice());
                cartItem.setCoverImage(game.getCoverImage());
                cartRepo.save(cartItem);
            }
        }

        return "redirect:/checkout";
    }

    @Transactional
    @PostMapping("/cart/remove/{gameId}")
    public String removeFromCart(
        @PathVariable Long gameId,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            cartRepo.deleteByUserIdAndGameId(userId, gameId);
        }
        return "redirect:/cart";
    }

    @Transactional
    @PostMapping("/cart/remove-all")
    public String removeAll(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            List<CartItem> items = cartRepo.findByUserId(userId);
            cartRepo.deleteAll(items);
        }
        // Clear voucher too when cart is wiped
        session.removeAttribute("appliedVoucher");
        session.removeAttribute("discountPercent");
        return "redirect:/cart";
    }

    @PostMapping("/cart/apply-voucher")
    public String applyVoucher(
        @RequestParam String voucherCode,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        String code = voucherCode == null ? "" : voucherCode.trim();

        if (code.isEmpty()) {
            session.setAttribute("voucherError", "Please select a voucher.");
            return "redirect:/cart";
        }

        int discount = voucherService.applyVoucher(userId, code);
        if (discount > 0) {
            session.setAttribute("appliedVoucher", code);
            session.setAttribute("discountPercent", discount);
        } else {
            session.setAttribute("voucherError", "That voucher code is invalid or already used.");
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart/remove-voucher")
    public String removeVoucher(HttpSession session) {
        session.removeAttribute("appliedVoucher");
        session.removeAttribute("discountPercent");
        return "redirect:/cart";
    }

    // Original checkout — kept exactly as-is
    @Transactional
    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth";
        }

        List<CartItem> cartItems = cartRepo.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        List<Order> existingOrders = orderRepo.findByUserId(userId);

        for (CartItem item : cartItems) {
            boolean alreadyPurchased = existingOrders.stream()
                .anyMatch(order -> order.getGameId().equals(item.getGameId()));

            if (!alreadyPurchased) {
                Order order = new Order();
                order.setUserId(userId);
                order.setGameId(item.getGameId());
                order.setGameTitle(item.getGameTitle());
                order.setGamePrice(item.getGamePrice());
                order.setCoverImage(item.getCoverImage());
                orderRepo.save(order);
            }
        }

        for (CartItem item : cartItems) {
            cartRepo.deleteById(item.getCartItemId());
        }

        return "redirect:/library";
    }
}