<%--
  User: anyang
  Date: 2014-4-21 下午7:57:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="position_addPosition_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="positionId" id="positionId" value="${positionId}"/>
    <div class="control-group">
        <label class="control-label">职位名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="职位名称不能为空"
                   name="positionName" id="positionName" value="${position.positionName}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">相关描述<span style="color: white">*</span></label>

        <div class="controls">
            <input type="text"
                   class="validate[required] text-input" style="width: 200px"
                   name="positionDesc" id="positionDesc" value="${position.positionDesc}">
        </div>
    </div>
</form>
<script language="JAVASCRIPT" >

    $("#position_addPosition_form").form({
        url:'${path}/jsp/position/savePosition.do',
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
                parent.$("#addPosition").dialog("close");
                parent.$("#position_positionsList_list").datagrid("reload");
            }else if(data == "exist") {
            	$.messager.alert("系统提示", "职位已存在!", "info");
            }
        }
    })
</script>
</body>
</html>