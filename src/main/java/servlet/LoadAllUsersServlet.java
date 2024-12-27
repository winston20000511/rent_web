package servlet;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.google.gson.Gson;

import Bean.UserTableBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;

@WebServlet("/LoadAllUsersServlet.do")
public class LoadAllUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
	
		response.setCharacterEncoding("UTF-8");
		
		
		List<UserTableBean> users;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// 使用 HQL 查詢
			session.beginTransaction();
			Query<UserTableBean> query = session.createQuery("FROM UserBean2 ORDER BY createtime DESC", UserTableBean.class);
//			System.out.println(query);
			users = query.getResultList();
			// 將用戶資料轉換為 JSON 格式
//			System.out.println(users);
		
			
			String json = new Gson().toJson(users);
			
			
			response.getWriter().write(json);
//			System.out.println("json="+json);
			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
				} 
	}
}
