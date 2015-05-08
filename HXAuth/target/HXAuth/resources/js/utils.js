/**
 * User: yulei
 * Date: 13-1-11
 * Time: 下午2:31
 */

var Utils = new Object();

/**
 * 按照某对象的的宽度乘以某比例，计算出一个新的宽度
 * @param obj 对象（jquery对象）
 * @param p 比例
 * @return {Number}
 */
Utils.getWidth = function(obj, p){
    var parentWidth = obj.width();
    return Math.round(parentWidth * p);
};

/**
 * 显示提示信息
 * @param msg
 */
Utils.showMsg = function(msg) {
    $.messager.show({
        title:'注意',
        msg:msg,
        timeout:5000,
        showType:'slide'
    });
};

/**
 * 显示确定对话框，确定后执行fn
 * @param msg
 * @param fn
 */
Utils.confirm = function(msg, fn) {
    $.messager.confirm('注意', msg,
        function(r) {
            if(r)
                fn();
        }
    );
};

Utils.loading = function(){
	$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",zIndex:9999999999,height:$(window).height()}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",zIndex:9999999999,left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}

Utils.loaded = function(){
	$('body').find("div.datagrid-mask-msg").remove(); 
	$('body').find("div.datagrid-mask").remove(); 	
}
