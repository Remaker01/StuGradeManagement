<%@ page import="domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <title>修改密码</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
</head>
<body>
<% User user = (User) session.getAttribute("user");
if (user == null) {
    out.print("您尚未登录！");
}
else {
%>
    <form>
        <label id="uname">当前用户：<%=user.getUsername()%></label>
        <p>当前密码 <input type="password" id="pass-old"/></p>
        <p>新密码 <input type="password" id="pass-new"/></p>
        <input type="button" onclick="submit_updateuser(<%=user.getId()%>,'<%=user.getUsername()%>','pass-old','pass-new')" value="修改">
        <p id="status" style="font-size: small;color:red;"></p>
    </form>
<%
    }
%>
</body>
</html>