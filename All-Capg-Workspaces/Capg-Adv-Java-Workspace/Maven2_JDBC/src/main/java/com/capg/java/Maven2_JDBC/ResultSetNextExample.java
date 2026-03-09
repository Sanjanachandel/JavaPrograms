package com.capg.java.Maven2_JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetNextExample {
	public static void main(String[] args) {
		Connection conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		
		String username = "scott";
		String password = "tiger";
		
		try {
			Class.forName(driver);
			
			conn = DriverManager.getConnection(url,username,password);
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("Select * from EmployeeInfo1");
				while(rs.next()) {
					System.out.println("Emp no: "+rs.getInt(1));
					System.out.println("Emp Name: "+rs.getString("ename"));
					System.out.println("Emp no: "+rs.getInt(3));
				}
			}
			catch(SQLException s) {
				System.out.println(s);
			}
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}