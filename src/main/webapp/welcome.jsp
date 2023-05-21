<%@ page import="domain.User,util.LogUtil,java.util.logging.Level" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <meta http-equiv="Pragma" content="no-cache">
    <%User u = (User) session.getAttribute("user");
    String loginMsg = (String) session.getAttribute("login_msg");
    boolean isAdmin;
    if (u==null||loginMsg==null||loginMsg.endsWith("失败")) {
        response.sendError(500,"用户或登录信息为空");
        return;
    } else {
        LogUtil.log(Level.INFO, u.getUsername());
        isAdmin = u.isAdmin();
    String redirect = isAdmin ? "admin.jsp" : "teacher.jsp";
    %>
    <title>欢迎页面</title>
    <meta http-equiv="refresh" content="1;url=<%=redirect%>">
</head>
<body>
<p>欢迎<%=(isAdmin ? "管理员" : "教师") + u.getUsername()%>登录</p>
<%--<p>您的权限有：学生信息查询、学生成绩查询、课程查询、学生成绩登记、修改与删除</p>--%>
<%--<%if (isAdmin) {%>--%>
<%--<p>课程、学生、用户的增、删、改操作</p>--%>
<%--<%}%>--%>
<a href="<%=redirect%>">点击跳转</a>
    <%
    }
    %>
</body>
</html>
