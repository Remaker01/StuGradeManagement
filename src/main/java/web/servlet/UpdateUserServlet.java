package web.servlet;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import service.UserService;
import util.EncryptUtil;
import util.VerifyUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 更新用户信息。参数：id:待修改的用户，uname:用户名，old_pswd:旧密码，new_pswd:新密码。
//带body，用post
@WebServlet("/updateuser")
@Component
public class UpdateUserServlet extends HttpServlet {
    @Autowired
    private UserService userService;

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,conf.getServletContext());
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 这是为了检查用户是否存在的，若不存在，则返回
        User u = (User) req.getSession().getAttribute("user");
        if (u == null) {
            resp.getWriter().write("<p>您尚未登录！</p>");
            return;
        }
        String uname = req.getParameter("uname"),oldPswd,newPswd;
        oldPswd = EncryptUtil.base64Decode(req.getParameter("old"));
        newPswd = EncryptUtil.base64Decode(req.getParameter("new"));
        if (!VerifyUtil.verifyPassword(newPswd)) {
            resp.sendError(400,"密码不合法");
            return;
        }
        // 检查密码是否符合强度要求
        if (u.getUsername().equals(uname)) { //管理员只可通过修改密码页面改自己的密码
            if (userService.modifyPassword(uname, oldPswd, newPswd)) {
                resp.getWriter().write("更新成功！");
            } else {
                resp.getWriter().write("更新失败！用户名或原密码错误。");
            }
        }
        else {
            if(userService.updateUser(uname,newPswd))
                resp.getWriter().write("更新成功！");
            else
                resp.getWriter().write("更新失败！用户名错误。");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(405);
    }
}
