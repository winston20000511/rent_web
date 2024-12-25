package IMPL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;

import Dao.AdDao;
import util.HibernateUtil;

public class AdDaoImpl implements AdDao {

    @Override
    public Map<String, Integer> getadtypeCount() {
        Map<String, Integer> adCounts = new HashMap<>();
        String hql = "SELECT adtype, COUNT(*) FROM AdBean GROUP BY adtype";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();

            for (Object[] result : results) {
                String adType = (String) result[0];
                Integer count = ((Long) result[1]).intValue(); // HQL count returns Long, cast to Integer
                adCounts.put(adType, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adCounts;
    }
}
