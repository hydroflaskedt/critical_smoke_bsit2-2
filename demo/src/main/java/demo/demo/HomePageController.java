package demo.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class HomePageController {

    @Autowired userRepository repo;
    @Autowired SessionService sessionService;

    @GetMapping("/")
    public String homepage(Model model, HttpSession session,  HttpServletRequest request){
        
        sessionService.restoreSession(
            request,
            session
        );

        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId",userId);
        userInfo.put("username", username);
        userInfo.put("email", email);

        boolean loggedIn = session.getAttribute("userId") != null;

        model.addAttribute("loggedIn", loggedIn);
         
        model.addAttribute("userInfo", userInfo);

        return "homepage";
    }

    @PostMapping("/logout")
    public void logout(HttpSession session,  HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();

        Cookie cookie = new Cookie("userId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
    } 
}