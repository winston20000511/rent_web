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
	public List<Map> getFilteredOrders(List<String> receivedData) {
		
		logger.info("get filtered orders: " + receivedData.toString());

		List<Map> dataList = new ArrayList<>();

		// 沒有輸入篩選值時
		if (receivedData.get(3).isEmpty()) {
			dataList = getOrdersByStatus(receivedData.get(2));
			reformedOrderDetails(dataList);
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
	public Map<String, Object> getOrderDetailsByTradNo(List<String> receivedData) {
		
		logger.info("get order details by tradNo: " + receivedData.toString());
		
		List<Object[]> details = orderBeanDao.getOrderAdCombinedDataByTradNo(receivedData.get(1));
		return setOrderDetails(details);
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
	private List<Map> getOrdersByStatus(String orderStatus) {
		switch (orderStatus) {
		case "active":
			return orderBeanDao.getOrderTableDataByOrderStatus(1);
		case "canceled":
			return orderBeanDao.getOrderTableDataByOrderStatus(0);
		default:
			return orderBeanDao.getOrderTableData();
		}
	}

	/**
	 * 以訂單號碼取得訂單資料
	 * @param receivedData
	 * @return
	 */
	private List<Map> getOrdersByTradNo(List<String> receivedData) {
		
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
	private List<Map> getOrdersByUserId(List<String> receivedData) {
		
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
	private void reformedOrderDetails(List<Map> dataList) {
		try {
			for (Map order : dataList) {
				if ((Integer) order.get("orderStatus") == 1) {
					order.put("orderStatus", "一般訂單");
				}
				else if ((Integer) order.get("orderStatus") == 0) {
					order.put("orderStatus", "已取消");
				}
				
				ZonedDateTime time = (ZonedDateTime)order.get("merchantTradDate");
				order.put("merchantTradDate", TimeForm.convertZonedDateTimeToString(time));
			}
		}catch(ClassCastException exception){
			logger.severe("型別轉換錯誤");
			logger.severe(exception.getMessage());
		}
	}

	/**
	 * 建立 order details 回傳資料
	 * @param details
	 * @return
	 */
	private Map<String, Object> setOrderDetails(List<Object[]> details) {

		if (!details.isEmpty()) {
			Map<String, Object> orderMap = new HashMap<>();
			Object[] orderDetails = details.get(0);
			orderMap.put("userId", orderDetails[0]);
			orderMap.put("merchantTradNo", orderDetails[1]);
			String timeStr = TimeForm.convertZonedDateTimeToString((ZonedDateTime)orderDetails[2]);
			orderMap.put("merchantTradDate", timeStr);
			orderMap.put("choosePayment", orderDetails[3]);
			if ((Integer) orderDetails[4] == 0)
				orderMap.put("orderStatus", "已取消");
			else if ((Integer) orderDetails[4] == 1)
				orderMap.put("orderStatus", "一般訂單");
			orderMap.put("totalAmount", orderDetails[5]);

			List<Map<String, Object>> adDataList = new ArrayList<>();
			for (Object[] adDetails : details) {
				Map<String, Object> adMap = new HashMap<>();
				adMap.put("userName", adDetails[6]);
				adMap.put("houseId", adDetails[7]);
				adMap.put("adId", adDetails[8]);
				adMap.put("adType", adDetails[9]);
				adMap.put("adDuration", adDetails[10]);
				adMap.put("adPrice", adDetails[11]);
				adMap.put("quantity", adDetails[12]);

				adDataList.add(adMap);
			}

			orderMap.put("ads", adDataList);
			return orderMap;

		} else {
			logger.info("找不到該筆訂單資料");
		}

		return null;
	}

}
