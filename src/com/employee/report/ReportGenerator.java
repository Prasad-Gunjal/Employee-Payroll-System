package com.employee.report;

import java.util.List;
import java.util.Map;

import com.employee.model.Attendance;
import com.employee.model.Employee;
import com.employee.model.Payroll;

public class ReportGenerator {

    public String allEmployeesReport(List<Employee> employees) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== ALL EMPLOYEES REPORT =====\n");
        for (Employee e : employees) {
            sb.append(String.format("[%d] %-20s %-15s %-10s Rs.%.2f%n",
                    e.getEmployeeId(), e.getFirstName(),
                    e.getDepartment() != null ? e.getDepartment().getDeptName() : "N/A",
                    e.getStatus(), e.getBasicSalary()));
        }
        sb.append("Total Employees: ").append(employees.size()).append("\n");
        return sb.toString();
    }

    public String departmentWiseReport(Map<String, List<Employee>> grouped) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== DEPARTMENT-WISE EMPLOYEE REPORT =====\n");
        for (Map.Entry<String, List<Employee>> entry : grouped.entrySet()) {
            sb.append("\nDepartment: ").append(entry.getKey())
              .append(" (").append(entry.getValue().size()).append(" employees)\n");
            for (Employee e : entry.getValue()) {
                sb.append("   - [").append(e.getEmployeeId()).append("] ").append(e.getFirstName()).append("\n");
            }
        }
        return sb.toString();
    }

    public String attendanceReport(List<Attendance> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== ATTENDANCE REPORT =====\n");
        for (Attendance a : records) {
            sb.append(String.format("EmployeeID: %-6d Date: %-12s Status: %s%n",
                    a.getEmployeeId(), a.getAttendanceDate(), a.getStatus()));
        }
        return sb.toString();
    }

    public String monthlyPayrollReport(List<Payroll> records, String month) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== PAYROLL REPORT: ").append(month).append(" =====\n");
        double totalExpense = 0;
        for (Payroll p : records) {
            sb.append(String.format("EmployeeID: %-6d Present: %-3d Absent: %-3d Leave: %-3d Net: Rs.%.2f%n",
                    p.getEmployeeId(), p.getPresentDays(), p.getAbsentDays(), p.getLeaveDays(), p.getNetSalary()));
            totalExpense += p.getNetSalary();
        }
        sb.append(String.format("%nTotal Payroll Expense: Rs.%.2f%n", totalExpense));
        sb.append("Employees Paid: ").append(records.size()).append("\n");
        return sb.toString();
    }

    public String salarySummary(double totalExpense, int employeeCount) {
        double avg = employeeCount == 0 ? 0 : totalExpense / employeeCount;
        return String.format("===== SALARY SUMMARY =====%nTotal Employees: %d%nTotal Salary Expense: Rs.%.2f%nAverage Salary: Rs.%.2f%n",
                employeeCount, totalExpense, avg);
    }
}
