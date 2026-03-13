package com.capg.food.main;

import com.capg.food.entity.Category;
import com.capg.food.entity.FoodItem;
import com.capg.food.entity.FoodOrder;
import com.capg.food.service.CategoryService;
import com.capg.food.service.FoodService;
import com.capg.food.service.OrderService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 @Configuration - Marks this as Spring Config class
 @ComponentScan - Scans all classes in com.capg.food package
 @ImportResource - Loads applicationContext.xml for JPA/DataSource config
*/
@Configuration
@ComponentScan(basePackages = "com.capg.food")
@ImportResource("classpath:applicationContext.xml")
// applicationContext.xml is in src/main/resources — Maven puts it on classpath automatically
public class FoodManagementApplication {

    public static void main(String[] args) {

        // 1. Initialize Spring Context
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(FoodManagementApplication.class);

        // 2. Get Service beans from Spring
        CategoryService categoryService = context.getBean(CategoryService.class);
        FoodService foodService = context.getBean(FoodService.class);
        OrderService orderService = context.getBean(OrderService.class);

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        System.out.println("==============================");
        System.out.println("  FOOD MANAGEMENT SYSTEM");
        System.out.println("==============================");

        // 3. Menu loop
        boolean flag = true;
        while (flag) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add Category");
            System.out.println("2. View All Categories");
            System.out.println("3. Delete Category");
            System.out.println("4. Add Food Item");
            System.out.println("5. View Food Items by Category");
            System.out.println("6. Delete Food Item");
            System.out.println("7. Place Order");
            System.out.println("8. Calculate Total Bill");
            System.out.println("9. View All Orders");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); 
            switch (choice) {

                case 1:
                    System.out.print("Enter Category Name: ");
                    String catName = sc.nextLine();
                    System.out.print("Enter Description: ");
                    String catDesc = sc.nextLine();
                    categoryService.addCategory(catName, catDesc);
                    System.out.println("Category added successfully!");
                    break;

                case 2:
                    List<Category> categories = categoryService.getAllCategories();
                    if (categories.isEmpty()) {
                        System.out.println("No categories found.");
                    } else {
                        System.out.println("\n--- All Categories ---");
                        categories.forEach(System.out::println);
                    }
                    break;

                case 3:
                    System.out.print("Enter Category ID to delete: ");
                    Long delCatId = sc.nextLong();
                    categoryService.deleteCategory(delCatId);
                    break;

                case 4:
                    System.out.print("Enter Category ID: ");
                    Long catId = sc.nextLong();
                    sc.nextLine();
                    System.out.print("Enter Food Item Name: ");
                    String itemName = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    foodService.addFoodItem(catId, itemName, price);
                    System.out.println("Food item added successfully!");
                    break;

                case 5:
                    System.out.print("Enter Category ID to view items: ");
                    Long viewCatId = sc.nextLong();
                    List<FoodItem> items = foodService.getItemsByCategory(viewCatId);
                    if (items.isEmpty()) {
                        System.out.println("No items found for this category.");
                    } else {
                        System.out.println("\n--- Food Items ---");
                        items.forEach(System.out::println);
                    }
                    break;

                case 6:
                    System.out.print("Enter Food Item ID to delete: ");
                    Long delItemId = sc.nextLong();
                    foodService.removeFoodItem(delItemId);
                    break;

                case 7:
                    System.out.print("Enter Customer Name: ");
                    String custName = sc.nextLine();
                    System.out.print("Enter Food Item IDs (comma separated, e.g. 1,2,3): ");
                    String[] idParts = sc.nextLine().split(",");
                    List<Long> itemIds = new ArrayList<>();
                    for (String part : idParts) {
                        itemIds.add(Long.parseLong(part.trim()));
                    }
                    orderService.placeOrder(itemIds, custName);
                    break;

                case 8:
                    System.out.print("Enter Order ID: ");
                    Long orderId = sc.nextLong();
                    orderService.calculateTotal(orderId);
                    break;

                case 9:
                    List<FoodOrder> orders = orderService.getAllOrders();
                    if (orders.isEmpty()) {
                        System.out.println("No orders found.");
                    } else {
                        System.out.println("\n--- All Orders ---");
                        orders.forEach(System.out::println);
                    }
                    break;

                case 10:
                    flag = false;
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
        context.close();
        System.out.println("Spring context closed.");
    }
}
