package ThreadDay22;



public class Car extends Thread {

    private final TollGate gate;
    private final String name;

    Car(TollGate gate, String name) {
        this.gate = gate;
        this.name = name;
    }

    @Override
    public void run() {
        gate.passGate(name);
    }
}
