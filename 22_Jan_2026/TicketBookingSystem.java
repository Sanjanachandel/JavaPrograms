package ThreadDay22;


public class TicketBookingSystem {

    private final Object seatLock = new Object();
    private final Object paymentLock = new Object();

    // Seat first → then payment
    public void bookByChoosingSeatFirst(String person) {

        synchronized (seatLock) {
            System.out.println(person + " -> Got seatLock, choosing seats...");

            try {
                Thread.sleep(1000); // thinking time (realistic delay)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(person + " -> Now waiting for paymentLock...");
        }

        synchronized (paymentLock) {
            System.out.println(person + " -> Got paymentLock -> Booking confirmed!");
        }
    }

    // Payment first → then seat
    public void bookByPayingFirst(String person) {

        synchronized (paymentLock) {
            System.out.println(person + " -> Got paymentLock, entering card details...");

            try {
                Thread.sleep(1000); // entering OTP time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(person + " -> Now waiting for seatLock...");
        }

        synchronized (seatLock) {
            System.out.println(person + " -> Got seatLock -> Booking confirmed!");
        }
    }
}