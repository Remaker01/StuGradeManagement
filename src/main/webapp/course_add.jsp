<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!--思路：一个是下拉菜单，一个是文本框-->
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="style/frames.css">
    <script>
        function submit_() {
            var tid=$("#user-option").val(),cname=$("#name-text").val();
            var ctype=$("#type-option").val();
            var param="type=0&name="+cname+"&tid="+tid+"&prop="+ctype;
            $.ajax({
                url:_root_+"course",type:"post",data:param,processData:false,success:function (d) {$("#status").text(d);delayedReload(500);}
            });
            return FALSE;
        }
    </script>
    <style>select.form-control {width: 200px;display: inline;}</style>
</head>
<body>
<c:choose>
    <c:when test="${(sessionScope.user eq null) or (not sessionScope.user.admin)}">
    <p>您尚未登录！</p>
    </c:when><c:otherwise>
    <%request.getRequestDispatcher("findusers?type=1").include(request,response);%>
    <form onsubmit="return submit_()">
        <p>教师：<select id="user-option" class="form-control">
            <c:forEach items="${sessionScope.users}" var="u">
                <option value="${u.id}">${u.username}</option>
            </c:forEach>
        </select></p>
        <p>课程类型：<select id="type-option" class="form-control">
            <option value="公共课">公共课</option>
            <option value="专业基础课">专业基础课</option>
            <option value="专业课">专业课</option>
            <option value="选修课">选修课</option>
        </select></p>
        <p>课程名：<input type="text" id="name-text" required autocomplete="false"></p>
        <p><input type="submit" value="提交" class="btn btn-primary"></p>
        <p id="status" style="color: red;font-size:small;font-weight: bold;"></p>
    </form>
</c:otherwise></c:choose>
</body>
</html>
