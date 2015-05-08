<%--
  User: anyang
  Date: 2014-5-15 下午4:30:45
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
	<!-- easyUI-TreeGrid -->
	<div style="padding:5px;"><b>${roleName}</b></div>
	<div data-options="region:'center',border:false">
		<table id="showTree" style="width:450px;">
			<thead>
				<th data-options="field:'id',checkbox:true" width="auto"></th>
				<th data-options="field:'name'" width="auto">权限列表</th>
			</thead>
		</table>
	</div>
	
<script language="JAVASCRIPT" >
	//获取当前正在操作的节点信息
	var editingId;
	var editingName;
	var editingPid;
	function getEditingInfo() {
		var row = $('#showTree').treegrid('getSelected');
		if (row) {
			editingId = row.id;
			editingName = row.name;
			editingPid = row._parentId;
		} else {
			editingId = -1;
		}
	}
	
	//TreeGrid的相关配置
	$('#showTree').treegrid({
		url : '${path}/jsp/function/functionTree.do?roleId=${roleId}',
		method : 'get',
		collapsible : true,
		fitColumns : true,
		animate : true,
		singleSelect : false,
		idField : 'id',
		treeField : 'name',
		onClickRow : function(row) {	//点击树节点时
			 $(this).treegrid('cascadeCheck',{  
                 id:row.id, //当前点击的节点ID  
                 deepCascade:true
             });
		},
		onLoadSuccess : function() {
			if('${functionIds}' != "") {
				var functionIds = '${functionIds}'.split('-');
				$.each(functionIds,function(key,value){
					$('#showTree').treegrid('select',functionIds[key]);
	 	        });
			}
		}
	});
	
    /** 
     * 扩展树表格级联勾选方法： 
     * @param {Object} container 
     * @param {Object} options 
     * @return {TypeName}  
     */  
    $.extend($.fn.treegrid.methods,{  
        /** 
         * 级联选择 
         * @param {Object} target 
         * @param {Object} param  
         *      param包括两个参数: 
         *          id:勾选的节点ID 
         *          deepCascade:是否深度级联 
         * @return {TypeName}  
         */  
        cascadeCheck : function(target,param){ 
        	
            var opts = $.data(target[0], "treegrid").options;  
            if(opts.singleSelect)  
                return;  
            var idField = opts.idField;//这里的idField其实就是API里方法的id参数  
            var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选  
            var selectNodes = $(target).treegrid('getSelections');//获取当前选中项  
            for(var i=0;i<selectNodes.length;i++){  
                if(selectNodes[i][idField]==param.id)  
                    status = true;  
            } 
            //级联选择父节点  
            selectParent(target[0],param.id,idField,status);  
            selectChildren(target[0],param.id,idField,param.deepCascade,status);  
            /** 
             * 级联选择父节点 
             * @param {Object} target 
             * @param {Object} id 节点ID 
             * @param {Object} status 节点状态，true:勾选，false:未勾选 
             * @return {TypeName}  
             */
            function selectParent(target,id,idField,status){ 
                var parent = $(target).treegrid('getParent',id);  
                if(parent){  
                    var parentId = parent[idField];  
                    if(status)  
                        $(target).treegrid('select',parentId);  
                    else  
                        $(target).treegrid('unselect',parentId);  
                    selectParent(target,parentId,idField,status);  
                } 
                
            }  
            /** 
             * 级联选择子节点 
             * @param {Object} target 
             * @param {Object} id 节点ID 
             * @param {Object} deepCascade 是否深度级联 
             * @param {Object} status 节点状态，true:勾选，false:未勾选 
             * @return {TypeName}  
             */  
            function selectChildren(target,id,idField,deepCascade,status){  
                //深度级联时先展开节点  
                if(!status&&deepCascade)  
                    $(target).treegrid('expand',id);  
                //根据ID获取下层孩子节点  
                var children = $(target).treegrid('getChildren',id);  
                for(var i=0;i<children.length;i++){  
                    var childId = children[i][idField];  
                    if(status)  
                        $(target).treegrid('select',childId);  
                    else  
                        $(target).treegrid('unselect',childId);  
                    selectChildren(target,childId,idField,deepCascade,status);//递归选择子节点  
                }  
            }
        }  
    });  
</script>
</body>
</html>