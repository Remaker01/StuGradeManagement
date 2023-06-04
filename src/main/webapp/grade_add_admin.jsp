<%@ page import="domain.User,java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <style>
        form select {padding: 2px 4px;}
    </style>
</head>
<body>
    <%  User u = (User) session.getAttribute("user");
        if (u == null||!u.isAdmin()) {
            %><p>您尚未登录！</p>
    <%
        }else {%>
    <form>
        <p>
            尊敬的管理员<%=u.getUsername()%><br>请选择要添加成绩的教师：<select id="user-option">
            <%  request.getRequestDispatcher("findusers?type=1").include(request,response);
                List<User> users = (List<User>) session.getAttribute("users");
                StringBuilder str = new StringBuilder(32);
                for (User user:users) {
                    str.setLength(0);
                    str.append(String.format("<option value='%d'>",user.getId())).append(user.getUsername()).append("</option>\n");
            %><%=str.toString()%>
            <%
                }
            %></select>
        </p>
        <input type="button" value="提交" onclick="document.location.href=_root_+'grade_add.jsp?uid='+$('#user-option').val()"
         style="padding: 3px 6px;"/>
    </form>
<%}%>
</body>
</html>
<%session.removeAttribute("users");%>