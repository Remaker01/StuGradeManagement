package web.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import service.UserService;
import util.EncryptUtil;
import util.LogUtil;
import util.VerifyUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;

/*
注册要求的参数：用户名、密码、验证码、是否为管理员。不校验时间戳
注册流程：
1.校验用户名密码，如果用户名被用过或密码长度过短，则注册失败
2.调用服务注册用户
*/
@WebServlet("/register")
@Component
public class RegisterServlet extends HttpServlet {
    @Autowired
    private UserService userService;
    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,conf.getServletContext());
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("cache-control","no-cache");
        resp.setHeader("pragma","no-cache");
        String verifyCode = req.getParameter("verifycode");
        //3.验证码校验
        HttpSession session = req.getSession();
        String checkcode_server = (String)(session.getAttribute("CHECKCODE_SERVER"));
        session.removeAttribute("CHECKCODE_SERVER");//确保验证码一次性
        if (!checkcode_server.equalsIgnoreCase(verifyCode)) {
            //验证码不正确
            //提示信息
//            request.setAttribute("login_msg", "验证码错误");
            resp.getWriter().write("验证码错误");
            return;
        }
        //开始调用注册服务注册
        String uname = req.getParameter("username"),pswd = req.getParameter("password"),role = req.getParameter("role");
        if (uname == null||pswd == null) {
            resp.sendError(400,"至少一个参数缺失");
            return;
        }
        pswd = EncryptUtil.base64Decode(pswd);
        if (!VerifyUtil.verifyPassword(pswd)) {
            resp.sendError(400,"密码不合法");
            return;
        }
        if (userService.findUser(uname) != null) {
            resp.getWriter().write("用户已存在");
        }
        else {
            if (role == null||role.equalsIgnoreCase("user"))
                userService.register(uname, pswd);
            else if (role.equalsIgnoreCase("admin")) {
                if (userService.adminCount() >= 10) {
                    resp.getWriter().write("管理员人数已达上限10");
                    return;
                }
                userService.addUser(uname, pswd, true);
            }
            else {
                resp.sendError(400,"参数错误");
                return;
            }
            resp.getWriter().write("注册成功,请将注册信息告知管理员");
        }
    }
}
