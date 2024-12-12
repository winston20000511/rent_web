package IMPL;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Bean.houseBACKBean;
import Bean.houseTableBean;
import Dao.houseDAO;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class houseIMPL implements houseDAO {

    @Override
    public houseTableBean findHouseById(int houseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(houseTableBean.class, houseId);
        }
    }

    @Override
    public boolean createHouse(houseTableBean houseTB) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(houseTB);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateHouse(Map<String, Object> updates, int houseId) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start a transaction
        	if (session == null) {
        	    throw new IllegalStateException("Session is not available");
        	}
            transaction = session.beginTransaction();
            
            // Retrieve the existing house entity
            houseTableBean house = session.get(houseTableBean.class, houseId);
            if (house == null) {
                return false; // No house found with the given ID
            }

            // Update fields based on the provided updates map
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String column = entry.getKey();
                Object value = entry.getValue();

                switch (column) {
                    case "title":
                        house.setTitle((String) value);
                        break;
                    case "price":
                        house.setPrice((Integer) value);
                        break;
                    case "description":
                        house.setDescription((String) value);
                        break;
                    case "size":
                        house.setSize((Integer) value);
                        break;
                    case "city":
                        house.setCity((String) value);
                        break;
                    case "township":
                        house.setTownship((String) value);
                        break;
                    case "street":
                        house.setStreet((String) value);
                        break;
                    case "room":
                        house.setRoom((Byte) value);
                        break;
                    case "bathroom":
                        house.setBathroom((Byte) value);
                        break;
                    case "livingroom":
                        house.setLivingroom((Byte) value);
                        break;
                    case "kitchen":
                        house.setKitchen((Byte) value);
                        break;
                    case "housetype":
                        house.setHousetype((Byte) value);
                        break;
                    case "floor":
                        house.setFloor((Byte) value);
                        break;
                    case "atticAddition":
                        house.setAtticAddition((Boolean) value);
                        break;
                    // Add more cases as needed for other fields
                }
            }

            // Commit the transaction
            transaction.commit();
            System.out.println("已修改了房屋資料");
            session.close();
            
            return true; // Update successful
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback in case of error
            e.printStackTrace(); // Handle exceptions appropriately
        }
        
        return false; // Update failed
    }

    @Override
    public boolean deleteHouseById(int houseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            houseTableBean house = session.get(houseTableBean.class, houseId);
            if (house != null) {
                session.delete(house);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<houseTableBean> getAllHouses() {
        List<houseTableBean> list = new ArrayList<houseTableBean>();
        
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
        	session.beginTransaction();
            Query<houseTableBean> query = session.createQuery("From houseTableBean", houseTableBean.class);
            list= query.getResultList();
       
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        
        return list;
    }}