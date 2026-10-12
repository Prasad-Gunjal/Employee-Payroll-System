package com.employee.model;

import java.time.LocalDate;

public class Attendance {

	private int attendanceId;
	private int employeeId;
	private LocalDate attendanceDate;
	private AttendanceStatus status;

	public Attendance() {

	}

	public Attendance(int attendanceId, LocalDate attendanceDate, AttendanceStatus status) {
		super();
		this.attendanceId = attendanceId;
		this.attendanceDate = attendanceDate;
		this.status = status;
	}

	public Attendance(int attendanceId, int employeeId, LocalDate attendanceDate, AttendanceStatus status) {
		super();
		this.attendanceId = attendanceId;
		this.employeeId = employeeId;
		this.attendanceDate = attendanceDate;
		this.status = status;
	}

	public int getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(int attendanceId) {
		this.attendanceId = attendanceId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public AttendanceStatus getStatus() {
		return status;
	}

	public void setStatus(AttendanceStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Attendance [attendanceId=" + attendanceId + ", employeeId=" + employeeId + ", attendanceDate="
				+ attendanceDate + ", status=" + status + "]";
	}
	
	

}
