public class Solutionn {

    static final Object PEN = new Object();
    static final Object PAPER = new Object();

    public static void main(String[] args) {

        
        runResolvedPhase();
    }

    static void runDeadlockPhase() {

        Thread martin = new Thread(() -> {
            synchronized (PEN) {
                System.out.println("Martin using Pen waiting for Paper");
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (PAPER) {
                    System.out.println("Martin picked Paper");
                }
            }
        });

        Thread david = new Thread(() -> {
            synchronized (PAPER) {
                System.out.println("David using Paper waiting for Pen");
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (PEN) {
                    System.out.println("David picked Pen");
                }
            }
        });

        martin.start();
        david.start();
    }

    static void runResolvedPhase() {

        Thread martin = new Thread(() -> {
            synchronized (PEN) {
                System.out.println("Martin picked Pen");
                synchronized (PAPER) {
                    System.out.println("Martin picked Paper");
                }
            }
        });

        Thread david = new Thread(() -> {
            synchronized (PEN) {
                System.out.println("David picked Pen");
                synchronized (PAPER) {
                    System.out.println("David picked Paper");
                }
            }
        });

        martin.start();
        david.start();
    }
}
