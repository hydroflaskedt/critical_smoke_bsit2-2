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
public class adminController {
    @Autowired
GameRepository gameRepo;
    @Autowired
userRepository repo;
    @Autowired
OrderRepository orderRepo;
    @Autowired
PendingEditRepository pendingEditRepo;
    @Autowired
SessionService sessionService;
    @GetMapping("/admin")
public String adminPanel(
Model model,
HttpServletRequest request,
HttpSession session
    ) {
        sessionService.restoreSession(request, session);
Long userId = (Long) session.getAttribute("userId");
if (userId == null) {
return "redirect:/auth";
        }
user user = repo.findById(userId).orElse(null);
if (user == null || !user.isAdmin()) {
return "redirect:/";
        }
        sessionService.addUserToModel(model, request, session);
List<Game> pendingGames = gameRepo.findByApprovedFalseAndDeletedFalse();
List<Game> approvedGames = gameRepo.findByApprovedTrueAndDeletedFalse();
List<Game> deletedGames = gameRepo.findByApprovedTrueAndDeletedTrue();
List<PendingEdit> pendingEdits = pendingEditRepo.findAll();
model.addAttribute("pendingGames", pendingGames);
model.addAttribute("approvedGames", approvedGames);
model.addAttribute("deletedGames", deletedGames);
model.addAttribute("pendingEdits", pendingEdits);
return "adminPanel";
    }
    @PostMapping("/admin/approve/{id}")
public String approveGame(
        @PathVariable
Long id,
HttpSession session
    ) {
Long userId = (Long) session.getAttribute("userId");
if (userId == null) {
return "redirect:/auth";
        }
user user = repo.findById(userId).orElse(null);
if (user == null || !user.isAdmin()) {
return "redirect:/";
        }
Game game = gameRepo.findById(id).orElse(null);
if (game != null) {
game.setApproved(true);
            gameRepo.save(game);
        }
return "redirect:/admin";
    }
    @PostMapping("/admin/reject/{id}")
public String rejectGame(
        @PathVariable
Long id,
HttpSession session
    ) {
Long userId = (Long) session.getAttribute("userId");
if (userId == null) {
return "redirect:/auth";
        }
user user = repo.findById(userId).orElse(null);
if (user == null || !user.isAdmin()) {
return "redirect:/";
        }
        gameRepo.deleteById(id);
return "redirect:/admin";
    }
    @PostMapping("/admin/delete/{id}")
public String deleteGame(
        @PathVariable
Long id,
HttpSession session
    ) {
Long userId = (Long) session.getAttribute("userId");
if (userId == null) {
return "redirect:/auth";
        }
user user = repo.findById(userId).orElse(null);
if (user == null || !user.isAdmin()) {
return "redirect:/";
        }
Game game = gameRepo.findById(id).orElse(null);
if (game != null) {
game.setDeleted(true);
            gameRepo.save(game);
        }
return "redirect:/admin";
    }

    // Approve an edit request — copies the PendingEdit fields onto the live Game
    @PostMapping("/admin/approveEdit/{editId}")
public String approveEdit(
        @PathVariable
Long editId,
HttpSession session
    ) {
Long userId = (Long) session.getAttribute("userId");
if (userId == null) {
return "redirect:/auth";
        }
user user = repo.findById(userId).orElse(null);
if (user == null || !user.isAdmin()) {
return "redirect:/";
        }

PendingEdit edit = pendingEditRepo.findById(editId).orElse(null);
if (edit != null) {
Game game = gameRepo.findById(edit.getGameId()).orElse(null);
if (game != null) {
game.setTitle(edit.getTitle());
game.setDescription1(edit.getDescription1());
game.setDescription2(edit.getDescription2());
game.setGenre(edit.getGenre());
game.setPrice(edit.getPrice());
game.setCoverImage(edit.getCoverImage());
game.setGameLogo(edit.getGameLogo());
game.setPreviewImage1(edit.getPreviewImage1());
game.setPreviewImage2(edit.getPreviewImage2());
game.setPreviewImage3(edit.getPreviewImage3());
game.setUnderReview(false);
                gameRepo.save(game);
            }
            pendingEditRepo.delete(edit);
        }
return "redirect:/admin";
    }

    // Reject an edit request — discards the PendingEdit, restores the game to visible
    @PostMapping("/admin/rejectEdit/{editId}")
public String rejectEdit(
        @PathVariable
Long editId,
HttpSession session
    ) {
Long userId = (Long) session.getAttribute("userId");
if (userId == null) {
return "redirect:/auth";
        }
user user = repo.findById(userId).orElse(null);
if (user == null || !user.isAdmin()) {
return "redirect:/";
        }

PendingEdit edit = pendingEditRepo.findById(editId).orElse(null);
if (edit != null) {
Game game = gameRepo.findById(edit.getGameId()).orElse(null);
if (game != null) {
game.setUnderReview(false);
                gameRepo.save(game);
            }
            pendingEditRepo.delete(edit);
        }
return "redirect:/admin";
    }

    @GetMapping("/admin/customers")
public String customerManagement(
    Model model,
    HttpServletRequest request,
    HttpSession session
) {
    sessionService.restoreSession(request, session);

    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
        return "redirect:/auth";
    }

    user user = repo.findById(userId).orElse(null);
    if (user == null || !user.isAdmin()) {
        return "redirect:/";
    }

    sessionService.addUserToModel(model, request, session);

    List<user> allUsers = repo.findAll();
    List<CustomerRow> customerRows = new java.util.ArrayList<>();

    for (user u : allUsers) {
        List<Order> purchases = orderRepo.findByUserId(u.getUserId());
        List<Game> published = gameRepo.findByOwnerId(u.getUserId());
        
        CustomerRow row = new CustomerRow(u, purchases, published);
        customerRows.add(row);
    }

    model.addAttribute("customerRows", customerRows);

    return "customerManagement";
}
}