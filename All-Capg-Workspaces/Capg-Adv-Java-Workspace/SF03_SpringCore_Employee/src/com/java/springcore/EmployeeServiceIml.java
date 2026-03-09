package com.java.springcore;

public class EmployeeServiceIml implements EmployeeService {
		String name;
		String designation;
		int eid;
		public EmployeeServiceIml()
		{
			
		}
		public EmployeeServiceIml(String name, String designation, int eid) {
			
			this.name = name;
			this.designation = designation;
			this.eid = eid;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setDesignation(String designation) {
			this.designation = designation;
		}
		public void setEid(int eid) {
			this.eid = eid;
		}
		public void allEmployee()
		{
			System.out.println("-----------");
			System.out.println("Employee Details");
			System.out.println("Employee Name : "+name);
			System.out.println("Employee Name : "+designation);
			System.out.println("Employee Name : "+eid);
		}
		
}
