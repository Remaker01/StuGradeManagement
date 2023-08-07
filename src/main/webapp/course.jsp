<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,domain.Course,domain.User,dao.UserDao" %>
<%--TODO:分页查询，按条件查询；查找教师姓名不再使用UserDao--%>
<html>
<head><%User u = (User) session.getAttribute("user");%>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <script src="http://cdn.staticfile.org/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="http://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="style/frames.css">
    <%if(u!=null&&u.isAdmin()) {
    %><script>
        function modify(obj) {
            $("#modify").modal("show");
            var tds=$(obj).parent().siblings();
            $("#dosubmit").on("click",function () {
                var id=tds[0].innerText,name=tds[1].innerText;
                var param="type=2&id="+id+"&name="+name+"&prop="+$("#prop-option").val()+"&tid="+$("#teacher-option").val();
                $.ajax({url:_root_+"updatecourse",type:"post",data:param,processData:false,success:function (d) {$("#status").text(d);}});
                $(this).off("click");
                delayedReload(750);
                $("#modify").modal("hide");
            });
        }
        function del(obj) {
            var td=$(obj).parent();
            var id=$(td).siblings()[0];
            //查找还有多少相关成绩信息
            $.ajax({url:_root_+"grade",type:"get",data:"type=1&para="+id.innerText,processData:false,success:function (d) {
                if (confirm("确定要删除吗？\n删除该课程后，有关"+d.result+"条成绩信息也将同步删除！")) { //json会自动处理
                    var cid=id.innerText;
                    var param="type=1&id="+cid;
                    $.ajax({url:_root_+"updatecourse",type: "post",data:param,processData:false,success:function (d1) {
                        $("#status").text(d1);
                        delayedReload(750);
                    }});
                }
            }});
        }
    </script>
    <%}%></head>
<body>
    <% UserDao userDao = new UserDao();
    if (u == null) {
        %><p>您尚未登录！</p>
    <% } else {
    %><p>以下为查询结果</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>编号</th>
            <th>课程名</th>
            <th>课程性质</th>
            <% if(u.isAdmin()) {
            %><th>任课教师</th>
            <th>操作</th>
            <%
            }
            %></tr>
        </thead>
        <tbody>
    <%  String uid = request.getParameter("userid");
        request.getRequestDispatcher("/findcourse?type=0&userid="+uid).include(request,response);
        List<Course> courses = (List<Course>) session.getAttribute("courses");
        StringBuilder str = new StringBuilder(32);
        for (Course c:courses) {
    %>
    <tr>
        <%  str.setLength(0);
            str.append("<td>").append(c.getId()).append("</td>\n");
            str.append("\t\t<td>").append(c.getCname()).append("</td>\n");
            str.append("\t\t<td>").append(c.getCtype()).append("</td>\n");
            if (u.isAdmin()) {
                str.append("\t\t<td>").append(userDao.findById(c.getTeacher()).getUsername()).append("</td>\n");
            }
        %><%=str.toString()%>
        <%if (u.isAdmin()) {
    %><td>
        <input type="button" value="修改" class="btn-in-table" style="background-color: #5050ff;font-size: 13px;" onclick="modify(this)"/>
        <input type="button" value="删除" class="btn-in-table" style="background-color: red;font-size: 13px;" onclick="del(this)"/>
    </td>
    <%
    }
    %></tr>
    <%
        }
    %></tbody>
    </table>
    <%if (u.isAdmin()) {%>
    <div class="modal" id="modify" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title" id="myModalLabel">修改信息</h4>
                </div>
                <form class="modal-body">
                    <p>修改任教教师为：
                        <select class="form-control" style="display:inline;width:200px;" id="teacher-option">
                            <%
                            request.getRequestDispatcher("findusers?type=1").include(request,response);
                            List<User> users = (List<User>) session.getAttribute("users");
                            for (User user:users) {
                                str.setLength(0);
                                str.append(String.format("<option value='%d'>%s</option>",user.getId(),user.getUsername()));
                                %><%=str.toString()%>
                            <%
                            }
                            %></select>
                    </p>
                    <p>修改课程属性为：
                        <select class="form-control" style="display:inline;width:200px;" id="prop-option">
                            <option value="公共课">公共课</option>
                            <option value="专业基础课">专业基础课</option>
                            <option value="专业课">专业课</option>
                            <option value="选修课">选修课</option>
                        </select>
                    </p>
                </form>
                <p class="modal-footer">
                    <input type="button" class="btn" data-dismiss="modal" value="关闭">
                    <input type="button" id="dosubmit" class="btn btn-primary" value="提交更改">
                </p>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <%}%>
    <p id="status" style="color: red;font-size: small;font-weight: bold"></p>
<%}%>
</body>
</html>
<%session.removeAttribute("courses");session.removeAttribute("users");%>