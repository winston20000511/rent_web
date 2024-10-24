package rent189.adOrder.adBean;

import java.util.ArrayList;
import java.util.List;

public interface AdBeanDao {
	
	// create by client
	public boolean createAdBean(AdBean adBean);
	
	// r
	public ArrayList<AdBean> getAdBeans();
	public AdBean getAdBeanByAdId(Integer adId);
	public ArrayList<AdBean> getAdBeansByUserId(Integer userId);
	public ArrayList<AdBean> getAdBeansByHouseId(Integer houseId);
	public ArrayList<AdBean> getAdBeansByIsPaid(Boolean isPaid);
	public ArrayList<AdBean> getAdBeanByAdIdAndIsPaid(Integer adId, Boolean isPaid);
	public ArrayList<AdBean> getAdBeanByHouseIdAndIsPaid(Integer houseId, Boolean isPaid);
	public ArrayList<AdBean> getAdBeanByUserIdAndIsPaid(Integer userId, Boolean isPaid);
	public ArrayList<AdBean> getAdBeansByOrderId(String orderId);
	
	public ArrayList<AdBean> getCanceledAdBeans();
	/*
	 	select*from ads_table
		join orders_table
		on ads_table.order_id = orders_table.merchantTradNo;
	 */
	
	// u
	public AdBean updateAdBeanByAdId(Integer adId, List<String> receivedData);
	
	// d
	public boolean deleteAdBeanByAdId(Integer adId);
	
}
