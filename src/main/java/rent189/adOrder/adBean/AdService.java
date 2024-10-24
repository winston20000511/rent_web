package rent189.adOrder.adBean;

import java.util.ArrayList;
import java.util.List;

public class AdService {
	private AdBeanDao adBeanDao = new AdBeanDaoImpl();
	private ArrayList<AdDto> adList = new ArrayList<AdDto>();
	private ArrayList<AdBean> adBeans = new ArrayList<AdBean>();

	// retrieve data
	public ArrayList<AdDto> getAdInformationList(List<String> receivedData) {
		// receivedData = ("搜尋條件", "搜尋值", "付款狀態")

		// 廣告搜尋一覽表 - 1
		// if input is empty
		if (receivedData.get(1).equals("")) {
			adBeans = new ArrayList<AdBean>();
			// if input is empty and paid condition is not
			if (receivedData.get(2).equals("true")) {
				adBeans = adBeanDao.getAdBeansByIsPaid(true);
			} else if (receivedData.get(2).equals("false")) {
				adBeans = adBeanDao.getAdBeansByIsPaid(false);
			} else {
				// if paid condition is default = all
				adBeans = adBeanDao.getAdBeans();
			}

			for (AdBean adBean : adBeans) {
				AdDto adDto = setAdDto(adBean);
				adList.add(adDto);
			}
			
			System.out.println("adList: " +  adList);

			return adList;

		}

		// 廣告搜尋一覽表 - 2
		// if input isn't empty
		else if (receivedData.get(0).equals("adId")) {

			// if paid condition isn't empty
			if (receivedData.get(2).equals("true")) {
				adBeans = adBeanDao.getAdBeanByAdIdAndIsPaid(Integer.valueOf(receivedData.get(1)), true);
				for (AdBean adBean : adBeans) {
					// set AdDto: convert AdBean to AdDto
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			} else if (receivedData.get(2).equals("false")) {
				adBeans = adBeanDao.getAdBeanByAdIdAndIsPaid(Integer.valueOf(receivedData.get(1)), false);
				for (AdBean adBean : adBeans) {
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			} else {
				AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.valueOf(receivedData.get(1)));
				AdDto adDto = setAdDto(adBean);
				adList.add(adDto);

				return adList;
			}

		} else if (receivedData.get(0).equals("userId")) {

			// if paid condition isn't empty
			if (receivedData.get(2).equals("true")) {
				adBeans = adBeanDao.getAdBeanByUserIdAndIsPaid(Integer.valueOf(receivedData.get(1)), true);
				for (AdBean adBean : adBeans) {
					// set AdDto: convert AdBean to AdDto
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			} else if (receivedData.get(2).equals("false")) {
				adBeans = adBeanDao.getAdBeanByUserIdAndIsPaid(Integer.valueOf(receivedData.get(1)), false);
				for (AdBean adBean : adBeans) {
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			} else {
				adBeans = adBeanDao.getAdBeansByUserId(Integer.valueOf(receivedData.get(1)));
				for (AdBean adBean : adBeans) {
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			}

		} else if (receivedData.get(0).equals("houseId")) {

			// if paid condition isn't empty
			if (receivedData.get(2).equals("true")) {
				adBeans = adBeanDao.getAdBeanByHouseIdAndIsPaid(Integer.valueOf(receivedData.get(1)), true);
				for (AdBean adBean : adBeans) {
					// set AdDto: convert AdBean to AdDto
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			} else if (receivedData.get(2).equals("false")) {
				adBeans = adBeanDao.getAdBeanByHouseIdAndIsPaid(Integer.valueOf(receivedData.get(1)), false);
				for (AdBean adBean : adBeans) {
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			} else {
				adBeans = adBeanDao.getAdBeansByHouseId(Integer.valueOf(receivedData.get(1)));
				for (AdBean adBean : adBeans) {
					AdDto adDto = setAdDto(adBean);
					adList.add(adDto);
				}
				return adList;
			}
		}

		
		// 印出單一廣告資訊表
		if(receivedData.get(0).equals("adDetails")) {
			AdBean adBean = adBeanDao.getAdBeanByAdId(Integer.valueOf(receivedData.get(1)));
			AdDto adDto = setAdDto(adBean);
			adList.add(adDto);
			return adList;
		}
		
		
		// 以order Id找廣告
		if(receivedData.get(0).equals("orderDetails")) {
			adBeans = adBeanDao.getAdBeansByOrderId(receivedData.get(1));
			for (AdBean adBean : adBeans) {
				AdDto adDto = setAdDto(adBean);
				System.out.println("ad bean order id: " + adBean.getOrderId());
				adList.add(adDto);
			}
			return adList;
		}
		
		return null;
	}
	
	
	// update data
	public AdDto updateAdInformationList(List<String> receivedData){
		AdBean adBean = adBeanDao.updateAdBeanByAdId(Integer.parseInt(receivedData.get(0)), receivedData);
		AdDto adDto = setAdDto(adBean);
		return adDto;
	}
	
	// inner methods

	// set AdDto
	private AdDto setAdDto(AdBean adBean) {
		AdDto adDto = new AdDto();
		adDto.setAdId(adBean.getAdId());
		adDto.setUserId(adBean.getUserId());
		adDto.setUserName(adBean.getUserName());
		adDto.setHouseId(adBean.getHouseId());
		adDto.setAdType(adBean.getAdType());
		adDto.setAdDuration(adBean.getAdDuration());
		adDto.setAdPrice(adBean.getAdPrice());
		adDto.setQuantity(adBean.getQuantity());
		adDto.setCreatedAt(adBean.getCreatedAt());
		adDto.setIsPaid(adBean.getIsPaid());
		adDto.setOrderId(adBean.getOrderId());
		adDto.setPaidDate(adBean.getPaidDate());
		adDto.setExpiresAt(adBean.getExpiresAt());

		return adDto;
	}

}
