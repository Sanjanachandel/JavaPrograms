package FileClass;




public class Food {
	private String foodName;
	private int foodQuantity;
	private double foodPrice;
	
	public Food(String foodName, int foodQuantity, double foodPrice) {
		this.foodName = foodName;
		this.foodQuantity = foodQuantity;
		this.foodPrice = foodPrice;
	}
	
	public double getTotal() {
		return foodQuantity * foodPrice;
	}
	
	
	@Override
	public String toString() {
		double total = foodQuantity * foodPrice;
		return String.format( "%-30s Qty: %2d   Price: %-6.2f   Total: %.2f" , foodName ,foodQuantity , foodPrice , total);
	}
}