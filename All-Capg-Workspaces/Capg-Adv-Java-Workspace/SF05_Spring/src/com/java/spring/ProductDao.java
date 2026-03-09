
//
//public class ProductDao {
//	public void save(Product product);
//	public void update(Product product);
//	public void remove(int pid);
//	public Product get(int pid);
//
//}
package com.java.spring;

public interface ProductDao {

    void save(Product product);

    void update(Product product);

    void remove(int pid);

    Product get(int pid);
}
