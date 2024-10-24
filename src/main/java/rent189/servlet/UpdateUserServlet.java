package rent189.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.config.ConnectionUtil;

@WebServlet("/UpdateUserServlet.do")
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // 獲取請求的 JSON 數據
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            // 解析 JSON 數據
            Gson gson = new Gson();
            User user = gson.fromJson(sb.toString(), User.class);

            try (Connection conn = ConnectionUtil.getConnection()) {
                String sql = "UPDATE user_table SET name=?, email=?, password=?, phone=? WHERE user_id=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getPhone());
                statement.setInt(5, user.getUserId());
                
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    out.print("{\"message\": \"更新成功！\"}");
                } else {
                    out.print("{\"message\": \"更新失敗，請檢查用戶ID！\"}");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.print("{\"message\": \"更新過程中發生錯誤！\"}");
            }
        }
    }

    // 用戶類
    private class User {
        private int userId;
        private String name;
        private String email;
        private String password;
        private String phone;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}