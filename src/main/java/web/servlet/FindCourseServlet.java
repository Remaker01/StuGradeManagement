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

// 逻辑：检测是否存在userid参数，如果有就返回对应user的课程，否则返回全部
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
}
