<%--
  User: anyang
  Date: 2014-4-1 下午1:13:12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp"%>

<body>
	<!-- easyUI-layout begin -->
	<div class="easyui-layout" style="width:100%;height:85%;">
		<div data-options="region:'west',split:true" title="和信公司"
			style="width:480px;">
			<!-- 内嵌的layout，用来区分按钮和树结构 -->
			<div data-options="region:'north',split:true,border:false"
				style="height:30px">
				<div id="department_departmentsList_toolbar" style="height:auto">
					<mis:PermisTag code="010302"> 
					<a href="javascript:void(0);" class="easyui-linkbutton"
						iconCls="icon-add" plain="true" onclick="addDepartment();">新增</a>
					</mis:PermisTag>
				</div>
			</div>
			<!-- easyUI-TreeGrid -->
			<div data-options="region:'center',border:false">
				<table id="showTree" style="width:800px;">
					<thead>
						<th data-options="field:'name'" width="auto">&nbsp;</th>
					</thead>
				</table>
			</div>
		</div>
		<!-- 节点详情 -->
		<div data-options="region:'center'" id="departmentDetail" title="详细信息"
			style="padding:15px">
			
			<div id="detail" class="container-fluid" style="padding: 5px 0px 0px 10px">
			    <div id="showDepartment" class="easyui-tabs" style="width:auto;height:auto">
			    	<div title="节点信息" data-options="href:''" style="padding:10px"></div>
			    	<div title="维护人员" data-options="href:''" style="padding:10px"></div>
			    </div>
			</div>
		</div>
	</div>
	<!-- easyUI-layout end -->



<script language="javascript">
		//可以添加js，在页面加载时控制tree的节点状态，open、close		

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
								//节点信息
								$("#showDepartment").tabs('update', {
									tab: $("#showDepartment").tabs('getTab',"节点信息"),
									options: {
										href: "${path}/jsp/department/showDepartment.do?editingId="+editingId
									}
								});
								$("#showDepartment").tabs('update', {
									tab: $("#showDepartment").tabs('getTab',"维护人员"),
									options: {
										href: "${path}/jsp/department/showAdmins.do?editingId="+editingId
									}
								});
								
							}
						});

		function addDepartment() {
			getEditingInfo();
			//判断是否选中节点 
			if(editingId==-1){
				alert("请先选择组织结构节点");
				return;
			}
			
			$("#showDepartment").tabs('update', {
				tab: $("#showDepartment").tabs('getTab',"节点信息"),
				options: {
					href: "${path}/jsp/department/addDepartment.do?editingId="+editingId+"&editingPid="+editingPid
				}
			});
			$("#showDepartment").tabs('select',"节点信息");
		}

    function test(){
        $.post('${path}/jsp/department/test.do?deptCode=' + $('#showTree').treegrid('getSelected').id,function(data){

        })
    }

</script>

</body>