package web.servlet;
import service.UserService;
import util.VerifyUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 更新用户信息。参数：id:待修改的用户，uname:用户名，old_pswd:旧密码，new_pswd:新密码。注意更新完就退出，要求用新密码重新登录
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
        int id;
        String uname,oldPswd,newPswd;
        try {
            id = Integer.parseInt(req.getParameter("id"));
            uname = req.getParameter("uname");
            oldPswd = req.getParameter("old");
            newPswd = req.getParameter("new");
        } catch (NumberFormatException e) {
            resp.sendError(400,"至少一个参数缺失或错误");
            return;
        }
        // 参数id是为了检查用户是否存在的，若不存在，则返回
        if(userService.findUser(id) == null) {
            resp.getWriter().write("用户不存在");
            return;
        }
        // 检查密码是否符合强度要求
        if (!VerifyUtil.verifyPassword(newPswd)) {
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
