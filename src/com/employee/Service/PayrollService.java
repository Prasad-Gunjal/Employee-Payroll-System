package com.employee.Service;

import java.util.ArrayList;
import java.util.List;

import com.employee.dao.overtimeDAO;
import com.employee.dao.PayrollDAO;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.invalidSalaryException;
import com.employee.file.LogUtil;
import com.employee.model.Employee;
import com.employee.model.Overtime;
import com.employee.model.Payroll;
import com.employee.model.Salary;
import com.employee.Service.AttendanceService;
import com.employee.Service.EmployeeService;
import com.employee.util.DateUtil;
import com.employee.util.Validator;;

public class PayrollService {
	private final PayrollDAO payrollDAO = new PayrollDAO();
	private final overtimeDAO overtimeDAO = new overtimeDAO();
	private final EmployeeService employeeService = new EmployeeService();
	private final AttendanceService attendanceService = new AttendanceService();

	public Payroll processPayroll(int employeeId, String payrollMonth, double otherAllowance, double otherDeduction)
			throws EmployeeNotFoundException, invalidSalaryException {
		if (!DateUtil.isValidPayrollMonth(payrollMonth))
			throw new IllegalArgumentException("Payroll month must be yyyy-MM, got: " + payrollMonth);
		
		Validator.validatePositive(otherAllowance, "Allowance");
		Validator.validatePositive(otherDeduction, "Other deduction");
		Employee emp = employeeService.getEmployeeOrThrow(employeeId);
		Validator.validateSalary(emp.getBasicSalary());
		
		int presentDays = attendanceService.countPresentDays(employeeId, payrollMonth);
		int absentDays = attendanceService.countAbsentDays(employeeId, payrollMonth);
		int leaveDays = attendanceService.countLeaveDays(employeeId, payrollMonth);
		Overtime ot = overtimeDAO.findByEmployeeAndMonth(employeeId, payrollMonth);
		double totalAllowance = otherAllowance + (ot != null ? ot.getOvertimePay() : 0.0);
		Salary salary = new Salary(employeeId, emp.getBasicSalary(), totalAllowance, otherDeduction, payrollMonth);
		Payroll payroll = new Payroll(employeeId, payrollMonth, presentDays, absentDays, leaveDays,
				salary.getNetSalary(), null, payrollMonth);
		int payrollId = payrollDAO.saveFullPayroll(salary, payroll);
		payroll.setPayrollId(payrollId);
		LogUtil.info(
				"Payroll processed: emp=" + employeeId + " month=" + payrollMonth + " net=" + salary.getNetSalary());
		return payroll;
	}

	public List<String> processBulkPayroll(String payrollMonth, double defaultAllowance, double defaultDeduction) {
		if (!DateUtil.isValidPayrollMonth(payrollMonth))
			throw new IllegalArgumentException("Payroll month must be yyyy-MM.");
		List<Employee> all = employeeService.getAllEmployees();
		List<String> results = new ArrayList<>();
		for (Employee emp : all) {
			if (!"ACTIVE".equalsIgnoreCase(emp.getStatus().name())) {
				results.add("[SKIPPED]  EmpID=" + emp.getEmployeeId() + " (" + emp.getFirstName() + ") - Inactive");
				continue;
			}
			if (payrollDAO.findByEmployeeAndMonth(emp.getEmployeeId(), payrollMonth) != null) {
				results.add("[SKIPPED]  EmpID=" + emp.getEmployeeId() + " (" + emp.getFirstName()
						+ ") - Already processed");
				continue;
			}
			try {
				Payroll p = processPayroll(emp.getEmployeeId(), payrollMonth, defaultAllowance, defaultDeduction);
				results.add("[SUCCESS]  EmpID=" + emp.getEmployeeId() + " (" + emp.getFirstName() + ") - Net: Rs."
						+ String.format("%.2f", p.getNetSalary()));
			} catch (Exception e) {
				results.add("[FAILED]   EmpID=" + emp.getEmployeeId() + " (" + emp.getFirstName() + ") - "
						+ e.getMessage());
			}
		}
		LogUtil.info("Bulk payroll done for month=" + payrollMonth);
		return results;
	}

	public Overtime recordOvertime(int employeeId, String month, double hours) throws EmployeeNotFoundException {
		Employee emp = employeeService.getEmployeeOrThrow(employeeId);
		Overtime ot = new Overtime(employeeId, month, hours, emp.getBasicSalary());
		int id = overtimeDAO.save(ot);
		ot.setOvertimeId(id);
		LogUtil.info("Overtime recorded: empId=" + employeeId + " month=" + month + " hours=" + hours);
		return ot;
	}

	public Overtime getOvertime(int employeeId, String month) {
		return overtimeDAO.findByEmployeeAndMonth(employeeId, month);
	}

	public List<Overtime> getOvertimeHistory(int employeeId) {
		return overtimeDAO.findByEmployee(employeeId);
	}

	public Payroll getPayroll(int employeeId, String payrollMonth) {
		return payrollDAO.findByEmployeeAndMonth(employeeId, payrollMonth);
	}

	public Salary getSalaryBreakdown(int employeeId, String payrollMonth) {
		return payrollDAO.findSalary(employeeId, payrollMonth);
	}

	public List<Payroll> getPayrollHistory(int employeeId) {
		return payrollDAO.findByEmployee(employeeId);
	}

	public List<Payroll> getMonthlyPayrollReport(String payrollMonth) {
		return payrollDAO.findByMonth(payrollMonth);
	}

	public void markStatus(int payrollId, String status) {
		payrollDAO.updateStatus(payrollId, status);
	}

}
