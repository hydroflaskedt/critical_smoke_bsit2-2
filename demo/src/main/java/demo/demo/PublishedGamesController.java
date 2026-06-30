package demo.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
public class PublishedGamesController {
    @Autowired
userRepository repo;
    @Autowired
GameRepository gameRepo;
    @Autowired
SessionService sessionService;
    @GetMapping("/publishedGames")
public String publishedGames(
Model model,
HttpServletRequest request,
HttpSession session
    ) {
        sessionService.restoreSession(request, session);
        sessionService.addUserToModel(model, request, session);
Long userId = (Long) session.getAttribute("userId");
if (userId != null) {
List<Game> games = gameRepo.findByOwnerId(userId);
model.addAttribute("games", games);
        } else {
model.addAttribute("games", java.util.Collections.emptyList());
        }
return "publishedGames";
    }
}