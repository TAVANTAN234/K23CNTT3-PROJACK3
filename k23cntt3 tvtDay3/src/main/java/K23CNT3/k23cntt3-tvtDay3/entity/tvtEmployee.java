package K23CNT3.tvtDay03.service.entity;

public class tvtEmployee {
    private long id;
    private String fullName;
    private String gender;
    private int age;
    private double salary;

    public tvtEmployee() {}
    public tvtEmployee(long id, String fullName, String gender, int age, double salary) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.salary = salary;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
