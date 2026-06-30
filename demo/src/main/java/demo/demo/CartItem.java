package demo.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "game_title")
    private String gameTitle;

    @Column(name = "game_price")
    private Double gamePrice;

    @Column(name = "cover_image")
    private String coverImage;

    public CartItem() {}

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public Double getGamePrice() {
        return gamePrice;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public void setGamePrice(Double gamePrice) {
        this.gamePrice = gamePrice;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}