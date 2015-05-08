<%--
  User: anyang
  Date: @date 2014-5-30 上午8:11:00
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<body>
<div id="optionLog_optionLogsList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="" style="height:auto;padding-left: 30px;">
        <form action="/#" id="conditionSearch" method="post">
            <span>操作员工:</span><input type="text" id="inputAdminName" value="" size=10/>
            <span>操作内容:</span><input type="text" id="inputLogInfo" value="" size=10/>
            <span>操作时间:</span><input id="inputOptionTime" class="easyui-datebox" value=""/>
            
            <a onclick="conditionSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
        </form>
    </div>
    <div id="optionLog_optionLogsList_toolbar" style="height:auto"></div>

    <table id="optionLog_optionLogsList_list"></table>
</div>

<script language="javascript">
    function doSearch() {
        $("#optionLog_optionLogsList_list").datagrid({
            idField: 'optionLogId',
            title: '操作日志列表',
            url: '${path}/jsp/optionLog/listByPage.do',
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            rownumbers: true,
            toolbar: '#optionLog_optionLogsList_toolbar',
            columns:[[
                      //{field:'optionLogId',checkbox:true},
                      {field:'adminName', width:150,title:'操作员工',align:'center',
                    	  formatter:function(value,row,index){
                    		  if(value == null) {
                    			  value = "—";
                    		  }
                              return value;
                          }  
                      },
                      {field:'optionTime', width:130,title:'操作时间',align:'center',
                    	  formatter:function(value,row,index){
                    		  value = getDateTimeStr(new Date(value));
                              return value;
                          }
                      },
                      {field:'logInfo', width:500,title:'操作内容',
                    	  formatter:function(value,row,index){
                    		  str = new RegExp("/r/n","g");
                    		  value = value.replace(str,"</br>");
                    		  //value = value.replaceAll(eval("/r/n"),"</br>");
                              return value;
                          }
                      },
                      {field:'departmentName',title:'组织机构',align:'center',
                    	  formatter:function(value,row,index){
                    		  if(value == null) {
                    			  value = "—";
                    		  }
                              return value;
                          }  
                      },
                      {field:'positionName',title:'职位',align:'center',
                    	  formatter:function(value,row,index){
                    		  if(value == null) {
                    			  value = "—";
                    		  }
                              return value;
                          }  
                      },
                      {field:'roleName',title:'角色',align:'center',
                    	  formatter:function(value,row,index){
                    		  if(value == null) {
                    			  value = "—";
                    		  }
                              return value;
                          }  
                      },
                      {field:'loginIP', width:100,title:'登录IP',align:'center'}
            ]]
       });
    }
    
    function showOptionLog(index) {
    	$("#optionLog_optionLogsList_list").datagrid("selectRow",index);
    	var selRow = $("#optionLog_optionLogsList_list").datagrid("getSelected");
    	if (selRow) {
            $("#optionLog_optionLogsList").after("<div id='showOptionLog' style=' padding:10px; '></div>");
            $("#showOptionLog").dialog({
                resizable: false,
                title: '日志',
                href: '${path}/jsp/optionLog/showOptionLog.do?optionLogId=' + selRow.optionLogId,
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
                            $("#showOptionLog").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择!", "info");
        }
    }
    
    //封装查询条件到对象，重新加载数据
    function conditionSearch() {
    	var optionLog={};
    	if($("#inputAdminName").val() != ""){
    		optionLog.adminName = $("#inputAdminName").val();
    	}
    	var optTime = $("#inputOptionTime").datebox('getValue');
    	if(optTime != ""){
    		optionLog.optionTime = new Date(optTime);
    	}
    	if($("#inputLogInfo").val() != ""){
 			var reg = new RegExp("^(/r/n)|(/r)|(/n)|(/)"); 
 			if ($("#inputLogInfo").val().search(reg) == -1){ 
    			optionLog.logInfo = $("#inputLogInfo").val();
			}
    	}
    	//$("#optionLog_optionLogsList_list").datagrid("options").queryParams = optionLogInfo;	reload
    	$("#optionLog_optionLogsList_list").datagrid('load',optionLog);
    }
    
    doSearch();
</script>
</body>