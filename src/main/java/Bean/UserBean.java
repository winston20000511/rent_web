package Bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import util.Exclude;

@Entity
@Table(name = "user_table")
public class UserBean {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String name;
	private String email;
	private String password;
	private String phone;
	private byte[] picture;
	private Timestamp createtime;
	private Integer gender;
	private Integer status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBean", cascade = CascadeType.ALL)
	@JsonInclude(JsonInclude.Include.NON_EMPTY) 
	@Exclude
	private List<BookingBean> bookings = new ArrayList<BookingBean>();
	
	//weirong
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBean", cascade = CascadeType.ALL)
	@JsonInclude(JsonInclude.Include.NON_EMPTY) 
	@Exclude
	private List<houseTableBean> houses = new ArrayList<houseTableBean>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	@JsonInclude(JsonInclude.Include.NON_EMPTY) 
	@Exclude
	private List<AdBean> ads = new ArrayList<AdBean>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	@JsonInclude(JsonInclude.Include.NON_EMPTY) 
	@Exclude
	private List<OrderBean> orders = new ArrayList<OrderBean>();

	// Constructor
	public UserBean(Integer userId, String name, String email, String password, String phone, byte[] picture,
			Timestamp createtime, Integer gender, Integer status) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.picture = picture;
		this.createtime = createtime;
		this.gender = gender;
		this.status = status;
	}

	public UserBean() {
	}

	// Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}