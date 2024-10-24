package rent189.servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.dao.BookingDao;
import rent189.dao.BookingDaoImpl;
import rent189.model.BookingBean;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BookingDao bookingDao;
	
	// 處理查詢請求
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "list";
		}
		switch (action) {
		case "list":
			listBooking(request, response);
			break;
		case "edit":
			showEditForm(request, response);
			break;
		case "delete":
			deleteBooking(request, response);
			break;
		default:
			listBooking(request, response);
			break;
		}
	}

	// 查詢所有預訂
	private void listBooking(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		bookingDao = new BookingDaoImpl();
	    List<BookingBean> list = bookingDao.findAllBooking();
	    Gson gson = new Gson();
	    String json = gson.toJson(list);
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	}

	// 顯示編輯表單
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		BookingBean booking = bookingDao.findByHouseId(id);
		request.setAttribute("booking", booking);
		RequestDispatcher dispatcher = request.getRequestDispatcher("sidebar_booking.jsp");
		dispatcher.forward(request, response);
	}

	// 新增預訂
	private void createBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BookingBean booking = new BookingBean();
		booking.setHouse_id(Long.parseLong(request.getParameter("house_id")));
		booking.setUser_id(Long.parseLong(request.getParameter("user_id")));
		booking.setBooking_date(request.getParameter("booking_date"));
		booking.setStart_time(Time.valueOf(request.getParameter("start_time")));
		booking.setEnd_time(Time.valueOf(request.getParameter("end_time")));
		booking.setStatus(request.getParameter("status"));

		bookingDao.createBooking(booking);
		response.sendRedirect("booking?action=list");
	}

	// 更新預訂
	private void updateBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BookingBean booking = new BookingBean();
		booking.setBooking_id(Long.parseLong(request.getParameter("booking_id")));
		booking.setHouse_id(Long.parseLong(request.getParameter("house_id")));
		booking.setUser_id(Long.parseLong(request.getParameter("user_id")));
		booking.setBooking_date(request.getParameter("booking_date"));
		booking.setStart_time(Time.valueOf(request.getParameter("start_time")));
		booking.setEnd_time(Time.valueOf(request.getParameter("end_time")));
		booking.setStatus(request.getParameter("status"));

		bookingDao.updateBooking(booking);
		response.sendRedirect("booking?action=list");
	}

	// 刪除預訂
	private void deleteBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		bookingDao.deleteBooking(id);
//		response.sendRedirect("booking?action=list");
	}

}
