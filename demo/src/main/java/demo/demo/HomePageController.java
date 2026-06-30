package demo.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePageController {

    @Autowired
    userRepository repo;

    @Autowired
    GameRepository gameRepo;

    @Autowired
    SessionService sessionService;

    @GetMapping("/")
    public String homepage(
        Model model,
        HttpSession session,
        HttpServletRequest request,
        @RequestParam(value = "genre", required = false) String genre,
        @RequestParam(value = "search", required = false) String search
    ) {
        sessionService.restoreSession(request, session);

        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        boolean isAdmin = false;
        if (userId != null) {
            user u = repo.findById(userId).orElse(null);
            if (u != null) {
                isAdmin = u.isAdmin();
            }
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("username", username);
        userInfo.put("email", email);
        userInfo.put("isAdmin", isAdmin);

        boolean loggedIn = session.getAttribute("userId") != null;
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("userInfo", userInfo);

        // Get only approved games that are NOT deleted
        List<Game> approvedGames = gameRepo.findByApprovedTrueAndDeletedFalse();

        // Filter by search if provided
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.trim().toLowerCase();
            approvedGames = approvedGames.stream()
                .filter(game -> game.getTitle().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
            model.addAttribute("searchQuery", search.trim());
        }

        // Filter by genre if provided
        if (genre != null && !genre.isEmpty()) {
            approvedGames = approvedGames.stream()
                .filter(game -> game.getGenre() != null && 
                        game.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
            model.addAttribute("selectedGenre", genre);
        }

        model.addAttribute("games", approvedGames);

        return "homepage";
    }

    @PostMapping("/logout")
    public void logout(
        HttpSession session,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        request.getSession().invalidate();
        Cookie cookie = new Cookie("userId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}