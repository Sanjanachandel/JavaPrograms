package com.capg.Springboot.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("/mycontroller")
	public String display() {
		return "My Controller";
	}
	
	@RequestMapping("/products")
	public List<Product> getAllPoducts(){
		return productService.getAllProducts();
	}
	
	@RequestMapping("/products/{id}")
	public Product getProduct(@PathVariable String id) {
		return productService.getProduct(id);
	}
	    
	@RequestMapping(method=RequestMethod.POST,value="/products")
	public void addProduct(@RequestBody Product product) {
		productService.addProduct(product);
	}
	
	@RequestMapping(method=RequestMethod.DELETE,value="/products{id}")
	public void deleteTopic(@RequestBody Product product,String id) {
		productService.deleteProduct(id);
	}
}