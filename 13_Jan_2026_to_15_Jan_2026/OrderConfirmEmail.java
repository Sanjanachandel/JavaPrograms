package ObjectHandling;

public class OrderConfirmEmail {
	public static void main(String[] args){

	    AdvancedOrderEmailBuilder builder=new AdvancedOrderEmailBuilder();

	    builder.addGreeting("Arjun");
	    builder.addOrderItem("Pizza",2,300);
	    builder.addOrderItem("Burger",2,150);
	    builder.addOrderItem("Coke",3,50);

	    builder.applyDiscount();
	    builder.addTax();
	    builder.addDeliveryCharge();
	    builder.addFinalSummary();

	    System.out.println(builder.getEmailContent());
	    }
	    }

	class AdvancedOrderEmailBuilder{
	    StringBuilder email;
	    int subTotal;
	    double discount;
	    double tax;
	    int deliveryCharge;

	public AdvancedOrderEmailBuilder(){
	email=new StringBuilder();
	subTotal=0;
	discount=0;
	tax=0;
	deliveryCharge=0;
	}

	public void addGreeting(String customerName){
	email.append("Hello ").append(customerName).append(",\n\n");
	email.append("Thank you for ordering with FoodExpress.\n");
	email.append("Here are your order details:\n");
	email.append("--------------------------------------------------\n");
	}

	public void addOrderItem(String itemName,int quantity,int price){
	int total=quantity*price;
	subTotal+=total;
	email.append(itemName+" | Qty: "+quantity+" | Price: "+price+" Rs | Total: "+total+" Rs\n");
	}

	public void applyDiscount(){
	if(subTotal>=1000){
	discount=subTotal*0.10;
	email.append("Discount (10%): "+(int)discount+" Rs\n");
	}
	}

	public void addTax(){
	tax=(subTotal-discount)*0.05;
	email.append("GST (5%): "+(int)tax+" Rs\n");
	}

	public void addDeliveryCharge(){
	if(subTotal<500){
	deliveryCharge=50;
	email.append("Delivery Charge: "+deliveryCharge+" Rs\n");
	}else{
	email.append("Delivery Charge: Free\n");
	}
	}

	public void addFinalSummary(){
	email.append("--------------------------------------------------\n");
	email.append("Subtotal        : "+subTotal+" Rs\n");
	int finalPayable=(int)(subTotal-discount+tax+deliveryCharge);
	email.append("Final Payable   : "+finalPayable+" Rs\n");
	}

	public String getEmailContent(){
	return email.toString();
	}
	}