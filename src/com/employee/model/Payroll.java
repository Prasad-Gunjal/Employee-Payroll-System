package com.employee.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Payroll {

	private static  final long serialVersionUID=1L;
	
	private int payrollId;
	private int employeeId;
	private String payrollMonth;
	private int presentDays;
	private int absentDays;
	private int leaveDays;
	private double netSalary;
	private LocalDateTime processOn;
	
	private String Status;
	
	public Payroll() {
		
	}

	public Payroll(int employeeId, String payrollMonth, int presentDays, int absentDays, int leaveDays,
			double netSalary, LocalDateTime processOn, String status) {
	
		this.employeeId = employeeId;
		this.payrollMonth = payrollMonth;
		this.presentDays = presentDays;
		this.absentDays = absentDays;
		this.leaveDays = leaveDays;
		this.netSalary = netSalary;
		this.processOn = LocalDateTime.now();
		Status = "GENERATED";
	}

	public int getPayrollId() {
		return payrollId;
	}

	public void setPayrollId(int payrollId) {
		this.payrollId = payrollId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getPayrollMonth() {
		return payrollMonth;
	}

	public void setPayrollMonth(String payrollMonth) {
		this.payrollMonth = payrollMonth;
	}

	public int getPresentDays() {
		return presentDays;
	}

	public void setPresentDays(int presentDays) {
		this.presentDays = presentDays;
	}

	public int getAbsentDays() {
		return absentDays;
	}

	public void setAbsentDays(int absentDays) {
		this.absentDays = absentDays;
	}

	public int getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}

	public double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(double netSalary) {
		this.netSalary = netSalary;
	}

	public LocalDateTime getProcessOn() {
		return processOn;
	}

	public void setProcessOn(LocalDateTime processOn) {
		this.processOn = processOn;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Payroll [payrollId=" + payrollId + ", employeeId=" + employeeId + ", payrollMonth=" + payrollMonth
				+ ", presentDays=" + presentDays + ", absentDays=" + absentDays + ", leaveDays=" + leaveDays
				+ ", netSalary=" + netSalary + ", processOn=" + processOn + ", Status=" + Status + "]";
	}
	
	
	
	
	
}
