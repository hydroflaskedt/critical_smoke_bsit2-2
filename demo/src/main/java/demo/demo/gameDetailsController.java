package demo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class gameDetailsController {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    SessionService sessionService;

    @GetMapping("/game/{id}")
    public String gameDetails(

        @PathVariable
        Long id,

        Model model,
        HttpServletRequest request,
        HttpSession session

    ) {

        sessionService.restoreSession(request, session);
        sessionService.addUserToModel(model, request, session);

        Game game = gameRepo.findById(id).orElse(null);

        if (game == null) {
            return "redirect:/library";
        }

        model.addAttribute("game", game);

        return "gameDetails";
    }

}