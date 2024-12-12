package Dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import Bean.AdBean;
import Bean.AdViewBean;
import IMPL.AdBeanDaoImpl;

public class AdService {
// get ad beans -> ads view
	
	private AdBeanDao adBeanDao;
	
	public AdService(Session session) {
		adBeanDao = new AdBeanDaoImpl(session);
	}

	// 1. get filtered ads
	public List<AdViewBean> getFilteredAds(List<String> receivedData) {
		// receivedData = ["search", "searchCondition", "isPaid", "userInput"]
		List<AdBean> adBeanList = new ArrayList<AdBean>();

		// when there is no input
		if (receivedData.get(3).equals("")) {
			// if is paid
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeansByIsPaid(true);
			} else if (receivedData.get(2).equals("unpaid")) {
				// if is paid
				adBeanList = adBeanDao.getAdBeansByIsPaid(false);
			} else {
				// else if paid = all
				adBeanList = adBeanDao.getAllAdBeans();
			}
			return setAdsView(adBeanList);
		}

		// when there is an input
		// if condition = ad id
		if (receivedData.get(1).equals("adId")) {
			// paid/unpaid + user id
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeanByAdIdAndIsPaid(Integer.parseInt(receivedData.get(3)), true);
			} else if (receivedData.get(2).equals("unpaid")) {
				adBeanList = adBeanDao.getAdBeanByAdIdAndIsPaid(Integer.parseInt(receivedData.get(3)), false);
			} else {
				// all + ad id
				AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.parseInt(receivedData.get(3)));
				adBeanList.add(adBean);
			}
			return setAdsView(adBeanList);
		}

		// if condition = user id
		if (receivedData.get(1).equals("userId")) {
			// paid/unpaid + user id
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

		// if condition = house id
		if (receivedData.get(1).equals("houseId")) {
			// if paid/unpaid
			if (receivedData.get(2).equals("paid")) {
				adBeanList = adBeanDao.getAdBeansByHouseIdAndIsPaid(Integer.parseInt(receivedData.get(3)), true);
			} else if (receivedData.get(2).equals("unpaid")) {
				adBeanList = adBeanDao.getAdBeansByHouseIdAndIsPaid(Integer.parseInt(receivedData.get(3)), false);
				// all + house id
			} else {
				adBeanList = adBeanDao.getAdBeansByHouseId(Integer.parseInt(receivedData.get(3)));
			}
			return setAdsView(adBeanList);
		}

		return null;
	}

	// 2. return the information of a specific ad
	public AdViewBean getAdDetails(List<String> receivedData) {
		// receivedData ["adDetail", "adId"]
		AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.valueOf(receivedData.get(1)));
		return setAdView(adBean);
	}

	// 3. update ad information
	public AdViewBean updateAdDetails(List<String> receivedData) {
		// receivedData ["adUpdate", "adId", "adType", "quantity"]
		String adType = (receivedData.get(2).equals("a")) ? "A廣告" : "B廣告";
		adBeanDao.updateAdBeanOnTypeAndQty(Integer.parseInt(receivedData.get(1)), adType,
				Integer.parseInt(receivedData.get(3)));
		AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.parseInt(receivedData.get(1)));
		return setAdView(adBean);
	}

	//4. delete ad
	public boolean deleteAd(List<String> receivedData) {
		// receivedData ["delete", "adId"]
		System.out.println("delete ad: " + receivedData);
		return adBeanDao.deleteAdBeanByAdId(Integer.parseInt(receivedData.get(1)));
	}
	
	
	/* inner methods */
	// set up view for an ad
	private AdViewBean setAdView(AdBean adBean) {
		AdViewBean adView = new AdViewBean(adBean);
		return adView;
	}
	
	// set up view for ads
	private List<AdViewBean> setAdsView(List<AdBean> adBeanList) {
		List<AdViewBean> adsView = new ArrayList<AdViewBean>();
		for(AdBean adBean : adBeanList) {
			AdViewBean adView = new AdViewBean(adBean);
			adsView.add(adView);
		}
		return adsView;
	}

}