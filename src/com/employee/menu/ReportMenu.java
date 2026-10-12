package com.employee.menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.employee.file.AppBackup;
import com.employee.file.BackupUtil;
import com.employee.model.Attendance;
import com.employee.model.Employee;
import com.employee.model.Payroll;
import com.employee.report.ReportGenerator;
import com.employee.Service.AttendanceService;
import com.employee.Service.EmployeeService;
import com.employee.Service.PayrollService;
import com.employee.util.ConsoleUI;
import com.employee.util.InputUtil;

public class ReportMenu {

	private final EmployeeService employeeService = new EmployeeService();
	private final AttendanceService attendanceService = new AttendanceService();
	private final PayrollService payrollService = new PayrollService();
	private final ReportGenerator reportGenerator = new ReportGenerator();
	private final BackupUtil backupUtil = new BackupUtil();

	public void show() {
		boolean back = false;
		while (!back) {
			System.out.println();
			System.out.println(ConsoleUI.topBorder());
			System.out.println(ConsoleUI.centered("REPORTS & BACKUP"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("1", "All Employees Report"));
			System.out.println(ConsoleUI.option("2", "Department-wise Report"));
			System.out.println(ConsoleUI.option("3", "Attendance Report (by date)"));
			System.out.println(ConsoleUI.option("4", "Monthly Payroll Report"));
			System.out.println(ConsoleUI.option("5", "Salary Summary"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("6", "Backup Application Data"));
			System.out.println(ConsoleUI.option("7", "Restore From Backup"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("0", "Back to Main Menu"));
			System.out.println(ConsoleUI.bottomBorder());
			int choice = InputUtil.readInt("  Enter choice: ");
			try {
				switch (choice) {
				case 1:
					allEmployees();
					break;
				case 2:
					departmentWise();
					break;
				case 3:
					attendanceByDate();
					break;
				case 4:
					monthlyPayroll();
					break;
				case 5:
					salarySummary();
					break;
				case 6:
					backup();
					break;
				case 7:
					restore();
					break;
				case 0:
					back = true;
					break;
				default:
					ConsoleUI.error("Invalid choice.");
				}
			} catch (Exception e) {
				ConsoleUI.error(e.getMessage());
			}
			if (!back)
				InputUtil.pressEnterToContinue();
		}
	}

	private void allEmployees() {
		System.out.println();
		ConsoleUI.separator();
		System.out.println(reportGenerator.allEmployeesReport(employeeService.getAllEmployees()));
		ConsoleUI.separator();
	}

	private void departmentWise() {
		List<Employee> emp = employeeService.getAllEmployees();
		Map<String, List<Employee>> g = employeeService.groupByDepartment(emp);
		System.out.println();
		ConsoleUI.separator();
		System.out.println(reportGenerator.departmentWiseReport(g));
		ConsoleUI.separator();
	}

	private void attendanceByDate() {
		LocalDate date = InputUtil.readDate("  Date");
		List<Attendance> r = attendanceService.getAttendanceByDate(date);
		System.out.println();
		ConsoleUI.separator();
		System.out.println(reportGenerator.attendanceReport(r));
		ConsoleUI.separator();
	}

	private void monthlyPayroll() {
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		List<Payroll> r = payrollService.getMonthlyPayrollReport(month);
		System.out.println();
		ConsoleUI.separator();
		System.out.println(reportGenerator.monthlyPayrollReport(r, month));
		ConsoleUI.separator();
	}

	private void salarySummary() {
		List<Employee> emp = employeeService.getAllEmployees();
		double total = employeeService.totalSalaryExpense(emp);
		System.out.println();
		ConsoleUI.separator();
		System.out.println(reportGenerator.salarySummary(total, emp.size()));
		ConsoleUI.separator();
	}

	private void backup() {
		List<Employee> emp = employeeService.getAllEmployees();
		String path = backupUtil.backup(new AppBackup(emp));
		ConsoleUI.success("Backup saved to: " + path);
	}

	private void restore() {
		AppBackup bk = backupUtil.restore();
		if (bk == null) {
			ConsoleUI.error("No backup file found.");
		} else {
			ConsoleUI.success("Backup from " + bk.getBackupTime() + " has " + bk.getEmployees().size() + " record(s).");
		}
	}
}
