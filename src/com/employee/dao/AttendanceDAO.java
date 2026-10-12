package com.employee.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.employee.exception.DatabaseException;
import com.employee.model.Attendance;
import com.employee.model.AttendanceStatus;
import com.employee.util.DBConnection;

public class AttendanceDAO {

	public int insert(Attendance atten) {
		String sql = "INSERT INTO attendance (employee_id, attendance_date, status) VALUES (?, ?, ?)";
		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, atten.getEmployeeId());
			ps.setDate(2, Date.valueOf(atten.getAttendanceDate()));
			ps.setString(3, atten.getStatus().name());
			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
			}
			return -1;
		} catch (SQLException e) {
			throw new DatabaseException("Error in inserting attendance: " + e.getMessage(), e);
		}

	}

	public boolean update(Attendance att) {
		String sql = "UPDATE attendance SET status = ? WHERE employee_id = ? AND attendance_date = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, att.getStatus().name());
			ps.setInt(2, att.getEmployeeId());
			ps.setDate(3, Date.valueOf(att.getAttendanceDate()));
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DatabaseException("Error updating attendance: " + e.getMessage(), e);
		}
	}

	public Attendance findByEmployeeAndDate(int employeeId, LocalDate date) {
		String sql = "SELECT * FROM attendance WHERE employee_id = ? AND attendance_date = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			ps.setDate(2, Date.valueOf(date));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
				return null;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error finding attendance record: " + e.getMessage(), e);
		}
	}

	public List<Attendance> findByEmployee(int employeeId) {
		String sql = "SELECT * FROM attendance WHERE employee_id = ? ORDER BY attendance_date";
		return queryList(sql, employeeId, null);
	}

	public List<Attendance> findByEmployeeAndMonth(int employeeId, String yearMonth) {
		String sql = "SELECT * FROM attendance WHERE employee_id = ? "
				+ "AND DATE_FORMAT(attendance_date, '%Y-%m') = ? ORDER BY attendance_date";
		return queryList(sql, employeeId, yearMonth);
	}

	public List<Attendance> findByDate(LocalDate date) {
		String sql = "SELECT * FROM attendance WHERE attendance_date = ? ORDER BY employee_id";
		List<Attendance> list = new ArrayList<>();
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(date));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapRow(rs));
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error finding attendance by date: " + e.getMessage(), e);
		}
		return list;
	}

	private List<Attendance> queryList(String sql, int employeeId, String yearMonth) {
		List<Attendance> list = new ArrayList<>();
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			if (yearMonth != null) {
				ps.setString(2, yearMonth);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapRow(rs));
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching attendance: " + e.getMessage(), e);
		}
		return list;
	}

	private Attendance mapRow(ResultSet rs) throws SQLException {
		return new Attendance(rs.getInt("attendance_id"), rs.getInt("employee_id"),
				rs.getDate("attendance_date").toLocalDate(), AttendanceStatus.valueOf(rs.getString("status")));
	}
}
