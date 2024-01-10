package domain;

import java.util.Arrays;

public class Course {
    private int id;
    private String cname,ctype;
    private int teacher;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public int getTeacher() {
        return teacher;
    }
    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (teacher != course.teacher) return false;
        return ctype != null ? ctype.equals(course.ctype) : course.ctype == null;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{id,teacher,ctype}); //课程名一旦确定不能更改
    }
}
