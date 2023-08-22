package org.example.Model;

import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "is_admin")
    private boolean is_admin;

    public User(){}
    public User(String username, String user_password, boolean is_admin) {
        this.username = username.toLowerCase();
        this.user_password = encryptUserPassword(user_password);
        this.is_admin = is_admin;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUser_password(){return user_password;}
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
    public boolean getIsAdmin() {
        return is_admin;
    }
    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    private static byte[] generateMessageDigest(String user_password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(user_password.getBytes());
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MessageDigest algorithm not available", e);
        }
    }

    public static String encryptUserPassword(String user_password) {
            byte[] resultByteArray = generateMessageDigest(user_password);
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : resultByteArray) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
    }
}
