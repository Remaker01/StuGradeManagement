package web.servlet;

import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import service.UserService;
import util.LogUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//参数：id:待删除用户
@WebServlet("/deluser")
@Component
public class DelUserServlet extends HttpServlet {
    @Autowired
    private UserService userService;
    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,conf.getServletContext());
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (u == null||!u.isAdmin()) {
            resp.getWriter().write("您尚未登录！");
            return;
        }
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if (id == u.getId())
                req.getSession().removeAttribute("user");
            if (!userService.deleteAccount(id)) {
                resp.getWriter().write("删除失败，请确认该教师未任教任何课程！");
            } else {
                resp.getWriter().write(String.format("%d号用户删除成功",id));
            }
        } catch (NumberFormatException e) {
            LogUtil.log(e);
            resp.sendError(400,"参数错误");
        }
    }
}
