package com.employee.file;

import java.time.LocalDateTime;
import java.util.List;

import com.employee.model.Employee;

public class AppBackup {

	private static final long serialVersionUID = 1L;

	private LocalDateTime backupTime;
	private List<Employee> employees;

	public AppBackup(List<Employee> employees) {
		this.backupTime = LocalDateTime.now();
		this.employees = employees;
	}

	public LocalDateTime getBackupTime() {
		return backupTime;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	@Override
	public String toString() {
		return "AppBackup{" + "backupTime=" + backupTime + ", employeeCount="
				+ (employees != null ? employees.size() : 0) + '}';

	}
}
