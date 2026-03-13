package com.capg.food.dao;

import com.capg.food.entity.FoodItem;
import com.capg.food.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class FoodItemDAO {

    @PersistenceContext
    private EntityManager em;

    // Save new food item
    public void save(FoodItem item) {
        em.persist(item);
        System.out.println("FoodItem saved: " + item.getItemName());
    }

    // Get items by category ID
    public List<FoodItem> findByCategory(Long categoryId) {
        return em.createQuery(
            "FROM FoodItem f WHERE f.category.id = :catId", FoodItem.class)
            .setParameter("catId", categoryId)
            .getResultList();
    }

    // Find by ID
    public FoodItem findById(Long id) {
        return em.find(FoodItem.class, id);
    }

    // Delete food item
    public void delete(Long id) {
        FoodItem item = em.find(FoodItem.class, id);
        if (item != null) {
            // Remove from parent category list to avoid stale cache
            Category cat = item.getCategory();
            if (cat != null && cat.getItems() != null) {
                cat.getItems().remove(item);
            }
            em.remove(item);
            em.flush(); // force immediate DB delete
            System.out.println("FoodItem deleted: " + item.getItemName());
        } else {
            System.out.println("FoodItem not found with ID: " + id);
        }
    }
}