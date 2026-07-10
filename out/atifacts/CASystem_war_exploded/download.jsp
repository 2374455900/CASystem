<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Masker
  Date: 2022/6/8
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>下载证书</title>
    <link href="css/style.css" rel="stylesheet" type="text/css">
    <link href="css/login_style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        window.onload = function () {
            Particles.init({
                selector: '.background'
            });
        };
    </script>
</head>

<body>

<div>
    <!--nav begin-->
    <div id="nav">
        <!--<div class="nav_left"></div>
        <div class="nav_right"></div>-->
        <ul>
            <li class="login info"><a style=" padding-bottom:20px; margin-left:8px; " href="information.jsp">个人中心</a></li>
            <li class="info"><a href="apply.jsp">申请证书</a></li>
            <li class="info"><a href="cancel_the_certificate.jsp">注销证书</a></li>
            <li class="info"><a href="download.jsp">下载证书</a></li>
            <%--            <li class="info"><a href="Get_private_public_key.jsp">获取公钥</a> </li>--%>
            <li class="info"><a href="changePassword.jsp">修改密码</a></li>
            <li class="info" onClick="firm()"><a href="LogoutServlet">退出</a></li>
        </ul>
    </div>
    <!--nav end-->
    <center>
        <h1 style="color: #FFFFFF">下载证书</h1>
    </center>
    <div id="window" class="login_box">
        <div class="float_l">
            <div class="login_title">下载证书</div>
            <form name="form1" method="post" action="revokeCerServlet">
                <div id="login_node" style="display:block; margin-left: 50px;">
                    <ul>证书序列号<label><input type="text" name="serial_number" id="serial_number" placeholder="证书序列号" required="" class="input_on" />
                        <input type="text" name="sign_serial_number" id="sign_serial_number" style="display: none"></label></ul>
                    <ul>密码<label><input type="password" name="check_node" placeholder="验证码" required="" class="input_on" />
                        <input type="text" name="sign_revoke_pwd" id="sign_revoke_pwd" style="display: none"></label>
                    </ul>
                    <a class="a-upload">
                        <input type="submit" name="button" id="button" value="提交" />提交
                    </a>

                </div>
            </form>
        </div>
        <div class="clear"></div>
    </div>

    <body>
    …

    <canvas class="background"></canvas>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/particlesjs/2.2.2/particles.min.js"
            type="text/javascript"></script>
    </body>
</div>
</body>


</html>

