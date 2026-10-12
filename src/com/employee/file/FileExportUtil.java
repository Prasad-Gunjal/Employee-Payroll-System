package com.employee.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.employee.model.Attendance;
import com.employee.model.Employee;
import com.employee.model.Payroll;
import com.employee.util.Constants;
import com.employee.util.DateUtil;

public class FileExportUtil {
	public FileExportUtil() {
		new File(Constants.EMPLOYEE_DIR).mkdirs();
		new File(Constants.ATTENDANCE_DIR).mkdirs();
		new File(Constants.PAYROLL_DIR).mkdirs();
		new File(Constants.REPORTS_DIR).mkdirs();
	}

	public String exportEmployeesToCsv(List<Employee> employees, String fileName) {
		File file = new File(Constants.EMPLOYEE_DIR, fileName);
		try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {

			bw.write("EmployeeID,FirstName,LastName,Email,Phone,Department,Designation,JoiningDate,BasicSalary,Status");
			bw.newLine();

			for (Employee e : employees) {
				bw.write(String.join(",", String.valueOf(e.getEmployeeId()), e.getFirstName(), e.getLastName(),
						e.getEmail(), e.getPhone(), e.getDepartment() != null ? e.getDepartment().getDeptName() : "",
						e.getDesignation(), DateUtil.formatDate(e.getJoiningDate()), String.valueOf(e.getBasicSalary()),
						e.getStatus().name()));
				bw.newLine();
			}
			LogUtil.info("Exported " + employees.size() + " employees to " + file.getPath());
			return file.getPath();
		} catch (IOException e) {
			LogUtil.Error("Failed to export employees CSV: " + e.getMessage());
			throw new RuntimeException("Failed to export employees: " + e.getMessage(), e);
		}
	}

	public String exportAttendanceReport(List<Attendance> records, String fileName) {
		File file = new File(Constants.ATTENDANCE_DIR, fileName);
		try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {

			bw.write("AttendanceID,EmployeeID,Date,Status");
			bw.newLine();
			for (Attendance a : records) {
				bw.write(String.join(",", String.valueOf(a.getAttendanceId()), String.valueOf(a.getEmployeeId()),
						DateUtil.formatDate(a.getAttendanceDate()), a.getStatus().name()));
				bw.newLine();
			}
			LogUtil.info("Exported " + records.size() + " attendance records to " + file.getPath());
			return file.getPath();
		} catch (IOException e) {
			LogUtil.Error("Failed to export attendance report: " + e.getMessage());
			throw new RuntimeException("Failed to export attendance report: " + e.getMessage(), e);
		}
	}

	public String exportPayrollReport(List<Payroll> records, String fileName) {
		File file = new File(Constants.PAYROLL_DIR, fileName);
		try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {

			bw.write("PayrollID,EmployeeID,Month,PresentDays,AbsentDays,LeaveDays,NetSalary,Status");
			bw.newLine();
			for (Payroll p : records) {
				bw.write(String.join(",", String.valueOf(p.getPayrollId()), String.valueOf(p.getEmployeeId()),
						p.getPayrollMonth(), String.valueOf(p.getPresentDays()), String.valueOf(p.getAbsentDays()),
						String.valueOf(p.getLeaveDays()), String.valueOf(p.getNetSalary()), p.getStatus()));
				bw.newLine();
			}
			LogUtil.info("Exported " + records.size() + " payroll records to " + file.getPath());
			return file.getPath();
		} catch (IOException e) {
			LogUtil.Error("Failed to export payroll report: " + e.getMessage());
			throw new RuntimeException("Failed to export payroll report: " + e.getMessage(), e);
		}
	}

	/**
	 * Generates a plain-text payslip (used as fallback / preview before PDF
	 * generation).
	 */
	public String generateTextPayslip(Employee emp, com.employee.model.Salary salary, String fileName) {
		File file = new File(Constants.REPORTS_DIR, fileName);
		try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {

			bw.write("========================================");
			bw.newLine();
			bw.write("      " + Constants.COMPANY_NAME);
			bw.newLine();
			bw.write("           PAYSLIP - " + salary.getPayrollMonth());
			bw.newLine();
			bw.write("========================================");
			bw.newLine();
			bw.write("Employee ID   : " + emp.getEmployeeId());
			bw.newLine();
			bw.write("Name          : " + emp.getFirstName());
			bw.newLine();
			bw.write("Department    : " + (emp.getDepartment() != null ? emp.getDepartment().getDeptName() : ""));
			bw.newLine();
			bw.write("Designation   : " + emp.getDesignation());
			bw.newLine();
			bw.write("----------------------------------------");
			bw.newLine();
			bw.write(String.format("Basic Salary  : %.2f", salary.getBasicSalary()));
			bw.newLine();
			bw.write(String.format("HRA           : %.2f", salary.getHra()));
			bw.newLine();
			bw.write(String.format("DA            : %.2f", salary.getDa()));
			bw.newLine();
			bw.write(String.format("Allowance     : %.2f", salary.getAllowance()));
			bw.newLine();
			bw.write(String.format("Gross Salary  : %.2f", salary.getGrossSalary()));
			bw.newLine();
			bw.write("----------------------------------------");
			bw.newLine();
			bw.write(String.format("PF            : %.2f", salary.getPf()));
			bw.newLine();
			bw.write(String.format("Prof. Tax     : %.2f", salary.getProfessionalTax()));
			bw.newLine();
			bw.write(String.format("Other Deduct. : %.2f", salary.getOtherDeduction()));
			bw.newLine();
			bw.write(String.format("Total Deduct. : %.2f", salary.getTotalDeduction()));
			bw.newLine();
			bw.write("----------------------------------------");
			bw.newLine();
			bw.write(String.format("NET SALARY    : %.2f", salary.getNetSalary()));
			bw.newLine();
			bw.write("========================================");
			bw.newLine();

			LogUtil.info("Text payslip generated: " + file.getPath());
			return file.getPath();
		} catch (IOException e) {
			LogUtil.Error("Failed to generate text payslip: " + e.getMessage());
			throw new RuntimeException("Failed to generate text payslip: " + e.getMessage(), e);
		}
	}
}
