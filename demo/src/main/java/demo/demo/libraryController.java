package demo.demo;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class libraryController {
    @Autowired userRepository repo;
    @Autowired SessionService sessionService;

    @GetMapping("/library")
    public String library(Model model, HttpSession session,  HttpServletRequest request){

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

    return "library";
}
}
