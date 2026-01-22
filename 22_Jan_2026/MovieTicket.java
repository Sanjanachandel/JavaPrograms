package ThreadDay22;



public class MovieTicket {

    public static void main(String[] args) {

        System.out.println("=== Movie Ticket Booking Started (only 2 tickets left!) ===");

        TicketBookingSystem bookingSystem = new TicketBookingSystem();

        PersonA rahul = new PersonA(bookingSystem);
        PersonB priya = new PersonB(bookingSystem);

        rahul.start();
        priya.start();
    }
}
