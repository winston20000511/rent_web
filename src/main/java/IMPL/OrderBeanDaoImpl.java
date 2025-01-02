
package IMPL;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.OrderBean;
import Dao.OrderBeanDao;
import dto.OrderDetailsResponseDTO;

public class OrderBeanDaoImpl implements OrderBeanDao{
	
	private Logger logger = Logger.getLogger(OrderBeanDaoImpl.class.getName());
	private Session session;
	
	public OrderBeanDaoImpl(Session session) {
		this.session = session;
	}
	
	@Override
	public List<OrderBean> getAllOrders() {
		Query<OrderBean> query = session.createQuery("from OrderBean", OrderBean.class);
		return query.getResultList();
	}

	@Override
	public List<OrderBean> filterOrders(String searchCondition, String orderStatus, String input) {
	    
		StringBuilder hqlstr = new StringBuilder("FROM OrderBean o ");
		
		boolean hasCondition = false;

	    // 根據 searchCondition 和 input 添加條件
	    if (searchCondition != null && input != null && !input.isEmpty()) {
	        if (searchCondition.equals("merchantTradNo")) {
	        	hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        	hqlstr.append("o.merchantTradNo LIKE :input ");
	        	hasCondition = true;
	        } else if (searchCondition.equals("userId")) {
	        	hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        	hqlstr.append("CAST(o.userId AS string) LIKE :input ");
	        	hasCondition = true;
	        }
	    }

	    // 根據 orderStatus 添加條件
	    if (orderStatus != null && !orderStatus.equals("all") && !orderStatus.isEmpty()) {
	    	hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        hqlstr.append("o.orderStatus = :orderStatus ");
	    }
	    
	    hqlstr.append(" ORDER BY o.merchantTradDate DESC");
	    
	    logger.info("HQL 查詢字串: " + hqlstr.toString());

	    // 創建 Query 物件
	    Query<OrderBean> query = session.createQuery(hqlstr.toString(), OrderBean.class);

	    if (searchCondition != null && input != null && !input.isEmpty()) {
	    	if(searchCondition.equals("merchantTradNo")) {
	    		query.setParameter("input", "%" + input + "%");
	    	}
	    	
	    	if(searchCondition.equals("userId")) {
	    		query.setParameter("input", "%" + input + "%");
	    	}
	    }

	    if (orderStatus != null && !orderStatus.isEmpty() && !orderStatus.equals("all")) {
	        query.setParameter("orderStatus", orderStatus);
	    }
	    
	    // 返回結果
	    List<OrderBean> resultList = query.getResultList();
	    return resultList;
	}




	@Override
	public OrderDetailsResponseDTO getOrderAdCombinedDataByTradNo(String orderId){
		String hqlstr = "select o.merchantTradNo, o.merchantTradDate, o.choosePayment, "
	              + "o.orderStatus, o.totalAmount, o.user.userName, "
	              + "a.adId, a.adtype, a.adPrice "
	              + "from OrderBean o join o.ads a "
	              + "where o.merchantTradNo = :orderId";
	    
	    Query<OrderDetailsResponseDTO> query = session.createQuery(hqlstr, OrderDetailsResponseDTO.class);
	    query.setParameter("merchantTradNo", orderId);

	    return query.getSingleResult();
	}


	@Override
	public OrderBean cancelOrderStatusByTradNo(String orderId) {
		OrderBean orderBean = getOrderBeanByTradNo(orderId);
		orderBean.setOrderStatus((short)0);
		return orderBean;
	}

	@Override
	public OrderBean getOrderBeanByTradNo(String orderId) {
		Query<OrderBean> query = session.createQuery("from OrderBean where merchantTradNo=:merchantTradNo", OrderBean.class);
		query.setParameter("merchantTradNo", orderId);
		return query.getSingleResult();
	}


}

