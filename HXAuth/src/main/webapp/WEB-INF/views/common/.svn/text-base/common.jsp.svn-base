<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.hexindaiauth.com/jsp/taglib" prefix="mis" %>
<!DOCTYPE>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=100">
    <!-- IE8 mode -->
    <!-- IE8 mode -->
    <title></title>


</head>
<link rel="stylesheet" href="${path}/css/validationEngine.jquery.css">
<%--<link rel="stylesheet" href="${path}/css/template.css">--%>
<link rel="stylesheet" href="${path}/css/bootstrap.css">
<%--<link rel="stylesheet" href="${path}/css/bootstrap-responsive.css">--%>
<%--<link rel="stylesheet" href="${path}/css/rounded-corners.css">--%>

<link rel="stylesheet" href="${path}/js/themes/bootstrap/easyui.css">
<link rel="stylesheet" href="${path}/js/themes/icon.css">


<script type="text/javascript" src="${path}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${path}/js/bootstrap.js"></script>
<script type="text/javascript" src="${path}/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${path}/js/jquery.validationEngine.js"></script>

<%--<script type="text/javascript" src="${path}/js/utils.js"></script>--%>
<script type="text/javascript" src="${path}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${path}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${path}/js/jquery.form.js"></script>
<script type="text/javascript" src="${path}/js/utils.js"></script>

<!-- 引入自定义标签 -->


