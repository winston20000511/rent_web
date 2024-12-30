package servlet.houseServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import DTO.HouseSimpleInfoDTO;
import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HouseListServlet")
public class HouseListServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("application/json; charset=UTF-8");

	    try {
	        // 檢查參數並設置默認值
	        int start = 0;
	        int length = 10;

	        String startParam = request.getParameter("start");
	        String lengthParam = request.getParameter("length");

	        if (startParam != null && !startParam.isEmpty()) {
	            start = Integer.parseInt(startParam);
	        }
	        if (lengthParam != null && !lengthParam.isEmpty()) {
	            length = Integer.parseInt(lengthParam);
	        }

	        String keyword = request.getParameter("search[value]");
	        keyword = (keyword != null) ? keyword : "";

	        // 獲取分頁的房屋列表
	        houseIMPL houseDAO = new houseIMPL();
	        List<HouseSimpleInfoDTO> houses = houseDAO.getPaginatedHouseList(start / length + 1, length, keyword);

	        // 獲取總記錄數
	        int totalRecords = houseDAO.getTotalRecordCount("");

	        // 構建 DataTables 回應數據
	        Map<String, Object> jsonResponse = new HashMap<>();
	        jsonResponse.put("draw", request.getParameter("draw"));
	        jsonResponse.put("recordsTotal", totalRecords);
	        jsonResponse.put("recordsFiltered", totalRecords);
	        jsonResponse.put("data", houses);

	        // 將數據返回給前端
	        response.getWriter().write(new Gson().toJson(jsonResponse));

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"error\": \"數據加載失敗\"}");
	    }
	}
}