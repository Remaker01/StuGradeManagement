package service;

import dao.UserDao;
import domain.User;

import java.util.List;

public class UserService {
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
     */
    public User login(User user) {
        return userDao.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
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
    public void deleteAccount(int uid) {
        userDao.delete(uid);
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
}
