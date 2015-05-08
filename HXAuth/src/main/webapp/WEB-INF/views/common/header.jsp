<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.hexindaiauth.com/jsp/taglib" prefix="mis" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="brand" href="#">和信组织机构管理</a>
        <ul class="nav">
            <ul class="nav">
                <mis:PermisTag code="01">
                    <li class="dropdown">
                    <a href="#" onclick="javascript:goto('jsp/department/departmentsList.do','>>组织机构管理')" class="dropdown-toggle" data-toggle="dropdown">
                      组织机构管理
                    </a>
                    </li>
                </mis:PermisTag>

                <mis:PermisTag code="01">
                    <li class="dropdown">
                    <a href="#" onclick="javascript:goto('jsp/admin/adminsList.do','>>员工管理')" class="dropdown-toggle" data-toggle="dropdown">
                      员工管理
                    </a>
                    </li>
                </mis:PermisTag>
                
                <mis:PermisTag code="01">
                    <li class="dropdown">
                    <a href="#" onclick="javascript:goto('jsp/position/positionsList.do','>>职位管理')" class="dropdown-toggle" data-toggle="dropdown">
                      职位管理
                    </a>
                    </li>
                </mis:PermisTag>

                <mis:PermisTag code="01">
                    <li class="dropdown">
                    <a href="#" onclick="javascript:goto('jsp/role/rolesList.do','>>角色管理')" class="dropdown-toggle" data-toggle="dropdown">
                      角色管理
                    </a>
                    </li>
                </mis:PermisTag>

                <mis:PermisTag code="01">
                    <li class="dropdown">
                    <a href="#" onclick="javascript:goto('jsp/function/functionsList.do','>>权限管理')" class="dropdown-toggle" data-toggle="dropdown">
                      权限管理
                    </a>
                    </li>
                </mis:PermisTag>
                
                <mis:PermisTag code="01">
                    <li class="dropdown">
                    <a href="#" onclick="javascript:goto('jsp/optionLog/optionLogsList.do','>>操作日志')" class="dropdown-toggle" data-toggle="dropdown">
                      操作日志
                    </a>
                    </li>
                </mis:PermisTag>
            </ul>
        </ul>
        
        <ul class="nav pull-right" style="position: static;font-size: 12px">
            <li id="loginUser" name="loginUser">
                <a onclick="changePassword()">当前用户：<span id="displayName"></span></a>
            </li>
            <!-- 部门角色选择 -->
            <li class="dropdown">
                <!-- 部门角色显示 -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <span id="info"></span>
                    <b class="caret"></b>
                </a>
                <!-- 当前用户所有部门的下拉菜单 -->
                <ul class="dropdown-menu" id="departs">
                    <c:forEach items="${sessionScope.userInfos}" var="userInfo">
                        <c:if test="${userInfo.userRoleId != sessionScope.userRoleId}">
                            <li><a href="#" onclick="changeDepart(${userInfo.userRoleId})">${userInfo.departmentName}&nbsp;&lt;${userInfo.roleName}&gt;</a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </li>
            <li id="logout" name="logout">
                <a href="#" onclick="logout();">退出</a>
            </li>
        </ul>
    </div>
</div>
<div id="changePwdDiv"></div>
<script>
    function changePassword() {
        $("#changePwdDiv").after("<div id='showAdminInfo' style=' padding:10px; '></div>");
        $("#showAdminInfo").dialog({
            resizable: false,
            title: '当前用户信息',
            href: '${path}/jsp/admin/showAdminInfo.do?adminId=${sessionScope.loginUser.adminId}',
            width: 700,
            height: 550,
            modal: true,
            top: 130,
            left: 500,
            buttons: [
                {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#showAdminInfo").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }

    function changeDepart(userRoleId) {
        try {
            $.post("${path}/changeDepart.do?userRoleId=" + userRoleId,
                    function (data) {
                        if (data == "success") {
                            window.location.href = "${path}/refresh.do";
                            //post,get，reload 方式都不可以
                        }
                        if (data == "false") {
                            $.messager.alert("系统提示", "部门角色切换失败！", "info");
                        }
                    });
        } catch (e) {
            $.messager.alert("系统提示", "转换部门角色失败!", "info");
            return;
        }
    }

    function goto(url, title) {
        $("#breadcrumb").html(title);
        $('#main').panel('refresh', url);

    }

    function logout() {
        $.post("${path}/logout.do",
                function (data) {
                    if (data == "success") {
                        window.location.href = "${basePath}";
                    }
                });
    }

    function init() {

        $.ajaxSetup({
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            complete: function (xhr, textStatus) {
                //$.ts.hideLoading();
                //session timeout
                if (xhr.status == 911) {
                    window.location = "${path}/sessionTimeout.do?timeout=true";//返回登录页
                    return;
                }
                //no access
                if (xhr.status == 912) {
                    window.location = "${path}/noAccess.do?view=true";//返回无权限页
                    return;
                }
            }
        });

        //右侧当前父级部门、当前部门、当前角色的显示
        if ('${sessionScope.loginUser.displayName}' != "") {
            $("#displayName").text('${sessionScope.loginUser.displayName}');	//，您好！
            var parent;
            if ('${sessionScope.currentParentDepartment.departmentName}' != "") {
                parent = '${sessionScope.currentParentDepartment.departmentName}  ';
            } else {
                parent = '';
            }

            $("#info").text(parent + '${sessionScope.currentDepartment.departmentName} <${sessionScope.currentRole.roleName}>');
        }
    }

    init();
</script>