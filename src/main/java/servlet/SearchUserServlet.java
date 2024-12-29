package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.gson.Gson;

import Bean.UserTableBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;

@WebServlet("/SearchUserServlet.do")
public class SearchUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchType = request.getParameter("searchType"); // 取得搜尋類型
        String searchInput = request.getParameter("searchInput"); // 取得搜尋輸入
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            List<UserTableBean> users = new ArrayList<>();
            Transaction transaction = null;

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                Query<UserTableBean> query;

                if (searchInput == null || searchInput.isEmpty()) {
                    // 階段一：未輸入任何搜尋條件，顯示所有用戶資料並按創建時間降序排序
                    query = session.createQuery("FROM UserBean2 ORDER BY createtime DESC", UserTableBean.class);
                } else {
                    // 階段二、三：根據搜尋條件顯示符合的資料
                    if ("userId".equals(searchType)) {
                        query = session.createQuery("FROM UserBean2 WHERE userId = :userId", UserTableBean.class);
                        query.setParameter("userId", Integer.parseInt(searchInput));
                    } else if ("userName".equals(searchType)) {
                        query = session.createQuery("FROM UserBean2 WHERE name LIKE :name", UserTableBean.class);
                        query.setParameter("name", searchInput + "%");
                    } else { // 預設為 email 搜尋
                        query = session.createQuery("FROM UserBean2 WHERE email LIKE :email", UserTableBean.class);
                        query.setParameter("email", searchInput + "%");
                    }
                }

                users = query.getResultList();
                transaction.commit();

                // 將查詢結果轉為 JSON 格式
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(users);
                out.print(jsonResponse);
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}