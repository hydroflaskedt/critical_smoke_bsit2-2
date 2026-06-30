package demo.demo;

import java.util.List;

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
            model.addAttribute("games", purchasedGames);
        }

        return "library";
    }
}