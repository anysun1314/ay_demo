<%--
  User: anyang
  Date: 2014-4-8 上午11:49:14
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
<form class="form-horizontal" id="function_showFunction_form" method="post" style="padding: 3px 0px 0px 10px">
    
    <div class="control-group">
    	<label class="control-label" id="showParentNode" style="width:auto;"><b id="parentNodeName"></b> >> <b id="childNodeName"></b></label>
    </div>
    
    <div class="control-group">
        <label class="control-label">权限名称</label>
        <div class="controls-text" id="functionName" style="width:500px;">${functionInfo.functionName}&nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">权限描述</label>
        <div class="controls-text" id="functionDesc" style="width:500px;">${functionInfo.functionDesc}&nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">权限编码</label>
        <div class="controls-text" id="functionCode" style="width:500px;">${functionInfo.functionCode}&nbsp;</div>
    </div>
    
    <!-- 权限url -->
    <div style="padding-left:111px;padding-top:10px;display:none;" id="hide" >
	    <table id="functionUrl_list">
	        <thead>
	        <th data-options="field:'urlId',hidden:true"></th>
	        <th data-options="field:'urlInfo',width:497">匹配的URL列表</th>
	        </thead>
	    </table>
    </div>
    
    <div id="function_functionsList_toolbar" style="height:auto;padding:3px 111px">
	    <a href="javascript:void(0);" class="btn btn-primary"
							iconCls="icon-edit" plain="true" onclick="editFunction();">&nbsp;&nbsp;修改&nbsp;&nbsp;</a>
	</div>
</form>

<script language="JAVASCRIPT" >
	
	function init(){
		//显示当前标签的父级标签
		var editingId = parent.$("#showFunctionTree").treegrid("getSelected").id;
		var editingPid = parent.$("#showFunctionTree").treegrid("getParent",editingId);
		if(editingPid == null) {
			editingPid = 0;
		}
		if (!editingId) {
			editingId = -1;
		}
		
		var childName = '${childName}';
		var parentName = '${parentName}';
		if(childName != null){
			$("#childNodeName").text(childName);
		}
		if(parentName != null){
			$("#parentNodeName").text(parentName);
		}
		
		//显示权限对应Url
		$("#functionUrl_list").datagrid({
	        title: '权限URL',
	        url: '${path}/jsp/function/showUrls.do?functionId=' + '${functionInfo.functionId}',
	        width: 500,
	        onLoadSuccess:function(data){  
		        if(data.total > 0) {
		        	$("#hide").css("display","");
		        }
		    }
	    });
	}
	
	function editFunction() {
		getEditingInfo();
		//判断是否选中节点 
		if(editingId==-1){
			alert("请先选择权限节点");
			return;
		}
		
		$("#showFunction").tabs('update', {
			tab: $("#showFunction").tabs('getTab',"权限信息"),
			options: {
				href: "${path}/jsp/function/editFunction.do?editingId="+editingId
			}
		});
		$("#showFunction").tabs('select',"权限信息");
	}
	
	function deleteFunction() {
		getEditingInfo();
		//判断是否选中节点 
		if(editingId==-1){
			alert("请先选择权限节点");
			return;
		}
		
		$.messager.confirm('确认信息','是否确认删除该权限？',function(r){
			if(r){
				try {
                	$.post("${path}/jsp/function/deleteFunction.do?editingId="+editingId,
                        	function(data){
                        		if(data == "success"){
                        			$.messager.alert("系统提示", "操作成功!", "info");
                        			parent.$("#showFunctionTree").treegrid("reload");//刷新树结构
                             	    parent.$("#showFunctionTree").treegrid({
                         				onLoadSuccess : function() {
                         	            	   parent.$("#showFunctionTree").treegrid("select",editingPid);
                         	            	   parent.$("#showFunction").tabs('update', {
                         		           			tab: $("#showFunction").tabs('getTab',"权限信息"),
                         		           			options: {
                         		           				href: "${path}/jsp/function/showFunction.do?functionId="+editingPid
                         		           			}
                         		           	   });
                         		           	   parent.$("#showFunction").tabs('select',"权限信息");
                         				}
                         		    });
                        		}
                        		if(data == "error"){
                        			$.messager.alert("系统提示", "操作失败!", "info");
                        			return;
                        		}
                        		if(data == "used"){
                        			$.messager.alert("系统提示", "该权限被角色关联，不可以删除!", "info");
                        			return;
                        		}
                	  		});
                } catch (e) {
                    alert("删除失败！");
                    return;
                }
			}
		});
	}
	
	init();
</script>
</body>
</html>