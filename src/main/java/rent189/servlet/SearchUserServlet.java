package rent189.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.config.ConnectionUtil;

@WebServlet("/SearchUserServlet.do")
public class SearchUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("searchInput");
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            try (Connection conn =ConnectionUtil.getConnection()) {
                String sql = "SELECT * FROM user_table WHERE email=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    // 構建 JSON 輸出
                    out.write("{");
                    out.write("\"user_id\":" + rs.getInt("user_id") + ",");
                    out.write("\"name\":\"" + rs.getString("name") + "\",");
                    out.write("\"email\":\"" + rs.getString("email") + "\",");
                    out.write("\"password\":\"" + rs.getString("password") + "\",");
                    out.write("\"phone\":\"" + rs.getString("phone") + "\",");
                    out.write("\"picture\":\"/path/to/image.jpg\","); // 替換為真實圖片路徑
                    out.write("\"createtime\":\"" + rs.getTimestamp("createtime") + "\",");
                    out.write("\"gender\":" + rs.getInt("gender"));
                    out.write("}");
                } else {
                    out.write("{}"); // 如果查無資料返回空 JSON
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}