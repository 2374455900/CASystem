var uname = document.getElementById('username');
var uemail = document.getElementById('emailing');
var upwd = document.getElementById('password');
var u_pwd = document.getElementById('u_password');
var message = document.querySelectorAll('.message');
var error1 = false, error2 = false, error3 = false, error4 = false;
var sub = document.querySelector('.submit');
var check = document.getElementById('check');
var flag = 0;
// 判断用户名称
uname.onblur = function () {
    if (this.value.length < 5 || this.value.length > 16) {
        message[0].className = 'message wrong'
        message[0].innerHTML = '输入不符合要求';
        error1 = false;
    } else {
        message[0].className = 'message correct'
        message[0].innerHTML = '您输入正确';
        error1 = true;
    }
}
// 判断邮箱
uemail.onblur = function () {
    var reg = /^[A-Za-z0-9_.]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    let isok = reg.test(this.value);
    if (this.value === '' || !isok) {
        message[1].className = 'message wrong'
        message[1].innerHTML = '邮箱不符合要求';
        error2 = false;

    } else {
        message[1].className = 'message correct'
        message[1].innerHTML = '您输入正确';
        error2 = true;

    }
}
// 判断密码
upwd.onblur = function () {
    if (this.value.length < 5 || this.value.length > 18) {
        message[2].className = 'message wrong'
        message[2].innerHTML = '输入不符合要求';
        error3 = false;

    } else {
        message[2].className = 'message correct'
        message[2].innerHTML = '您输入正确';
        error3 = true;

    }
}
// 确认密码判断
u_pwd.onblur = function () {
    if (this.value !== upwd.value) {
        message[3].className = 'message wrong'
        message[3].innerHTML = '输入不符合要求';
        error4 = false;

    } else {
        message[3].className = 'message correct'
        message[3].innerHTML = '您输入正确';
        error4 = true;

    }
}
check.onclick = function () {
    if (flag === 0) {
        sub.onmouseover = function () {
            sub.style.backgroundColor = ' #ff8500';
        }
        sub.onmouseout = function () {
            sub.style.backgroundColor = '#000';
        }
        sub.removeAttribute('disabled');
        sub.style.backgroundColor = '#000';
        sub.style.cursor = 'pointer';
        flag = 1;
    } else {
        sub.disabled = 'true';
        sub.style.cursor = 'default';
        sub.style.backgroundColor = '#999';
        flag = 0;
    }
}
