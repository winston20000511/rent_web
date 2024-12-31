package IMPL;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.AdBean;
import Bean.AdtypeBean;
import Dao.AdBeanDao;

public class AdBeanDaoImpl implements AdBeanDao {

	private Logger logger = Logger.getLogger(OrderBeanDaoImpl.class.getName());
	private Session session;

	public AdBeanDaoImpl(Session session) {
		this.session = session;
	}

	@Override
	public boolean createAdBean(AdBean adBean) {
		try {
			session.persist(adBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<AdBean> getAllAdBeans() {
		Query<AdBean> query = session.createQuery("from AdBean", AdBean.class);
		return query.list();
	}

	@Override
	public List<AdBean> filterAds(String searchCondition, String paidCondition, String input) {
	    StringBuilder hqlstr = new StringBuilder("FROM AdBean a ");
	    boolean hasCondition = false;

	    if (searchCondition != null && input != null && !input.isEmpty()) {
	        if (searchCondition.equals("adId")) {
	            hqlstr.append(hasCondition ? " AND " : " WHERE ");
	            hqlstr.append("CAST(a.adId AS string) LIKE :input ");
	            hasCondition = true;
	        } else if (searchCondition.equals("userId")) {
	            hqlstr.append(hasCondition ? " AND " : " WHERE ");
	            hqlstr.append("CAST(a.user.id AS string) LIKE :input ");
	            hasCondition = true;
	        } else if (searchCondition.equals("houseId")) {
	            hqlstr.append(hasCondition ? " AND " : " WHERE ");
	            hqlstr.append("CAST(a.house.id AS string) LIKE :input "); 
	            hasCondition = true;
	        }
	    }

	    // 根據付款狀況來篩選廣告
	    if (paidCondition != null && !paidCondition.equals("all") && !paidCondition.isEmpty()) {
	        hqlstr.append(hasCondition ? " AND " : " WHERE ");
	        hqlstr.append("a.isPaid = :isPaid ");
	    }

	    logger.info("HQL 查詢字串: " + hqlstr.toString());

	    // 執行查詢
	    Query<AdBean> query = session.createQuery(hqlstr.toString(), AdBean.class);

	    // 根據條件設置搜尋參數
	    if (searchCondition != null && input != null && !input.isEmpty()) {
	        query.setParameter("input", "%" + input + "%"); 
	    }

	    if (paidCondition != null && !paidCondition.isEmpty() && !paidCondition.equals("all")) {
	        query.setParameter("isPaid", paidCondition);
	    }

	    // 返回結果
	    List<AdBean> resultList = query.getResultList();
	    return resultList;
	}



	@Override
	public AdBean checkAdDetails(Long adId) {
        String hql = "FROM AdBean a WHERE a.adId = :adId";
        Query<AdBean> query = session.createQuery(hql, AdBean.class);
        query.setParameter("adId", adId);

        return query.getSingleResult();
	}

	@Override
	public boolean deleteAdBeanByAdId(Long adId) {
		try {
			Query<?> query = session.createQuery("delete from AdBean where adId = :adId");
			query.setParameter("adId", adId);
			int result = query.executeUpdate();
			return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public AdBean editAd(Long adId, Integer newAdtypeId) {
		// 前端取得廣告資訊 + 廣告編號
		
		// 取出要修改的廣告
		String hql = "FROM AdBean a WHERE a.adId = :adId";
		Query<AdBean> query = session.createQuery(hql, AdBean.class);
        query.setParameter("adId", adId);
        AdBean ad = query.getSingleResult();

        // 取出選取的廣告類型
        String adtypeHql = "From AdtypeBean adt WHERE adt.id = :adtypeId";
     	Query<AdtypeBean> adtQuery = session.createQuery(adtypeHql, AdtypeBean.class);
        adtQuery.setParameter("adtypeId", newAdtypeId);
     	// 送出修改內容
        AdtypeBean adtype = adtQuery.getSingleResult();
        ad.setAdtype(adtype);
        session.saveOrUpdate(ad);

		return ad;
	}

}
