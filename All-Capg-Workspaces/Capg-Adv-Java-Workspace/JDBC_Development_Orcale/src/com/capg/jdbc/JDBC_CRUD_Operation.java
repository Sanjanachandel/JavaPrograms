package com.capg.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBC_CRUD_Operation {

	public static void main(String[] args) {
		
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String driver = "oracle.jdbc.driver.OracleDriver";
		String userName = "scott"; 
		String password = "tiger";	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Scanner sc = new Scanner(System.in);
		
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,userName,password);
			
			int choice=0;
			boolean flag = true;
			do {
				 System.out.println("\n--- Employee CRUD Menu ---");
	             System.out.println("1. Insert Employee");
	             System.out.println("2. Show All Employees");
	             System.out.println("3. Delete Employee by ID");
	             System.out.println("4. Exit");
	             System.out.print("Enter your choice: ");
	             
	             choice = sc.nextInt();
	             
	             switch(choice) {
	             	case 1:
	             		String query = "INSERT INTO EmployeeInfo2 Values(?,?,?)";
	             		pstmt = conn.prepareStatement(query);
	             		
	             		System.out.print("Enter Emp No: ");
	             		int empno = sc.nextInt();
	             		
	             		System.out.print("Enter Employee Name: ");
	             		String name = sc.next();
	             		
	             		System.out.print("Enter Employee Salary: ");
	             		double sal = sc.nextDouble();
	             		
	             		pstmt.setInt(1, empno);
	             		pstmt.setString(2,name);
	             		pstmt.setDouble(3, sal);
	             		
	             		int insertCnt = pstmt.executeUpdate();
	             		System.out.println(insertCnt + " record inserted.");
	             		break;
	             		
	             	case 2:
	             		String selectQuery = "SELECT * FROM EmployeeInfo2";
	             		pstmt = conn.prepareStatement(selectQuery);
	             		
	             		rs = pstmt.executeQuery();
	             		
	             		System.out.println("\nEmpNo\tEmpName\tSalary");
	             		while(rs.next()) {
	             			System.out.println(
	             					rs.getInt(1)+"\t"+
	             					rs.getString(2)+"\t"+
	             					rs.getDouble(3)
	             			);
	             		}
	             		break;
	             	
	             	case 3:
	             		String deleteQuery = "DELETE FROM EmployeeInfo2 WHERE EmpNo = ?";
	             		pstmt = conn.prepareStatement(deleteQuery);
	             		System.out.print("Enter EmpNo To Delete: ");
	             		int delId = sc.nextInt();
	             		
	             		pstmt.setInt(1, delId);
	             		int delCnt = pstmt.executeUpdate();
	             		
	             		if(delCnt > 0) {
	             			System.out.println("Record deleted successfully");
	             		}
	             		else {
	             			System.out.println("Employee not found");
	             		}
	             		
	             		break;
	             		
	             	case 4:
	             		System.out.println("Exiting...");
	             		flag = false;
	             		break;
	             	default:
	             		System.out.println("Not a valid choice");
	             		break;
	             }
			}
			while(flag);
			conn.close();
			rs.close();
			pstmt.close();
			sc.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
