package service;

import dao.UserDao;
import domain.User;
import util.EncryptUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class UserService {
    public static final int LOGIN_EXPIRES = 10*60*1000;
    public static final int MAX_LOGIN_RETRY = 5;
    private UserDao userDao = new UserDao();

    public List<User> findAll() {return userDao.findAll();}

    public List<User> findByRole(boolean isAdmin) {return userDao.findByRole(isAdmin);}
    /**
     * 由id查找用户
     */
    public User findUser(int id) {return userDao.findById(id);}
    /**
     * 由用户名uname查找用户
     */
    public User findUser(String uname) {
        return userDao.findUserByName(uname);
    }
    /**
     * 执行登录动作
     * @param user 用户对象。只要提供用户名和密码即可，无需提供id
     * @return 登录的用户
     * @apiNote 本方法不检查是否允许登录，但如密码错误则自动增加登录失败次数
     */
    public User login(User user) {
        User result = userDao.findUserByName(user.getUsername());
        String encrypted = EncryptUtil.encrypt(user.getPassword(), StandardCharsets.ISO_8859_1);
        if (result != null) {
            String uname = user.getUsername();
            if (encrypted.equals(result.getPassword()))
                userDao.clearFailCount(uname);
            else {
                userDao.incrFailCount(uname);
                result = null;
            }
        }
        return result;
    }
    public boolean canLogin(User user) {
        Map.Entry<Integer,Long> result = userDao.getFailCountAndTime(user.getUsername());
        //情况1：失败次数为Null，用户不存在，不能登录
        if (result.getKey() == null)
            return false;
        //情况2：失败时间大于expires，无论之前失败了多少次，都允许登录
        int times = result.getKey();
        long lastFail = result.getValue();
        if(System.currentTimeMillis() - lastFail > LOGIN_EXPIRES) {
            userDao.clearFailCount(user.getUsername());
            return true;
        }
        //情况3：失败时间在expires内，要校验失败次数
        return times < MAX_LOGIN_RETRY;
    }
    /**
     * 注册用户，用于一般用户
     * @param userName 用户名
     * @param pswd 密码。密码必须为ISO 8859-1可编码字符
     */
    public void register(String userName,String pswd) {
        addUser(userName,pswd,false);
    }
    /**
     * 添加用户，用于管理员
     * @param uname 用户名
     * @param pswd 密码
     * @param admin 是否为管理员
     */
    public void addUser(String uname,String pswd,boolean admin) {
        User user = new User();
        user.setUsername(uname);
        user.setPassword(pswd);
        user.setAdmin(admin);
        userDao.add(user);
    }
    /**
     * 注销账户
     * @param uid 要注销的账户id
     */
    public boolean deleteAccount(int uid) {
        try {
            userDao.delete(uid);
            return true;
        } catch (org.springframework.core.NestedRuntimeException e) {
            // 仍有教师任教课程
            return false;
        }
    }

    public int adminCount() {
        return userDao.getAdminCount();
    }
    /**
     * 修改用户密码
     * @param userName 用户名
     * @param before 原密码
     * @param after 新密码
     * @return 是否修改成功
     */
    public boolean modifyPassword(String userName,String before,String after) {
        User user = userDao.findUserByUsernameAndPassword(userName,before);
        if (user == null)
            return false;
        user.setPassword(after);
        userDao.update(user);
        return true;
    }
    /**
     * 在不知道原密码的情况下，修改用户密码
     * @param userName 用户名
     * @param password 密码
     */
    public boolean updateUser(String userName,String password) {
        User user = userDao.findUserByName(userName);
        if (user == null)
            return false;
        user.setPassword(password);
        userDao.update(user);
        return true;
    }
}
