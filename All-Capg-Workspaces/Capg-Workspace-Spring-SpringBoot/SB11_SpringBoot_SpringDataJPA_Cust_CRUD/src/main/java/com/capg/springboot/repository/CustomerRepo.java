package com.capg.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capg.springboot.entity.Customer;

@Repository
public interface CustomerRepo extends CrudRepository<Customer,Integer>{

}
