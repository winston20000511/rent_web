package IMPL;

import org.hibernate.Session;

import Bean.AdtypeBean;
import Dao.AdtypeBeanDao;

public class AdtypeBeanDaoImpl implements AdtypeBeanDao{

	private Session session;

	public AdtypeBeanDaoImpl(Session session) {
		this.session = session;
	}
	
	@Override
	public AdtypeBean getAdtypeBeanByAdtypeId(Integer adtypeId) {
		return session.get(AdtypeBean.class, adtypeId);
	}

}
