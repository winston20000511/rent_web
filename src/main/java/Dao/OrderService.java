package Dao;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Session;

import Bean.AdBean;
import Bean.OrderBean;
import IMPL.OrderBeanDaoImpl;
import dto.OrderDetailsResponseDTO;
import util.TimeForm;

public class OrderService {

	private Logger logger = Logger.getLogger(OrderService.class.getName());
	private OrderBeanDao orderBeanDao;

	public OrderService(Session session) {
		orderBeanDao = new OrderBeanDaoImpl(session);
	}
	
	public List<OrderDetailsResponseDTO> getAllOrders(){
		
		logger.info("get all orders");
		List<OrderBean> allOrders = orderBeanDao.getAllOrders();
		List<OrderDetailsResponseDTO> orderDetailsDTOs = setupOrderDetailDTOs(allOrders);
		
		logger.info("get all orders: " + orderDetailsDTOs.toString());
		
		return orderDetailsDTOs;
	}

	/**
	 * 取得篩選的訂單 filterParams = {"searchCondition":"","orderStatus":"","input":""}
	 * @param receivedData
	 * @return
	 */
	public List<OrderDetailsResponseDTO> filterOrders(Map<String, Object> filterParams) {
		
		logger.info("get filtered orders: " + filterParams.toString());

		String condition = (String) filterParams.get("searchCondition");
		String orderStatus = (String) filterParams.get("orderStatus");
		String input = (String) filterParams.get("input");
		
		List<OrderBean> filterOrders = orderBeanDao.filterOrders(condition, orderStatus, input);
		List<OrderDetailsResponseDTO> orderDetailDTOs = setupOrderDetailDTOs(filterOrders);
		
		return orderDetailDTOs;
	}

	/**
	 * 更新訂單狀態，變成取消 receivedData = ["canceled", "tradeNo"]
	 * @param receivedData
	 * @return
	 */
	public Map<String, Object> cancelOrder(List<String> receivedData) {
		
		logger.info("cancel order: " + receivedData.toString());
		
		Map<String, Object> orderTableData = new HashMap<String, Object>();
		OrderBean canceledOrderBean = orderBeanDao.cancelOrderStatusByTradNo(receivedData.get(1));
		orderTableData.put("userId", canceledOrderBean.getUserId());
		orderTableData.put("merchantTradNo", canceledOrderBean.getMerchantTradNo());
		ZonedDateTime merchanttraddate = canceledOrderBean.getMerchantTradDate();
		String tradDateStr = TimeForm.convertZonedDateTimeToString(merchanttraddate);
		orderTableData.put("merchantTradDate", tradDateStr);
		orderTableData.put("orderStatus", canceledOrderBean.getOrderStatus());
		return orderTableData;
	}
	
	
	private List<OrderDetailsResponseDTO> setupOrderDetailDTOs(List<OrderBean> orders) {
		List<OrderDetailsResponseDTO> orderDetails = new ArrayList<>();
		List<Long> houseIds = new ArrayList<>();
		List<String> houseTitles = new ArrayList<>();
		List<Long> adIds = new ArrayList<>();
		List<String> adtypes = new ArrayList<>();
		
		for(OrderBean order : orders) {
			OrderDetailsResponseDTO detailDTO = new OrderDetailsResponseDTO();
			detailDTO.setUserId(order.getUserId());
			detailDTO.setUserName(order.getUser().getName());
			detailDTO.setOrderId(order.getMerchantTradNo());
			detailDTO.setPaidDate(TimeForm.convertZonedDateTimeToString(order.getMerchantTradDate()));
			detailDTO.setPaymentMethod(order.getChoosePayment());
			detailDTO.setOrderStatus(order.getOrderStatus());
			detailDTO.setAdPeriod(null); // 要另外計算
			List<AdBean> ads = order.getAds();
			for(AdBean ad : ads) {
				houseIds.add(ad.getHouse().getHouseId());
				houseTitles.add(ad.getHouse().getTitle());
				adIds.add(ad.getAdId());
				adtypes.add(ad.getAdtype().getAdName());
			}
			detailDTO.setHouseIds(houseIds);
			detailDTO.setHouseTitles(houseTitles);
			detailDTO.setAdIds(adIds);
			detailDTO.setAdtypes(adtypes);
			
			orderDetails.add(detailDTO);
		}
		
		return orderDetails;
	}
	
	/**
	 * 處理訂單詳細資料中的資料顯示內容
	 * @param dataList
	 */
	private void reformedOrderDetails(List<OrderDetailsResponseDTO> dataList) {
	    try {
	    	
	    	logger.info("reformed order details: " + dataList);
	        for (OrderDetailsResponseDTO order : dataList) {
	        }
	    } catch (ClassCastException exception) {
	        logger.severe("型別轉換錯誤");
	        logger.severe(exception.getMessage());
	    }
	}
	
}
