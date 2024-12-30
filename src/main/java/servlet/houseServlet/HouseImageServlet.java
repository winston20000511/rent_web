package servlet.houseServlet;

import java.io.IOException;

import Dao.houseDAO;
import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HouseImageServlet")
public class HouseImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private houseIMPL houseDAO;

    public void init() {
        houseDAO = new houseIMPL();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long houseId = Long.parseLong(request.getParameter("houseId"));

        byte[] image = houseDAO.getSmallestImageByHouseId(houseId);

        if (image != null) {
            response.setContentType("image/jpeg"); // 或其他對應的圖片格式
            response.getOutputStream().write(image);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
        }
    }
}
