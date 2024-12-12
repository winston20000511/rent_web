package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import Bean.formComplationBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HibernateUtil;

@WebServlet("/Complaints/*")
public class formcrud extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		Transaction transaction = null;
		String pathInfo = request.getPathInfo();
		String renderPage = request.getParameter("renderPage"); //add 1208
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try (PrintWriter out = response.getWriter()) {
			
			 if ("true".equalsIgnoreCase(renderPage)) {
		            List<formComplationBean> complaintsList = session
		                    .createQuery("from fromComplationBean", formComplationBean.class)
		                    .getResultList();
		            request.setAttribute("complaints", complaintsList); 
		            request.getRequestDispatcher("/WEB-INF/views/complaints.jsp").forward(request, response);
		            return; 
		        }
			 
			
			transaction = session.beginTransaction();
			//compile
			if (pathInfo != null && pathInfo.length() > 1) {
				System.out.println("get if compel");
				int complaintId = Integer.parseInt(pathInfo.substring(1));
				formComplationBean complaint = session.get(formComplationBean.class, complaintId);
				if (complaint != null) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("complaints_id", complaint.getComplaints_id());
					jsonObject.put("user_id", complaint.getUser_id());
					jsonObject.put("username", complaint.getUsername());
					jsonObject.put("category", complaint.getCategory());
					jsonObject.put("subject", complaint.getSubject());
					jsonObject.put("content", complaint.getContent());
					jsonObject.put("note", complaint.getNote());
					jsonObject.put("status", complaint.getStatus());
					jsonObject.put("submission_date",
							complaint.getSubmission_date() != null ? complaint.getSubmission_date().toString() : "");
					out.print(jsonObject.toString());
				} else {
					response.getWriter().println("{\"error\":\"Complaint not found\"}");
				}
			} else {
				System.out.println("GET else create new data");
				List<formComplationBean> complaintsList = session.createQuery("from formComplationBean", formComplationBean.class)
						.getResultList();

				JSONArray jsonArray = new JSONArray();

				for (formComplationBean complaint : complaintsList) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("complaints_id", complaint.getComplaints_id());
					jsonObject.put("user_id", complaint.getUser_id());
					jsonObject.put("username", complaint.getUsername());
					jsonObject.put("category", complaint.getCategory());
					jsonObject.put("subject", complaint.getSubject());
					jsonObject.put("content", complaint.getContent());
					jsonObject.put("note", complaint.getNote());
					jsonObject.put("status", complaint.getStatus());
								
					jsonObject.put("submission_date",
							complaint.getSubmission_date() != null ? new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
									.format(complaint.getSubmission_date())
											: "");			
										
					jsonArray.put(jsonObject);
				}
				out.print(jsonArray.toString());
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			response.getWriter().println("{\"error\":\"Data fetch failed\"}");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		Transaction transaction = null;

		try (BufferedReader reader = request.getReader()) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			formComplationBean newComplaint = new formComplationBean();
			newComplaint.setUser_id(jsonObject.getInt("user_id"));
			newComplaint.setUsername(jsonObject.getString("username"));
			newComplaint.setCategory(jsonObject.getString("category"));
			newComplaint.setSubject(jsonObject.getString("subject"));
			newComplaint.setContent(jsonObject.getString("content"));
			newComplaint.setNote(jsonObject.optString("note", "無"));
			newComplaint.setStatus(jsonObject.optString("status", "待處理"));
			

			session.persist(newComplaint);
			transaction.commit();
			response.getWriter().println("{\"success\":\"Data saved successfully\"}");
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			response.getWriter().println("{\"error\":\"Data save failed\"}");
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    response.setContentType("application/json;charset=UTF-8");
	    Transaction transaction = null;
	    
	    System.out.println("arrvial put");
	    try (BufferedReader reader = request.getReader()) {
	        StringBuilder sb = new StringBuilder();
	        String line;
	        
		    System.out.println("Received Request Method: " + request.getMethod());
		    System.out.println("Received JSON1: " + sb.toString());
		    
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        System.out.println("Received JSON2: " + sb.toString());
	        
	        JSONObject jsonObject = new JSONObject(sb.toString());
	        
	        int complaintId = jsonObject.getInt("complaint_id");

	        if (!jsonObject.has("complaint_id")) {
	            response.getWriter().println("{\"error\":\"Complaint ID is required\"}");
	            return;
	        }


	        Session session = HibernateUtil.getSessionFactory().openSession(); //.getCurrentSession();
	        transaction = session.beginTransaction();

	        formComplationBean complaint = session.get(formComplationBean.class, complaintId);
	        System.out.println("check complaintId : "+complaintId);
	        String newStatus = jsonObject.optString("status", null);
	        boolean hasUserId = jsonObject.has("user_id");
	       
	        if (complaint != null) {
	            if (!hasUserId) {
	                System.out.println("Updating status only for complaint ID: " + complaintId);
	                if (newStatus != null) {
	                    complaint.setStatus(newStatus);
	                }
	            } else  {
	            complaint.setUser_id(jsonObject.getInt("user_id"));
//	            complaint.setComplaints_id(complaintId);
	            complaint.setUsername(jsonObject.getString("username"));
	            complaint.setCategory(jsonObject.getString("category"));
	            complaint.setSubject(jsonObject.getString("subject"));
	            System.out.println("Updated subject in entity: " + complaint.getSubject());

	            complaint.setContent(jsonObject.getString("content"));
	            complaint.setNote(jsonObject.optString("note", "無"));
	            complaint.setStatus(jsonObject.optString("status", "待處理"));
	            
	            System.out.println("check some thing");
	            System.out.println(jsonObject.getInt("user_id"));
	            
	            System.out.println("servlet doPut");
	            
	            }
	            
	            session.merge(complaint);
	            session.flush();
	            transaction.commit();
	            
	            System.out.println("Before commit: " + complaint.getSubject());

	            response.getWriter().println("{\"success\":\"Data updated successfully\"}");
	        } else {
	            response.getWriter().println("{\"error\":\"Complaint not found\"}");
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	        response.getWriter().println("{\"error\":\"Data update failed\"}");
	    }
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		Transaction transaction = null;
		String pathInfo = request.getPathInfo();

		try (PrintWriter out = response.getWriter()) {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

			if (pathInfo != null && pathInfo.length() > 1) {
				int complaintId = Integer.parseInt(pathInfo.substring(1));
				formComplationBean complaint = session.get(formComplationBean.class, complaintId);
				if (complaint != null) {
					session.remove(complaint);
					transaction.commit();
					out.print("{\"success\":\"Data deleted successfully\"}");
				} else {
					out.print("{\"error\":\"Complaint not found\"}");
				}
			} else {
				out.print("{\"error\":\"Complaint ID not provided\"}");
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			response.getWriter().println("{\"error\":\"Data delete failed\"}");
		}
	}
}

