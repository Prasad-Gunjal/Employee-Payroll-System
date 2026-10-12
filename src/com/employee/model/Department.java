package com.employee.model;

public class Department {

	int DeptId;
	String DeptName;
	String DeptDesc;

	public Department() {

	}

	public Department(String Name, String Desc) {
		DeptName = Name;
		DeptDesc = Desc;

	}

	public Department(int deptId, String deptName, String deptDesc) {
		super();
		DeptId = deptId;
		DeptName = deptName;
		DeptDesc = deptDesc;
	}

	public int getDeptId() {
		return DeptId;
	}

	public void setDeptId(int deptId) {
		DeptId = deptId;
	}

	public String getDeptName() {
		return DeptName;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
	}

	public String getDeptDesc() {
		return DeptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		DeptDesc = deptDesc;
	}

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Department)) return false;
//        Department that = (Department) o;
//        return DeptId == that.departmentId;
//    }
//
//    @Override
//    public int hashCode() {
//        return Integer.hashCode(departmentId);
//    }

	@Override
	public String toString() {
		return "Department [DeptId=" + DeptId + ", DeptName=" + DeptName + ", DeptDesc=" + DeptDesc + "]";
	}

}
