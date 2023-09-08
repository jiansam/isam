<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<style>
.select2-container-multi .select2-choices .select2-search-choice{
padding:0px 5px 0px 15px !important;;
}
</style>
<script type="text/javascript">
$(function() {

	setRejDefault();
	setDeleteThis();
	inputTextNext("#myform",".nextInput",".skip");
	setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
	setRedioToDefalut("isNew","${rejbean.isNew}");
	setRedioToDefalut("orgType","${rejbean.orgType}");
	
	setRedioToDefalut("currency","${rejectObj.currency}");
	setSelectedToDefalut("rejectType","${rejectObj.rejectType}");
	setRedioToDefalut("decision","${rejectObj.decision}");
	$("#sic").select2();
	if("${rSic}".length>0){
		var ary ="${rSic}".split(",");
		 $("#sic").select2("val",ary);
	}
	reloadApply("${rejectObj.serno}");
	if("${infoMap.editType}"!="edit"){
		$("input[name='investName']").blur(function(){
			checkCName($(this));
		});
	}
	$("input[name='money']").focus(function(){
		if($("input[name='currency']:checked").val()==="XXX"){
			//alert("請選擇幣別");
			$("input[name='currency'][value='NTD']").prop("checked",true);
			checkCurrencyLayout();
		}
	});
	$("#addApplicant").click(function(){
		checkApplicant();
	});
	$("#mySent").click(function(){
		if($("input[name='receiveDate']").val().length===0){
			alert("此欄位不可為空值");
			$("input[name='receiveDate']").focus();
			return false;
		}
		if($("input[name='issueDate']").val().length===0){
			alert("此欄位不可為空值");
			$("input[name='issueDate']").focus();
			return false;
		}
		if($("#ApplicantTable").find(".emptyData:visible").length>0){
			alert("至少需要填寫一位申請人");
			checkApplicant();
			return false;
		}
		if("${infoMap.editType}"==="edit"){
			checkRenameCName($("input[name='investName']"));
		}else{
			$("#myform").submit();
		}
	});
	
	checkCurrencyLayout();
	$("input[name='currency']").change(checkCurrencyLayout);
	
});
</script>
<script type="text/javascript">
function checkRenameCName($item){
	var cName=$item.val();
	var result=0;
	if($.trim(cName).length>0){
		$.post( "${pageContext.request.contextPath}/console/reject/checkcname.jsp",{
			'cname':cName,'cNo':$("input[name='cNo']").val()
		}, function(data){
			result=data.length;
		},"json").done(function(){
			if(result>0){
				$( "<div>系統已存在相同國內事業名稱，請選擇「確定」進行覆蓋資料，或選擇「取消」，修改國內事業名稱。</div>" ).dialog({
					 resizable: false,
				     height:200,
				     width:450,
				     modal: true,
				     title:"覆蓋國內事業",
				     buttons: {
				        "確定": function() {
				        	$("#myform").append("<input type='hidden' name='cNoUpdate' value='1'>").submit();
				          $( this ).dialog( "close" );
				        },
				        "取消": function() {
				          $( this ).dialog( "close" );
				        }
				    }
				})
			}else{
				$("#myform").submit();
			}
		});
	}else{
		alert("請先填寫國內事業名稱");
		$item.focus();
		return false;
	}
}

