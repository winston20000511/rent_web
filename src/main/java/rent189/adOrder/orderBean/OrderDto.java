package rent189.adOrder.orderBean;

import java.time.ZonedDateTime;

public class OrderDto {
	// 13 items
	private Integer userId;
	private String merchantId;
	private String merchantTradNo;
	private String merchantTradDate;
	private String paymentType;
	private Integer totalAmount;
	private String tradeDesc;
	private String itemName;
	private String orderStatus;
	private String returnUrl;
	private String choosePayment;
	private String checkMacValue;
	private Integer encryptType;
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantTradNo() {
		return merchantTradNo;
	}
	public void setMerchantTradNo(String merchantTradNo) {
		this.merchantTradNo = merchantTradNo;
	}
	public String getMerchantTradDate() {
		return merchantTradDate;
	}
	public void setMerchantTradDate(ZonedDateTime merchantTradDate) {
		this.merchantTradDate = merchantTradDate.toString().substring(0, 10);
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTradeDesc() {
		return tradeDesc;
	}
	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Boolean orderStatus) {
		this.orderStatus = (orderStatus == true)? "一般訂單" : "已取消訂單";
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getChoosePayment() {
		return choosePayment;
	}
	public void setChoosePayment(String choosePayment) {
		this.choosePayment = choosePayment;
	}
	public String getCheckMacValue() {
		return checkMacValue;
	}
	public void setCheckMacValue(String checkMacValue) {
		this.checkMacValue = checkMacValue;
	}
	public Integer getEncryptType() {
		return encryptType;
	}
	public void setEncryptType(Integer encryptType) {
		this.encryptType = encryptType;
	}
	
	
	
}
