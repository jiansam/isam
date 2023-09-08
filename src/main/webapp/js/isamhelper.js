/*js版c:url，僅適用在有設專案名稱時，若沒有寫專案名稱會抓錯*/
function getRootPath(){
	var curWwwPath=window.document.location.href;
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName);
}
function getProjectName(){
	var pathName=window.document.location.pathname;
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return projectName;
}
function getAfterProjectName(){
    var pathName=window.document.location.pathname;
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    var afterProjName=pathName.substring(pathName.indexOf('/',1)+1);
    return afterProjName;
}
function getLocalHostXPort(){
	var curWwwPath=window.document.location.href;
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    return localhostPaht;
}
function startsWith(str,checkstr){
	return (str.substr(0,checkstr.length)===checkstr);
}
function endsWith(str,checkstr){
	return (str.substr(str.length-checkstr.length)===checkstr);
}
function contains(str,checkstr){
	return (str.indexOf(checkstr)>=0);
}
function getDatatableParam(tableId,$form) {
	var oTable = $(tableId).dataTable();
	var nNodes = oTable.fnGetNodes();
	var sData = $("input[name*='Temp']", nNodes).serialize();
	var sDataAry=sData.split("&");
	var dParamStr="<div style='display:none;'>";
	for(var i=0;i<sDataAry.length;i++){
		var data=sDataAry[i].split("=");
		var dParamName=data[0].substring(0,data[0].length-4);
		var dParamValue=data[1];
		dParamStr+="<input type='checkbox' name='"+dParamName+"' value='"+dParamValue+"' checked=checked>";
	}
	dParamStr+="</div>";
	$form.append(dParamStr).submit();
}
function postUrlByForm(url,values){
	var actionUrl=getRootPath()+url;
	var $form=$("<form></form>",{"method":"post","action":actionUrl});
	var params="";
	$.each(values,function(key,value){
		params+="<input type='hidden' value='"+value+"' name='"+key+"' />";
	});
	$form.append(params);
	$('body').append($form);
	$form.submit();
}
function postOpenUrlByForm(url,values){
	var actionUrl=getRootPath()+url;
	var $form=$("<form></form>",{"method":"post","action":actionUrl,"target":"_blank"});
	var params="";
	$.each(values,function(key,value){
		params+="<input type='hidden' value='"+value+"' name='"+key+"' />";
	});
	$form.append(params);
	$('body').append($form);
	$form.submit();
}
function postOpenUrlByForm1(url,values){
	var actionUrl=getRootPath()+url;
	var x="";
		$.each(values,function(key,value){
			if(x==""){
				x+="?"+key+"="+value;
			}else{
				x+="&"+key+"="+value;
			}
		});
	window.open(actionUrl+x);
}