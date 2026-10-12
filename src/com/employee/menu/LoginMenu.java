package com.employee.menu;

import com.employee.Service.LoginService;
import com.employee.model.User;
import com.employee.util.InputUtil;

public class LoginMenu {

	private final LoginService loginservice = new LoginService();

	public User show() {

		System.out.println("\n========================================================================");
		System.out.println("\n================= EMPLOYEE MANAGEMENT AND PAYROLL SYSTEM  ==============");
		System.out.println("\n========================================================================");

		while (!loginservice.isLockedOut()) {

			try {

				String username = InputUtil.readString("Username: ");
				String password = InputUtil.readString("Password: ");

				User user = loginservice.login(username, password);
				if (user != null) {
					System.out.println("Welcome User login successfull !" + user.getUsername());
					return user;

				} else {
					System.out.println("Invalid Credentials Remaining Attempts:" + loginservice.getRemainingAttempts());

				}

			} catch (Exception e) {
				System.out.println("Error occured " + e.getMessage());
			}

		}
		System.out.println("Your Maximum Attempts Exceeded:");
		return null;
	}

}
