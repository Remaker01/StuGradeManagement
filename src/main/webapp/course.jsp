<%@ page import="java.util.List,domain.Course" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Title</title>
    <style>
        .main-table {
            border: 1px solid #999999;
            border-collapse: collapse;
            width: 80%;
        }
        .main-table tbody tr{text-align: center;height: 30px;}
        .main-table tbody tr:hover {background-color: #dddddd;}
        .main-table tr:nth-child(even){background-color: #f2f2f2;}
        .main-table th, .main-table tr td {border: 1px solid #aaaaaa;}
    </style>
</head>
<body>
    <p>以下为查询结果</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>编号</th>
            <th>课程名</th>
            <th>课程性质</th>
        </tr>
        </thead>
        <tbody>
        <%  String uid = request.getParameter("userid");
            request.getRequestDispatcher("/findcourse?userid="+uid).include(request,response);
            List<Course> courses = (List<Course>) request.getSession().getAttribute("courses");
            StringBuilder str = new StringBuilder(32);
            for (Course c:courses) {
                %>
        <tr>
        <%  str.setLength(0);
            str.append("<td>").append(c.getId()).append("</td>\n");
            str.append("\t\t<td>").append(c.getCname()).append("</td>\n");
            str.append("\t\t<td>").append(c.getCtype()).append("</td>\n");
    %><%=str.toString()%></tr>
        <%
            }
        %>
        </tbody>
</table>
</body>
</html>
<%request.getSession().setAttribute("courses",null);%>