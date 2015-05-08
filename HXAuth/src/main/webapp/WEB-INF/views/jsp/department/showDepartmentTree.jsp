<%--
  User: anyang
  Date: 2014-4-21 下午4:30:45
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
	<div data-options="region:'center',border:false">
		<table id="showTree" style="width:800px;">
			<thead>
				<th data-options="field:'name'" width="auto">&nbsp;</th>
			</thead>
		</table>
	</div>
	<!-- 选中的节点信息，供父级页面使用 -->
	<div id="editingId"></div>
	<div id="editingName"></div>
	
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
	$("#editingId").css("display","none");
	$("#editingName").css("display","none");
	
	//TreeGrid的相关配置
	$('#showTree')
		.treegrid(
			{
				url : '${path}/jsp/department/list.do',
				method : 'get',
				collapsible : true,
				fitColumns : true,
				animate : true,
				idField : 'id',
				treeField : 'name',
				onClickRow : function() {
					getEditingInfo();
					$("#editingId").text(editingId);
					$("#editingName").text(editingName);
				}
			});
</script>
</body>
</html>