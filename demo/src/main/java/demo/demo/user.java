package demo.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class user {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long userId;

    @Column(name = "user_password")
    private String userPassword;

    private String username; 
    private String email;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_banned")
    private boolean isBanned;

    public user() {}

    // ===== GETTERS =====

    public Long getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

     public boolean isBanned() {
        return isBanned;
    }

    // ===== SETTERS =====

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }
}