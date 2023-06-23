<%@ page import="java.util.List,domain.Course,domain.User,dao.UserDao" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--TODO:分页查询，按条件查询；查找教师姓名不再使用UserDao--%>
<html>
<head><%User u = (User) session.getAttribute("user");%>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style/frames.css">
    <%if(u!=null&&u.isAdmin()) {
    %><script>
        function modify(obj) {
            var td = $(obj).parent(); //TODO:要选类型和教师，这个不太好弄
        }
        function del(obj) {
            var td=$(obj).parent();
            var id=$(td).siblings()[0];
            //查找还有多少相关成绩信息
            $.ajax({url:_root_+"grade",type:"get",data:"type=1&para="+id.innerText,processData:false,async:false,success:function (d) {
                if (confirm("确定要删除吗？\n删除该课程后，有关"+d.result+"条成绩信息也将同步删除！")) { //json会自动处理
                    var cid=id.innerText;
                    var param="type=1&id="+cid;
                    $.ajax({url:_root_+"updatecourse",type: "post",data:param,processData:false,success:function (d1) {
                        $("#status").text(d1);
                        delayedReload(500);
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
        <tr style="text-align: center">
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
        <input type="button" value="修改" class="btn-in-table" style="background-color: #5050ff" onclick="modify(this)"/>
        <input type="button" value="删除" class="btn-in-table" style="background-color: red" onclick="del(this)"/>
    </td>
    <%
    }
    %></tr>
    <%
        }
    %></tbody>
    </table>
    <p id="status" style="color: red;font-size: small;font-weight: bold"></p>
<%}%>
</body>
</html>
<%session.setAttribute("courses",null);%>