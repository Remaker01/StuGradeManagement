## 学生成绩管理系统
### 目前状态
5.14 尚未完成

目标：通过采用json返回数据等方式尽量减少jsp页面
### 介绍
JavaEE学生成绩管理系统项目，基于Servlet、JSP、Spring框架

支持功能：查询学生信息(支持分页)，查询学生分数、查询课程、增加学生考试信息、增加课程(仅管理员)等
### 环境信息
| 名称     |               值               |
|--------|:-----------------------------:|
| 语言     |          Java(JDK8)           |
| 操作系统   |         MS Windows 10         |
| IDE    | IntelliJ IDEA 2021.3 Ultimate |
| 数据库    |           MySQL 8.0           |
| 服务器    |     Apache tomcat 9.0.71      |
| 项目管理工具 |            Maven 4            |

### 使用教程
1. 首先按上面的表格配置好环境（你也可以选不同版本，但不保证能正常运行）。
2. 新建名为stuDB的数据库，并在其中加入表：
```sql
create table student
(
    id      int auto_increment
        primary key,
    sname   varchar(255)               not null,
    gender  varchar(255)               not null,
    address varchar(255)               null,
    age     tinyint unsigned           not null,
    qq      varchar(10) charset latin1 null,
    phone   varchar(11) charset latin1 null
) AUTO_INCREMENT = 2000 CHARACTER SET = utf8mb4;

create table users
(
    id         int auto_increment primary key,
    username   varchar(255) not null,
    `password` varchar(100) not null,
    isadmin    tinyint(1) unsigned default 0 null
) AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4;

CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cname` varchar(10) CHARACTER SET utf8mb4 NOT NULL,
  `ctype` varchar(10) CHARACTER SET utf8mb4 NOT NULL COMMENT '课程类型：公共课、专业基础课、专业课、选修课',
  `teacher` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `TEA_ID` FOREIGN KEY (`teacher`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) CHARACTER SET = utf8mb4;

create table grade
(
   stuid    int              not null
      primary key,
   courseid int              not null,
   score    tinyint unsigned null comment '分数为空代表考试异常',
   constraint COURSE_ID
      foreign key (courseid) references course (id)
         on update cascade,
   constraint STU_ID
      foreign key (stuid) references student (id)
         on update cascade
);
```
3. 本项目使用了Tomcat自带连接池。在tomcat安装目录下conf文件夹中的context.xml文件中`<Context>`条目下加入以下内容
```xml
<Resource name="jdbc/Stu" auth="Container"  
          type="javax.sql.DataSource" maxActive="100" maxIdle="30"  
          maxWait="10000" username="" password=""  
          driverClassName="com.mysql.jdbc.Driver"  
          url="jdbc:mysql://localhost:3306/StuDB"/>
```
记得把`username`与`password`改为你数据库的用户名和密码。最后一行可换成你的数据库地址。
4. 在IDEA中新建JavaEE项目，添加Servlet、Spring依赖，并配置好Tomcat。这个网上教程很多，不再赘述。注意http端口号为80
5. 在IDEA的Project Structure->Modules->Dependencies中加入Tomcat安装目录下的lib目录。
6. 在src/main/resources目录下新建jdbc.properties文件,存入以下内容
```properties
    driverClassName=com.mysql.cj.jdbc.Driver
    url=jdbc:mysql://localhost:3306/stdDB?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
    username=你的数据库用户名
    password=你的数据库密码
   ```
### 注意事项
- 本项目所有用户密码均使用SHA256加密后存储在数据库中，如要在数据库中新建用户，请不要忘了加密。
- 本项目前端不考虑旧版浏览器(如IE9以下版本)兼容性(Updated 230524：经实测IE11正常)
- 本项目使用MIT协议。

### 参考

[teacher.jsp等 前端设计](https://blog.csdn.net/weixin_58270359/article/details/124413055) <br>
[course.jsp表格设计](https://www.w3school.com.cn/tiy/t.asp?f=css_table_fancy) <br>
[出错页面](https://yz.chsi.com.cn/404) <br>

**欢迎提出意见与建议**