package Mysql.Method;

import Mysql.Chattingneed.ChatConnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Insertimage {
    // 上传图片到数据库
    public static boolean uploadImage(String imagePath, String imageName) {
        String sql = "INSERT INTO images (image_name, image_data) VALUES (?, ?)";

        try (Connection conn = ChatConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            File file = new File(imagePath);
            try (FileInputStream fis = new FileInputStream(file)) {
                pstmt.setString(1, imageName);
                pstmt.setBinaryStream(2, fis, (int) file.length());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }




    // 根据图片名称下载图片
    public static boolean downloadImageByName(String imageName) {
        String sql = "SELECT image_data FROM images WHERE image_name = ?";

        try (Connection conn = ChatConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imageName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    byte[] imageData = rs.getBytes("image_data");

                    // 选择保存位置
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File(imageName));
                    int result = fileChooser.showSaveDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File saveFile = fileChooser.getSelectedFile();
                        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                            fos.write(imageData);
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "下载文件时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}