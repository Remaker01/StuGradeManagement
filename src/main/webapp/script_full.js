///提交到后端
function submit_login(uname_id,pswd_id,vcode_id) { //名称不能为submit，否则调用不了这个函数
    // console.log("submitting with "+"#"+uname_id);
    var uname = $("#"+uname_id).val();
    var pswd = $("#"+pswd_id).val();
    var code = $("#"+vcode_id).val(); //这里不能.value，否则均为undefined
    var tp = Date.parse(new Date())/1000;
    var data_ = "username=" + uname + "&password=" + pswd + "&verifycode=" + code + "&token=" + tp; //前面不能加'?'，否则后端无法处理
    // console.log(data_);
    $.ajax({
        url:"/StuGradeManagement_war_exploded/login",
        type:"post",
        data:data_,
        processData: false,
        contentType:"application/x-www-form-urlencoded",
        success:function (data) {$("#status").text(data);window.location}
    });
}
