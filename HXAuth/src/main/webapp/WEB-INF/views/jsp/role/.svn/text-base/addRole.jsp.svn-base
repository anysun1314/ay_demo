<%--
  User: anyang
  Date: 2014-3-27 下午3:51:29
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="role_addRole_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="roleId" id="roleId" value="${roleId}"/>
    <input type="hidden" name="roleCode" id="roleCode" value="${roleInfo.roleCode}"/>
    <div class="control-group">
        <label class="control-label">角色名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="角色名称不能为空"
                   name="roleName" id="roleName" value="${roleInfo.roleName}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">相关描述<span style="color: white">*</span></label>

        <div class="controls">
            <input type="text"
                   class="validate[required] text-input" style="width: 200px"
                   name="roleDesc" id="roleDesc" value="${roleInfo.roleDesc}">
        </div>
    </div>
</form>
<script language="JAVASCRIPT" >

    $("#role_addRole_form").form({
        url:'${path}/jsp/role/saveRole.do',
        onSubmit:function() {
        	//遮罩
            Utils.loading(); 
			 var result = $(this).form('validate');
			 if(!result){
				Utils.loaded();
			 }
	         return result;
        	//return $(this).form('validate');
        },
        success:function(data) {
        	Utils.loaded();
            if (data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#addRole").dialog("close");
                parent.$("#role_rolesList_list").datagrid("reload");
            }else if(data == "exist") {
            	$.messager.alert("系统提示", "角色已存在!", "info");
            }
        }
    })
</script>
</body>
</html>