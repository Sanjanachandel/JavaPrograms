package com.hibernate.controller;

import java.util.Scanner;
import com.hibernate.service.BankService;

public class BankController {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankService service = new BankService();

        while(true){

            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Create Account");
            System.out.println("2. Show Balance");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Fund Transfer");
            System.out.println("6. Show Transactions");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch(choice){

            case 1:

                System.out.print("Enter Account Number: ");
                long accNo = sc.nextLong();

                sc.nextLine(); 

                System.out.print("Enter Name: ");
                String name = sc.nextLine(); 

                System.out.print("Enter Mobile: ");
                long mobile = sc.nextLong();

                System.out.print("Enter Initial Balance: ");
                double balance = sc.nextDouble();

                service.createAccount(accNo, name, mobile, balance);
                break;


            case 2:

                System.out.print("Enter Account Number: ");
                long balAcc = sc.nextLong();
                service.showBalance(balAcc);
                break;


            case 3:

                System.out.print("Enter Account Number: ");
                long dAcc = sc.nextLong();

                System.out.print("Enter Amount: ");
                double dAmt = sc.nextDouble();

                service.deposit(dAcc, dAmt);
                break;


            case 4:

                System.out.print("Enter Account Number: ");
                long wAcc = sc.nextLong();

                System.out.print("Enter Amount: ");
                double wAmt = sc.nextDouble();

                service.withdraw(wAcc, wAmt);
                break;


            case 5:

                System.out.print("Enter Sender Account: ");
                long from = sc.nextLong();

                System.out.print("Enter Receiver Account: ");
                long to = sc.nextLong();

                System.out.print("Enter Amount: ");
                double amt = sc.nextDouble();

                service.fundTransfer(from, to, amt);
                break;


            case 6:

                System.out.print("Enter Account Number: ");
                long tAcc = sc.nextLong();

                service.showTransactions(tAcc);
                break;


            case 7:

                System.out.println("Thank you for using the Bank!");
                sc.close();
                System.exit(0);
                break;


            default:
                System.out.println("Invalid Choice!");
            }
        }
    }
}
