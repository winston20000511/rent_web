package Dao;

import java.util.List;

import Bean.OrderBean;
import DTO.OrderDetailsDTO;

public interface OrderBeanDao {
	
	List<OrderBean> getAllOrderBeans();
	OrderBean getOrderBeanByTradNo(String tradeNo);
	
	List<OrderDetailsDTO> getOrderTableData();
	List<OrderDetailsDTO> getOrderTableDataByTradNo(String tradNo);
	List<OrderDetailsDTO> getOrderTableDataByUserId(Integer userId);
	List<OrderDetailsDTO> getOrderTableDataByOrderStatus(Short orderStatus);
	List<OrderDetailsDTO> getOrderTableDataByTradNoAndOrderStatus(String tradeNo, Integer orderStatus);
	List<OrderDetailsDTO> getOrderTableDataByUserIdAndOrderStatus(Integer userId, Integer orderStatus);
	
	OrderDetailsDTO getOrderAdCombinedDataByTradNo(String tradNo);
	
	OrderBean cancelOrderStatusByTradNo(String tradNo);
	
}
