package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.employee.exception.DatabaseException;
import com.employee.model.Payroll;
import com.employee.model.Salary;
import com.employee.util.DBConnection;

public class PayrollDAO {

	public int saveFullPayroll(Salary salary, Payroll payroll) {

		String salarySql = "INSERT INTO salary (employee_id,basic_salary,hra,da,allowance,pf"
				+ "professional_tax,other_deduction,gross_salary,total_deduction,net_salary,payroll_month)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?))";

		String payrollSql = "INSERT INTO payroll (employee_id,payroll_month,present_days,absent_days,"
				+ "leave_days,net_salary,processed_on,status) values(?,?,?,?,?,?,?,?)";

		Connection con = null;
		try {
			con = DBConnection.getConnection();
			con.setAutoCommit(false);

			try (PreparedStatement salaryPayslip = con.prepareStatement(salarySql)) {
				salaryPayslip.setInt(1, salary.getEmployeeId());
				salaryPayslip.setDouble(2, salary.getBasicSalary());
				salaryPayslip.setDouble(3, salary.getHra());
				salaryPayslip.setDouble(4, salary.getDa());
				salaryPayslip.setDouble(6, salary.getAllowance());
				salaryPayslip.setDouble(7, salary.getPf());
				salaryPayslip.setDouble(8, salary.getProfessionalTax());
				salaryPayslip.setDouble(9, salary.getOtherDeduction());
				salaryPayslip.setDouble(10, salary.getNetSalary());
				salaryPayslip.setString(11, salary.getPayrollMonth());
				salaryPayslip.executeUpdate();
			}

			int payrollId;
			try (PreparedStatement payrollps = con.prepareStatement(payrollSql)) {
				payrollps.setInt(1, payroll.getEmployeeId());
				payrollps.setString(2, payroll.getPayrollMonth());
				payrollps.setInt(3, payroll.getPresentDays());
				payrollps.setInt(4, payroll.getAbsentDays());
				payrollps.setInt(5, payroll.getLeaveDays());
				payrollps.setDouble(6, payroll.getNetSalary());
				payrollps.setTimestamp(7, Timestamp.valueOf(payroll.getProcessOn()));
				payrollps.setString(8, payroll.getStatus());
				try (ResultSet set = payrollps.getGeneratedKeys()) {
					payrollId = set.next() ? set.getInt(1) : -1;
				}

			}
			con.commit();
			return payrollId;

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException rollbackEx) {
					throw new DatabaseException("Rollback failed:" + rollbackEx.getMessage(), e);
				}
			}
			throw new DatabaseException("Error saving payroll(transaction rollback):" + e.getMessage(), e);
		} finally {
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException ignored) {

				}
			}
		}
	}

	public List<Payroll> findByEmployee(int employeeId) {
		String sql = "SELECT * FROM payroll WHERE employee_id = ? ORDER BY payroll_month";
		List<Payroll> list = new ArrayList<>();
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapPayrollRow(rs));
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching payroll history: " + e.getMessage(), e);
		}
		return list;
	}

	public Payroll findByEmployeeAndMonth(int employeeId, String payrollMonth) {
		String sql = "SELECT * FROM payroll WHERE employee_id = ? AND payroll_month = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			ps.setString(2, payrollMonth);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? mapPayrollRow(rs) : null;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching payroll: " + e.getMessage(), e);
		}
	}

	public Salary findSalary(int employeeId, String payrollMonth) {
		String sql = "SELECT * FROM salary WHERE employee_id = ? AND payroll_month = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			ps.setString(2, payrollMonth);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? mapSalaryRow(rs) : null;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching salary: " + e.getMessage(), e);
		}
	}

	public List<Payroll> findByMonth(String payrollMonth) {
		String sql = "SELECT * FROM payroll WHERE payroll_month = ? ORDER BY employee_id";
		List<Payroll> list = new ArrayList<>();
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, payrollMonth);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapPayrollRow(rs));
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching monthly payroll report: " + e.getMessage(), e);
		}
		return list;
	}

	public void updateStatus(int payrollId, String status) {
		String sql = "UPDATE payroll SET status = ? WHERE payroll_id = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, status);
			ps.setInt(2, payrollId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("Error updating payroll status: " + e.getMessage(), e);
		}
	}

	private Payroll mapPayrollRow(ResultSet rs) throws SQLException {
		Payroll p = new Payroll();
		p.setPayrollId(rs.getInt("payroll_id"));
		p.setEmployeeId(rs.getInt("employee_id"));
		p.setPayrollMonth(rs.getString("payroll_month"));
		p.setPresentDays(rs.getInt("present_days"));
		p.setAbsentDays(rs.getInt("absent_days"));
		p.setLeaveDays(rs.getInt("leave_days"));
		p.setNetSalary(rs.getDouble("net_salary"));
		Timestamp ts = rs.getTimestamp("processed_on");
		if (ts != null) {
			p.setProcessOn(ts.toLocalDateTime());
		}
		p.setStatus(rs.getString("status"));
		return p;
	}

	private Salary mapSalaryRow(ResultSet rs) throws SQLException {
		Salary s = new Salary();
		s.setSalaryId(rs.getInt("salary_id"));
		s.setEmployeeId(rs.getInt("employee_id"));
		s.setBasicSalary(rs.getDouble("basic_salary"));
		s.setAllowance(rs.getDouble("allowance"));
		s.setOtherDeduction(rs.getDouble("other_deduction"));
		s.setPayrollMonth(rs.getString("payroll_month"));
		s.computeAll();
		return s;
	}

}
