package ThreadDay22;



public class TollGateDemo {

    public static void main(String[] args) {

        TollGate tollBooth = new TollGate();

        // 5 cars coming at same time
        Car c1 = new Car(tollBooth, "Maruti");
        Car c2 = new Car(tollBooth, "Honda");
        Car c3 = new Car(tollBooth, "Tata");
        Car c4 = new Car(tollBooth, "Hyundai");
        Car c5 = new Car(tollBooth, "Mahindra");

        c1.start();
        c2.start();
        c3.start();
        c4.start();
        c5.start();
    }
}
