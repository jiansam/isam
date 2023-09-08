<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/js/hideOptionBySpan.js'/>" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$(".mySent").click(function(){
		$("FORM").submit();
	});
	//setToProjItem();
	if("${fn:contains(memberUrls,'R0101')}"==="true"){
		setToProjItem();
	}else{
		setToComItem();
	}
	if("${not empty terms}"){
		setRadioToDefalut("approval","${terms.approval}");
		setRadioToDefalut("year","${terms.year}");
		setRadioToDefalut("quarter","${terms.quarter}");
		setRadioToDefalut("type","${terms.type}");
		setRadioToDefalut("repType","${terms.repType}");
		setRadioToDefalut("declare","${terms.declare}");
 		if("${terms.approval}"==="0102"){
			setToComItem();
		}else{
			/* setToProjItem(); */
			if("${fn:contains(memberUrls,'R0101')}"==="true"){
				setToProjItem();
			}else{
				setToComItem();
			}
		}
	}
	$("input[name='approval']").change(function(){
		if($(this).val()==="0102"){
			setToComItem();
		}else{
			setToProjItem();
		}
	});
	$("input[name='type']").change(function(){
		if($("input[name='type']:checked").val()!=="01"){
			$(".rep01").hide();
		}else{
			$(".rep01").show();
		}
		setSelectRangeCommit();
	});
	$("select[name='year']").change(function(){
		var x = $("input[name='approval']:checked").val();
		if(x==="0101"){
			setSelectRangeProj(parseInt($(this).val(),10));
		}
	});
});
function setToProjItem(){
	$(".projItem").show();
	$(".comItem").hide();
	setYearRPRange("${pMax.minyear}");
	setSelectRangeProj("");
}
function setToComItem(){
	$(".projItem").hide();
	$(".comItem").show();
	if($("input[name='type']:checked").val()!=="01"){
		$(".rep01").hide();
	}
	//setYearRPRange("100");
	setSelectRangeCommit();
}
function setYearRPRange(min){
	var date = new Date();
	var maxY = date.getFullYear()-1911;
	var Q = Math.ceil((date.getMonth()+1/3)-1);
	if(Q===0){
		maxY=maxY-1;
	}
	var startY = parseInt(min, 10);
	var endY = parseInt(maxY, 10);
	var sel="";
	for(var i=endY;i>=startY;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$("select[name='year']").html(sel);
}
function setSelectRangeProj(y){
	var date = new Date();
	var maxY = parseInt(date.getFullYear())-1911;
	var Q = Math.ceil((date.getMonth()+1)/3-1);
	if(Q===0){
		maxY=maxY-1;
		Q=4;
	}
	if(y.length===0){
		y=maxY;
	}
	setSelectRange("quarter",y,maxY,Q,"${pMax.minyear}","${pMax.minQ}");
}
function setSelectRangeCommit(){
	var type=$("input[name='type']:checked").val();
	var min;
	var max;
	if(type==="01"){
		min="${cMax['01'].minyear}";
		max="${cMax['01'].maxyear}";
	}else if(type==="02"){
		min="${cMax['02'].minyear}";
		max="${cMax['02'].maxyear}";
	}else if(type==="03"){
		min="${cMax['03'].minyear}";
		max="${cMax['03'].maxyear}";
	}
	setYearOptionRange($("select[name='from']"),min,max);
	setYearOptionRange($("select[name='to']"),min,max);
	setYearRangeDefult($("select[name='from']"),min,3);
	setYearSelectRange($("select[name='from']"),$("select[name='to']"));
	hideYearRange($("select[name='to']"),$("select[name='from']").val());
}
function setYearOptionRange($select,minY,maxY){
	$select.html("");
	var startY = parseInt(minY, 10);
	var endY = parseInt(maxY, 10);
	var sel="";
	for(var i=endY;i>=startY;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$select.html(sel);
}
function setYearRangeDefult($from,minyear,yeargap){
	var maxY=parseInt($from.val(),10);
	var minY=parseInt(minyear,10);
	var mingap=maxY-yeargap+1;
	if(mingap>minY){
		$from.val(mingap);
	}else{
		$from.val(minY);
	}
}
function setRadioToDefalut(name,value){
	if(value.length>0){
		$("input[name='"+name+"'][value='"+value+"']").prop("checked",true);
	}
}
</script>
<div id="mainContent">
<form action="<c:url value='/approval/download.jsp'/>" method="post" class="myForm">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;下載審查執行情形&nbsp;</strong>&nbsp;</span>
		</legend>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;選擇資料類型&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin: 0px auto;">
				<div style="float:left;width: 80%;">
					<div class="basetable">
					<span>審查類型：</span>
					<c:choose>
						<c:when test="${fn:contains(memberUrls,'R0101')&&!fn:contains(memberUrls,'R0102')}">
							<input type="radio" name="approval" value="0101" id="R0101" checked="checked"><label for="R0101">專案</label>
						</c:when>
						<c:when test="${fn:contains(memberUrls,'R0102')&&!fn:contains(memberUrls,'R0101')}">
							<input type="radio" name="approval" value="0102" id="R0102" checked="checked"><label for="R0102">承諾</label>
						</c:when>
						<c:otherwise>
							<input type="radio" name="approval" value="0101" id="R0101" checked="checked"><label for="R0101">專案</label>
							<input type="radio" name="approval" value="0102" id="R0102"><label for="R0102">承諾</label>
						</c:otherwise>
					</c:choose>
					</div>
				</div>
				<div style="clear: left;"></div>	
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;設定查詢條件&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin: 0px auto;">
				<div class="basetable comItem">
					<span>管制類型：</span>
					<input type="radio" name="type" value="01" id="rep01" checked="checked"><label for="rep01">${CRType['01']}</label>
					<input type="radio" name="type" value="02" id="rep02"><label for="rep02">${CRType['02']}</label>
					<input type="radio" name="type" value="03" id="rep03"><label for="rep03">${CRType['03']}</label>
				</div>	
				<div class="basetable">
					<span>查詢年度：</span>
					<span class="projItem">
						<select style="margin:2px 0px;width: 15%;" name="year">
						</select>
					</span>
					<span class="comItem">
						<select style="margin:2px 0px;width: 15%;" name="from">
						</select>
						<label>~</label>
						<select style="margin:2px 0px;width: 15%;" name="to">
						</select>
					</span>
				</div>
				<div class="basetable projItem">
					<span>查詢期間：</span>
					<select style="margin:2px 0px;width: 15%;" name="quarter">
						<option value="1">第一季</option>
						<option value="2">第二季</option>
						<option value="3">第三季</option>
						<option value="4">年報</option>
					</select>
				</div>
				<div class="basetable comItem">
					<span>投資人統編：</span>
					<input type="text" name="idno" value="${idno}">
				</div>	
				<div class="basetable comItem">
					<span>投資人名稱：</span>
					<input type="text" name="investment" value="${investment}">
				</div>	
				<div class="basetable comItem rep01">
					<span>申報類型：</span>
					<input type="radio" name="repType" value="0103" id="code0103" checked="checked"><label for="code0103">${AoCode['0103']}</label>
					<input type="radio" name="repType" value="0101" id="code0101"><label for="code0101">${AoCode['0101']}</label>
					<input type="radio" name="repType" value="0102" id="code0102"><label for="code0102">${AoCode['0102']}</label>
				</div>	
				<div class="basetable">
					<span>申報狀態：</span>
					<input type="radio" name="declare" value="0" id="X01" checked="checked"><label for="X01">未申報</label>
					<input type="radio" name="declare" value="1" id="X02"><label for="X02">已申報</label>
					<input class="projItem" type="radio" name="declare" value="99" id="X04"><label for="X04" class="projItem">本次免申報</label>
					<input type="radio" name="declare" value="-1" id="X03"><label for="X03">不拘</label>
				</div>	
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 3&nbsp;]</span><strong style="color:#222;">&nbsp;下載檔案&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行下載：
				<img class="mySent" src="<c:url value='/images/sub/button_search.jpg'/>" width="60" height="22" style="cursor:pointer;" />
			</div>
		</fieldset>
	</fieldset>
	</form>
</div>