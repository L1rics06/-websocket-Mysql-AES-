package Mysql.Chattingneed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ChatConnect {
	private static final String URL = "jdbc:mysql://localhost:3306/chatdata";
	private static final String USER = "root";
	private static final String PASSWORD = "123456";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}