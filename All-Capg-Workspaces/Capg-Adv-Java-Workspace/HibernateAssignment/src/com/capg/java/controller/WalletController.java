package com.capg.java.controller;

import com.capg.java.service.*;
import java.util.Scanner;

public class WalletController {

    public static void start() {

        AccountService service = new AccountServiceImpl();
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== Wallet Menu =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Fund Transfer");
            System.out.println("5. Show Balance");
            System.out.println("6. Exit");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.next();

                    System.out.print("Enter Mobile: ");
                    String mob = sc.next();

                    System.out.print("Enter Initial Amount: ");
                    double amt = sc.nextDouble();

                    service.createAccount(name, mob, amt);
                    break;

                case 2:
                    System.out.print("Account ID & Amount: ");
                    service.deposit(sc.nextLong(), sc.nextDouble());
                    break;

                case 3:
                    System.out.print("Account ID & Amount: ");
                    service.withdraw(sc.nextLong(), sc.nextDouble());
                    break;

                case 4:
                    System.out.print("From ID: ");
                    long f = sc.nextLong();

                    System.out.print("To ID: ");
                    long t = sc.nextLong();

                    System.out.print("Amount: ");
                    double a = sc.nextDouble();

                    service.transfer(f, t, a);
                    break;

                case 5:
                    System.out.print("Account ID: ");
                    service.showBalance(sc.nextLong());
                    break;

                case 6:
                    System.exit(0);
            }
        }
    }
}
