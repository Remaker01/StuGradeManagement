package service;

import com.sun.istack.internal.NotNull;
import dao.StudentDao;
import domain.Student;

import java.util.List;

public class StudentService {
    private StudentDao stuDao = new StudentDao();

    public List<Student> findAllStudents() {
        return stuDao.findAll();
    }

    public void addStudent(@NotNull Student student) {
        stuDao.add(student);
    }

    public void delStudent(int id) {
        stuDao.delete(id);
    }

    public String findNameById(int id) {
        return stuDao.findStudentNameById(id);
    }

    public Student findStudentById(int id) {
        return stuDao.findById(id);
    }
    public void update(@NotNull Student student) {
        stuDao.update(student);
    }
}
