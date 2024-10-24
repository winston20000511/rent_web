package rent189.adOrder.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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

@WebServlet("/UpdateAdDataServelt.do")
public class UpdateAdDataServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// response format
		response.setContentType("application/json;charset=UTF-8");

		try (PrintWriter out = response.getWriter(); BufferedReader reader = request.getReader();) {
			System.out.println("connect to servlet test update");

			// read the json data from client request
			StringBuilder requestJson = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				requestJson.append(line);
			}

			// debugging
			System.out.println("requestJson test update:" + requestJson.toString());

			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<List<String>>() {
			}.getType();
			List<String> receivedData = gson.fromJson(requestJson.toString(), listType);

			AdService adService = new AdService();
			AdDto updatedAdInformationList = adService.updateAdInformationList(receivedData);

			String jsonResponse;
			jsonResponse = gson.toJson(updatedAdInformationList); // serialize
			System.out.println("json response update:" + jsonResponse);
			out.write(jsonResponse);

			out.flush();
		}

	}
}
