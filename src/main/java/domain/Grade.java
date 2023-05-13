package domain;

public class Grade {
    private int stuId,courseId;
    private short score;

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }
}