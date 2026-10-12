package com.employee.email;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.employee.file.LogUtil;
import com.employee.model.Employee;
import com.employee.model.Overtime;
import com.employee.model.Payroll;
import com.employee.util.Constants;

public class EmailService {
	private final Session session;

	public EmailService() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.host", Constants.SMTP_HOST);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.trust", Constants.SMTP_HOST);
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		this.session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constants.EMAIL_FROM, Constants.EMAIL_PASSWORD);
			}
		});
	}

	private MimeMessage buildBase(String toEmail, String subject) throws MessagingException {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(Constants.EMAIL_FROM));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
		msg.setSubject(subject);
		return msg;
	}

	public void sendPayslip(Employee emp, String payrollMonth, File pdfAttachment) {
		try {
			MimeMessage message = buildBase(emp.getEmail(), "Monthly Salary Payslip - " + payrollMonth);
			String body = "Dear " + emp.getFirstName() + ",\n\nYour salary for " + payrollMonth
					+ " has been processed.\nPlease find your payslip attached.\n\nRegards,\nHR Department\n"
					+ Constants.COMPANY_NAME;
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(body);
			MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.attachFile(pdfAttachment);
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(textPart);
			mp.addBodyPart(attachPart);
			message.setContent(mp);
			Transport.send(message);
			LogUtil.info("Payslip emailed to " + emp.getEmail() + " for " + payrollMonth);
		} catch (Exception e) {
			LogUtil.Error("Payslip email failed: " + e.getMessage());
			throw new RuntimeException("Failed to send payslip: " + e.getMessage(), e);
		}
	}

	public void sendWelcomeEmail(Employee emp) {
		try {
			MimeMessage message = buildBase(emp.getEmail(), "Welcome to " + Constants.COMPANY_NAME + "!");
			String body = "Dear " + emp.getFirstName() + ",\n\nWelcome to " + Constants.COMPANY_NAME
					+ "!\n\nYour employee profile has been created.\n\n" + "Employee ID  : " + emp.getEmployeeId()
					+ "\nName         : " + emp.getFirstName() + "\nDepartment   : "
					+ (emp.getDepartment() != null ? emp.getDepartment().getDeptName() : "N/A") + "\nDesignation  : "
					+ emp.getDesignation() + "\nJoining Date : " + emp.getJoiningDate()
					+ "\n\nWe are excited to have you on board!\n\nRegards,\nHR Department\n" + Constants.COMPANY_NAME;
			message.setText(body);
			Transport.send(message);
			LogUtil.info("Welcome email sent to " + emp.getEmail());
		} catch (Exception e) {
			LogUtil.Error("Welcome email failed: " + e.getMessage());
			throw new RuntimeException("Failed to send welcome email: " + e.getMessage(), e);
		}
	}

	public void sendPayrollNotification(Employee emp, Payroll payroll, double netSalary) {
		try {
			MimeMessage message = buildBase(emp.getEmail(), "Salary Processed for " + payroll.getPayrollMonth());
			String body = "Dear " + emp.getFirstName() + ",\n\nYour salary for " + payroll.getPayrollMonth()
					+ " has been processed.\n\n" + "Present Days : " + payroll.getPresentDays() + "\nAbsent Days  : "
					+ payroll.getAbsentDays() + "\nLeave Days   : " + payroll.getLeaveDays() + "\nNet Salary   : Rs. "
					+ String.format("%.2f", netSalary)
					+ "\n\nYour payslip will be shared shortly.\n\nRegards,\nHR Department\n" + Constants.COMPANY_NAME;
			message.setText(body);
			Transport.send(message);
			LogUtil.info("Payroll notification sent to " + emp.getEmail());
		} catch (Exception e) {
			LogUtil.Error("Payroll notification failed: " + e.getMessage());
			throw new RuntimeException("Failed to send payroll notification: " + e.getMessage(), e);
		}
	}

	public void sendOvertimeNotification(Employee emp, Overtime ot) {
		try {
			MimeMessage message = buildBase(emp.getEmail(), "Overtime Recorded - " + ot.getOvertimeMonth());
			String body = "Dear " + emp.getFirstName() + ",\n\nYour overtime for " + ot.getOvertimeMonth()
					+ " has been recorded.\n\n" + "Overtime Hours : " + String.format("%.1f", ot.getOvertimeHours())
					+ " hrs\n" + "Hourly Rate    : Rs. " + String.format("%.2f", ot.getHourlyRate()) + "/hr\n"
					+ "Overtime Pay   : Rs. " + String.format("%.2f", ot.getOvertimePay())
					+ "\n\nThis will be added to your payslip.\n\nRegards,\nHR Department\n" + Constants.COMPANY_NAME;
			message.setText(body);
			Transport.send(message);
			LogUtil.info("Overtime notification sent to " + emp.getEmail());
		} catch (Exception e) {
			LogUtil.Error("Overtime notification failed: " + e.getMessage());
			throw new RuntimeException("Failed to send overtime notification: " + e.getMessage(), e);
		}
	}
}
