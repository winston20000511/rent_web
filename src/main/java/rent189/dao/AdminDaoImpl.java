package rent189.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import rent189.config.ConnectionUtil;
import rent189.model.AdminBean;

public class AdminDaoImpl implements AdminDao {
	 
	@Override
	public AdminBean confirmAccount(String email, String password) {
		String sql ="SELECT * FROM admin_table WHERE adminEmail = ? AND adminPassword = ?";
		AdminBean admin = null;
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admin = new AdminBean();
                admin.setAdminId(rs.getInt("adminId"));
                admin.setAdminName(rs.getString("adminName"));
                admin.setAdminEmail(rs.getString("adminEmail"));
                admin.setAdminPassword(rs.getString("adminPassword"));
                admin.setAdminPhone(rs.getString("adminPhone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
	}

	@Override
	public boolean updateAdmin(AdminBean admin) {
		boolean updateResult = false;
		String sql = "update admin_table set adminName = ?, adminEmail = ?, "
				+ "adminPassword = ?, adminPhone = ? "
				+ "where adminId = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setString(1, admin.getAdminName());
			statement.setString(2, admin.getAdminEmail());
			statement.setString(3, admin.getAdminPassword());
			statement.setString(4, admin.getAdminPhone());
			statement.setInt(5, admin.getAdminId());
			int update = statement.executeUpdate();
			if (update > 0) {
				updateResult = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updateResult;
	}
}
