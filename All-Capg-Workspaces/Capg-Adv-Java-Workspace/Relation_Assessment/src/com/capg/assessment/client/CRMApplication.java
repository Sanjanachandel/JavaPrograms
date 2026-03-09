package com.capg.assessment.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.capg.assessment.entity.Address;
import com.capg.assessment.service.CustomerService;
import com.capg.assessment.service.LeadService;
import com.capg.assessment.service.OrderService;
import com.capg.assessment.service.ProductService;
import com.capg.assessment.service.ReportService;
import com.capg.assessment.service.TicketService;
import com.capg.assessment.util.JPAUtil;

public class CRMApplication {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		CustomerService customerService = new CustomerService();
        LeadService leadService = new LeadService();
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        TicketService ticketService = new TicketService();
        ReportService reportService = new ReportService();

        
		
		
		while(true) {
			System.out.println("\n----- CRM MENU -----");
            System.out.println("1. Register Customer");
            System.out.println("2. Add Address to Customer");
            System.out.println("3. Create Lead");
            System.out.println("4. Assign Lead to Employee");
            System.out.println("5. Convert Lead to Customer");
            System.out.println("6. Add Product");
            System.out.println("7. Place Order");
            System.out.println("8. Raise Support Ticket");
            System.out.println("9. View Employee Performance");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice) {
            	case 1:
            		System.out.print("Enter name: ");
            		String name = sc.nextLine();
            		System.out.print("Enter email: ");
            		String email = sc.nextLine();
            		System.out.print("Enter phone: ");
            		String phone = sc.nextLine();
            		customerService.registerCustomer(name, email, phone);
            		break;
            		
            	case 2:
            		System.out.print("Enter customer Id: ");
            		Long custId = sc.nextLong();
            		sc.nextLine();
            		Address address = new Address();
            		System.out.print("Enter Street: ");
            		String street = sc.nextLine();
            		address.setStreet(street);
            		System.out.print("Enter City: ");
            		String city = sc.nextLine();
            		address.setCity(city);
            		System.out.print("Enter State: ");
            		String state = sc.nextLine();
            		address.setState(state);
            		System.out.print("Enter Zipcode: ");
            		String zipCode = sc.nextLine();
            		address.setZipCode(zipCode);
            		customerService.addAddressToCustomer(custId, address);
            		break;
            		
            	case 3:
            		System.out.print("Enter Lead name: ");
            		String leadName = sc.nextLine();
            		System.out.print("Enter Source: ");      
            		String source = sc.nextLine();
            		System.out.print("Enter contact info: ");
            		String contact = sc.nextLine();
            		leadService.createLead(leadName, source, contact);
            		break;
            		
            	case 4:
            		System.out.print("Enter LeadId: ");
            		Long leadId = sc.nextLong();
            		System.out.println("Enter Employee Id: ");
            		Long empId = sc.nextLong();
            		leadService.assignLeadToEmployee(leadId, empId);
            		break;
            		
            	case 5:
            		System.out.print("Enter Lead Id to convert: ");
            		Long convertId = sc.nextLong();
            		leadService.convertLeadToCustomer(convertId);
            		break;
            		
            	case 6:
            		System.out.print("Enter Product name: ");
            		String prodName = sc.nextLine();
                    System.out.print("Enter Product Price: ");
                    double price = sc.nextDouble();
                    productService.addProduct(prodName, price);
            		break;
            		
            	case 7:
            		System.out.print("Enter Customer ID: ");
                    Long orderCustId = sc.nextLong();
                    System.out.print("Enter number of products: ");
                    int count = sc.nextInt();
                    List<Long> productIds = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        System.out.print("Enter Product ID: ");
                        productIds.add(sc.nextLong());
                    }
                    orderService.placeOrder(orderCustId, productIds);
            		break;
            		
            	case 8:
            		System.out.print("Enter Order ID: ");
                    Long orderId = sc.nextLong();
                    sc.nextLine();
                    System.out.print("Enter Issue Description: ");
                    String issue = sc.nextLine();
                    ticketService.raiseTicket(orderId, issue);
            		break;
            		
            	case 9:
            		System.out.print("Enter Employee ID: ");
                    Long performanceId = sc.nextLong();
                    reportService.getEmployeePerformance(performanceId);
            		break;
            		
            	case 10:
            		sc.close();
            		JPAUtil.closeFactory();
            		System.out.println("Application Closed");
            		System.exit(0);
            		break;
            		
            	default:
            		System.out.println("Enter a valid choice");
            }
            
		}
	}
}
