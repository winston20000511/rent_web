package servlet.houseServlet;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.HouseDetailsDTO;
import Dao.houseDAO;
import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HouseDetails")
public class HouseDetail extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long houseId = Long.parseLong(request.getParameter("houseId"));
            houseDAO houseDAO = new houseIMPL();
            HouseDetailsDTO houseDetails = houseDAO.getHouseDetailsById(houseId);

            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getWriter(), houseDetails);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid request\"}");
        }
    }
}