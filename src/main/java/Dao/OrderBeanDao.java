package Dao;

import java.util.List;
import java.util.Map;

import Bean.OrderBean;

@SuppressWarnings("rawtypes")
public interface OrderBeanDao {
	
	// READ
	public List<OrderBean> getAllOrderBeans();
	public OrderBean getOrderBeanByTradNo(String tradeNo);
	
	// READ ORDER TABLE
	public List<Map> getOrderTableData();
	public List<Map> getOrderTableDataByTradNo(String tradNo);
	public List<Map> getOrderTableDataByUserId(Integer userId);
	public List<Map> getOrderTableDataByOrderStatus(Integer orderStatus);
	public List<Map> getOrderTableDataByTradNoAndOrderStatus(String tradeNo, Integer orderStatus);
	public List<Map> getOrderTableDataByUserIdAndOrderStatus(Integer userId, Integer orderStatus);
	

	// READ ORDER DETAILS
	public List<Object[]> getOrderAdCombinedDataByTradNo(String tradNo);
	
	// UPDATE
	public OrderBean cancelOrderStatusByTradNo(String tradNo);
	
}
