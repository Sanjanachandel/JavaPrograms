package com.capg.ram.web;

import java.sql.*;
import org.springframework.stereotype.Repository;

/*
 @Repository
 - Indicates DAO layer
 - Handles database logic
*/
@Repository
public class LoginDAO {

    public User validateUser(String user, String pass) {
    System.out.println("DAO USER = " + user);
    System.out.println("DAO PASS = " + pass);
        User dbUser = null;

        try {
            // Load Oracle Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Create DB connection
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE",
                "scott",
                "tiger"
            );
                   System.out.println("DB CONNECTED");
            PreparedStatement ps = con.prepareStatement(
           "SELECT username, password FROM users WHERE username=? AND password=?");

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dbUser = new User();
                dbUser.setUsername(rs.getString("username"));
                dbUser.setPassword(rs.getString("password"));
                System.out.println("LOGIN SUCCESS");
            } else {
                System.out.println("NO RECORD FOUND");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbUser;
    }
}