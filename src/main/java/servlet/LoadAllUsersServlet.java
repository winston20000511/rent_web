package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;

import com.google.gson.Gson;

import Bean.UserBean2;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/LoadAllUsersServlet.do")
public class LoadAllUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
	
		response.setCharacterEncoding("UTF-8");
		
		
		List<UserBean2> users;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			// 使用 HQL 查詢
			session.beginTransaction();
			Query<UserBean2> query = session.createQuery("FROM UserBean2 ORDER BY createtime DESC", UserBean2.class);
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
