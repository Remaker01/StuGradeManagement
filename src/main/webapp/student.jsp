<%@ page import="domain.Student,java.util.List,domain.User" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%--TODO:分页查询，按条件查询--%>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="style/frames.css">
    <style>#modify input[type="text"] {
        display: inline;
    } </style>
    <script>
        var submit_ = function () {
            var val = $("#pageno-text").val();
            document.location.href = _root_ + "student.jsp?pageno=" + val;
            return Math.random() > 1;
        };
    </script>
    <% User u = (User) session.getAttribute("user");
    if(u != null&&u.isAdmin()) {
    %><script>
        $(document).ready(function () {
            var ids=["age-text","phone-text","qq-text","pageno-text"],regexp = /[^\d]/g;
            for (var i=0; i<ids.length; i++) {
                var id=ids[i];
                $("#" + id).keyup(function () { //注意只能在ready之后选取元素
                    $(this).val($(this).val().replace(regexp, '')); //这里是score_text调用，所以要用this
                }).on("paste", function () {
                    $(this).val($(this).val().replace(regexp, ''));
                }).blur(function () {
                    $(this).val($(this).val().replace(regexp, ''));
                });
            }
        });
        function del(obj){
            var td=$(obj).parent();
            if (confirm("确认要删除该学生吗？")) {
                var id_=$(td).siblings()[0];
                var param="type=1&id="+id_.innerText;
                $.ajax(
                    {url:_root_+"student",type:"post",data:param,processData:false,success:function (d) {$("#status").text(d);delayedReload(550);}}
                );
            }
        }
        function modify(obj){
            var doclose = function() {
                $("#phone-text,#qq-text").val("");$("#dosubmit").off("click");$("#modify").modal("hide");
            }
            $("#modify").modal("show");
            var td=$(obj).parent();
            var td_siblings=$(td).siblings();
            $("#name")[0].innerText="姓名："+td_siblings[1].innerText;//显示姓名
            $("#age-text").val(td_siblings[3].innerText);
            $("#addr-text").val(td_siblings[4].innerText);
            $("#phone-text").val(td_siblings[5].innerText)//自动填充一些信息
            $("#doclose").on("click",doclose);
            $("#dosubmit").on("click",function () {
                var id_=td_siblings[0].innerText, sname=td_siblings[1].innerText,gender=td_siblings[2].innerText;//变不了的信息
                var age=$("#age-text").val(),addr=$("#addr-text").val(),phone=$("#phone-text").val(),qq=$("#qq-text").val(); //改变的信息
                if (age == null||age.length===0||addr==null||addr.length===0) {
                    $("#status").text("年龄、地址不能为空");
                    doclose();
                    return;
                }
                if (!checkphone(phone)) {
                    $("#status").text("手机号格式错误");
                    doclose();
                    return;
                }
                var param="type=2&id="+id_+"&sname="+sname+"&age="+age+"&gender="+gender+"&address="+addr+"&phone="+phone+"&qq="+qq;
                $.ajax({
                    url:_root_+"student", type: "post", data: param, processData: false, success:function (d) {$("#status").text(d);}
                });
                delayedReload(750);
                doclose();
            });
        }
        function checkphone(phone) {
            if (typeof phone !== "string")
                return false;
            if (phone.length === 0)
                return true;
            return phone[0] === '1' && phone.length === 11;
        }
    </script>
    <%}%></head>
<body>
    <p>以下为查询到的学生信息</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>地址</th>
            <th>手机号</th>
            <th>QQ</th>
            <% if (u != null&&u.isAdmin()) {
            %><th>操作</th>
            <%
            }
            %>
        </tr>
        </thead>
        <tbody>
        <%  String pageno = request.getParameter("pageno");
            try {
                Integer.parseInt(pageno);
            }catch (NumberFormatException e) {
                pageno = "1";
            }
            request.getRequestDispatcher("/student?type=0&pageno="+pageno).include(request,response);
            List<Student> students = (List<Student>) session.getAttribute("students");
            StringBuilder str = new StringBuilder(48);
            for (Student c:students) {
        %>
        <tr>
            <%  str.setLength(0);
                str.append("<td>").append(c.getId()).append("</td>\n");
                str.append("\t\t<td>").append(c.getSname()).append("</td>\n");
                str.append("\t\t<td>").append(c.getGender()).append("</td>\n");
                str.append("\t\t<td>").append(c.getAge()).append("</td>\n");
                str.append("\t\t<td>").append(c.getAddress() == null ? "" : c.getAddress()).append("</td>\n");
                str.append("\t\t<td>").append(c.getPhone() == null ? "" : c.getPhone()).append("</td>\n");
                str.append("\t\t<td>").append(c.getQQ() == null ? "" : c.getQQ()).append("</td>\n");
            %><%=str.toString()%>
                <%if (u != null&&u.isAdmin()) {
                %><td>
                <input type="button" value="修改" class="btn-in-table" style="background-color: #5050ff;font-size: 13px;" onclick="modify(this)"/>
                <input type="button" value="删除" class="btn-in-table" style="background-color: red;font-size: 13px;" onclick="del(this)"/></td>
            <%
                }
            %></tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%if (u != null&&u.isAdmin()) {
    %><div class="modal" id="modify" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title" id="myModalLabel">修改学生信息</h4>
                </div>
                <form class="modal-body">
                    <p id="name"></p>
                    <p>年龄：<input type="text" class="form-control" maxlength="2" id="age-text" autocomplete="off"></p>
                    <p>地址：<input type="text" class="form-control" id="addr-text" style="width:200px; display:inline;"/></p>
                    <p>手机号：<input type="text" class="form-control" maxlength="11" id="phone-text"></p>
                    <p>QQ：<input type="text" class="form-control" maxlength="12" id="qq-text"></p>
                </form>
                <p class="modal-footer">
                    <input type="button" id="doclose" class="btn" data-dismiss="modal" value="关闭">
                    <input type="button" id="dosubmit" class="btn btn-primary" value="提交更改">
                </p>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <%
        }
    %><p id="status" style="color: red;font-size: small;font-weight: bold"></p>
    <div style="text-align:right;">
        <form id="topage" onsubmit="return submit_();">跳到第<input type="text" id="pageno-text" maxlength="3" required autocomplete="off" style="width: 30px;">页
        <input type="submit" class="btn" value="GO">&nbsp;
    </form>
    </div>
</body>
</html>
<%session.setAttribute("students",null);%>