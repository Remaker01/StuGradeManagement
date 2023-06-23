package web.servlet;

import domain.Course;
import domain.Grade;
import domain.User;
import service.CourseService;
import service.GradeService;
import util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
get请求参数：
1.type:按什么查询:
2.para:参数，按不同type:
    0.全部
    1.课程名
    2.教师id&page
    3.学生名，目前忽略page
    4.课程名
3.pageno:目前只是type==0时有用
post请求参数：
1.type，含义同StudentServlet
2.courseid sid tid
3.score:分数，删除时忽略
 */
@WebServlet("/grade")
public class GradeServlet extends HttpServlet {
    private GradeService gradeService = null;
    private CourseService courseService = null;

    @Override
    public void init() throws ServletException {
        super.init();
        gradeService = new GradeService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.sendError(403);
            return;
        }
        int type;
        List<Grade> grades = new ArrayList<>(1);
        String para = req.getParameter("para"),pageno = req.getParameter("pageno");
        try {
            type = Integer.parseInt(req.getParameter("type"));
        } catch (NumberFormatException e) {
            resp.sendError(400,"参数错误");
            return;
        }
        switch (type) {
            case 0:
                if (pageno == null)
                    pageno = "1";
                try {
                    grades = gradeService.getGrades(Integer.parseInt(pageno));
                } catch (NumberFormatException e) {
                    resp.sendError(400,"参数错误");
                    break;
                }
                break;
            case 4: //按课程名查询
                 if (para == null) {
                     resp.sendError(400,"参数错误");
                     break;
                 }
                 grades = gradeService.getGradeByCourseName(para);
                 break;
            case 2:
//                if (para == null) {
//                    resp.sendError(400);
//                    break;
//                }
                try {
                    grades = gradeService.getGradesByTeacherId(Integer.parseInt(para));
                } catch (NumberFormatException e) {
                    resp.sendError(400,"参数错误");
                    break;
                }
                break;
            case 3:
                if (para == null) {
                    resp.sendError(400,"参数错误");
                    break;
                }
                grades = gradeService.getGradesByStudentName(para);
                break;
            case 1: //按课程id查询，这个比上面的4可能更合理
                try {
                    int cid = Integer.parseInt(para);
                    grades = gradeService.getGradesByCourseId(cid);
                    resp.setHeader("Content-type","application/json");
                    resp.getWriter().write(String.format("{\"cid\":%d,\"result\":%d}",cid,grades.size())); //json
                    break;
                } catch (NumberFormatException e) {
                    resp.sendError(400,"参数错误");
                    return;
                }
            default:resp.sendError(400,"参数错误");break;
        }
        req.getSession().setAttribute("grades",grades);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendError(403);
            return;
        }
        int type,courseId,sid,tid;
        try {
            type = Integer.parseInt(req.getParameter("type"));
            courseId = Integer.parseInt(req.getParameter("courseid"));
            sid = Integer.parseInt(req.getParameter("sid"));
            tid = Integer.parseInt(req.getParameter("tid"));
        } catch (NumberFormatException e) {
            resp.sendError(400,"参数错误");
            LogUtil.log(e);
            return;
        }
        if (courseService == null)
            courseService = new CourseService();
        Course c = courseService.getByCourseId(courseId);
        if (c == null) {
            resp.getWriter().write("课程不存在");
            return;
        }
        if (c.getTeacher() != tid&&!user.isAdmin()) {
            resp.getWriter().write("教师与课程不匹配");
            return;
        }
        switch (type) {
            case 0:
                try {
                    short score = Short.parseShort(req.getParameter("score"));
                    if(score >= 0&&score <= 100)
                        gradeService.addGrade(sid,courseId,score);
                    else {
                        resp.getWriter().write("添加失败，分数不合法，必须不小于0且不大于100");
                        return;
                    }
                } catch (NumberFormatException e) {
                    resp.sendError(400, "参数错误");
                    return;
                }
                break;
            case 1:
                gradeService.deleteGrade(sid, courseId);
                break;
            case 2:
                try {
                    short score = Short.parseShort(req.getParameter("score"));
                    if (score >= 0&&score <= 100) {
                        Grade g = new Grade();
                        g.setStuId(sid);
                        g.setCourseId(courseId);
                        g.setScore(score);
                        if (gradeService.updateGrade(g) == 0) {
                            resp.getWriter().write("修改失败");
                            return;
                        }
                    }
                    else {
                        resp.getWriter().write("修改失败，分数不合法，必须大于0且小于100");
                        return;
                    }
                } catch (NumberFormatException e) {
                    resp.sendError(400, "参数错误");
                    return;
                }
                break;
            default: resp.sendError(400, "参数错误");return;
        }
        resp.getWriter().write("操作成功");
    }
}
