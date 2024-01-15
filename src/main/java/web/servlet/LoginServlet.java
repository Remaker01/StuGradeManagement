package web.servlet;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import domain.User;
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
import java.util.Map;
import java.util.logging.Level;
/**
 * 登录servlet
 */
// ajax请求，带body,默认且仅允许post
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
        //5.判断是否允许登录
        if (!userService.canLogin(user)) {
            response.getWriter().write(String.format("您的密码错误次数达到上限:%d,请等%d分钟后重试",UserService.MAX_LOGIN_RETRY,UserService.LOGIN_EXPIRES/1000/60));
            return;
        }
        //6.调用Service查询
        User login = userService.login(user);
        //7.判断是否登录成功
        if (login != null) {
            //登录成功
            //将用户存入session
            session.setAttribute("user", login);
            //跳转页面
            response.getWriter().write("登录成功");
//            session.setAttribute("login_msg", "登录成功");
        } else {
            //登录失败提示信息
            response.getWriter().write("登录失败,如忘记密码请联系管理员修改");
//            session.setAttribute("login_msg", "登录失败");
            //跳转登录页面
        }
    }
    //get方法默认为返回已经登录的用户
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("cache-control","no-cache");
        User u = (User)request.getSession().getAttribute("user");
        JsonGenerator generator = new JsonFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
        generator.writeStartObject();generator.writeBooleanField("infoValid",u != null);
        if(u != null) {
            generator.writeObjectFieldStart("userInfo");
            generator.writeNumberField("id",u.getId());
            generator.writeStringField("username",u.getUsername());
            generator.writeBooleanField("admin",u.isAdmin());
        } else {
            generator.writeNullField("userInfo");
        }
        generator.writeEndObject();
        response.setHeader("Content-type","application/json");
        generator.close();
    }
}