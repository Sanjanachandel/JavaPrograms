package Collection;

import java.util.ArrayList;

public class ShopingCart {
 public static void main(String[] args) {

        ShoppingCart cart = new ShoppingCart();

        cart.addProduct(new Product(1, "Milk"));
        cart.addProduct(new Product(2, "Tea"));
        cart.addProduct(new Product(3, "Biscuit"));
        cart.addProduct(new Product(4, "Coffee"));
        cart.addProduct(new Product(5, "Chocolate"));

        cart.displayCart();

        cart.removeProduct(new Product(1, "Milk"));
        cart.displayCart();

        cart.removeProduct(new Product(3, "Honey"));
        cart.displayCart();
    }


    static class Product {
        int productID;
        String productName;

        Product(int productID, String productName) {
            this.productID = productID;
            this.productName = productName;
        }

        @Override
        public String toString() {
            return productID + " " + productName;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Product p = (Product) obj;
            return this.productID == p.productID &&
                   this.productName.equals(p.productName);
        }
    }

    static class ShoppingCart {
        ArrayList<Product> items = new ArrayList<>();

        void addProduct(Product p) {
            items.add(p);
        }

        void removeProduct(Product p) {
            if (items.remove(p)) {
                System.out.println("Item removed successfully");
            } else {
                System.out.println("Item not removed from the cart");
            }
        }

        void displayCart() {
            System.out.println(items);
        }
    }
}

   
