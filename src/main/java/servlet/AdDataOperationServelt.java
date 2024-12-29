package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

@WebServlet("/AdDataOperationServelt.do")
public class AdDataOperationServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AdDataOperationServelt.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// set up the response format
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setContentType("application/json;charset=UTF-8");

		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session = factory.getCurrentSession();

		try (PrintWriter out = response.getWriter(); BufferedReader reader = request.getReader();) {
			System.out.println("connect to the AdDataOperationServlet");

			session.beginTransaction();

			// read the json data from client request
			StringBuilder requestJson = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				requestJson.append(line);
			}

			// debugging
			System.out.println("request Json: " + requestJson.toString());

			// register custom JsonSerializer for ZonedDateTime
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateAdapter()).create();
			Type listType = new TypeToken<List<String>>() {
			}.getType();
			List<String> receivedData = gson.fromJson(requestJson.toString(), listType);

			AdService adService = new AdService(session);
			String jsonResponse = null;

			switch (receivedData.get(0)) {
			case "search":
				List<AdDetailResponseDTO> adList = adService.getFilteredAds(receivedData);
				logger.info("ad list: " + adList.toString());
				jsonResponse = gson.toJson(adList); // serialize
				break;
			case "adDetails":
				AdDetailResponseDTO adView = adService.getAdDetails(receivedData);
				jsonResponse = gson.toJson(adView);
				break;
			case "adUpdate":
				adView = adService.updateAdDetails(receivedData);
				jsonResponse = gson.toJson(adView);
				System.out.println("訂單已修改");
				break;
			case "adDelete":
				try {
					adService.deleteAd(receivedData);
					jsonResponse = gson.toJson("訂單已刪除");
				} catch (Exception e) {
					jsonResponse = gson.toJson("刪除失敗：" + e.getMessage());
				}
				break;
			}

			session.getTransaction().commit();

			System.out.println("json response:" + jsonResponse);
			out.write(jsonResponse);

			out.flush();

		} catch (Exception exception) {
			session.getTransaction().rollback();
			exception.printStackTrace();
		}
	}
}