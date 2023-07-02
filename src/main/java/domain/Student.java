package domain;

public class Student {
    private int id;
    private short age;
    private String sname,gender,address,qq, phone;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public short getAge() {
        return age;
    }
    public void setAge(short age) {
        this.age = age;
    }

    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getQQ() {
        return qq;
    }
    public void setQQ(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Student student = (Student) o;

        return id == student.id;
    }
    @Override
    public int hashCode() {
        return id;
    }
    @Override
    public String toString() {
        return String.format("Student %s gender=%s,age=%d", sname,gender,age);
    }
}