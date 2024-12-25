package Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;

import Bean.AdBean;
import Bean.AdViewBean;
import IMPL.AdBeanDaoImpl;

public class AdService {
	
	private Logger logger = Logger.getLogger(AdService.class.getName());
	
	private AdBeanDao adBeanDao;
	
	public AdService(Session session) {
		adBeanDao = new AdBeanDaoImpl(session);
	}

	/**
	 * 篩選廣告
	 * 傳入參數：receivedData = ["search", "searchCondition", "isPaid", "userInput"]
	 * @param receivedData
	 * @return
	 */
	public List<AdViewBean> getFilteredAds(List<String> receivedData) {
		
		logger.info(receivedData.toString());
		
		List<AdBean> adBeanList = new ArrayList<AdBean>();

		// 沒有使用者輸入時
		if (receivedData.get(3).equals("")) {
			// 找已付款
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeansByIsPaid(true);
			} else if (receivedData.get(2).equals("unpaid")) {
				// 找未付款
				adBeanList = adBeanDao.getAdBeansByIsPaid(false);
			} else {
				// 已付跟未付都找
				adBeanList = adBeanDao.getAllAdBeans();
			}
			
			return setAdsView(adBeanList);
		}

		
		// 選擇輸入為 adId 時
		if (receivedData.get(1).equals("adId")) {
			// 有付款/沒付款 + user id
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeanByAdIdAndIsPaid(Integer.parseInt(receivedData.get(3)), true);
			} else if (receivedData.get(2).equals("unpaid")) {
				adBeanList = adBeanDao.getAdBeanByAdIdAndIsPaid(Integer.parseInt(receivedData.get(3)), false);
			} else {
				// 全選 + user id
				AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.parseInt(receivedData.get(3)));
				adBeanList.add(adBean);
			}
			return setAdsView(adBeanList);
		}

		// 如果篩選條件 = user id
		if (receivedData.get(1).equals("userId")) {
			// 已付/未付 + user id
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeansByUserIdAndIsPaid(Integer.parseInt(receivedData.get(3)), true);
			} else if (receivedData.get(2).equals("unpaid")) {
				adBeanList = adBeanDao.getAdBeansByUserIdAndIsPaid(Integer.parseInt(receivedData.get(3)), false);
			} else {
				// all + user id
				adBeanList = adBeanDao.getAdBeansByUserId(Integer.parseInt(receivedData.get(3)));
			}
			return setAdsView(adBeanList);
		}

		// 如果篩選條件 = house id
		if (receivedData.get(1).equals("houseId")) {
			// 已付/未付
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeansByHouseIdAndIsPaid(Integer.parseInt(receivedData.get(3)), true);
			} else if (receivedData.get(2).equals("unpaid")) {
				adBeanList = adBeanDao.getAdBeansByHouseIdAndIsPaid(Integer.parseInt(receivedData.get(3)), false);
				// 全部 + house id
			} else {
				adBeanList = adBeanDao.getAdBeansByHouseId(Integer.parseInt(receivedData.get(3)));
			}
			return setAdsView(adBeanList);
		}

		return null;
	}

	/**
	 * 接收輸入條件 receivedData ["adDetail", "adId"] ，回傳廣告詳細資料
	 * @param receivedData
	 * @return
	 */
	public AdViewBean getAdDetails(List<String> receivedData) {
		
		logger.info(receivedData.toString());
		
		AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.valueOf(receivedData.get(1)));
		return setAdView(adBean);
	}

	/**
	 * 接收輸入條件 receivedData ["adUpdate", "adId", "adType", "quantity"]，找到廣告並更新資料
	 * @param receivedData
	 * @return
	 */
	public AdViewBean updateAdDetails(List<String> receivedData) {
		
		logger.info(receivedData.toString());
		
		String adType = (receivedData.get(2).equals("a")) ? "A廣告" : "B廣告"; // 可能要跟前端溝通名稱
		adBeanDao.updateAdBeanOnTypeAndQty(
				Integer.parseInt(receivedData.get(1)), adType,
				Integer.parseInt(receivedData.get(3)));
		AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.parseInt(receivedData.get(1)));
		return setAdView(adBean);
	}

	/**
	 * 刪除廣告 receivedData ["delete", "adId"]
	 * @param receivedData
	 * @return
	 */
	public boolean deleteAd(List<String> receivedData) {
		logger.info("delete ad: " + receivedData);
		return adBeanDao.deleteAdBeanByAdId(Integer.parseInt(receivedData.get(1)));
	}
	
	
	/**
	 * 展示廣告詳細內容
	 * @param adBean
	 * @return
	 */
	private AdViewBean setAdView(AdBean adBean) {
		AdViewBean adView = new AdViewBean();
		adView.setAdId(adBean.getAdId());
		adView.setUserId(adBean.getUserId());
//		adView.setHouseTitle(adBean.getHouse().getHouseId());
		adView.setAdName(adBean.getAdtype().getAdName());
//		adView.setAdDuration(null);
		adView.setAdPrice(adBean.getAdPrice());
//		adView.setSubtotal(null);
		adView.setIsPaid(adBean.getIsPaid());
		adView.setOrderId(adBean.getOrderId());
		adView.setPaidDate(adBean.getPaidDate());
		
		return adView;
	}
	
	/**
	 * 建立廣告詳細內容物件
	 * @param adBeanList
	 * @return
	 */
	private List<AdViewBean> setAdsView(List<AdBean> adBeanList) {
		List<AdViewBean> adsView = new ArrayList<AdViewBean>();
		for(AdBean adBean : adBeanList) {
			AdViewBean adView = setAdView(adBean);
			adsView.add(adView);
		}
		
		return adsView;
	}

}