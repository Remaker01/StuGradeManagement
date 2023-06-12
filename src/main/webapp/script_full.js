var _root_= "/StuGradeManagement/";
//登录时，密码进行MD5加密；注册时，把密码进行MD5加密后的结果与原密码拼接起来
function _callback(f,loc,vcode_id) {
    $("#status").text(f);
    if(f.indexOf("成功")>=0) {
        setTimeout(function () {
            document.location.href=_root_+loc;
        },400);
    }
    else if (vcode_id != null)
        $("#"+vcode_id).click();
}
///提交到后端
function submit_login(uname_id, pswd_id, vcode_text_id,vcode_id) { //名称不能为submit，否则调用不了这个函数
    var uname = $("#"+uname_id).val(),pswd = $("#"+pswd_id).val(),vcode = $("#"+vcode_text_id).val(),token = Date.parse(new Date())/1000;//这里不能.value，否则均为undefined
    pswd = md5(pswd,null,false);
    var data_ = "username=" + uname + "&password=" + pswd + "&verifycode=" + vcode + "&token=" + token; //前面不能加'?'，否则后端无法处理
    // console.debug(data_);
    $.ajax({
        url:_root_+"login",
        type:"post",
        data:data_,
        processData: false,
        contentType:"application/x-www-form-urlencoded",
        success:function (f) {_callback(f,"welcome.jsp",vcode_id);}
    });
}
function submit_register(uname_id, pswd_id, vcode_text_id, vcode_id) {
    var uname = $("#"+uname_id).val(),pswd = $("#"+pswd_id).val(),vcode = $("#"+vcode_text_id).val();
    pswd = md5(pswd)+pswd;
    var data_ = "username=" + uname + "&password=" + pswd + "&verifycode=" + vcode;
    // console.debug(data_);
    $.ajax({
        url:_root_+"register",
        type: "post",
        data:data_,
        processData: false,
        contentType:"application/x-www-form-urlencoded",
        success:function (f) {_callback(f,"index.html",vcode_id);}
    });
}
function submit_updateuser(uname,old_pswd_id,new_pswd_id) {
    var old_pswd=$("#"+old_pswd_id).val(),new_pswd=$("#"+new_pswd_id).val();
    old_pswd = md5(old_pswd,null,false);new_pswd = md5(new_pswd,null,false)+new_pswd;
    var data_ = "uname=" + uname + "&old=" + old_pswd + "&new=" + new_pswd;
    $.ajax({
        url:_root_+"updateuser",
        type:"post",
        data:data_,
        processData:false,
        contentType:"application/x-www-form-urlencoded",
        success:function (f) {$("#status").text(f);}
    });
}
function getCurrentParam(name) {
    var params = document.location.search.split("&"),index = -1;
    for (var i = 0; i < params.length; i++) {
        if (params[i].indexOf(name) >= 0) {
            index = i;
            break;
        }
    }
    if (index < 0)
        return "";
    return params[index].split("=")[1];
}
function delayedReload(delay) {
    setTimeout(function (){location.reload();;},delay);
}
function getPass(data) {
    var _new = '';
    data = window.btoa(data).replace('=','');
    for (var i = 0; i < data.length; i += 2) {
        if (i+1>data.length)
            break;
        _new += data[i+1];
        _new += data[i];
    }
    return _new;
}