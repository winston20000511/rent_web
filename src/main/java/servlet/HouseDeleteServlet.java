package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/housesDelete.123")
public class HouseDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private houseIMPL house = new houseIMPL(); // Assuming you have a service class
    private Gson gson = new Gson(); // Create a Gson instance

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        // Read the JSON input
        String json = request.getReader().lines().collect(Collectors.joining());
        System.out.println("Received JSON: " + json);
        
        try {
            // Parse the JSON to HouseRequest object
            HouseRequest houseRequest = gson.fromJson(json, HouseRequest.class);
            if (houseRequest == null) {
                throw new IllegalArgumentException("HouseRequest is null");
            }

            int houseId = houseRequest.getId();
            System.out.println("Received house ID: " + houseId);

            // Validate house ID
            if (houseId <= 0) {
                out.print("{\"status\":\"error\",\"message\":\"Invalid house ID.\"}");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Attempt to delete the house
            boolean success = house.deleteHouseById(houseId);
            if (success) {
                out.print("{\"status\":\"success\"}");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                out.print("{\"status\":\"error\",\"message\":\"No records deleted.\"}");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.flush();
        }
    }

    class HouseRequest {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}