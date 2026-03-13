package com.capg.food.service;

import com.capg.food.dao.CategoryDAO;
import com.capg.food.dao.FoodItemDAO;
import com.capg.food.entity.Category;
import com.capg.food.entity.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodItemDAO foodItemDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    // Add food item under a category
    public void addFoodItem(Long categoryId, String name, double price) {
        Category cat = categoryDAO.findById(categoryId);
        if (cat == null) {
            System.out.println("Category not found with ID: " + categoryId);
            return;
        }
        FoodItem item = new FoodItem();
        item.setItemName(name);
        item.setPrice(price);
        item.setCategory(cat);
        foodItemDAO.save(item);
    }

    // View items by category
    public List<FoodItem> getItemsByCategory(Long categoryId) {
        return foodItemDAO.findByCategory(categoryId);
    }

    // Delete food item
    public void removeFoodItem(Long itemId) {
        foodItemDAO.delete(itemId);
    }
}
