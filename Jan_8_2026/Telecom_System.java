package Day8;

public class Telecom_System{
    public static void main(String[] args)
    {
        PremiumReacharge p=new PremiumReacharge(299,199,50);
        p.calculateFinalAmount();
    }
}
class Recharge{
    private int amount;
    public Recharge(int amount){
        this.amount=amount;
        System.out.println("Base recharge initialized: "+amount);
    }
    public int getAmount(){
        return amount;
    }
}
class DataRecharge extends Recharge{
    private int amount;
    public DataRecharge(int a,int amount)
    {
        super(a);
        this.amount=amount;
        System.out.println("Data pack added: "+amount);
    }
   
    public int getAmount()
    {
        return super.getAmount()+amount;
    }
}
class PremiumReacharge extends DataRecharge{
    private int amount;
    public PremiumReacharge(int a,int b,int amount)
    {
        super(a,b);
        this.amount=amount;

        System.out.println("Service tax added: "+amount);
    }
    public int calculateFinalAmount(){
         int afterData = super.getAmount();
        System.out.println("Amount after data pack: " + afterData);

        int finalAmount = afterData + amount;
        System.out.println("Final recharge amount calculated");
        System.out.println("Payable Amount: " + finalAmount);

        return finalAmount;
    }
}