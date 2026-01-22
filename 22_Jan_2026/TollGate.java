package ThreadDay22;



class TollGate {

    private boolean gateIsFree = true; // true = free, false = busy

    public synchronized void passGate(String carName) {

        System.out.println(carName + " arrived at gate...");

        while (!gateIsFree) {
            try {
                System.out.println(carName + " -> Gate busy... waiting...");
                wait();
            } catch (InterruptedException e) {
                System.out.println(carName + " interrupted!");
            }
        }

        // gateIsFree = false;
        System.out.println(carName + " -> Gate OPEN -> Passing now");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // ignored
        }

        gateIsFree = true;
        System.out.println(carName + " -> Passed! Gate free now.");

        // notify();
        notifyAll();
    }
}
