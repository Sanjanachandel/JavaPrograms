 class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;

    Employee(int id,String name,String department,double salary){
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId(){
        return id;
    }

    public String getDepartment(){
        return department;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public void setSalary(double salary){
        this.salary = salary;
    }

    @Override
    public String toString(){
        return "Employee[ID=" + id + ", Name=" + name + ", Department=" + department +", Salary=" + (int)salary +"]";
    }

    @Override
    public boolean equals(Object obj){
        Employee e = (Employee) obj;
        return this.id == e.id;
    }

    
}