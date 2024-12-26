package Dao;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Session;

import Bean.OrderBean;
import IMPL.OrderBeanDaoImpl;
import dto.OrderDetailsDTO;
import util.TimeForm;

@SuppressWarnings("rawtypes")
public class OrderService {

	private Logger logger = Logger.getLogger(OrderService.class.getName());
	private OrderBeanDao orderBeanDao;

	public OrderService(Session session) {
		orderBeanDao = new OrderBeanDaoImpl(session);
	}

	/**
	 * 取得篩選的訂單 receivedData = ["search", "searchCondition", "orderStatus", "userInput"]
	 * @param receivedData
	 * @return
	 */
	public List<OrderDetailsDTO> getFilteredOrders(List<String> receivedData) {
		
		logger.info("get filtered orders: " + receivedData.toString());

		List<OrderDetailsDTO> dataList = new ArrayList<>();

		// 沒有輸入篩選值時
		if (receivedData.get(3).isEmpty()) {
			logger.info("沒有輸入篩選值");
			dataList = getOrdersByStatus(receivedData.get(2));
			return dataList;
		}

		// 有輸入篩選值時
		if (receivedData.get(1).equals("merchantTradNo")) {
			dataList = getOrdersByTradNo(receivedData);
		} else if (receivedData.get(1).equals("userId")) {
			dataList = getOrdersByUserId(receivedData);
		}

		reformedOrderDetails(dataList);
		return dataList.isEmpty() ? null : dataList;
	}
	
	/**
	 * 取得訂單詳細資料 receivedData = ["orderDetail", "merchantTradNo"]
	 * @param receivedData
	 * @return
	 */
	public OrderDetailsDTO getOrderDetailsByTradNo(List<String> receivedData) {
		
		logger.info("get order details by tradNo: " + receivedData.toString());
		
		return orderBeanDao.getOrderAdCombinedDataByTradNo(receivedData.get(1));
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
	
	/**
	 * 依訂單狀態篩選符合的訂單
	 * @param orderStatus
	 * @return
	 */
	private List<OrderDetailsDTO> getOrdersByStatus(String orderStatus) {
		switch (orderStatus) {
		case "active":
			return orderBeanDao.getOrderTableDataByOrderStatus((short)1);
		case "canceled":
			return orderBeanDao.getOrderTableDataByOrderStatus((short)0);
		default:
			logger.info("order service: " + orderBeanDao.getOrderTableData());
			return orderBeanDao.getOrderTableData();
		}
		
	}

	/**
	 * 以訂單號碼取得訂單資料
	 * @param receivedData
	 * @return
	 */
	private List<OrderDetailsDTO> getOrdersByTradNo(List<String> receivedData) {
		
		logger.info("get orders by tradNo: " + receivedData);
		
		String tradNo = receivedData.get(3);
		switch (receivedData.get(2)) {
		case "active":
			return orderBeanDao.getOrderTableDataByTradNoAndOrderStatus(tradNo, 1);
		case "canceled":
			return orderBeanDao.getOrderTableDataByTradNoAndOrderStatus(tradNo, 0);
		default:
			return orderBeanDao.getOrderTableDataByTradNo(tradNo);
		}
		
	}

	/**
	 * 以 user id 取的訂單資料
	 * @param receivedData
	 * @return
	 */
	private List<OrderDetailsDTO> getOrdersByUserId(List<String> receivedData) {
		
		logger.info("get orders by user id: " + receivedData);
		
		int userId = Integer.valueOf(receivedData.get(3));
		switch (receivedData.get(2)) {
		case "active":
			return orderBeanDao.getOrderTableDataByUserIdAndOrderStatus(userId, 1);
		case "canceled":
			return orderBeanDao.getOrderTableDataByUserIdAndOrderStatus(userId, 0);
		default:
			return orderBeanDao.getOrderTableDataByUserId(userId);
		}
	}
	
	/**
	 * 處理訂單詳細資料中的資料顯示內容
	 * @param dataList
	 */
	private void reformedOrderDetails(List<OrderDetailsDTO> dataList) {
	    try {
	    	
	    	logger.info("reformed order details: " + dataList);
	        for (OrderDetailsDTO order : dataList) {
	        }
	    } catch (ClassCastException exception) {
	        logger.severe("型別轉換錯誤");
	        logger.severe(exception.getMessage());
	    }
	}
}
