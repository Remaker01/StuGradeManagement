package domain;

import java.util.Arrays;
import java.util.Objects;

public class User {
    private int id;
    private String username,password;
    private boolean isAdmin;
    private int errCount; //总感觉这个没必要

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getErrCount() {
        return errCount;
    }
    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{id, username, password}); //用户属性一旦确定不能修改
    }
}
