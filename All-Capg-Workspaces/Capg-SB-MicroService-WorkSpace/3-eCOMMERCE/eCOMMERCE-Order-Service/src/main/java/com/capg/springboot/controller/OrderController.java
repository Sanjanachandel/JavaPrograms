package com.capg.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.capg.springboot.model.Product;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private InventoryClient inventoryClient;

    @GetMapping("/place/{productId}")
    public String placeOrder(@PathVariable Long productId) {

        Product product = productClient.getProduct(productId);

        boolean inStock = inventoryClient.isStockAvailable(productId);

        if (!inStock) {
            return "Product is out of stock";
        }

        return "Order placed successfully for product: "
                + product.getName() + " price: " + product.getPrice();
    }
}