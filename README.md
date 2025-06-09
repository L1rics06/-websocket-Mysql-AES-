#🔐 双端加密聊天室项目
本项目是一个基于 Java 的双端加密聊天室，使用了 java.org.websocket 库、MySQL 数据库 和 AES 加密算法 实现。支持注册登录、私聊群聊、消息加密存储等功能。

🚀 项目技术栈
Java & Swing（客户端）

HTML + JavaScript（网页端）

WebSocket（org.java_websocket）

MySQL

AES 加密算法

✨ 主要功能
✅ 用户注册与登录

🔄 Web 端与本地客户端互通

💬 私聊与群发消息

💾 消息加密存储与读取

🔐 敏感数据 AES 加密

🛠️ 部署方法
导入项目文件，并引入 libs 文件夹中的第三方 JAR 库。

数据库初始化：

运行项目根目录下的 sql.script 文件，创建并初始化数据库。

本项目默认使用 MySQL 数据库，账号为 root，密码为 123456，默认端口。

启动服务端：

启动 Server 包中的 ServerWebsocket.java。

WebSocket 默认监听端口为：8887。

启动客户端：

网页端：运行 html 文件夹中的 main.html。

本地客户端：运行 Client 包中的 LoginPanel.java。

🔐 关于 AES 加密
使用 AES 算法对数据库中的敏感数据进行加密存储。

默认加密密钥（Key）与偏移量（IV）均为：1234567890asdfgh。

📽️ 项目演示视频
👉 点击查看 Bilibili 演示视频

