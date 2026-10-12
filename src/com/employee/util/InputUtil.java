package com.employee.util;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

import com.employee.exception.InvalidInputException;

public final class InputUtil {

	private static final Scanner sc = new Scanner(System.in);

	private InputUtil() {

	}

	public static String readString(String prompt) {
		System.out.println(prompt);
		String value = sc.nextLine();
		if (value == null || value.trim().isEmpty()) {
			throw new InvalidInputException("Input Can Not Blank Be Blank");
		}
		return value.trim();
	}

	public static String readOptionalString(String prompt) {
		System.out.println(prompt);
		String value = sc.nextLine();
		return value == null ? "" : value.trim();
	}

	public static int readInt(String prompt) {

		System.out.println(prompt);
		String value = sc.nextLine().trim();
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new InvalidInputException("Expected a whole number but got a value." + value);

		}
	}

	public static double readDouble(String prompt) {
		System.out.println(prompt);
		String value = sc.nextLine().trim();
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new InvalidInputException("Expected a number but got a value." + value);
		}

	}

	public static LocalDate readDate(String prompt) {
		System.out.println(prompt + ":(yyyy-mm-dd)");
		String value = sc.nextLine().trim();

		try {
			return LocalDate.parse(value);

		} catch (DateTimeException e) {
			throw new InvalidInputException("Expected a date in yyyy-mm-dd format but got.'" + value + "'");
		}
	}

	public static char readChar(String prompt) {

		System.out.println(prompt);
		String value = sc.nextLine().trim();

		if (value.length() != 0) {
			throw new InvalidInputException("Expected a Single Character.");
		}
		return value.charAt(0);

	}

	public static void pressEnterToContinue() {
		System.out.println("\nPress Enter To Continue....");
		sc.nextLine();
	}
}
