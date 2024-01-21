<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<c:choose><c:when test="${sessionScope.user eq null}">
    <p>您尚未登录！</p>
</c:when><c:otherwise>
    <form>
        <p>尊敬的管理员${sessionScope.user.username}<br>请选择要添加成绩的教师：<select id="user-option" class="form-control" style="width: 200px;display: inline">
            <%request.getRequestDispatcher("findusers?type=1").include(request,response);%>
            <c:forEach items="${sessionScope.users}" var="u"><option value="${u.id}">${u.username}</option></c:forEach>
        </select>
        </p>
        <input type="button" value="提交" onclick="document.location.href=_root_+'grade_add.jsp?uid='+$('#user-option').val()"
         class="btn btn-primary"/>
    </form>
</c:otherwise>
</c:choose>
</body>
</html>
<%session.removeAttribute("users");%>