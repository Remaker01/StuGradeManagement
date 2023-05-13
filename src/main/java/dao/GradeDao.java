package dao;

import domain.Grade;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

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
    public List<Grade> queryByCourseId(int cid) {
        String sql = "select * from grade where courseid=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),cid);
    }
    /**
     * 按学生姓名查找成绩
     * @param stuName 学生姓名
     * @return 查找结果
     */
    public List<Grade> getGradesByStudent(String stuName) {
        String sql = "select grade.stuid, courseid, score from grade " +
                "inner join student as s on grade.stuid = s.id " +
                "where s.sname=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Grade.class),stuName);
    }
}
