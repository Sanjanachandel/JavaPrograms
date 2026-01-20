package collection1;
import java.util.ArrayList;
import java.util.List;

public class StudentAttendenceSystem

{
    public static void main(String[] args){
        AttendanceManager am = new AttendanceManager();
        am.addStudent(new Student(101,"Rahul",true));
        am.addStudent(new Student(102,"Anjali",false));
        am.addStudent(new Student(103,"Vikram",true));
        am.addStudent(new Student(104,"Sneha",false));

        System.out.println("All Students:");
        am.displayAllStudents();

        System.out.println("\nPresent Students:");
        am.displayPresentStudents();

        System.out.println("\nAbsent Students:");
        am.displayAbsentStudents();

        System.out.println("\nUpdating Attendance (Roll No 102 marked Present)\n");
        am.markAttendance(102,true);

        System.out.println("Updated Student List:");
        am.displayAllStudents();
    }
}

class Student{
    private int rollNumber;
    private String studentName;
    private boolean isPresent;

    Student(int rollNumber,String studentName,boolean isPresent){
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.isPresent = isPresent;
    }

    public int getRollNumber(){
        return rollNumber;
    }

    public boolean isPresent(){
        return isPresent;
    }
    
    public void setPresent(boolean isPresent){
        this.isPresent = isPresent;
    }

    @Override
    public String toString(){
        return "Roll No: " + rollNumber + ", Name: " + studentName +", Attendance: " + (isPresent ? "Present" : "Absent");
    }
}

class AttendanceManager{
    private List<Student> studentList = new ArrayList<>();

    public void addStudent(Student student){
        studentList.add(student);
    }

    public void markAttendance(int rollNumber,boolean isPresent){
        for(Student s:studentList){
            if(s.getRollNumber() == rollNumber){
                s.setPresent(isPresent);
                
            }
        }
    }

    public void displayAllStudents(){
        for(Student s:studentList){
            System.out.println(s);
        }
    }

    public void displayPresentStudents(){
        for(Student s:studentList){
            if(s.isPresent()){
                System.out.println(s);
            }
        }
    }

    public void displayAbsentStudents(){
        for(Student s:studentList){
            if(!s.isPresent()){
                System.out.println(s);
            }
        }
    }


}