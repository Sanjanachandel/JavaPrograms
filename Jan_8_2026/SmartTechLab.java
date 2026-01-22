package Day8;

public class SmartTechLab{
    public static void main(String[] args)
    {
        Smartphone s=new Smartphone("Galaxy S24","5000");
         Smartphone s1=new Smartphone("iPhone 16","4200");
        s.displaySmartphone();
        
         s1.displaySmartphone();
       
    }
}
class Battery {
    private String capacity;
    public Battery(String capacity)
    {
        this.capacity=capacity;
    }
    public void displayBattery()
    {
        

        System.out.println("Battery capacity: "+capacity+"mAh");
    }
}

class Smartphone {
    private String model;
    private Battery battery;
    public Smartphone(String model,String battery)
    {
        this.model=model;
        this.battery=new Battery(battery);
    }
    public void displaySmartphone()
    {
        System.out.println("Phone model: "+model);
        battery.displayBattery();
    }

}


