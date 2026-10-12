package com.employee.model;


public class Overtime {

    private static final long serialVersionUID = 1L;
    public static final double OVERTIME_MULTIPLIER = 1.5;

    private int overtimeId;
    private int employeeId;
    private String overtimeMonth;
    private double overtimeHours;
    private double hourlyRate;
    private double overtimePay;

    public Overtime() {}

    public Overtime(int employeeId, String overtimeMonth, double overtimeHours, double basicSalary) {
        this.employeeId    = employeeId;
        this.overtimeMonth = overtimeMonth;
        this.overtimeHours = overtimeHours;
        this.hourlyRate    = basicSalary / 208.0;
        this.overtimePay   = overtimeHours * this.hourlyRate * OVERTIME_MULTIPLIER;
    }

    public int getOvertimeId() { return overtimeId; }
    public void setOvertimeId(int id) { this.overtimeId = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int id) { this.employeeId = id; }
    public String getOvertimeMonth() { return overtimeMonth; }
    public void setOvertimeMonth(String m) { this.overtimeMonth = m; }
    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double h) { this.overtimeHours = h; }
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double r) { this.hourlyRate = r; }
    public double getOvertimePay() { return overtimePay; }
    public void setOvertimePay(double p) { this.overtimePay = p; }

    public String toString() {
        return String.format("Overtime{empId=%d, month='%s', hours=%.1f, hourlyRate=%.2f, pay=%.2f}",
            employeeId, overtimeMonth, overtimeHours, hourlyRate, overtimePay);
    }
}
