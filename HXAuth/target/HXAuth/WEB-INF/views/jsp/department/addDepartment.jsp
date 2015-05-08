<%--
  User: anyang
  Date: 2014-4-4 上午11:35:38
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<link rel="stylesheet" href="${path}/js/themes/bootstrap/easyui.css">
<link rel="stylesheet" href="${path}/js/themes/icon.css">
<script type="text/javascript" src="${path}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${path}/js/locale/easyui-lang-zh_CN.js"></script>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="department_saveDepartment_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="departmentCode" id="departmentCode" value="${departmentCode}"/>
    <input type="hidden" name="pDeptCode" id="pDeptCode" value="${pDeptCode}"/>
    <!-- <input type="hidden" name="customerAccountId" id="customerAccountId" value="${customerAccountId}"/> -->
    
    <div class="control-group">
    	<label class="control-label" style="width:auto;"><b id="addparentNodeName"></b> >> <b id="addchildName"></b></label>
    </div><!-- 父级节点 >> <b id="parentNode"></b> -->
    
    <div class="control-group">
        <label class="control-label">组织名称<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            	class="easyui-validatebox" required="true" missingMessage="组织机构名不能为空"
                   name="departmentName" id="departmentName" value="${department.departmentName}">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">节点类型<span style="color: white">*</span></label>
        <div class="controls">
			<input id="nodeType" name="nodeType" value="" style="width:200px;">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">类型值<span id="isRequireNodeValue" style="color: white">*</span></label>
        <div class="controls">
           <input id="nodeValue" name="nodeValue" value="" style="width:200px;">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">相关描述<span style="color: white">*</span></label>
        <div class="controls">
            <textarea rows="3" cols="auto" style="width: 200px" name="deptDesc" id="deptDesc">${department.deptDesc}</textarea>
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">资金账户<span style="color: white">*</span></label>
        <div class="controls">
            <input type="radio" name="enableAccount" id="enableAccountOn" value="1" onclick="showLoginName()">启用
            <input type="radio" name=enableAccount id="enableAccountOff" value="0" onclick="hiddenLoginName()">不启用
        </div>
    </div>
    
    <div class="control-group" id="loginNameDiv" style="display:none">
        <label class="control-label">登录名<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   name="loginName" id="loginName" value="${loginName}">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"></label>
        <div class="controls">
        	<a href="javascript:void(0);" class="btn btn-primary"
			onclick="savaDepartment();">&nbsp;&nbsp;&nbsp;提交&nbsp;&nbsp;&nbsp;</a>
        </div>
    </div>
     
