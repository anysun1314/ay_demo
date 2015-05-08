<%--
  User: anyang
  Date: 2014-5-8 上午9:43:01
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="admin_addAdminRole_form" method="post">
    <input type="hidden" name="departmentCode" id="departmentCode" value="${departmentCode}"/>
    <input type="hidden" name="adminId" id="adminId" value=""/>
    <input type="hidden" name="adminName" id="adminName" value=""/>
    <div class="control-group">
        <label class="control-label">工号<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="工号不能为空"
                   name="adminCode" id="adminCode" value="">
            <a onclick="searchAdmin();" class="btn btn-primary">查询</a>
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">姓名<span style="color: white">*</span></label>
        <div class="controls">
            <div id="showAdminName" style="padding-top:3px"></div>
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">职位<span style="color: red">*</span></label>
        <div class="controls">
        	<input style="width: 200px"
        	 required="true" missingMessage="职位不能为空" editable="false"
        	 id="positionId" name="positionId" value="" >
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">角色<span style="color: red">*</span></label>
        <div class="controls">
            <input style="width: 200px"
            required="true" missingMessage="角色不能为空" editable="false"
            id="roleId" name="roleId" value="">
        </div>
    </div>
</form>
<script language="JAVASCRIPT" >

	$("#adminCode").keyup(function(e){
	    if(e.keyCode == 13){
	    	searchAdmin();
	    }
	});

	function searchAdmin() {
		var adminCode = $("#adminCode").val();
		if(adminCode == '') {
			$.messager.alert("系统提示", "请输入工号查询!", "info");
			$("#showAdminName").text("");
			$("#adminCode").focus();
			return;
		}
		$.post('${path}/jsp/department/searchAdmin.do?adminCode=' + adminCode,function(data){
			if(data == false) {
				$.messager.alert("系统提示", "查询出错！", "info");
				$("#showAdminName").text("");
				$("#adminCode").focus();
				return;
			}else if(data == 'empty') {
				$.messager.alert("系统提示", "该工号的员工不存在！", "info");
				$("#showAdminName").text("");
				$("#adminCode").focus();
				return;
			}else {
				$("#showAdminName").text(data.displayName);
				$("#adminId").val(data.adminId);
				$("#adminName").val(data.displayName);
			}
        })
	}

    $("#admin_addAdminRole_form").form({
        url:'${path}/jsp/department/saveAdminRole.do',
        onSubmit:function() {
        	if($(this).form('validate')) {
	        	if($("#adminId").val() == '') {
	        		$.messager.alert("系统提示", "员工查询有误，请重新查询!", "info");
	        		return false;
	        	}
        		return true;
        	}else {
        		return false;
        	}
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
            if (data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#addAdmin").dialog("close");
                parent.$("#admin_adminsList_list").datagrid("reload");
            }else if(data == "emptyAdmin") {
            	$.messager.alert("系统提示", "员工不存在!", "info");
            }else if(data == "wrong") {
            	$.messager.alert("系统提示", "请核对填写的信息!", "info");
            }
        }
    })
    
    //职位下拉框--注意加验证时，不要再加class='easyui-validatebox'，否则提示框验证移位
	$('#positionId').combobox({ 
		url:'${path}/jsp/position/findPositionNames.do', 
		valueField:'id', 
		textField:'text',
	});
	
	//角色下拉框
	$('#roleId').combobox({ 
		url:'${path}/jsp/role/findRoleNames.do', 
		valueField:'id', 
		textField:'text',
	});
	
	$("#adminCode").focus();
</script>
</body>
</html>