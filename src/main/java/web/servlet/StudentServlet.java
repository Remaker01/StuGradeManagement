package web.servlet;
// 应当支持增加、查询、修改、删除学生信息。增加、修改、删除对应的数码分别为0、1、2
/*
参数
1. 若为get方法则认为是查询信息。需校验用户是否已登录，否则拒绝查询。
    1.页数：pageno(可选，默认为1)
    2.学生姓名：name(可选)
2. 若post方法认为是要修改信息，参数为1.type,2.学生id(删除)学生的各项信息(其他)。需要校验是否为管理员。
 */
import domain.Student;
import domain.User;
import service.StudentService;
import util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//TODO:像StudentServlet这样整合用户有关的servlet
@WebServlet("/student")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = null;

    @Override
    public void init() throws ServletException {
        super.init();
        studentService = new StudentService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO:添加更多参数选项
        if (req.getSession().getAttribute("user") == null) {
            resp.sendError(403);
            return;
        }
        String name = req.getParameter("name"),pagenoStr = req.getParameter("pageno");
        List<Student> students;
        if (pagenoStr == null)
            pagenoStr = "1";
        //name为null
        if (name == null) {
            try {
                students = studentService.findStudents(Integer.parseInt(pagenoStr));
            } catch (NumberFormatException e) {
                resp.sendError(400,"参数错误");
                LogUtil.log(e);
                return;
            }
        }
        //name不为null
        else {
            try {
                students = studentService.findStudentsByName(name,Integer.parseInt(pagenoStr));
            } catch (NumberFormatException e) {
                resp.sendError(400,"参数错误");
                LogUtil.log(e);
                return;
            }
        }
        req.getSession().setAttribute("students", students);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null||!user.isAdmin()) {
            resp.sendError(403);
            return;
        }
        int type;
        Map<String, String[]> paraMap = req.getParameterMap();
        try {
            type = Integer.parseInt(paraMap.get("type")[0]);
        } catch (RuntimeException e) {
            resp.sendError(400,"参数错误");
            return;
        }
        switch (type) {
            case 0:addStudent(paraMap,req,resp);break;
            case 1:deleteStudent(paraMap.get("id")[0],resp);break;
            case 2:modifyStudent(paraMap,req,resp);
            default:resp.sendError(400,"参数错误");break;
        }
    }
    //paraMap需要除id外的各项信息
    private void addStudent(Map<String, String[]> paraMap,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String [] infos = new String[6];
        try {
            infos[0] = paraMap.get("sname")[0];
            infos[1] = paraMap.get("age")[0];
            infos[2] = paraMap.get("gender")[0];
            infos[3] = paraMap.get("address")[0];
            infos[4] = paraMap.get("phone")[0];
            infos[5] = paraMap.get("qq")[0];
        } catch (RuntimeException e) {
            resp.sendError(400);
            return;
        }
//        for (int i = 0; i < infos.length; i++) {
//            if (infos[i].length() == 0)
//                infos[i] = null;
//        }
        Student student = new Student();
        student.setSname(infos[0]);
        student.setAge(Short.parseShort(infos[1]));
        student.setGender(infos[2]);
        student.setAddress(infos[3]);
        student.setPhone(infos[4]);
        student.setQQ(infos[5]);
        studentService.addStudent(student);
    }

    private void deleteStudent(String id, HttpServletResponse resp) throws IOException {
        try {
            int i = Integer.parseInt(id);
            studentService.delStudent(i);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        }
    }
    //paraMap需要学生的所有信息，包括id
    private void modifyStudent(Map<String, String[]> paraMap,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String [] infos = new String[7];
        try {
            infos[0] = paraMap.get("sname")[0];
            infos[1] = paraMap.get("age")[0];
            infos[2] = paraMap.get("gender")[0];
            infos[3] = paraMap.get("address")[0];
            infos[4] = paraMap.get("phone")[0];
            infos[5] = paraMap.get("qq")[0];
            infos[6] = paraMap.get("id")[0];
        } catch (RuntimeException e) {
            resp.sendError(400);
            return;
        }
        Student student = new Student();
        student.setSname(infos[0]);
        student.setAge(Short.parseShort(infos[1]));
        student.setGender(infos[2]);
        student.setAddress(infos[3]);
        student.setPhone(infos[4]);
        student.setQQ(infos[5]);
        student.setId(Integer.parseInt(infos[6]));
        studentService.update(student);
    }
}