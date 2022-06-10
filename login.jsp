<%--
  Created by IntelliJ IDEA.
  User: Masker
  Date: 2022/6/1
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录</title>
    <link rel="stylesheet" href="css/login.css">
    <script src="js/jquery-easyui-1.4.3/jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/md5.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        function md5() {
            const password = $("#pwd").val();
            const md5password = $.md5(password);
            document.getElementById("pwd").value = md5password;
            console.log("没有加密之前的是：" + password);
            console.log("加密以后是：" + md5password);
        }
    </script>
</head>


<body>
<div class="box">
    <h2>Login</h2>
    <form action="loginServlet" method="post">
        <div class="inputbox">
            <label>用户名：</label>
            <label for="username"></label><input type="text" id="username" name="username">
        </div>
        <div class="inputbox">
            <label><span class="space">密</span>码：</label>
            <img src="layout/image/login_regist/close.png" alt="" id="eye">
            <label for="pwd"></label><input type="password" id="pwd" name="password">
        </div>
        <div style="color: #03a9f4">
            ${error}<br>
            ${LoginError}
        </div>
        <div>
            <input type="submit" value="开始登录" class="submit" id="sub" onclick="md5()">
        </div>
    </form>

    <a href="regist.jsp" target="_self"><input type="submit" value="没有账号？注册一个" class="submit2"></a>
</div>
<script src="js/login.js"></script>
<script src="js/reqwest.js"></script>
</body>

</html>

