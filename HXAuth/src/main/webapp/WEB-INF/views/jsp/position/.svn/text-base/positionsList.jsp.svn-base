<%--
  User: anyang
  Date: 2014-4-21 下午7:58:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/pageContext.jsp"%>
<%@include file="../../common/common.jsp" %>
<body>
<div id="position_positionsList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="position_positionsList_toolbar" style="height:auto">

        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addPosition();">新增</a>
    </div>

    <table id="position_positionsList_list"></table>
</div>

<script language="javascript">
    function doSearch() {
        $("#position_positionsList_list").datagrid({
            idField: 'positionId',
            title: '职位列表',
            url: '${path}/jsp/position/list.do',
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.89,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                      {field:'positionId',checkbox:true},
                      {field:'positionName', width:160,title:'职位名称'},
                      {field:'positionDesc', width:160,title:'相关描述'},
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var value = '<a class="label label-important" onclick="editPosition(' + index + ')">修改</a>';
                               return value;
                          }
                      }
            ]],
            toolbar: '#position_positionsList_toolbar'
        });
    }
    function addPosition() {
        $("#position_positionsList").after("<div id='addPosition' style=' padding:10px; '></div>");
        $("#addPosition").dialog({
            resizable: false,
            title: '新增职位',
            href: '${path}/jsp/position/addPosition.do',
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
                            $("#addPosition").contents().find("#position_addPosition_form").submit();
                        } catch (e) {
                            alert(e);
                        }

                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#addPosition").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
	
    function editPosition(index) {
    	$("#position_positionsList_list").datagrid("selectRow",index);
        var selRow = $("#position_positionsList_list").datagrid("getSelected");
        if (selRow) {
            $("#position_positionsList").after("<div id='addPosition' style=' padding:10px; '></div>");
            $("#addPosition").dialog({
                resizable: false,
                title: '修改职位',
                href: '${path}/jsp/position/editPosition.do?positionId=' + selRow.positionId,
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
                            $("#addPosition").contents().find("#position_addPosition_form").submit();
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addPosition").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要修改的职位!", "info");
        }
    }
 	
    doSearch();
</script>
</body>