<%--
  User: anyang
  Date: 2014-3-27 下午3:47:27
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<body>
<div id="role_rolesList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="role_rolesList_toolbar" style="height:auto">

        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addRole();">新增</a>
    </div>

    <table id="role_rolesList_list"></table>
</div>

<script language="javascript">
    function doSearch() {
        $("#role_rolesList_list").datagrid({
            idField: 'roleId',
            title: '角色列表',
            url: '${path}/jsp/role/list.do',
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.89,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                      {field:'roleId',checkbox:true},
                      {field:'roleName', width:160,title:'职位名称'},
                      {field:'roleDesc', width:160,title:'相关描述'},
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var value = '<a class="label label-important" onclick="editRole(' + index + ')">修改</a> &nbsp;';
                               value += '<a class="label label-warning" onclick="addRoleFunction(' + index + ')">授权</a>';
                               return value;
                          }
                      }
            ]],
            toolbar: '#role_rolesList_toolbar'
        });
    }
    function addRole() {
        $("#role_rolesList").after("<div id='addRole' style=' padding:10px; '></div>");
        $("#addRole").dialog({
            resizable: false,
            title: '新增角色',
            href: '${path}/jsp/role/addRole.do',
            width: 400,
            modal: true,
            height: 300,
            top: 200,
            left: 500,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        try {
                            $("#addRole").contents().find("#role_addRole_form").submit();
                        } catch (e) {
                            alert(e);
                        }

                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#addRole").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
	
    function editRole(index) {
    	$("#role_rolesList_list").datagrid("selectRow",index);
        var selRow = $("#role_rolesList_list").datagrid("getSelected");
        if (selRow) {
            $("#role_rolesList").after("<div id='addRole' style=' padding:10px; '></div>");
            $("#addRole").dialog({
                resizable: false,
                title: '修改角色',
                href: '${path}/jsp/role/editRole.do?roleId='+selRow.roleId,
                width: 400,
                modal: true,
                height: 300,
                top: 100,
                left: 400,
                buttons: [
                    {
                        text: '提交',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $("#addRole").contents().find("#role_addRole_form").submit();
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addRole").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要修改的角色!", "info");
        }
    }
    
    function addRoleFunction(index) {
    	$("#role_rolesList_list").datagrid("selectRow",index);
    	var selRow = $("#role_rolesList_list").datagrid("getSelected");
        if (selRow) {
            $("#role_rolesList").after("<div id='addRoleFunction' style=' padding:10px; '></div>");
            $("#addRoleFunction").dialog({
                resizable: false,
                title: '角色授权',
                href: '${path}/jsp/function/showFunctionTree.do?roleId='+selRow.roleId+'&roleName='+selRow.roleName,
                width: 500,
                modal: true,
                height: 600,
                top: 100,
                left: 400,
                buttons: [
                    {
                        text: '提交',
                        iconCls: 'icon-ok',
                        handler: function () {
                        	try {
                        		var rows = $("#addRoleFunction").contents().find("#showTree").treegrid('getSelections');
                        		var functionIdStr = "";
                        		$.each(rows,function(key,value){
                        			functionIdStr = functionIdStr + value.id;
                        			if(key != rows.length - 1){
                        				functionIdStr = functionIdStr + '-';
                        			}
                     	        })
                            	$.post("${path}/jsp/role/saveRoleFunction.do?functionIdStr="+functionIdStr+'&roleId='+selRow.roleId,
                                    	function(data){
                                    		if(data == "success"){
                                    			$.messager.alert("系统提示", "操作成功!", "info");
                                    			$("#addRoleFunction").dialog('close');
                                    		}
                                    		if(data == "error"){
                                    			$.messager.alert("系统提示", "操作失败!", "info");
                                    			return;
                                    		}
                                    		if(data == "empty"){
                                    			$.messager.alert("系统提示", "请选择要匹配的权限!", "info");
                                    			return;
                                    		}
                            	  		});
                            } catch (e) {
                                alert("操作失败！");
                                return;
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addRoleFunction").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要授权的角色!", "info");
        }
    }
 	
    doSearch();
</script>
</body>