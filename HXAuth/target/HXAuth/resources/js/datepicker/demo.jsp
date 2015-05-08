<%--
  User: Ren yulin
  Date: 13-7-2 下午3:41
  http://www.eyecon.ro/datepicker/
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${path}/datepicker/css/datepicker.css">
    <link rel="stylesheet" href="${path}/datepicker/css/layout.css">
    <script type="text/javascript" src="${path}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${path}/datepicker/js/datepicker.js"></script>
    <%--<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>--%>
    <%--<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>--%>

</head>
<script>
    $(function () {
        $('#date').DatePicker({
            flat: true,
            date: '2008-07-31',
            current: '2008-07-31',
            calendars: 1,
            starts: 1
        });
        $("#date2").DatePicker({
            flat: true,
            date: ['2013-07-10', '2013-07-28'],
            current: '2013-07-31',
            format: 'Y-m-d',
            calendars: 1,
            mode: 'multiple',
            regional:'zh-TW',
//            onRender: function (date) {
//                return {
//                    disabled: (date.valueOf() < new Date().valueOf()),
//                    className: date.valueOf() == new Date() ? 'datepickerSpecial' : false
////                    disabled:true,
////                    className:'datepickerSpecial'
//                }
//            },
            starts: 7
        });
    })

</script>
<body>
<div type="text" id="date"></div>
<%--<input type="text" id="date"/>--%>
<input type="button" value="查看" id="getDate">

</body>
<script language="javascript">
    $("#getDate").click(function(){
        alert($('#date').DatePickerGetDate('yyyy-MM-dd'));
    })
</script>


</html>