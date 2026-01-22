package ThreadDay22;



public class PersonB extends Thread {

    private final TicketBookingSystem system;

    PersonB(TicketBookingSystem system) {
        this.system = system;
    }

    @Override
    public void run() {
        system.bookByPayingFirst("Priya (Payment First)");
    }
}
