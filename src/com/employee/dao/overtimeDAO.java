package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.employee.exception.DatabaseException;
import com.employee.model.Overtime;
import com.employee.util.DBConnection;

public class overtimeDAO {
	public int save(Overtime ot) {
		String sql = "INSERT INTO overtime (employee_id, overtime_month, overtime_hours, hourly_rate, overtime_pay) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE overtime_hours=VALUES(overtime_hours), hourly_rate=VALUES(hourly_rate), overtime_pay=VALUES(overtime_pay)";
		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, ot.getEmployeeId());
			ps.setString(2, ot.getOvertimeMonth());
			ps.setDouble(3, ot.getOvertimeHours());
			ps.setDouble(4, ot.getHourlyRate());
			ps.setDouble(5, ot.getOvertimePay());
			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				return keys.next() ? keys.getInt(1) : -1;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error saving overtime: " + e.getMessage(), e);
		}
	}

	public Overtime findByEmployeeAndMonth(int employeeId, String month) {
		String sql = "SELECT * FROM overtime WHERE employee_id = ? AND overtime_month = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			ps.setString(2, month);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? mapRow(rs) : null;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching overtime: " + e.getMessage(), e);
		}
	}

	public List<Overtime> findByEmployee(int employeeId) {
		String sql = "SELECT * FROM overtime WHERE employee_id = ? ORDER BY overtime_month DESC";
		List<Overtime> list = new ArrayList<>();
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(mapRow(rs));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching overtime history: " + e.getMessage(), e);
		}
		return list;
	}

	private Overtime mapRow(ResultSet rs) throws SQLException {
		Overtime ot = new Overtime();
		ot.setOvertimeId(rs.getInt("overtime_id"));
		ot.setEmployeeId(rs.getInt("employee_id"));
		ot.setOvertimeMonth(rs.getString("overtime_month"));
		ot.setOvertimeHours(rs.getDouble("overtime_hours"));
		ot.setHourlyRate(rs.getDouble("hourly_rate"));
		ot.setOvertimePay(rs.getDouble("overtime_pay"));
		return ot;
	}
}
