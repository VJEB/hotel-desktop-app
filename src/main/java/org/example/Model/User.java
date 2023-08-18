package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private byte[] user_id;

    private String username;

    private String user_password;

    private boolean is_admin;

    public User(String username, String user_password, boolean is_admin) {
        this.username = username;
        this.user_password = user_password;
        this.is_admin = is_admin;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
    public boolean getIsAdmin() {
        return is_admin;
    }
    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
}
