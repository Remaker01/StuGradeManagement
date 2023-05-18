<%@ page import="domain.Student,java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%--TODO:分页查询，按条件查询--%>
<html>
<head>
    <title>Title</title>
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <link rel="stylesheet" href="style/frames.css">
</head>
<body>
    <p>以下为查询到的学生信息</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>地址</th>
            <th>联系方式</th>
        </tr>
        </thead>
        <tbody>
        <%  request.getRequestDispatcher("/student").include(request,response);
            List<Student> students = (List<Student>) session.getAttribute("students");
            StringBuilder str = new StringBuilder(48);
            for (Student c:students) {
        %>
        <tr>
            <%  str.setLength(0);
                str.append("<td>").append(c.getId()).append("</td>\n");
                str.append("\t\t<td>").append(c.getSname()).append("</td>\n");
                str.append("\t\t<td>").append(c.getAge()).append("</td>\n");
                str.append("\t\t<td>").append(c.getAddress() == null ? "" : c.getAddress()).append("</td>\n");
                str.append("\t\t<td>").append(c.getPhone() == null ? "" : c.getPhone()).append("</td>\n");
            %><%=str.toString()%></tr>
        <%
            }
        %>
        </tbody>
    </table>
    <div style="text-align:right;">当前第1页</div> <!--TODO:分页-->
</body>
</html>
<%session.setAttribute("students",null);%>