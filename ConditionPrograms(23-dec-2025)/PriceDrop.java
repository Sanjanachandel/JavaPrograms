class PriceDrop {
    public static void main(String[] args) {
        int currentPrice = 900;
        int lastPrice = 1000;
        String result = (currentPrice < lastPrice) ? "Price Dropped" : "No Price Drop";
        System.out.println(result);
    }
}

