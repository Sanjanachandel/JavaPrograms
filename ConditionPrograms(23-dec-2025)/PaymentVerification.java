class PaymentVerification {
    public static void main(String[] args) {
        int paidAmount = 500;
        int orderAmount = 500;
        String result = (paidAmount == orderAmount) ? "Payment Successful" : "Payment Failed";
        System.out.println(result);
    }
}

