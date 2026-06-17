package demo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {

    @Autowired
    private userRepository repo;

    public void restoreSession(HttpServletRequest request,
                               HttpSession session) {

        if (session.getAttribute("userId") != null) {
            return;
        }

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("userId")) {

                Long userId =
                    Long.parseLong(cookie.getValue());

                user foundUser =
                    repo.findById(userId).orElse(null);

                if (foundUser != null) {

                    session.setAttribute(
                        "userId",
                        foundUser.getUserId()
                    );

                    session.setAttribute(
                        "username",
                        foundUser.getUsername()
                    );

                    session.setAttribute(
                        "email",
                        foundUser.getEmail()
                    );
                }

                break;
            }
        }
    }
}