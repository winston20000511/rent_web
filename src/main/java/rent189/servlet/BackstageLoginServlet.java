package rent189.servlet;

import jakarta.servlet.ServletConfig;
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

@WebServlet("/BackstageLoginServlet")
public class BackstageLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AdminDao adminDao;

	@Override
	public void init(ServletConfig config) throws ServletException {
		adminDao = new AdminDaoImpl();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		String email = request.getParameter("userEmail");
		String password = request.getParameter("password");

		AdminBean admin = adminDao.confirmAccount(email, password);
		if (admin != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("admin", admin);
			session.setMaxInactiveInterval(600);

			response.sendRedirect("backstage-panel.jsp");
		} else {
			request.setAttribute("loginError", "無效的 Email 或密碼");
			request.getRequestDispatcher("backstage-login.jsp").forward(request, response);
		}
	}
}
