<%--
  Created by IntelliJ IDEA.
  User: Masker
  Date: 2022/6/8
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>申请证书</title>
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
            <li class="login info"><a style=" padding-bottom:20px; margin-left:8px; " href="information.jsp">个人中心</a>
            </li>
            <li class="info"><a href="apply.jsp">申请证书</a></li>
            <li class="info"><a href="cancel_the_certificate.jsp">注销证书</a></li>
            <li class="info"><a href="download.jsp">下载证书</a></li>
            <li class="info"><a href="changePassword.jsp">修改密码</a></li>
            <li class="info" onClick="firm()"><a href="LogoutServlet">退出</a></li>
        </ul>
    </div>
    <!--nav end-->
    <center>
        <h1 style="color: #FFFFFF">User Login</h1>
    </center>
    <div id="window" class="login_box">
        <div class="float_l">
            <div class="login_title"> 申请证书</div>
            <form name="form1" method="post" action="">
                <div id="login_node" style="display:block; margin-left: 50px;">
                    <ul>用户名<label><input type="text" name="juridical_person" id="juridical_person" class="input_on"/>
                        <input type="text" name="sign_juridical_person" id="sign_juridical_person"
                               style="display: none"></label></ul>
                    <ul>公钥<input type="text" class="input_on" disabled="true" id="upload_file_name"
                                 name="upload_file_name" placeholder="请选择密钥文件">
                        <input type="button" id="upload_btn" name="upload_btn" value="选择上传文件" class="input_on">
                        <input type="file" id="upload_file" name="upload_file" class="input_on"></ul>
                    <ul>国家<label><input type="text" name="country" id="country" class="input_on"></label></ul>
                    <ul>州市<label><input type="text" name="city" id="city" class="input_on"></label></ul>
                    <ul>地区<label><input type="text" name="area" id="area" class="input_on"></label></ul>
                    <ul>组织<label><input type="text" name="organization" id="organize" class="input_on">
                        <input type="text" name="sign_organization" id="sign_organization" style="display: none"></label></ul>
                    <ul>姓名<label><input type="text" name="charge_person" id="charge_person" placeholder="经办人姓名" class="input_on"
                                        required="">
                        <input type="text" name="sign_charge_person" id="sign_charge_person" style="display: none"></label></ul>
                    <ul>有效期
                        <label>
                            <select name="period">
                                <option value="1">1年</option>
                                <option value="2">2年</option>
                                <option value="3">3年</option>
                            </select>
                        </label>
                    </ul>
                    <ul>邮箱<label><input type="text" name="charge_mail" id="charge_mail" class="input_on">
                        <input type="text" name="sign_charge_mail" id="sign_charge_mail" style="display: none"></label></ul>
                    <ul>密码<label><input type="password" name="password" id="password" class="input_on"></label></ul>
                    <a class="a-upload">
                        <input type="submit" name="button" id="button" value="提交"/>提交
                    </a>
                </div>
            </form>
        </div>
        <div class="clear"></div>

        <div style="text-align: center;color: red">证书序列号${serial_number}</div>
    </div>

    <body>
        …

        <canvas class="background"></canvas>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/particlesjs/2.2.2/particles.min.js"></script>
    </body>
</div>
</body>

<script type="text/javascript">
    const upload_btn = document.getElementById("upload_btn");
    const upload_file = document.getElementById("upload_file");
    const upload_file_name = document.getElementById("upload_file_name");
    upload_btn.addEventListener("click", function () {
        upload_file.click();
    });
    upload_file.addEventListener("change", function () {
        upload_file_name.value = upload_file.value;
    });
</script>

<script type="text/javascript">
    var submit_btn = document.getElementById("button");
    var organization = document.getElementById("organization");
    var juridical_person = document.getElementById("juridical_person");
    var charge_person = document.getElementById("charge_person");
    var charge_mail = document.getElementById("charge_mail");
    var sign_organization = document.getElementById("sign_organize");
    var sign_juridical_person = document.getElementById("sign_username");
    var sign_charge_person = document.getElementById("sign_charge_person");
    var sign_charge_mail = document.getElementById("sign_charge_mail");
    submit_btn.addEventListener("click", function () {
        var radios = document.getElementsByName("valid_time");
        var checked;
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) {
                checked = radios[i].value;
            }
        }
        var message = "请确认信息无误：\n" +
            organization.getAttribute("placeholder") + ": " + organization.value + "\n" +
            juridical_person.getAttribute("placeholder") + ": " + juridical_person.value + "\n" +
            charge_person.getAttribute("placeholder") + ": " + charge_person.value + "\n" +
            charge_mail.getAttribute("placeholder") + ": " + charge_mail.value + "\n" + "有效期: " + checked + " 年";
        var r = confirm(message);
        if (r === true) {
            apply_encrypt();
            document.getElementById("apply_form").submit();
            organization.value = "";
            juridical_person.value = "";
            charge_person.value = "";
            charge_mail.value = "";
        } else {
        }
    });

    function apply_encrypt() {
        var publicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLFxU/IBjwf0UjGsXy/dx6QiRH6pvvZoGvtUtDhGT1Wq0Vga2h7CUKwNazN8/l8YksKgz01JqIEh8NUAGOFY3tVMk/sNlMgYqCWyRw6QvMtspLDe3dzJn/83qAsMq3wr3Ooxgk143AXV6YlXhDvipYqwasP7luwRrbgdTANItl3wIDAQAB";
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(publicKey);
        var enc_organization = encrypt.encrypt(organization.value);
        var enc_juridical_person = encrypt.encrypt(juridical_person.value);
        var enc_charge_person = encrypt.encrypt(charge_person.value);
        var enc_charge_mail = encrypt.encrypt(charge_mail.value);
        var sign_organization = hex_sha256(organization.value);
        var sign_juridical_person = hex_sha256(juridical_person.value);
        var sign_charge_person = hex_sha256(charge_person.value);
        var sign_charge_mail = hex_sha256(charge_mail.value);
        organization.value = enc_organization;
        juridical_person.value = enc_juridical_person;
        charge_person.value = enc_charge_person;
        charge_mail.value = enc_charge_mail;
        this.sign_organization.value = sign_organization;
        this.sign_juridical_person.value = sign_juridical_person;
        this.sign_charge_person.value = sign_charge_person;
        this.sign_charge_mail.value = sign_charge_mail;
    }

    addEventListener("load", function () {
        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

</script>

</html>
