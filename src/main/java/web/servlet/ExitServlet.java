package web.servlet;

import domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exit")
//不带body,故默认使用get
public class ExitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println(req.getContextPath());
        if (user == null) {
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();
            writer.write("<html><head>");
            writer.write(String.format("<script>alert('您尚未登录');document.location.href='%s';</script>",req.getContextPath()));
            writer.write("</head></html>");
            return;
        }
        session.removeAttribute("user");
        resp.sendRedirect("./");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
