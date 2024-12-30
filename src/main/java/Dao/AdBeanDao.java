package Dao;

import java.sql.SQLException;
import java.util.List;

import Bean.AdBean;

public interface AdBeanDao {
	
	boolean createAdBean(AdBean adBean) throws SQLException;
	
	List<AdBean> getAllAdBeans();
	List<AdBean> filterAds(String searchCondition, String paidCondition, String input);
	AdBean checkAdDetails(Long adId);
	
	List<AdBean> getCanceledAdBeans();
	
	boolean updateAdBean(AdBean adBean);
	AdBean updateAdBeanOnAdTypeAndPrice(Long adId, Integer adtypeId);
	
	boolean deleteAdBeanByAdId(Long adId);
	
}
