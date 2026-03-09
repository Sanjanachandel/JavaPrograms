package com.capg.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ProductDAO {

    int i;

    public int addProduct(ProductBean productBean) {

        try {
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String userName = "scott";
            String password = "tiger";

            Connection conn = DriverManager.getConnection(url, userName, password);

            String query = "insert into product values(?,?,?,?,?)";

            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, productBean.getProductId());
            pstmt.setString(2, productBean.getProductName());
            pstmt.setDouble(3, productBean.getPrice());
            pstmt.setInt(4, productBean.getStock());
            pstmt.setString(5, productBean.getCategory());

            i = pstmt.executeUpdate();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return i;
    }
}
