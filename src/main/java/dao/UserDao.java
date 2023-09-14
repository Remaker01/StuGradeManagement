package dao;

import domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import util.EncryptUtil;
import util.LogUtil;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Date;
import java.util.List;
import java.util.Map;

// findbyusernameandpswd add
/**
 * 用户Dao
 */
public class UserDao extends AbstractDao{
    static class UserMapper implements RowMapper<User> {
        static final UserMapper INSTANCE = new UserMapper();
        private UserMapper() {}
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User u = new User();
            u.setId(resultSet.getInt("id"));
            u.setUsername(resultSet.getString("username"));
            u.setPassword(resultSet.getString("password"));
            u.setAdmin(resultSet.getByte("isadmin") == 1);
            u.setErrCount(resultSet.getInt("err_count"));
            return u;
        }
    }

    public List<User> findAll() {
        String sql = "select * from users";
        return template.query(sql,UserMapper.INSTANCE);
    }

    public List<User> findByRole(boolean admin) {
        String sql = "select * from users where isadmin=?";
        return template.query(sql,UserMapper.INSTANCE,admin ? 1 : 0);
    }

    public User findById(int id) {
        String sql = "select * from users where id=?";
        return template.queryForObject(sql,UserMapper.INSTANCE,id);
    }

    public User findUserByName(String uname) {
        String sql = "select * from users where username=?";
        try {
            return template.queryForObject(sql, UserMapper.INSTANCE, uname);
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
            return template.queryForObject(sql, UserMapper.INSTANCE, username, pswd);
        } catch (Exception e) {
            LogUtil.log(e);
            return null;
        }
    }
    @Override
    public void add(Object obj) {
        User user = (User)obj;
        //1.定义sql
        String sql = "insert into users(username, `password`,isadmin) values(?,?,?)";
        //2.执行sql
        String pswd = EncryptUtil.encrypt(user.getPassword(), StandardCharsets.ISO_8859_1);
        try {
            template.update(sql, user.getUsername(),pswd,user.isAdmin()?1:0);
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
    public int update(Object obj) {
        User user = (User) obj;
        String uname = user.getUsername(),pswd = user.getPassword();
        pswd = EncryptUtil.encrypt(pswd,StandardCharsets.ISO_8859_1);
        String sql = "update users set `password`=? where username=?";
        return template.update(sql,pswd,uname);
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
    /**
     * 获取给定用户登录失败的次数与上次失败时间
     * @return 失败次数与时间。用户不存在返回两个null。如果未曾失败过，则时间为0
     */
    public Map.Entry<Integer,Long> getFailCountAndTime(String username) {
        String sql = "select err_count,last_err_time from users where username=?";
        try {
            return template.queryForObject(sql, new RowMapper<SimpleImmutableEntry<Integer,Long>>() {
                @Override
                public SimpleImmutableEntry<Integer, Long> mapRow(ResultSet resultSet, int i) throws SQLException {
                    int errCount = resultSet.getInt("err_count");
                    long lastErrTime=resultSet.getLong("last_err_time");
                    return new SimpleImmutableEntry<>(errCount,lastErrTime);
                }
            },username);
        } catch (DataAccessException e) {
            LogUtil.log(e);
            return new SimpleImmutableEntry<>(null,null);
        }
    }
    public void incrFailCount(String username) {
        String sql = "update users set err_count=err_count+1,last_err_time=? where username=?";
        template.update(sql,System.currentTimeMillis(),username);
    }
    public void clearFailCount(String username) {
        String sql = "update users set err_count=0,last_err_time=null where username=?";
        template.update(sql,username);
    }
}