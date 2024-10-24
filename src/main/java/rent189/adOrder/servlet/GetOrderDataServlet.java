package rent189.adOrder.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import rent189.adOrder.orderBean.OrderDto;
import rent189.adOrder.orderBean.OrderService;

@WebServlet("/GetOrderDataServlet.do")
public class GetOrderDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}
	
	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// response format
		response.setContentType("application/json;charset=UTF-8");
		
		try(PrintWriter out = response.getWriter();
				BufferedReader reader = request.getReader();){
			System.out.println("connect to servlet get order");
		
			// read the json data from client request
			StringBuilder requestJson = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				requestJson.append(line);
			}
			
			// debugging
			System.out.println("requestJson test update:" + requestJson.toString());
			
			// 設一個combinedResponse，準備將訂單和廣告詳情組合成一個 JSON 對象
			JsonObject combinedResponse = new JsonObject();
			
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<List<String>>() {}.getType();
			List<String> receivedData = gson.fromJson(requestJson.toString(), listType);
			
			OrderService orderService = new OrderService();
			ArrayList<OrderDto> getOrderInformationList = orderService.getOrderInformationList(receivedData);			
			
			// order Details
			String jsonResponseOrderDetail = gson.toJson(getOrderInformationList); // 
			System.out.println("json response order details:" + jsonResponseOrderDetail);
			// 將 order details 加入 combinedResponse
			combinedResponse.add("orderDetails", gson.fromJson(jsonResponseOrderDetail, JsonElement.class));
			
			// get ad Details; if only search for ad > skip
			if(receivedData.get(0).equals("orderDetails")) {
				request.setAttribute("requestJsonFromGetOrderServlet", requestJson.toString());
				
				// 使用 StringWriter 捕獲 GetAdDataForOrderServlet 的響應
				StringWriter stringWriter = new StringWriter();
				PrintWriter writer = new PrintWriter(stringWriter);
				HttpServletResponse wrappedResponse = new HttpServletResponseWrapper(response) {
					@Override
					public PrintWriter getWriter() {
						return writer;
					}
				};
				
				// 包含 GetAdDataForOrderServlet
				RequestDispatcher dispatcher = request.getRequestDispatcher("/GetAdDataForOrderServlet.do");
				dispatcher.include(request, wrappedResponse);
				
				// 從 GetAdDataForOrderServlet 獲取的 JSON 資料
				String jsonResponseAdDetails = stringWriter.toString();
				System.out.println("json response ad details:" + jsonResponseAdDetails);
				
				// 將 ad details 加入 combinedResponse
				combinedResponse.add("adDetails", gson.fromJson(jsonResponseAdDetails, JsonElement.class));
			}

			// 返回給前端的 JSON 數據
			out.write(gson.toJson(combinedResponse));

			
			out.flush();
		}
	}
		
}
