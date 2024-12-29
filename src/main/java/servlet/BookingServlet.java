package servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Bean.BookingBean;
import DTO.BookingDTO;
import Dao.BookingDao;
import IMPL.BookingDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DateUtils;
import util.LocalDateTimeAdapter;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookingDao bookingDao;

	@Override
	public void init() throws ServletException {
		bookingDao = new BookingDaoImpl();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "default";
		}
		switch (action) {
		case "list":
			listBooking(request, response);
			break;
		case "detail": // 顯示預約詳情
			showBookingDetails(request, response);
			break;
		case "edit":
			showEditForm(request, response);
			break;
		case "delete":
			deleteBooking(request, response);
			break;
		case "create":
			createBooking(request, response);
			break;
		case "update":
			updateBooking(request, response);
			break;
		default:
			response.sendRedirect("backstage-panel.jsp");
		}
	}

	// 顯示預約列表
	private void listBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int draw = Integer.parseInt(request.getParameter("draw")); // 這是前端的計數器。
		int start = Integer.parseInt(request.getParameter("start")); // 資料的起始位置
		int length = Integer.parseInt(request.getParameter("length")); // 每頁顯示的資料筆數
		String searchValue = request.getParameter("search[value]"); // 搜尋框中的值
		String orderColumnParam = request.getParameter("order[0][column]");
		String orderDirParam = request.getParameter("order[0][dir]");
		// 檢查是否有排序參數，沒有的話設置預設值
		int orderColumn = (orderColumnParam != null) ? Integer.parseInt(orderColumnParam) : 0; // 預設為第一列
		String orderDir = (orderDirParam != null && (orderDirParam.equals("asc") || orderDirParam.equals("desc")))
				? orderDirParam
				: "asc";
		String[] columns = { "bookingId", "houseTitle", "houseOwnerName", "userName", "bookingDate", "status" };
		String orderByColumn = columns[orderColumn]; // 排序的列名

		System.out.println("draw: " + draw);
		System.out.println("start: " + start);
		System.out.println("length: " + length);
		System.out.println("searchValue: " + searchValue);
		System.out.println("orderColumn: " + orderColumn);
		System.out.println("orderByColumn: " + orderByColumn);
		System.out.println("orderDir: " + orderDir);

		// 取得預約資料
		List<BookingDTO> data = bookingDao.findBookingsByPage(searchValue, orderByColumn, orderDir, start, length);
		long totalRecords = bookingDao.countTotal();
		long filteredRecords = bookingDao.countFiltered(searchValue);

		// 回傳 JSON 數據
		Map<String, Object> result = new HashMap<>();
		result.put("draw", draw);
		result.put("recordsTotal", totalRecords);
		result.put("recordsFiltered", filteredRecords);
		result.put("data", data);
//		result.put("draw", draw);
//		result.put("recordsTotal", totalRecords);
//		result.put("recordsFiltered", filteredRecords);
//		result.put("data", data);

		// 為LocalDateTime 處理序列化和反序列化
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(LocalTime.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

		String json = gson.toJson(result);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	// 顯示預約詳情
	private void showBookingDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookingId = request.getParameter("bookingId");

		if (bookingId != null) {
			// 查詢預約詳情
			BookingDTO booking = bookingDao.findBookingById(Long.parseLong(bookingId));

			// 為LocalDateTime 處理序列化和反序列化
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTimeAdapter())
					.registerTypeAdapter(LocalTime.class, new LocalDateTimeAdapter())
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
			String json = gson.toJson(booking);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("預約 ID 不存在");
		}
	}

	// 顯示編輯表單
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		BookingBean booking = bookingDao.findByHouseId(id);
		request.setAttribute("booking", booking);
		request.getRequestDispatcher("sidebar_booking.jsp").forward(request, response);
	}

	// 新增預訂
	private void createBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BookingBean booking = new BookingBean();
		booking.setHouseId(Long.parseLong(request.getParameter("house_id")));
		booking.setUserId(Long.parseLong(request.getParameter("user_id")));
		booking.setBookingDate(LocalDate.parse(request.getParameter("booking_date")));
		booking.setBookingTime(LocalTime.parse(request.getParameter("booking_time")));

		booking.setStatus(Byte.parseByte(request.getParameter("status")));

		bookingDao.createBooking(booking);
		response.sendRedirect("booking?action=list");
	}

	// 更新預訂
	private void updateBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookingId = Long.parseLong(request.getParameter("bookingId"));
		LocalDate bookingDate = LocalDate.parse(request.getParameter("bookingDate"));
		LocalTime bookingTime = LocalTime.parse(request.getParameter("bookingTime"));
		Byte status = Byte.parseByte(request.getParameter("status"));

		boolean updateResult = bookingDao.updateBookingStatus(bookingId, bookingDate, bookingTime, status);

		request.setAttribute("updateConfirm", updateResult);
	}

	// 刪除預訂
	private void deleteBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		boolean confirmDelete = bookingDao.deleteBooking(id);
		request.setAttribute("updateConfirm", confirmDelete);
	}

}
