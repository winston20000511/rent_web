package servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import Bean.AdminBean;
import Dao.AdminDao;
import IMPL.AdminDaoImpl;

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

	    AdminBean admin = adminDao.checkAccount(email);
	    if (admin != null && BCrypt.checkpw(password, admin.getAdminPassword())) {
	        HttpSession session = request.getSession(true);
	        session.setAttribute("admin", admin);
	        session.setAttribute("role", admin.getRole());
	        session.setMaxInactiveInterval(600);

	        response.sendRedirect("backstage-panel.jsp");
	    } else {
	        request.setAttribute("loginError", "無效的 Email 或密碼");
	        request.getRequestDispatcher("backstage-login.jsp").forward(request, response);
	    }
	}
}
