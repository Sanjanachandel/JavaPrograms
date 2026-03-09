package com.ram.corejava.layers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDAO {
	int i;            // 100  myjava 100  C
	public int addBook(BookBean bookBean) throws ClassNotFoundException {
		
		System.out.println("Book DAO Layer");
		System.out.println("BookID :"+bookBean.getBookId());
		System.out.println("BookTitle:"+bookBean.getTitle());
		System.out.println("Book Price :"+bookBean.getPrice());
		try {
		
		
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String driver = "oracle.jdbc.driver.OracleDriver";
		String userName = "scott"; 
		String password = "tiger";	 
		  Class.forName(driver);
			conn = DriverManager.getConnection(url,userName,password);
	        String query="insert into book values(?,?,?,?)";
	        
	       PreparedStatement pstmt=conn.prepareStatement(query);
	        pstmt.setInt(1, bookBean.getBookId()); //100
	        pstmt.setString(2,bookBean.getTitle()); //java
	        pstmt.setDouble(3,bookBean.getPrice()); //250
	        pstmt.setString(4, bookBean.getGrade()); //c
	        
	         i= pstmt.executeUpdate();
	        
	      }
	      catch(SQLException e) {
	    	  System.out.println(e);
	      }
		return i;
	}
		
		
		
		
	
}