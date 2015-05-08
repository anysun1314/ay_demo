<%--
  User: anyang
  Date: 2014-4-25 上午9:45:20
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/pageContext.jsp"%>
<%@include file="common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>

<!-- Modal -->
<div id="login_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">和信组织机构管理</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" id="loginForm" target="_self" action="${path}/login.do" method="post">
            <div class="control-group">
                <label class="control-label" for="loginName">用户名</label>
                <div class="controls">
                    <input type="text" id="loginName" name="loginName" placeholder="用户名" value="">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="loginPwd">密码</label>
                <div class="controls">
                    <input type="password" id="loginPwd" name="loginPwd" placeholder="密码" value="">
                </div>
            </div>

        </form>

    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" id="loginSubmit">&nbsp;&nbsp;登录&nbsp;&nbsp;</button>
    </div>
</div>
<script>
    $('#login_modal').modal({
        show:true,
        backdrop:false
    })
    
    /*去掉非法字符
    function tripScript(loginPwd){
		var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
		var rs = "";
		for (var i = 0; i < loginPwd.length; i++) {
		 rs = rs+loginPwd.substr(i, 1).replace(pattern, '');
		}
		return rs;
	}*/

    $("#loginSubmit").click(function(){
        $("#loginForm").submit();
    });
    
    $("#loginPwd").keyup(function(e){
        if(e.keyCode == 13){
        	$("#loginForm").submit();
        }
    });
    
    if('${message}' != "") {
    	$("#loginName").attr('value',"${loginName}");
    	$("#loginPwd").attr('value',"${loginPwd}");
    	$.messager.alert("系统提示", '${message}', "info");
    }
    
    //session超时，跳转登录页
    if('${timeout}' == "true") {
    	window.location.href="${basePath}?time=out";
    }
    
    //session失效提示
    var url = location.search;
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("=");
        if(strs[1] == "out") {
    		$.messager.alert("系统提示", '登录超时，请重新登录！', "info");
        }
    }
</script>
</body>
</html>
