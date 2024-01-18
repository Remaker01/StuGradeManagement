package web.servlet;

import domain.Course;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import service.CourseService;
import util.LogUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// 逻辑：type==0:检测是否存在userid参数，如果有就返回对应user的课程，否则返回全部;type==1:通过课程id查找课程，要求必须有id
// 不带body,带params，默认get
@WebServlet("/course")
@Component
public class FindCourseServlet extends HttpServlet {
    @Autowired
    private CourseService courseService = null;
    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,conf.getServletContext());
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.setHeader("Content-type","text/html");
            resp.getWriter().write("<p>您尚未登录！</p>");
            return;
        }
        int type;
        try {
            type = Integer.parseInt(req.getParameter("type"));
        } catch (NumberFormatException e) {
            resp.sendError(400,"参数type错误");
            return;
        }
        if (type == 0) {
            String pagenoStr = req.getParameter("pageno");
            if (pagenoStr == null||pagenoStr.isEmpty())
                pagenoStr = "1";
            int pageno;
            try{
                pageno = Integer.parseInt(pagenoStr);
            } catch (NumberFormatException e) {
                resp.sendError(400,"参数pageno错误");
                return;
            }
            int userid;
            try {
                userid = Integer.parseInt(req.getParameter("userid"));
            } catch (NumberFormatException e) {
                List<Course> allCourses = courseService.getCourses(pageno);
                req.getSession().setAttribute("courses",allCourses);
                return;
            }
            List<Course> courses = courseService.getCoursesByTeacherId(userid,pageno);
            req.getSession().setAttribute("courses",courses);
        }
        else if (type == 1){
            Course course;
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                course = courseService.getByCourseId(id);
            } catch (RuntimeException e) {
                resp.sendError(400,"参数id错误");
                return;
            }
            req.getSession().setAttribute("course",course);
        }
        else {
            resp.sendError(400,"参数type错误");
        }
    }// type==0:增，type==1:删，type==2:改
    //type==0:name,tid,prop:用户id,课程名,教师id，课程属性(类型)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (u == null||!u.isAdmin()) {
            resp.setHeader("Content-type","text/html");
            resp.getWriter().write("<p>您尚未登录！</p>");
            return;
        }
        int type;
        try {
            type = Integer.parseInt(req.getParameter("type"));
        } catch (NumberFormatException e) {
            resp.sendError(400,"类型非法");
            return;
        }
        switch (type) {
            case 0:
                try {
                    String cname = req.getParameter("name");
                    int tid = Integer.parseInt(req.getParameter("tid"));
                    String ctype = req.getParameter("prop");
                    courseService.addCourse(cname,ctype,tid);
                } catch (RuntimeException e) {
                    LogUtil.log(e);
                    resp.sendError(400,"至少一个参数缺失或错误");
                    return;
                }
                break;
            case 1:
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    courseService.deleteCourse(id);
                } catch (NumberFormatException e) {
                    resp.sendError(400,"参数id错误");
                    return;
                } catch (RuntimeException e) {
                    resp.getWriter().write("删除失败，请确认该");
                }
                break;
            case 2:
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    String cname = req.getParameter("name");
                    String ctype = req.getParameter("prop");
                    int tid = Integer.parseInt(req.getParameter("tid"));
                    Course course = new Course();
                    course.setId(id);
                    course.setCname(cname);
                    course.setCtype(ctype);
                    course.setTeacher(tid); //没用？
                    courseService.updateCourse(course);
                } catch (RuntimeException e) {
                    resp.sendError(400,"至少一个参数缺失或错误");
                    return;
                }
                break;
            default:resp.sendError(400,"类型非法");return;
        }
        resp.getWriter().write("操作成功！");
    }
}
