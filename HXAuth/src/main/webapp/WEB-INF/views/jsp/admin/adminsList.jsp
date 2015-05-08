<%--
  User: anyang
  Date: @date 2014-4-17 下午4:31:00
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<body>
<div id="admin_adminsList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="" style="height:auto;padding-left: 30px;">
        <form action="/#" id="conditionSearch" method="post">
            <span>姓名:</span><input type="text" id="displayNameSearch" value="" size=10/>
            <span>工号:</span><input type="text" id="adminCodes" value="" size=10/>
            <span>邮箱:</span><input type="text" id="email" value="" size=10/>
            <span>手机号:</span><input type="text" id="telPhone" value="" size=10/>
            <span>职位:</span><input id="positionIds" value="" size=10>
            <span>状态:</span>
            <select class="easyui-combobox" id="adminStateValue" value="" style="width:200px;">
                <option value="">全部</option>
                <option value="1" >有效</option>
                <option value="0" >无效</option>
                <option value="2" >离职</option>
            </select>
            <span>组织机构:</span><input type="text" id="searchDepartment" value="" size=10/>
            <input type="hidden" id="searchDepartmentCode" value="" size=10/>
			<a href="javascript:void(0);" class="btn btn-primary" onclick="selectSearchDepartment();">&nbsp;选择&nbsp;</a>&nbsp;&nbsp;
            <a onclick="conditionSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
        </form>
    </div>
    <div id="admin_adminsList_toolbar" style="height:auto">
        <mis:PermisTag code="010201">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addAdmin();">新增</a>
        </mis:PermisTag>
    </div>

    <table id="admin_adminsList_list"></table>
</div>

