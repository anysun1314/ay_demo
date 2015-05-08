<%--
  User: du yaxing
  Date: 14-3-31 上午11:50
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/pageContext.jsp" %>
<body>
<div id="loan_contract_List" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="loanCustomerAccountFlowList_toolbar" style="height:auto">


        <div id="searchtool" style="padding:5px">
            <form  method="post" id="accountFlowListForm" >
                <span>组织机构:</span>
                <select id="customerId" required="true" missingMessage="请选择组织机构">

                </select>
                <span>账户类型:</span>
                <select id="accountType"  required="true" missingMessage="请选择账户类型">

                </select>

                <span>流水类型:</span>
                <select id="flowType" class="easyui-combobox">
                    <option value="-1">全部</option>
                    <option value="1">收入</option>
                    <option value="2">支出</option>
                    <option value="3">冻结</option>
                    <option value="4">解冻</option>
                </select>

                <span>费用类型:</span>
                <select id="feesType" class="easyui-combobox">
                    <option value="-1">全部</option>
                </select>


                <span>时间段:</span><input class="easyui-datebox" editable="false" id="beginTime" size=5/> - <input
                    class="easyui-datebox" editable="false" id="endTime" size=5/>
                <a href="#" id="searchSubmit" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>

                <%--<a href="#" class="easyui-linkbutton" id="mt_recharge" iconCls="icon-add" plain="true"
                   onclick="recharge();">手工确认充值</a>     --%>
            </form>

        </div>
    </div>

    <table id="customer_account_flow_list">

    </table>


</div>

<script type="text/javascript">

//$.ajax({async: false});

$("#customerId").combobox({
    url: '${path}/jsp/customer/departmentCustomerList.do',
    valueField: 'customerId',
    textField: 'trueName'
});

$("#accountType").combobox({
    url: '${path}/jsp/customer/accountTypeList.do',
    valueField: 'accountTypeCode',
    textField: 'accountTypeName'
});
$("#accountFlowListForm").form({
    //url:'#',
    onSubmit:function(data) {
        return $("#accountFlowListForm").form('validate');
    },
    success:function(data){
        search();
    }

});
$("#searchSubmit").click(function(){
    $("#accountFlowListForm").submit();
});
function load_fees_type() {
    $('#feesType').empty();
    $.ajax({
        async: false,
        url: '${path}/jsp/constant/listByTypeCode.do',
        data: 'typeCode=accountCashFeesType',
        success: function (results) {
            if (!results)
                return false;

            $('<option value="-1">==全部==</option>').appendTo('#feesType');

            $.each(results, function (index, item) {
                $('<option value="' + item.constantValue + '">' + item.constantName + '</option>').appendTo('#feesType');
            });

        }
    })
    <%--$.post('${path}/jsp/constant/listByTypeCode.do',{'typeCode':'accountCashFeesType'},function(results){--%>
    <%--if(!results)--%>
    <%--return false;--%>
    <%----%>
    <%--$('<option value="-1">==全部==</option>').appendTo('#feesType');--%>
    <%----%>
    <%--$.each(results,function(index,item){--%>
    <%--$('<option value="'+item.constantValue+'">'+item.constantName+'</option>').appendTo('#feesType');--%>
    <%--});	--%>
    <%----%>
    <%--});--%>

}

