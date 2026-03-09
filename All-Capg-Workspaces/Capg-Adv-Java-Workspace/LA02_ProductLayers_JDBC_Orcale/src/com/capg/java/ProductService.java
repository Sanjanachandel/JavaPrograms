package com.capg.java;

public class ProductService {

    ProductDAO dao = new ProductDAO();

    public int addProduct(ProductBean productBean)throws ClassNotFoundException {

       

        if (productBean.getStock() > 100) {
            productBean.setCategory("High Demand");
        } else if (productBean.getStock() < 10) {
            productBean.setCategory("Low Demand");
        } else {
            productBean.setCategory("Normal");
        }

        return dao.addProduct(productBean);
    }
}
