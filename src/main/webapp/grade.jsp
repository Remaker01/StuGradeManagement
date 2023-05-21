<%@ page import="domain.User,java.util.List,domain.Grade,dao.StudentDao,dao.CourseDao" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <meta http-equiv="Pragma" content="no-cache">
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <title>Title</title>
    <link rel="stylesheet" href="style/frames.css">
</head>
<body>
    <% User user = (User) session.getAttribute("user");
        StudentDao stuDao = new StudentDao();
        CourseDao coDao = new CourseDao();
    if (user == null) {
    %>
    <p>您尚未登录</p>
    <%
    }
    else {
    %>
    <p>以下为查询结果</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>学生</th>
            <th>课程</th>
            <th>分数</th>
        </tr>
        </thead>
        <tbody>
            <% StringBuilder params = new StringBuilder("?");
            String type = request.getParameter("type");
            params.append("type=").append(type);
            String para = request.getParameter("para");
            if (para != null)  params.append("&para=").append(para);
            String pageno = request.getParameter("pageno");
            if (pageno != null) params.append("&pageno=").append(pageno);
            request.getRequestDispatcher("/grade"+params).include(request,response);
            List<Grade> grades = (List<Grade>) session.getAttribute("grades");
            StringBuilder str = new StringBuilder(24);
            for (Grade g:grades) {
            %>
        <tr>
            <% str.setLength(0);
            int sid = g.getStuId();
            str.append("<td>").append(stuDao.findStudentNameById(sid)).append("</td>\n");
            str.append("\t\t<td>").append(coDao.getNameById(g.getCourseId())).append("</td>\n");
            str.append("\t\t<td>").append(g.getScore()).append("</td>\n");
            %><%=str.toString()%>
        </tr>
        </tbody>
        <%
            }
        %>
    </table>
</body>
<%
    }
%>
</html>
<%session.removeAttribute("grades");%>