<script language="javascript">


    function setValue(objId, val) {
        $("#" + objId).val(val);
    }

    function num2String(numValue, strLength) {
        var result = "";
        if (numValue < 10) {
            result = "0" + numValue;
        } else {
            result = "" + numValue;
        }
        return result;
    }

    function getDateStr(date, num, ymd) {
        var now = null;
        if (typeof(date) == 'undefined' || date == null) {
            now = new Date();
        }
        else {
            now = date;
        }
        var yy = now.getFullYear();//getYear();
        if (ymd == "yy" && num != null && num != "") {
            yy = yy + num;
        }
        if (ymd == "mm" && num != null && num != "") {
            now.setMonth(now.getMonth() + num);
        }
        if ((ymd == null || ymd == "dd") && num != null && num != "") {
            now.setDate(now.getDate() + num);
        }
        var dd = now.getDate();
        if (dd < 10) dd = "0" + dd;
        var mm = now.getMonth() + 1;
        if (mm < 10) mm = "0" + mm;
        var currdate = now.getFullYear() + "-" + mm + "-" + dd;
        return currdate;
    }
    function getDateTimeStr(date, num) {
        var now = null;
        if (typeof(date) == 'undefined' || date == null) {
            now = new Date();
        }
        else {
            now = date;
        }
        var yy = now.getFullYear();//getYear();
        var mm = now.getMonth() + 1;
        if (mm < 10) mm = "0" + mm;
        var dd = now.getDate();
        if (num != null && num != "") {
            dd = dd + num;
        }
        if (dd < 10) dd = "0" + dd;
        var hh = now.getHours();
        if (hh < 10) hh = "0" + hh;
        var mi = now.getMinutes();
        if (mi < 10) mi = "0" + mi;
        var ss = now.getSeconds();
        if (ss < 10) ss = "0" + ss;
        var currdate = '' + yy + "-" + mm + "-" + dd + " " + hh + ":" + mi + ":" + ss;
        return currdate;
    }
    Array.prototype.indexOf = function (val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };
    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    function formatNum(num, digit) //将数字转换成三位逗号分隔的样式
    {
        if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)) {
            //alert("wrong!");
            return num;
        }
        var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
        var re = new RegExp().compile("(\\d)(\\d{3})(,|$)");
        while (re.test(b)) b = b.replace(re, "$1,$2$3");
        if (c && digit && new RegExp("^.(\\d{" + digit + "})(\\d)").test(c)) {
            if (RegExp.$2 > 4) c = (parseFloat(RegExp.$1) + 1) / Math.pow(10, digit);
            else c = "." + RegExp.$1;
        }

        if (!c) {
            c = 0.00;
            c = parseFloat(c).toFixed(digit);
        }
        if ((c+"").indexOf(".") != 0) {
            c = parseFloat(c).toFixed(digit);
        }

        return a + "" + b + "" + (c + "").substr((c + "").indexOf("."));
    }
    //alert(formatNum1(201, 2));

    function parseDate(dateTime) {
        return _parseDate(dateTime);
    }

    function _parseDate(dateTime) {
        var arr = dateTime.split("-");
        var date = new Date(arr[0], arr[1], arr[2]);
        var times = date.getTime();

        return times;
    }

    $.extend($.fn.validatebox.defaults.rules,{
        zhCheck:{
            validator:function(value,param){
                return /^[\u0391-\uFFE5]+$/.test(value);
            },
            message:'必须为中文'
        }
    });


    function toChinese(num){
        for(i=num.length-1;i>=0;i--)
        {
            num = num.replace(",","")
            num = num.replace(" ","")
        }
        num = num.replace("￥","")
        if(isNaN(num)) {
            alert("请检查小写金额是否正确");
            return;
        }
       var part = String(num).split(".");
       var newchar = "";
        for(i=part[0].length-1;i>=0;i--){
            if(part[0].length > 10){ alert("位数过大，无法计算");return "";}
           var tmpnewchar = ""
           var perchar = part[0].charAt(i);
            switch(perchar){
                case "0": tmpnewchar="零" + tmpnewchar ;break;
                case "1": tmpnewchar="壹" + tmpnewchar ;break;
                case "2": tmpnewchar="贰" + tmpnewchar ;break;
                case "3": tmpnewchar="叁" + tmpnewchar ;break;
                case "4": tmpnewchar="肆" + tmpnewchar ;break;
                case "5": tmpnewchar="伍" + tmpnewchar ;break;
                case "6": tmpnewchar="陆" + tmpnewchar ;break;
                case "7": tmpnewchar="柒" + tmpnewchar ;break;
                case "8": tmpnewchar="捌" + tmpnewchar ;break;
                case "9": tmpnewchar="玖" + tmpnewchar ;break;
            }
            switch(part[0].length-i-1){
                case 0: tmpnewchar = tmpnewchar +"元" ;break;
                case 1: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break;
                case 2: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break;
                case 3: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break;
                case 4: tmpnewchar= tmpnewchar +"万" ;break;
                case 5: if(perchar!=0)tmpnewchar= tmpnewchar +"拾" ;break;
                case 6: if(perchar!=0)tmpnewchar= tmpnewchar +"佰" ;break;
                case 7: if(perchar!=0)tmpnewchar= tmpnewchar +"仟" ;break;
                case 8: tmpnewchar= tmpnewchar +"亿" ;break;
                case 9: tmpnewchar= tmpnewchar +"拾" ;break;
            }
            newchar = tmpnewchar + newchar;
        }
        if(num.indexOf(".")!=-1){
            if(part[1].length > 2) {
                part[1] = part[1].substr(0,2)
            }
            for(i=0;i<part[1].length;i++){
                tmpnewchar = ""
                perchar = part[1].charAt(i)
                switch(perchar){
                    case "0": tmpnewchar="零" + tmpnewchar ;break;
                    case "1": tmpnewchar="壹" + tmpnewchar ;break;
                    case "2": tmpnewchar="贰" + tmpnewchar ;break;
                    case "3": tmpnewchar="叁" + tmpnewchar ;break;
                    case "4": tmpnewchar="肆" + tmpnewchar ;break;
                    case "5": tmpnewchar="伍" + tmpnewchar ;break;
                    case "6": tmpnewchar="陆" + tmpnewchar ;break;
                    case "7": tmpnewchar="柒" + tmpnewchar ;break;
                    case "8": tmpnewchar="捌" + tmpnewchar ;break;
                    case "9": tmpnewchar="玖" + tmpnewchar ;break;
                }
                if(i==0)tmpnewchar =tmpnewchar + "角";
                if(i==1)tmpnewchar = tmpnewchar + "分";
                newchar = newchar + tmpnewchar;
            }
        }
        while(newchar.search("零零") != -1)
            newchar = newchar.replace("零零", "零");
        newchar = newchar.replace("零亿", "亿");
        newchar = newchar.replace("亿万", "亿");
        newchar = newchar.replace("零万", "万");
        newchar = newchar.replace("零元", "元");
        newchar = newchar.replace("零角", "零");
        newchar = newchar.replace("零分", "");
        if ( newchar.charAt(newchar.length-1) == "零"){

            var arr = newchar.split('');
            arr[newchar.length-1] = '整';
            newchar=arr.join('');
        }
        if (newchar.charAt(newchar.length-1) == "元" || newchar.charAt(newchar.length-1) == "角" )
            newchar = newchar+"整"
        return newchar;
    }

</script>

