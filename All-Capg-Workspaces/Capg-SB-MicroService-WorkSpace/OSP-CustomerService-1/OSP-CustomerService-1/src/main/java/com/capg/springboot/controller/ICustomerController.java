package com.capg.springboot.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.springboot.Exceptions.CustomerNotFoundException;
import com.capg.springboot.dto.Customerdto;
import com.capg.springboot.entity.Customer;
import com.capg.springboot.service.ICustomerService;

/*/*****************************************************************************
 *ICart Controller Class 
 *
 * Created By: 
 * Date: 
 *******************************************************************************/

@RestController
@RequestMapping("/customers")
public class ICustomerController {
  
	@Autowired
    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customerdto dto) {
        return new ResponseEntity<>(customerService.addCustomer(dto), HttpStatus.CREATED);
    }

//*********************************************************************************************************************	
	
	/* Method: viewCustomerByLocation
	 * Description: It allows to view Customer by Location.
	 * @PathVariable: It is used to handle template variables in the request URL
	 * @GetMapping:It is used to handle the HTTP GET requests matched with given URL Expression.
     */
	
	@GetMapping("/viewCust/{location}")
	public ResponseEntity<List<Customer>> ViewAllCustomers(@PathVariable String location) throws CustomerNotFoundException{
		List<Customer> custImpl=customerService.findCustomerByCity(location);
		return new ResponseEntity(custImpl,HttpStatus.OK);
		}

//*********************************************************************************************************************	

	/* Method: viewCustomer
	 * Description: It allows to view the Customer.
	 * @PathVariable: It is used to handle template variables in the request URL
	 * @GetMapping:It is used to handle the HTTP GET requests matched with given URL Expression.
	 * @RequestBody: It is used to bind HTTP request body with a domain object in method parameter or return type
     */	
	//http://localhost:9090/customerController/viewAllCustomer
	
	@GetMapping("/viewAllCustomer") 
	public ResponseEntity<List<Customer>> viewAllCustomer() throws CustomerNotFoundException
	{ 
	List<Customer> custImpl=customerService.viewAllCustomer();
	return new  ResponseEntity(custImpl,HttpStatus.OK);
	}

//*********************************************************************************************************************	
	
	/* Method: removeCustomer
	 * Description: It allows to remove the Customer.
	 * @RequestBody: It is used to bind HTTP request body with a domain object in method parameter or return type
	 * @DeleteMapping: It is used to handle the HTTP DELETE requests matched with given URL expression.
     */
	
	@DeleteMapping("/removeCust") 
	public ResponseEntity<Customer> removeCustomer(@RequestBody Customer customer){
       Customer custImpl=customerService.removeCustomer(customer);
       return new ResponseEntity(custImpl,HttpStatus.OK); 
       }

//*********************************************************************************************************************	
	
		/* Method: viewAllCustomer
		 * Description: It allows to view All the Customers.
		 * @RequestBody: It is used to bind HTTP request body with a domain object in method parameter or return type
		 * @GetMapping:It is used to handle the HTTP GET requests matched with given URL Expression.
	     */
		
	@GetMapping("/{id}")
	public ResponseEntity<Customer> findByCustomerId(@PathVariable String id)
	        throws CustomerNotFoundException {

	    Customer custImpl = customerService.findByCustomerId(id);
	    return new ResponseEntity<>(custImpl, HttpStatus.OK);
	}
}


