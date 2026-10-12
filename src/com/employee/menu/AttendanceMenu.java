package com.employee.menu;

import java.time.LocalDate;
import java.util.List;

import com.employee.file.FileExportUtil;
import com.employee.model.Attendance;
import com.employee.model.AttendanceStatus;
import com.employee.Service.AttendanceService;
import com.employee.util.ConsoleUI;
import com.employee.util.DateUtil;
import com.employee.util.InputUtil;

public class AttendanceMenu {
	private final AttendanceService attendanceService = new AttendanceService();
	private final FileExportUtil fileExportUtil = new FileExportUtil();

	public void show() {
		boolean back = false;
		while (!back) {
			System.out.println();
			System.out.println(ConsoleUI.topBorder());
			System.out.println(ConsoleUI.centered("ATTENDANCE MANAGEMENT"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("1", "Mark Attendance"));
			System.out.println(ConsoleUI.option("2", "Update Attendance"));
			System.out.println(ConsoleUI.option("3", "View Attendance (by Employee)"));
			System.out.println(ConsoleUI.option("4", "View Attendance (by Date)"));
			System.out.println(ConsoleUI.option("5", "Monthly Summary (Present/Absent/Leave)"));
			System.out.println(ConsoleUI.option("6", "Export Attendance Report (CSV)"));
			System.out.println(ConsoleUI.divider());
			System.out.println(ConsoleUI.option("0", "Back to Main Menu"));
			System.out.println(ConsoleUI.bottomBorder());
			int choice = InputUtil.readInt("  Enter choice: ");
			try {
				switch (choice) {
				case 1:
					markAttendance();
					break;
				case 2:
					updateAttendance();
					break;
				case 3:
					viewByEmployee();
					break;
				case 4:
					viewByDate();
					break;
				case 5:
					summary();
					break;
				case 6:
					exportCsv();
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

	private AttendanceStatus pickStatus() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("SELECT STATUS"));
		System.out.println(ConsoleUI.divider());
		System.out.println(ConsoleUI.option("1", "PRESENT"));
		System.out.println(ConsoleUI.option("2", "ABSENT"));
		System.out.println(ConsoleUI.option("3", "HALF DAY"));
		System.out.println(ConsoleUI.option("4", "LEAVE"));
		System.out.println(ConsoleUI.bottomBorder());
		int c = InputUtil.readInt("  Choice: ");
		switch (c) {
		case 2:
			return AttendanceStatus.ABSENT;
		case 3:
			return AttendanceStatus.HALF_DAY;
		case 4:
			return AttendanceStatus.LEAVE;
		default:
			return AttendanceStatus.PRESENT;
		}
	}

	private void markAttendance() throws Exception {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("MARK ATTENDANCE"));
		System.out.println(ConsoleUI.divider());
		int empId = InputUtil.readInt("  Employee ID : ");
		LocalDate date = InputUtil.readDate("  Date");
		AttendanceStatus status = pickStatus();
		int id = attendanceService.markAttendance(empId, date, status);
		ConsoleUI.success("Attendance marked | ID: " + id + " | Status: " + status);
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void updateAttendance() throws Exception {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("UPDATE ATTENDANCE"));
		System.out.println(ConsoleUI.divider());
		int empId = InputUtil.readInt("  Employee ID : ");
		LocalDate date = InputUtil.readDate("  Date");
		AttendanceStatus status = pickStatus();
		boolean ok = attendanceService.updateAttendance(empId, date, status);
		if (ok)
			ConsoleUI.success("Attendance updated.");
		else
			ConsoleUI.error("Update failed.");
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void viewByEmployee() {
		int empId = InputUtil.readInt("  Employee ID: ");
		printList(attendanceService.getAttendanceForEmployee(empId));
	}

	private void viewByDate() {
		LocalDate date = InputUtil.readDate("  Date");
		printList(attendanceService.getAttendanceByDate(date));
	}

	private void summary() {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("ATTENDANCE SUMMARY"));
		System.out.println(ConsoleUI.divider());
		int empId = InputUtil.readInt("  Employee ID      : ");
		String month = InputUtil.readString("  Month (yyyy-MM)  : ");
		System.out.println(ConsoleUI.blankRow());
		System.out.printf("  %-20s: %d%n", "Present Days", attendanceService.countPresentDays(empId, month));
		System.out.printf("  %-20s: %d%n", "Absent Days", attendanceService.countAbsentDays(empId, month));
		System.out.printf("  %-20s: %d%n", "Leave Days", attendanceService.countLeaveDays(empId, month));
		System.out.printf("  %-20s: %d%n", "Half Days", attendanceService.countHalfDays(empId, month));
		System.out.println(ConsoleUI.bottomBorder());
	}

	private void exportCsv() {
		int empId = InputUtil.readInt("  Employee ID: ");
		List<Attendance> records = attendanceService.getAttendanceForEmployee(empId);
		String path = fileExportUtil.exportAttendanceReport(records,
				"attendance_" + empId + "_" + System.currentTimeMillis() + ".csv");
		ConsoleUI.success("Exported to: " + path);
	}

	private void printList(List<Attendance> list) {
		System.out.println();
		System.out.println(ConsoleUI.topBorder());
		System.out.println(ConsoleUI.centered("ATTENDANCE RECORDS"));
		System.out.println(ConsoleUI.divider());
		if (list.isEmpty()) {
			System.out.println(ConsoleUI.centered("No records found."));
		} else {
			for (Attendance a : list)
				System.out.printf("  EmpID: %-6d  Date: %-14s  Status: %s%n", a.getEmployeeId(),
						DateUtil.formatDate(a.getAttendanceDate()), a.getStatus());
		}
		System.out.println(ConsoleUI.bottomBorder());
	}
}
