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
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Grade grade = (Grade) o;

        if (stuId != grade.stuId)
            return false;
        return courseId == grade.courseId;
    }
    @Override
    public int hashCode() {
        int result = stuId;
        return 31 * result + courseId;
    }
}