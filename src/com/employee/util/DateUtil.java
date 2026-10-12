package com.employee.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	public static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

	private DateUtil() {
	}

	public static String formatDate(LocalDate date) {
		return date == null ? "" : date.format(DATE_FORMAT);
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		return dateTime == null ? "" : dateTime.format(DATE_TIME_FORMAT);
	}

	public static String currentPayrollMonth() {
		return YearMonth.now().format(MONTH_FORMAT);
	}

	public static boolean isValidPayrollMonth(String month) {
		try {
			YearMonth.parse(month, MONTH_FORMAT);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** Total calendar days in the given "yyyy-MM" payroll month. */
	public static int totalDaysInMonth(String payrollMonth) {
		YearMonth ym = YearMonth.parse(payrollMonth, MONTH_FORMAT);
		return ym.lengthOfMonth();
	}

	public static long daysBetween(LocalDate start, LocalDate end) {
		return ChronoUnit.DAYS.between(start, end);
	}
}
