package com.capg.java;

import java.util.Scanner;

public class ProductController {

    public static void main(String[] args) throws ClassNotFoundException {

        Scanner sc = new Scanner(System.in);

        ProductBean productBean = new ProductBean();

        System.out.println("Enter Product Id:");
        productBean.setProductId(sc.nextInt());

        System.out.println("Enter Product Name:");
        productBean.setProductName(sc.next());

        System.out.println("Enter Price:");
        productBean.setPrice(sc.nextDouble());

        System.out.println("Enter Stock:");
        productBean.setStock(sc.nextInt());


        ProductService productService = new ProductService();

        int result = productService.addProduct(productBean);

        if (result > 0) {
            System.out.println("Product Inserted Successfully "+result);
        } else {
            System.out.println("Product Not Inserted");
        }

        sc.close();
    }
}
