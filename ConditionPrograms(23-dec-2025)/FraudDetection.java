class FraudDetection {
    public static void main(String[] args) {
        int orderAmount = 9000;
        int averageAmount = 5000;
        String result = (orderAmount > averageAmount) ? "Suspicious Transaction" : "Normal Transaction";
        System.out.println(result);
    }
}

