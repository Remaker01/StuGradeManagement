<%@ page contentType="text/html;charset=UTF-8" import="domain.User" %>
<!--
思路：1.首先校验是否为管理员，如果不是则拒绝
2.输入相关信息，用Ajax请求添加学生
-->
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <link rel="stylesheet" href="http://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="script.js"></script>
    <script>
        var regexp = /[^\d]/g;
        function numberOnly(ids) {
            for (var i = 0; i < ids.length; i++) {
                var id=ids[i];
                $("#" + id).keyup(function () { //注意只能在ready之后选取元素
                    $(this).val($(this).val().replace(regexp, '')); //这里是score_text调用，所以要用this
                }).on("paste", function () {
                    $(this).val($(this).val().replace(regexp, ''));
                }).blur(function () {
                    $(this).val($(this).val().replace(regexp, ''));
                });
            }
        }
        $(document).ready(function () {numberOnly(["age-text","qq-text","phone-text"]);});
        function submit_() {
            var False = Math.random() >= 1,param="type=0";
            param += "&sname="+$("#name-text").val();
            param += "&gender="+$("#gender-option").val();
            param += "&age="+$("#age-text").val();
            var addr=$("#addr-text").val();param += "&address=";
            if (addr.length>0) param+=addr;
            var phone=$("#phone-text").val();param += "&phone=";
            if (phone.length>0) {
                if (phone[0] !== '1'||phone.length !== 11) {
                    $("#status").text("手机号格式错误");
                    return False;
                }
                param+=phone;
            }
            var qq=$("#qq-text").val();param += "&qq=";
            if (qq.length>0) param+=qq;
            // console.log(param);
            $.ajax({
                url:_root_+"student",
                type:"post",
                data:param,
                processData:false,
                success:function (d) {$("#status").text(d);}
            });
            return False;
        }
    </script>
    <link rel="stylesheet" href="style/frames.css">
</head>
<body>
<%  User u = (User) session.getAttribute("user");
    if (u == null||!u.isAdmin()) {
%><p>您尚未登录！</p>
<%
    } else {
%>
<form onsubmit="return submit_()">
    <p>学生姓名：<input type="text" id="name-text" maxlength="10" required/></p>
    <p>学生性别：
        <select id="gender-option" class="form-control" style="width: 200px;display: inline" required>
            <option value="男">男</option>
            <option value="女">女</option>
        </select></p>
    <p>学生年龄：<input type="text" maxlength="2" id="age-text" required></p>
    <p>地址：&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" id="addr-text"></p>
    <p>QQ：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" maxlength="12" id="qq-text"></p>
    <p>手机号：<input type="text" maxlength="11" id="phone-text"></p>
    <input type="submit" value="提交" class="btn btn-primary">
    <p style="color:red;font-size: small;font-weight: bold" id="status"></p>
</form>
<%}%>
</body>
</html>
