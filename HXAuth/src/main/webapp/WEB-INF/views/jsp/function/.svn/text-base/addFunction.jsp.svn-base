<%--
  User: anyang
  Date: 2014-5-13 上午11:35:38
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
<form class="form-horizontal" id="function_saveFunction_form" method="post">
    <input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
    <input type="hidden" name="functionCode" id="functionCode" value="${functionCode}"/>
    <input type="hidden" name="functionId" id="functionId" value="${functionId}"/>
    <input type="hidden" name="pFunctionId" id="pFunctionId" value="${pFunctionId}"/>
    <input type="hidden" name="flag" id="flag" value="${flag}"/>
    <!-- 权限URL -->
	<input type="hidden" name="Url" id="Url"/>
	
    <div class="control-group">
    	<label class="control-label" style="width:auto;" id="hide"><b id="addparentNodeName"></b> >> <b id="addchildName"></b></label>
    </div>
    
    <div class="control-group">
        <label class="control-label">权限名称<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            	class="easyui-validatebox" required="true" missingMessage="权限名称不能为空"
                   name="functionName" id="functionName" value="${functionInfo.functionName}">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">权限描述<span style="color: white">*</span></label>
        <div class="controls">
        	<textarea rows="3" cols="auto" style="width: 200px" name="functionDesc" id="functionDesc">${functionInfo.functionDesc}</textarea>
        </div>
    </div>
    
    <!-- 权限匹配URL begin-->
    <div class="control-group">
	    <label class="control-label">权限URL<span style="color: white">*</span></label>
	    <div class="controls">
		    <table id="url_list">
		        <thead>
		        <tr>
		        	<th data-options="checkbox:true"></th>
		        	
		        	<th data-options="field: 'urlId',hidden:true,editor: {
		           		type: 'text'
		            }"></th>
		        
		            <th data-options="field: 'urlInfo', width: 269,editor: {
		           		type: 'text',
		            }">权限匹配URL</th>
		        </tr>
		        </thead>
		    </table>
	    </div>
    </div>
    <!-- 权限匹配URL end-->
    
    <div class="control-group">
        <label class="control-label"></label>
        <div class="controls" style="padding-top:5px">
        	<a href="javascript:void(0);" class="btn btn-primary"
			onclick="savaFunction();">&nbsp;&nbsp;&nbsp;提交&nbsp;&nbsp;&nbsp;</a>
        </div>
    </div>
     
</form>
<script language="JAVASCRIPT" >

	//function URL使用
	var editIndex = undefined;
	$("#function_saveFunction_form #url_list").datagrid({
	    singleSelect: true,
	    rownumbers: false,
	    pagination: false,
	    width: 300,
	    toolbar: [
	        {
	            iconCls: 'icon-add',
	            text: '添加',
	            handler: function () {
	                append();
	            }
	        },
	        {
	            iconCls: 'icon-add',
	            text: '删除',
	            handler: function () {
	                remove();
	            }
	        }
	    ],
	    onLoadSuccess:function(data){  
	        var rows = $('#url_list').datagrid('getRows');
	        $.each(rows,function(key,value){
	        	$('#url_list').datagrid('beginEdit',  $('#url_list').datagrid('getRowIndex',value));
	        })
	        
	    }  //获取加载回来的数据，循环每列，并回显选中
	})

	function init(){
		//层级关系
		var childName = '${childName}';
		var parentName = '${parentName}';
		if(childName == '' && parentName == '') {
			$("#hide").text("");
		}else {
			if(childName != null){
				$("#addchildName").text(childName);
			}
			if(parentName != null){
				$("#addparentNodeName").text(parentName);
			}
		}
		
		if(${add} == false) {
			//编辑时，权限Url的回显
			$("#function_saveFunction_form #url_list").datagrid("options").url=
				'${path}/jsp/function/showUrls.do?functionId=' + '${functionId}';
		}
	}

    $("#function_saveFunction_form").form({
        url:'${path}/jsp/function/saveFunction.do',
        onSubmit:function() {
        	 endEditing();
        	 $('#url_list').datagrid("acceptChanges");
             $("#Url").val(JSON.stringify($("#url_list").datagrid("getData")));
             
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
        	
            var editingId = parent.$("#showFunctionTree").treegrid("getSelected").id;
            data = eval('(' + data + ')');
            if(data.name == 'null') {
            	returnMsg();
            	$.messager.alert("系统提示", "权限名称不能为空!", "info");
            }
            if(data.equal) {
            	returnMsg();
            	$.messager.alert("系统提示", "存在重复URL，请检查!", "info");
            }
            if(data.error) {
            	returnMsg();
            	$.messager.alert("系统提示", "操作失败!", "info");
            }
            if(data.success) {
            	   var transId;
           		   if(${add}==false){
           			   transId = editingId;
                   }
                   if(${add}){
                	   transId = data.newId;
                   }
                   //alert(transId);
            	   parent.$("#showFunctionTree").treegrid("reload");//刷新树结构
            	   parent.$("#showFunctionTree").treegrid({
        				onLoadSuccess : function() {
        	            	   parent.$("#showFunctionTree").treegrid("select",transId);
        	            	   parent.$("#showFunction").tabs('update', {
        		           			tab: $("#showFunction").tabs('getTab',"权限信息"),
        		           			options: {
        		           				href: "${path}/jsp/function/showFunction.do?functionId="+transId
        		           			}
        		           	   });
        		           	   parent.$("#showFunction").tabs('select',"权限信息");
        				}
        		   });
                   $.messager.alert("系统提示", "操作成功!", "info");
            }
        }
    });
    
    //返回提示信息后，返回编辑状态
    function returnMsg() {
       	var rows = $('#url_list').datagrid('getRows');
        $.each(rows,function(key,value){
        	$('#url_list').datagrid('beginEdit',  $('#url_list').datagrid('getRowIndex',value));
        })
    }
    
    function savaFunction() {
		try {
	    	$("#function_saveFunction_form").submit();
	    } catch (e) {
	        alert(e);
    	}
    }
    
    //4 function URL
    function append() {
        $('#url_list').datagrid('appendRow', {});
        editIndex = $('#url_list').datagrid('getRows').length - 1;
        $('#url_list').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);
    }

    function remove() {
        var selRow = $("#url_list").datagrid("getSelected");
        if (selRow) {
            $.messager.confirm("系统提示", "是否确定删除该行？", function (data) {
                if (data) {
                    $("#url_list").datagrid("deleteRow",
                            $("#url_list").datagrid("getRowIndex", selRow));
                }
            })
        } else {
            $.messager.alert("系统提示", "请选择要删除的行!", "info");
        }
    }
    
    function endEditing() {
        if (editIndex == undefined) {
            return true;
        }
        if ($('#url_list').datagrid('validateRow', editIndex)) {
        	var functionUrlId = $('#url_list').datagrid('getEditor', {index: editIndex, field: 'urlId'});
            var functionUrlInfo = $('#url_list').datagrid('getEditor', {index: editIndex, field: 'urlInfo'});
            
            if(functionUrlId != null) {
            	$('#url_list').datagrid('getRows')[editIndex]['urlId'] = $(functionUrlId.target).text();
            }
            if(functionUrlInfo != null) {
            	$('#url_list').datagrid('getRows')[editIndex]['urlInfo'] = $(functionUrlInfo.target).text();
            }
            return true;
        }else {
            return false;
        }
    }
    
    init();
</script>
</body>
</html>