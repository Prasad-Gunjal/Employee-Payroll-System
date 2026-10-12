package com.employee.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.employee.exception.DatabaseException;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.model.EmployeeStatus;
import com.employee.model.Gender;
import com.employee.util.DBConnection;

public class EmployeeDAO {

	private static final String Select_Join = "select e* ,d.department_name ,d.description AS dept_description"
			+ "select employee e left Join department d ON e.department_id=d.department_id";

	
	public int insert(Employee emp) {
        String sql = "INSERT INTO employees (first_name, last_name, email, phone, address, gender, " +
                "department_id, designation, joining_date, basic_salary, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindEmployee(ps, emp);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting employee: " + e.getMessage(), e);
        }
    }

    public boolean update(Employee emp) {
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, phone=?, address=?, gender=?, " +
                "department_id=?, designation=?, joining_date=?, basic_salary=?, status=? WHERE employee_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bindEmployee(ps, emp);
            ps.setInt(12, emp.getEmployeeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating employee: " + e.getMessage(), e);
        }
    }

    public boolean delete(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting employee: " + e.getMessage(), e);
        }
    }

    public Employee findById(int employeeId) {
        String sql = Select_Join + "WHERE e.employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding employee: " + e.getMessage(), e);
        }
    }

    public List<Employee> findByName(String name) {
        String sql = Select_Join + "WHERE e.first_name LIKE ? OR e.last_name LIKE ?";
        List<Employee> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching employees by name: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Employee> findByDepartment(int departmentId) {
        String sql = Select_Join + "WHERE e.department_id = ?";
        List<Employee> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching employees by department: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Employee> findAll() {
        String sql = Select_Join + "ORDER BY e.employee_id";
        List<Employee> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching employees: " + e.getMessage(), e);
        }
        return list;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM employees";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting employees: " + e.getMessage(), e);
        }
    }

    private void bindEmployee(PreparedStatement ps, Employee emp) throws SQLException {
        ps.setString(1, emp.getFirstName());
        ps.setString(2, emp.getLastName());
        ps.setString(3, emp.getEmail());
        ps.setString(4, emp.getPhone());
        ps.setString(5, emp.getAddress());
        ps.setString(6, emp.getGender().name());
        ps.setInt(7, emp.getDepartment().getDeptId());
        ps.setString(8, emp.getDesignation());
        ps.setDate(9, Date.valueOf(emp.getJoiningDate()));
        ps.setDouble(10, emp.getBasicSalary());
        ps.setString(11, emp.getStatus().name());
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        // ResultSetMetaData usage example: could be used to build generic exports.
        // Here it's used defensively to check the join actually produced a department name.
        Department dept = new Department(
                rs.getInt("department_id"),
                rs.getString("department_name"),
                rs.getString("dept_description")
        );

        Employee emp = new Employee();
        emp.setEmployeeId(rs.getInt("employee_id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setEmail(rs.getString("email"));
        emp.setPhone(rs.getString("phone"));
        emp.setAddress(rs.getString("address"));
        emp.setGender(Gender.valueOf(rs.getString("gender")));
        emp.setDepartment(dept);
        emp.setDesignation(rs.getString("designation"));
        emp.setJoiningDate(rs.getDate("joining_date").toLocalDate());
        emp.setBasicSalary(rs.getDouble("basic_salary"));
        emp.setStatus(EmployeeStatus.valueOf(rs.getString("status")));
        return emp;
    }
}
