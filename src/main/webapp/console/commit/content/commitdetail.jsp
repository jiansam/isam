<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatableOld.js'/>"></script>
<script>
$(function() {
	$("#changeState").hide();
	$("#changebtn").button({ icons:{primary: "ui-icon-gear",secondary:"ui-icon-triangle-1-s"}})
			.click(function(){
			var menu = $("#changeState").show().position({
				            my: "left top",
				            at: "left bottom",
				            of: this
				          });
			$( document ).one( "click", function() {
	            menu.hide();
	        });
	        return false;
	});
	$("#changeState li").hover(function(){
		$(this).toggleClass("active");
	});
	$(".datatable").dataTable(getOption());
	$(".DTRestrain").dataTable(getOption2([4,5]));
	$(".datailByInvest").dataTable(getOption());
});
$(function() {
    $( "#tabs" ).tabs();
	$(".parentCheck").change(function(){
		checkChild($(this));
	});
	$(".childCheck").change(function(){
		checkParent($(this));
	});
	setOfficeChecked("${cdInfo.officstr}","restrainType");
	//getAlertInfo("${cdInfo.updateOK}");
	
	//107-01-04 如果要調整完管制或解除管制，要繼續編輯聯絡人，就跳轉到聯絡人tab
	//console.log("editContact="+"${editContact}")
	if("${editContact}" == "Y"){
		$( "#tabs" ).tabs( "option", "active", 5 ); //跳往訪視群組tab
	}
  });
function toEditPage(serno,idno,investor){
	postUrlByForm('/console/commit/commiteditview.jsp',{'serno':serno,'editType':'edit','idno':idno,'investor':investor});
}
function toEditDateilPage(repserno){
	postUrlByForm('/console/commit/showsubcommitreport.jsp',{'repserno':repserno,'editType':'edit'});
}
function toCheckPage(serno,idno,investor){
	postUrlByForm('/console/commit/commitviewo.jsp',{'serno':serno,'editType':'show','idno':idno,'investor':investor});
}
function toAddReport(serno,idno,investor){
	 postUrlByForm('/console/commit/commitaddreport.jsp',{'serno':serno,'editType':'add','idno':idno,'investor':investor});
}
function toEditReport(repserno,idno,investor){
	if("${fn:contains(memberUrls,'E0102')}"==="true"){
		postUrlByForm('/console/commit/commitaddreport.jsp',{'repserno':repserno,'editType':'edit','idno':idno,'investor':investor});
	}else{
	 	postUrlByForm('/approval/seecommit.jsp',{'repserno':repserno,'type':'edit','idno':idno,'investor':investor});
	}
}
function getAlertInfo(updateOK){
	  if(updateOK.length>0){
		 if(updateOK==="0"){
			 $( "#tabs" ).tabs("option","active",updateOK);
			 alert("承諾企業編輯項目編輯成功。");
		 }else if(updateOK==="1"){
			 $( "#tabs" ).tabs("option","active",updateOK);
			 alert("管制項目狀態編輯成功。");
		 }else if(updateOK==="2"){
			 $( "#tabs" ).tabs("option","active",updateOK);
			 alert("執行情形編輯成功。");
		 }else if(updateOK==="5"){
			 $( "#tabs" ).tabs("option","active",updateOK);
			 alert("聯絡人編輯成功。");
		 }else if(updateOK==="6"){
			 $( "#tabs" ).tabs("option","active","5");
			 alert("管制項目狀態編輯成功，請繼續編輯聯絡人。");
		 }
	  }	  
}
function setOfficeChecked(checkStr,name){
	  if(checkStr.length>0){
		  var ary=checkStr.split(",");
		  $("input[name='"+name+"']").each(function(){
			  var v= $(this).val();
			  if($.inArray(v,ary)!=-1){
				  $(this).prop("checked",true);
				  checkParent($(this));
			  }
		  });
	  }	  
}
/* function setOfficeReadonly(name){
  $("input[id^='"+name+"']").each(function(){
		$(this).prop("readonly",true);
  });
} */
  function checkChild($thisObj){
	var flag=$thisObj.prop("checked");
	var val=$thisObj.val();
	var x="input[name='restrainType'][value^='"+val+"']";
	$(x).prop("checked",flag);
  }
  function checkParent($thisObj){
	var flag=true;
	var thisCheck=$thisObj.prop("checked");
	var $parentObj=$thisObj.parent().children(".parentCheck");
	$thisObj.parent().children(".childCheck").each(function(){
		if($(this).prop("checked")!==thisCheck){
			flag=false;
			$parentObj.prop("checked",false);
			return;
		}
	});
	if(flag){
		$parentObj.prop("checked",thisCheck);
	}
  }
  function getOption(){
	  var option={
				"bAutoWidth" : false, //自適應寬度
				"bLengthChange": false,
				//"bFilter": false,
				//多語言配置
				"oLanguage" : {
					"sProcessing" : "正在載入中......",
					"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
					"sZeroRecords" : "對不起，查詢不到相關資料！",
					"sEmptyTable" : "目前尚無資料！",
					"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料",
					"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料",
					"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
					"sSearch" : "搜尋",
					"oPaginate" : {
						"sFirst" : "最前頁",
						"sPrevious" : "上一頁",
						"sNext" : "下一頁",
						"sLast" : "最末頁"
					}
				}
		};
	  return option;
  }
