package rent189.adOrder.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.adOrder.adBean.AdDto;
import rent189.adOrder.adBean.AdService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


@WebServlet("/GetAdDataServlet.do")
public class GetAdDataServlet extends HttpServlet {
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
		
		try(PrintWriter out = response.getWriter();
				BufferedReader reader = request.getReader();){
			System.out.println("connect to ad data servlet");
		
			// read the json data from client request
			StringBuilder requestJson = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				requestJson.append(line);
			}
			
			// debug
			System.out.println("requestJson:" + requestJson.toString());
			
			Gson gson = new GsonBuilder().create(); //用了DTO是否就不用再轉註冊zonedDateTime？
			Type listType = new TypeToken<List<String>>() {}.getType();
			List<String> receivedData = gson.fromJson(requestJson.toString(), listType);
			
			AdService adService = new AdService();
			ArrayList<AdDto> adInformationList = adService.getAdInformationList(receivedData);
			
			String jsonResponse;
			jsonResponse = gson.toJson(adInformationList); // serialize
			System.out.println("json response:" + jsonResponse);
			out.write(jsonResponse);
			
			
			out.flush();
		}
	}
			

		


}
