package web.servlet;
import service.UserService;
import util.VerifyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 更新用户信息。参数：id:待修改的用户，uname:用户名，old_pswd:旧密码，new_pswd:新密码。
//带body，用post
@WebServlet("/updateuser")
public class UpdateUserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().write("<p>您尚未登录！</p>");
            return;
        }
        String uname,oldPswd,newPswd;
        try {
            uname = req.getParameter("uname");
            oldPswd = req.getParameter("old");
            newPswd = req.getParameter("new");
        } catch (NumberFormatException e) {
            resp.sendError(400,"至少一个参数缺失或错误");
            return;
        }
        // 这是为了检查用户是否存在的，若不存在，则返回
        if(req.getSession().getAttribute("user") == null) {
            resp.getWriter().write("用户未登录");
            return;
        }
        String newPswdOriginal = newPswd.substring(32);
        newPswd = newPswd.substring(0,32);
        // 检查密码是否符合强度要求
        if (!VerifyUtil.verifyPassword(newPswdOriginal)) {
            resp.getWriter().write(
                    String.format("密码强度不合要求，要求必须不少于%d位且不大于%d位", VerifyUtil.PASS_MIN_LEN, VerifyUtil.PASS_MAX_LEN)
            );
            return;
        }
        if(!userService.modifyPassword(uname,oldPswd,newPswd)) {
            resp.getWriter().write("更新失败！原密码错误。");
        } else {
            resp.getWriter().write("更新成功！");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(405);
    }
}
