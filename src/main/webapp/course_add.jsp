<%@ page contentType="text/html;charset=UTF-8" import="domain.User,java.util.List" %>
<!--思路：一个是下拉菜单，一个是文本框-->
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style/frames.css">
    <script>
    </script>
</head>
<body>
<% User u = (User) session.getAttribute("user");
    if (u == null||!u.isAdmin()) {
        %><p>您尚未登录！</p>
<%
    } else {
        request.getRequestDispatcher("findusers?type=1").include(request,response);
        List<User> users = (List<User>) session.getAttribute("users");
%>
<form>
    <p>教师：<select id="user-option">
        <% StringBuilder str = new StringBuilder(32);
        for (User user:users) {
            str.setLength(0);
            str.append(String.format("<option value='%d'>%s</option>\n",user.getId(),user.getUsername()));
            %><%=str.toString()%>
        <%
        }
        %></select></p>
    <p>课程名：<input type="text"></p>
    <p><input type="submit" value="提交"></p>
</form>
<%}%>
</body>
</html>
