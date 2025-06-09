package Client;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ClientWebsocket extends JFrame implements ActionListener {
    private final Client clientUser;
    private final GroupChatWindow groupChatWindow;
    private final Map<String, PrivateChatWindow> privateChatWindows = new HashMap<>();
    private final JButton groupChatButton;
    private final JButton profileButton;
    private final JButton sendFileButton;
    private final JList<String> onlineUserList;
    private final DefaultListModel<String> onlineUserModel;
    private final JPanel mainPanel;
    private final JPanel userListPanel;
    private WebSocketClient webSocketClient;

    public ClientWebsocket(String serverUri, Client clientUser) {
        super("聊天客户端 - " + clientUser.getNickname());
        this.clientUser = clientUser;
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顶部工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profileButton = new JButton("个人资料");
        profileButton.addActionListener(this);
        profileButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        profileButton.setBackground(new Color(52, 152, 219));
        profileButton.setForeground(Color.WHITE);
        toolBar.add(profileButton);

        sendFileButton = new JButton("发送文件");
        sendFileButton.addActionListener(this);
        sendFileButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        sendFileButton.setBackground(new Color(52, 152, 219));
        sendFileButton.setForeground(Color.WHITE);
        toolBar.add(sendFileButton);

        add(toolBar, BorderLayout.NORTH);

        // 主面板和群聊窗口
        mainPanel = new JPanel(new BorderLayout());
        groupChatWindow = new GroupChatWindow(this);
        groupChatWindow.setVisible(true);
        mainPanel.add(groupChatWindow, BorderLayout.CENTER);

        // 在线用户面板
        userListPanel = new JPanel(new BorderLayout());
        userListPanel.setPreferredSize(new Dimension(200, 100));

        groupChatButton = new JButton("群聊");
        groupChatButton.addActionListener(this);
        groupChatButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        groupChatButton.setBackground(new Color(52, 152, 219));
        groupChatButton.setForeground(Color.WHITE);

        JPanel userListHeader = new JPanel(new BorderLayout());
        userListHeader.add(groupChatButton, BorderLayout.NORTH);

        // 在线用户列表
        onlineUserModel = new DefaultListModel<>();
        onlineUserList = new JList<>(onlineUserModel);
        onlineUserList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        onlineUserList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "在线用户",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("微软雅黑", Font.BOLD, 13)
        ));
        onlineUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineUserList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String user = onlineUserList.getSelectedValue();
                    if (user != null && !user.equals(clientUser.getUsername())) {
                        openPrivateChat(user);
                    }
                }
            }
        });

        JScrollPane userListScrollPane = new JScrollPane(onlineUserList);
        userListPanel.add(userListHeader, BorderLayout.NORTH);
        userListPanel.add(userListScrollPane, BorderLayout.CENTER);

        // 分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, userListPanel);
        splitPane.setDividerLocation(800);
        splitPane.setResizeWeight(0.8);
        add(splitPane, BorderLayout.CENTER);

        // 连接服务器
        connectToServer(serverUri);
        setVisible(true);
    }

    public Client getClientUser() {
        return clientUser;
    }

    private void connectToServer(String serverUri) {
        try {
            webSocketClient = new WebSocketClient(new URI(serverUri)) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    SwingUtilities.invokeLater(() -> {
                        groupChatWindow.appendSystemMessage("已连接到服务器");
                        sendLoginMessage();
                    });
                }

                @Override
                public void onMessage(String message) {
                    SwingUtilities.invokeLater(() -> handleMessage(message));
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    SwingUtilities.invokeLater(() -> {
                        groupChatWindow.appendSystemMessage("与服务器断开连接: " + reason);
                        onlineUserModel.clear();
                    });
                }

                @Override
                public void onError(Exception ex) {
                    SwingUtilities.invokeLater(() -> groupChatWindow.appendSystemMessage("连接错误: " + ex.getMessage()));
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            groupChatWindow.appendSystemMessage("无效的服务器地址: " + e.getMessage());
        } catch (Exception e) {
            groupChatWindow.appendSystemMessage("连接失败: " + e.getMessage());
        }
    }

    private void sendLoginMessage() {
        JSONObject json = new JSONObject();
        json.put("category", "login");
        json.put("userId", clientUser.getUsername());
        json.put("password", clientUser.getPassword());
        webSocketClient.send(json.toString());
    }

    private void handleMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            String category = json.optString("category");

            switch (category) {
                case "users":
                    updateOnlineUsers(json);
                    break;
                case "message":
                    handleIncomingMessage(json);
                    break;
                case "system":
                    groupChatWindow.appendSystemMessage(json.optString("text"));
                    break;
                case "error":
                    groupChatWindow.appendSystemMessage("错误: " + json.optString("text"));
                    break;
            }
        } catch (Exception e) {
            groupChatWindow.appendSystemMessage("消息解析错误: " + message);
            e.printStackTrace();
        }
    }

    private void updateOnlineUsers(JSONObject json) {
        JSONArray usersArray = json.optJSONArray("data");
        if (usersArray == null) {
            return;
        }

        // 假设 onlineUserModel 是一个 DefaultListModel<String>，用来填充 JList
        // 假设 clientUser.getUsername() 返回当前自己登录的 userId
        onlineUserModel.clear();

        // 用来保存列表中显示的“昵称” → 真正的 userId
        // 以便在用户从列表里双击昵称时，知道要给哪个 userId 发送私聊消息
// 视情况存到成员变量

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userObj = usersArray.getJSONObject(i);
            String userId = userObj.optString("userId");
            // 如果服务端没给 nickname，就退回到 userId
            String nickname = userObj.optString("nickname", userId);

            // 过滤掉自己
            if (userId.equals(clientUser.getUsername())) {
                continue;
            }

            // 将“昵称”加入列表模型，前端显示昵称
            onlineUserModel.addElement(nickname);

            // 如果你需要存 avatar/signature，也可以：
            // String avatar   = userObj.optString("avatar", null);
            // String signature = userObj.optString("signature", null);
            // 然后把它们放在另一个 Map<String, ClientInfo> 里缓存，后续点击用户时拿出来展示
        }
    }

    private void handleIncomingMessage(JSONObject json) {
        json.optString("To");
        String from = json.optString("from");
        String text = json.optString("text");
        String type = json.optString("type");
        String time = json.optString("time");

        if (!"private".equals(type)) {
            groupChatWindow.appendMessage(from, text, time);
        } else {
            PrivateChatWindow window = openPrivateChat(from);
            window.appendMessage(from, text, false); // 你这里没有传时间参数，如果需要，也可以改
        }
    }

    public PrivateChatWindow openPrivateChat(String username) {
        if (!privateChatWindows.containsKey(username)) {
            PrivateChatWindow window = new PrivateChatWindow(this, username);
            privateChatWindows.put(username, window);
            window.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    privateChatWindows.remove(username);
                }
            });
        }
        PrivateChatWindow window = privateChatWindows.get(username);
        window.setVisible(true);
        return window;
    }

    public void openProfile(String username) {
        if (username.equals(clientUser.getUsername())) {
            new ProfileDialog(this, clientUser).setVisible(true);
        } else {
            JSONObject json = new JSONObject();
            json.put("category", "userinfo");
            json.put("userId", username);
            sendMessage(json.toString()); // 使用公共方法发送消息
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == profileButton) {
            openProfile(clientUser.getUsername());
        } else if (e.getSource() == groupChatButton) {
            groupChatWindow.setVisible(true);
        } else if (e.getSource() == sendFileButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // 上传文件到数据库
                    boolean success = Mysql.Method.Insertimage.uploadImage(
                            selectedFile.getAbsolutePath(),
                            clientUser.getUsername() + "_" + selectedFile.getName()
                    );
                    if (success) {
                        try {
                            // 发送文件消息到服务器
                            JSONObject json = new JSONObject();
                            json.put("category", "message");
                            json.put("type", "group");
                            json.put("from", clientUser.getUsername());
                            json.put("text", "[文件] " + selectedFile.getName());
                            json.put("filename", selectedFile.getName());
                            sendMessage(json.toString()); // 使用公共方法发送消息
                            groupChatWindow.appendMessage(clientUser.getUsername(), "[文件] " + selectedFile.getName(), true);
                        } catch (Exception exception){
                            System.out.println("error:"+exception);
                        }
                    } else {
                        groupChatWindow.appendSystemMessage("文件上传失败");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    groupChatWindow.appendSystemMessage("文件上传出错: " + ex.getMessage());
                }
            }
        }
    }

    public void downloadFile(String filename) {
        try {
            boolean success = Mysql.Method.Insertimage.downloadImageByName(filename);
            if (success) {
                groupChatWindow.appendSystemMessage("文件 " + filename + " 下载成功");
            } else {
                groupChatWindow.appendSystemMessage("文件下载失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            groupChatWindow.appendSystemMessage("文件下载出错: " + ex.getMessage());
        }
    }

    // 添加公共方法用于发送消息
    public void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
        } else {
            groupChatWindow.appendSystemMessage("WebSocket连接未打开，无法发送消息");
        }
    }
}