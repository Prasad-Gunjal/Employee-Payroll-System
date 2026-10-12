package com.employee.Service;

import com.employee.dao.EmployeeDAO;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.invalidSalaryException;
import com.employee.file.LogUtil;
import com.employee.model.Employee;
import com.employee.util.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmployeeService {

	private final EmployeeDAO employeeDAO = new EmployeeDAO();

	public int addEmployee(Employee emp) throws invalidSalaryException {
		Validator.NoBlankInput(emp.getFirstName(), "First name");
		Validator.NoBlankInput(emp.getLastName(), "Last name");
		Validator.validEmail(emp.getEmail());
		Validator.validMobileNo(emp.getPhone());
		Validator.validateSalary(emp.getBasicSalary());

		int id = employeeDAO.insert(emp);
		LogUtil.info("Employee added: " + emp.getFirstName() + " (id=" + id + ")");
		return id;
	}

	public void updateEmployee(Employee emp) throws EmployeeNotFoundException, invalidSalaryException {
		getEmployeeOrThrow(emp.getEmployeeId());
		Validator.validEmail(emp.getEmail());
		Validator.validMobileNo(emp.getPhone());
		Validator.validateSalary(emp.getBasicSalary());
		employeeDAO.update(emp);
		LogUtil.info("Employee updated: id=" + emp.getEmployeeId());
	}

	public void deleteEmployee(int employeeId) throws EmployeeNotFoundException {
		getEmployeeOrThrow(employeeId);
		employeeDAO.delete(employeeId);
		LogUtil.info("Employee deleted: id=" + employeeId);
	}

	public Employee getEmployeeOrThrow(int employeeId) throws EmployeeNotFoundException {
		Employee emp = employeeDAO.findById(employeeId);
		if (emp == null) {
			throw new EmployeeNotFoundException("No employee found with ID: " + employeeId);
		}
		return emp;
	}

	public List<Employee> searchByName(String name) {
		return employeeDAO.findByName(name);
	}

	public List<Employee> searchByDepartment(int departmentId) {
		return employeeDAO.findByDepartment(departmentId);
	}

	public List<Employee> getAllEmployees() {
		return employeeDAO.findAll();
	}

	public int countEmployees() {
		return employeeDAO.countAll();
	}

	/** Sorts by natural order (employeeId) using Comparable. */
	public List<Employee> sortById(List<Employee> employees) {
		List<Employee> copy = new ArrayList<>(employees);
		java.util.Collections.sort(copy);
		return copy;
	}

	/** Sorts by name using a Comparator (lambda). */
	public List<Employee> sortByName(List<Employee> employees) {
		List<Employee> copy = new ArrayList<>(employees);
		copy.sort(Comparator.comparing(Employee::getFirstName, String.CASE_INSENSITIVE_ORDER));
		return copy;
	}

	/** Sorts by basic salary, descending, using a Comparator. */
	public List<Employee> sortBySalaryDesc(List<Employee> employees) {
		List<Employee> copy = new ArrayList<>(employees);
		copy.sort(Comparator.comparingDouble(Employee::getBasicSalary).reversed());
		return copy;
	}

	/** Sorts by joining date (oldest first). */
	public List<Employee> sortByJoiningDate(List<Employee> employees) {
		List<Employee> copy = new ArrayList<>(employees);
		copy.sort(Comparator.comparing(Employee::getJoiningDate));
		return copy;
	}

	
	public Set<String> distinctDepartmentNames(List<Employee> employees) {
		Set<String> names = new HashSet<>();
		Iterator<Employee> it = employees.iterator();
		while (it.hasNext()) {
			Employee e = it.next();
			if (e.getDepartment() != null) {
				names.add(e.getDepartment().getDeptName());
			}
		}
		return names;
	}

	
	public Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
		Map<String, List<Employee>> grouped = new HashMap<>();
		for (Employee e : employees) {
			String deptName = e.getDepartment() != null ? e.getDepartment().getDeptName() : "Unassigned";
			grouped.computeIfAbsent(deptName, k -> new ArrayList<>()).add(e);
		}
		return grouped;
	}

	public double totalSalaryExpense(List<Employee> employees) {
		double total = 0;
		for (Employee e : employees) {
			total += e.getBasicSalary();
		}
		return total;
	}

}
