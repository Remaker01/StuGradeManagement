package web.servlet;

import service.UserService;
import util.LogUtil;
import util.VerifyUtil;

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
public class RegisterServlet extends HttpServlet {
    /*
    检查密码
    要求：长度至少7位，至多30位
     */
    private UserService userService;
    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
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
            //跳转登录页面
//            request.getRequestDispatcher("index.html").forward(request, response);
            LogUtil.log(Level.WARNING,String.format("(Register)ip=%s,input_vcode=%s,expected=%s",req.getRemoteAddr(),verifyCode,checkcode_server));
            return;
        }
        //开始调用注册服务注册
        String uname = req.getParameter("username"),pswd = req.getParameter("password"),role = req.getParameter("role");
        if (uname == null||pswd == null) {
            resp.sendError(400,"至少一个参数缺失或出现错误");
            return;
        }
        if (userService.findUser(uname) != null) {
            resp.getWriter().write("用户已存在");
        }
        else if (!VerifyUtil.verifyPassword(pswd)) {
            resp.getWriter().write(
                    String.format("密码强度不合要求，要求必须不少于%d位且不大于%d位",VerifyUtil.PASS_MIN_LEN,VerifyUtil.PASS_MAX_LEN)
            );
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
