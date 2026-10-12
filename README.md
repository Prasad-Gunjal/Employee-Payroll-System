# 💼 Employee Payroll System

A **Java-based console application** for managing employee records, attendance, payroll processing, and report generation. Built with a clean layered architecture (Model → DAO → Service → Menu), MySQL for persistence, and supporting PDF payslip generation & email notifications.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [How to Run](#how-to-run)
- [Modules Breakdown](#modules-breakdown)
- [Salary Computation Logic](#salary-computation-logic)
- [Email Notifications](#email-notifications)
- [PDF Payslip Generation](#pdf-payslip-generation)
- [Data Storage](#data-storage)
- [Author](#author)

---

## 📌 Overview

The **Employee Payroll System** is a full-featured command-line Java application that enables HR departments to:

- Manage employee records (add, update, search, delete)
- Track daily attendance and leaves
- Process individual or bulk monthly payroll
- Record and calculate overtime pay
- Generate PDF payslips and email them to employees
- Generate department-wise and monthly payroll reports
- Maintain application logs and data backups

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 Login System | Secure login with max attempt limit & password hashing |
| 👤 Employee Management | Add, update, search, and deactivate employee profiles |
| 🏢 Department Management | Create and manage departments |
| 📅 Attendance Tracking | Mark Present / Absent / Leave per employee per day |
| 💰 Payroll Processing | Individual & bulk payroll with auto salary breakdown |
| ⏱️ Overtime Management | Record overtime hours; auto-calculates overtime pay |
| 📄 PDF Payslip | Generates professional A4 PDF payslips using iText |
| 📧 Email Notifications | Sends payslip, welcome, payroll & overtime emails via Gmail SMTP |
| 📊 Reports | Monthly payroll summaries and department-wise reports |
| 📁 Logging | Application-level logging to file |
| 💾 Backup | Data backup and restore support |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (JDK 8+) |
| Database | MySQL 8.x |
| JDBC Driver | MySQL Connector/J |
| PDF Generation | iText PDF library |
| Email | JavaMail API (javax.mail) via Gmail SMTP |
| IDE | Eclipse IDE |
| Build | Eclipse Java Project (classpath-based) |

---

## 📁 Project Structure

```
EmployeePayrollSystem/
├── src/
│   └── com/
│       └── employee/
│           ├── main/           # Entry point (mainMethod.java)
│           ├── controller/     # MainController - orchestrates the app
│           ├── model/          # POJOs: Employee, Salary, Payroll, Attendance, Overtime, Department, User
│           ├── dao/            # Database Access Objects (JDBC)
│           ├── Service/        # Business logic layer
│           ├── menu/           # Console UI menus
│           ├── pdf/            # PDF payslip generation (iText)
│           ├── email/          # Email service (JavaMail)
│           ├── report/         # Report generation
│           ├── file/           # LogUtil - logging utility
│           ├── exception/      # Custom exceptions
│           └── util/           # Constants, DBConnection, Validators, DateUtil, etc.
├── data/
│   ├── attendance/             # Attendance data files
│   ├── employees/              # Employee data files
│   ├── payroll/                # Payroll data files
│   ├── reports/                # Generated PDF payslips
│   ├── logs/                   # Application logs (application.log)
│   └── backup/                 # Serialized backup files
├── .classpath
├── .project
└── README.md
```

---

## 🏗️ Architecture

```
[ Console Input ]
       │
       ▼
[ Menu Layer ]  ←→  [ Service Layer ]  ←→  [ DAO Layer ]  ←→  [ MySQL DB ]
                           │
                    [ Model / POJO ]
                           │
              ┌────────────┼────────────┐
              ▼            ▼            ▼
         [ PDF Gen ]  [ Email Svc ]  [ Logger ]
```

The application follows a classic **layered architecture**:
- **Model** – Plain Java objects (POJOs) representing domain entities
- **DAO** – Handles all SQL queries via JDBC
- **Service** – Contains business rules and delegates to DAO
- **Menu** – Handles user input/output via console
- **Controller** – Bootstraps and connects all layers

---

## 🗄️ Database Setup

1. Install and start **MySQL 8.x** locally.
2. Create the database:
   ```sql
   CREATE DATABASE employee_payroll_db;
   ```
3. Import the schema (create tables for `employees`, `departments`, `attendance`, `salary`, `payroll`, `overtime`, `users`).

> **Note:** The application connects to `jdbc:mysql://localhost:3306/employee_payroll_db` with the credentials configured in `Constants.java`.

---

## ⚙️ Configuration

Open `src/com/employee/util/Constants.java` and update the following before running:

```java
// Database
public static final String DB_URL      = "jdbc:mysql://localhost:3306/employee_payroll_db?useSSL=false&serverTimezone=UTC";
public static final String DB_USER     = "root";
public static final String DB_PASSWORD = "your_mysql_password";

// Email (Gmail SMTP)
public static final String EMAIL_FROM     = "your_email@gmail.com";
public static final String EMAIL_PASSWORD = "your_app_password";  // Use Gmail App Password

// Company name shown on payslips
public static final String COMPANY_NAME = "Your Company Name";
```

> 💡 **Gmail App Password**: Go to Google Account → Security → 2-Step Verification → App Passwords. Generate a 16-character app password and use it instead of your regular Gmail password.

---

## ▶️ How to Run

### Using Eclipse IDE

1. Clone or download this repository.
2. Open **Eclipse IDE**.
3. Go to `File → Import → Existing Projects into Workspace`.
4. Select the project root folder.
5. Update `Constants.java` with your DB credentials and email settings.
6. Ensure the following JAR libraries are added to the build path:
   - `mysql-connector-java-x.x.x.jar`
   - `itextpdf-x.x.x.jar`
   - `javax.mail.jar` (or `jakarta.mail.jar`)
7. Right-click `mainMethod.java` → **Run As → Java Application**.

### From Command Line

```bash
# Compile (from project root)
javac -cp "lib/*" -d bin src/com/employee/**/*.java

# Run
java -cp "bin;lib/*" com.employee.main.mainMethod
```

---

## 📦 Modules Breakdown

### 🔐 Login Module
- Console-based login with username & password
- Maximum **3 login attempts** enforced
- Password stored with hashing via `PasswordUtil`

### 👤 Employee Module
- Add new employee (with welcome email sent automatically)
- View all employees / search by ID or name
- Update employee details
- Change employee status (ACTIVE / INACTIVE)

### 🏢 Department Module
- Add and list departments
- Associate employees to departments

### 📅 Attendance Module
- Mark attendance: **PRESENT**, **ABSENT**, **LEAVE**
- View attendance by employee and month
- Count present/absent/leave days (used in payroll)

### 💰 Payroll Module
- Process payroll for a single employee for a given month
- **Bulk payroll** processing for all active employees at once
- Skips already-processed months and inactive employees
- View payroll history and salary breakdown

### ⏱️ Overtime Module
- Record overtime hours for an employee and month
- Overtime pay auto-calculated based on hourly rate
- Overtime pay is added to net salary during payroll

### 📄 PDF Payslip
- Generates a professional **A4 PDF payslip** using **iText**
- Includes: employee info, earnings table, deductions table, gross/net salary, authorized signature field
- Saved to `data/reports/`

### 📧 Email Service
- **Welcome Email** – sent when a new employee is added
- **Payroll Notification Email** – sent after payroll is processed
- **Payslip Email** – sends PDF payslip as an email attachment
- **Overtime Notification** – notifies employee when overtime is recorded

### 📊 Report Module
- Monthly payroll summary report
- Department-wise employee report

---

## 💵 Salary Computation Logic

| Component | Calculation |
|---|---|
| **HRA** | 20% of Basic Salary |
| **DA** | 10% of Basic Salary |
| **Allowances** | User-defined + Overtime Pay |
| **PF (Deduction)** | 12% of Basic Salary |
| **Professional Tax** | Fixed ₹200 |
| **Other Deductions** | User-defined |
| **Gross Salary** | Basic + HRA + DA + Allowances |
| **Total Deduction** | PF + Professional Tax + Other Deductions |
| **Net Salary** | Gross Salary − Total Deduction |

---

## 📧 Email Notifications

The system uses **Gmail SMTP (SSL on port 465)** to send emails. Emails are sent for:

- ✅ New employee onboarding (Welcome email)
- ✅ Monthly payroll processed (notification with attendance summary)
- ✅ PDF payslip delivery (email with attachment)
- ✅ Overtime recorded (hours, rate, and pay details)

---

## 📄 PDF Payslip Generation

PDFs are generated using the **iText library** and saved to `data/reports/`. Each payslip includes:

- Company name header
- Payroll month
- Employee info (ID, Name, Department, Designation)
- Earnings breakdown (Basic, HRA, DA, Allowances)
- Deductions breakdown (PF, Professional Tax, Other)
- Gross Salary, Total Deduction, **Net Salary**
- Generation date and authorized signature line

---

## 💾 Data Storage

| Directory | Contents |
|---|---|
| `data/logs/application.log` | All application events (INFO / ERROR) |
| `data/reports/` | Generated PDF payslips |
| `data/backup/backup.ser` | Serialized backup data |
| `data/attendance/` | Attendance records |
| `data/employees/` | Employee data files |
| `data/payroll/` | Payroll records |

---

## 👨‍💻 Author

**Prasad Gunjal**  
Java Developer | Eclipse IDE | MySQL | iText | JavaMail

---

## 📜 License

This project is open-source and available under the [MIT License](LICENSE).

---

> ⭐ If you found this project helpful, please consider giving it a **star** on GitHub!
