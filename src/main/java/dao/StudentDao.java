package dao;

import domain.Student;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// findall add delete findbyid update findtotalcount findbypage
public class StudentDao extends AbstractDao {
    static class StudentMapper implements RowMapper<Student> {
        static final StudentMapper INSTANCE = new StudentMapper();
        private StudentMapper() {}
        @Override
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            Student stud = new Student();
            stud.setId(resultSet.getInt("id"));
            stud.setSname(resultSet.getString("sname"));
            stud.setAge(resultSet.getShort("age"));
            stud.setAddress(resultSet.getString("address"));
            stud.setGender(resultSet.getString("gender"));
            stud.setPhone(resultSet.getString("phone"));
            stud.setQQ(resultSet.getString("qq"));
            return stud;
        }
    }

    public List<Student> findAll() {
        //使用JDBC操作数据库...
        //1.定义sql
        String sql = "select * from student";
        return template.query(sql, StudentMapper.INSTANCE);
    }

    public void add(Object obj) {
        Student stud = (Student) obj;
        //1.定义sql
        String sql = "insert into student(sname, gender, address, age, qq, phone) values(?,?,?,?,?,?)";
        //2.执行sql
        template.update(sql,
                stud.getSname(),
                stud.getGender(),
                stud.getAddress(),
                stud.getAge(),
                stud.getQQ(),
                stud.getPhone()
        );
    }

    @Override
    public void delete(int id) {
        String sql = "delete from student where id = ?";
        template.update(sql, id);
    }

    public String findStudentNameById(int id) {
        String sql = "select sname from student where id=?";
        try {
            return template.queryForObject(sql, String.class, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public Student findById(int id) {
        String sql = "select * from student where id = ?";
        try {
            return template.queryForObject(sql, StudentMapper.INSTANCE, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public int update(Object obj) {
        Student student = (Student) obj;
        String sql = "update student set sname = ?,gender = ? ,age = ? , address = ? , qq = ?, phone = ? where id = ?";
        return template.update(sql,
                student.getSname(),
                student.getGender(),
                student.getAge(),
                student.getAddress(),
                student.getQQ(),
                student.getPhone(),
                student.getId()
        );
    }

    public int findTotalCount(Map<String, String[]> condition) {
        //1.定义模板初始化sql
        String sql = "select count(*) from student where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        //2.遍历map
        Set<String> keySet = condition.keySet();
        //定义参数的集合
        List<Object> params = new ArrayList<>();
        for (String key : keySet) {

            //排除分页条件参数
            if ("currentPage".equals(key) || "rows".equals(key)) {
                continue;
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if (value != null && !"".equals(value)) {
                //有值
                sb.append(" and ").append(key).append(" like ? ");
                params.add("%" + value + "%");//？条件的值
            }
        }
//        System.out.println(sb.toString());
//        System.out.println(params);
        return template.queryForObject(sb.toString(), params.toArray(), int.class);
    }

    public List<Student> findByPage(int start, int rows, Map<String, String[]> condition) {
        String sql = "select * from student  where 1 = 1 ";
        return super.findByPage(sql, StudentMapper.INSTANCE, start, rows, condition);
    }
}