</script>
<c:if test="${fn:contains(memberUrls,'E0102')}">
<c:set var="EditType" value="修改" />
<c:set var="isEditor" value="Y" />
</c:if>
<c:if test="${!fn:contains(memberUrls,'E0102')}">
<c:set var="EditType" value="檢視" />
</c:if>
<c:if test="${not empty newadd}">
<c:set var="isEditor" value="Y" />
</c:if>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;承諾編輯&nbsp;]</span>&nbsp;&nbsp;
					<span style="margin-left: 830px;">
						<a href="<c:url value='/console/commit/showcommit.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<strong style="color:#222;margin-left: 15px;margin-bottom: 5px;">企業基本資料</strong><br/>
					<table style="width: 100%;">
						<tr>
							<td style="width: 25%;text-align: right;">投資人名稱(統一編號)：</td>
							<td>${cdInfo.investor}（${cdbean.IDNO}）</td>
						</tr>
						<c:if test="${not empty cdInfo.minmaxyear}">
						<tr>
							<td style="width: 25%;text-align: right;">彙總申報起始年：</td>
							<td>${cdInfo.minmaxyear}</td>
						</tr>
						</c:if>
						<c:if test="${not empty cdInfo.restrainOffice}">
						<tr>
							<td style="width: 25%;text-align: right;">涉及機關：</td>
							<td>${cdInfo.restrainOffice}</td>
						</tr>
						</c:if>
						<tr>
							<td style="width: 25%;text-align: right;">最後修改人(日期)：</td>
							<td>${cdInfo.lastEditor}（${cdInfo.lastUpdate}）</td>
						</tr>
					</table>					
				</div>
				<div id="tabs" style="font-size: 14px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">承諾企業編輯項目</a></li>
					    <li><a href="#tabs-2">管制項目狀態</a></li>
					    <li><a href="#tabs-3">執行情形彙總</a></li>
					    <li><a href="#tabs-4">涉及大陸事業</a></li>
					    <li><a href="#tabs-5">涉及文號</a></li>
					    <li><a href="#tabs-6">聯絡人資訊</a></li>
					  </ul>
					  <div id="tabs-1">
					  	<form id="Updatedetail" action='<c:url value="/console/commit/updatecommitdetail.jsp"/>' method="post">
					        <div style="font-size: 16px;margin-top: 5px;">
					        	<c:if test="${not empty isEditor}">
						        	<div style="margin-bottom: 10px;">
							        	<strong style="color:#222;margin-left: 15px;margin-bottom: 5px;">特殊列管項目：</strong><br/>
							        	<span style="margin-left: 25px;margin-bottom: 5px;">
											<input type="checkbox" name="restrainType" value="0501" id="restrainType0501"/><label for="restrainType0501">逾期核備</label>&nbsp;
										</span><br/>
							        	<span style="margin-left: 25px;margin-bottom: 5px;">
											<input type="checkbox" name="restrainType" value="0502" id="restrainType0502"/><label for="restrainType0502">違規投資</label>&nbsp;
										</span><br/>
							        	<span style="margin-left: 25px;margin-bottom: 5px;">
											<input type="checkbox" name="restrainType" value="0503" id="restrainType0503"/><label for="restrainType0503">未按時申報季報／年報</label>&nbsp;
										</span><br/>
							        	<span style="margin-left: 25px;margin-bottom: 5px;">
											<input type="checkbox"  value="0504" class="parentCheck" id="restrainType0504"/><label for="restrainType0504">勞資爭議</label>&nbsp;
											（<input type="checkbox" name="restrainType" value="050401" id="restrainType050401" class="childCheck"/><label for="restrainType050401">勞資糾紛</label>&nbsp;<input type="checkbox" name="restrainType" value="050402" id="restrainType050402" class="childCheck"/><label for="restrainType050402">改善勞動條件</label>&nbsp;<input type="checkbox" name="restrainType" value="050403" id="restrainType050403" class="childCheck"/><label for="restrainType050403">無薪假廠商</label>&nbsp;）
										</span><br/>
							        	<span style="margin-left: 25px;margin-bottom: 5px;">
											<input type="checkbox" value="0505" class="parentCheck" id="restrainType0505"/><label for="restrainType0505">經補助之技術列管</label>&nbsp;
											（<input type="checkbox" name="restrainType" value="050501" id="restrainType050501" class="childCheck"/><label for="restrainType050501">經濟部產業發展署</label>&nbsp;<input type="checkbox" name="restrainType" value="050502" id="restrainType050502" class="childCheck"/><label for="restrainType050502">經濟部產業技術司</label>&nbsp;<input type="checkbox" name="restrainType" value="050503" id="restrainType050503" class="childCheck"/><label for="restrainType050503">國科會</label>&nbsp;）
										</span><br/>
							        	<span style="margin-left: 25px;margin-bottom: 5px;">
											<input type="checkbox" name="restrainType" value="0506" id="restrainType0506"/><label for="restrainType0506">請託關說／檢舉陳情</label>&nbsp;
										</span>
										</div>
							        	<strong style="color:#222;margin-left: 15px;">備註：</strong>
								    	<textarea name="notes" rows="12" style="width: 90%;vertical-align: text-top;">${cdbean.note}</textarea>
											<input type="hidden" name="serno" value="${cdbean.IDNO}"/>
								    	<div style="text-align: center; margin-top: 10px;font-size: 14px;">
											<input type="submit" id="myUpdate" class="btn_class_Approval" value="修改"/>
										</div> 
								</c:if>
								<c:if test="${empty isEditor}">
									<c:if test="${empty cdInfo.officstr && empty cdbean.note}">
										<strong style="color:#222;margin-left: 15px;margin-bottom: 5px;">無特殊列管項目且無備註</strong><br/>
									</c:if>
									<c:if test="${not empty cdInfo.officstr}">
										<div style="margin-bottom: 10px;margin-left: 15px;">
											<div style="margin-bottom: 5px;">
											<c:set var="offic" value="${fn:split(cdInfo.officstr,',')}"/>
							        		<strong style="color:#222;">特殊列管項目：</strong><br/>
							        		</div> 
							        		<div>
							        		<c:forEach var="itemOffic" items="${offic}">
							        			<c:if test="${not empty CRType[fn:substring(itemOffic,0,4)]&&fn:length(itemOffic)>4}">${CRType[fn:substring(itemOffic,0,4)]}-</c:if>${CRType[itemOffic]}<br/>
							        		</c:forEach>
							        		</div>
							    		</div>
						    		</c:if>
										<c:if test="${not empty cdbean.note}">
											<div style="margin-left: 15px;">
									        		<strong style="color:#222;margin-bottom: 0px;">備註：</strong> 
											    	<pre style="margin-top: 10px;">${cdbean.note}</pre>
											</div>
										</c:if>
								</c:if>
							</div>
					    </form>
					  </div>
					  <div id="tabs-2" style="margin-bottom: 20px;">
					  	<div id="changeDiv" style="font-size: 16px;margin-left: 800px;">
							<button id="changebtn" style="font-size: 14px;">點此新增管制項目</button>
							<ul id="changeState" >
							    <li><div class="myChange" onclick="postUrlByForm('/console/commit/commiteditview.jsp',{'idno':'${cdbean.IDNO}','investor':'${cdInfo.investor}','editType':'add','type':'1'});" ><span class="triangle"></span>國內相對投資計畫</div></li>
							    <li><div class="myChange" onclick="postUrlByForm('/console/commit/commiteditview.jsp',{'idno':'${cdbean.IDNO}','investor':'${cdInfo.investor}','editType':'add','type':'2'});"><span class="triangle"></span>改善財務</div></li>
							    <li><div class="myChange" onclick="postUrlByForm('/console/commit/commiteditview.jsp',{'idno':'${cdbean.IDNO}','investor':'${cdInfo.investor}','editType':'add','type':'3'});"><span class="triangle"></span>資金回饋</div></li>
							    <li><div class="myChange" onclick="postUrlByForm('/console/commit/commiteditview.jsp',{'idno':'${cdbean.IDNO}','investor':'${cdInfo.investor}','editType':'add','type':'4'});"><span class="triangle"></span>金融服務</div></li>
							</ul>
						</div>
						<c:if test="${empty crlist}">
							<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
						</c:if>
						<c:if test="${not empty crlist}">
							<strong style="margin:5px 10px;color:#222;font-size: 16px;">管制項目狀態彙總</strong>
							<table class="DTRestrain" style="font-size: 16px;text-align: center;width: 100%;">
								<thead>
								<tr>
									<th>項目</th>
									<th>期間</th>
									<th>狀態</th>
									<th>提示</th>
									<th>檢視管制項目</th>
									<c:if test="${fn:contains(memberUrls,'E0102')}">								
									<th>修改管制項目</th>
									</c:if>								
									<th>填報執行情形</th>								
								</tr></thead>
								<tbody>
								<c:forEach var="crbean" items="${crlist}">
									<tr>
										<td>${crbean.type}</td>
										<td>${crbean.startYear}~${crbean.endYear}</td>
										<td>${crbean.state}</td>
										<td style="text-align: center;">${crbean.needAlert eq '1'?'請確認':''}</td>
										<td style="text-align: center;">
											<input type="button" onclick="toCheckPage('${crbean.serno}','${cdbean.IDNO}','${cdInfo.investor}')" class="btn_class_opener" style="text-align: center;font-size: 12px;" value="檢視"/>
										</td>
										<c:if test="${fn:contains(memberUrls,'E0102')}">
											<td style="text-align: center;">
												<input type="button" onclick="toEditPage('${crbean.serno}','${cdbean.IDNO}','${cdInfo.investor}')" class="myEdit btn_class_opener" style="text-align: center;font-size: 12px;" value="修改"/>
											</td>
										</c:if>
										<td style="text-align: center;">
											<c:if test="${crbean.type != '金融服務'}">
												<input type="button" onclick="toAddReport('${crbean.serno}','${cdbean.IDNO}','${cdInfo.investor}')" class="myEdit btn_class_opener" style="text-align: center;font-size: 12px;" value="新增"/>
											</c:if>
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</c:if>
					  </div>
					  <div id="tabs-3" style="margin-bottom: 20px;">
					  	<c:if test="${empty povCR && empty subcrSer}">
							<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
						</c:if>
						<c:if test="${not empty povCR|| not empty subcrSer}">
							<jsp:include page="/console/commit/content/commitdetailreport.jsp" flush="true" >
								<jsp:param value="${EditType}" name="EditType"/>
							</jsp:include>
						</c:if>
					  </div>
					  <div id="tabs-4" style="margin-bottom: 20px;">
					  	<c:if test="${not empty investList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">涉及大陸事業</strong>
							<table class="datatable" style="font-size: 16px;width: 100%;">
								<thead>
								<tr>
									<th>案號</th>
									<th>大陸事業名稱</th>
									<th>管制項目</th>
								</tr>
								</thead>
								<tbody>
								<c:forEach var="invest" items="${investList}">
									<tr>
										<td>${invest[0]}</td>										
										<td>${invest[1]}</td>										
										<td>${invest[2]}</td>										
									</tr>
								</c:forEach>
								</tbody>
							</table>
					  	</c:if>
					  	<c:if test="${empty investList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
					  	</c:if>
					  </div>
					  <div id="tabs-5" style="margin-bottom: 20px;">
					  	<c:if test="${not empty creNOList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">涉及文號</strong>
							<table class="datatable" style="font-size: 16px;width: 100%;">
								<thead>
								<tr>
									<th>核准日</th>
									<th>文號</th>
									<th>事由</th>
									<th>管制項目</th>
								</tr></thead><tbody>
								<c:forEach var="reNo" items="${creNOList}">
									<tr>
										<td>${reNo[0]}</td>										
										<td>${reNo[1]}</td>										
										<td>${reNo[2]}</td>										
										<td>${reNo[3]}</td>										
									</tr>
								</c:forEach></tbody>
							</table>
					  	</c:if>
					  	<c:if test="${empty creNOList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
					  	</c:if>
					  </div>
					  <div id="tabs-6" style="margin-bottom: 20px;">
					  	<jsp:include page="/console/commit/content/contacts.jsp" flush="true" />
					  </div>
				</div>
			</fieldset>
		</div>
	<script>
	$(function() {
		getAlertInfo("${cdInfo.updateOK}");
	  });
	</script>	