<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 14-4-14
  Time: 下午4:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <%
        String contextPath = request.getContextPath();
        pageContext.setAttribute("path", contextPath);
        
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		pageContext.setAttribute("basePath", basePath);
    %>

