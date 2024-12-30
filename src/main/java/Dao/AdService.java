package Dao;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	public List<AdDetailResponseDTO> filterAds(Map<String, Object> filterParams) {
		
		logger.info("get filtered ads: " + filterParams.toString());

		String condition = (String) filterParams.get("searchCondition");
		String paidCondition = (String) filterParams.get("paidCondition");
		String input = (String) filterParams.get("input");
		
		List<AdBean> filterAds = adBeanDao.filterAds(condition, paidCondition, input);
		return setupAdDetailResponseDTOs(filterAds);
	}

	/**
	 * 接收輸入條件 receivedData ["adDetail", "adId"] ，回傳廣告詳細資料
	 * @param receivedData
	 * @return
	 */
	public AdDetailResponseDTO checkAdDetails(Map<String, Object> filterParams) {
		
		Double adId = (Double)filterParams.get("adId");
		Long adIdL = adId.longValue();
		logger.info("篩選 AD ID: " + adId);
		
		AdBean ad = adBeanDao.checkAdDetails(adIdL);
		return setupAdDetailResponseDTO(ad);
	}

	/**
	 * 接收輸入條件 receivedData ["adUpdate", "adId", "adType", "quantity"]，找到廣告並更新資料
	 * @param receivedData
	 * @return
	 */
//	public AdDetailResponseDTO updateAdDetails(List<String> receivedData) {
//		
//		logger.info(receivedData.toString());
//		
//		String adType = (receivedData.get(2).equals("a")) ? "A廣告" : "B廣告"; // 可能要跟前端溝通名稱
//		adBeanDao.updateAdBeanOnAdTypeAndPrice(
//				Integer.parseInt(receivedData.get(1)), Integer.parseInt(receivedData.get(3))); // 要傳 adId 跟 adtypeId
//		AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.parseInt(receivedData.get(1)));
//		return setAdView(adBean);
//	}

	/**
	 * 刪除廣告 receivedData
	 * @param receivedData
	 * @return
	 */
	public boolean deleteAd(Long adId) {
		logger.info("delete ad: " + adId);
		return adBeanDao.deleteAdBeanByAdId(adId);
	}
	
	
	/**
	 * 展示廣告詳細內容
	 * @param adBean
	 * @return
	 */
	private AdDetailResponseDTO setupAdDetailResponseDTO(AdBean adBean) {
		AdDetailResponseDTO responseDTO = new AdDetailResponseDTO();
		responseDTO.setAdId(adBean.getAdId());
		responseDTO.setUserId((long)(adBean.getUser().getUserId()));
		responseDTO.setUserName(adBean.getUser().getName());
		responseDTO.setHouseId(adBean.getHouse().getHouseId());
		responseDTO.setHouseTitle(adBean.getHouse().getTitle());
		responseDTO.setAdtypeName(adBean.getAdtype().getAdName());
//		responseDTO.setAdPrice(adBean.getAdPrice());
		responseDTO.setIsPaid(adBean.getIsPaid());
		responseDTO.setOrderId(adBean.getOrderId());
		responseDTO.setPaidDate(adBean.getPaidDate());
		
		if(adBean.getIsPaid()) {
			ZonedDateTime expiryDate = calculateExpiryDate(adBean.getPaidDate(), adBean.getAdtype().getAdName());
			responseDTO.setExpiryDate(expiryDate);
		}
		
		return responseDTO;
	}
	
	/**
	 * 建立廣告詳細內容物件
	 * @param adBeanList
	 * @return
	 */
	private List<AdDetailResponseDTO> setupAdDetailResponseDTOs(List<AdBean> ads) {
		List<AdDetailResponseDTO> responseDTOs = new ArrayList<AdDetailResponseDTO>();
		for(AdBean adBean : ads) {
			AdDetailResponseDTO responseDTO = setupAdDetailResponseDTO(adBean);
			responseDTOs.add(responseDTO);
		}
		
		return responseDTOs;
	}

	private ZonedDateTime calculateExpiryDate(ZonedDateTime paidDate, String adName) {
		String numericPart = adName.replaceAll("\\D+","");
		
		int days = Integer.parseInt(numericPart);
		logger.info("要轉換的廣告天數 " + days);
		
		ZonedDateTime expiryDate = paidDate.plusDays(days);
		
		return expiryDate;
	}
}