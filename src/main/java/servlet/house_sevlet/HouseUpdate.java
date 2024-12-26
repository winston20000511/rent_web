package servlet.house_sevlet;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.HouseDetailsDTO;
import Dao.HouseDAO;
import IMPL.HouseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UpdateHouseDetails")
public class HouseUpdate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long houseId = Long.parseLong(request.getParameter("houseId"));
            HouseDetailsDTO houseDetails = new ObjectMapper().readValue(request.getReader(), HouseDetailsDTO.class);
            HouseDAO houseDAO = new HouseIMPL();
            boolean success = houseDAO.updateHouseDetailsWithoutImageAndUser(houseId, houseDetails);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid request\"}");
        }
    }
}






