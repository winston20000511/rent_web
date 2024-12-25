package Dao;

import java.sql.SQLException;
import java.util.List;

import Bean.AdBean;

public interface AdBeanDao {
	
	boolean createAdBean(AdBean adBean) throws SQLException;
	
	List<AdBean> getAllAdBeans();
	AdBean getAdBeanByAdId(Integer adId);
	List<AdBean> getAdBeansByUserId(Integer userId);
	List<AdBean> getAdBeansByHouseId(Integer houseId);
	List<AdBean> getAdBeansByIsPaid(Boolean isPaid);
	List<AdBean> getAdBeanByAdIdAndIsPaid(Integer adId, Boolean isPaid);
	List<AdBean> getAdBeansByHouseIdAndIsPaid(Integer houseId, Boolean isPaid);
	List<AdBean> getAdBeansByUserIdAndIsPaid(Integer userId, Boolean isPaid);
	List<AdBean> getAdBeansByOrderId(String orderId);
	
	List<AdBean> getCanceledAdBeans();
	
	boolean updateAdBean(AdBean adBean);
	AdBean updateAdBeanOnTypeAndQty(Integer adId, String adType, Integer Quantity);
	
	boolean deleteAdBeanByAdId(Integer adId);
	
}
