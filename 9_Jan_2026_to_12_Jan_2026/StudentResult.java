package Collection;

public class StudentResult {

    public static void main(String[] args) {

        Student s1 = new Student("S1", "Asha",
                Integer.valueOf(70), Integer.valueOf(65), Integer.valueOf(60));

        Student s2 = new Student("S2", "Rohan",
                Integer.valueOf(35), null, Integer.valueOf(40));

        Student s3 = new Student("S3", "Kiran",
                Integer.valueOf(30), Integer.valueOf(25), Integer.valueOf(20));

        ResultService service = new ResultService();

        processResult(s1, service);
        processResult(s2, service);
        processResult(s3, service);
    }

    static void processResult(Student student, ResultService service) {

        Integer total = service.calculateTotal(student);
        Double avg = service.calculateAverage(total);
        boolean passed = service.isPassed(avg);

        System.out.println(student.getStudentName()
                + " --> Total: " + total
                + " --> Avg: " + avg.intValue()
                + " --> " + (passed ? "Pass" : "Fail"));
    }
}


class Student {

    private String studentId;
    private String studentName;
    private Integer mathsMarks;
    private Integer scienceMarks;
    private Integer englishMarks;

    public Student(String studentId, String studentName,
                   Integer mathsMarks, Integer scienceMarks, Integer englishMarks) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.mathsMarks = mathsMarks;
        this.scienceMarks = scienceMarks;
        this.englishMarks = englishMarks;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getMathsMarks() {
        return mathsMarks;
    }

    public Integer getScienceMarks() {
        return scienceMarks;
    }

    public Integer getEnglishMarks() {
        return englishMarks;
    }
}


class ResultService {

    Integer getSafeMarks(Integer marks) {
        return (marks == null) ? 0 : marks;
    }

    Integer calculateTotal(Student student) {

        int maths = getSafeMarks(student.getMathsMarks());
        int science = getSafeMarks(student.getScienceMarks());
        int english = getSafeMarks(student.getEnglishMarks());

        return maths + science + english;
    }

    Double calculateAverage(Integer total) {
        return total / 3.0;
    }

    boolean isPassed(Double avg) {
        return avg >= 40;
    }
}

