package Mysql.Method;

import Mysql.Chattingneed.ChatConnect;
import Client.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Searchinfo {
	// 验证用户信息
	public static boolean searchinfo(String name, int password) {
		String sql = "SELECT na FROM clientinfo WHERE name = ? AND password = ?";

		try (Connection conn = ChatConnect.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, name);
			pstmt.setInt(2, password);

			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 获取用户信息
	public static Client getClientInfo(String name) {
		String sql = "SELECT * FROM clientinfo WHERE name = ?";

		try (Connection conn = ChatConnect.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, name);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String username = rs.getString("name");
					String email = rs.getString("email");
					String avatar = rs.getString("avatar");
					String signature = rs.getString("signature");
					String nickname = rs.getString("nickname");

					return new Client(username, "", email, avatar, signature, nickname);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}