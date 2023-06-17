<%@ page import="java.util.List,domain.Course,domain.User,dao.UserDao" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--TODO:分页查询，按条件查询；查找教师姓名不再使用UserDao--%>
<html>
<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <title>Title</title>
    <link rel="stylesheet" href="style/frames.css">
</head>
<body>
    <% User u = (User) session.getAttribute("user");
    UserDao userDao = new UserDao();
    if (u == null) {
        %><p>您尚未登录！</p>
    <% } else {
    %><p>以下为查询结果</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>编号</th>
            <th>课程名</th>
            <th>课程性质</th>
            <% if(u.isAdmin()) {
            %><th>任课教师</th>
            <%
            }
            %></tr>
        </thead>
        <tbody>
    <%  String uid = request.getParameter("userid");
        request.getRequestDispatcher("/findcourse?type=0&userid="+uid).include(request,response);
        List<Course> courses = (List<Course>) session.getAttribute("courses");
        StringBuilder str = new StringBuilder(32);
        for (Course c:courses) {
    %>
    <tr>
        <%  str.setLength(0);
            str.append("<td>").append(c.getId()).append("</td>\n");
            str.append("\t\t<td>").append(c.getCname()).append("</td>\n");
            str.append("\t\t<td>").append(c.getCtype()).append("</td>\n");
            if (u.isAdmin()) {
                str.append("\t\t<td>").append(userDao.findById(c.getTeacher()).getUsername()).append("</td>\n");
            }
        %><%=str.toString()%></tr>
    <%
        }
    %></tbody>
    </table>
<%}%>
</body>
</html>
<%session.setAttribute("courses",null);%>