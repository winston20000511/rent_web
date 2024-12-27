package servlet.houseServlet;

import java.io.IOException;

import Dao.houseDAO;
import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HouseImage")
public class HouseImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long houseId = Long.parseLong(request.getParameter("houseId"));
            houseDAO houseDAO = new houseIMPL();
            byte[] image = houseDAO.getSmallestImageByHouseId(houseId);

            if (image != null) {
                response.setContentType("image/jpeg");
                response.getOutputStream().write(image);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Image not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid request\"}");
        }
    }
}