function checkCurrencyLayout(){
	if($("input[value='XXX']").prop('checked')){
		$("input[name='money']").css('color','white');
		$("input[name='moneyother']").css('color','');
		$("input[name='money']").attr('readonly','readonly');
		$("input[name='moneyother']").removeAttr('readonly');
	}
	else{
		$("input[name='money']").css('color','');
		$("input[name='moneyother']").css('color','white');
		$("input[name='money']").removeAttr('readonly');
		$("input[name='moneyother']").attr('readonly','readonly');
	}
}
	function checkApplicant(){
		var cNo=$("input[name='cNo']").val();
		var serno=$("input[name='serno']").val();
		var cname=$("input[name='investName']").val();
		if($("input[name='investName']").val().length===0&&cNo.length===0){
			alert("請先填寫國內事業名稱");
			$("input[name='investName']").focus();
			return false;
		}
		if(cNo.length===0||serno.length===0){
			getCNoXSerno(cname,serno,cNo);
		}else{
			toApplicant(cNo,serno);
		}
	}
	function toApplicant(cNo,serno){
		$.post( "${pageContext.request.contextPath}/console/reject/content/applicant.jsp",{
				'cNo':cNo,'serno':serno,'cname':$("input[name='investName']").val()
			}, function(data){
			$( "#appDig" ).html(data);
			$( "#appDig" ).dialog({
			      height:550,
			      width:820,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:$("input[name='investName']").val(),
			      buttons: {
			        "確定": function() {
			          if($.trim($("input[name='cApplicant']").val()).length===0){
			        	  alert("申請人姓名/公司名稱不可以為空白");
			        	  $("input[name='cApplicant']").focus();
			        	  return false;
			          }else{
		        	  	$.post( "${pageContext.request.contextPath}/console/reject/addapplicant.jsp",
		        			$("#DTForm").serialize()
		        		, function(data){
		        		},"json").done(function(){
		        			reloadApply($("input[name='serno']").val());
		        			alert("新增完成")
		        		});
				         $(this).dialog( "close" );
			          }
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			      },
			      close: function( event, ui ) {
			    	  $( "#appDig" ).html(""); 
			      }
		});
		},"html");
	}
	function getCNoXSerno(cname){
		var cNo="";
		var serno="";
		$.post( "${pageContext.request.contextPath}/console/reject/getcnoxserno.jsp",
			$( "#myform" ).serialize()
		, function(data){
			cNo=data.cNo;
			serno=data.serno;
			$("input[name='serno']").val(serno);
			$("input[name='cNo']").val(cNo);
		},"json").done(function(){
			toApplicant(cNo,serno);
		});
	}
	function checkCName($item){
		var cName=$item.val();
		var cNo="";
		if($.trim(cName).length>0){
			$.post( "${pageContext.request.contextPath}/console/reject/checkcname.jsp",{
				'cname':cName,'type':'add'
			}, function(data){
				cNo=data[0];
			},"json").done(function(){
				if(cNo.length>0){
					alert("此國內事業已經存在，系統將為您帶入相關資訊。");
					postUrlByForm('/console/reject/showform.jsp',{'cNo':cNo,'isExists':'1'});
				}
			});
		}else{
			alert("請先填寫國內事業名稱");
			$item.focus();
		}
	}
	function setRejDefault(){
		setRedioToDefalut("isNew","0");
		setRedioToDefalut("orgType","0");
		setRedioToDefalut("currency","NTD");
		setRedioToDefalut("decision","1");
	}
	function reloadApply(serno){
		$.post( "${pageContext.request.contextPath}/console/reject/content/reloadapply.jsp",{
				'serno':serno
			}, function(data){
			$( "#ApplicantTable" ).html(data); 
		 },"html").done(function(){
			 setDeleteAPL();
			 setEditAPL();
		});
	}
	function setDeleteThis(){
		$("#delThis").click(function(){
			var serno=$(this).prop("alt");
			$( "<div>刪除後，將無法復原本筆資料，請問是否繼續？</div>" ).dialog({
				 resizable: false,
			     height:200,
			     modal: true,
			     title:"刪除",
			     buttons: {
			        "確定": function() {
			          postUrlByForm('/console/reject/rejectdelete.jsp',{'serno':serno});
			          $( this ).dialog( "close" );
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			    }
			})
		});
	}
	function setDeleteAPL(){
		$(".delAPL").click(function(){
			var applyNo=$(this).prop("alt");
			$( "<div>刪除後，將無法復原本筆資料，請問是否繼續？</div>" ).dialog({
				 resizable: false,
			     height:200,
			     modal: true,
			     title:"刪除",
			     buttons: {
			        "確定": function() {
		        	$.post( "${pageContext.request.contextPath}/console/reject/content/deleteapply.jsp",{
		    			'applyNo':applyNo
		    		}, function(data){
			    	},"html").done(function(){
			          reloadApply($("input[name='serno']").val());
			          alert("資料已經刪除")
			    	});	
			          $( this ).dialog( "close" );
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			    }
			})
		});
	}
	function setEditAPL(){
		$(".editAPL").click(function(){
			var cNo=$("input[name='cNo']").val();
			var serno=$("input[name='serno']").val();
			var cname=$("input[name='investName']").val();
			var applyNo=$(this).prop("alt");
			$.post( "${pageContext.request.contextPath}/console/reject/content/applicant.jsp",{
				'cNo':cNo,'serno':serno,'cname':cname,'applyNo':applyNo
			}, function(data){
			$( "#appDig" ).html(data);
			$( "#appDig" ).dialog({
				 height:550,
			      width:820,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:$("input[name='investName']").val(),
			      buttons: {
			        "確定": function() {
			          if($.trim($("input[name='cApplicant']").val()).length===0){
			        	  alert("申請人姓名/公司名稱不可以為空白");
			        	  $("input[name='cApplicant']").focus();
			        	  return false;
			          }else{
		        	  	$.post( "${pageContext.request.contextPath}/console/reject/addapplicant.jsp",
		        			$("#DTForm").serialize()
		        		, function(data){
		        		},"json").done(function(){
		        			reloadApply($("input[name='serno']").val());
		        			alert("編輯完成")
		        		});
				         $(this).dialog( "close" );
			          }
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			      },
			      close: function( event, ui ) {
			    	  $( "#appDig" ).html(""); 
			      }
		});
		},"html");
		})
	}
</script>

<form action='<c:url value="/console/reject/editreject.jsp"/>' id="myform" method="post">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6;padding-top: 10px;">
			<span style="color:#F30;">[&nbsp;編輯國內事業基本資料&nbsp;]</span>
			<c:if test="${fn:contains(memberUrls,'E0401') && infoMap.editType eq 'edit'}">
				<div style="float: right;padding-right: 15px;"><input id="delThis" type="button" alt="${rejectObj.serno}" class="btn_class_opener" value="刪除"></div>
			</c:if>
		</legend>
	   		<input type="hidden" name="cNo" value="${rejbean.cNo}"/>
	   		<input type="hidden" name="serno" value="${rejectObj.serno}"/>
		    <table style="width: 98%;font-size: 16px;" class="formProj">
				<tr>
					<th colspan="4">國內事業基本資料</th>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${userInfo.company eq 'cier' || userInfo.company eq 'ibtech'}">
							<td class="trRight"><span style="color: red;">*</span>國內事業名稱：</td>				
							<td class="trLeft" style="width: 45%;">
								<c:choose>
									<c:when test="${infoMap.isExists eq 1}">${rejbean.cname}
										<input type="hidden" name="investName" value="${rejbean.cname}"/>
									</c:when>
									<c:otherwise>
										<input type="text" name="investName" style="width:95%;" value="${rejbean.cname}"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="trRight">統一編號：</td>				
							<td><input type="text" name="IDNO" maxlength="8" value="${rejbean.idno}"/></td>	
						</c:when>
						<c:otherwise>
							<td class="trRight">國內事業名稱：</td>				
							<td class="trLeft" colspan="3">
								<input type="text" name="investName" style="width:350px;" value="${rejbean.cname}"/>
								<input type="hidden" name="IDNO" value="${rejbean.idno}"/>
							</td>
						</c:otherwise>
					</c:choose>	
				</tr>
				<tr>
					<td class="trRight">設立情形：</td>
					<td>
						<c:forEach var="newitem" items="${opt.isNew}" varStatus="i">
							<input type="radio" name="isNew" id="isNew${i.index}" value="${newitem.key}"><label for="isNew${i.index}">${newitem.value}</label>
						</c:forEach>
					</td>
					<td class="trRight">設立日期：</td>
					<td>
						<input id="singledate" class="singledate" type="text" name="setupdate" value="${ibfn:addSlash(rejbean.setupdate)}">
					</td>
				</tr>					
				<tr>
					<td class="trRight">組織型態：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${opt.orgType}" varStatus="i">
							<input type="radio" name="orgType" id="orgType${i.index}" value="${newitem.key}"><label for="orgType${i.index}">${newitem.value}</label>
						</c:forEach>
					</td>
				</tr>					
		   	</table>
	</fieldset>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6;">
			<span style="color:#F30;">[&nbsp;編輯申請人與代理人資料&nbsp;]</span>
		   	<div style="float: right;padding-right: 15px;"><input id="addApplicant" type="button" class="btn_class_loaddata" value="新增申請人與代理人"></div>
		</legend>
			<div id="ApplicantTable">
	   		</div>
	</fieldset>
   	<div id="appDig">
   	</div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6;padding-top: 10px;">
			<span style="color:#F30;">[&nbsp;編輯相關資料&nbsp;]</span>
		</legend>
		    <table style="width: 98%;font-size: 16px;" class="formProj">
				<tr>
					<th colspan="4">申請相關資料</th>
				</tr>
				<tr>
					<td class="trRight"><span style="color: red;">*</span>申請日期：</td>
					<td>
						<input class="singledate" type="text" name="receiveDate" value="${ibfn:addSlash(rejectObj.receiveDate)}">
					</td>
					<td class="trRight">收文文號：</td>
					<td>
						<input type="text" name="receiveNo" value="${rejectObj.receiveNo}" maxlength="11">
					</td>
				</tr>					
				<tr>
					<td class="trRight">申請事項：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${opt.currency}" varStatus="i">
							<c:if test="${i.index eq 0}">投資金額(</c:if>
							<c:if test="${newitem.key eq 'XXX'}">
								)<span><input type="text" name="money" class="numberFmt" value="${rejectObj.money}">&nbsp;元</span>
								<br/>
							</c:if>
							<input type="radio" name="currency" id="currency${i.index}" value="${newitem.key}"><label for="currency${i.index}">${newitem.value}</label>
							<c:if test="${newitem.key eq 'XXX'}">
							
							<span>
							(限50字)
							</span>
							</c:if>
						</c:forEach>
					</td>
					
				</tr>
				<tr>
					<td class="trRight">其他說明：</td>
					<td colspan="3">
					<input type="text" name="moneyother" style="width: 90%;"  maxlength="50" value="${rejectObj.moneyother}"/>
					</td>
				</tr>
				<tr>
					<td class="trRight">持股比例：</td>
					<td colspan="3">
						<input type="text" name="shareholding" class="numberFmt" value="${rejectObj.shareholding}">&nbsp;%
					</td>
				</tr>
				<tr>
					<td class="trRight">業別項目：</td>
					<td colspan="3">
						<select name="sic" id="sic" multiple="multiple" style="width: 90%;">
							<c:forEach var="item" items="${twsic}" varStatus="i">
								<option value="${item.key}">${item.key}-${item.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="trRight">其他業別項目：</td>
					<td colspan="3">
						<textarea name="otherSic" rows="5" style="width: 95%;">${rejectObj.otherSic}</textarea>
					</td>
				</tr>
				<tr>
					<th colspan="4">審議相關資料</th>
				</tr>
				<tr>
					<td class="trRight" style="width: 15%;"><span style="color: red;">*</span>發文日期：</td>
					<td style="width: 40%;">
						<input class="singledate" type="text" name="issueDate" value="${ibfn:addSlash(rejectObj.issueDate)}">
					</td>
					<td class="trRight" style="width: 10%;">發文文號：</td>
					<td>
						<input type="text" name="issueNo" value="${rejectObj.issueNo}" style="width: 75%;">
					</td>
				</tr>					
				<tr>
					<td class="trRight">駁回類型：</td>
					<td colspan="3">
						<select name="rejectType" style="width: 90%;">
							<c:forEach var="newitem" items="${opt.rejectType}" varStatus="i">
								<option value="${newitem.key}">${newitem.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="trRight">駁回決議：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${opt.decision}" varStatus="i">
							<input type="radio" name="decision" id="decision${i.index}" value="${newitem.key}"><label for="decision${i.index}">${newitem.value}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td class="trRight">駁回決議說明：</td>
					<td colspan="3">
						<textarea name="explanation" rows="5" style="width: 95%;">${rejectObj.explanation}</textarea>
					</td>
				</tr>
				<tr>
					<td class="trRight">駁回理由：</td>
					<td colspan="3">
						<textarea name="reason" rows="5" style="width: 95%;">${rejectObj.reason}</textarea>
					</td>
				</tr>
				<tr>
					<td class="trRight">備註：</td>
					<td colspan="3">
						<textarea name="note" rows="5" style="width: 95%;">${rejectObj.note}</textarea>
					</td>
				</tr>
		   	</table>
	</fieldset>
	<div style="text-align: center;">
		<input type="button" id="mySent" class="btn_class_opener" value="送出"/>
	</div>
</form>