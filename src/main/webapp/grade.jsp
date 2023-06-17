<%@ page import="domain.*,java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<%User user = (User) session.getAttribute("user");
    if (user == null) {
%><html><head><title>您尚未登录！</title></head><body><p>您尚未登录</p></body></html>
    <%
    }
    else {
    %>
<html>
<head>
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <meta http-equiv="Pragma" content="no-cache">
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <title>Title</title>
    <link rel="stylesheet" href="style/frames.css">
    <style>
        tbody .btn-in-table {
            width: 50px;
            height: 30px;
            color: #ffffff;
            border: transparent;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
    <script>
        function modify(obj) {
            // 思路：弹出对话框输入成绩，点击确认后将成绩作为参数发到后端进行ajax请求，请求结束后刷新
            // TODO:能不能不弹对话框，直接在网页上生成一个input?
            // 1.找到成绩
            var td=$(obj).parent().prev();
            var old_score=td.text(),new_score=prompt("输入新成绩");
            if (new_score === null)
                return;
            if (new_score.indexOf('.') >= 0||isNaN(parseInt(new_score))) { //小数也能parseInt
                $("#status").text("新成绩不合法，无法修改！");
                return;
            }
            //2.发送Ajax请求
            var course=td.prev();
            var sid=course.prev();
            var param="type=2&courseid="+course.attr("value")+"&sid="+sid.attr("value")+"&tid=<%=user.getId()%>&score="+new_score;
            $.ajax({
                url:_root_+"grade", type: "post", data:param, processData: false, contentType:"application/x-www-form-urlencoded", success:function (d) {$("#status").text(d);delayedReload(400);}
            });
        }
        function del(obj) {
            // 思路：选取第1，2列，获得其中的name，把两个name作为http参数进行ajax请求至/grade执行真正删除，success后删除该行
            if (confirm("确认删除吗？")) {
                var td = $(obj).parent();
                // var score = td.prev();
                var course = td.prev().prev();
                var sid = course.prev();
                // console.log(sid.attr("value"));console.log(course.attr("value"));
                var param="type=1&courseid="+course.attr("value")+"&sid="+sid.attr("value")+"&tid=<%=user.getId()%>";
                console.log(param);
                $.ajax({
                    url:_root_+"grade", type:"post", data:param, processData:false, contentType:"application/x-www-form-urlencoded", success:function (d) {$("#status").text(d);td.parent().remove();}
                });
            }
        }
    </script>
</head>
<body>
    <p>以下为查询结果</p>
    <table class="main-table">
        <thead>
        <tr>
            <th>学生</th>
            <th>课程</th>
            <th>分数</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
            <% StringBuilder params = new StringBuilder("?");
            String type = request.getParameter("type");
            params.append("type=").append(type);
            String para = request.getParameter("para");
            if (para != null)  params.append("&para=").append(para);
            String pageno = request.getParameter("pageno");
            if (pageno != null) params.append("&pageno=").append(pageno);
            request.getRequestDispatcher("/grade"+params).include(request,response);
            List<Grade> grades = (List<Grade>) session.getAttribute("grades");
            StringBuilder str = new StringBuilder(24);
            for (Grade g:grades) {
            %>
        <tr>
            <% str.setLength(0);
            int sid = g.getStuId(),cid=g.getCourseId();
            request.getRequestDispatcher("student?type=1&id="+sid).include(request,response);
            Student student = (Student) session.getAttribute("student");
            request.getRequestDispatcher("findcourse?type=1&id="+cid).include(request,response);
            Course course = (Course)  session.getAttribute("course");
            str.append(String.format("<td value='%d'>", sid)).append(student.getSname()).append("</td>\n");
            str.append(String.format("\t\t<td value='%d'>", cid)).append(course.getCname()).append("</td>\n");
            short s = g.getScore();
            String style = (s < 60) ? "style='color:red;'" : "";
            str.append(String.format("\t\t<td %s>",style)).append(s).append("</td>\n");
            %><%=str.toString()%>
            <td>
                <input type="button" value="修改" class="btn-in-table" style="background-color: #5050ff" onclick="modify(this)"/>
                <input type="button" value="删除" class="btn-in-table" style="background-color: red" onclick="del(this)"/>
            </td>
        </tr>
        <%
            }
        %></tbody>
    </table>
    <p id="status" style="font-weight: bold;color: red;font-size: small;"></p>
</body>
</html>
<%
    }
%><%session.removeAttribute("grades");session.removeAttribute("student");%>