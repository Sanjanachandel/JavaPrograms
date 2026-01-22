package Day8;

public class Hospital_Management_System {
    public static void main(String[] args)
    {

        Doctor d=new Doctor("Dr. Karthik",42,301,"Cardiology","Heart Surgery");
        d.displayPerson();
        d.displayStaff();
        d.treatPatient();
        System.out.println();
        Nurse n1=new Nurse("Suma",29,302,"Cardiology","Night");
        n1.displayPerson();
        n1.displayStaff();
        n1.assistDoctor();

    }
}
class Person{
    private String name;
    private int age;
    public Person(String name,int age)
    {
        this.name=name;
        this.age=age;
    }
    public void displayPerson()
    {
        System.out.println("Name: "+name);
        System.out.println("Age: "+age);
    }
}

class Staff extends Person{
    private int staffId;
    private String department;
    public Staff(String name,int age,int staffId,String department)
    {
        super(name,age);
        this.staffId=staffId;
        this.department=department;
    }
    public void displayStaff()
    {
        System.out.println("Staff ID: "+staffId);
        System.out.println("Department: "+department);
    }
}

class Doctor extends Staff{
    private String specialization;
    public Doctor(String name,int age,int staffId,String department,String specialization)
    {
        super(name,age,staffId,department);
        this.specialization=specialization;
    }
    public void treatPatient()
    {
        System.out.println("Specialization: "+specialization);
        System.out.println("Doctor is treating a patient");
    }

}
class Nurse extends Staff{
    private String shift;
    public Nurse(String name,int age,int staffId,String department,String shift){
        super(name,age,staffId,department);
        this.shift=shift;
    }
    public void assistDoctor()
    {
        System.out.println("Shift: "+shift);
        System.out.println("Nurse is assisting the doctor");
    }
}