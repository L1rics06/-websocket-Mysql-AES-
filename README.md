# 🔐 双端加密聊天室项目

本项目是一个基于 Java 的双端加密聊天室，使用了 `org.java_websocket` 库、MySQL 数据库和 AES 加密算法实现。支持注册登录、私聊群聊、消息加密存储等功能。

## 🚀 技术栈

- Java & Swing（客户端）  
- HTML + JavaScript（网页端）  
- WebSocket（`org.java_websocket`）  
- MySQL  
- AES 加密算法  

## ✨ 主要功能

- ✅ 用户注册与登录  
- 🔄 Web 端与本地客户端互通  
- 💬 私聊与群发消息  
- 💾 消息加密存储与读取  
- 🔐 敏感数据 AES 加密  

## 🛠️ 部署方法

1. **导入项目文件**，并将 `libs` 文件夹中的第三方 JAR 包添加到项目依赖。  
2. **初始化数据库**  
   - 运行根目录下的 `sql.script`，创建并初始化所需表结构。  
   - 默认使用 MySQL，用户名：`root`，密码：`123456`，端口：`3306`。  
3. **启动服务端**  
   - 运行 `Server` 包中的 `ServerWebsocket.java`。  
   - WebSocket 默认监听端口：`8887`。  
4. **启动客户端**  
   - **网页端**：打开 `html/main.html`。  
   - **本地端**：运行 `Client` 包中的 `LoginPanel.java`。  

## 🔐 关于 AES 加密

- 对数据库中敏感字段进行 AES 加密存储。  
- 默认密钥（Key）与偏移量（IV）：`1234567890asdfgh`。  

## 📽️ 项目演示视频

[![项目演示](https://i0.hdslb.com/bfs/archive/XXX.jpg)](https://www.bilibili.com/video/BV1oBT2ztEbh/)  
点击查看 Bilibili 演示视频：https://www.bilibili.com/video/BV1oBT2ztEbh/
