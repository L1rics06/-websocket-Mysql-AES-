package Mysql.Method;

import Mysql.Chattingneed.ChatConnect;
import Client.*;
import cryptonomicon.AES;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Insert {
	private static final String key = "1234567890asdfgh"; // 聊天室公钥
	private static final String iv = "1234567890asdfgh";  // CBC 模式的初始随机量

	// 插入用户信息
	public static boolean insertinfo(String name, char gender, int age, String password, String email, String avatar, String signature) {
		String sql = "INSERT INTO clientinfo (name, gender, age, password, email, avatar, signature) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = ChatConnect.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// 1) AES 加密
			byte[] pwdBytes = AES.aesCbcEncryptWrapper(password, key, iv);
			byte[] emailBytes = AES.aesCbcEncryptWrapper(email, key, iv);

			// 2) Base64 编码
			String encodedPwd = Base64.getEncoder().encodeToString(pwdBytes);
			String encodedEmail = Base64.getEncoder().encodeToString(emailBytes);

			pstmt.setString(1, name);
			pstmt.setString(2, String.valueOf(gender));
			pstmt.setInt(3, age);
			pstmt.setString(4, encodedPwd);
			pstmt.setString(5, encodedEmail);
			pstmt.setString(6, avatar);
			pstmt.setString(7, signature);

			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 插入聊天记录
	public static void insertdialogue(String name, String content) {
		String sql = "INSERT INTO history (name, content, time) VALUES (?, ?, ?)";

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedTime = now.format(formatter);

		try (Connection conn = ChatConnect.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// AES 加密并 Base64 编码
			byte[] nameBytes = AES.aesCbcEncryptWrapper(name, key, iv);
			byte[] contentBytes = AES.aesCbcEncryptWrapper(content, key, iv);
			String encodedName = Base64.getEncoder().encodeToString(nameBytes);
			String encodedContent = Base64.getEncoder().encodeToString(contentBytes);

			pstmt.setString(1, encodedName);
			pstmt.setString(2, encodedContent);
			pstmt.setString(3, formattedTime);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更新用户信息
	public static void updateClientInfo(Client client) {
		String sql = "UPDATE clientinfo SET nickname = ?, email = ?, signature = ?, avatar = ? WHERE name = ?";

		try (Connection conn = ChatConnect.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// 对昵称和邮箱进行 AES 加密并 Base64 编码
			byte[] nickBytes = AES.aesCbcEncryptWrapper(client.getNickname(), key, iv);
			byte[] emailBytes = AES.aesCbcEncryptWrapper(client.getEmail(), key, iv);
			String encodedNick = Base64.getEncoder().encodeToString(nickBytes);
			String encodedEmail = Base64.getEncoder().encodeToString(emailBytes);

			pstmt.setString(1, encodedNick);
			pstmt.setString(2, encodedEmail);
			pstmt.setString(3, client.getSignature());
			pstmt.setString(4, client.getAvatarPath());
			pstmt.setString(5, client.getUsername());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
