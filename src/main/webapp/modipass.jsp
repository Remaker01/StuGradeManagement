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
    <style>
        .i2{border-radius: 5px 5px 5px 5px;height: 20px;width: 200px;}
    </style>
</head>
<body>
<% User user = (User) session.getAttribute("user");
if (user == null) {
    out.print("您尚未登录！");
}
else {
%>
    <form>
        <label id="uname">当前用户：<%=request.getParameter("uname")%></label>
        <p>当前密码：<input type="password" id="pass-old" class="i2"/></p>
        <p>新密码： <input type="password" id="pass-new" class="i2" /></p>
        <input type="button" onclick="submit_updateuser('<%=request.getParameter("uname")%>','pass-old','pass-new')" value="修改" style="padding: 3px 6px;">
        <p id="status" style="font-size: small;color:red;"></p>
    </form>
<%
    }
%>
</body>
</html>