package com.capg.Springboot.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
	
	List<Product> products = new ArrayList<>(Arrays.asList(
			new Product("P1","Watch","Accessories"),
			new Product("P2","Laptop","Electronics"),
			new Product("P3", "Shirt","Clothes")
		));

	public List<Product> getAllProducts() {
		
		return products;
	}

	public Product getProduct(String id) {
		// TODO Auto-generated method stub
		return (Product) products.stream().filter(t -> ((Product) t).getId().equals(id)).findFirst().get();

	}

	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		products.add(product);
		
	}

	public void deleteProduct(String id) {
		// TODO Auto-generated method stub
		products.removeIf(t -> ((Product) t).getId().equals(id));
	}

	

}