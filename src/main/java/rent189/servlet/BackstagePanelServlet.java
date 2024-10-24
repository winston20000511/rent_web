package rent189.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import rent189.model.AdminBean;

@WebServlet("/BackstagePanelServlet")
public class BackstagePanelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession(false);
		AdminBean admin = (AdminBean) session.getAttribute("admin");
		if (admin != null) {
            // 將管理員名稱寫入 request，供 JSP 使用
            request.setAttribute("adminName", admin.getAdminName());

            // 轉發到 JSP 頁面
            request.getRequestDispatcher("backstage-panel.jsp").forward(request, response);
        } else {
            // 如果 session 中沒有 admin，重定向到登入頁面
        	request.getRequestDispatcher("backstage-login.jsp").forward(request, response);
        }
		
	}
}