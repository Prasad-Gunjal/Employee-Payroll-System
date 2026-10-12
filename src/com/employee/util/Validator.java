package com.employee.util;

import java.util.regex.Pattern;

import com.employee.exception.InvalidInputException;
import com.employee.exception.invalidSalaryException;

public class Validator {

	private Validator() {

	}

	static public void NoBlankInput(String value, String fieldName) throws InvalidInputException {
		if (value == null || value.trim().isEmpty()) {
			throw new InvalidInputException("Please Enter Valid Input");
		}

	}

	static public void validEmail(String email) {

		NoBlankInput(email, "email");

		if (!Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
				email)) {
			throw new InvalidInputException("Email is not valid..");
		}

	}

	static public void validMobileNo(String MobNo) {
		NoBlankInput(MobNo, "MobNo");

		if (!Pattern.matches("^[6-9][0-9]{9}$", MobNo)) {

			throw new InvalidInputException("Invalid Mobile Number");
		}

	}

	static public void adharNo(String adhar) {
		NoBlankInput(adhar, "adhar");

		if (!Pattern.matches("^[2-9][0-9]{11}$", adhar)) {
			throw new InvalidInputException("Adhar Number is invalid..");
		}

	}

	public static void validateSalary(double salary) {

		if (salary < 0) {
			throw new invalidSalaryException("Basic Salary Must Be Greater Than 0. Got:" + salary);
		}
		if (salary > 10_000_000) {
			throw new invalidSalaryException("Basic Salary Must Be Less Than 10000000. Got:" + salary);

		}

	}

	public static void validatePositive(double value, String FieldName) {
		if (value < 0) {
			throw new InvalidInputException(FieldName + "field Name Can not be Negative");
		}
	}

}
