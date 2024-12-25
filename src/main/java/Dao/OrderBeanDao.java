package Dao;

import java.util.List;
import java.util.Map;

import Bean.OrderBean;

@SuppressWarnings("rawtypes")
public interface OrderBeanDao {
	
	List<OrderBean> getAllOrderBeans();
	OrderBean getOrderBeanByTradNo(String tradeNo);
	
	List<Map> getOrderTableData();
	List<Map> getOrderTableDataByTradNo(String tradNo);
	List<Map> getOrderTableDataByUserId(Integer userId);
	List<Map> getOrderTableDataByOrderStatus(Integer orderStatus);
	List<Map> getOrderTableDataByTradNoAndOrderStatus(String tradeNo, Integer orderStatus);
	List<Map> getOrderTableDataByUserIdAndOrderStatus(Integer userId, Integer orderStatus);
	
	List<Object[]> getOrderAdCombinedDataByTradNo(String tradNo);
	
	OrderBean cancelOrderStatusByTradNo(String tradNo);
	
}
