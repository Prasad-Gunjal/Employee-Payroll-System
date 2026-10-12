package com.employee.menu;

import com.employee.Service.LoginService;
import com.employee.model.User;
import com.employee.util.ConsoleUI;
import com.employee.util.InputUtil;

public class MainMenu {

	 private final EmployeeMenu   employeeMenu   = new EmployeeMenu();
	    private final DepartmentMenu departmentMenu = new DepartmentMenu();
	    private final AttendanceMenu attendanceMenu = new AttendanceMenu();
	    private final PayrollMenu    payrollMenu    = new PayrollMenu();
	    private final ReportMenu     reportMenu     = new ReportMenu();
	    private final LoginService  loginService   = new LoginService();

	    public boolean show(User currentUser) {
	        System.out.println();
	        System.out.println(ConsoleUI.topBorder());
	        System.out.println(ConsoleUI.centered("MAIN MENU"));
	        System.out.println(ConsoleUI.centered("Logged in as: " + currentUser.getUsername()));
	        System.out.println(ConsoleUI.divider());
	        System.out.println(ConsoleUI.option("1", "Employee Management"));
	        System.out.println(ConsoleUI.option("2", "Department Management"));
	        System.out.println(ConsoleUI.option("3", "Attendance Management"));
	        System.out.println(ConsoleUI.option("4", "Payroll Processing"));
	        System.out.println(ConsoleUI.option("5", "Reports & Backup"));
	        System.out.println(ConsoleUI.divider());
	        System.out.println(ConsoleUI.option("6", "Change Password"));
	        System.out.println(ConsoleUI.option("7", "Logout"));
	        System.out.println(ConsoleUI.option("0", "Exit Application"));
	        System.out.println(ConsoleUI.bottomBorder());

	        int choice = InputUtil.readInt("  Enter choice: ");
	        switch (choice) {
	            case 1: employeeMenu.show(); break;
	            case 2: departmentMenu.show(); break;
	            case 3: attendanceMenu.show(); break;
	            case 4: payrollMenu.show(); break;
	            case 5: reportMenu.show(); break;
	            case 6: changePassword(currentUser); break;
	            case 7: loginService.logout(currentUser); return false;
	            case 0: System.out.println("\n  Goodbye! Have a great day!"); System.exit(0); break;
	            default: ConsoleUI.error("Invalid choice. Please try again.");
	        }
	        return true;
	    }

	    private void changePassword(User currentUser) {
	        System.out.println();
	        System.out.println(ConsoleUI.topBorder());
	        System.out.println(ConsoleUI.centered("CHANGE PASSWORD"));
	        System.out.println(ConsoleUI.divider());
	        System.out.println(ConsoleUI.blankRow());
	        try {
	            String current = InputUtil.readString("  Current password   : ");
	            String newPass = InputUtil.readString("  New password       : ");
	            if (newPass.length() < 6) { ConsoleUI.error("Password must be at least 6 characters."); InputUtil.pressEnterToContinue(); return; }
	            String confirm = InputUtil.readString("  Confirm new password: ");
	            if (!newPass.equals(confirm)) { ConsoleUI.error("Passwords do not match. Not changed."); InputUtil.pressEnterToContinue(); return; }
	            if (loginService.changePassword(currentUser, current, newPass)) {
	                System.out.println(ConsoleUI.blankRow());
	                System.out.println(ConsoleUI.centered("Password changed successfully!"));
	            } else {
	                ConsoleUI.error("Current password incorrect. Not changed.");
	            }
	        } catch (Exception e) { ConsoleUI.error(e.getMessage()); }
	        System.out.println(ConsoleUI.blankRow());
	        System.out.println(ConsoleUI.bottomBorder());
	        InputUtil.pressEnterToContinue();
	    }
}
