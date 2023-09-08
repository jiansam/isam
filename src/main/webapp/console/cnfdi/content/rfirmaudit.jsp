<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript">
	$(function(){
		var y=$.extend({
			"aaSorting": [[ 1, "desc" ]],
			"aoColumnDefs": [
                             {'sClass':'center', "aTargets": [ 0 ] },
			                 {"sSortDataType":"chinese", "aTargets": [ 2,3 ]},
			                 {"sType":"string", "aTargets": [ 1 ]},
                             {"bSortable": false, "aTargets": [0]}
			                // ,{ "bVisible": false, "aTargets": [6,7,8] }
			              ],
			 "bFilter": false, "bPaginate": false, "bInfo": false,
			 "fnDrawCallback": function ( oSettings ) {
							/* Need to redo the counters if filtered or sorted */
							if ( oSettings.bSorted || oSettings.bFiltered ){
								for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
									$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
								}
							}
						}
		},sdInitDataTableSetting(),sdSortChinese());
		var oTable=$("#audit02table").dataTable(y);

		$("#newTwo").click(function(){
			newTwo();
		});
		$(".show02").click(function(){
			showTwo($(this));
		});
		
		//107-10-25
		var oTable=$("#audit07table").dataTable(y);
		$("#newSeven").click(function(){
			newSeven();
		});
		$(".show07").click(function(){
			showSeven($(this));
		});		
	});
	function newTwo(){
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit02.jsp",{
			'type':'add','investNo':"${sysinfo.INVESTMENT_NO}","tabsNum":$("#tabs").tabs( "option", "active" )
		}, function(data){
			$( "#audit02hide").html(data); 
			var $d=$("#date02").datepicker({
				dateFormat:"yy/mm/dd",
				changeYear: true, //手動修改年
		        changeMonth: true, //手動修改月			
		        maxDate: "+0",
		        onSelect: function (dateText, inst) {
		        	var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; //取出格式文字
		            var reM = /m+/g;
		            var reD = /d+/g;
		            var year=inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911;
		            if(String(year).length<3){
		            	year="0"+year;
		            }
		            var month=parseInt(inst.selectedMonth,10) >8  ? inst.selectedMonth + 1 : "0" + String(inst.selectedMonth + 1);
		            var date=String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay);
		            
		             inst.input.val(year+"/"+month+"/"+date);
		             $("#audit02hide input:eq(1)").focus();
		         }
			});
			$("#date02").datepicker("disable");
			$("#audit02hide").dialog({
				height: 430,
				width: 650,
				modal: true,
				draggable: false,
				title:"稽核二 公文附加附款案件",
				open:function() {
					$("#date02").datepicker("enable");
				},
				close: function() {
					$("#audit02hide input").val("");
					$("#audit02hide textarea").val("");
					$("#date02").datepicker("destroy");
				}
		});
		},"html");
	}
	function showTwo($item){
		var seq=$item.prop("alt");
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit02.jsp",{
			'type':'show','investNo':"${sysinfo.INVESTMENT_NO}","seq":seq,"tabsNum":$("#tabs").tabs( "option", "active" )
		}, function(data){
			$( "#audit02hide").html(data); 
			$("#audit02hide").dialog({
				height: 430,
				width: 650,
				modal: true,
				draggable: false,
				title:"稽核二 公文附加附款案件"
		});
		},"html");
	}
