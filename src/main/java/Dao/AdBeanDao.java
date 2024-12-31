package Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Bean.AdBean;

public interface AdBeanDao {
	
	boolean createAdBean(AdBean adBean) throws SQLException;
	
	List<AdBean> getAllAdBeans();
	List<AdBean> filterAds(String searchCondition, String paidCondition, String input);
	AdBean checkAdDetails(Long adId);
	
	AdBean editAd(Long adId, Integer newAdtypeId);
	
	boolean deleteAdBeanByAdId(Long adId);
	
}
