<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/hideOptionBySpan.js'/>"></script>
<script type="text/javascript">
$(function(){
	//107-07-26  因為加了select2，在選了國家變動程式選項時，會多一個設定是讓select2選第一個選項（設定在setTWAddrChangeSpan方法裡）
 	$("select[name='${param.City}'], select[name='${param.Town}']").select2({
		language: "zh-TW",
		closeOnSelect: false,
	});
	
	var classNo=".${param.fin}"+$("#lev1${param.City}").find("option:eq(0)").val();
	setTWAddrChangeSpan($("#lev2${param.Town}"),classNo);
	setTWAddrSpan($("#lev1${param.City}"),$("#lev2${param.Town}"),"${param.fin}");
	
	
	 if("${reBean.city}".length>0){
		//setSelectedToDefalut("city","${reBean.city}");
		//setSelectedToDefalut("town","${reBean.town}");

		setTWAddrChangeSpan($("#lev2${param.Town}"),".${reBean.city}");
		//107-09-04 改寫成select2的set值，這樣span裡面的字才會變成正確的區域
	 	setSelect2Value($("select[name='city']"), "${reBean.city}");
	 	setSelect2Value($("select[name='town']"), "${reBean.town}");
		
	 }
	if("${IOBaseInfo.editType}".length>0&&"${param.AddrStr}".length>0){
	 	if("${param.City}"==="City"){
			//setSelectedToDefalut("City","${ioclists.City}");
			//setSelectedToDefalut("Town","${ioclists.Town}");
			
			setTWAddrChangeSpan($("#lev2${param.Town}"),".${param.fin}${ioclists.City}");
			//107-09-04 改寫成select2的set值，這樣span裡面的字才會變成正確的區域
			setSelect2Value($("select[name='${param.City}']"), "${ioclists.City}");
		 	setSelect2Value($("select[name='${param.Town}']"), "${ioclists.Town}");

		}else{
			//setSelectedToDefalut("bcity","${ioclists.bcity}");
			//setSelectedToDefalut("btown","${ioclists.btown}");
			
			setTWAddrChangeSpan($("#lev2${param.Town}"),".${param.fin}${ioclists.bcity}");
			//107-09-04 改寫成select2的set值，這樣span裡面的字才會變成正確的區域
			setSelect2Value($("select[name='${param.City}']"), "${ioclists.bcity}");
		 	setSelect2Value($("select[name='${param.Town}']"), "${ioclists.btown}");
			
		} 
	}
});

function setSelect2Value($tag, value){  //ex: tag="#itvQAForm #industry"
	if(value.length != 0){
		var ary;
		if(value.indexOf(",") != -1){
			ary = value.split(",");
		}else {
			ary = [value];
		}
		$tag.val(ary).trigger('change'); //要加上trigger('change')，不然span不會放入選中的資料顯示出來
	  //$tag.val(ary);                   //適用複選，但是少了trigger
	  //$tag.select2("val",ary);         //只能用於單選，複選會只選到第一個（4.1會失效）
	}
}
</script>
<select id="lev1${param.City}" name="${param.City}" style="width: 90px;">
	<c:forEach var="item" items="${IOLV1}">
		<option value="${item.key}">${item.value}</option>
	</c:forEach>
</select>
<select id="lev2${param.Town}" name="${param.Town}" style="width: 90px;">
	<c:forEach var="i" items="${IOLV2}">
		<option value="${i.key}" class="${param.fin}${fn:substring(i.key,0,5)}">${i.value}</option>
	</c:forEach>
</select>
<input type="text" name="${param.Addr}" style="width: 75%;" value="${param.AddrStr}">