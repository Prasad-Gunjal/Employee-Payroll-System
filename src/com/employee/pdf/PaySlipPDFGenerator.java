package com.employee.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

import com.employee.file.LogUtil;
import com.employee.model.Employee;
import com.employee.model.Salary;
import com.employee.util.Constants;
import com.employee.util.DateUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PaySlipPDFGenerator {

    public PaySlipPDFGenerator() {
        new File(Constants.REPORTS_DIR).mkdirs();
    }

    public String generatePayslipPdf(Employee emp, Salary salary, String fileName) {
        File file = new File(Constants.REPORTS_DIR, fileName);
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            Paragraph title = new Paragraph(Constants.COMPANY_NAME, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Payslip for " + salary.getPayrollMonth(), headerFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(15);
            document.add(subtitle);

            // Employee info table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            addInfoRow(infoTable, "Employee ID", String.valueOf(emp.getEmployeeId()), normalFont);
            addInfoRow(infoTable, "Employee Name", emp.getFirstName(), normalFont);
            addInfoRow(infoTable, "Department",
                    emp.getDepartment() != null ? emp.getDepartment().getDeptName() : "N/A", normalFont);
            addInfoRow(infoTable, "Designation", emp.getDesignation(), normalFont);
            addInfoRow(infoTable, "Payroll Month", salary.getPayrollMonth(), normalFont);
            document.add(infoTable);

            document.add(new Paragraph(" "));

            // Earnings / Deductions table
            PdfPTable payTable = new PdfPTable(2);
            payTable.setWidthPercentage(100);
            payTable.addCell(headerCell("Earnings", headerFont));
            payTable.addCell(headerCell("Deductions", headerFont));

            payTable.addCell(dataCell("Basic Salary: " + String.format("%.2f", salary.getBasicSalary()), normalFont));
            payTable.addCell(dataCell("PF: " + String.format("%.2f", salary.getPf()), normalFont));

            payTable.addCell(dataCell("HRA: " + String.format("%.2f", salary.getHra()), normalFont));
            payTable.addCell(dataCell("Professional Tax: " + String.format("%.2f", salary.getProfessionalTax()), normalFont));

            payTable.addCell(dataCell("DA: " + String.format("%.2f", salary.getDa()), normalFont));
            payTable.addCell(dataCell("Other Deduction: " + String.format("%.2f", salary.getOtherDeduction()), normalFont));

            payTable.addCell(dataCell("Other Allowances: " + String.format("%.2f", salary.getAllowance()), normalFont));
            payTable.addCell(dataCell("", normalFont));

            document.add(payTable);
            document.add(new Paragraph(" "));

            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(100);
            addInfoRow(totalsTable, "Gross Salary", String.format("Rs. %.2f", salary.getGrossSalary()), headerFont);
            addInfoRow(totalsTable, "Total Deduction", String.format("Rs. %.2f", salary.getTotalDeduction()), headerFont);
            addInfoRow(totalsTable, "NET SALARY", String.format("Rs. %.2f", salary.getNetSalary()), headerFont);
            document.add(totalsTable);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Generated Date: " + DateUtil.formatDate(LocalDate.now()), normalFont));

            Paragraph signature = new Paragraph("\n\nAuthorized Signature: ______________________", normalFont);
            signature.setSpacingBefore(30);
            document.add(signature);

            document.close();
            LogUtil.info("PDF payslip generated: " + file.getPath());
            return file.getPath();

        } catch (Exception e) {
            LogUtil.Error("Failed to generate PDF payslip: " + e.getMessage());
            throw new RuntimeException("Failed to generate PDF payslip: " + e.getMessage(), e);
        }
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font font) {
        table.addCell(dataCell(label, font));
        table.addCell(dataCell(value, font));
    }

    private PdfPCell headerCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell dataCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setPadding(5);
        return cell;
    }
}
