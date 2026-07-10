var uname = document.getElementById('username');
var upwd = document.getElementById('pwd');
var eye = document.getElementById('eye');
var sub = document.getElementById('sub');
var sub1 = document.querySelector('.submit2');
var flag = 0;
eye.onclick = function () {
    if (flag === 0) {
        upwd.type = 'text'
        eye.src = 'images/open.png';
        flag = 1;
    } else {
        upwd.type = 'password';
        eye.src = "images/close.png";
        flag = 0;
    }
}
sub.onmouseover = function () {
    sub.style.backgroundColor = '#07bcfc';
}
sub.onmouseout = function () {
    sub.style.backgroundColor = '#03a9f4';
}
sub1.onmouseover = function () {
    sub1.style.color = '#333';
}
sub1.onmouseout = function () {
    sub1.style.color = '#999';
}
// sub.onclick = function () {
//     if (uname.value === '') {
//         window.alert('用户名不能为空');
//     }
//     else if (upwd.value === '') {
//         window.alert('密码不能为空');
//     }
//     else {
//         reqwest({
//             url: 'http://39.106.168.39:82/api/auth/login'
//             , type: 'json'
//             , method: 'post'
//             , data: JSON.stringify({
//                 username: uname.value,
//                 password: upwd.value,
//             })
//             , contentType: 'application/json'
//             , error: function (err) {
//                 alert("请求失败！")
//             }
//             , success: function (resp) {
//                 if (resp.Header.header.code !== 200) {
//                     window.alert(resp.Header.header.msg);
//                     window.alert("登录失败！");
//                 }
//                 else {
//                     window.alert("登录成功" + resp.Header.token);
//
//                 }
//
//             }
//         })
//     }
// }
