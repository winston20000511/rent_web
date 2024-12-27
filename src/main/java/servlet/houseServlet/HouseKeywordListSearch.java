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
            String pageParam = request.getParameter("page");
            String pageSizeParam = request.getParameter("pageSize");
            String keyword = request.getParameter("keyword");

            int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
            int pageSize = (pageSizeParam != null) ? Integer.parseInt(pageSizeParam) : 10;

            // 確保 page 至少為 1
            if (page < 1) {
                page = 1;
            }

            if (pageSize < 1) {
                throw new IllegalArgumentException("Page size must be greater than 0");
            }

            houseDAO houseDAO = new houseIMPL();
            List<HouseSimpleInfoDTO> houses = houseDAO.getPaginatedHouseList(page, pageSize, keyword);

            response.setContentType("application/json;charset=UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), houses);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid page or pageSize parameter\"}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}