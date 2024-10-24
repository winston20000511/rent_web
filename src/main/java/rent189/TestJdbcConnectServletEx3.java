package rent189;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rent189.config.ConnectionUtil;

@WebServlet("/TestJdbcConnectServletEx3.do")
public class TestJdbcConnectServletEx3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
	private Connection conn;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processAction(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processAction(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeConn() throws SQLException {
		if(conn!=null) {
			conn.close();
		}
		out.write("Connection Closed<br/>");
	}
	private void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		ConnectionUtil.getConnection();

		try {			
			ConnectionUtil.getConnection();		
			//crud action 以下操作crud各功能
//			processQueryAllAction(); //先來檢查目前資料庫有哪些資料
//			processQueryById(2);
			
			//新增一筆資料
//			byte[] picture = new byte[] { (byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE0, (byte)0x00, (byte)0x14, (byte)0x4A, (byte)0x46, (byte)0x49, (byte)0x46 };
//			processInsertAction("David Smith", "david.smith@example.com", "davidpass345", "5566778899", picture, new java.sql.Timestamp(System.currentTimeMillis()), 0);

			processQueryAllAction();
			out.write("資料前後對比"+"<hr>");
			processQueryById(8) ;
			processUpdateAction(2, "Akai"); //當你要傳遞字串常數（如 "Akai"）作為參數時，必須把它用雙引號包起來
//			processDeleteAction(1003);
			processQueryAllAction();
			
			closeConn();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		out.close();

	}	
//	刪除功能
	private void processDeleteAction(int user_id) throws SQLException {
		out.write("成功執行刪除！"+ "<br/>"); // 確認方法被呼叫
		String sqlstr = "delete from user_table where user_id=?";
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setInt(1, user_id);
		state.execute();
		state.close();
		out.write("Delete Success<br/>");
	}

//	更新功能
	private void processUpdateAction(int user_id, String name) throws SQLException {
		out.write("成功執行更新！"+ "<br/>"); // 確認方法被呼叫
		String sqlstr = "update user_table set name=? where user_id=?";
		PreparedStatement state = conn.prepareStatement(sqlstr);		
		state.setString(1, name);  // 將 name 設置為第 1 個參數
	    state.setInt(2, user_id);  // 將 user_id 設置為第 2 個參數 
	 // 因為 SQL 語句 update user_table set name=? where user_id=? 中，第 1 個問號對應的是 name，第 2 個問號對應的是 user_id
		int result = state.executeUpdate();
		state.close();
		out.write("更新成功次數:" + result + "<br/>");
		 // 如果更新成功，查詢該筆資料並顯示出來
        String queryStr = "select * from user_table where user_id=?";
        PreparedStatement queryState = conn.prepareStatement(queryStr);
        queryState.setInt(1, user_id);
        
        ResultSet rs = queryState.executeQuery();
        if (rs.next()) {
            // 這裡把更新後的那筆資料印出來
            out.write("更新成功結果: " + rs.getInt("user_id") + " " 
                      + rs.getString("name") + " " 
                      + rs.getString("email") + " "
                      + rs.getString("password") + " "
                      + rs.getString("phone") + " "
                      + rs.getBytes("picture") + " "
                      + rs.getTimestamp("createtime") + " "
                      + rs.getInt("gender") + "<br/>");
        }
        rs.close();
        queryState.close();
    }
	
//	新增功能
	private void processInsertAction(String name, String email, String password, String phone, byte[] picture, Timestamp createtime, int gender) throws SQLException {
		out.write("成功執行新增！"+ "<br/>"); // 確認方法被呼叫
		String sqlstr = "insert into user_table(name, email, password, phone, picture, createtime, gender)values(?,?,?,?,?,?,?)";
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setString(1, name);
		state.setString(2, email);
		state.setString(3, password);
		state.setString(4, phone);
		state.setBytes(5, picture); // 使用 setBytes() 方法插入圖片的 byte[] 資料
		state.setTimestamp(6, createtime);  // 使用 setTimestamp 方法插入日期時間
		state.setInt(7, gender);
		int count = state.executeUpdate();	
		out.write("Insert Success:" + count + "<br/>");
		state.close();
	}

//	查詢功能（用id查詢某筆資料）
	private void processQueryById(int user_id) throws SQLException {
		out.write("成功執行id查詢！"+ "<br/>"); // 確認方法被呼叫
		String sqlsrt = "select * from user_table where user_id=?";
		PreparedStatement state = conn.prepareStatement(sqlsrt);
		state.setInt(1, user_id);
		ResultSet rs = state.executeQuery();
		
		if(rs.next()) {
			out.write(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "  + rs.getString(4) + " "  + rs.getBytes(5)+ " "+ "<br/>"); //rs.getTimestamp(6) + " " + rs.getInt(7)  + " " 無法正確印出
		}else {
			out.write("no result<br/>");
		}
		
		rs.close();
		state.close();
	}

//	查詢功能（一次看全部資料）
	private void processQueryAllAction() throws SQLException {
		out.write("成功執行全部查詢！"+ "<br/>"); // 確認方法被呼叫
		String sqlstr = "select * from user_table";
		PreparedStatement state = conn.prepareStatement(sqlstr);
		ResultSet rs = state.executeQuery();
		while(rs.next()) {
			out.write(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "  + rs.getString(4) + " "  + rs.getBytes(5)+ " "+ "<br/>"); //rs.getTimestamp(6) + " " + rs.getInt(7)  + " " 無法正確印出
		}
//		out.write(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getBytes(5) + " " + rs.getTimestamp(6) + " " + rs.getInt(7)  + " " + "<br/>");
//	}
		
		rs.close();
		state.close();
	}

}
