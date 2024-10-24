package rent189.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rent189.config.ConnectionUtil;
import rent189.model.BookingBean;

public class BookingDaoImpl implements BookingDao {
	@Override
	public BookingBean findByHouseId(int houseId) {
		String sql = "select * from booking_table where house_id = ?";
		BookingBean newBK = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, houseId);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				newBK = new BookingBean();
				newBK.setBooking_id(result.getLong("booking_id"));
				newBK.setHouse_id(result.getLong("house_id"));
				newBK.setUser_id(result.getLong("user_id"));
				newBK.setBooking_date(result.getString("booking_date"));
				newBK.setStart_time(result.getTime("start_time"));
				newBK.setEnd_time(result.getTime("end_time"));
				newBK.setStatus(result.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newBK;
	}

	@Override
	public List<BookingBean> findAllBooking() {
		String sql = "select * from booking_table";
		BookingBean newBK = null;
		List<BookingBean> list = new ArrayList<BookingBean>();
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				newBK = new BookingBean();
				newBK.setBooking_id(result.getLong("booking_id"));
				newBK.setHouse_id(result.getLong("house_id"));
				newBK.setUser_id(result.getLong("user_id"));
				newBK.setBooking_date(result.getString("booking_date"));
				newBK.setStart_time(result.getTime("start_time"));
				newBK.setEnd_time(result.getTime("end_time"));
				newBK.setStatus(result.getString("status"));
				list.add(newBK);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean createBooking(BookingBean booking) {
		boolean createResult = false;
		String sql = "insert into booking_table(" 
				+ "house_id,user_id,booking_date,start_time,end_time,status) "
				+ "values(?,?,?,?,?,?)";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setLong(1, booking.getHouse_id());
			statement.setLong(2, booking.getUser_id());
			statement.setString(3, booking.getBooking_date());
			statement.setTime(4, booking.getStart_time());
			statement.setTime(5, booking.getEnd_time());
			statement.setString(6, booking.getStatus());
			int update = statement.executeUpdate();
			if (update > 0) {
				createResult = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return createResult;
	}

	@Override
	public boolean deleteBooking(int bookingId) {
		boolean deleteResult = false;
		String sql = "delete from booking_table where booking_id = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, bookingId);
			int delete = statement.executeUpdate();
			if (delete > 0) {
				deleteResult = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deleteResult;
	}

	@Override
	public boolean updateBooking(BookingBean booking) {
		boolean updateResult = false;
		String sql = "update booking_table set house_id = ?, user_id = ?, "
				+ "booking_date = ?, start_time = ?, end_time = ?, status = ? "
				+ "where booking_id = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setLong(1, booking.getHouse_id());
			statement.setLong(2, booking.getUser_id());
			statement.setString(3, booking.getBooking_date());
			statement.setTime(4, booking.getStart_time());
			statement.setTime(5, booking.getEnd_time());
			statement.setString(6, booking.getStatus());
			statement.setLong(7, booking.getBooking_id());
			int update = statement.executeUpdate();
			if (update > 0) {
				updateResult = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updateResult;
	}

	@Override
	public List<BookingBean> findLatestBookings(int limit) {
		String sql = "SELECT TOP (?) * FROM booking_table ORDER BY booking_date DESC";
	    List<BookingBean> bookings = new ArrayList<>();
	    try (Connection connection = ConnectionUtil.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, limit);
	        ResultSet result = statement.executeQuery();
	        while (result.next()) {
	            BookingBean booking = new BookingBean();
	            booking.setBooking_id(result.getLong("booking_id"));
	            booking.setHouse_id(result.getLong("house_id"));
	            booking.setUser_id(result.getLong("user_id"));
	            booking.setBooking_date(result.getString("booking_date"));
	            booking.setStart_time(result.getTime("start_time"));
	            booking.setEnd_time(result.getTime("end_time"));
	            booking.setStatus(result.getString("status"));
	            bookings.add(booking);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return bookings;
	}
}
