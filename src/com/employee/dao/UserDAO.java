package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.employee.exception.DatabaseException;
import com.employee.model.User;
import com.employee.util.DBConnection;

public class UserDAO {

	public User findUsername(String username) {
		String query = "SELECT * from user where username=?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement st = con.prepareStatement(query)) {

			st.setString(1, username);
			try (ResultSet set = st.executeQuery()) {
				if (set.next()) {
					return mapRow(set);
				}

			}
			return null;

		} catch (SQLException e) {
			throw new DatabaseException("Error finding user by username:" + e.getMessage(), e);
		}
	}

	public void updateLastLogin(int userId, LocalDateTime loginTime) {

		String query = "Update user set last_login=? where user_Id=?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement st = con.prepareStatement(query)) {
			st.setTimestamp(1, Timestamp.valueOf(loginTime));
			st.setInt(2, userId);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error updating last login: " + e.getMessage(), e);
		}
	}

	public int insert(User user) {
		String query = "INSERT INTO User(username,password_hash,role) values(?,?,?)";
		try (Connection con = DBConnection.getConnection(); PreparedStatement st = con.prepareStatement(query)) {

			st.setString(1, user.getUsername());
			st.setString(2, user.getPasswordHash());
			st.setString(3, user.getRole());
			st.executeUpdate();
			try (ResultSet set = st.getGeneratedKeys()) {
				if (set.next()) {
					return set.getInt(1);
				}

			}
			return -1;
		} catch (SQLException e) {
			throw new DatabaseException("Error inserting user: " + e.getMessage(), e);
		}
	}
	
	public boolean changePassword(int userId, String newPasswordHash) {
		String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, newPasswordHash);
			ps.setInt(2, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DatabaseException("Error changing password: " + e.getMessage(), e);
		}
	}

	private User mapRow(ResultSet set) throws SQLException {
		User user = new User();
		user.setUserId(set.getInt("user_id"));
		user.setUsername(set.getString("username"));
		user.setPasswordHash(set.getString("password_hash"));
		user.setRole(set.getString("role"));
		Timestamp ts = set.getTimestamp("last_login");
		if (ts != null) {
			user.setLastLogin(ts.toLocalDateTime());
		}

		return user;

	}
}
