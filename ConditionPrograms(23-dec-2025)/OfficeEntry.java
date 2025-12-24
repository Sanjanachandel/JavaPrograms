public class OfficeEntry {
    public static void main(String[] args) {

        boolean hasIDCard = true;
        boolean isPermanent = false;
        boolean isContract = true;
        boolean managerApproval = true;

        boolean canEnter =
                hasIDCard && (isPermanent || (isContract && managerApproval));

        System.out.println(
                canEnter ? "Entry Allowed" : "Entry Denied"
        );
    }
}

