package com.capg.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capg.springboot.Exceptions.CustomerNotFoundException;
import com.capg.springboot.client.LoginClient;
import com.capg.springboot.dto.Customerdto;
import com.capg.springboot.dto.UserDTO;
import com.capg.springboot.entity.Customer;
import com.capg.springboot.repository.ICustomerRepository;

@Service
@Transactional
public class ICustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository icustomerrepository;

    @Autowired
    private LoginClient loginClient;

    @Override
    public Customer addCustomer(Customerdto custdto) {

        UserDTO user = loginClient.getUser(custdto.getCustomerId());

        if (user == null) {
            throw new RuntimeException("User not found in LoginService");
        }

        Customer customer = new Customer();

        customer.setCustomerId(custdto.getCustomerId());
        customer.setUserId(user.getUserId());

        customer.setEmail(custdto.getEmail());
        customer.setFirstName(custdto.getFirstName());
        customer.setLastName(custdto.getLastName());
        customer.setMobileNumber(custdto.getMobileNumber());
        customer.setLocation(custdto.getLocation()); // ✅ FIXED

        return icustomerrepository.save(customer);
    }

    @Override
    public Customer removeCustomer(Customer cust) {
        icustomerrepository.delete(cust);
        return cust;
    }

    @Override
    public List<Customer> viewAllCustomer() throws CustomerNotFoundException {

        List<Customer> customerList = icustomerrepository.findAll();

        if (customerList.isEmpty())
            throw new CustomerNotFoundException("Customer Not Found");

        return customerList;
    }

    @Override
    public List<Customer> findCustomerByCity(String location) throws CustomerNotFoundException {

        List<Customer> customerList = icustomerrepository.findByLocation(location);

        if (customerList == null || customerList.isEmpty())
            throw new CustomerNotFoundException("Customer Not Found");

        return customerList;
    }

    @Override
    public Customer findByCustomerId(String id) throws CustomerNotFoundException {

        Customer customer = icustomerrepository.findByCustomerId(id);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");

        return customer;
    }
}