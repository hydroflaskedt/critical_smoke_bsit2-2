package demo.demo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginSignupController {

    @Autowired
    private userRepository repo;

    @Autowired
    SessionService sessionService;

    @Autowired
    VoucherService voucherService;

    @GetMapping("/auth")
    public String showLoginSignup(Model model, HttpSession session, HttpServletRequest request) {
        sessionService.restoreSession(request, session);
        if (session.getAttribute("username") != null) {
            return "redirect:/";
        }
        return "login_signup";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/userOrAdmin")
    public String userOrAdmin(HttpSession session, HttpServletResponse jar) {
        return "userOrAdmin";
    }

    @PostMapping("/logUser")
    @ResponseBody
    public Map<String, Object> login(@RequestBody user request, HttpSession session, HttpServletResponse jar) {
        user foundUser = repo.findByUsernameAndUserPassword(
            request.getUsername(),
            request.getUserPassword()
        );

        Map<String, Object> response = new HashMap<>();

        if (foundUser == null) {
            response.put("success", false);
            return response;
        }

        session.setAttribute("userId", foundUser.getUserId());
        session.setAttribute("username", foundUser.getUsername());
        session.setAttribute("email", foundUser.getEmail());
        session.setAttribute("isAdmin", foundUser.isAdmin());

        Cookie cookie = new Cookie("userId", String.valueOf(foundUser.getUserId()));
        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setPath("/");
        jar.addCookie(cookie);

        response.put("success", true);
        if (foundUser.isAdmin()) {
            response.put("redirect", "/userOrAdmin");
        } else {
            response.put("redirect", "/");
        }

        return response;
    }

    @PostMapping("/signUser")
    @ResponseBody
    public Map<String, Object> signup(@RequestBody user request) {
        Map<String, Object> response = new HashMap<>();

        // Check username and email separately so either one being taken blocks signup
        user byUsername = repo.findByUsername(request.getUsername());
        user byEmail = repo.findByEmail(request.getEmail());

        if (byUsername != null) {
            response.put("success", false);
            response.put("reason", "username taken");
            return response;
        }

        if (byEmail != null) {
            response.put("success", false);
            response.put("reason", "email taken");
            return response;
        }

        user savedUser = repo.save(request);
        voucherService.createWelcomeVouchers(savedUser.getUserId());

        response.put("success", true);
        return response;
    }
}