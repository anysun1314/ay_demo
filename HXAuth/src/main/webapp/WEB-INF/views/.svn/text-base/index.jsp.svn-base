<%--
  User: Ren yulin
  Date: 14-3-3 下午5:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/pageContext.jsp" %>
<%@include file="common/common.jsp" %>

<html>
<html>
<head>
    <title></title>
</head>
<body>
<%@include file="common/header.jsp" %>
<p id="breadcrumb" class="muted"></p>
<div id="main" style="display: block;"
     data-options="closable:false,
                collapsible:false,minimizable:false,maximizable:false">

</div>
<script>
    $("#main").panel({
        width: document.body.clientWidth * 0.985,
        height: document.body.clientHeight * 0.9,
        fit:true,
        border: 0
    })

    if('${defaultUrl}' != "" && '${defaultNavName}' != "") {
    	$("#breadcrumb").html('${defaultNavName}');
    	
    	var url = '${defaultUrl}'.substr(1);
        $('#main').panel('refresh',url);
    }
    
    if('${view}' == 'true') {
    	$("#main").panel({
            content:"<div style='padding-top: 270px' align='center'><h2>对不起，您没有操作权限！</h2></div>"
        })
    }
</script>
</body>
</html>