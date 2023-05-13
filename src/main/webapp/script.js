var _root_= "/StuGradeManagement_war_exploded/";
function _callback(f,loc,vcode_id=null) {
    $("#status").text(f);
    if(f.indexOf("成功")>=0) {
        setTimeout(function () {
            window.location.href=_root_+loc;
        },200);
    }
    else if (vcode_id != null)
        $("#"+vcode_id).click();
}
///提交到后端
function submit_login(uname_id, pswd_id, vcode_text_id,vcode_id) { //名称不能为submit，否则调用不了这个函数
    var uname = $("#"+uname_id).val(),pswd = $("#"+pswd_id).val(),vcode = $("#"+vcode_text_id).val(),token = Date.parse(new Date())/1000;//这里不能.value，否则均为undefined
    var data_ = "username=" + uname + "&password=" + pswd + "&verifycode=" + vcode + "&token=" + token; //前面不能加'?'，否则后端无法处理
    console.debug(data_);
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
    var data_ = "username=" + uname + "&password=" + pswd + "&verifycode=" + vcode;
    console.debug(data_);
    $.ajax({
        url:_root_+"register",
        type: "post",
        data:data_,
        processData: false,
        contentType:"application/x-www-form-urlencoded",
        success:function (f) {_callback(f,"index.html",vcode_id);}
    });
}
function submit_updateuser(id,uname,old_pswd_id,new_pswd_id) {
    var old_pswd=$("#"+old_pswd_id).val(),new_pswd=$("#"+new_pswd_id).val();
    var data_ = "id=" + id + "&uname=" + uname + "&old=" + old_pswd + "&new=" + new_pswd;
    $.ajax({
        url:_root_+"updateuser",
        type:"post",
        data:data_,
        processData:false,
        contentType:"application/x-www-form-urlencoded",
        success:function (f) {$("#status").text(f);}
    });
}