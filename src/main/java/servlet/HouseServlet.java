package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import Bean.houseBACKBean;
import Bean.houseTableBean;
import Dao.houseDAO;
import IMPL.BookingDaoImpl;
import IMPL.houseIMPL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/houses.123")
public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private houseDAO houseDAO;

	@Override
	public void init() throws ServletException {
		houseDAO = new houseIMPL();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processAction(request, response);
	}

	private void processAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set the response format

		List<houseTableBean> list = houseDAO.getAllHouses();
		List<houseBACKBean> houseTable = new ArrayList<>();
		for (houseTableBean house : list) {
			String address = String.format("%s, %s, %s", house.getCity(), house.getTownship(), house.getStreet());
			String userName = house.getUserBean().getName(); // Assuming UserTableBean is mapped correctly

			// Convert status from BYTE to String
			String status;
			switch (house.getStatus()) { // Assuming getStatus returns a byte
			case 0:
				status = "待審核"; // Pending review
				break;
			case 1:
				status = "上架"; // Approved
				break;
			case 2:
				status = "停權"; // Violated/Prohibited
				break;
			case 3:
				status = "下架"; // Violated/Prohibited
				break;
			default:
				status = "未知狀態"; // Unknown status
				break;
			}
			// Create houseBACKBean instance
			houseBACKBean Newlist = new houseBACKBean(house.getHouseId(), house.getTitle(), house.getUserId(), userName,
					address, house.getPrice(), status // Set the converted status here
			);
			houseTable.add(Newlist);
			System.out.println(Newlist);
		}
		

			System.out.println(houseTable);
			// Create a Gson instance
			Gson gson = new Gson();

			// Serialize the list of houses to JSON
			String json = gson.toJson(houseTable);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		

		try

		{
			System.out.println("Attempting to load SQLServerDriver");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("SQLServerDriver loaded successfully");
		} catch (ClassNotFoundException e) {
			System.err.println("SQLServerDriver not found!");
			e.printStackTrace(); // 打印堆栈跟踪
		}
	}
}