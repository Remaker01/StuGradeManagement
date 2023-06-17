package web.servlet;

import domain.Course;
import service.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// 逻辑：type==0:检测是否存在userid参数，如果有就返回对应user的课程，否则返回全部;type==1:通过课程id查找课程，要求必须有id
// 不带body,带params，默认get
@WebServlet("/findcourse")
public class FindCourseServlet extends HttpServlet {
    private CourseService courseService = null;
    @Override
    public void init() throws ServletException {
        super.init();
        courseService = new CourseService();
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
            int userid;
            try {
                userid = Integer.parseInt(req.getParameter("userid"));
            } catch (NumberFormatException e) {
                List<Course> allCourses = courseService.getAllCourses();
                req.getSession().setAttribute("courses",allCourses);
                return;
            }
            List<Course> courses = courseService.getCoursesByTeacherId(userid);
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
    }
}
