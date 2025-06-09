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

public class ProfileDialog extends JDialog implements ActionListener {
    private final Client client;
    private final JTextField nicknameField;
    private final JTextField emailField;
    private final JTextField signatureField;
    private final JTextField avatarPathField;
    private final JButton saveButton;
    private final JButton cancelButton;
    private final JButton browseButton;
    private final JLabel avatarLabel;
    private ImageIcon avatarIcon;

    public ProfileDialog(JFrame parent, Client client) {
        super(parent, "个人资料", true);
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
        avatarIcon = loadAvatar(client.getAvatarPath());
        avatarLabel = new JLabel(avatarIcon);
        avatarPanel.add(avatarLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(avatarPanel, gbc);

        // 浏览按钮
        browseButton = new JButton("选择头像");
        browseButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        contentPanel.add(browseButton, gbc);

        // 昵称
        JLabel nicknameLabel = new JLabel("昵称:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(nicknameLabel, gbc);

        nicknameField = new JTextField(client.getNickname());
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(nicknameField, gbc);

        // 邮箱
        JLabel emailLabel = new JLabel("邮箱:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(emailLabel, gbc);

        emailField = new JTextField(client.getEmail());
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(emailField, gbc);

        // 个性签名
        JLabel signatureLabel = new JLabel("个性签名:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(signatureLabel, gbc);

        signatureField = new JTextField(client.getSignature());
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(signatureField, gbc);

        // 头像路径（隐藏）
        avatarPathField = new JTextField(client.getAvatarPath());
        avatarPathField.setVisible(false);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("保存");
        saveButton.addActionListener(this);
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

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
        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") ||
                            f.getName().toLowerCase().endsWith(".png") ||
                            f.getName().toLowerCase().endsWith(".jpeg");
                }

                @Override
                public String getDescription() {
                    return "图片文件 (*.jpg, *.png, *.jpeg)";
                }
            });

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                avatarPathField.setText(selectedFile.getAbsolutePath());
                avatarIcon = loadAvatar(selectedFile.getAbsolutePath());
                avatarLabel.setIcon(avatarIcon);
                repaint();
            }
        } else if (e.getSource() == saveButton) {
            String email = emailField.getText().trim();
            String signature = signatureField.getText().trim();
            String avatarPath = avatarPathField.getText().trim();


            // 更新客户端信息
            client.setEmail(email);
            client.setSignature(signature);
            client.setAvatarPath(avatarPath);

            // 更新数据库
            Mysql.Method.Insert.updateClientInfo(client);

            JOptionPane.showMessageDialog(this, "资料更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}