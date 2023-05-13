package domain;

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
}
