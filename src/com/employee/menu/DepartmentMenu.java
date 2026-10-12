package com.employee.menu;

import java.util.List;

import com.employee.model.Department;
import com.employee.Service.DepartmentService;
import com.employee.util.ConsoleUI;
import com.employee.util.InputUtil;

public class DepartmentMenu {

	private final DepartmentService departmentService = new DepartmentService();

	public void show() {
		boolean back = false;
		while (!back) {
			System.out.println();
			System.out.println(ConsoleUI.topBorder());
			System.out.println(ConsoleUI.centered("DEPARTMENT MANAGEMENT"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("1", "Add Department"));
			System.out.println(ConsoleUI.option("2", "Update Department"));
			System.out.println(ConsoleUI.option("3", "Delete Department"));
			System.out.println(ConsoleUI.option("4", "Search Department"));
			System.out.println(ConsoleUI.option("5", "Display All Departments"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("0", "Back to Main Menu"));
			System.out.println(ConsoleUI.bottomBorder());
			int choice = InputUtil.readInt("  Enter choice: ");
			try {
				switch (choice) {
				case 1:
					addDepartment();
					break;
				case 2:
					updateDepartment();
					break;
				case 3:
					deleteDepartment();
					break;
				case 4:
					searchDepartment();
					break;
				case 5:
					displayAll();
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

	private void addDepartment() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("ADD DEPARTMENT"));
		System.out.println(ConsoleUI.divider());
		String name = InputUtil.readString("  Department name : ");
		String desc = InputUtil.readOptionalString("  Description     : ");
		int id = departmentService.addDepartment(name, desc);
		ConsoleUI.success("Department added with ID: " + id);
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void updateDepartment() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("UPDATE DEPARTMENT"));
		System.out.println(ConsoleUI.divider());
		int id = InputUtil.readInt("  Department ID : ");
		Department dept = departmentService.getById(id);
		if (dept == null) {
			ConsoleUI.error("No department found with ID: " + id);
			System.out.println(ConsoleUI.bottomBorder());
			return;
		}
		ConsoleUI.info("Current name: " + dept.getDeptName());
		dept.setDeptName(InputUtil.readString("  New name        : "));
		dept.setDeptDesc(InputUtil.readOptionalString("  New description : "));
		boolean ok = departmentService.updateDepartment(dept);
		if (ok)
			ConsoleUI.success("Department updated.");
		else
			ConsoleUI.error("Update failed.");
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void deleteDepartment() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("DELETE DEPARTMENT"));
		System.out.println(ConsoleUI.divider());
		int id = InputUtil.readInt("  Department ID : ");
		boolean ok = departmentService.deleteDepartment(id);
		if (ok)
			ConsoleUI.success("Department deleted.");
		else
			ConsoleUI.error("Delete failed (employees may still be assigned).");
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void searchDepartment() {
		String name = InputUtil.readString("  Search by name: ");
		printList(departmentService.search(name));
	}

	private void displayAll() {
		printList(departmentService.getAll());
	}

	private void printList(List<Department> list) {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("DEPARTMENTS"));
		System.out.println(ConsoleUI.divider());
		if (list.isEmpty()) {
			System.out.println(ConsoleUI.centered("No departments found."));
		} else {
			for (Department d : list)
				System.out.printf("  [%d] %-20s %s%n", d.getDeptId(), d.getDeptName(),
						d.getDeptDesc() != null ? d.getDeptDesc() : "");
		}
		System.out.println(ConsoleUI.bottomBorder());
	}
}
