package Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;

import Bean.AdBean;
import IMPL.AdBeanDaoImpl;
import dto.AdDetailResponseDTO;

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
	public List<AdDetailResponseDTO> getFilteredAds(List<String> receivedData) {
		
		logger.info("get filtered ads: " + receivedData.toString());
		
		return null;
	}

	/**
	 * 接收輸入條件 receivedData ["adDetail", "adId"] ，回傳廣告詳細資料
	 * @param receivedData
	 * @return
	 */
	public AdDetailResponseDTO getAdDetails(List<String> receivedData) {
		
		logger.info(receivedData.toString());
		
		AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.valueOf(receivedData.get(1)));
		return setAdView(adBean);
	}

	/**
	 * 接收輸入條件 receivedData ["adUpdate", "adId", "adType", "quantity"]，找到廣告並更新資料
	 * @param receivedData
	 * @return
	 */
	public AdDetailResponseDTO updateAdDetails(List<String> receivedData) {
		
		logger.info(receivedData.toString());
		
		String adType = (receivedData.get(2).equals("a")) ? "A廣告" : "B廣告"; // 可能要跟前端溝通名稱
		adBeanDao.updateAdBeanOnAdTypeAndPrice(
				Integer.parseInt(receivedData.get(1)), Integer.parseInt(receivedData.get(3))); // 要傳 adId 跟 adtypeId
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
	private AdDetailResponseDTO setAdView(AdBean adBean) {
		AdDetailResponseDTO adView = new AdDetailResponseDTO();
		adView.setAdId(adBean.getAdId());
		adView.setUserId((long)(adBean.getUser().getUserId()));
		adView.setUserName(adBean.getUser().getName());
		adView.setHouseId(adBean.getHouse().getHouseId());
		adView.setHouseTitle(adBean.getHouse().getTitle());
		adView.setAdtypeName(adBean.getAdtype().getAdName());
		adView.setAdPrice(adBean.getAdPrice());
		adView.setSubtotal(null);
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
	private List<AdDetailResponseDTO> setAdsView(List<AdBean> adBeanList) {
		List<AdDetailResponseDTO> responseDTOs = new ArrayList<AdDetailResponseDTO>();
		for(AdBean adBean : adBeanList) {
			AdDetailResponseDTO responseDTO = setAdView(adBean);
			responseDTOs.add(responseDTO);
		}
		
		return responseDTOs;
	}

}