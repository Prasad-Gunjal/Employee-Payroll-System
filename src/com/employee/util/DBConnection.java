package com.employee.util;

import java.sql.Connection;
import java.sql.DriverManager;

import com.employee.exception.DatabaseException;

public class DBConnection {

	private static Connection connection = null;
	private static boolean flag = false;

	private DBConnection() {

	}

	public static void loadDriverClass() {

		if (!flag) {
			try {

				Class.forName("com.mysql.cj.jdbc.Driver");
				flag = true;

			} catch (Exception e) {

				throw new DatabaseException("Driver Class Loading failed..");
			}
		}

	}

	public static Connection getConnection() {

		try {
			loadDriverClass();
			if (connection == null) {

				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_payroll_db", "root",
						"Prasad@123");

				return connection;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return connection;
	}

}
