package dao;

import domain.Course;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class CourseDao extends AbstractDao{
    public String getNameById(int id) {
        String sql = "select cname from course where id=?";
        return template.queryForObject(sql,String.class,id);
    }

    public List<Course> getByTeacherId(int tid) {
        String sql = "select * from course where teacher=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Course.class),tid);
    }

    public List<Course> getAll() {
        String sql = "select * from course";
        return template.query(sql,new BeanPropertyRowMapper<>(Course.class));
    }
    @Override
    public void add(Object obj) {
        Course course = (Course) obj;
        String sql = "insert into course(cname,ctype) values(?,?);";
        template.update(sql,course.getCname(),course.getCtype());
    }
    @Override
    public void delete(int id) {
        String sql = "delete from course where id=?";
        template.update(sql,id);
    }
    @Override
    public void update(Object obj) {
        Course course = (Course) obj;
        String sql = "update course set cname=? and ctype=? where id=?";
        final String cname = course.getCname();
        template.update(sql,cname,course.getCtype(),course.getId());
    }
}
