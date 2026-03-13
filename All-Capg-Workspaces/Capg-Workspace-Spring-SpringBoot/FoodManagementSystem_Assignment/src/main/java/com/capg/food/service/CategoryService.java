package com.capg.food.service;

import com.capg.food.dao.CategoryDAO;
import com.capg.food.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 @Service - Marks this as Business Logic Layer
 @Autowired - Spring injects CategoryDAO automatically
*/
@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    // Add a new category
    public void addCategory(String name, String description) {
        Category cat = new Category();
        cat.setCategoryName(name);
        cat.setDescription(description);
        categoryDAO.save(cat);
    }

    // View all categories
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    // Delete category by ID
    public void deleteCategory(Long id) {
        categoryDAO.delete(id);
    }
}
