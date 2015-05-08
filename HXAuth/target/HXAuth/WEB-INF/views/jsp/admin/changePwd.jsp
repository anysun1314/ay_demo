<%--
  User: anyang
  Date: 2014-4-28 上午11:09:10
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="admin_changePwd_form" method="post">
    <input type="hidden" name="adminId" id="adminId" value="${adminId}"/>
    <div class="control-group">
        <label class="control-label">密码<span style="color: red">*</span></label>

        <div class="controls">
            <input type="password" name="loginPwdInput" id="loginPwdInput"
                   class="validate[required]" style="width: 200px" value="">
            <a href="javascript:void(0);" class="btn btn-primary" onclick="reset();">&nbsp;&nbsp;清空&nbsp;&nbsp;</a>
        </div>
    </div>
</form>
<script language="JAVASCRIPT" >

	function reset(){
		$("#loginPwdInput").attr("value","");
		$("#loginPwdInput").focus();
	}

    $("#admin_changePwd_form").form({
        url:'${path}/jsp/admin/savePwd.do',
        onSubmit:function() {
        	//遮罩
            Utils.loading(); 
			 var result = $(this).form('validate');
			 if(!result){
				Utils.loaded();
			 }
	         return result;
        },
        success:function(data) {
        	Utils.loaded();
        	if(data == "empty") {
            	$.messager.alert("系统提示", "密码不能为空!", "info");
            }else if(data == "none") {
            	$.messager.alert("系统提示", "密码未做修改!", "info");
            }else if(data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#changePwd").dialog('close');
            }else{	// if(data == "false") 
            	$.messager.alert("系统提示", "操作失败!", "info");
            }
        }
    });
</script>
</body>
</html>