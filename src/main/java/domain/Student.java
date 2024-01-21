package domain;

import java.util.Arrays;

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

    public String getQq() {return qq;} //JSTL使用

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (age != student.age) return false;
        if (sname != null ? !sname.equals(student.sname) : student.sname != null) return false;
        if (address != null ? !address.equals(student.address) : student.address != null) return false;
        if (qq != null ? !qq.equals(student.qq) : student.qq != null) return false;
        return phone != null ? phone.equals(student.phone) : student.phone == null;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{id, age, sname, address, qq, phone});
    }//学生性别一旦确定不能修改

    @Override
    public String toString() {
        return String.format("Student %s gender=%s,age=%d", sname,gender,age);
    }
}