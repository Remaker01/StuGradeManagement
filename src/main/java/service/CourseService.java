package service;

import com.sun.istack.internal.NotNull;
import dao.CourseDao;
import domain.Course;
import domain.User;

import java.util.List;

public class CourseService {
    private CourseDao courseDao = new CourseDao();

    public String getCourseNameById(int id) {
        return courseDao.getNameById(id);
    }

    public List<Course> getCoursesByTeacherId(int tid) {
        return courseDao.getByTeacherId(tid);
    }

    public Course getByCourseId(int id) {return courseDao.getById(id);}

    public List<Course> getAllCourses() {return courseDao.getAll();}

    public void addCourse(String cname,String ctype,int teacher) {
        Course c = new Course();
        c.setCname(cname);
        c.setCtype(ctype);
        c.setTeacher(teacher);
        courseDao.add(c);
    }

    public void addCourse(String cname, String ctype,User teacher) {
        if (!teacher.isAdmin())
            addCourse(cname,ctype,teacher.getId());
    }

    public void deleteCourse(int id) {
        courseDao.delete(id);
    }

    public int updateCourse(Course course) {
         return courseDao.update(course);
    }
}