function init() {
    load_fees_type();
    //init_recharge();

}
var datagridUrl = '${path}/jsp/cash_flow/page/controller/query';
function doSearch() {
    $("#customer_account_flow_list").datagrid({
        idField: 'loanApplicationId',
        title: '流水列表',
        pagination: true,
        pageSize: 20,
        width: document.body.clientWidth * 0.97,
        height: document.body.clientHeight * 0.89,
        singleSelect: true,
        rownumbers: true,
        toolbar: '#loanCustomerAccountFlowList_toolbar',
        columns: [
            [
                {field: 'accountCashFlowId', width: 160, title: '资金账户流水ID', checkbox: true},
                {field: 'createTime', width: 160, title: '日期'},
                {field: 'flowType', width: 50, title: '流水类型', hidden: true},
                {field: 'flowTypeName', width: 50, title: '流水类型'},
                {field: 'feesType', width: 80, title: '费用类型', hidden: true},
                {field: 'feeTypeName', width: 80, title: '费用类型'},
                {field: 'repaymentBalance', width: 80, align: 'right', title: '收入（元）'},
                {field: 'paymentBalance', width: 80, align: 'right', title: '支出（元）'},
                {field: 'frozenBalance', width: 80, align: 'right', title: '冻结（元）'},
                {field: 'unfrozenBalance', width: 80, align: 'right', title: '解冻（元）'},
                {field: 'allAssetsAfterChange2', width: 120, align: 'right', title: '操作账户(元)'},
                {field: 'allAssetsAfterChange', width: 180, align: 'right', title: '操作账户(元,18位)'},
                {field: 'allAccountAssetsAfterChange', width: 100, align: 'right', title: '总账户（元）'},
                {field: 'flowDesc', width: 660, title: '备注'}
                /*,
                 {field:'action',title:'操作',width:160,align:'center',
                 formatter:function(value,row,index){
                 var value = '<a href="#" onclick="saverow('+index+')">操作</a> ';
                 return value;

                 }
                 }
                 */
            ]
        ],
        onBeforeLoad: function (value, rec) {
            var createTime = $(this).datagrid("getColumnOption", "createTime");
            if (createTime) {
                createTime.formatter = function (value, rowData, rowIndex) {
                    var date = new Date(value);
                    //return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                    return getDateTimeStr(date);
                    //return date.toLocaleString();
                }
            }
            var repaymentBalance = $(this).datagrid("getColumnOption", "repaymentBalance");
            if (repaymentBalance) {
                repaymentBalance.formatter = function (value, rowData, rowIndex) {
                    var value = rowData.balance;
                    if (rowData.flowType != '1')
                        return '';
                    return formatNum(rowData.balance, 2);
                }
            }
            var paymentBalance = $(this).datagrid("getColumnOption", "paymentBalance");
            if (paymentBalance) {
                paymentBalance.formatter = function (value, rowData, rowIndex) {
                    var value = rowData.balance;
                    if (rowData.flowType != '2')
                        return '';
                    return formatNum(rowData.balance, 2);
                }
            }
            var frozenBalance = $(this).datagrid("getColumnOption", "frozenBalance");
            if (frozenBalance) {
                frozenBalance.formatter = function (value, rowData, rowIndex) {
                    var value = rowData.balance;
                    if (rowData.flowType != '3')
                        return '';
                    return formatNum(rowData.balance, 2);
                }
            }
            var unfrozenBalance = $(this).datagrid("getColumnOption", "unfrozenBalance");
            if (unfrozenBalance) {
                unfrozenBalance.formatter = function (value, rowData, rowIndex) {
                    var value = rowData.balance;
                    if (rowData.flowType != '4')
                        return '';
                    return formatNum(rowData.balance, 2);
                }
            }
           var allAssetsAfterChange = $(this).datagrid("getColumnOption", "allAssetsAfterChange2");
            if (allAssetsAfterChange) {
                allAssetsAfterChange.formatter = function (value, rowData, rowIndex) {
                    return formatNum(value, 2);
               }
            }
            var allAccountAssetsAfterChange = $(this).datagrid("getColumnOption", "allAccountAssetsAfterChange");
            if (allAccountAssetsAfterChange) {
                allAccountAssetsAfterChange.formatter = function (value, rowData, rowIndex) {
                    return formatNum(value, 2);
                }
            }


        }
    });
}


function search() {
    var params = {};

    if ($('#flowType').val() != '-1')
        params.flowType = $('#flowType').val();

    if ($('#feesType').val() != '-1')
        params.feesType = $('#feesType').val();

    if ($("#loanCustomerAccountFlowList_toolbar #beginTime").datebox("getValue") != '')
        params.beginTime = $("#loanCustomerAccountFlowList_toolbar #beginTime").datebox("getValue");

    if ($("#loanCustomerAccountFlowList_toolbar #endTime").datebox("getValue") != '')
        params.endTime = $("#loanCustomerAccountFlowList_toolbar #endTime").datebox("getValue");

    var customerId = $("#customerId").combobox("getValue");
    //alert(customerId);
    if (customerId) {
        params.customerId = customerId;
    }
    var accountType = $("#accountType").combobox("getValue");
    if (accountType)
        params.accountTypeCode = accountType;
    /*
     if(typeof(params.beginTime)=='undefined' || typeof(params.endTime)=='undefined') {
     if(typeof(params.beginTime)=='undefined' && typeof(params.endTime)!='undefined')
     params.beginTime = params.endTime;
     if(typeof(params.beginTime)!='undefined' && typeof(params.endTime)=='undefined')
     params.endTime = params.beginTime;
     }
     */

    $("#customer_account_flow_list").datagrid("options").url = datagridUrl;
    $("#customer_account_flow_list").datagrid("options").queryParams = params;
    $("#customer_account_flow_list").datagrid("reload");
}

/*function recharge(){
 *//*
 $("#repayment_plan_summary_list").datagrid("selectRow",index);
 var selRow = $("#repayment_plan_summary_list").datagrid("getSelected");
 var date = new Date(selRow.repaymentDay);
 var repaymentDay = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
 *//*
 $("#customer_account_flow_list").after("<div id='mt_recharge_nav' style=' padding:10px; '></div>");
 $("#mt_recharge_nav").dialog({
 resizable: false,
 title: '手动确认充值',
 href: '${path}/jsp/lend_application/to_mt_recharge_page?lendOrderId='+lendOrderId+'&customerId='+customerId,
 width: 600,
 modal: true,
 height: 500,
 top: 100,
 left: 400,
 buttons: [
 {
 text: '提交',
 iconCls: 'icon-ok',
 handler: function () {
 $("#mt_recharge_nav").contents().find("#mt_recharge_form").submit();
 }
 },
 {
 text: '取消',
 iconCls: 'icon-cancel',
 handler: function () {
 $("#mt_recharge_nav").dialog('close');
 }
 }
 ],
 onClose: function () {
 $(this).dialog('destroy');
 }
 });
 }      */

function detail() {
    var selRow = $("#customer_account_flow_list").datagrid("getSelected");
    if (selRow) {
        var url = '${path}/jsp/loan_application/controller/to_detail_page';
        url += '?loanApplicationId=' + selRow.loanApplicationId;
        window.open(url, '_blank');
    } else {
        $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
    }
}

$(document).ready(function () {
    init();
    doSearch();
});

</script>