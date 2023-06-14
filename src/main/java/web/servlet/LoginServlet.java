package web.servlet;

import domain.User;
import service.UserService;
import util.EncryptUtil;
import util.LogUtil;
import util.VerifyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
/**
 * 登录servlet
 */
// ajax请求，带body,默认且仅允许post
/*
1.数据库密码改为MD5+SHA256加密后的结果
2.更新index.html,register.html,user_add.jsp,modipass.html
3.LoginServlet去掉base64部分
4.更新Register与UpdateUser
 */
@WebServlet("/login")
// 访问流程：servlet->service->dao
public class LoginServlet extends HttpServlet {
    private UserService userService = null;
    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //2.获取数据
        //2.1获取用户填写验证码
        response.setHeader("cache-control","no-cache");
        response.setHeader("pragma","no-cache");
        String verifyCode = request.getParameter("verifycode");
        //3.验证码校验
        HttpSession session = request.getSession();
        String checkcode_server = (String)(session.getAttribute("CHECKCODE_SERVER"));
        session.removeAttribute("CHECKCODE_SERVER");//确保验证码一次性
        if (!VerifyUtil.verifyTimestamp(request.getParameter("token"),getClass())) {
            response.sendError(403); //Forbidden
            return;
        }
        if (!checkcode_server.equalsIgnoreCase(verifyCode)) {
            //验证码不正确
            //提示信息
//            request.setAttribute("login_msg", "验证码错误");
            response.getWriter().write("验证码错误");
            //跳转登录页面
//            request.getRequestDispatcher("index.html").forward(request, response);
            LogUtil.log(Level.WARNING,String.format("%s,input_vcode=%s,expected=%s",request.getRemoteAddr(),verifyCode,checkcode_server));
            return;
        }
        Map<String, String[]> map = request.getParameterMap();
        //4.封装User对象
        User user = new User();
        String username = map.get("username")[0];
        String password = map.get("password")[0];
        if(username == null||password == null) {
            response.sendError(400,"至少一个参数缺失或出现错误");
            return;
        }
//        password = EncryptUtil.base64Decode(password);
        user.setUsername(username);
        user.setPassword(password);
        //5.调用Service查询
        User login = userService.login(user);
        //6.判断是否登录成功
        if (login != null) {
            //登录成功
            //将用户存入session
            session.setAttribute("user", login);
            //跳转页面
            response.getWriter().write("登录成功");
            session.setAttribute("login_msg", "登录成功");
        } else {
            //登录失败提示信息
            response.getWriter().write("登录失败,如忘记密码请联系管理员修改");
            session.setAttribute("login_msg", "登录失败");
            //跳转登录页面
//            request.getRequestDispatcher("login.html").forward(request, response);
        }
//        request.getRequestDispatcher("welcome.jsp").forward(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(405); //Method not allowed
    }
}