package demo.demo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pending_edits")
public class PendingEdit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long editId;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column
    private String title;

    @Column(name = "description1", columnDefinition = "TEXT")
    private String description1;

    @Column(name = "description2", columnDefinition = "TEXT")
    private String description2;

    @Column
    private String genre;

    @Column
    private Double price;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "game_logo")
    private String gameLogo;

    @Column(name = "preview_image1")
    private String previewImage1;

    @Column(name = "preview_image2")
    private String previewImage2;

    @Column(name = "preview_image3")
    private String previewImage3;

    @Column(
        name = "submitted_at",
        insertable = false,
        updatable = false
    )
    private java.sql.Timestamp submittedAt;

    public PendingEdit() {}

    public Long getEditId() { return editId; }
    public Long getGameId() { return gameId; }
    public Long getOwnerId() { return ownerId; }
    public String getTitle() { return title; }
    public String getDescription1() { return description1; }
    public String getDescription2() { return description2; }
    public String getGenre() { return genre; }
    public Double getPrice() { return price; }
    public String getCoverImage() { return coverImage; }
    public String getGameLogo() { return gameLogo; }
    public String getPreviewImage1() { return previewImage1; }
    public String getPreviewImage2() { return previewImage2; }
    public String getPreviewImage3() { return previewImage3; }
    public java.sql.Timestamp getSubmittedAt() { return submittedAt; }

    public void setEditId(Long editId) { this.editId = editId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription1(String description1) { this.description1 = description1; }
    public void setDescription2(String description2) { this.description2 = description2; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPrice(Double price) { this.price = price; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public void setGameLogo(String gameLogo) { this.gameLogo = gameLogo; }
    public void setPreviewImage1(String previewImage1) { this.previewImage1 = previewImage1; }
    public void setPreviewImage2(String previewImage2) { this.previewImage2 = previewImage2; }
    public void setPreviewImage3(String previewImage3) { this.previewImage3 = previewImage3; }
    public void setSubmittedAt(java.sql.Timestamp submittedAt) { this.submittedAt = submittedAt; }
}