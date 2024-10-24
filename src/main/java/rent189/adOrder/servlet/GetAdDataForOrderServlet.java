package rent189.adOrder.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.adOrder.adBean.AdDto;
import rent189.adOrder.adBean.AdService;

@WebServlet("/GetAdDataForOrderServlet.do")
public class GetAdDataForOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processAction(request, response);
	}
	
	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// response format
		response.setContentType("application/json;charset=UTF-8");
		
		try(PrintWriter out = response.getWriter();){
			System.out.println("connect to get data for order servlet");
		
			String requestJsonFromGetOrderServlet = (String) request.getAttribute("requestJsonFromGetOrderServlet");

			// debug
			System.out.println("requestJson:" + requestJsonFromGetOrderServlet.toString());
			
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<List<String>>() {}.getType();
			List<String> receivedData = gson.fromJson(requestJsonFromGetOrderServlet, listType);
			
			System.out.println("requestJsonFromGetOrderServlet: " + requestJsonFromGetOrderServlet);
			
			// 得到要給訂單的廣告詳細
			AdService adService = new AdService();
			ArrayList<AdDto> adInformationListForOrder = adService.getAdInformationList(receivedData);
			
			
			String jsonResponse;
			jsonResponse = gson.toJson(adInformationListForOrder); // serialize
			System.out.println("json response:" + jsonResponse);
			out.write(jsonResponse);

			out.flush();
		}
	}
			

		


}