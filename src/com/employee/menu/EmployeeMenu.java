package com.employee.menu;

import java.util.List;

import com.employee.Service.DepartmentService;
import com.employee.Service.EmployeeService;
import com.employee.email.EmailService;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.InvalidInputException;
import com.employee.exception.invalidSalaryException;
import com.employee.file.FileExportUtil;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.model.Gender;
import com.employee.util.ConsoleUI;
import com.employee.util.DateUtil;
import com.employee.util.InputUtil;

public class EmployeeMenu {

	private final EmployeeService employeeService = new EmployeeService();
	private final DepartmentService departmentService = new DepartmentService();
	private final FileExportUtil fileExportUtil = new FileExportUtil();

	public void show() {
		boolean back = false;
		while (!back) {
			System.out.println();
			System.out.println(ConsoleUI.topBorder());
			System.out.println(ConsoleUI.centered("EMPLOYEE MANAGEMENT"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("1", "Add Employee"));
			System.out.println(ConsoleUI.option("2", "Update Employee"));
			System.out.println(ConsoleUI.option("3", "Delete Employee"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("4", "Search by ID"));
			System.out.println(ConsoleUI.option("5", "Search by Name"));
			System.out.println(ConsoleUI.option("6", "Search by Department"));
			System.out.println(ConsoleUI.option("7", "Display All Employees"));
			System.out.println(ConsoleUI.option("8", "Sort Employees"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("9", "Count Employees"));
			System.out.println(ConsoleUI.option("10", "Export Employee Data (CSV)"));
			System.out.println(ConsoleUI.option("11", "Print Employee Profile"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("0", "Back to Main Menu"));
			System.out.println(ConsoleUI.bottomBorder());
			int choice = InputUtil.readInt("  Enter choice: ");
			try {
				switch (choice) {
				case 1:
					addEmployee();
					break;
				case 2:
					updateEmployee();
					break;
				case 3:
					deleteEmployee();
					break;
				case 4:
					searchById();
					break;
				case 5:
					searchByName();
					break;
				case 6:
					searchByDepartment();
					break;
				case 7:
					displayAll();
					break;
				case 8:
					sortMenu();
					break;
				case 9:
					System.out.println();
					ConsoleUI.info("Total employees: " + employeeService.countEmployees());
					break;
				case 10:
					exportCsv();
					break;
				case 11:
					printEmployeeDetails();
					break;
				case 0:
					back = true;
					break;
				default:
					ConsoleUI.error("Invalid choice.");
				}
			} catch (InvalidInputException | invalidSalaryException | EmployeeNotFoundException e) {
				ConsoleUI.error(e.getMessage());
			} catch (Exception e) {
				ConsoleUI.error("Unexpected: " + e.getMessage());
			}
			if (!back)
				InputUtil.pressEnterToContinue();
		}
	}

	private Department pickDepartment() {
		List<Department> depts = departmentService.getAll();
		if (depts.isEmpty())
			throw new InvalidInputException("No departments found. Add one first.");
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("DEPARTMENTS"));
		System.out.println(ConsoleUI.divider());
		for (Department d : depts)
			System.out.printf("  [%d] %s%n", d.getDeptId(), d.getDeptName());
		System.out.println(ConsoleUI.bottomBorder());
		int id = InputUtil.readInt("  Enter department ID: ");
		Department chosen = departmentService.getById(id);
		if (chosen == null)
			throw new InvalidInputException("No department with ID: " + id);
		return chosen;
	}

	private void addEmployee() throws invalidSalaryException {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("ADD EMPLOYEE"));
		System.out.println(ConsoleUI.divider());
		String first = InputUtil.readString("  First name     : ");
		String last = InputUtil.readString("  Last name      : ");
		String email = InputUtil.readString("  Email          : ");
		String phone = InputUtil.readString("  Phone (10 dig) : ");
		String address = InputUtil.readOptionalString("  Address        : ");
		char g = InputUtil.readChar("  Gender (M/F/O) : ");
		Gender gender = g == 'M' || g == 'm' ? Gender.MALE : g == 'F' || g == 'f' ? Gender.FEMALE : Gender.OTHER;
		Department dept = pickDepartment();
		String designation = InputUtil.readString("  Designation    : ");
		java.time.LocalDate joiningDate = InputUtil.readDate("  Joining date");
		double basicSalary = InputUtil.readDouble("  Basic salary   : ");
		Employee emp = new Employee(first, last, email, phone, address, gender, dept, designation, joiningDate,
				basicSalary);
		int id = employeeService.addEmployee(emp);
		emp.setEmployeeId(id);
		ConsoleUI.success("Employee added with ID: " + id);
		System.out.println(ConsoleUI.bottomBorder());
		String sendMail = InputUtil.readOptionalString("  Send welcome email? (Y/N): ");
		if ("Y".equalsIgnoreCase(sendMail.trim())) {
			try {
				new EmailService().sendWelcomeEmail(emp);
				ConsoleUI.success("Welcome email sent to " + emp.getEmail());
			} catch (Exception ex) {
				ConsoleUI.error("Could not send email: " + ex.getMessage());
			}
		}
	}

	private void updateEmployee() throws EmployeeNotFoundException, invalidSalaryException {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("UPDATE EMPLOYEE"));
		System.out.println(ConsoleUI.divider());
		int id = InputUtil.readInt("  Employee ID : ");
		Employee emp = employeeService.getEmployeeOrThrow(id);
		ConsoleUI.info("Name: " + emp.getFirstName() + " | Dept: " + emp.getDepartment().getDeptName());
		emp.setPhone(InputUtil.readString("  New phone         : "));
		emp.setAddress(InputUtil.readOptionalString("  New address        : "));
		emp.setDesignation(InputUtil.readString("  New designation    : "));
		emp.setBasicSalary(InputUtil.readDouble("  New basic salary   : "));
		employeeService.updateEmployee(emp);
		ConsoleUI.success("Employee updated successfully.");
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void deleteEmployee() throws EmployeeNotFoundException {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("DELETE EMPLOYEE"));
		System.out.println(ConsoleUI.divider());
		int id = InputUtil.readInt("  Employee ID : ");
		employeeService.deleteEmployee(id);
		ConsoleUI.success("Employee deleted.");
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void searchById() throws EmployeeNotFoundException {
		int id = InputUtil.readInt("  Employee ID: ");
		Employee emp = employeeService.getEmployeeOrThrow(id);
		printProfile(emp);
	}

	private void searchByName() {
		String name = InputUtil.readString("  Name: ");
		printList(employeeService.searchByName(name));
	}

	private void searchByDepartment() {
		printList(employeeService.searchByDepartment(pickDepartment().getDeptId()));
	}

	private void displayAll() {
		printList(employeeService.getAllEmployees());
	}

	private void sortMenu() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("SORT EMPLOYEES"));
		System.out.println(ConsoleUI.divider());
		System.out.println(ConsoleUI.option("1", "Sort by ID"));
		System.out.println(ConsoleUI.option("2", "Sort by Name"));
		System.out.println(ConsoleUI.option("3", "Sort by Salary (High to Low)"));
		System.out.println(ConsoleUI.option("4", "Sort by Joining Date"));
		System.out.println(ConsoleUI.bottomBorder());
		int c = InputUtil.readInt("  Choice: ");
		List<Employee> all = employeeService.getAllEmployees();
		List<Employee> sorted;
		switch (c) {
		case 2:
			sorted = employeeService.sortByName(all);
			break;
		case 3:
			sorted = employeeService.sortBySalaryDesc(all);
			break;
		case 4:
			sorted = employeeService.sortByJoiningDate(all);
			break;
		default:
			sorted = employeeService.sortById(all);
		}
		printList(sorted);
	}

