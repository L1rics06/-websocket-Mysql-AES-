package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

public class ProfileViewDialog extends JDialog implements ActionListener {
    private final Client client;
    private final JButton closeButton;

    public ProfileViewDialog(JFrame parent, Client client) {
        super(parent, "查看资料", true);
        this.client = client;
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        // 面板设置
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 头像区域
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon avatarIcon = loadAvatar(client.getAvatarPath());
        JLabel avatarLabel = new JLabel(avatarIcon);
        avatarPanel.add(avatarLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(avatarPanel, gbc);

        // 用户名
        JLabel usernameLabel = new JLabel("用户名:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(usernameLabel, gbc);

        JLabel usernameValueLabel = new JLabel(client.getUsername());
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(usernameValueLabel, gbc);

        // 昵称
        JLabel nicknameLabel = new JLabel("昵称:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(nicknameLabel, gbc);

        JLabel nicknameValueLabel = new JLabel(client.getNickname());
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(nicknameValueLabel, gbc);

        // 邮箱
        JLabel emailLabel = new JLabel("邮箱:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(emailLabel, gbc);

        JLabel emailValueLabel = new JLabel(client.getEmail());
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(emailValueLabel, gbc);

        // 个性签名
        JLabel signatureLabel = new JLabel("个性签名:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(signatureLabel, gbc);

        JLabel signatureValueLabel = new JLabel(client.getSignature());
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(signatureValueLabel, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButton = new JButton("关闭");
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private ImageIcon loadAvatar(String path) {
        try {
            if (path != null && !path.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    Image img = ImageIO.read(file);
                    Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 默认头像
        return new ImageIcon(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            dispose();
        }
    }
}