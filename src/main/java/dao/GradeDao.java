package dao;

import domain.Grade;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;

public class GradeDao extends AbstractDao{
    @Override
    public void add(Object obj) {
        Grade grade = (Grade) obj;
        String sql = "insert into grade values (?,?,?)";
        template.update(sql,grade.getStuId(),grade.getCourseId(),grade.getScore());
    }
    @Override
    public void delete(int id) {
        throw new RuntimeException("Call to delete(int) in GradeDao is not allowed");
    }
    @Override
    public void update(Object obj) {
        Grade grade = (Grade) obj;
        String sql = "update grade set score=? where stuid=? and courseid=?";
        template.update(sql,grade.getScore(),grade.getStuId(),grade.getCourseId());
    }
    /**
     * 删除学号为sid的学生与课程编号为cid的课程的成绩
     */
    public void delete(int sid,int cid) {
        String sql = "delete from grade where stuid=? and courseid=?";
        template.update(sql,sid,cid);
    }
    /**
     * 按课程编号查找成绩，用于教师按自己任教的课程查找学生成绩
     * @param cid 课程编号
     * @return 查找结果
     */
    public List<Grade> getByCourseId(int cid) {
        String sql = "select * from grade where courseid=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),cid);
    }
    /**
     * 按学生姓名查找成绩
     * @param stuName 学生姓名
     * @return 查找结果
     */
    public List<Grade> getByStudent(String stuName) {
        String sql = "select grade.stuid, courseid, score from grade " +
                "inner join student as s on grade.stuid = s.id " +
                "where s.sname=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),stuName);
    }

    public List<Grade> getByCourseName(String courseName) {
        String sql = "select * from grade " +
                "inner join course as c on grade.courseid = c.id " +
                "where c.cname=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),courseName);
    }

    public List<Grade> getByTeacher(int tid) {
        String sql = "select * from grade " +
                "inner join course c on grade.courseid = c.id " +
                "where c.teacher=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),tid);
    }

    public List<Grade> getByTeacher(String name) { //TODO:这个好像不需要？
        String sql = "select * from grade " +
                "inner join course c on grade.courseid = c.id " +
                "inner join users u on c.teacher = u.id " +
                "where u.username=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),name);
    }

    public List<Grade> findByPage(int start, int rows, Map<String,String []> conditions) {
        String sql = "select * from grade where 1 = 1 ";
        return super.findByPage(sql,new BeanPropertyRowMapper<>(Grade.class),start,rows,conditions);
    }
}
