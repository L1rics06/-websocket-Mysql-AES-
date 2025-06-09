package Client;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatWindow extends JPanel implements ActionListener {
    private final ClientWebsocket mainClient;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JButton sendButton;
    private static final String key = "1234567890asdfgh"; // 聊天室公钥
    private static final String iv = "1234567890asdfgh";  // CBC 模式的初始随机量

    public GroupChatWindow(ClientWebsocket mainClient) {
        this.mainClient = mainClient;
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        add(chatScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.addActionListener(this);
        messageField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("发送");
        sendButton.addActionListener(this);
        sendButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        sendButton.setBackground(new Color(52, 152, 219));
        sendButton.setForeground(Color.WHITE);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // 显示历史消息
        //loadHistoryMessages(clientUser.getUsername());
    }

//     private void loadHistoryMessages(String username) {
//        String sql = "SELECT name, content, time FROM history ORDER BY time";
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatdata", "root", "123456");
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {

//            while (rs.next()) {
//                try {
//                    String encodedSender = rs.getString("name");
//                    String encodedContent = rs.getString("content");
//                    String time = rs.getString("time");
//
//                    // Base64 解码
//                    byte[] senderBytes = Base64.getDecoder().decode(encodedSender);
//                    byte[] contentBytes = Base64.getDecoder().decode(encodedContent);
//
//                    // AES 解密
//                    String sender = AES.aesCbcDecryptWrapper(senderBytes, key, iv);
//                    String content = AES.aesCbcDecryptWrapper(contentBytes, key, iv);
//
//                    // 区分自己和他人的消息
//                    boolean isSelf = sender.equals(username);
//                    appendMessage(sender, content, isSelf, time);
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    appendSystemMessage("某条历史消息解密失败: " + ex.getMessage());
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            appendSystemMessage("加载历史消息失败: " + e.getMessage());
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton || e.getSource() == messageField) {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                Client client = mainClient.getClientUser();

                // 发送消息到服务器
                JSONObject json = new JSONObject();
                json.put("category", "message");
                json.put("To", "all");
                json.put("type","group");
                json.put("from", client.getNickname());
                json.put("text", message);
                json.put("userId",client.getUsername());
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                json.put("time",time);
                mainClient.sendMessage(json.toString()); // 使用公共方法发送消息

               // 添加到本地聊天区域

//                appendMessage(client.getUsername(), message, true, time);

                // 清空输入框
                messageField.setText("");

                // 保存到数据库
            }
        }
    }

    public void appendMessage(String sender, String content,String time) {
        appendMessage(sender, content, false, time);
    }

    public void appendMessage(String sender, String content, boolean isSelf) {
        appendMessage(sender, content, isSelf, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public void appendMessage(String sender, String content, boolean isSelf, String time) {
        SwingUtilities.invokeLater(() -> {
            Color textColor = isSelf ? Color.BLUE : Color.BLACK;

            if (content.startsWith("[文件]")) {
                String filename = content.substring(4).trim();
                chatArea.setForeground(textColor);
                chatArea.append(time + " " + sender + ": " + content + "\n");

                // 添加下载按钮
                JButton downloadButton = new JButton("下载文件: " + filename);
                downloadButton.addActionListener(e -> {
                    mainClient.downloadFile(filename);
                });

                // 创建一个面板来容纳按钮
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(downloadButton);
                add(buttonPanel, BorderLayout.SOUTH);
                revalidate();
                repaint();
            } else {
                chatArea.setForeground(textColor);
                chatArea.append(time + " " + sender + ": " + content + "\n");
            }

            // 滚动到底部
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void appendSystemMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.setForeground(Color.RED);
            chatArea.append("系统: " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
}