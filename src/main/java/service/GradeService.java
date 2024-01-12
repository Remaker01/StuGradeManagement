package service;

import dao.GradeDao;
import domain.Grade;

import java.util.HashMap;
import java.util.List;
/*
需要分页的表：学生、成绩。
课程、用户不会太多，故不需分页？
*/
public class GradeService {
    private GradeDao gradeDao = new GradeDao();
    public static final int PAGE_SIZE = 20;

    public List<Grade> getGrades(int page) {
        return gradeDao.findByPage((page-1) * PAGE_SIZE,PAGE_SIZE,new HashMap<>(0));
    }

    public List<Grade> getGradesByCourseId(int cid) {
        return gradeDao.getByCourseId(cid);
    }

    public List<Grade> getGradesByCourseId(int cid,int page) {
        HashMap<String ,String []> condition = new HashMap<>(1);
        condition.put("courseid",new String[]{Integer.toString(cid)});
        return gradeDao.findByPage((page-1) * PAGE_SIZE,PAGE_SIZE,condition);
    }

    public List<Grade> getGradesByTeacherId(int tid) { // 多表联查
        return gradeDao.getByTeacher(tid);
    }

    public List<Grade> getGradesByTeacherId(int tid,int page) {
        return gradeDao.getByTeacher(tid,(page-1)*PAGE_SIZE,PAGE_SIZE);
    }

    public List<Grade> getGradesByStudentName(String stuName) { // 多表联查
        return gradeDao.getByStudent(stuName);
    }

    public List<Grade> getGradeByCourseName(String course) { //多表联查
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

    public int updateGrade(Grade grade) {
        return gradeDao.update(grade);
    }
}
