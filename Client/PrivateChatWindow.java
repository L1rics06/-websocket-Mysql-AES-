package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject; // 添加缺失的导入

public class PrivateChatWindow extends JFrame implements ActionListener {
    private final ClientWebsocket mainClient;
    private final String targetUser;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JButton sendButton;

    public PrivateChatWindow(ClientWebsocket mainClient, String targetUser) {
        super("私聊: " + targetUser);
        this.mainClient = mainClient;
        this.targetUser = targetUser;
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton || e.getSource() == messageField) {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                Client client = mainClient.getClientUser();

                // 发送私聊消息到服务器
                JSONObject json = new JSONObject();
                json.put("category", "message");
                json.put("type", "private");
                json.put("from", client.getUsername());
                json.put("To", targetUser);
                json.put("text", message);
                json.put("userId",client.getUsername());
                mainClient.sendMessage(json.toString()); // 使用公共方法发送消息

                // 添加到本地聊天区域
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                appendMessage(client.getUsername(), message, true, time);

                // 清空输入框
                messageField.setText("");
            }
        }
    }

    public void appendMessage(String sender, String content, boolean isSelf) {
        appendMessage(sender, content, isSelf, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public void appendMessage(String sender, String content, boolean isSelf, String time) {
        SwingUtilities.invokeLater(() -> {
            Color textColor = isSelf ? Color.BLUE : Color.BLACK;
            chatArea.setForeground(textColor);
            chatArea.append(time + " " + sender + ": " + content + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
}