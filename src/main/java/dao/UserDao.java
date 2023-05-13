package dao;

import domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import util.EncryptUtil;
import util.LogUtil;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

// findbyusernameandpswd add
/**
 * 用户Dao
 */
public class UserDao extends AbstractDao{
    static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User u = new User();
            u.setId(resultSet.getInt("id"));
            u.setUsername(resultSet.getString("username"));
            u.setPassword(resultSet.getString("password"));
            u.setAdmin(resultSet.getByte("isadmin") == 1);
            return u;
        }
    }

    public User findById(int id) {
        String sql = "select * from users where id=?";
        return template.queryForObject(sql,new UserMapper(),id);
    }

    public User findUserByName(String uname) {
        String sql = "select * from users where username=?";
        try {
            return template.queryForObject(sql, new UserMapper(), uname);
        }catch (DataAccessException e) {
            LogUtil.log(e);
            return null;
        }
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        try {
            //1.将用户输入的密码用SHA256加密
            String pswd = EncryptUtil.encrypt(password, StandardCharsets.ISO_8859_1);
            String sql = "select * from users where username = ? and `password` = ?";
            //2.将用户名与加密过的密码进行查询
            return template.queryForObject(sql, new UserMapper(), username, pswd);
        } catch (Exception e) {
            LogUtil.log(e);
            return null;
        }
    }
    @Override
    public void add(Object obj) {
        User user = (User)obj;
        //1.定义sql
        String sql = "insert into users(username, `password`) values(?,?)";
        //2.执行sql
        String pswd = EncryptUtil.encrypt(user.getPassword(), StandardCharsets.ISO_8859_1);
        try {
            template.update(sql, user.getUsername(),pswd);
        }catch (DataAccessException e) {
            LogUtil.log(e);
        }
    }
    @Override
    public void delete(int id) {
        String sql = "delete from users where id=?";
        template.update(sql,id);
    }
    /**
     * 修改用户信息，主要是修改密码
     * @param obj 用户
     */
    @Override
    public void update(Object obj) {
        User user = (User) obj;
        String uname = user.getUsername(),pswd = user.getPassword();
        pswd = EncryptUtil.encrypt(pswd,StandardCharsets.ISO_8859_1);
        String sql = "update users set `password`=? where username=?";
        template.update(sql,pswd,uname);
    }

    public int getAdminCount() {
        String sql = "select count(id) from users where isadmin=1";
        try {
            return template.queryForObject(sql, int.class);
        }catch (NullPointerException e) {
            LogUtil.log(e);
            return -1;
        }
    }
}