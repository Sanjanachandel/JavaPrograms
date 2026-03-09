package com.capg.ram.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 @Service
 - Indicates Business Logic Layer
*/
@Service
public class LoginService {

    /*
     @Autowired
     - Injects DAO layer
    */
    @Autowired
    private LoginDAO loginDAO;

    // Business method
    public User checkLogin(String user, String pass) {
        return loginDAO.validateUser(user, pass);
    }
}