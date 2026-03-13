package com.capg.food.service;

import com.capg.food.dao.FoodItemDAO;
import com.capg.food.dao.OrderDAO;
import com.capg.food.entity.FoodItem;
import com.capg.food.entity.FoodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private FoodItemDAO foodItemDAO;

    // Place a new order
    public void placeOrder(List<Long> foodItemIds, String customerName) {
        List<FoodItem> items = new ArrayList<>();

        for (Long id : foodItemIds) {
            FoodItem item = foodItemDAO.findById(id);
            if (item != null) {
                items.add(item);
            } else {
                System.out.println("FoodItem not found with ID: " + id + " - skipping.");
            }
        }

        if (items.isEmpty()) {
            System.out.println("No valid items found. Order not placed.");
            return;
        }

        // Calculate total
        double total = items.stream().mapToDouble(FoodItem::getPrice).sum();

        FoodOrder order = new FoodOrder();
        order.setCustomerName(customerName);
        order.setOrderDate(new Date());
        order.setFoodItems(items);
        order.setTotalAmount(total);

        orderDAO.save(order);
        System.out.println("Order placed! Total Bill = Rs." + total);
    }

    // Calculate total for existing order
    public void calculateTotal(Long orderId) {
        FoodOrder order = orderDAO.findById(orderId);
        if (order == null) {
            System.out.println("Order not found with ID: " + orderId);
            return;
        }
        double total = order.getFoodItems().stream().mapToDouble(FoodItem::getPrice).sum();
        System.out.println("Order ID: " + orderId + " | Customer: " + order.getCustomerName());
        System.out.println("Items Ordered:");
        order.getFoodItems().forEach(i -> System.out.println("  - " + i.getItemName() + " : Rs." + i.getPrice()));
        System.out.println("Total Bill = Rs." + total);
    }

    // View all orders
    public List<FoodOrder> getAllOrders() {
        return orderDAO.findAll();
    }
}
