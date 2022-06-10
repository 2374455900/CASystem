<%--
  Created by IntelliJ IDEA.
  User: Masker
  Date: 2022/6/8
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>注销证书</title>
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
            <li class="info"><a href="apply.jsp">申请证书</a> </li>
            <li class="info"><a href="cancel_the_certificate.jsp">注销证书</a> </li>
            <li class="info"><a href="download.jsp">下载证书</a> </li>
<%--            <li class="info"><a href="Get_private_public_key.jsp">获取公钥</a> </li>--%>
            <li class="info"><a href="changePassword.jsp">修改密码</a> </li>
            <li class="info" onClick="firm()"><a href="LogoutServlet">退出</a> </li>
        </ul>
    </div>
    <!--nav end-->
    <center>
        <h1 style="color: #FFFFFF">注销证书</h1>
    </center>
    <div id="window" class="login_box">
        <div class="float_l">
            <div class="login_title"> 注销证书</div>
            <form name="form1" method="post" action="revokeCerServlet">
                <div id="login_node" style="display:block; margin-left: 50px;">
                    <ul>证书序列号<label><input type="text" name="serial_number" id="serial_number" placeholder="证书序列号" required="" class="input_on" />
                        <input type="text" name="sign_serial_number" id="sign_serial_number" style="display: none"></label></ul>
                    <ul>密码<label><input type="password" name="revoke_pwd" placeholder="验证码" required="" class="input_on" />
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

    <script src="https://cdnjs.cloudflare.com/ajax/libs/particlesjs/2.2.2/particles.min.js"></script>
    </body>
</div>
</body>

<script type="text/javascript">
    var submit_btn = document.getElementById("button");
    var serial_number = document.getElementById("serial_number");
    var revoke_pwd = document.getElementById("revoke_pwd");
    var sign_serial_number = document.getElementById("sign_serial_number");
    var sign_revoke_pwd = document.getElementById("sign_revoke_pwd");
    submit_btn.addEventListener("click", function () {
        var message = "您确定要撤销该证书吗？\n" +
            document.getElementById("serial_number").getAttribute("placeholder") + ": " +
            document.getElementById("serial_number").value + "\n";
        var r = confirm(message);
        if (r == true) {
            revoke_encrypt();
            document.getElementById("revoke_form").submit();
            serial_number.value = "";
            revoke_pwd.value = "";
        } else {

        }
    });

    function revoke_encrypt() {
        var publicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiEctQMqqeQjnS1vU6BXOaVThhanUcOT67jziw25NuVUKEHjZgs194OOz5IZ0w9UGtzn59opBwy3295UKb9r2QLMXTFWh88zeZ7KF4LLNesnYhwy0MIXknb6lxnJ7Dfnz5K+vgGJd0O0LmTUJDQ+xnlgpkh7x2jVrDJglcU5M0aQIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_serial_number = encrypt.encrypt(serial_number.value);
        var enc_revoke_pwd = encrypt.encrypt(revoke_pwd.value);
        var sign_serial_number = hex_sha256(serial_number.value);
        var sign_revoke_pwd = hex_sha256(revoke_pwd.value);
        // var keyHex = CryptoJS.enc.Utf8.parse("7c6ffnDSak8YMiVq");
        // var enc_serial_number = CryptoJS.DES.encrypt(serial_number.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        // var enc_revoke_pwd = CryptoJS.DES.encrypt(revoke_pwd.value, keyHex, {
        //     mode: CryptoJS.mode.ECB,
        //     padding: CryptoJS.pad.Pkcs7
        // });
        serial_number.value = enc_serial_number;
        revoke_pwd.value = enc_revoke_pwd;
        this.sign_serial_number.value = sign_serial_number;
        this.sign_revoke_pwd.value = sign_revoke_pwd;
    }

    addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

</script>

</html>
