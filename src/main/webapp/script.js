var _root_="/StuGradeManagement/";
function _callback(f,loc,vcode_id){$("#status").text(f);if(f.indexOf("成功")>=0){setTimeout(function(){document.location.href=_root_+loc},200)}else if(vcode_id!=null)$("#"+vcode_id).click()}
function submit_login(uname_id,pswd_id,vcode_text_id,vcode_id){var uname=$("#"+uname_id).val(),pswd=$("#"+pswd_id).val(),vcode=$("#"+vcode_text_id).val(),token=Date.parse(new Date())/1000;pswd=window.btoa(pswd).replace(/=/g,'');console.log(pswd);var data_="username="+uname+"&password="+pswd.split('').reverse().join('')+"&verifycode="+vcode+"&token="+token;$.ajax({url:_root_+"login",type:"post",data:data_,processData:false,contentType:"application/x-www-form-urlencoded",success:function(f){_callback(f,"welcome.jsp",vcode_id)}})}
function submit_register(uname_id,pswd_id,vcode_text_id,vcode_id){var uname=$("#"+uname_id).val(),pswd=$("#"+pswd_id).val(),vcode=$("#"+vcode_text_id).val();var data_="username="+uname+"&password="+pswd+"&verifycode="+vcode;$.ajax({url:_root_+"register",type:"post",data:data_,processData:false,contentType:"application/x-www-form-urlencoded",success:function(f){_callback(f,"index.html",vcode_id)}})}
function submit_updateuser(uname,old_pswd_id,new_pswd_id){var old_pswd=$("#"+old_pswd_id).val(),new_pswd=$("#"+new_pswd_id).val();var data_ = "&uname=" + uname + "&old=" + old_pswd + "&new=" + new_pswd;$.ajax({url:_root_+"updateuser",type:"post",data:data_,processData:false,contentType:"application/x-www-form-urlencoded",success:function(f){$("#status").text(f)}})}
function getCurrentParam(name) {var params = document.location.search.split("&"),index = -1;for (var i = 0; i < params.length; i++) {if (params[i].indexOf(name) >= 0) {index = i;break;}}if (index < 0) return "";return params[index].split("=")[1];}