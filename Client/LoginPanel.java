package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPanel extends JFrame implements ActionListener {
    private static final String key = "1234567890asdfgh"; // 聊天室公钥
    private static final String iv = "1234567890asdfgh";  // CBC 模式的初始随机量
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;


    public LoginPanel() {
        super("聊天客户端 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("聊天客户端登录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 用户名
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        // 密码
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.addActionListener(this);
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(loginButton, gbc);

        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.addActionListener(this);
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(registerButton, gbc);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPanel::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "用户名或密码不能为空",
                        "错误",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            new ClientWebsocket("ws://localhost:8887", new Client(username, password));
            this.setVisible(false);
        } else if (e.getSource() == registerButton) {
            this.setVisible(false);
            new RegisterPanel().setVisible(true);
        }
    }

    private boolean validateUser(String username, String encryptedPasswordBase64) {
        String sql = "SELECT password FROM clientinfo WHERE name = ? LIMIT 1";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatdata", "root", "123456");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password"); // 数据库中加密后的 Base64 密码
                    return storedPassword.equals(encryptedPasswordBase64);
                } else {
                    return false; // 用户名不存在
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}