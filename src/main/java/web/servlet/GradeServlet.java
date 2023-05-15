package web.servlet;

import service.GradeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
请求参数：
1.
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
        //TODO:implement this
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
