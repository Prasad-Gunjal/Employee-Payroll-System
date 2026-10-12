package com.employee.Service;

import java.util.List;

import com.employee.dao.DepartmentDAO;
import com.employee.file.LogUtil;
import com.employee.model.Department;
import com.employee.util.Validator;

public class DepartmentService {

	private final DepartmentDAO departmentDAO = new DepartmentDAO();

	public int addDepartment(String name, String description) {
		Validator.NoBlankInput(name, "Department name");

		int id = departmentDAO.insert(new Department(name, description));
		LogUtil.info("Department Added:" + name + "(id=+" + id + ")");
		return id;
	}

	public boolean updateDepartment(Department dept) {
		Validator.NoBlankInput(dept.getDeptName(), "Department name");
		boolean done = departmentDAO.update(dept);
		LogUtil.info("Department update attempt id=" + dept.getDeptId() + "success" + done);
		return done;
	}

	public boolean deleteDepartment(int departmentId) {
		boolean done = departmentDAO.delete(departmentId);
		LogUtil.info("Department Delete Attempt id:" + departmentId + "success" + done);
		return done;
	}

	public Department getById(int deparmentId) {

		return departmentDAO.findById(deparmentId);
	}

	public List<Department> getAll() {
		return departmentDAO.findAll();

	}

	public List<Department> search(String name) {
		return departmentDAO.searchByName(name);
	}
}
