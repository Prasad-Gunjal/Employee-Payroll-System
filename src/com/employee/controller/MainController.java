package com.employee.controller;

import java.io.File;

import com.employee.file.LogUtil;
import com.employee.menu.LoginMenu;
import com.employee.menu.MainMenu;
import com.employee.model.User;
import com.employee.util.Constants;

public class MainController {
	private final LoginMenu loginMenu = new LoginMenu();
	private final MainMenu mainMenu = new MainMenu();

	public void run() {
		ensureDataDirectories();
		LogUtil.info("Application started.");

		while (true) {
			User user = loginMenu.show();
			if (user == null) {
				// login attempts exhausted
				break;
			}

			boolean stayLoggedIn = true;
			while (stayLoggedIn) {
				try {
					stayLoggedIn = mainMenu.show(user);
				} catch (Exception e) {
					System.out.println("Unexpected error: " + e.getMessage());
					LogUtil.Error("Unexpected error in main menu: " + e.getMessage());
				}
			}
			// stayLoggedIn == false means user logged out -> loop back to login screen
		}

		LogUtil.info("Application exited.");
		System.out.println("Application closed.");
	}

	private void ensureDataDirectories() {
		String[] dirs = { Constants.EMPLOYEE_DIR, Constants.ATTENDANCE_DIR, Constants.PAYROLL_DIR,
				Constants.REPORTS_DIR, Constants.LOGS_DIR, Constants.BACKUP_DIR };
		for (String dir : dirs) {
			new File(dir).mkdirs();
		}
	}
}
