package demo.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentHistoryController {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    SessionService sessionService;

    @GetMapping("/payment-history")
    public String paymentHistory(
        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {
        sessionService.restoreSession(request, session);

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        sessionService.addUserToModel(model, request, session);

        List<Order> orders = orderRepo.findByUserId(userId);
        model.addAttribute("orders", orders);

        return "paymentHistory";
    }

    @PostMapping("/payment-history/refund/{orderId}")
    public String refund(
        @PathVariable Long orderId,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        Order order = orderRepo.findById(orderId).orElse(null);

        // Only allow refund if it belongs to this user and within 14 days
        if (order != null && order.getUserId().equals(userId) && !order.isRefunded()) {
            long daysSincePurchase = java.time.temporal.ChronoUnit.DAYS.between(
                order.getCreatedAt().toInstant(),
                java.time.Instant.now()
            );
            if (daysSincePurchase <= 14) {
                order.setRefunded(true);
                orderRepo.save(order);
            }
        }

        return "redirect:/payment-history";
    }
}