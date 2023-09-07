<%@ page contentType="text/html;charset=UTF-8" import="domain.User,java.util.List" %>
<!DOCTYPE HTML>
<!--用户信息，展示用户id,用户名、是否为管理员，不展示密码-->
<html>
<head>
    <title>Title</title>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge"/>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <script src="http://cdn.staticfile.org/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="http://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="http://cdn.staticfile.org/blueimp-md5/2.19.0/js/md5.min.js"></script>
    <link rel="stylesheet" href="style/frames.css">
    <script>
        $(document).ready(function () {
            var type_=getCurrentParam("type");
            // console.log(type_);
            var selected = $("#type-option>option[value='"+type_+"']");
            selected.attr("selected","true");
            $("#type-option").change(function () {
                document.location.href=_root_+"users.jsp?type="+this.value;
            });
        });
        function del(obj) {
            if (confirm("确认删除吗？")) {
                var td=$(obj).parent();
                var uid=td.prev().prev().prev().text();
                var param="id="+uid;
                // console.log(param);
                $.ajax({
                    url:_root_+"deluser",type:"post",data:param,processData:false,success:function (d) {$("#status").text(d);delayedReload(750);}
                });
            }
        }
        function modify(obj) {
            $("#modify").modal("show");
            $("#doclose").on("click",function () {$("#pswd-input")[0].value="";});
            $("#dosubmit").on("click",function () {
                var pswd_input=$("#pswd-input")[0];
                var pswd=pswd_input.value,td=$(obj).parent();
                if (pswd == null||pswd.length === 0)
                    return;
                if (!passCheck(pswd)) {
                    $("#status").text(PASSWORD_REQUIREMENT);
                    return;
                }
                var uid=td.prev().prev().text();
                pswd=md5(pswd,null,false);
                var param="uname="+uid+"&new="+pswd;
                $.ajax({
                    url:_root_+"updateuser",type: "post",data: param,processData: false,success:function (d) {$("#status").text(d);}
                });
                $(this).off("click");
                delayedReload(750);
                $("#modify").modal("hide");
                pswd_input.value="";
            });
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
<p>过滤条件：<select id="type-option">
    <option value="0">全部</option>
    <option value="1">教师</option>
    <option value="2">管理员</option>
</select><br>
</p>
<table class="main-table">
    <thead>
    <tr>
        <th>用户编号</th>
        <th>用户名</th>
        <th>身份</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
        <%  request.getRequestDispatcher(String.format("findusers?type=%s",request.getParameter("type"))).include(request,response);
            List<User> users = (List<User>) session.getAttribute("users");
            users.remove(u);
            StringBuilder str = new StringBuilder(48);
            for (User user:users) {%>
        <tr>
            <%  str.setLength(0);
                str.append(String.format("<td>%d</td>\n",user.getId()));
                str.append(String.format("\t\t<td>%s</td>\n",user.getUsername()));
                str.append(String.format("\t\t<td>%s</td>\n",user.isAdmin() ? "管理员" : "教师"));
            %><%=str.toString()%>
        <td>
            <input type="button" value="修改密码" class="btn-in-table" style="background-color: #5050ff;width: 70px" onclick="modify(this)"/>
            <input type="button" value="删除" class="btn-in-table" style="background-color: red" onclick="del(this)"/>
        </td>
    </tr>
    <%
        }
    %></tbody>
</table>
<div class="modal" id="modify" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" id="myModalLabel">修改密码</h4>
            </div>
            <form class="modal-body">
                请输入该用户的新密码：<input type="password" class="form-control" id="pswd-input" style="width:200px; display:inline;"/>
            </form>
            <p class="modal-footer">
                <input type="button" id="doclose" class="btn" data-dismiss="modal" value="关闭">
                <input type="button" id="dosubmit" class="btn btn-primary" value="提交更改">
            </p>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<p id="status" style="color: red;font-size: small;font-weight: bold"></p>
<%
    }
%></body>
</html>
<%session.removeAttribute("users");%>