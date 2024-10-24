package rent189.adOrder.orderBean;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
	private OrderBeanDao orderBeanDao = new OrderBeanDaoImpl();
	private ArrayList<OrderDto> orderList = new ArrayList<OrderDto>();
	private ArrayList<OrderBean> orderBeans = new ArrayList<OrderBean>();

	// retrieve data
	public ArrayList<OrderDto> getOrderInformationList(List<String> receivedData) {
		// receivedData = ("搜尋條件", "搜尋值", "付款狀態")

		// 訂單搜尋一覽表 - 1
		// if input is empty
		if (receivedData.get(1).equals("")) {
			orderBeans = new ArrayList<OrderBean>();
			// if input is empty and order status is not
			if (receivedData.get(2).equals("true")) {
				orderBeans = orderBeanDao.getOrderBeanByOrderStatus(true);
			} else if (receivedData.get(2).equals("false")) {
				orderBeans = orderBeanDao.getOrderBeanByOrderStatus(false);
			} else {
				// if paid condition is default = all
				orderBeans = orderBeanDao.getOrderBeans();
			}

			for (OrderBean orderBean : orderBeans) {
				OrderDto orderDto = setOrderDto(orderBean);
				orderList.add(orderDto);
			}

			return orderList;

		}

		// 訂單搜尋一覽表 - 2
		// if input isn't empty
		if (receivedData.get(0).equals("merchantTradNo")) {
			// if order status isn't empty
			if (receivedData.get(2).equals("true")) {
				orderBeans = orderBeanDao.getOrderByTradNoAndOrderStatus(receivedData.get(1), true);
				for (OrderBean orderBean : orderBeans) {
					// set oderDto: convert OrderBean to OrderDto
					OrderDto orderDto = setOrderDto(orderBean);
					orderList.add(orderDto);
				}
			} else if (receivedData.get(2).equals("false")) {
				orderBeans = orderBeanDao.getOrderByTradNoAndOrderStatus(receivedData.get(1), true);
				for (OrderBean orderBean : orderBeans) {
					OrderDto adDto = setOrderDto(orderBean);
					orderList.add(adDto);
				}
			} else {
				// order status is all
				OrderBean orderBean = orderBeanDao.getOrderBeansByTradNo(receivedData.get(1));
				OrderDto orderDto = setOrderDto(orderBean);
				orderList.add(orderDto);
			}

			return orderList;

		} else if (receivedData.get(0).equals("userId")) {
			// if order status isn't all
			if (receivedData.get(2).equals("true")) {
				orderBeans = orderBeanDao.getOrderByUserIdAndOrderStatus(Integer.valueOf(receivedData.get(1)), true);
				for (OrderBean orderBean : orderBeans) {
					OrderDto orderDto = setOrderDto(orderBean);
					orderList.add(orderDto);
				}
			} else if (receivedData.get(2).equals("false")) {
				orderBeans = orderBeanDao.getOrderByUserIdAndOrderStatus(Integer.valueOf(receivedData.get(1)), false);
				for (OrderBean orderBean : orderBeans) {
					OrderDto orderDto = setOrderDto(orderBean);
					orderList.add(orderDto);
				}
			} else {
				orderBeans = orderBeanDao.getOrderBeansByUserId(Integer.valueOf(receivedData.get(1)));
				for (OrderBean orderBean : orderBeans) {
					OrderDto orderDto = setOrderDto(orderBean);
					orderList.add(orderDto);
				}
			}

			return orderList;

		}

		// 印出單一廣告資訊表 
		if(receivedData.get(0).equals("orderDetails")) { 
			// 取得訂單詳細
			OrderBean orderBean = orderBeanDao.getOrderBeansByTradNo(receivedData.get(1)); 
			OrderDto orderDto = setOrderDto(orderBean); 
			orderList.add(orderDto); 
		
			// 納入 GetAdDataServlet 並提供 訂單號碼
			
			
			return orderList;
		}
		
		return null;
	}
	
	// 修改 Order Bean 內容
	public OrderDto cancelOrder(String tradNo) {
		boolean result = orderBeanDao.updateOrderStatusByTradNo(tradNo);
		System.out.println("update result: " + result);
		if(result) {
			OrderBean canceledOrderBean = orderBeanDao.getOrderBeansByTradNo(tradNo);
			System.out.println(canceledOrderBean.getMerchantTradNo() + " 訂單狀態：已取消");
			OrderDto canceledOrder = setOrderDto(canceledOrderBean); 
			return canceledOrder;
		}
		return null;
	}
	
	
	// inner methods

	// set AdDto
	private OrderDto setOrderDto(OrderBean orderBean) {
		OrderDto orderDto = new OrderDto();
		orderDto.setUserId(orderBean.getUserId());
		orderDto.setMerchantId(orderBean.getMerchantId());
		orderDto.setMerchantTradNo(orderBean.getMerchantTradNo());
		orderDto.setMerchantTradDate(orderBean.getMerchantTradDate());
		orderDto.setPaymentType(orderBean.getPaymentType());
		orderDto.setTotalAmount(orderBean.getTotalAmount());
		orderDto.setTradeDesc(orderBean.getTradeDesc());
		orderDto.setItemName(orderBean.getItemName());
		orderDto.setOrderStatus(orderBean.getOrderStatus());
		orderDto.setReturnUrl(orderBean.getReturnUrl());
		orderDto.setChoosePayment(orderBean.getChoosePayment());
		orderDto.setCheckMacValue(orderBean.getCheckMacValue());
		orderDto.setEncryptType(orderBean.getEncryptType());

		return orderDto;
	}

}
