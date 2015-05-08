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
<div id="admin_showAdmin">
<form class="form-horizontal" id="admin_showAdmin_form" method="post">
    
    <div class="control-group">
        <label class="control-label">工号</label>
        <div class="controls-text" id="adminCode" style="width:300px;">${adminInfo.adminCode}&nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">登录密码</label>
        <div class="controls-text" id="loginPwd" style="width:300px;">
        	<a href="javascript:void(0);" class="btn btn-primary" onclick="changePwd();">&nbsp;&nbsp;重置&nbsp;&nbsp;</a>
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">姓名</label>
        <div class="controls-text" id="displayName" style="width:300px;">${adminInfo.displayName}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">身份证</label>
        <div class="controls-text" id="adminIDCode" style="width:300px;">${adminInfo.adminIDCode}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">邮箱</label>
        <div class="controls-text" id="email" style="width:300px;">${adminInfo.email}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">手机</label>
        <div class="controls-text" id="telPhone" style="width:300px;">${adminInfo.telPhone}&nbsp;</div>
    </div>

    <div class="control-group">
        <label class="control-label">状态</label>
        <div class="controls-text" id="adminStateName" style="width:300px;">${adminStateName}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">创建时间</label>
        <div class="controls-text" id="createTime" style="width:300px;">${adminInfo.createTime}&nbsp;</div>
    </div>
    
    <!-- 部门权限 -->
    <div style="padding-left:110px;padding-top:10px;">
	    <table id="department_list">
	        <thead>
	        <th data-options="field:'departmentName',width:268">组织机构</th>
	        <th data-options="field:'positionName',width:100">职位</th>
	        <th data-options="field:'roleName',width:100">角色</th> 
	        </thead>
	    </table>
    </div>
</form>
</div>

<script language="JAVASCRIPT" >

	function doSearch() {
	    $("#department_list").datagrid({
	        title: '部门权限',
	        url: '${path}/jsp/admin/showAdminDepart.do?adminId=' + '${adminInfo.adminId}',
	        width: 474
	    });
	}
	
	function init(){
		//var createTime = getDateTimeStr(${adminInfo.createTime});
		$("#createTime").text(getDateTimeStr(new Date('${adminInfo.createTime}')));
	}
	init();
	
	function changePwd() {
        $("#admin_showAdmin").after("<div id='changePwd' style=' padding:10px; '></div>");
        $("#changePwd").dialog({
            resizable: false,
            title: '设置密码',
            href: '${path}/jsp/admin/changePwd.do?adminId='+${adminId}+'&loginPwd='+'${adminInfo.loginPwd}',
            width: 500,
            height: 200,
            modal: true,
            top: 300,
            left: 510,
            buttons: [
                {
                    text: '确认',
                    iconCls: 'icon-ok',
                    handler: function () {
                        try {
                            $("#changePwd").contents().find("#admin_changePwd_form").submit();
                        } catch (e) {
                            alert(e);
                        }
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#changePwd").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
	
	doSearch();
</script>
</body>
</html>