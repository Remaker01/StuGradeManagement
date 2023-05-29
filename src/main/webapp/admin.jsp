<%--
  管理员页面
--%>
<%@ page contentType="text/html;charset=UTF-8" import="domain.User,java.net.URLEncoder" %>
<html>
<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1" />
    <title>学生成绩管理系统 - 管理页</title>
    <script src="https://cdn.staticfile.org/jquery/1.12.4/jquery.min.js"></script>
    <script src="script.js"></script>
    <script src="http://cdn.staticfile.org/jquery-easing/1.4.1/jquery.easing.min.js"></script>
    <script>
        function confirm_close() {
            if(confirm('确认要退出吗？')===true) {
                window.open(_root_+'exit','_self');
            }
        }
    </script>
    <style>
        *{margin: 0;padding: 0;}
        #a1 {
            width: 80%;
            height: 70px;
            padding-left: 23px;
            padding-top: 20px;
            margin-bottom: 10px;
            margin-top: 5px;
            box-shadow: 0 0 1500px 0 #DDDDDD;
            float: left;
            z-index: 1;
        }
        #top{
            /* background: linear-gradient(to right,#6495ED,mistyrose);
                   -webkit-background-clip: text;
                   color: transparent; */
            color: #6495DD;
        }
        #a2{width: 18%;height: 70px;float: left;margin-bottom: 10px;padding-top: 20px;
            margin-top: 5px;box-shadow: 0 0 1500px 0 #DDDDDD;z-index: 1;}
        #aa1,#aa2{width: 35%;height: 45px;float: left;margin-right: 10px;
            border-radius: 5px 5px 5px 5px;box-shadow: 0 0 20px 0 #DCDCDC;}
        #aa2 a,#aa1 a{font-size: larger;width: 100%;height: 42px;color: #6495DD;line-height: 42px;
            text-decoration: none;}
        #aa1 a:hover{color: #BD0A01;}
        #aa2 a:hover{color: #BD0A01;}
        #tab1{float: left;width: 220px;height: 550px;text-align: left;margin-top: 0;border: 1px solid #6495DD;background:#6495DD;margin-left: 5px;box-shadow: 0 0 35px 0px #DDDDDD;}
        #tab2{float: left;width: 100%;height: 170px;text-align: center;border-bottom: 1px solid #000000;color: #DDDDDD;}
        #t2{height: 150px;width: 100%;}
        #t2 tr th{height: 30px;font-size: x-large;}
        img{border:0;}
        #img1{margin-top: 5px;border-radius: 50%;}
        ul,li{list-style-type:none;}
        a{color:#6495DD;text-decoration:none;}
        a:hover {color:#bd0a01;text-decoration:underline;}
        .treebox{ width: 100%; margin: 0 auto; background-color: #6495DD;}
        .menu{ overflow: hidden; border-color: #ddd; border-style: solid ; border-width: 0 1px 1px ; }
        /*第一层*/
        .menu li.level1>a{
            display:block;
            height: 47px;
            line-height: 47px;
            color: white;
            padding-left: 50px;
            border-bottom: 1px solid #000;
            font-size: 15px;
            position: relative;
            transition:all .5s ease 0s;
        }
        #mg{height: 100px;background-color: #6495ED;text-align: center;width: 200px;}
        .menu li.level1 a:hover{ text-decoration: none;background-color:#6495FF;/* border: 2px solid #000000; */}
        .menu li.level1 a.current{/*background:-webkit-linear-gradient(top,#6495FF,pink);*/background-color:#6495FF;}

        /*============修饰图标*/
        .ico{ width: 20px; height: 20px; display:block;   position: absolute; left: 20px; top: 13px; background-repeat: no-repeat; background-image: url(imgs/ico1.png); }

        /*============小箭头*/
        .level1 i{ width: 20px; height: 10px; background-image:url(imgs/arrow.png); background-repeat: no-repeat; display: block; position: absolute; right: 20px; top: 20px; }
        .level1 i.down{ background-position: 0 -10px; }

        .ico1{ background-position: 0 0; }
        .ico2{ background-position: 0 -20px; }
        .ico3{ background-position: 0 -40px; }

        /*第二层*/
        .menu li ul{ overflow: hidden; }
        .menu li ul.level2{ display: none;background: #6495DD; }
        .menu li ul.level2 li a{
            display: block;
            height: 45px;
            line-height: 45px;
            color: white;
            text-indent: 60px;
            /*border-bottom: 1px solid #ddd; */
            font-size: 17px;
            transition:all 1s ease 0s;
        }
        #main_{width: 80%;height: 550px;float: left;margin-left: 5px; box-shadow: 0 0 35px 0 #DDDDDD;z-index: 0;}
    </style>
</head>
<body>
    <%
    User user = (User) session.getAttribute("user");
    if (user == null||!user.isAdmin()) {
        out.print("您尚未登录！");
    }
    else {
%>
<div id="a1">
    <img src="imgs/logo_jw_d.png" style="vertical-align: bottom;">

    <b><span style="font-size: 9px;"> 学生信息管理系统服务平台</span></b>
</div>
<div id="a2">
    <span style="text-align: center">
        <div id="aa1">
            <a href="#" onclick="confirm_close()"><span id="top"><b>退出</b></span></a>
        </div>
    </span>
</div>
<div id="tab1">
    <div id="tab2">
        <table style="border: 0;" cellspacing="0" cellpadding="0" id="t2">
            <%--            <tr><th>管 理 员 信 息</th></tr>--%>
            <tr><td ><img src="imgs/3d84e1a572d7a401ad5c01661b571b06.jpg" width="100px" height="100px" id="img1"></td></tr>
            <%--            <tr><td height="30px">20202607010146</td></tr>--%>
        </table>
    </div>
    <div class="treebox">
        <ul class="menu">
            <li class="level1">
                <a href="#none"><em class="ico ico2"></em>学生管理<i></i></a>
                <ul class="level2">
                    <li><a href="student.jsp" target="yem">学生信息</a></li>
                    <li><a href="about:blank" target="yem">新增学生</a> </li>
                </ul>
            </li>
            <li class="level1">
                <a href="#none">
                    <em class="ico ico3"></em>成绩管理<i></i></a>
                <ul class="level2">
                    <li><a href="grade.jsp?type=0" target="yem">成绩信息查询</a></li>
                    <li><a href="about:blank" target="yem">成绩信息添加</a></li>
                </ul>
            </li>
            <li class="level1">
                <a href="#none"><em class="ico ico3"></em>课程管理<i></i></a>
                <ul class="level2">
                    <li><a href="about:blank" target="yem">新增课程</a></li>
                    <li><a href="course.jsp" target="yem">全部课程查询</a></li>
                </ul>
            </li>
            <li class="level1">
                <a href="#none"><em class="ico ico1"></em>用户管理<i></i></a>
                <ul class="level2">
                    <li><a href="about:blank" target="yem">新增用户</a></li>
                    <li><a href="about:blank" target="yem">用户信息管理</a></li>
                    <li><a href="modipass.html?uname=<%=URLEncoder.encode(user.getUsername(),"UTF-8")%>" target="yem">修改密码</a></li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<div id="main_">
    <iframe src="" width="100%" height="550px" id="yem" name="yem" border="0" scrolling="no" frameborder="0">
    </iframe>
</div>
<script>
    //等待dom元素加载完毕.
    $(function(){
        $(".treebox .level1>a").click(
            function(){
                $(this).addClass('current')   //给当前元素添加"current"样式
                    .find('i').addClass('down')   //小箭头向下样式
                    .parent().next().slideDown(500,'easeOutQuad')  //下一个元素显示
                    .parent().siblings().children('a').removeClass('current')//父元素的兄弟元素的子元素去除"current"样式
                    .find('i').removeClass('down').parent().next().slideUp(500,'easeOutQuad');//隐藏
                return false; //阻止默认时间
            });
    });
</script>
    <%
    }
%>
</html>
