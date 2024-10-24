package rent189.adOrder.orderBean;

import java.util.ArrayList;

public interface OrderBeanDao {
	
	// c  clients create orders
	public boolean createOrderBean(OrderBean orderBean);
	
	// r
	public ArrayList<OrderBean> getOrderBeans();
	public OrderBean getOrderBeansByTradNo(String tradNo);
	public ArrayList<OrderBean> getOrderBeansByUserId(Integer userId);
	public ArrayList<OrderBean> getOrderBeanByOrderStatus(Boolean orderStatus);
	public ArrayList<OrderBean> getOrderByTradNoAndOrderStatus(String tradeNo, Boolean orderStatus);
	public ArrayList<OrderBean> getOrderByUserIdAndOrderStatus(Integer userId, Boolean orderStatus);
	
	// u
	public boolean updateOrderStatusByTradNo(String tradNo);
	
	// d
	public boolean deleteOrderBeanByTradNo(String tradNo);

}
