<%--
  User: anyang
  Date: 2014-5-21 上午9:29:10
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="admin_changeAdminPwd_form" method="post">
    <input type="hidden" name="adminId" id="adminId" value="${adminId}"/>
    <div class="control-group">
        <label class="control-label">原密码<span style="color: red">*</span></label>
        <div class="controls">
            <input class="easyui-validatebox" required="true" missingMessage="原始密码不能为空"
             type="password" name="loginPwdInput" id="loginPwdInput" style="width: 200px" value="">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">新密码<span style="color: red">*</span></label>
        <div class="controls">
            <input class="easyui-validatebox" required="true" missingMessage="新密码不能为空"
             type="password" name="newPassword" id="newPassword" style="width: 200px" value="">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">确认密码<span style="color: red">*</span></label>
        <div class="controls">
            <input class="easyui-validatebox" required="true" missingMessage="确认密码不能为空" validType="equals['#newPassword']" invalidMessage="两次密码输入不一致!"
             type="password" name="passwordAgain" id="passwordAgain" style="width: 200px" value="">
        </div>
    </div>
</form>
<script language="JAVASCRIPT" >
	$.extend($.fn.validatebox.defaults.rules, { 
		equals: { 
	    	validator: function(value,param){ 
	    	return value == $(param[0]).val(); 
	    	}, 
	    	message: 'Field do not match.' 
		} 
	}); 

    $("#admin_changeAdminPwd_form").form({
        url:'${path}/jsp/admin/saveAdminPwd.do',
        onSubmit:function() {
        	return $(this).form('validate');
        },
        success:function(data) {
        	if(data == "empty") {
            	$.messager.alert("系统提示", "输入项不能为空!", "info");
            }else if(data == "wrongPwd") {
                $.messager.alert("系统提示", "原密码错误，请重新输入!", "info");
                $("#loginPwdInput").focus();
            }else if(data == "notEqual") {
            	$.messager.alert("系统提示", "新密码与确认密码输入不一致!", "info");
            }else if(data == "success") {
                $.messager.alert("系统提示", "操作成功，请重新登录!", "info");
                parent.$("#changePwd").dialog('close');
                window.location.href="${path}"; 
            }else{	// if(data == "false") 
            	$.messager.alert("系统提示", "操作失败!", "info");
            }
        }
    });
</script>
</body>
</html>