package rent189.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import rent189.dao.AdminDao;
import rent189.dao.AdminDaoImpl;
import rent189.model.AdminBean;

import java.io.IOException;

@WebServlet("/UpdateAdminDataServlet")
public class UpdateAdminDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		AdminDao adminDao = new AdminDaoImpl();
		AdminBean admin = (AdminBean) session.getAttribute("admin");

		if (admin != null) {
			int adminId = admin.getAdminId();
			String adminName = request.getParameter("adminName");
			String adminEmail = request.getParameter("adminEmail");
			String adminPassword = request.getParameter("adminPassword");
			String adminPhone = request.getParameter("adminPhone");

			admin.setAdminId(adminId);
			admin.setAdminName(adminName);
			admin.setAdminEmail(adminEmail);
			admin.setAdminPassword(adminPassword);
			admin.setAdminPhone(adminPhone);
			
			
			boolean updateConfirm = adminDao.updateAdmin(admin);
			request.setAttribute("updateConfirm", updateConfirm);
			request.getRequestDispatcher("backstage-panel.jsp").forward(request, response);
			
		} else {
			response.sendRedirect("backstage-login.jsp");
		}

	}
}
