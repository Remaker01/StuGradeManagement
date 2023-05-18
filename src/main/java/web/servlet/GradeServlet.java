package web.servlet;

import domain.Grade;
import domain.User;
import service.GradeService;

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
    1.全部
2.para:参数，按不同type:
    2.课程名
    3.教师id&page
    4.学生名，目前忽略page
3.pageno:目前只是type==1时有用

post请求参数：
1.type，含义同StudentServlet
2.courseid及id
3.score:分数，删除时忽略
 */
@WebServlet("/grade")
public class GradeServlet extends HttpServlet {
    private GradeService gradeService = null;

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
            case 1: //按课程查询
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
        int type,courseId,sid;
        try {
            type = Integer.parseInt(req.getParameter("type"));
            courseId = Integer.parseInt(req.getParameter("courseid"));
            sid = Integer.parseInt(req.getParameter("sid"));
        } catch (NumberFormatException e) {
            resp.sendError(400,"参数错误");
            return;
        }
        switch (type) {
            case 0:
                try {
                    short score = Short.parseShort(req.getParameter("score"));
                    gradeService.addGrade(sid,courseId,score);
                } catch (NumberFormatException e) {
                    resp.sendError(400, "参数错误");
                    break;
                }
                break;
            case 1:
                gradeService.deleteGrade(sid, courseId);
                break;
            case 2:
                try {
                    short score = Short.parseShort(req.getParameter("score"));
                    Grade g = new Grade();
                    g.setStuId(sid);
                    g.setCourseId(courseId);
                    g.setScore(score);
                    gradeService.updateGrade(g);
                } catch (NumberFormatException e) {
                    resp.sendError(400, "参数错误");
                    break;
                }
            default: resp.sendError(400, "参数错误");break;
        }
    }
}
