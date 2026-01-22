package OnlinebankingSystem1;

import java.util.Scanner;

public class OnlineBankingSystem {

    private static Account[] accounts = {
            new Account(101, "Alice", 5000),
            new Account(102, "Bob", 5000),
    };

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            transferFunds();
        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } finally {
            System.out.println("Transaction ended.");
        }
    }

    private static void transferFunds() throws InsufficientFundsException,AccountNotFoundException,IllegalArgumentException {

        System.out.print("Enter source account number: ");
        int sourceAccNo = sc.nextInt();

        System.out.print("Enter target account number: ");
        int targetAccNo = sc.nextInt();

        System.out.print("Enter amount to transfer: ");
        double amount = sc.nextDouble();

        Account source = findAccount(sourceAccNo);
        Account target = findAccount(targetAccNo);

        source.withdraw(amount);
        target.deposit(amount);  

        System.out.println("Transaction successful!");
        System.out.println("Updated balance of " + sourceAccNo + ": " + (int)source.getBalance());
        System.out.println("Updated balance of " + targetAccNo + ": " + (int)target.getBalance());
    }

    private static Account findAccount(int accNo) throws AccountNotFoundException {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accNo) {
                return acc;
            }
        }
        throw new AccountNotFoundException("Target account " + accNo + " does not exist");
    }
}


class InsufficientFundsException extends Exception{
    InsufficientFundsException(String message){
        super(message);
    }
}

class AccountNotFoundException extends Exception{
    AccountNotFoundException(String message){
        super(message);
    }
}
