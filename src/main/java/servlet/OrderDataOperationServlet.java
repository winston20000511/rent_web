package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Dao.OrderService;
import dto.OrderDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;
import util.ZonedDateAdapter;

@WebServlet("/OrderDataOperationServlet.do")
public class OrderDataOperationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(OrderDataOperationServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}
	
	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setContentType("application/json;charset=UTF-8");
		
		Session session = null;
		
		try(PrintWriter out = response.getWriter();
			BufferedReader reader = request.getReader();){
			
			session= HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
		
			StringBuilder requestJson = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				requestJson.append(line);
			}
			
			// debugging
			logger.info("requestJson order update/cancel:" + requestJson.toString());
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateAdapter()).create();
			Type listType = new TypeToken<List<String>>() {}.getType();
			List<String> receivedData = gson.fromJson(requestJson.toString(), listType);
			
			OrderService orderService = new OrderService(session);
			String jsonResponse = null;
			
			switch(receivedData.get(0)) {
				case "search":
					logger.info("進入 search");
					List<OrderDetailsDTO> filteredOrders = orderService.getFilteredOrders(receivedData);
					jsonResponse = gson.toJson(filteredOrders); 
					break;
					
				case "orderDetails":
					OrderDetailsDTO orderDetails = orderService.getOrderDetailsByTradNo(receivedData);
				    jsonResponse = gson.toJson(orderDetails);
					break;
				
				case "cancelOrder":
					Map<String, Object> canceledOrder = orderService.cancelOrder(receivedData);
					jsonResponse = gson.toJson(canceledOrder); 
					break;
			
			}
			
			out.write(jsonResponse);
			session.getTransaction().commit();
			
		}catch(Exception exception) {
			session.getTransaction().rollback();
			exception.printStackTrace();
		}finally {
            session.close();
		}
	}
		
}
