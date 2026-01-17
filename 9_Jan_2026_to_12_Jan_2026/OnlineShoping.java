package Collection;

public class OnlineShoping {
    public static void main(String[] args) {

        Cart c1 = new Cart("C1", Integer.valueOf(4), Double.valueOf(2500.0));
        Cart c2 = new Cart("C2", Integer.valueOf(2), Double.valueOf(1800.0));
        Cart c3 = new Cart("C3", null, Double.valueOf(3000.0));

        DiscountService service = new DiscountService();

        processCart(c1, service);
        processCart(c2, service);
        processCart(c3, service);
    }

    static void processCart(Cart cart, DiscountService service) {

    if (!service.validateCartData(cart)) {
        System.out.println("Cart " + cart.getCartId() + " --> Invalid Cart Data");
        return;
    }

    if (service.isDiscountApplicable(cart)) {
        double discount = service.calculateDiscount(cart);
        double finalAmount = cart.getTotalAmount() - discount;
        System.out.println("Cart " + cart.getCartId()
                + " --> Discount Applied --> Final Amount: " + finalAmount);
    } else {
        System.out.println("Cart " + cart.getCartId() + " --> No Discount");
    }
}

}

class Cart {
    private String cartId;
    private Integer itemCount;
    private Double totalAmount;

    Cart(String cartId, Integer itemCount, Double totalAmount) {
        this.cartId = cartId;
        this.itemCount = itemCount;
        this.totalAmount = totalAmount;
    }

    public String getCartId() {
        return cartId;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}

class DiscountService {

    boolean validateCartData(Cart cart) {
        return cart.getItemCount() != null && cart.getTotalAmount() != null;
    }

    boolean isDiscountApplicable(Cart cart) {

    if (cart.getItemCount() == null || cart.getTotalAmount() == null) {
        return false;
    }

    return cart.getItemCount() >= 3 && cart.getTotalAmount() >= 2000;
}


    double calculateDiscount(Cart cart) {
        return cart.getTotalAmount() * 0.10;
    }
}

