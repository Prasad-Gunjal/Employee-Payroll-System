package com.employee.menu;

import java.io.File;
import java.util.List;

import com.employee.Service.EmployeeService;
import com.employee.Service.PayrollService;
import com.employee.email.EmailService;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.invalidSalaryException;
import com.employee.file.FileExportUtil;
import com.employee.model.Employee;
import com.employee.model.Overtime;
import com.employee.model.Payroll;
import com.employee.model.Salary;
import com.employee.pdf.PaySlipPDFGenerator;
import com.employee.util.ConsoleUI;
import com.employee.util.InputUtil;

public class PayrollMenu {

	private final PayrollService payrollService = new PayrollService();
	private final EmployeeService employeeService = new EmployeeService();
	private final FileExportUtil fileExportUtil = new FileExportUtil();
	private final PaySlipPDFGenerator pdfGenerator = new PaySlipPDFGenerator();

	public void show() {
		boolean back = false;
		while (!back) {
			System.out.println();
			System.out.println(ConsoleUI.topBorder());
			System.out.println(ConsoleUI.centered("PAYROLL PROCESSING"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("1", "Process Payroll (single employee)"));
			System.out.println(ConsoleUI.option("2", "Bulk Payroll - All Employees"));
			System.out.println(ConsoleUI.option("3", "Display Payroll Record"));
			System.out.println(ConsoleUI.option("4", "View Payroll History (by employee)"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("5", "Generate Text Payslip"));
			System.out.println(ConsoleUI.option("6", "Generate PDF Payslip"));
			System.out.println(ConsoleUI.option("7", "Email Payslip (PDF must exist)"));
			System.out.println(ConsoleUI.option("8", "Send Payroll Notification Email"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("9", "Record Overtime"));
			System.out.println(ConsoleUI.option("10", "View Overtime Record"));
			System.out.println(ConsoleUI.option("11", "View Overtime History"));
			System.out.println(ConsoleUI.option("12", "Send Overtime Notification Email"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("13", "Export Monthly Report (CSV)"));
			System.out.println(ConsoleUI.option("0", "Back to Main Menu"));
			System.out.println(ConsoleUI.bottomBorder());
			int choice = InputUtil.readInt("  Enter choice: ");
			try {
				switch (choice) {
				case 1:
					processPayroll();
					break;
				case 2:
					bulkPayroll();
					break;
				case 3:
					displayPayroll();
					break;
				case 4:
					history();
					break;
				case 5:
					textPayslip();
					break;
				case 6:
					pdfPayslip();
					break;
				case 7:
					emailPayslip();
					break;
				case 8:
					sendPayrollNotification();
					break;
				case 9:
					recordOvertime();
					break;
				case 10:
					viewOvertime();
					break;
				case 11:
					overtimeHistory();
					break;
				case 12:
					sendOvertimeEmail();
					break;
				case 13:
					exportMonthlyReport();
					break;
				case 0:
					back = true;
					break;
				default:
					ConsoleUI.error("Invalid choice.");
				}
			} catch (EmployeeNotFoundException | invalidSalaryException e) {
				ConsoleUI.error(e.getMessage());
			} catch (Exception e) {
				ConsoleUI.error("Unexpected: " + e.getMessage());
			}
			if (!back)
				InputUtil.pressEnterToContinue();
		}
	}

	private void processPayroll() throws EmployeeNotFoundException, invalidSalaryException {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("PROCESS PAYROLL"));
		System.out.println(ConsoleUI.divider());
		int empId = InputUtil.readInt("  Employee ID         : ");
		String month = InputUtil.readString("  Payroll month (yyyy-MM): ");
		double allowance = InputUtil.readDouble("  Other allowances (0 if none): ");
		double deduction = InputUtil.readDouble("  Other deductions (0 if none): ");
		Payroll payroll = payrollService.processPayroll(empId, month, allowance, deduction);
		ConsoleUI.success("Payroll processed!");
		System.out.println();
		ConsoleUI.field("Present Days", String.valueOf(payroll.getPresentDays()));
		ConsoleUI.field("Absent Days", String.valueOf(payroll.getAbsentDays()));
		ConsoleUI.field("Leave Days", String.valueOf(payroll.getLeaveDays()));
		ConsoleUI.field("Net Salary", "Rs. " + String.format("%.2f", payroll.getNetSalary()));
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void bulkPayroll() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("BULK PAYROLL PROCESSING"));
		System.out.println(ConsoleUI.divider());
		String month = InputUtil.readString("  Payroll month (yyyy-MM)      : ");
		double allowance = InputUtil.readDouble("  Default allowances for all   : ");
		double deduction = InputUtil.readDouble("  Default deductions for all   : ");
		System.out.println();
		ConsoleUI.info("Processing all active employees...");
		List<String> results = payrollService.processBulkPayroll(month, allowance, deduction);
		int success = 0, skipped = 0, failed = 0;
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("BULK PAYROLL RESULTS"));
		System.out.println(ConsoleUI.divider());
		for (String r : results) {
			System.out.println("  " + r);
			if (r.startsWith("[SUCCESS]"))
				success++;
			else if (r.startsWith("[SKIPPED]"))
				skipped++;
			else
				failed++;
		}
		System.out.println(ConsoleUI.divider());
		System.out
				.println(ConsoleUI.row("  Success : " + success + "   Skipped : " + skipped + "   Failed : " + failed));
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void displayPayroll() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("PAYROLL RECORD"));
		System.out.println(ConsoleUI.divider());
		int empId = InputUtil.readInt("  Employee ID  : ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Payroll p = payrollService.getPayroll(empId, month);
		if (p == null) {
			ConsoleUI.error("No record found.");
		} else {
			ConsoleUI.field("Payroll ID", String.valueOf(p.getPayrollId()));
			ConsoleUI.field("Month", p.getPayrollMonth());
			ConsoleUI.field("Present Days", String.valueOf(p.getPresentDays()));
			ConsoleUI.field("Absent Days", String.valueOf(p.getAbsentDays()));
			ConsoleUI.field("Leave Days", String.valueOf(p.getLeaveDays()));
			ConsoleUI.field("Net Salary", "Rs. " + String.format("%.2f", p.getNetSalary()));
			ConsoleUI.field("Status", p.getStatus());
		}
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void history() {
		int empId = InputUtil.readInt("  Employee ID: ");
		List<Payroll> list = payrollService.getPayrollHistory(empId);
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("PAYROLL HISTORY"));
		System.out.println(ConsoleUI.divider());
		if (list.isEmpty()) {
			System.out.println(ConsoleUI.centered("No history found."));
		} else {
			System.out.printf("  %-10s %-10s %-8s %-8s %-8s %-12s%n", "Month", "Status", "Present", "Absent", "Leave",
					"Net Salary");
			System.out.println("  " + ConsoleUI.repeat("-", 52));
			for (Payroll p : list)
				System.out.printf("  %-10s %-10s %-8d %-8d %-8d Rs.%-9.2f%n", p.getPayrollMonth(), p.getStatus(),
						p.getPresentDays(), p.getAbsentDays(), p.getLeaveDays(), p.getNetSalary());
		}
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void textPayslip() throws EmployeeNotFoundException {
		int empId = InputUtil.readInt("  Employee ID: ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Employee emp = employeeService.getEmployeeOrThrow(empId);
		Salary salary = payrollService.getSalaryBreakdown(empId, month);
		if (salary == null) {
			ConsoleUI.error("No salary record. Process payroll first.");
			return;
		}
		String path = fileExportUtil.generateTextPayslip(emp, salary, "payslip_" + empId + "_" + month + ".txt");
		ConsoleUI.success("Text payslip: " + path);
	}

	private void pdfPayslip() throws EmployeeNotFoundException {
		int empId = InputUtil.readInt("  Employee ID: ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Employee emp = employeeService.getEmployeeOrThrow(empId);
		Salary salary = payrollService.getSalaryBreakdown(empId, month);
		if (salary == null) {
			ConsoleUI.error("No salary record. Process payroll first.");
			return;
		}
		String path = pdfGenerator.generatePayslipPdf(emp, salary, "payslip_" + empId + "_" + month + ".pdf");
		ConsoleUI.success("PDF payslip: " + path);
	}

	private void emailPayslip() throws EmployeeNotFoundException {
		int empId = InputUtil.readInt("  Employee ID: ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Employee emp = employeeService.getEmployeeOrThrow(empId);
		File pdf = new File(com.employee.util.Constants.REPORTS_DIR, "payslip_" + empId + "_" + month + ".pdf");
		if (!pdf.exists()) {
			ConsoleUI.error("PDF not found. Generate it first (option 6).");
			return;
		}
		new EmailService().sendPayslip(emp, month, pdf);
		ConsoleUI.success("Payslip emailed to " + emp.getEmail());
	}

	private void sendPayrollNotification() throws EmployeeNotFoundException {
		int empId = InputUtil.readInt("  Employee ID: ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Employee emp = employeeService.getEmployeeOrThrow(empId);
		Payroll payroll = payrollService.getPayroll(empId, month);
		if (payroll == null) {
			ConsoleUI.error("No payroll record. Process payroll first.");
			return;
		}
		Salary salary = payrollService.getSalaryBreakdown(empId, month);
		new EmailService().sendPayrollNotification(emp, payroll,
				salary != null ? salary.getNetSalary() : payroll.getNetSalary());
		ConsoleUI.success("Notification sent to " + emp.getEmail());
	}

	private void recordOvertime() throws EmployeeNotFoundException {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("RECORD OVERTIME"));
		System.out.println(ConsoleUI.divider());
		int empId = InputUtil.readInt("  Employee ID         : ");
		String month = InputUtil.readString("  Month (yyyy-MM)      : ");
		double hours = InputUtil.readDouble("  Overtime hours       : ");
		Overtime ot = payrollService.recordOvertime(empId, month, hours);
		ConsoleUI.success("Overtime recorded!");
		System.out.println();
		ConsoleUI.field("Hours", String.format("%.1f hrs", ot.getOvertimeHours()));
		ConsoleUI.field("Hourly Rate", String.format("Rs. %.2f/hr", ot.getHourlyRate()));
		ConsoleUI.field("Overtime Pay", String.format("Rs. %.2f  (x 1.5)", ot.getOvertimePay()));
		ConsoleUI.info("This pay will be added when payroll is processed.");
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void viewOvertime() {
		int empId = InputUtil.readInt("  Employee ID: ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Overtime ot = payrollService.getOvertime(empId, month);
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("OVERTIME RECORD"));
		System.out.println(ConsoleUI.divider());
		if (ot == null) {
			System.out.println(ConsoleUI.centered("No overtime record found."));
		} else {
			ConsoleUI.field("Month", ot.getOvertimeMonth());
			ConsoleUI.field("Hours", ot.getOvertimeHours() + " hrs");
			ConsoleUI.field("Rate", "Rs." + String.format("%.2f", ot.getHourlyRate()) + "/hr");
			ConsoleUI.field("Pay", "Rs." + String.format("%.2f", ot.getOvertimePay()));
		}
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void overtimeHistory() {
		int empId = InputUtil.readInt("  Employee ID: ");
		List<Overtime> list = payrollService.getOvertimeHistory(empId);
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("OVERTIME HISTORY"));
		System.out.println(ConsoleUI.divider());
		if (list.isEmpty()) {
			System.out.println(ConsoleUI.centered("No history found."));
		} else {
			System.out.printf("  %-10s %-10s %-14s %-12s%n", "Month", "Hours", "Hourly Rate", "OT Pay");
			System.out.println("  " + ConsoleUI.repeat("-", 48));
			for (Overtime o : list)
				System.out.printf("  %-10s %-10.1f Rs.%-11.2f Rs.%-9.2f%n", o.getOvertimeMonth(), o.getOvertimeHours(),
						o.getHourlyRate(), o.getOvertimePay());
		}
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void sendOvertimeEmail() throws EmployeeNotFoundException {
		int empId = InputUtil.readInt("  Employee ID: ");
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		Employee emp = employeeService.getEmployeeOrThrow(empId);
		Overtime ot = payrollService.getOvertime(empId, month);
		if (ot == null) {
			ConsoleUI.error("No overtime record. Record it first (option 9).");
			return;
		}
		new EmailService().sendOvertimeNotification(emp, ot);
		ConsoleUI.success("Overtime notification sent to " + emp.getEmail());
	}

	private void exportMonthlyReport() {
		String month = InputUtil.readString("  Month (yyyy-MM): ");
		List<Payroll> list = payrollService.getMonthlyPayrollReport(month);
		String path = fileExportUtil.exportPayrollReport(list, "payroll_" + month + ".csv");
		ConsoleUI.success("Exported to: " + path);
	}
}
