package servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import Bean.BookingBean;
import Dao.BookingDTO;
import Dao.BookingDao;
import IMPL.BookingDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DateUtils;

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

	// 查詢所有預訂
	private void listBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<BookingBean> list = bookingDao.findAllBooking();
		List<BookingDTO> dtoList = new ArrayList<>();
		
		for (BookingBean booking : list) {
			
	        BookingDTO dto = new BookingDTO(
	    		booking.getBookingId(),
	            booking.getHouse().getTitle(),
	            booking.getUser().getName(),
	            DateUtils.formatDate(booking.getBookingDate()),
	            DateUtils.formatTime(booking.getStartTime()),
	            DateUtils.formatTime(booking.getEndTime()),
	            booking.getStatus()
	        );
	        dtoList.add(dto);
	    }
		Gson gson = new Gson();
		String json = gson.toJson(dtoList);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
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
		booking.setBookingDate(Date.valueOf(request.getParameter("booking_date")));
		booking.setStartTime(Time.valueOf(request.getParameter("start_time")));
		booking.setEndTime(Time.valueOf(request.getParameter("end_time")));
		booking.setStatus(request.getParameter("status"));

		bookingDao.createBooking(booking);
		response.sendRedirect("booking?action=list");
	}

	// 更新預訂
	private void updateBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookingId = Long.parseLong(request.getParameter("bookingId"));

		String status = request.getParameter("status");

		boolean updateResult = bookingDao.updateBookingStatus(bookingId, status);

		request.setAttribute("updateConfirm", updateResult);
	}

	// 刪除預訂
	private void deleteBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.parseLong(request.getParameter("id"));
		boolean confirmDelete  = bookingDao.deleteBooking(id);
		request.setAttribute("updateConfirm", confirmDelete);
	}

}
