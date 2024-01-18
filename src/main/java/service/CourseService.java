package service;

import dao.CourseDao;
import domain.Course;
import domain.Student;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.cache.Cache;
import util.cache.LRUCache;

import java.util.HashMap;
import java.util.List;
@Service
public class CourseService {
    @Autowired
    private CourseDao courseDao;
    public static final int PAGE_SIZE = 15; //课程数可能较少
    private static Cache<Integer,Course> cache = new LRUCache<>(20);
    @Deprecated
    public String getCourseNameById(int id) {
        return getByCourseId(id).getCname();
    }

    public List<Course> getCoursesByTeacherId(int tid) {
        return courseDao.getByTeacherId(tid);
    }

    public List<Course> getCoursesByTeacherId(int tid,int pageno) {
        HashMap<String,String[]> condition = new HashMap<>(1);
        condition.put("teacher",new String[]{Integer.toString(tid)});
        return courseDao.findByPage((pageno-1)*PAGE_SIZE,PAGE_SIZE,condition);
    }

    public Course getByCourseId(int id) {
        Course result = cache.get(id);
        if (result == null) {
            result=courseDao.getById(id);
            cache.put(id,result);
        }
        return result;
    }

    public List<Course> getAllCourses() {return courseDao.getAll();}

    public List<Course> getCourses(int pageno) {
        return courseDao.findByPage((pageno-1) * PAGE_SIZE,PAGE_SIZE,new HashMap<>(0));
    }

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
        Course inCache = cache.get(id);
        if (inCache != null){
            cache.invalidate(id);
        }
        courseDao.delete(id);
    }

    public int updateCourse(Course course) {
        Course inCache = cache.get(course.getId());
        if (inCache != null){
            cache.put(course.getId(), course);
        }
        return courseDao.update(course);
    }
}
