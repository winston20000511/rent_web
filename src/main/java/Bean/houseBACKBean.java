package Bean;

public class houseBACKBean {
    private Integer houseId;      // 房屋ID
    private String title;     // 房屋標題
    private Integer userId;
	private String userName; // 屋主姓名
    private String address;   // 地址（可以根據需要進一步拆分）
    private Integer price;     // 價格
    private String status;
	public houseBACKBean(Integer houseId, String title, Integer userId, String userName, String address, Integer price,
			String status) {
		this.houseId = houseId;
		this.title = title;
		this.userId = userId;
		this.userName = userName;
		this.address = address;
		this.price = price;
		this.status = status;
	}
	public Integer getHouseId() {
		return houseId;
	}
	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}