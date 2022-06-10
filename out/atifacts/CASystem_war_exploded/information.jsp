<%--
  Created by IntelliJ IDEA.
  User: Masker
  Date: 2022/6/8
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>个人中心</title>
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
            <%--            <li class="info"><a href="Get_private_public_key.jsp">获取公钥</a> </li>--%>
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
            <div class="login_title"></div>
            <div id="login_node" style="display:block; margin-left: 50px;">
                <%--                    <ul>转入卡号<label><input type="text" name="turn_id" id="turn_id" class="input_on" /></label></ul>--%>
                <%--                    <ul>转入金额<label><input type="text" name="turnmoney" id="turnmoney" class="input_on" /></label>--%>
                <%--                    </ul>--%>
                <%--                    <a class="a-upload">--%>
                <%--                        <input type="submit" name="button" id="button" value="提交" />提交--%>
                <%--                    </a>--%>
                <ul>
                    <li class="info">
                        <form action="DownloadServlet?filename=genkey.exe" method="post"><input type="submit"
                                                                                                value="密 钥 生 成 器"
                                                                                                class="input_on">
                        </form>
                    </li>
                </ul>
            </div>
        </div>
        <div class="clear"></div>
    </div>

    <div class="container" style="width: 75%;">
        <h3>证 书 列 表</h3>
        <table>
            <tr class="tr-header">
                <th>序列号</th>
                <th>组织机构</th>
                <th>工商注册号</th>
                <th>证书有效期起</th>
                <th>证书有效期止</th>
                <th>下载链接</th>
            </tr>
            <c:forEach items="${requestScope.certItems}" var="certItem" varStatus="s">
                <tr class="tr-body">
                    <th>${certItem.serial_number}</th>
                    <th>${certItem.organization}</th>
                    <th>${certItem.registration_number}</th>
                    <th>${certItem.start_time}</th>
                    <th>${certItem.end_time}</th>
                    <th><a
                            href="DownloadCerServlet?serial_number=${certItem.serial_number}&no_check_code=123">下 载</a>
                    </th>
                </tr>
            </c:forEach>
            <tr class="tr-footer">
                <td colspan="3"
                    style="text-align: right; padding-right: 20px; padding-top: 5px; padding-bottom:
                    5px;">当前为第
                    ${page.currentPage}
                    页，共
                    ${page.totalPage} 页
                </td>
                <td colspan="3" style="text-align: left; padding-left: 20px; padding-top: 5px; padding-bottom:
                    5px;">
                    <c:choose>
                        <c:when test="${page.hasPrePage}">
                            <a href="certificateInfoServlet?currentPage=1">首页</a> |
                            <a href="certificateInfoServlet?currentPage=${page.currentPage-1}">
                                上一页</a>
                        </c:when>
                        <c:otherwise>
                            首页 | 上一页
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${page.hasNextPage}">
                            <a href="certificateInfoServlet?currentPage=${page.currentPage+1}">下一页
                            </a> |
                        </c:when>
                        <c:otherwise>
                            下一页 | 尾页
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
        <div class="clear"></div>

    </div>

    <body>
    …

    <canvas class="background"></canvas>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/particlesjs/2.2.2/particles.min.js"></script>
    </body>
    <div id="div1"><img src="images/b.jpg"/></div>
</div>
</body>

</html>