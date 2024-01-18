package dao;

import domain.Grade;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
@Repository
public class GradeDao extends AbstractDao{
    static class GradeMapper implements RowMapper<Grade> {
        static final GradeMapper INSTANCE = new GradeMapper();
        private GradeMapper() {}
        @Override
        public Grade mapRow(ResultSet resultSet, int i) throws SQLException {
            Grade grade = new Grade();
            grade.setStuId(resultSet.getInt("stuid"));
            grade.setCourseId(resultSet.getInt("courseid"));
            grade.setScore(resultSet.getShort("score"));
            return grade;
        }
    }
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
    public int update(Object obj) {
        Grade grade = (Grade) obj;
        String sql = "update grade set score=? where stuid=? and courseid=?";
        return template.update(sql,grade.getScore(),grade.getStuId(),grade.getCourseId());
    }
    /** 删除学号为sid的学生与课程编号为cid的课程的成绩*/
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
        return template.query(sql,GradeMapper.INSTANCE,cid);
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
        return template.query(sql,GradeMapper.INSTANCE,stuName);
    }

    public List<Grade> getByCourseName(String courseName) {
        String sql = "select * from grade " +
                "inner join course as c on grade.courseid = c.id " +
                "where c.cname=?";
        return template.query(sql,GradeMapper.INSTANCE,courseName);
    }

    public List<Grade> getByTeacher(int tid) {
        String sql = "select * from grade " +
                "inner join course c on grade.courseid = c.id " +
                "where c.teacher=?";
        return template.query(sql,GradeMapper.INSTANCE,tid);
    }

    public List<Grade> getByTeacher(int tid,int start,int rows) {
        String sql = "select * from grade " +
                "inner join course c on grade.courseid = c.id " +
                "where c.teacher=? limit ?,?";
        return template.query(sql,GradeMapper.INSTANCE,tid,start,rows);
    }

    public List<Grade> findByPage(int start, int rows, Map<String,String []> conditions) {
        String sql = "select * from grade where 1 = 1 ";
        return super.findByPage(sql,GradeMapper.INSTANCE,start,rows,conditions);
    }
}
