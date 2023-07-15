<%@ page import="domain.Student,java.util.List,domain.User" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%--TODO:分页查询，按条件查询--%>
<html>
<head>
    <title>Title</title>
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style/frames.css">
    <% User u = (User) session.getAttribute("user");
    if(u != null&&u.isAdmin()) {
    %><script>
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
        function modify(obj){}
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
            <% if (u != null&&u.isAdmin()) {
            %><th>操作</th>
            <%
            }
            %>
        </tr>
        </thead>
        <tbody>
        <%  request.getRequestDispatcher("/student?type=0").include(request,response);
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
    <p id="status" style="color: red;font-size: small;font-weight: bold"></p>
    <div style="text-align:right;"><p>当前第1页</p></div>
</body>
</html>
<%session.setAttribute("students",null);%>