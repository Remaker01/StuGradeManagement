package web.servlet;

import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
获取用户
type==0:全部用户,type==1:仅非管理员,type==2:仅管理员
 */
@WebServlet("/findusers")
public class FindUsersServlet extends HttpServlet {
    UserService userService = null;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.getWriter().write("您尚未登录");
            return;
        }
        int type;
        List<User> users = new ArrayList<>(0);
        try {
            type = Integer.parseInt(req.getParameter("type"));
        } catch (NumberFormatException e) {
            resp.sendError(400,"参数错误");
            return;
        }
        switch (type) {
            case 0:
                users = userService.findAll();break;
            case 1:
                users = userService.findByRole(false);break;
            case 2:
                users = userService.findByRole(true);break;
            default:
                resp.sendError(400,"参数错误");return;
        }
        req.getSession().setAttribute("users",users);
    }
}
