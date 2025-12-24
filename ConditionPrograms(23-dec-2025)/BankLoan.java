class BankLoan {
    public static void main(String[] args) {

        double P = 500000;   
        double R = 10;       
        double T = 5;        

        double SI = (P * R * T) / 100;
        double totalAmount = P + SI;

        System.out.println("Principal Amount: ?" + P);
        System.out.println("Rate of Interest: " + R + "%");
        System.out.println("Time: " + T + " years");
        System.out.println("Simple Interest: ?" + SI);
        System.out.println("Total Amount to Pay: ?" + totalAmount);
    }
}

