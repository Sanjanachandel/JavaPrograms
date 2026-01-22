package ThreadDay22;



public class PersonA extends Thread {

    private final TicketBookingSystem system;

    PersonA(TicketBookingSystem system) {
        this.system = system;
    }

    @Override
    public void run() {
        system.bookByChoosingSeatFirst("Rahul (Seat First)");
    }
}
