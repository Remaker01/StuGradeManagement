package service;

import dao.GradeDao;
import domain.Grade;

import java.util.List;

public class GradeService {
    private GradeDao gradeDao = new GradeDao();

    public List<Grade> getGradesByCourseId(int cid) {
        return gradeDao.getByCourseId(cid);
    }

    public List<Grade> getGradesByStudentName(String stuName) {
        return gradeDao.getByStudent(stuName);
    }

    public List<Grade> getGradeByCourseName(String course) {
        return gradeDao.getByCourseName(course);
    }

    public void addGrade(int stuId,int courseId,short score) {
        Grade grade = new Grade();
        grade.setStuId(stuId);
        grade.setCourseId(courseId);
        grade.setScore(score);
        gradeDao.add(grade);
    }

    public void deleteGrade(int stuId,int courseId) {
        gradeDao.delete(stuId,courseId);
    }

    public void updateGrade(Grade grade) {
        gradeDao.update(grade);
    }
}
