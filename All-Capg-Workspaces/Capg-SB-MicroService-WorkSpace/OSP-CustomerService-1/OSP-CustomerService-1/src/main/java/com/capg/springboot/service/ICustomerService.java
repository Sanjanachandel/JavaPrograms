package com.capg.springboot.service;

import java.util.List;

import com.capg.springboot.Exceptions.CustomerNotFoundException;
import com.capg.springboot.dto.Customerdto;
import com.capg.springboot.entity.Customer;

public interface ICustomerService {

    Customer findByCustomerId(String id) throws CustomerNotFoundException;

    Customer removeCustomer(Customer cust);

    List<Customer> viewAllCustomer() throws CustomerNotFoundException;

    Customer addCustomer(Customerdto custdto);

    List<Customer> findCustomerByCity(String location) throws CustomerNotFoundException;
}