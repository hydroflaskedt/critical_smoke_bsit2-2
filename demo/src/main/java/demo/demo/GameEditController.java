package demo.demo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GameEditController {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PendingEditRepository pendingEditRepo;

    @Autowired
    userRepository userRepo;

    @Autowired
    SessionService sessionService;

    // Show the single-page edit form, pre-filled with current game data
    @GetMapping("/game/edit/{gameId}")
    public String showEditForm(
        @PathVariable Long gameId,
        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {
        sessionService.restoreSession(request, session);

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        Game game = gameRepo.findById(gameId).orElse(null);
        if (game == null || !game.getOwnerId().equals(userId)) {
            return "redirect:/publishedGames";
        }

        sessionService.addUserToModel(model, request, session);

        // If there's already a pending edit, show that instead (so owner
        // can keep editing their submitted-but-not-yet-approved changes)
        PendingEdit pendingEdit = pendingEditRepo.findByGameId(gameId);

        model.addAttribute("game", game);
        model.addAttribute("pendingEdit", pendingEdit);

        return "editGame";
    }

    // Submit the edit — saves as a PendingEdit, marks game as under review
    @PostMapping("/game/edit/{gameId}")
    public String submitEdit(
        @PathVariable Long gameId,

        @RequestParam String title,
        @RequestParam String description1,
        @RequestParam String description2,
        @RequestParam String genre,
        @RequestParam Double price,

        @RequestParam(required = false) MultipartFile coverImage,
        @RequestParam(required = false) MultipartFile gameLogo,
        @RequestParam(required = false) MultipartFile previewImage1,
        @RequestParam(required = false) MultipartFile previewImage2,
        @RequestParam(required = false) MultipartFile previewImage3,

        HttpSession session

    ) throws Exception {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/auth";

        Game game = gameRepo.findById(gameId).orElse(null);
        if (game == null || !game.getOwnerId().equals(userId)) {
            return "redirect:/publishedGames";
        }

        // Validate any uploaded files are real images before saving anything
        if (
            !isValidImage(coverImage) ||
            !isValidImage(gameLogo) ||
            !isValidImage(previewImage1) ||
            !isValidImage(previewImage2) ||
            !isValidImage(previewImage3)
        ) {
            return "redirect:/game/edit/" + gameId + "?error=invalidImage";
        }

        // Reuse existing pending edit row if one exists, else create new
        PendingEdit edit = pendingEditRepo.findByGameId(gameId);
        if (edit == null) {
            edit = new PendingEdit();
            edit.setGameId(gameId);
            edit.setOwnerId(userId);
        }

        edit.setTitle(title);
        edit.setDescription1(description1);
        edit.setDescription2(description2);
        edit.setGenre(genre);
        edit.setPrice(price);

        // New upload → save it. No upload → keep whatever the pending edit
        // already had, or fall back to the live game's current image.
        String fallbackCover = edit.getCoverImage() != null ? edit.getCoverImage() : game.getCoverImage();
        String fallbackLogo = edit.getGameLogo() != null ? edit.getGameLogo() : game.getGameLogo();
        String fallbackPreview1 = edit.getPreviewImage1() != null ? edit.getPreviewImage1() : game.getPreviewImage1();
        String fallbackPreview2 = edit.getPreviewImage2() != null ? edit.getPreviewImage2() : game.getPreviewImage2();
        String fallbackPreview3 = edit.getPreviewImage3() != null ? edit.getPreviewImage3() : game.getPreviewImage3();

        edit.setCoverImage(hasFile(coverImage) ? saveImage(coverImage, "covers") : fallbackCover);
        edit.setGameLogo(hasFile(gameLogo) ? saveImage(gameLogo, "logos") : fallbackLogo);
        edit.setPreviewImage1(hasFile(previewImage1) ? saveImage(previewImage1, "previews") : fallbackPreview1);
        edit.setPreviewImage2(hasFile(previewImage2) ? saveImage(previewImage2, "previews") : fallbackPreview2);
        edit.setPreviewImage3(hasFile(previewImage3) ? saveImage(previewImage3, "previews") : fallbackPreview3);

        pendingEditRepo.save(edit);

        // Mark the live game as under review (hides it until approved)
        game.setUnderReview(true);
        gameRepo.save(game);

        return "redirect:/publishedGames";
    }

    private boolean hasFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private boolean isValidImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return true;
        }
        BufferedImage image = ImageIO.read(file.getInputStream());
        return image != null;
    }

    private String saveImage(MultipartFile file, String folder) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = java.util.UUID.randomUUID() + ".jpg";
        String uploadDir = "src/main/resources/static/uploads/" + folder + "/";

        java.io.File directory = new java.io.File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        java.io.File destination = new java.io.File(uploadDir + fileName);

        BufferedImage image = ImageIO.read(file.getInputStream());

        BufferedImage resized = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resized.createGraphics();
        graphics.drawImage(image, 0, 0, 1280, 720, null);
        graphics.dispose();

        ImageIO.write(resized, "jpg", destination);

        return "/uploads/" + folder + "/" + fileName;
    }
}