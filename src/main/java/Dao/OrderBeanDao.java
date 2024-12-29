package Dao;

import java.util.List;

import Bean.OrderBean;
import dto.OrderDetailsResponseDTO;

public interface OrderBeanDao {
	
	OrderBean getOrderBeanByTradNo(String tradeNo);
	List<OrderBean> getAllOrders();
	List<OrderBean> filterOrders(String searchCondition, String orderStatus, String input);
	OrderDetailsResponseDTO getOrderAdCombinedDataByTradNo(String tradNo);
	OrderBean cancelOrderStatusByTradNo(String tradNo);
	
}
