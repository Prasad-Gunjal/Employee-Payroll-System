package com.employee.model;

public class Salary {

	private static final long serialVersionID = 1L;

	public static final double HRA_PERCENT = 20.0;
	public static final double DA_PERCENT = 10.0;
	public static final double PF_PERCENT = 12.0;
	public static final double PROFESSIONAL_TAX = 200.0;

	private int salaryId;
	private int employeeId;
	private double basicSalary;
	private double hra;
	private double da;
	private double allowance;
	private double pf;
	private double professionalTax;
	private double otherDeduction;
	private double grossSalary;
	private double totalDeduction;
	private double netSalary;
	private String payrollMonth;

	public Salary() {

	}

	public Salary(int employeeId, double basicSalary, double allowance, double otherDeduction, String payrollMonth) {
		super();
		this.employeeId = employeeId;
		this.basicSalary = basicSalary;
		this.allowance = allowance;
		this.otherDeduction = otherDeduction;
		this.payrollMonth = payrollMonth;
		computeAll();
	}

	public void computeAll() {
		calculateHRA();
		calculateDA();
		calculatePF();
		calculateProfessionalTax();
		calculateGrossSalary();
		calculateTotalDeduction();
		calculateNetSalary();

	}

	public double calculateHRA() {
		this.hra = basicSalary * (HRA_PERCENT / 100.0);
		return this.hra;

	}

	public double calculateDA() {
		this.hra = basicSalary = (DA_PERCENT / 100.0);
		return this.da;

	}

	public double calculatePF() {
		this.hra = basicSalary * (PF_PERCENT / 100.0);
		return this.pf;

	}

	public double calculateProfessionalTax() {
		this.professionalTax = PROFESSIONAL_TAX;
		return this.professionalTax;

	}

	public double calculateGrossSalary() {
		this.grossSalary = basicSalary + hra + da + allowance;
		return grossSalary;
	}

	public double calculateTotalDeduction() {
		this.totalDeduction = pf + professionalTax + otherDeduction;
		return this.totalDeduction;
	}

	public double calculateNetSalary() {
		this.netSalary = grossSalary - totalDeduction;
		return this.netSalary;
	}

	public int getSalaryId() {
		return salaryId;
	}

	public void setSalaryId(int salaryId) {
		this.salaryId = salaryId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public double getHra() {
		return hra;
	}

	public void setHra(double hra) {
		this.hra = hra;
	}

	public double getDa() {
		return da;
	}

	public void setDa(double da) {
		this.da = da;
	}

	public double getAllowance() {
		return allowance;
	}

	public void setAllowance(double allowance) {
		this.allowance = allowance;
	}

	public double getPf() {
		return pf;
	}

	public void setPf(double pf) {
		this.pf = pf;
	}

	public double getProfessionalTax() {
		return professionalTax;
	}

	public void setProfessionalTax(double professionalTax) {
		this.professionalTax = professionalTax;
	}

	public double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public double getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(double totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(double netSalary) {
		this.netSalary = netSalary;
	}

	public String getPayrollMonth() {
		return payrollMonth;
	}

	public void setPayrollMonth(String payrollMonth) {
		this.payrollMonth = payrollMonth;
	}

	@Override
	public String toString() {
		return "Salary [salaryId=" + salaryId + ", employeeId=" + employeeId + ", basicSalary=" + basicSalary + ", hra="
				+ hra + ", da=" + da + ", allowance=" + allowance + ", pf=" + pf + ", professionalTax="
				+ professionalTax + ", otherDeduction=" + otherDeduction + ", grossSalary=" + grossSalary
				+ ", totalDeduction=" + totalDeduction + ", netSalary=" + netSalary + ", payrollMonth=" + payrollMonth
				+ "]";
	}

}
