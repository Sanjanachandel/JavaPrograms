class CouponExpiry {
    public static void main(String[] args) {
        int currentDate = 10;
        int expiryDate = 15;
        String result = (currentDate < expiryDate) ? "Coupon Valid" : "Coupon Expired";
        System.out.println(result);
    }
}

