package servlet.houseServlet;

import java.io.IOException;

import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UpdateHouseStatusServlet")
public class UpdateHouseStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private houseIMPL houseDAO;

    public void init() {
        houseDAO = new houseIMPL();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        try {
            // Get houseId and status from the request
            Long houseId = Long.parseLong(request.getParameter("houseId"));
            byte status = Byte.parseByte(request.getParameter("status"));

            // Update the house status
            boolean isUpdated = houseDAO.updateHouseStatus(houseId, status);

            if (isUpdated) {
                response.getWriter().write("{\"success\": true, \"message\": \"狀態更新成功！\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"狀態更新失敗！\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"請求參數錯誤！\"}");
        }
    }
}
