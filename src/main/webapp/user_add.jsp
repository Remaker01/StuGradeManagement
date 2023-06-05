<%@ page contentType="text/html;charset=UTF-8" import="domain.User" %>
<!--思路：若复选框选择，则发送请求时role=admin，否则为role=user-->
<html>
<head>
    <title>Title</title>
    <meta http-equiv="x-ua-compatible" content="IE=Edge">
    <meta http-equiv="Pragma" content="no-cache">
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style/frames.css">
    <script>
        function submit_(vcode_id) {
            var uname=$("#uname-text").val(),pswd=$("#pswd-text").val(),vcode=$("#vcode-text").val();
            var role=$("#admin-box").prop("checked") ? "admin" : "user";
            console.log(role);
            var data_="username="+uname+"&password="+pswd+"&verifycode="+vcode+"&role="+role;
            $.ajax({
                url:_root_+"register", type:"post", data:data_, processData:false, success:function (d) {$("#status").text(d);$("#"+vcode_id).trigger("click");},error:function (xhr) {$("#status").text("出错了！请尝试刷新验证码或页面。错误码:"+xhr.status);}
            });
            return false;
        }
    </script>
</head>
<body>
<% User u = (User) session.getAttribute("user");
    if (u == null||!u.isAdmin()) {
        %><p>您尚未登录！</p>
<%
    } else {
        %>
<form onsubmit="submit_('vcode')">
    <p>用户名：<input type="text" id="uname-text" required></p>
    <p>密码：<input type="password" id="pswd-text" required></p>
    <p>验证码：<input type="text" id="vcode-text" class="loginInput" autocomplete="off" placeholder="验证码"></p>
        <p><img id="vcode" src="checkcode" onclick="this.src='checkcode?'+Math.random()"> </p>
    <p><input type="checkbox" id="admin-box">注册为管理员</p>
    <p><input type="submit" value="提交"></p>
    <p id="status" style="font-size: small;font-weight: bold;color:red;"></p>
</form>
<%
    }%>
</body>
</html>