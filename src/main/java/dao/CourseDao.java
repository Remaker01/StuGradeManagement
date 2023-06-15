package dao;

import domain.Course;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class CourseDao extends AbstractDao{
    public String getNameById(int id) {
        String sql = "select cname from course where id=?";
        try {
            return template.queryForObject(sql,String.class,id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public List<Course> getByTeacherId(int tid) {
        String sql = "select * from course where teacher=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Course.class),tid);
    }

    public Course getById(int cid) {
        String sql = "select * from course where id=?";
        try {
            return template.queryForObject(sql, new BeanPropertyRowMapper<>(Course.class), cid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public List<Course> getAll() {
        String sql = "select * from course";
        return template.query(sql,new BeanPropertyRowMapper<>(Course.class));
    }
    @Override
    public void add(Object obj) {
        Course course = (Course) obj;
        String sql = "insert into course(cname,ctype,teacher) values(?,?,?);";
        template.update(sql,course.getCname(),course.getCtype(),course.getTeacher());
    }
    @Override
    public void delete(int id) {
        String sql = "delete from course where id=?";
        template.update(sql,id);
    }
    @Override
    public int update(Object obj) {
        Course course = (Course) obj;
        String sql = "update course set cname=? and ctype=? where id=?";
        final String cname = course.getCname();
        return template.update(sql,cname,course.getCtype(),course.getId());
    }
}
