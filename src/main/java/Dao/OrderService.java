package Dao;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import Bean.OrderBean;
import IMPL.OrderBeanDaoImpl;
import util.TimeForm;

@SuppressWarnings("rawtypes")
public class OrderService {

	/*
	 * 1. order data for datatable -> order data view only 
	 * 2. order Details -> order + ads details
	 * 3. cancel order
	 */

	private OrderBeanDao orderBeanDao;

	public OrderService(Session session) {
		orderBeanDao = new OrderBeanDaoImpl(session);
	}

	// 1. get filtered orders for data table
	public List<Map> getFilteredOrders(List<String> receivedData) {
		// receivedData = ["search", "searchCondition", "orderStatus", "userInput"]

		List<Map> dataList = new ArrayList<>();

		// when there is no input
		if (receivedData.get(3).isEmpty()) {
			dataList = getOrdersByStatus(receivedData.get(2));
			reformedOrderDetails(dataList);
			return dataList;
		}

		// when there is an input
		if (receivedData.get(1).equals("merchantTradNo")) {
			dataList = getOrdersByTradNo(receivedData);
		} else if (receivedData.get(1).equals("userId")) {
			dataList = getOrdersByUserId(receivedData);
		}

		reformedOrderDetails(dataList);
		return dataList.isEmpty() ? null : dataList;
	}
	
	// 2. get order Details 
	public Map<String, Object> getOrderDetailsByTradNo(List<String> receivedData) {
		// receivedData = ["orderDetail", "merchantTradNo"]
		List<Object[]> details = orderBeanDao.getOrderAdCombinedDataByTradNo(receivedData.get(1));
		return setOrderDetails(details);
	}

	// 3. cancel order = set orderStatus as "canceled"
	// receivedData = ["canceled", "tradeNo"]
	public Map<String, Object> cancelOrder(List<String> receivedData) {
		Map<String, Object> orderTableData = new HashMap<String, Object>();
		OrderBean canceledOrderBean = orderBeanDao.cancelOrderStatusByTradNo(receivedData.get(1));
		orderTableData.put("userId", canceledOrderBean.getUserid());
		orderTableData.put("merchantTradNo", canceledOrderBean.getMerchanttradno());
		ZonedDateTime merchanttraddate = canceledOrderBean.getMerchanttraddate();
		String tradDateStr = TimeForm.convertZonedDateTimeToString(merchanttraddate);
		orderTableData.put("merchantTradDate", tradDateStr);
		orderTableData.put("orderStatus", canceledOrderBean.getOrderstatus());
		return orderTableData;
	}

	
	
	/* inner methods */
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

	private List<Map> getOrdersByTradNo(List<String> receivedData) {
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

	private List<Map> getOrdersByUserId(List<String> receivedData) {
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
	
	@SuppressWarnings("unchecked")
	private void reformedOrderDetails(List<Map> dataList) {
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
	}

	// set up order details
	private Map<String, Object> setOrderDetails(List<Object[]> details) {

		if (!details.isEmpty()) {
			// set up order details
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

			// set up ad details
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
			System.out.println("No result was found for the provided tradNo.");
		}

		return null;
	}

}
