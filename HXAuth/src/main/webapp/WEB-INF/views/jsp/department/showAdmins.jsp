<%--
  User: anyang
  Date: 2014-5-6 上午9:53:01
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.hexindaiauth.com/jsp/taglib" prefix="mis" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<body>
<mis:PermisTag code="010301"> 
<div id="admin_adminsList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    
	<div class="control-group">
    	<label class="control-label" style="width:auto;"><b id="parentName"></b> >> <b id="childName"></b></label>
    </div>
    <div id="admin_adminsList_toolbar" style="height:auto">
      	<mis:PermisTag code="010302"> 
    	<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"onclick="addAdmin();">&nbsp;&nbsp;添加&nbsp;&nbsp;</a>
    	</mis:PermisTag>
    </div>
    <table id="admin_adminsList_list"></table>
</div>
</mis:PermisTag>
<script language="javascript">
	function init(){
		//显示当前标签的父级标签
		var childNode = '${childNode}';
		var parentNode = '${parentNode}';
		if(childNode != null){
			$("#childName").text(childNode);
		}
		if(parentNode != null){
			$("#parentName").text(parentNode);
		}
		
	}
	
	init();
    
	var flag;
    function doSearch() {
        $("#admin_adminsList_list").datagrid({
            idField: 'userRoleIds',
            url: '${path}/jsp/department/showAdminInfo.do?departmentCode=${departmentCode}',
            rownumbers: true,
            singleSelect: true,
            toolbar: '#admin_adminsList_toolbar',
            columns:[[
                      {field:'userRoleIds',checkbox:true},
                      {field:'adminCode', width:100,title:'工号'},
                      {field:'displayName', width:100,title:'姓名'},
                      {field:'positionName',title:'职位'},
                      {field:'roleName',title:'角色'},
                      {field:'action',title:'操作',width:100,align:'center',
                          formatter:function(value,row,index){
                               var value = '<mis:PermisTag code="010303"><a href="#" class="label label-primary" onclick="deleteAdmin('+index+');">&nbsp;删除&nbsp;</a></mis:PermisTag> &nbsp;';
                               return value;
                          }
                      }
            ]],
            onLoadSuccess: function (data) {
            	flag = data.total
            	if(flag > 0) {
            		//$("#hideDel").css("display","");
            	}
            }
        });
    }
    
    function addAdmin() {
    	try {	//判断是否该节点或上级节点是否存在资金账户，不存在，就不添加员工
        	if(flag == 0) {
	    		$.post("${path}/jsp/department/addCondition.do?departmentCode=${departmentCode}",
	                	function(data){
	                		if(data == "noAccount"){
	                			$.messager.alert("系统提示", "该节点及其父级节点资金账户不可用，不可以添加人员!", "info");
	                			return;
	                		}
	                		if(data == "error"){
	                			$.messager.alert("系统提示", "操作失败!", "info");
	                			return;
	                		}
	                		addDialog();
	        	});
        	}else {
        		addDialog();
        	}
        } catch (e) {
            alert("操作失败！");
            return;
        }
    	
    	
    }
    
    function addDialog() {
        $("#admin_adminsList").after("<div id='addAdmin' style=' padding:10px; '></div>");
        $("#addAdmin").dialog({
            resizable: false,
            title: '添加组织架构人员',
            href: '${path}/jsp/department/addAdmin.do?departmentCode=${departmentCode}',
            width: 500,
            modal: true,
            height: 300,
            top: 230,
            left: 700,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        try {
                            $("#addAdmin").contents().find("#admin_addAdminRole_form").submit();
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
	
    function deleteAdmin(index) {
    	$("#admin_adminsList_list").datagrid("selectRow",index);
        var selRow = $("#admin_adminsList_list").datagrid("getSelected");
        if (selRow) {
        	$.messager.confirm('确认信息','是否确认删除该架构人员？',function(r){
    			if(r){
    				try {
                    	$.post("${path}/jsp/department/deleteAdmin.do?userRoleIds="+selRow.userRoleIds,//selRow.userRoleId,
                           	function(data){
                           		if(data == "success"){
                           			$.messager.alert("系统提示", "操作成功!", "info");
                           			$("#admin_adminsList_list").datagrid("reload");
                           		}else if(data == "error"){
                           			$.messager.alert("系统提示", "操作失败!", "info");
                           		}
                   	  		});
                    } catch (e) {
                        alert("删除失败！");
                        return;
                    }
    			}
    		});
        } else {
            $.messager.alert("系统提示", "请选择要删除的架构人员!", "info");
        }
    }
 	
    doSearch();
</script>
</body>