package com.capg.spring.Maven3_Spring;

public class ProductServiceImpl implements ProductService{
		String product;
public ProductServiceImpl() {
			
		}
		public ProductServiceImpl(String product) {
			
			this.product = product;
		}
		public void setProduct(String product)
		{
			this.product=product;
		}
		
		public void allProducts()
		{
			System.out.println("Product name is : "+product);
		}

}

