package demo.demo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class publishGamesController {

    @Autowired
    userRepository repo;

    @Autowired
    GameRepository gameRepo;

    @Autowired
    SessionService sessionService;


    @GetMapping("/publishGames")
    public String publishGames(
        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {

        sessionService.restoreSession(
            request,
            session
        );

        sessionService.addUserToModel(
            model,
            request,
            session
        );

        return "publishGames";
    }


    @PostMapping("/publishGames")
    public String publishGames(

        @RequestParam(required = false)
        String title,

        @RequestParam(required = false)
        String description1,

        @RequestParam(required = false)
        String description2,

        @RequestParam(required = false)
        List<String> genres,

        @RequestParam(required = false)
        Double price,

        HttpSession session

    ) {

        if (
            title == null || title.isBlank() ||
            description1 == null || description1.isBlank() ||
            description2 == null || description2.isBlank() ||
            genres == null || genres.isEmpty() ||
            price == null
        ) {
            return "redirect:/publishGames";
        }

        Long gameId = (Long) session.getAttribute("currentGameId");

        Game saveGame;

        if (gameId != null) {
            saveGame = gameRepo.findById(gameId).orElse(new Game());
        } else {
            saveGame = new Game();
        }

        saveGame.setTitle(title);
        saveGame.setDescription1(description1);
        saveGame.setDescription2(description2);
        saveGame.setGenre(String.join(",", genres));
        saveGame.setPrice(price);

        saveGame = gameRepo.save(saveGame);

        session.setAttribute(
            "currentGameId",
            saveGame.getGameId()
        );

        return "redirect:/uploadThumbnail";
    }


    @GetMapping("/uploadThumbnail")
    public String uploadThumbnails(

        @RequestParam(required = false)
        String error,

        Model model,
        HttpServletRequest request,
        HttpSession session
    ) {

        sessionService.restoreSession(
            request,
            session
        );

        sessionService.addUserToModel(
            model,
            request,
            session
        );

        model.addAttribute("error", error);

        return "uploadThumbnail";
    }


    @PostMapping("/uploadThumbnail")
    public String UploadThumbnail(

        @RequestParam(required = false)
        MultipartFile coverImage,

        @RequestParam(required = false)
        MultipartFile gameLogo,

        @RequestParam(required = false)
        MultipartFile previewImage1,

        @RequestParam(required = false)
        MultipartFile previewImage2,

        @RequestParam(required = false)
        MultipartFile previewImage3,

        HttpSession session

    ) throws Exception {

        Long gameId =
            (Long) session.getAttribute(
                "currentGameId"
            );

        if (gameId == null) {
            return "redirect:/publishGames";
        }

        Game saveImages = gameRepo.findById(gameId).orElse(null);

        if (saveImages == null) {
            return "redirect:/publishGames";
        }

        //check if atleast cover image or logo thats all we need its up to them if they want some preview or not
        boolean noCoverImage =
            coverImage == null ||
            coverImage.isEmpty();

        boolean noGameLogo =
            gameLogo == null ||
            gameLogo.isEmpty();

        if (noCoverImage && noGameLogo) {
            return "redirect:/uploadThumbnail?error=missingRequired";
        }

        // Validate all uploaded files are real images before saving anything
        if (
            !isValidImage(coverImage) ||
            !isValidImage(gameLogo) ||
            !isValidImage(previewImage1) ||
            !isValidImage(previewImage2) ||
            !isValidImage(previewImage3)
        ) {
            return "redirect:/uploadThumbnail?error=invalidImage";
        }

        saveImages.setCoverImage(
            saveImage(
                coverImage,
                "covers"
            )
        );

        saveImages.setGameLogo(
            saveImage(
                gameLogo,
                "logos"
            )
        );

        saveImages.setPreviewImage1(
            saveImage(
                previewImage1,
                "previews"
            )
        );

        saveImages.setPreviewImage2(
            saveImage(
                previewImage2,
                "previews"
            )
        );

        saveImages.setPreviewImage3(
            saveImage(
                previewImage3,
                "previews"
            )
        );

        saveImages.setOwnerId((Long) session.getAttribute("userId"));

        gameRepo.save(saveImages);

        return "redirect:/uploadGameFiles";
    }


  @GetMapping("/uploadGameFiles")
   public String uploadGameFiles(

    @RequestParam(required = false)
    String error,

    Model model,
    HttpServletRequest request,
    HttpSession session
    ) {

    sessionService.restoreSession(request, session);
    sessionService.addUserToModel(model, request, session);

    model.addAttribute("error", error);

    return "uploadGameFiles";
}


    @PostMapping("/uploadGameFiles")
    public String uploadGameFiles(

    @RequestParam(required = false)
    String gameFileName,

    HttpSession session

) {

    Long gameId =
        (Long) session.getAttribute(
            "currentGameId"
        );

    if (gameId == null) {
        return "redirect:/publishGames";
    }

    Game game =
        gameRepo.findById(gameId)
                .orElse(null);

    if (game == null) {
        return "redirect:/publishGames";
    }

    // If no file was selected, redirect back with an error
    if (
        gameFileName == null ||
        gameFileName.isBlank()
    ) {
        return "redirect:/uploadGameFiles?error=missingFile";
    }

    game.setGameFiles(gameFileName);

    gameRepo.save(game);

    session.removeAttribute("currentGameId");

    return "redirect:/library";
}

    private boolean isValidImage(
        MultipartFile file
    ) throws Exception {

        if (
            file == null ||
            file.isEmpty()
        ) {
            return true;
        }

        BufferedImage image =
            ImageIO.read(
                file.getInputStream()
            );

        return image != null;
    }


    private String saveImage(
        MultipartFile file,
        String folder
    ) throws Exception {

        if (
            file == null ||
            file.isEmpty()
        ) {
            return null;
        }

        String fileName =
            java.util.UUID.randomUUID()
            + ".jpg";

        String uploadDir =
            "src/main/resources/static/uploads/"
            + folder
            + "/";

        java.io.File directory =
            new java.io.File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        java.io.File destination =
            new java.io.File(
                uploadDir + fileName
            );

        BufferedImage image =
            ImageIO.read(
                file.getInputStream()
            );

        BufferedImage resized =
            new BufferedImage(
                1280,
                720,
                BufferedImage.TYPE_INT_RGB
            );

        Graphics2D graphics =
            resized.createGraphics();

        graphics.drawImage(
            image,
            0,
            0,
            1280,
            720,
            null
        );

        graphics.dispose();

        ImageIO.write(
            resized,
            "jpg",
            destination
        );

        return "/uploads/"
            + folder
            + "/"
            + fileName;
    }

}