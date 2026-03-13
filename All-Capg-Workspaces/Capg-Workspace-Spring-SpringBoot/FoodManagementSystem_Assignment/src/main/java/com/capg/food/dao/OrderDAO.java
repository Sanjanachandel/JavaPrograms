package com.capg.food.dao;

import com.capg.food.entity.FoodOrder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class OrderDAO {

    @PersistenceContext
    private EntityManager em;

    // Save new order
    public void save(FoodOrder order) {
        em.persist(order);
        System.out.println("Order placed for: " + order.getCustomerName());
    }

    // Get all orders
    public List<FoodOrder> findAll() {
        return em.createQuery("FROM FoodOrder", FoodOrder.class).getResultList();
    }

    // Find by ID
    public FoodOrder findById(Long id) {
        return em.find(FoodOrder.class, id);
    }
}
