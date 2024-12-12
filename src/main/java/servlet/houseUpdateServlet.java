package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/houseUpdateServlet.do")
public class houseUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 設置響應內容類型
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 從請求中獲取 houseId 和 status
        int houseId = Integer.parseInt(request.getParameter("houseId"));
        String status = request.getParameter("status"); // 獲取狀態值

        // 構建要更新的欄位及其值的映射
        Map<String, Object> updates = new HashMap<>();
        
        // 將狀態添加到更新映射中
        if (status != null) {
            updates.put("status", status); // 假設資料庫中的欄位名稱為 "status"
        }

        // 調用 updateHouse 方法進行更新
        boolean isUpdated = updateHouse(updates, houseId);

        // 根據更新結果生成響應
        if (isUpdated) {
            out.println("<html><body>");
            out.println("<h2>房屋信息更新成功!</h2>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<h2>房屋信息更新失敗!</h2>");
            out.println("</body></html>");
        }
    }

    private boolean updateHouse(Map<String, Object> updates, int houseId) {
        // 此處調用之前提供的 updateHouse 方法實現
       houseIMPL house = new houseIMPL();
       return house.updateHouse(updates, houseId);
    }
}