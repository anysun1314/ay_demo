<%--
  User: anyang
  Date: 2014-4-8 上午11:49:14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@ taglib uri="http://www.hexindaiauth.com/jsp/taglib" prefix="mis" %>
<link rel="stylesheet" href="${path}/js/themes/bootstrap/easyui.css">
<link rel="stylesheet" href="${path}/js/themes/icon.css">
<script type="text/javascript" src="${path}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${path}/js/locale/easyui-lang-zh_CN.js"></script>
<html>
<head>
    <title></title>
</head>
<body>
<mis:PermisTag code="010301"> 
<form class="form-horizontal" id="department_showDepartment_form" method="post" style="padding: 3px 0px 0px 10px">
    
    <div class="control-group">
    	<label class="control-label" id="showParentNode" style="width:auto;"><b id="parentNodeName"></b> >> <b id="childNodeName"></b></label>
    </div>
    
    <div class="control-group">
        <label class="control-label">组织编码</label>
        <div class="controls-text" id="departmentCode" style="width:500px;">${department.departmentCode}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">组织名称</label>
        <div class="controls-text" id="departmentName" style="width:500px;">${department.departmentName}&nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">节点类型</label>
        <div class="controls-text" id="nodeType" style="width:500px;">${nodeTypeName}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">类型值</label>
        <div class="controls-text" id="nodeValue" style="width:500px;">${department.nodeValue}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">相关描述</label>
        <div class="controls-text" id="deptDesc" style="width:500px;">${department.deptDesc}&nbsp;</div>
    </div>
    
 	<div class="control-group">
        <label class="control-label">资金账户</label>
        <div class="controls-text" id="enableAccount" style="width:500px;">&nbsp;</div>
    </div>
    
    <div class="control-group" id="loginNameDiv" style="display:none">
        <label class="control-label">登录名</label>
        <div class="controls-text" id="loginName" style="width:500px;">${loginName}&nbsp;</div>
    </div>
    <div id="department_departmentsList_toolbar" style="height:auto;padding:3px 111px">
	    <mis:PermisTag code="010303"> 
	    <a href="javascript:void(0);" class="btn btn-primary"
							iconCls="icon-edit" plain="true" onclick="editDepartment();">&nbsp;&nbsp;修改&nbsp;&nbsp;</a>
		</mis:PermisTag>
		<mis:PermisTag code="010304"> 
		<a href="javascript:void(0);" class="btn btn-primary"
							iconCls="icon-remove" plain="true" onclick="deleteDepartment();">&nbsp;&nbsp;删除&nbsp;&nbsp;</a>
		</mis:PermisTag>
	</div>
</form>
</mis:PermisTag>
<script language="JAVASCRIPT" >
	
	function init(){
		//显示当前标签的父级标签
		var editingId = parent.$("#showTree").treegrid("getSelected").id;
		var editingPid = parent.$("#showTree").treegrid("getParent",editingId);
		if (!editingId) {
			editingId = -1;
		}
		
		var childNode = '${childNode}';
		var parentNode = '${parentNode}';
		if(childNode != null){
			$("#childNodeName").text(childNode);
		}
		if(parentNode != null){
			$("#parentNodeName").text(parentNode);
		}
		
		//登录名的显示启用
		val = '${department.enableAccount}';
		if(val==1){
			$("#enableAccount").text("启用");
			$("#loginNameDiv").css("display","");
		}
		if(val==0){
			$("#enableAccount").text("不启用");
		}
	}
	
	init();
	
	function editDepartment() {
		getEditingInfo();
		//判断是否选中节点 
		if(editingId==-1){
			alert("请先选择组织结构节点");
			return;
		}
		$("#showDepartment").tabs('update', {
			tab: $("#showDepartment").tabs('getTab',"节点信息"),
			options: {
				href: "${path}/jsp/department/editDepartment.do?editingId="+editingId+"&editingPid="+editingPid
			}
		});
		$("#showDepartment").tabs('select',"节点信息");
	}
	
	function deleteDepartment() {
		getEditingInfo();
		//判断是否选中节点 
		if(editingId==-1){
			alert("请先选择组织结构节点");
			return;
		}
		
		$.messager.confirm('确认信息','是否确认删除该组织机构？',function(r){
			if(r){
				try {
                	$.post("${path}/jsp/department/deleteDepartment.do?editingId="+editingId,
                        	function(data){
                        		if(data == "hasAdmin"){
                        			$.messager.alert("系统提示", "该节点或其子节点下存在活动用户，不可以删除！", "info");
                        			return;
                        		}
                        		if(data == "success"){
                        			$.messager.alert("系统提示", "操作成功!", "info");
                        			parent.$("#showTree").treegrid("reload");//刷新树结构
                        			//注意！需要在onLoadSuccess后执行
                             	    parent.$("#showTree").treegrid({
                         				onLoadSuccess : function() {
                         	            	   parent.$("#showTree").treegrid("select",editingPid);
                         	            	   parent.$("#showDepartment").tabs('update', {
                         		           			tab: $("#showDepartment").tabs('getTab',"节点信息"),
                         		           			options: {
                         		           				href: "${path}/jsp/department/showDepartment.do?editingId="+editingPid
                         		           			}
                         		           	   });
                         		           	   parent.$("#showDepartment").tabs('select',"节点信息");
                         				}
                         		    });
                        		}
                        		
                	  		});
                } catch (e) {
                    alert("删除失败！");
                    return;
                }
			}
		});
	}
    
</script>
</body>
</html>