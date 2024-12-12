package Bean;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="orders_table")
public class OrderBean {
	
	@Column(name="user_id")
	private Integer userid;
	
	@Column(name="merchant_id")
	private String merchantid;
	
	@Id
	@Column(name="merchantTradNo")
	private String merchanttradno;
	
	@Column(name="merchantTradDate")
	private ZonedDateTime merchanttraddate;
	
	@Column(name="totalAmount")
	private Integer totalamount;
	
	@Column(name="tradeDesc")
	private String tradedesc;
	
	@Column(name="itemName")
	private String itemname;
	
	@Column(name="order_status")
	private Integer orderstatus;
	
	@Column(name="returnUrl")
	private String returnurl;
	
	@Column(name="choosePayment")
	private String choosepayment;
	
	@Column(name="checkMacValue")
	private String checkmacvalue;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="order", cascade = CascadeType.ALL)
	private List<AdBean> ads = new ArrayList<AdBean>();
	
	public OrderBean() {
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getMerchanttradno() {
		return merchanttradno;
	}

	public void setMerchanttradno(String merchanttradno) {
		this.merchanttradno = merchanttradno;
	}

	public ZonedDateTime getMerchanttraddate() {
		return merchanttraddate;
	}

	public void setMerchanttraddate(ZonedDateTime merchanttraddate) {
		this.merchanttraddate = merchanttraddate;
	}

	public Integer getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Integer totalamount) {
		this.totalamount = totalamount;
	}

	public String getTradedesc() {
		return tradedesc;
	}

	public void setTradedesc(String tradedesc) {
		this.tradedesc = tradedesc;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Integer getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}

	public String getChoosepayment() {
		return choosepayment;
	}

	public void setChoosepayment(String choosepayment) {
		this.choosepayment = choosepayment;
	}

	public String getCheckmacvalue() {
		return checkmacvalue;
	}

	public void setCheckmacvalue(String checkmacvalue) {
		this.checkmacvalue = checkmacvalue;
	}

	public List<AdBean> getAds() {
		return ads;
	}

	public void setAds(List<AdBean> ads) {
		this.ads = ads;
	}
	
}
