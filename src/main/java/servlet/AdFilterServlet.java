package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Dao.AdService;
import dto.AdDetailResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;
import util.ZonedDateAdapter;

@WebServlet("/AdFilterServlet.do")
public class AdFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AdFilterServlet.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setContentType("application/json;charset=UTF-8");

		Session session = null;

		try (PrintWriter out = response.getWriter(); BufferedReader reader = request.getReader();) {

			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			StringBuilder requestJson = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				requestJson.append(line);
			}

			logger.info("requestJson order update/cancel:" + requestJson.toString());
			// {"condition":"","orderStatus":"","input":""}
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateAdapter()).create();
			Map<String, Object> filterParams = gson.fromJson(requestJson.toString(), new TypeToken<Map<String, Object>>() {}.getType());
			logger.info("received data: " + filterParams);
			
			AdService adService = new AdService(session);
			String jsonResponse = null;

			// Debugging
			logger.info("Parsed filterParams: " + filterParams);

			// 調用orderService的方法
			List<AdDetailResponseDTO> ads = adService.filterAds(filterParams);
			logger.info("拿出來的ads們: " + ads);
			
			jsonResponse = gson.toJson(ads);
			
			out.write(jsonResponse);
			session.getTransaction().commit();

		} catch (Exception exception) {
			session.getTransaction().rollback();
			exception.printStackTrace();
		} finally {
			session.close();
		}
	}

}
