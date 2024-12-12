package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Bean.AdminBean;
import Dao.AdminDao;
import IMPL.AdminDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RegisterServlet")
public class BackstageRegisterServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AdminDao adminDao = new AdminDaoImpl();
	
	
	@Override
	public void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect("backstage-register.jsp");
	}
	

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Arrived at RegisterServlet doPost");

        response.setContentType("application/json;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"success\":false,\"message\":\"無法解析請求\"}");
            }
            return;
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);

        String name = jsonObject.get("name").getAsString();
        String email = jsonObject.get("email").getAsString() ;
        String password = jsonObject.get("password").getAsString();
        String phone = jsonObject.get("adminPhone").getAsString();
        String role = jsonObject.get("role").getAsString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"success\":false,\"message\":\"缺少必填字段\"}");
            }
            return;
        }

        AdminBean newAdmin = adminDao.registerAccount(name, email, password, phone, role);

        try (PrintWriter out = response.getWriter()) {
            if (newAdmin != null) {
                out.write("{\"success\":true,\"message\":\"註冊成功\"}");
            } else {
                out.write("{\"success\":false,\"message\":\"註冊失敗，Email 可能已存在\"}");
            }
        }
    }

}