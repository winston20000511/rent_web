package rent189.houseBean;

public class houseBACKBean {
    private int houseId;      // 房屋ID
    private String title;     // 房屋標題
    private int userId;
	private String userName; // 屋主姓名
    private String address;   // 地址（可以根據需要進一步拆分）
    private int price;     // 價格
    
    public houseBACKBean(int houseId, String title, int userId, String userName, String address, int price) {
        this.houseId = houseId;
        this.title = title;
        this.userId = userId;
        this.userName = userName;
        this.address = address;
        this.price = price;
    }
	public int getHouseId() {
		return houseId;
	}
	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}