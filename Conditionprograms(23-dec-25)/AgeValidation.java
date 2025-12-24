class AgeValidation {
    public static void main(String[] args) {
        int age = 20;
        String result = (age > 0 && age <= 120) ? "Valid Age" : "Invalid Age";
        System.out.println(result);
    }
}

