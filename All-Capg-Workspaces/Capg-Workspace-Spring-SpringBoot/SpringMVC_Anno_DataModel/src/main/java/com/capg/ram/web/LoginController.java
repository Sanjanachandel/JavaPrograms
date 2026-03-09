package com.capg.ram.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/*
 @Controller
 - Marks this class as a Controller
 - Handled by Spring MVC DispatcherServlet
*/
@Controller
public class LoginController {

    /*
     @Autowired
     - Injects LoginService automatically
     - Dependency Injection (DI)
    */
    @Autowired
    private LoginService loginService;

    /*
     @RequestMapping("/login.spring")
     - Maps URL to this method
     - Called when form is submitted
    */
    @RequestMapping("/login.spring")
    public ModelAndView loginValid(@RequestParam String user,@RequestParam String pass) {

        ModelAndView mav = new ModelAndView();
        User dbUser = loginService.checkLogin(user, pass);

        if (dbUser != null) {
            mav.addObject("username", dbUser.getUsername());
            mav.addObject("password", dbUser.getPassword());
            mav.setViewName("/Success.jsp");
        } else {
            mav.setViewName("/Failed.jsp");
        }
        return mav;
    }
}