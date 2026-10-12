package com.employee.model;

import java.time.LocalDate;
import java.time.Period;

public class Employee implements Comparable<Employee> {

	private int employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private Gender gender;
	private Department department;
	private String designation;
	private LocalDate joiningDate;
	private double basicSalary;
	private EmployeeStatus status;

	public Employee() {

	}

	public Employee(String firstName, String lastName, String email, String phone, String address, Gender gender,
			Department department, String designation, LocalDate joiningDate, double basicSalary,
			EmployeeStatus status) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.gender = gender;
		this.department = department;
		this.designation = designation;
		this.joiningDate = joiningDate;
		this.basicSalary = basicSalary;
		this.status = status;
	}

	public Employee(String firstName, String lastName, String email, String phone, String address,
			Gender gender, Department department, String designation, LocalDate joiningDate, double basicSalary) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.gender = gender;
		this.department = department;
		this.designation = designation;
		this.joiningDate = joiningDate;
		this.basicSalary = basicSalary;
		this.status = status;
	}

	public String getfullName() {
		return firstName + " " + lastName;

	}

	public int getYearsofService() {
		if (joiningDate == null) {
			return 0;
		}
		return Period.between(joiningDate, LocalDate.now()).getYears();

	}

	public int getEmployeeId() {
		return employeeId;

	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public EmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", phone=" + phone + ", address=" + address + ", gender=" + gender + ", department="
				+ department + ", designation=" + designation + ", joiningDate=" + joiningDate + ", basicSalary="
				+ basicSalary + ", status=" + status + "]";
	}

	@Override
	public int compareTo(Employee o) {
		if (o.getEmployeeId() > this.getEmployeeId()) {
			return 1;
		} else if (o.getEmployeeId() < this.getEmployeeId()) {
			return -1;
		} else {
			return 0;
		}
	}

}
