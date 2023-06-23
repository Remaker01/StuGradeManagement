package web.servlet;

import domain.Course;
import domain.User;
import service.CourseService;
import util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updatecourse")
// type==0:增，type==1:删，type==2:改
//type==0:name,tid,prop:用户id,课程名,教师id，课程属性(类型)
public class UpdateCourseServlet extends HttpServlet {
    CourseService courseService = null;

    @Override
    public void init() throws ServletException {
        super.init();
        courseService = new CourseService();
    }
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
