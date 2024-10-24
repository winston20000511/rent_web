package rent189.adOrder.adBean;

import java.time.ZonedDateTime;

public class AdBean {
	// 12 items
	private Integer adId;
	private Integer userId;
	private String userName;
	private Integer houseId;
	private String adType;
	private Integer adDuration;
	private Integer adPrice;
	private Integer quantity;
	private ZonedDateTime createdAt;
	private Boolean isPaid;
	private String orderId;
	private ZonedDateTime paidDate;
	private ZonedDateTime expiresAt;
	
	public AdBean() {
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
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

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}
	
	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public Integer getAdDuration() {
		return adDuration;
	}

	public void setAdDuration(Integer adDuration) {
		this.adDuration = adDuration;
	}

	public Integer getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Integer adPrice) {
		this.adPrice = adPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ZonedDateTime getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(ZonedDateTime paidDate) {
		this.paidDate = paidDate;
	}

	public ZonedDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(ZonedDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

}
