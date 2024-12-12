package Bean;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ads_table")
public class AdBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ad_id")
	private Integer adid;
	
	@Column(name="user_id")
	private Integer userid;
	
	@Column(name="user_name")
	private String username;
	
	@Column(name="house_id")
	private Integer houseid;
	
	@Column(name="ad_type")
	private String adtype;
	
	@Column(name="ad_duration")
	private Integer adduration;
	
	@Column(name="ad_price")
	private Integer adprice;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="created_at")
	private ZonedDateTime createdat;
	
	@Column(name="is_paid")
	private Boolean ispaid;
	
	@Column(name="order_id", insertable=false, updatable=false)
	private String orderid;
	
	@Column(name="paid_date")
	private ZonedDateTime paiddate;
	
	@Column(name="expires_at")
	private ZonedDateTime expiresat;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	private OrderBean order;
	
	public AdBean() {
	}

	public Integer getAdid() {
		return adid;
	}

	public void setAdid(Integer adid) {
		this.adid = adid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getHouseid() {
		return houseid;
	}

	public void setHouseid(Integer houseid) {
		this.houseid = houseid;
	}

	public String getAdtype() {
		return adtype;
	}

	public void setAdtype(String adtype) {
		this.adtype = adtype;
	}

	public Integer getAdduration() {
		return adduration;
	}

	public void setAdduration(Integer adduration) {
		this.adduration = adduration;
	}

	public Integer getAdprice() {
		return adprice;
	}

	public void setAdprice(Integer adprice) {
		this.adprice = adprice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ZonedDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(ZonedDateTime createdat) {
		this.createdat = createdat;
	}

	public Boolean getIspaid() {
		return ispaid;
	}

	public void setIspaid(Boolean ispaid) {
		this.ispaid = ispaid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public ZonedDateTime getPaiddate() {
		return paiddate;
	}

	public void setPaiddate(ZonedDateTime paiddate) {
		this.paiddate = paiddate;
	}

	public ZonedDateTime getExpiresat() {
		return expiresat;
	}

	public void setExpiresat(ZonedDateTime expiresat) {
		this.expiresat = expiresat;
	}

	public OrderBean getOrder() {
		return order;
	}

	public void setOrder(OrderBean order) {
		this.order = order;
	}

}
