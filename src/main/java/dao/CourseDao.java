package dao;

import domain.Course;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class CourseDao extends AbstractDao{
    static class CourseMapper implements RowMapper<Course> {
        static final CourseMapper INSTANCE = new CourseMapper();
        private CourseMapper() {}
        @Override
        public Course mapRow(ResultSet resultSet, int i) throws SQLException {
            Course course = new Course();
            course.setId(resultSet.getInt("id"));
            course.setCname(resultSet.getString("cname"));
            course.setCtype(resultSet.getString("ctype"));
            course.setTeacher(resultSet.getInt("teacher"));
            return course;
        }
    }
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
        return template.query(sql,CourseMapper.INSTANCE,tid);
    }

    public Course getById(int cid) {
        String sql = "select * from course where id=?";
        try {
            return template.queryForObject(sql, CourseMapper.INSTANCE, cid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public List<Course> getAll() {
        String sql = "select * from course";
        return template.query(sql,CourseMapper.INSTANCE);
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
        String sql = "update course set cname=?, ctype=?, teacher=? where id=?";
        return template.update(sql,course.getCname(),course.getCtype(),course.getTeacher(),course.getId());
    }
}
