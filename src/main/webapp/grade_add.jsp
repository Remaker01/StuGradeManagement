<%@ page import="java.util.List,domain.Course" contentType="text/html;charset=UTF-8"%>
<!-- 传参数：uid:教师编号 ajax提交请求 -->
<html>
<head>
    <title>Title</title>
    <style>
        input.i2{border-radius: 5px 5px 5px 5px;height: 7mm;width: 200px;padding: 3px 2px;}
    </style>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="style/frames.css">
    <%  if (session.getAttribute("user") == null) {
            response.getWriter().write("您尚未登录！");
        }
        else {
            int uid = Integer.parseInt(request.getParameter("uid"));
            request.getRequestDispatcher(String.format("course?type=0&userid=%d",uid)).include(request,response);
            List<Course> courses = (List<Course>) session.getAttribute("courses");%>
    <script>
        var regexp = /[^\d]/g;
        $(document).ready(function () {
            // document.onkeydown=function (ev) {return ev.key !== "Enter"||!$("#score-text").is(":focus");} //临时性缓解http500
            $("#score-text").on("keyup paste",function() { //注意只能在ready之后选取元素
                $(this).val($(this).val().replace(regexp,'')); //这里是score_text调用，所以要用this
            }).blur(function () {
                $(this).val($(this).val().replace(regexp,''));
            });
            $("#stu-text").on("keyup paste blur",function () {
                var sid=$("#stu-text");
                sid.val(sid.val().replace(regexp,''));
                if(sid.val().length === 0)  return;
                var para = "type=1&id="+sid.val();
                $.ajax({url:_root_+"student",
                    type: "get",
                    data: para,
                    processData: false,
                    success:function (d) {$("#stu-name")[0].innerText=(d.sname == null)?"学生不存在！":("学生姓名:" + d.sname);}
                });
            });
        });
        function submit_() {
            var sid=$("#stu-text").val(),courseid=$("#course-option").val(),score=$("#score-text").val();
            var sname=$("#stu-name")[0].innerText;
            if (sname === "学生不存在！"||sname.length === 0) {
                $("#status").text("学号错误！");
                return;
            }
            var para = "type=0&courseid="+courseid+"&sid="+sid+"&tid=<%=uid%>&score="+score;
            if (score == null||score.length === 0) {
                $("#status").text("成绩不能为空");
                return;
            }
            $.ajax({url:_root_+"grade",
                type:"post",
                data:para,
                processData:false,success:function (f) {$("#status").text(f);},
                error:function (f) {$("#status").text("添加失败!学号错误或成绩已存在")}}
            );
        }
    </script>
    <style>select.form-control {width: 200px;display: inline;}</style>
</head>
<body>
<form>
    <p>学生：
<%--        <select id="stu-option" class="form-control">--%>
<%--        <% StringBuilder str = new StringBuilder(32);--%>
<%--            for (Student student : students) {--%>
<%--                str.setLength(0);--%>
<%--                str.append(String.format("<option value='%d'>",student.getId()))--%>
<%--                        .append(student.getSname())--%>
<%--                        .append("</option>\n");--%>
<%--        %><%=str.toString()%>--%>
<%--        <%--%>
<%--            }--%>
<%--        %></select>--%>
        <input type="text" placeholder="请输入学号" id="stu-text"><br>
        <span id="stu-name" style="font-size: small;"></span>
    </p>
    <p>课程：<select id="course-option" class="form-control">
        <%  StringBuilder str = new StringBuilder(32);
            for (Course c : courses) {
            str.setLength(0);
            str.append(String.format("<option value='%d'>",c.getId()))
                    .append(c.getCname())
                    .append("</option>\n");
        %><%=str.toString()%>
        <%
            }
        %></select></p>
    <p>分数：<input type="text" id="score-text" placeholder="请输入分数" class="i2" maxlength="3"/></p>
    <p><input type="button" value="提交" onclick="submit_()" class="btn btn-primary"/></p>
    <p style="color: red;font-size: small" id="status"></p>
</form>
</body>
<%}%>
</html>