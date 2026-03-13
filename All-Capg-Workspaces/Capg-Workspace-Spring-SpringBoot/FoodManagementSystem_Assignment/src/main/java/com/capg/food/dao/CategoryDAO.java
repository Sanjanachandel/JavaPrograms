package com.capg.food.dao;

import com.capg.food.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/*
 @Repository  - Marks this as DAO layer
 @Transactional - Handles DB transactions automatically
*/
@Repository
@Transactional
public class CategoryDAO {

    @PersistenceContext
    private EntityManager em;

    // Save new category to DB
    public void save(Category category) {
        em.persist(category);
        System.out.println("Category saved: " + category.getCategoryName());
    }

    // Get all categories
    public List<Category> findAll() {
        return em.createQuery("FROM Category", Category.class).getResultList();
    }

    // Find by ID
    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    // Delete category
    public void delete(Long id) {
        Category cat = em.find(Category.class, id);
        if (cat != null) {
            em.remove(cat);
            System.out.println("Category deleted: " + cat.getCategoryName());
        } else {
            System.out.println("Category not found with ID: " + id);
        }
    }
}
