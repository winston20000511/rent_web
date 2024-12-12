package Dao;

import java.sql.SQLException;
import java.util.List;

import Bean.AdBean;

public interface AdBeanDao {
	
	// create by client
	public boolean createAdBean(AdBean adBean) throws SQLException;
	
	// r
	public List<AdBean> getAllAdBeans();
	public AdBean getAdBeanByAdId(Integer adId);
	public List<AdBean> getAdBeansByUserId(Integer userId);
	public List<AdBean> getAdBeansByHouseId(Integer houseId);
	public List<AdBean> getAdBeansByIsPaid(Boolean isPaid);
	public List<AdBean> getAdBeanByAdIdAndIsPaid(Integer adId, Boolean isPaid);
	public List<AdBean> getAdBeansByHouseIdAndIsPaid(Integer houseId, Boolean isPaid);
	public List<AdBean> getAdBeansByUserIdAndIsPaid(Integer userId, Boolean isPaid);
	public List<AdBean> getAdBeansByOrderId(String orderId);
	
	public List<AdBean> getCanceledAdBeans();
	/*
	 	select*from ads_table
		join orders_table
		on ads_table.order_id = orders_table.merchantTradNo;
	 */
	
	// u
	public boolean updateAdBean(AdBean adBean);
	public AdBean updateAdBeanOnTypeAndQty(Integer adId, String adType, Integer Quantity);
	
	// d
	public boolean deleteAdBeanByAdId(Integer adId);
	
}
