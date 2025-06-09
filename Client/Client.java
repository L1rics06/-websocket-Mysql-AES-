package Client;

import java.io.Serializable;

public class Client implements Serializable {
    private final String username;
    private final String password;
    private String email;
    private String avatarPath;
    private String signature;
    private String nickname;

    // 基础构造函数
    public Client(String username, String password) {
        this.username = username;
        this.password = password;
        this.nickname = username;
    }

    // 完整构造函数
    public Client(String username, String password, String email, String avatarPath,
                  String signature, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatarPath = avatarPath;
        this.signature = signature;
        this.nickname = nickname;
    }

    // Getter和Setter
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getAvatarPath() { return avatarPath; }
    public String getSignature() { return signature; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
    public void setSignature(String signature) { this.signature = signature; }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}