</script>
<div class='tbtitle'>稽核</div>
<div id="audit02hide"></div>
<div id="audit07hide"></div>
<table style="width: 95%;font-size: 16px;margin-left: 15px;" class="tchange">
	<c:forEach var="opt" items="${auditOpt}">
		<c:choose>
			<c:when test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
			</c:when>
			<c:when test="${fn:startsWith(opt.auditCode,'07') && fn:length(opt.auditCode)>2}">
			</c:when>			
			<c:when  test="${fn:length(opt.auditCode)!=2 && empty audit[opt.auditCode]}">
				<c:if test="${opt.auditCode=='0603'}"> 
				<tr>
					<td style="text-align: right;width: 30%;vertical-align: text-top;">${opt.description}：</td>
					<td>${empty audit[opt.auditCode]?'無':audit[opt.auditCode]}</td>
				</tr>
				</c:if>
			</c:when>
			<c:otherwise>
				<tr <c:if test="${fn:length(opt.auditCode)==2&&opt.auditCode!='01'}"> class="tbuborder"</c:if>>
					<td style="text-align: right;width: 30%;vertical-align: text-top;">
						<c:if test="${fn:length(opt.auditCode)==2}">(${opt.auditCode})</c:if>${opt.description}：
					</td>
					<td style="word-break: break-all;">
						<c:choose>
							<c:when test="${opt.selectName =='date'}">${ibfn:addSlash(audit[opt.auditCode])}</c:when>
							<c:when test="${opt.selectName =='YN' && opt.autoDef == '1'}">
								<c:choose>
									<c:when test="${audit[opt.auditCode]=='0'||opt.auditCode=='04'}">${optmap.YN[audit[opt.auditCode]]}</c:when>
									<c:when test="${empty audit[opt.auditCode]}">暫無訪查資料</c:when>
									<c:otherwise>${audit[opt.auditCode]}</c:otherwise>
								</c:choose>							
							</c:when>
							<c:when test="${opt.selectName =='YN'}">${empty optmap.YN[audit[opt.auditCode]]?optmap.YN['']:optmap.YN[audit[opt.auditCode]]}</c:when>
							<c:when test="${opt.selectName =='dept'}">
								<c:forEach var="x" items="${fn:split(audit[opt.auditCode],',')}" varStatus="i">
									<c:if test="${i.index>0}">、</c:if>
									${deptOpt[x]}
								</c:forEach>
							</c:when>
							<c:when test="${opt.selectName =='city'}">
								<c:forEach var="x" items="${fn:split(audit[opt.auditCode],',')}" varStatus="i">
									<c:if test="${i.index>0}">、</c:if>
									${IOLV1[fn:substring(x,0,5)]}${IOLV2[x]}
								</c:forEach>
							</c:when>
							<c:otherwise>${audit[opt.auditCode]}</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<c:if test="${opt.auditCode=='02'}">
			<tr>
				<td colspan="2">
					<div style="text-align: right;">
						<input id="newTwo" type="button" class="btn_class_loaddata" value="新增">
					</div>
					<table id="audit02table" style="width: 90%;">
						<thead>
							<tr>
								<th>序號</th>
								<th>發文日期</th>
								<th>文號</th>
								<th>需繳交資料</th>
								<th>檢視</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${audit02}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(item.value['0201'])}</td>
									<td>${item.value['0202']}</td>
									<td>${optmap.YN[item.value['0205']]}</td>
									<td align="center"><input class="show02 btn_class_opener" type="button" value="檢視" alt="${item.key}"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
		</c:if>
		
		<c:if test="${opt.auditCode=='07'}">
			<tr>
				<td colspan="2">
					<div style="text-align: right;">
						<input id="newSeven" type="button" class="btn_class_loaddata" value="新增">
					</div>
					<table id="audit07table" style="width: 90%;">
						<thead>
							<tr>
								<th>序號</th>
								<th>核准日期</th>
								<th>核准文號</th>
								<th>說明</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${audit07}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(item.value['0701'])}</td>
									<td>${item.value['0702']}</td>
									<td>${item.value['0703']}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
		</c:if>		
	</c:forEach>
</table>

<script>
function newSeven(){
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit07.jsp",{
		'type':'add','investNo':"${sysinfo.INVESTMENT_NO}","tabsNum":$("#tabs").tabs( "option", "active" )
	}, function(data){
		$( "#audit07hide").html(data); 
		var $d=$("#date07").datepicker({
			dateFormat:"yy/mm/dd",
			changeYear: true, //手動修改年
	        changeMonth: true, //手動修改月			
	        maxDate: "+0",
	        onSelect: function (dateText, inst) {
	        	var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; //取出格式文字
	            var reM = /m+/g;
	            var reD = /d+/g;
	            var year=inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911;
	            if(String(year).length<3){
	            	year="0"+year;
	            }
	            var month=parseInt(inst.selectedMonth,10) >8  ? inst.selectedMonth + 1 : "0" + String(inst.selectedMonth + 1);
	            var date=String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay);
	            
	             inst.input.val(year+"/"+month+"/"+date);
	             $("#audit07hide input:eq(1)").focus();
	         }
		});
		$("#date07").datepicker("disable");
		$("#audit07hide").dialog({
			height: 430,
			width: 650,
			modal: true,
			draggable: false,
			title:"稽核七 委員會核准之重大投資案",
			open:function() {
				$("#date07").datepicker("enable");
			},
			close: function() {
				$("#audit07hide input").val("");
				$("#audit07hide textarea").val("");
				$("#date07").datepicker("destroy");
			}
	});
	},"html");
}
function showSeven($item){
	var seq=$item.prop("alt");
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit07.jsp",{
		'type':'show','investNo':"${sysinfo.INVESTMENT_NO}","seq":seq,"tabsNum":$("#tabs").tabs( "option", "active" )
	}, function(data){
		$( "#audit07hide").html(data); 
		$("#audit07hide").dialog({
			height: 430,
			width: 650,
			modal: true,
			draggable: false,
			title:"稽核七 委員會核准之重大投資案"
	});
	},"html");
}
</script>