<script language="javascript">
    function doSearch() {
        $("#admin_adminsList_list").datagrid({
            idField: 'adminId',
            title: '员工列表',
            url: '${path}/jsp/admin/listByPage.do',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#admin_adminsList_toolbar',
            columns:[[
                      {field:'adminId',checkbox:true},
                      {field:'adminCode', width:160,title:'工号'},
                      {field:'displayName', width:160,title:'姓名'},
                      {field:'adminIDCode', width:160,title:'身份证'},
                      {field:'email', width:160,title:'邮箱'},
                      {field:'telPhone', width:160,title:'手机号'},
                      {field:'departmentName',title:'组织机构'},
                      {field:'positionName',title:'职位'},
                      {field:'roleName',title:'角色'},
                      {field:'adminState', width:160,title:'状态'},
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var value = '<mis:PermisTag code="010203"><a href="#" class="label label-info" onclick="showAdmin('+index+')">详情</a></mis:PermisTag> &nbsp;';
                               value += '<mis:PermisTag code="010202"><a class="label label-important" onclick="editAdmin(' + index + ')">修改</a></mis:PermisTag>';
                               return value;
                          }
                      }
            ]],
           	onBeforeLoad: function (value, rec) {
               var adminState = $(this).datagrid("getColumnOption", "adminState");
               if (adminState) {
            	   adminState.formatter = function (value, rowData, rowIndex) {
                       if (value == "0") {
                           return "无效";
                       } else if (value == "1") {
                           return "有效";
                       } else if (value == "2") {
                           return "离职";
                       }
                   }
               }
           }
       });
    }
    
    //用于选择组织机构节点
    function selectSearchDepartment() {
        $("#admin_adminsList").after("<div id='department_departmentsTree' style=' padding:10px; '></div>");
        $("#department_departmentsTree").dialog({
            resizable: false,
            title: '组织结构选择',
            href: '${path}/jsp/department/showDepartmentTree.do',
            width: 300,
            height: 450,
            modal: true,
            top: 150,
            left: 1000,
            buttons: [
                {
                    text: '确认',
                    iconCls: 'icon-ok',
                    handler: function () {
                    	var editingId = $("#department_departmentsTree").contents().find("#editingId").text();
                    	var editingName = $("#department_departmentsTree").contents().find("#editingName").text();
                        //alert(editingId + "==" + editingName);
						$("#searchDepartment").attr("value",editingName);
						$("#searchDepartmentCode").attr("value",editingId);
						 $("#department_departmentsTree").dialog('close');
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#department_departmentsTree").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
    
    function addAdmin() {
        $("#admin_adminsList").after("<div id='addAdmin' style=' padding:10px; '></div>");
        $("#addAdmin").dialog({
            resizable: false,
            title: '新增员工',
            href: '${path}/jsp/admin/addAdmin.do',
            width: 900,
            height: 550,
            modal: true,
            top: 100,
            left: 200,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        try {
                            $("#addAdmin").contents().find("#admin_addAdmin_form").submit();
                        } catch (e) {
                            alert(e);
                        }
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#addAdmin").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
	
    function editAdmin(index) {
    	$("#admin_adminsList_list").datagrid("selectRow",index);
        var selRow = $("#admin_adminsList_list").datagrid("getSelected");
        if (selRow) {
            $("#admin_adminsList").after("<div id='addAdmin' style=' padding:10px; '></div>");
            $("#addAdmin").dialog({
                resizable: false,
                title: '修改员工信息',
                href: '${path}/jsp/admin/editAdmin.do?adminId=' + selRow.adminId,
                width: 900,
                height: 550,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '提交',
                        iconCls: 'icon-ok',
                        handler: function () {
                        	$.messager.confirm('确认信息','是否确认修改员工信息？',function(r){
                    			if(r){
		                            try {
		                                $("#addAdmin").contents().find("#admin_addAdmin_form").submit();
		                            } catch (e) {
		                                alert(e);
		                            }
                    			}
                        	});
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addAdmin").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要修改的员工!", "info");
        }
    }
    
    function showAdmin(index) {
    	$("#admin_adminsList_list").datagrid("selectRow",index);
    	var selRow = $("#admin_adminsList_list").datagrid("getSelected");
    	if (selRow) {
            $("#admin_adminsList").after("<div id='showAdmin' style=' padding:10px; '></div>");
            $("#showAdmin").dialog({
                resizable: false,
                title: '员工信息',
                href: '${path}/jsp/admin/showAdmin.do?adminId=' + selRow.adminId,
                width: 700,
                height: 550,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#showAdmin").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要查看的员工!", "info");
        }
    }
    
    //封装查询条件到对象，重新加载数据
    function conditionSearch() {
    	var adminInfo={};
    	if($("#displayNameSearch").val() != ""){
    		adminInfo.displayName = $("#displayNameSearch").val();
    	}
    	if($("#adminCodes").val() != ""){
    		adminInfo.adminCode = $("#adminCodes").val();
    	}
    	if($("#email").val() != ""){
    		adminInfo.email = $("#email").val();
    	}
    	if($("#telPhone").val() != ""){
    		adminInfo.telPhone = $("#telPhone").val();
    	}
    	if($("#positionIds").combobox("getValue") != ""){
    		adminInfo.positionId = $("#positionIds").combobox("getValue");
    	}
    	if($("#adminStateValue").combobox("getValue") != ""){
    		adminInfo.adminState = $("#adminStateValue").combobox("getValue");
    	}
    	if($("#searchDepartment").val() != ""){
    		adminInfo.departmentCode = $("#searchDepartmentCode").val();
    	}else {
    		$("#searchDepartmentCode").val("");
    	}
    	//$("#admin_adminsList_list").datagrid("options").queryParams = adminInfo;	reload
    	$("#admin_adminsList_list").datagrid('load',adminInfo);
    }
    
  	//职位下拉框
	$('#positionIds').combobox({ 
		url:'${path}/jsp/position/findPositionNames.do', 
		valueField:'id', 
		textField:'text',
	});
 	
    doSearch();
</script>
</body>