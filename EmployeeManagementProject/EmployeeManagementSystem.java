import java.util.Scanner;

public class EmployeeManagementSystem {
    static Scanner sc = new Scanner(System.in);
    static Employee employees[] = new Employee[20];
    static int cnt = 0;

    public static void main(String[] args){
        int choice;

        do{
            System.out.println("\nWelcome to Employee Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee by ID");
            System.out.println("6. Count Employee by Department");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            choice = sc.nextInt();

            switch(choice){
                case 1:
                    addEmployee();
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    searchEmployee();
                    break;
                case 6:
                    countByDepartment();
                    break;
                case 7:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
        while(choice != 7);
    }

    static void addEmployee(){
        if(cnt == employees.length){
            System.out.println("Employee limit reached");
            return;
        }

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for(int i=0;i<cnt;i++){
            if(employees[i].equals(new Employee(id,"","",0))){
                System.out.println("Employee with ID "+ id + " already exists! Cannot add duplicate.");
                return;
            }
            
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Department: ");
        String department = sc.nextLine();

        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        employees[cnt++] = new Employee(id,name,department,salary);

        System.out.println("Employee added successfully");
        displayEmployees();


    }

    static void displayEmployees(){
        if(cnt == 0){
            System.out.println("No employees to display.");
        }

        System.out.println("\nCurrent Employees:");
        for(int i=0;i<cnt;i++){
            System.out.println(employees[i]);
        }
    }

    static void updateEmployee(){
        System.out.print("Entee employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        for(int i=0;i<cnt;i++){
            if(employees[i].getId() == id){

                System.out.print("Enter new Name: ");
                employees[i].setName(sc.nextLine());
                
                System.out.print("Enter new Department: ");
                employees[i].setDepartment(sc.nextLine());

                System.out.print("Enter new Salary: ");
                employees[i].setSalary(sc.nextDouble());

                System.out.println("Employee updated successfully!");
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    static void deleteEmployee(){
        System.out.print("Enter Employee ID to delete: ");
        int id = sc.nextInt();

        for(int i=0;i<cnt;i++){
            if(employees[i].getId() == id){
                
                for(int j=i;j<cnt-1;j++){
                    employees[j] = employees[j+1];
                }

                employees[--cnt] = null;
                System.out.println("Employee deleted successfully!");
                return;
            }
        }

        System.out.println("Employee not found.");
    }

    static void searchEmployee(){
        System.out.print("Enter Employee ID to search: ");
        int id = sc.nextInt();

        for (int i = 0; i < cnt; i++) {
            if (employees[i].getId() == id) {
                System.out.println("Found Employee:");
                System.out.println(employees[i]);
                return;
            }
        }

        System.out.println("Employee not found.");
    }

    static void countByDepartment(){
         sc.nextLine();
        System.out.print("Enter department to count: ");
        String dept = sc.nextLine();

        int deptCount = 0;
        for (int i = 0; i < cnt; i++) {
            if (employees[i].getDepartment().equalsIgnoreCase(dept)) {
                deptCount++;
            }
        }

        System.out.println("Number of employees in " + dept + ": " + deptCount);
    }

}