	private void exportCsv() {
		List<Employee> all = employeeService.getAllEmployees();
		String path = fileExportUtil.exportEmployeesToCsv(all, "employees_" + System.currentTimeMillis() + ".csv");
		ConsoleUI.success("Exported to: " + path);
	}

	private void printEmployeeDetails() throws EmployeeNotFoundException {
		int id = InputUtil.readInt("  Employee ID: ");
		printProfile(employeeService.getEmployeeOrThrow(id));
	}

	private void printProfile(Employee emp) {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("EMPLOYEE PROFILE"));
		System.out.println(ConsoleUI.divider());
		ConsoleUI.field("Employee ID", String.valueOf(emp.getEmployeeId()));
		ConsoleUI.field("Full Name", emp.getFirstName());
		ConsoleUI.field("Email", emp.getEmail());
		ConsoleUI.field("Phone", emp.getPhone());
		ConsoleUI.field("Address", emp.getAddress() != null ? emp.getAddress() : "N/A");
		ConsoleUI.field("Gender", emp.getGender().toString());
		ConsoleUI.field("Department", emp.getDepartment() != null ? emp.getDepartment().getDeptName() : "N/A");
		ConsoleUI.field("Designation", emp.getDesignation());
		ConsoleUI.field("Joining Date", DateUtil.formatDate(emp.getJoiningDate()));
		ConsoleUI.field("Years of Svc", emp.getYearsofService() + " yrs");
		ConsoleUI.field("Basic Salary", "Rs. " + String.format("%.2f", emp.getBasicSalary()));
		ConsoleUI.field("Status", emp.getStatus().toString());
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void printList(List<Employee> list) {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("EMPLOYEES"));
		System.out.println(ConsoleUI.divider());
		if (list.isEmpty()) {
			System.out.println(ConsoleUI.centered("No employees found."));
		} else {
			System.out.printf("  %-4s %-20s %-15s %-12s %-12s %s%n", "ID", "Name", "Dept", "Designation", "Salary",
					"Status");
			System.out.println("  " + ConsoleUI.repeat("-", 50));
			for (Employee e : list)
				System.out.printf("  %-4d %-20s %-15s %-12s %-12.2f %s%n", e.getEmployeeId(), e.getFirstName(),
						e.getDepartment() != null ? e.getDepartment().getDeptName() : "N/A", e.getDesignation(),
						e.getBasicSalary(), e.getStatus());
		}
		System.out.println(ConsoleUI.bottomBorder());
	}
}
