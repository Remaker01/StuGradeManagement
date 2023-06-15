<%@ page import="domain.User,java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="http://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="style/frames.css">
</head>
<body>
    <%  User u = (User) session.getAttribute("user");
        if (u == null||!u.isAdmin()) {
            %><p>您尚未登录！</p>
    <%
        }else {%>
    <form>
        <p>
            尊敬的管理员<%=u.getUsername()%><br>请选择要添加成绩的教师：<select id="user-option" class="form-control" style="width: 200px;display: inline">
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
         class="btn btn-primary"/>
    </form>
<%}%>
</body>
</html>
<%session.removeAttribute("users");%>