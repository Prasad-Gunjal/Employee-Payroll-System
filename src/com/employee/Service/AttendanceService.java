package com.employee.Service;

import java.time.LocalDate;
import java.util.List;

import com.employee.dao.AttendanceDAO;
import com.employee.exception.InvalidAttendanceException;
import com.employee.file.LogUtil;
import com.employee.model.Attendance;
import com.employee.model.AttendanceStatus;

public class AttendanceService {

	
	 private final AttendanceDAO attendanceDAO = new AttendanceDAO();

	    public int markAttendance(int employeeId, LocalDate date, AttendanceStatus status)
	            throws InvalidAttendanceException {
	        if (date.isAfter(LocalDate.now())) {
	            throw new InvalidAttendanceException("Cannot mark attendance for a future date: " + date);
	        }
	        Attendance existing = attendanceDAO.findByEmployeeAndDate(employeeId, date);
	        if (existing != null) {
	            throw new InvalidAttendanceException(
	                    "Attendance already marked for employee " + employeeId + " on " + date +
	                            ". Use update instead.");
	        }
	        int id = attendanceDAO.insert(new Attendance(employeeId, date, status));
	        LogUtil.info("Attendance marked: employee=" + employeeId + " date=" + date + " status=" + status);
	        return id;
	    }

	    public boolean updateAttendance(int employeeId, LocalDate date, AttendanceStatus status)
	            throws InvalidAttendanceException {
	        Attendance existing = attendanceDAO.findByEmployeeAndDate(employeeId, date);
	        if (existing == null) {
	            throw new InvalidAttendanceException(
	                    "No attendance record exists for employee " + employeeId + " on " + date);
	        }
	        existing.setStatus(status);
	        boolean ok = attendanceDAO.update(existing);
	        LogUtil.info("Attendance updated: employee=" + employeeId + " date=" + date + " status=" + status);
	        return ok;
	    }

	    public List<Attendance> getAttendanceForEmployee(int employeeId) {
	        return attendanceDAO.findByEmployee(employeeId);
	    }

	    public List<Attendance> getAttendanceForEmployeeMonth(int employeeId, String yearMonth) {
	        return attendanceDAO.findByEmployeeAndMonth(employeeId, yearMonth);
	    }

	    public List<Attendance> getAttendanceByDate(LocalDate date) {
	        return attendanceDAO.findByDate(date);
	    }

	    public long countByStatus(List<Attendance> records, AttendanceStatus status) {
	        return records.stream().filter(a -> a.getStatus() == status).count();
	    }

	    public int countPresentDays(int employeeId, String yearMonth) {
	        return (int) countByStatus(getAttendanceForEmployeeMonth(employeeId, yearMonth), AttendanceStatus.PRESENT);
	    }

	    public int countAbsentDays(int employeeId, String yearMonth) {
	        return (int) countByStatus(getAttendanceForEmployeeMonth(employeeId, yearMonth), AttendanceStatus.ABSENT);
	    }

	    public int countLeaveDays(int employeeId, String yearMonth) {
	        return (int) countByStatus(getAttendanceForEmployeeMonth(employeeId, yearMonth), AttendanceStatus.LEAVE);
	    }

	    public int countHalfDays(int employeeId, String yearMonth) {
	        return (int) countByStatus(getAttendanceForEmployeeMonth(employeeId, yearMonth), AttendanceStatus.HALF_DAY);
	    }
	
	
}
