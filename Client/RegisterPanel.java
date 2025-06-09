package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JFrame implements ActionListener {
    private static final String key = "1234567890asdfgh"; // 聊天室公钥
    private static final String iv = "1234567890asdfgh";  // CBC 模式的初始随机量
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JTextField emailField;
    private final JButton registerButton;
    private final JButton backButton;


    public RegisterPanel() {
        super("聊天客户端 - 注册");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("注册新账号");
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

        // 确认密码
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(confirmPasswordField, gbc);

        // 邮箱
        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(emailField, gbc);

        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.addActionListener(this);
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(registerButton, gbc);

        // 返回按钮
        backButton = new JButton("返回");
        backButton.addActionListener(this);
        backButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        backButton.setBackground(new Color(52, 152, 219));
        backButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(backButton, gbc);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterPanel::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String email = emailField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名、密码和邮箱不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "两次输入的密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 对密码进行 AES 加密并使用 Base64 编码


                // 调用数据库插入方法
                boolean success = Mysql.Method.Insert.insertinfo(
                        username,
                        ' ',          // 昵称
                        0,            // 性别
                        password, // 加密后的密码
                        email,
                        "../images/avatar1.png",         // 头像（可设为 null）
                        "  "           // 个性签名
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "注册成功，请登录", "成功", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    new LoginPanel().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "注册失败，用户名可能已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "注册失败：加密过程出错", "错误", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == backButton) {
            setVisible(false);
            new LoginPanel().setVisible(true);
        }
    }
}