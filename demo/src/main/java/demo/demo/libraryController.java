package demo.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class libraryController {

    @Autowired
    userRepository repo;

    @Autowired
    SessionService sessionService;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    GameRepository gameRepo;

    @GetMapping("/library")
    public String library(
        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {
        sessionService.restoreSession(request, session);
        sessionService.addUserToModel(model, request, session);

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            List<Order> purchasedGames = orderRepo.findByUserId(userId);

            // Exclude refunded games from the library
            List<LibraryItem> items = purchasedGames.stream()
                .filter(order -> !order.isRefunded())
                .map(order -> {
                    Game game = gameRepo.findById(order.getGameId()).orElse(null);
                    String genre = (game != null && game.getGenre() != null)
                        ? game.getGenre() : "";
                    return new LibraryItem(order, genre);
                })
                .collect(Collectors.toList());

            // Split comma-separated genre strings into individual genres
            List<String> genres = items.stream()
                .map(LibraryItem::getGenre)
                .filter(g -> g != null && !g.isBlank())
                .flatMap(g -> java.util.Arrays.stream(g.split(",")))
                .map(String::trim)
                .filter(g -> !g.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

            model.addAttribute("games", items);
            model.addAttribute("genres", genres);
        }

        // Pick up checkout success flag and receipt data, then clear them
        Boolean checkoutSuccess = (Boolean) session.getAttribute("checkoutSuccess");
        Integer discountPercent = (Integer) session.getAttribute("receiptDiscountPercent");
        Double originalTotal = (Double) session.getAttribute("receiptOriginalTotal");
        Double finalTotal = (Double) session.getAttribute("receiptFinalTotal");

        model.addAttribute("checkoutSuccess", checkoutSuccess != null && checkoutSuccess);
        model.addAttribute("receiptDiscountPercent", discountPercent != null ? discountPercent : 0);
        model.addAttribute("receiptOriginalTotal", originalTotal != null ? originalTotal : 0.0);
        model.addAttribute("receiptFinalTotal", finalTotal != null ? finalTotal : 0.0);

        // Clear so modal only shows once
        session.removeAttribute("checkoutSuccess");
        session.removeAttribute("receiptDiscountPercent");
        session.removeAttribute("receiptOriginalTotal");
        session.removeAttribute("receiptFinalTotal");

        return "library";
    }

    public static class LibraryItem {
        private final Order order;
        private final String genre;

        public LibraryItem(Order order, String genre) {
            this.order = order;
            this.genre = genre;
        }

        public Long getGameId()      { return order.getGameId(); }
        public String getGameTitle() { return order.getGameTitle(); }
        public String getCoverImage(){ return order.getCoverImage(); }
        public Double getGamePrice() { return order.getGamePrice(); }
        public String getGenre()     { return genre; }
    }
}