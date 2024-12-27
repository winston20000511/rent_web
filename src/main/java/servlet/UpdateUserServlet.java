package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Bean.UserTableBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;

@WebServlet("/UpdateUserServlet.do")
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        
        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        
        
        // 從請求中獲取用戶資料
        long userId = Long.parseLong(jsonObject.get("userId").getAsString());
        String name = jsonObject.get("name").getAsString();
        String email = jsonObject.get("email").getAsString();
        String password = jsonObject.get("password").getAsString();
        String phone = jsonObject.get("phone").getAsString();
        Byte gender = jsonObject.get("gender").getAsByte();
        Byte status = jsonObject.get("status").getAsByte();

        String jsonResponse;
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("transaction:" + transaction);
            
            UserTableBean user = session.get(UserTableBean.class, userId); // 根據 userId 獲取用戶實體

            if (user != null) {
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setPhone(phone);
                user.setGender(gender);
                user.setStatus(status);
                session.update(user); // 更新用戶資料

                transaction.commit();
                jsonResponse = "{\"status\":\"success\", \"message\":\"用戶資料更新成功\"}";
            } else {
                jsonResponse = "{\"status\":\"failure\", \"message\":\"用戶未找到\"}";
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            jsonResponse = "{\"status\":\"error\", \"message\":\"資料更新失敗\"}";
        }


        // 輸出 JSON 回應
        out.print(jsonResponse);
        out.close();
    }
    
}