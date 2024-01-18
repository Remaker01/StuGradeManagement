<%@ page contentType="text/html;charset=UTF-8" import="domain.User,java.util.List" %>
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
<% User u = (User) session.getAttribute("user");
    if (u == null||!u.isAdmin()) {
        %><p>您尚未登录！</p>
<%
    } else {
        request.getRequestDispatcher("findusers?type=1").include(request,response);
        List<User> users = (List<User>) session.getAttribute("users");
%>
<form onsubmit="return submit_()">
    <p>教师：<select id="user-option" class="form-control">
        <% StringBuilder str = new StringBuilder(32);
        for (User user:users) {
            str.setLength(0);
            str.append(String.format("<option value='%d'>%s</option>\n",user.getId(),user.getUsername()));
            %><%=str.toString()%>
        <%
        }
        %></select></p>
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
<%}%>
</body>
</html>
