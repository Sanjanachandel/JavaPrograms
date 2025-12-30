//loan eligiblity checker
//design a method that accetps monthly income,credit score and loan amount and return eligiblity status 
//avoid hardcoding inside method

import java.util.Scanner;

class LoanEligibility
{
    static int rejectedCount = 0;   

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String choice;

      
        double minIncome = 25000;
        int minCreditScore = 650;
        double maxLoanAmount = 500000;
        double annualRate = 10;  
        int tenureMonths = 60;    

        do {
            System.out.println("\nEnter monthly income:");
            double income = sc.nextDouble();

            System.out.println("Enter credit score:");
            int creditScore = sc.nextInt();

            System.out.println("Enter loan amount:");
            double loanAmount = sc.nextDouble();

            String status = checkEligibility(
                    income, creditScore, loanAmount,
                    minIncome, minCreditScore, maxLoanAmount
            );

            System.out.println("Eligibility Status: " + status);

            if (status.equals("Eligible")) {
                double emi = calculateEMI(loanAmount, annualRate, tenureMonths);
                System.out.println("Monthly EMI: ?" + emi);
            }
            else if (loanAmount > maxLoanAmount) {
                rejectedCount++;
            }

            System.out.println("Do you want to apply again? (yes/no):");
            choice = sc.next();

        } while (choice.equalsIgnoreCase("yes"));

        System.out.println("\nApplications rejected due to loan limit: " + rejectedCount);
        System.out.println("Program ended");
    }

  
    public static String checkEligibility(
            double income, int creditScore, double loanAmount,
            double minIncome, int minCreditScore, double maxLoanAmount)
    {
        if (income >= minIncome &&
            creditScore >= minCreditScore &&
            loanAmount <= maxLoanAmount)
        {
            return "Eligible";
        }
        else {
            return "Not Eligible";
        }
    }

    public static double calculateEMI(double principal, double annualRate, int months)
    {
        double monthlyRate = annualRate / (12 * 100);
        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                     (Math.pow(1 + monthlyRate, months) - 1);
        return emi;
    }
}
