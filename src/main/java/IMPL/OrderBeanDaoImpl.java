
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
	    
		logger.info("有進來");
		
		StringBuilder hqlstr = new StringBuilder("FROM OrderBean o ");
		
		boolean hasCondition = false;

	    // 根據 searchCondition 和 input 添加條件
	    if (searchCondition != null && input != null && !input.isEmpty()) {
	        if ("merchantTradNo".equals(searchCondition)) {
	        	logger.info("選到merchantTradNo，加進去");
	        	hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        	hqlstr.append("o.merchantTradNo LIKE :input ");
	        	hasCondition = true;
	        } else if ("userId".equals(searchCondition)) {
	        	logger.info("選到userId，加進去");
	        	hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        	hqlstr.append("CAST(o.userId AS string) LIKE :input ");
	        	hasCondition = true;
	        }
	    }

	    // 根據 orderStatus 添加條件
	    if (!orderStatus.equals("all")) {
	    	logger.info("orderStatus不是all，加進去");
	    	hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        hqlstr.append("o.orderStatus = :orderStatus ");
	    }
	    
	    logger.info("HQL 查詢字串: " + hqlstr.toString());

	    // 創建 Query 物件
	    Query<OrderBean> query = session.createQuery(hqlstr.toString(), OrderBean.class);

	    if (searchCondition != null && input != null && !input.isEmpty()) {
	    	if(searchCondition.equals("merchantTradNo")) {
	    		logger.info("驗證search condition是訂單號碼");
	    		query.setParameter("input", "%" + input + "%");
	    	}
	    	
	    	if(searchCondition.equals("userId")) {
	    		logger.info("驗證search condition是使用者編號");
	    		query.setParameter("input", "%" + input + "%");
	    	}
	    }

	    if (!orderStatus.equals("all")) {
	    	logger.info("order status 有條件");
	        query.setParameter("orderStatus", orderStatus);
	    }

	    // 返回結果
	    List<OrderBean> resultList = query.getResultList();
	    return resultList;
	}




	@Override
	public OrderDetailsResponseDTO getOrderAdCombinedDataByTradNo(String tradNo){
	    String hqlstr = "select o.userid, o.merchanttradno, o.merchanttraddate, "
                + "o.choosepayment, o.orderstatus, o.totalamount, "
                + "a.username, a.houseid, a.adid, a.adtype, "
                + "a.adduration, a.adprice, a.quantity "
                + "from OrderBean o join o.ads a "
                + "where o.merchanttradno = :merchanttradno";
	    
	    Query<OrderDetailsResponseDTO> query = session.createQuery(hqlstr, OrderDetailsResponseDTO.class);
	    query.setParameter("merchanttradno", tradNo);

	    return query.getSingleResult();
	}


	@Override
	public OrderBean cancelOrderStatusByTradNo(String tradNo) {
		OrderBean orderBean = getOrderBeanByTradNo(tradNo);
		orderBean.setOrderStatus((short)0);
		return orderBean;
	}

	@Override
	public OrderBean getOrderBeanByTradNo(String tradeNo) {
		Query<OrderBean> query = session.createQuery("from OrderBean where merchanttradno=:merchanttradno", OrderBean.class);
		query.setParameter("merchanttradno", tradeNo);
		return query.getSingleResult();
	}


}

