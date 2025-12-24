public class ExamEligibility {
    public static void main(String[] args) {

        int attendance = 70;
        boolean medicalCertificate = true;
        boolean feesPaid = true;

        boolean allowed =
                (attendance > 75 || medicalCertificate) && feesPaid;

        String result = allowed ? "Allowed for Exam" : "Not Allowed for Exam";
        System.out.println(result);
    }
}

