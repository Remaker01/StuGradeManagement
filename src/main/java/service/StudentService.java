package service;

import dao.StudentDao;
import domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import util.cache.Cache;
import util.cache.LRUCache;

import java.util.HashMap;
import java.util.List;
@Service
public class StudentService {
    @Autowired //注意加了autowired，不能在initializer、构造方法中使用
    private StudentDao stuDao;
    private static Cache<Integer,Student> cache = new LRUCache<>(50);
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
//插入时若已经存在则会直接出现异常，因此不用处理Cache
    public void addStudent(@NonNull Student student) {
        stuDao.add(student);
    }

    public boolean delStudent(int id) {
        try {
            Student studentInCache = cache.get(id);
            if (studentInCache != null) {
                cache.invalidate(id);
            }
            stuDao.delete(id);
            return true;
        } catch (org.springframework.core.NestedRuntimeException e) {
//            LogUtil.log(e);
            return false;
        }
    }
    @Deprecated
    public String findNameById(int id) {
        return findStudentById(id).getSname();
    }

    public Student findStudentById(int id) {
        Student result = cache.get(id);
//        long st = System.nanoTime() / 1000;
        if (result == null) {
            result = stuDao.findById(id);
            cache.put(id,result);
//            long end = System.nanoTime() / 1000;
//            LogUtil.log(Level.INFO,String.format("Cache miss:%d us",end-st));
        } /*else {
            long end = System.nanoTime() / 1000;
            LogUtil.log(Level.INFO,String.format("Cache hit on %d in %d us",id,end-st));
        }*/
        return result;
    }
    public int update(@NonNull Student student) {
        Student studentInCache = cache.get(student.getId());
        //写命中，更新缓存+数据库
        if (studentInCache != null) {
            cache.put(student.getId(), student);
        }
        return stuDao.update(student);
    }
}
