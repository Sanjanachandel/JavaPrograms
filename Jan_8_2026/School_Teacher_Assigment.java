package Day8;

public class School_Teacher_Assigment {

    public static void main(String[] args) {

        Teacher t1 = new Teacher();
        t1.setName("Meera");
        t1.setSubject("Mathematics");

        School s1 = new School();
        s1.setSchoolName("Green Valley School");
        s1.setTeacher(t1);

        s1.display();
    }
}

class Teacher {

    private String name;
    private String subject;


    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    
    
}

class School {

    private String schoolName;
    private Teacher teacher;

    public String getSchoolName() {
        return schoolName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }


    public void display() {
        System.out.println(schoolName + " - Teacher: " + teacher.getName() + ", Subject: " +
            teacher.getSubject()
        );
    }
}