</form>
<script language="JAVASCRIPT" >

	function init(){
		//显示当前标签的父级标签
		var childNode = '${childNode}';
		var parentNode = '${parentNode}';
		if(childNode != null){
			$("#addchildName").text(childNode);
		}
		if(parentNode != null){
			$("#addparentNodeName").text(parentNode);
		}
		
		//登录名的显示启用
		var val = 0;
		if('${department.enableAccount}' != "") {
			val = '${department.enableAccount}';
		}
		if(val==1){
			$("#enableAccountOn").attr("checked","checked");
			$("#loginNameDiv").css("display","");
		}
		if(val==0){
			$("#enableAccountOff").attr("checked","checked");
		}
	}
	
	init();

	function showLoginName(){
		$("#loginNameDiv").css("display","");
	}
	function hiddenLoginName(){
		$("#loginNameDiv").css("display","none");
	}
    
    $("#department_saveDepartment_form").form({
        url:'${path}/jsp/department/saveDepartment.do',
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
            var editingId = parent.$("#showTree").treegrid("getSelected").id;
            data = eval('(' + data + ')'); //注意后台返回map--直接return map.put()出错！！
            if(data.departName == 'null') {
            	$.messager.alert("系统提示", "组织名称不能为空!", "info");
            }
            if(data.nodeValueNone == 'none') {
            	$.messager.alert("系统提示", "类型值不能为空!", "info");
            }
            if(data.loginNameNone) {
            	$.messager.alert("系统提示", "登录名不能为空!", "info");
            	return;
            }
            if(data.loginNameExist) {
            	$.messager.alert("系统提示", "登录名重复!", "info");
            	return;
            }
            if(data.enableAccountWrong) {
            	$.messager.alert("系统提示", "上级无可用资金帐户，资金账户必须启用!", "info");
            	return;
            }
            if(data.customerAccount) {
            	$.messager.alert("系统提示", "启用资金账户失败!", "info");
            	return;
            }
            if(data.error) {
            	$.messager.alert("系统提示", "操作失败!", "info");
            	return;
            }
            
            if (data.success) {
           		if(${add}==false){
                  	   parent.$("#showTree").treegrid("reload");//刷新树结构
                  	   //注意！需要在onLoadSuccess后执行
                	   parent.$("#showTree").treegrid({
            				onLoadSuccess : function() {
            	            	   parent.$("#showTree").treegrid("select",editingId);	//TypeError: r is null ??row is NUll
            	            	   parent.$("#showDepartment").tabs('update', {
            		           			tab: $("#showDepartment").tabs('getTab',"节点信息"),
            		           			options: {
            		           				href: "${path}/jsp/department/showDepartment.do?editingId="+editingId
            		           			}
            		           	   });
            	            	   parent.$("#showDepartment").tabs('update', {
	   									tab: $("#showDepartment").tabs('getTab',"维护人员"),
	   									options: {
	   										href: "${path}/jsp/department/showAdmins.do?editingId="+editingId
	   									}
	   							   });
            		           	   parent.$("#showDepartment").tabs('select',"节点信息");
            				}
            		   });
                   }
                   if(${add}){
                	   parent.$("#showTree").treegrid("reload");//刷新树结构
            		   //注意！需要在onLoadSuccess后执行
                	   parent.$("#showTree").treegrid({
            				onLoadSuccess : function() {
            	            	   parent.$("#showTree").treegrid("select",data.newCode);	//TypeError: r is null ??row is NUll
            	            	   parent.$("#showDepartment").tabs('update', {
            		           			tab: $("#showDepartment").tabs('getTab',"节点信息"),
            		           			options: {
            		           				href: "${path}/jsp/department/showDepartment.do?editingId="+data.newCode
            		           			}
            		           	   });
            	            	   parent.$("#showDepartment").tabs('update', {
	   									tab: $("#showDepartment").tabs('getTab',"维护人员"),
	   									options: {
	   										href: "${path}/jsp/department/showAdmins.do?editingId="+data.newCode
	   									}
	   							   });
            		           	   parent.$("#showDepartment").tabs('select',"节点信息");
            				}
            		   });
                   }
               	
                $.messager.alert("系统提示", "操作成功!", "info");
            }
        }
    });
    
    $('#nodeType').combobox({ 
		url:'${path}/jsp/department/findNodeType.do', 
		valueField:'id', 
		textField:'text',
		onSelect: function(rec){ 
			var url = '${path}/jsp/department/findNodeValue.do?id='+rec.consId; 
			$('#nodeValue').combobox('clear');	//加载子列表时，先清空，否则会出现显示不正确
			$('#nodeValue').combobox('reload', url); 
			if(rec.consId != 0) {
				$("#isRequireNodeValue").attr("style","color:red");
        		$("#nodeValue").attr("class","easyui-validatebox");
        		$("#nodeValue").attr("required","true");
        		$("#nodeValue").attr("missingMessage","类型值不能为空");
    		}else {
    			$("#isRequireNodeValue").attr("style","color:white");
    		}
		}

	});
    
    $('#nodeValue').combobox({ 
		valueField:'id', 
		textField:'text',
	});
    
    function savaDepartment() {
		try {
	    	$("#department_saveDepartment_form").submit();
	    } catch (e) {
	        alert(e);
    	}
    }
    
    function forEdit(){
    	if(${add}==false){
    		if('${flag}' != "0"){
    			editNodeType = '${department.nodeType}';
        		editUrl = '${path}/jsp/department/findNodeValue.do?id=${nodeValueId}';
        		editNodeValue = '${department.nodeValue}';
        		$('#nodeType').combobox("select", editNodeType);
        		$('#nodeValue').combobox("reload", editUrl);
        		$('#nodeValue').combobox("select", editNodeValue);
    		}
    	}
    }
    forEdit();
    
	if(${add}==true) {
		$('#nodeType').combobox("select", '0');
	}
	
</script>
</body>
</html>