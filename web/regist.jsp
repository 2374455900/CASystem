<%--
  Created by IntelliJ IDEA.
  User: Masker
  Date: 2022/6/7
  Time: 0:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册</title>
    <link rel="stylesheet" href="css/regist.css">
    <script src="js/jquery-easyui-1.4.3/jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/md5.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        function md5() {
            const password = document.getElementById("password").value;
            const md5password = $.md5(password);
            document.getElementById("pwd").value = md5password;
            console.log("没有加密之前的是：" + password);
            console.log("加密以后是：" + md5password);
        }


        function check_username() {
            const username = document.getElementById("username").value.length;
            if (username < 5 || username > 16) {
                alert("用户名长度不合法");
                return false;
            }
        }

        function check_email() {
            var reg = /^[0-9a-zA-Z_.-]+[@][0-9a-zA-Z_.-]+([.][a-zA-Z]+){1,2}$/;
            if (reg.test(document.getElementById("emailing").value)) {

            } else {
                alert("邮箱错误")
                return false;
            }
        }

        function check_pwd() {
            const pwd_length = document.getElementById("password").value.length;
            if (pwd_length < 5 || pwd_length > 16) {
                alert("密码不符合要求")
            }
        }

        function check_pwd_sim() {
            if (document.getElementById("password") !== document.getElementById("u_password")) {
                alert("两次密码不相同，请重新输入");
            }
        }

    </script>
</head>

<body>
<div class="Box">
    <h1>新用户注册</h1>
    <form method="post" action="RegistServlet">

        <div class="inputbox">
            <span class="p">*</span>
            <label>用户名称：</label>
            <input type="text" name="username" id="username" onblur="check_username()">
            <p class="message">名称5-16位</p>
        </div>

        <div class="inputbox">
            <span class="p">*</span>
            <label>邮箱地址：</label>
            <input type="text" name="email" id="emailing" onblur="check_email()">
            <p class="message">email地址</p>
        </div>

        <div class="inputbox">
            <span class="p">*</span>
            <label>登录密码：</label>
            <input type="password" name="password" id="password" onblur="check_pwd()">
            <p class="message">请输入5-18位密码</p>
        </div>

        <div class="inputbox">
            <span class="p">* </span>
            <label>确认密码：</label>
            <input type="password" name="" id="u_password" onblur="check_pwd_sim()">
            <p class="message">再次输入密码</p>
        </div>
        <input type="submit" name="" value="注册并登录" class="submit" onclick="md5()" id="sub">
        <div>
            <a href="login.jsp" class="right">开始登录</a>
        </div>

    </form>
</div>

<%--<script src="js/regist.js" type="text/javascript"></script>--%>
</body>

</html>

