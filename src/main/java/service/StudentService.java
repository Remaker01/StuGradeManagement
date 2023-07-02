package service;

import com.sun.istack.internal.NotNull;
import dao.StudentDao;
import domain.Student;
import util.LogUtil;

import java.util.HashMap;
import java.util.List;

public class StudentService {
    private StudentDao stuDao = new StudentDao();
    /**页面大小，=20*/
    public static final int PAGE_SIZE = 20;

    public List<Student> findAllStudents() {
        return stuDao.findAll();
    }
    /** 查找第pageno页的学生信息*/
    public List<Student> findStudents(int pageno) {
        return stuDao.findByPage((pageno-1) * PAGE_SIZE,PAGE_SIZE,new HashMap<>(0));
    }

    public List<Student> findStudentsByName(String name, int pageno) {
        HashMap<String,String[]> condition = new HashMap<>(1);
        condition.put("sname",new String[]{name});
        return stuDao.findByPage((pageno-1) * PAGE_SIZE,PAGE_SIZE,condition);
    }

    public void addStudent(@NotNull Student student) {
        stuDao.add(student);
    }

    public boolean delStudent(int id) {
        try {
            stuDao.delete(id);
            return true;
        } catch (org.springframework.core.NestedRuntimeException e) {
            LogUtil.log(e);
            return false;
        }
    }

    public String findNameById(int id) {
        return stuDao.findStudentNameById(id);
    }

    public Student findStudentById(int id) {
        return stuDao.findById(id);
    }
    public int update(@NotNull Student student) {
        return stuDao.update(student);
    }
}
