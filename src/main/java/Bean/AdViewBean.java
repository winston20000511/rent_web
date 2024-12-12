package Bean;

import java.time.ZonedDateTime;

import util.TimeForm;

public class AdViewBean {
	private Integer adId;
	private Integer userId;
	private String userName;
	private Integer houseId;
	private String adType;
	private String adDuration;
	private Integer adPrice;
	private Integer quantity;
	private Integer subtotal; 
	private String createdAt;
	private String isPaid;
	private String orderId;
	private String paidDate;
	private String expiresAt;
		
	public AdViewBean() {
		
	}
	
	public AdViewBean(AdBean adBean) {
		this.setAdId(adBean.getAdid());
		this.setUserId(adBean.getUserid());
		this.setUserName(adBean.getUsername());
		this.setHouseId(adBean.getHouseid());
		this.setAdType(adBean.getAdtype());
		this.setAdDuration(adBean.getAdduration());
		this.setAdPrice(adBean.getAdprice());
		this.setQuantity(adBean.getQuantity());
		this.setSubtotal(adBean.getAdprice(), adBean.getQuantity());
		this.setCreatedAt(adBean.getCreatedat());
		this.setIsPaid(adBean.getIspaid());
		this.setOrderId(adBean.getOrderid());
		this.setPaidDate(adBean.getPaiddate());
		this.setExpiresAt(adBean.getExpiresat());
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

	public String getAdDuration() {
		return adDuration;
	}

	public void setAdDuration(Integer adDuration) {
		if(adDuration==30) this.adDuration = "30天";
		else if(adDuration==60)this.adDuration = "60天";
		else System.out.println("廣告天期有誤");;
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

	public Integer getSubtotal() {
		subtotal = adPrice*quantity;
		return subtotal;
	}

	public void setSubtotal(Integer adPrice, Integer quantity) {
		this.subtotal = adPrice*quantity;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = TimeForm.convertZonedDateTimeToString(createdAt);
	}

	public String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		if (isPaid) this.isPaid = "已付款";
		else this.isPaid = "未付款";
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = (orderId == null)? "N/A" : orderId;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(ZonedDateTime paidDate) {
		this.paidDate =
				(expiresAt == null)? "N/A" : TimeForm.convertZonedDateTimeToString(paidDate);
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(ZonedDateTime expiresAt) {
		this.expiresAt = 
				(expiresAt == null)? "N/A" : TimeForm.convertZonedDateTimeToString(expiresAt);
	}	
	
}
