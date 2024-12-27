package servlet.houseServlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.HouseSimpleInfoDTO;
import Dao.houseDAO;
import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HouseListInPage")
public class HouseKeywordListSearch extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = Integer.parseInt(request.getParameter("page"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            String keyword = request.getParameter("keyword");

            houseDAO houseDAO = new houseIMPL();
            List<HouseSimpleInfoDTO> houses = houseDAO.getPaginatedHouseList(page, pageSize, keyword);

            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getWriter(), houses);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid request\"}");
        }
    }
}
