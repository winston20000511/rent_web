package Bean;

import java.sql.Time;

public class BookingDetilBean {
	private Long booking_id;
	private Long house_id;
	private String house_title;
	private Long user_id;
	private String user_name;
	private String booking_date;
	private Time start_time;
	private Time end_time;
	private String status;
	
	public BookingDetilBean() {
		
	}
	
	public BookingDetilBean(Long booking_id, Long house_id, String house_title, Long user_id,String user_name, String booking_date,
			Time start_time, Time end_time, String status) {
		
		this.booking_id = booking_id;
		this.house_id = house_id;
		this.house_title = house_title;
		this.user_id = user_id;
		this.user_name = user_name;
		this.booking_date = booking_date;
		this.start_time = start_time;
		this.end_time = end_time;
		this.status = status;
	}

	public Long getBooking_id() {
		return booking_id;
	}
	public void setBooking_id(Long booking_id) {
		this.booking_id = booking_id;
	}
	public Long getHouse_id() {
		return house_id;
	}
	public void setHouse_id(Long house_id) {
		this.house_id = house_id;
	}
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getHouse_title() {
		return house_title;
	}
	public void setHouse_title(String house_title) {
		this.house_title = house_title;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getBooking_date() {
		return booking_date;
	}
	public void setBooking_date(String booking_date) {
		this.booking_date = booking_date;
	}
	public Time getStart_time() {
		return start_time;
	}
	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}
	public Time getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
