package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;

import IMPL.AdDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AdAnalysisServlet.do")
public class AdAnalysisServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            AdDaoImpl adDao = new AdDaoImpl();  // 不再需要 Connection
            Map<String, Integer> adData = adDao.getadtypeCount();

            String jsonResponse = new Gson().toJson(adData);
            System.out.println(jsonResponse);

            out.write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
