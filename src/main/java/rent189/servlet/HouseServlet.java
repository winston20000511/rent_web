package rent189.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.houseBean.houseBACKBean;
import rent189.houseBean.houseIMPL;


@WebServlet("/houses.123")
public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private houseIMPL house = new houseIMPL(); // Assuming you have a service class

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	processAction(request,response);
    }

    private void processAction(HttpServletRequest request, HttpServletResponse response) {
        // Set the response format
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    	response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            System.out.println("Connected to servlet");


            // Call the method to get all houses
            List<houseBACKBean> houses = house.getAllHouses();

            // Create a Gson instance
            Gson gson = new GsonBuilder().create();

            // Serialize the list of houses to JSON
            String jsonResponse = gson.toJson(houses);
            
            // Debugging output
            System.out.println("JSON response: " + jsonResponse);
            
            // Write the JSON response back to the client
            out.write(jsonResponse);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions appropriately
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    
    try {
        System.out.println("Attempting to load SQLServerDriver");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("SQLServerDriver loaded successfully");
    } catch (ClassNotFoundException e) {
        System.err.println("SQLServerDriver not found!");
        e.printStackTrace(); // 打印堆栈跟踪
    }}}