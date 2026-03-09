package com.java.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class ProductDaoImpl implements ProductDao {

    private DataSource dataSource;

    public ProductDaoImpl() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    
    @Override
    public void save(Product product) {

        String query = "INSERT INTO product VALUES(?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, product.getPid());
            ps.setString(2, product.getPname());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());

            ps.executeUpdate();

            System.out.println("Product saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public Product get(int pid) {

        Product p = null;
        String query = "SELECT * FROM product WHERE pid=?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, pid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Product();
                p.setPid(rs.getInt(1));
                p.setPname(rs.getString(2));
                p.setPrice(rs.getDouble(3));
                p.setQuantity(rs.getInt(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ===== UPDATE =====
    @Override
    public void update(Product product) {

        String query = "UPDATE product SET pname=?, price=?, quantity=? WHERE pid=?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, product.getPname());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setInt(4, product.getPid());

            ps.executeUpdate();

            System.out.println("Product updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void remove(int pid) {

        String query = "DELETE FROM product WHERE pid=?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, pid);
            ps.executeUpdate();

            System.out.println("Product deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}