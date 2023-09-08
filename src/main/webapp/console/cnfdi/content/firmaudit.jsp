<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript">
	$(function(){
		$(".auditSelect").each(function(){
			var tmp=$(this).prop("name");
			$("#"+tmp).select2();
		});
		if("${audit['0101']}".length>0){
			var ary ="${audit['0101']}".split(",");
			 $("#audit0101").select2("val",ary);
		}
		if("${audit['0301']}".length>0){
			var ary ="${audit['0301']}".split(",");
			 $("#audit0301").select2("val",ary);
		}
		if("${audit['0501']}".length>0){
			var ary ="${audit['0501']}".split(",");
			 $("#audit0501").select2("val",ary);
		}
		var y=$.extend({
			"aaSorting": [[ 1, "desc" ]],
			"aoColumnDefs": [
                             {'sClass':'center', "aTargets": [ 0,4,5 ] },
			                 {"sSortDataType":"chinese", "aTargets": [ 2,3 ]},
			                 {"sType":"string", "aTargets": [ 1 ]},
                             {"bSortable": false, "aTargets": [0]}
			                // ,{ "bVisible": false, "aTargets": [6,7,8] }
			                ],
			"bFilter": false, 
			"bPaginate": false, 
			"bInfo": false,
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
			if($("input[name='audit02']:checked").val()!=="1"){
				$("<div>[(02)公文附加附款案件]選項將改為[是]，並一併更新所有稽核資料，請問是否繼續新增?</div>").dialog({
					height: 200,
					width: 400,
					modal: true,
					draggable: false,
					buttons: {
					  "確定": function() {
						  $(this).dialog( "close" );
						  $("input[name='audit02'][value='1']").prop("checked",true);
						  newTwo();
					  },
					  "取消": function() {
						  $(this).dialog( "close" );
					  }
					},
				});
			}else{
				newTwo();
			}
		});
		$(".edit02").click(function(){
			var $item=$(this);
			$("<div>編輯[(02)公文附加附款案件]，將一併更新現有稽核資料，請問是否繼續編輯?</div>").dialog({
				height: 200,
				width: 400,
				modal: true,
				draggable: false,
				buttons: {
				  "確定": function() {
					  $(this).dialog( "close" );
					  editTwo($item);
				  },
				  "取消": function() {
					  $(this).dialog( "close" );
				  }
				},
			});			
		});
		$(".del02").click(function(){
			var seq=$(this).prop("alt");
			$("<div>刪除後將無法復原，請確認是否繼續刪除本筆資料？</div>").dialog({
				height: 200,
				width: 300,
				modal: true,
				title:"刪除",
				draggable: false,
				buttons: {
				  "確定": function() {
					  postUrlByForm('/console/cnfdi/updateaudit02.jsp',{'seq':seq,'type':'delete','investNo':"${sysinfo.INVESTMENT_NO}","tabsNum":$("#tabs").tabs( "option", "active" )});
				    $(this).dialog( "close" );
				  },
				  "取消": function() {
					  $(this).dialog( "close" );
				  }
				},
			});
		});
		
		
		//107-10-22
		var oTable=$("#audit07table").dataTable(y);
		$("#newSeven").click(function(){
			if($("input[name='audit07']:checked").val()!=="1"){
				$("<div>[(07)委員會核准之重大投資案]選項將改為[是]，並一併更新所有稽核資料，請問是否繼續新增?</div>").dialog({
					height: 220,
					width: 400,
					modal: true,
					draggable: false,
					buttons: {
					  "確定": function() {
						  $(this).dialog( "close" );
						  $("input[name='audit07'][value='1']").prop("checked",true);
						  newSeven();
					  },
					  "取消": function() {
						  $(this).dialog( "close" );
					  }
					},
				});
			}else{
				newSeven();
			}
		});
		$(".edit07").click(function(){
			var $item=$(this);
			$("<div>編輯[(07)委員會核准之重大投資案]，將一併更新現有稽核資料，請問是否繼續編輯?</div>").dialog({
				height: 220,
				width: 400,
				modal: true,
				draggable: false,
				buttons: {
				  "確定": function() {
					  $(this).dialog( "close" );
					  editSeven($item);
				  },
				  "取消": function() {
					  $(this).dialog( "close" );
				  }
				},
			});				
		});
		$(".del07").click(function(){
			var seq=$(this).prop("alt");
			$("<div>刪除後將無法復原，請確認是否繼續刪除本筆資料？</div>").dialog({
				height: 200,
				width: 300,
				modal: true,
				title:"刪除",
				draggable: false,
				buttons: {
				  "確定": function() {
					  postUrlByForm('/console/cnfdi/updateaudit07.jsp',{'seq':seq,'type':'delete','investNo':"${sysinfo.INVESTMENT_NO}","tabsNum":$("#tabs").tabs( "option", "active" )});
				    $(this).dialog( "close" );
				  },
				  "取消": function() {
					  $(this).dialog( "close" );
				  }
				},
			});			
		});
	});
	function newTwo(){
		var x=$("#UpdateAudit").serialize();
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit02.jsp",{
			'type':'add','investNo':"${sysinfo.INVESTMENT_NO}","tabsNum":$("#tabs").tabs( "option", "active" )
		}, function(data){
			$( "#audit02hide").html(data);
			$( "#UpdateAudit02").append("<input type='hidden' name='other' value='"+x+"'>");
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
	function editTwo($item){
		var x=$("#UpdateAudit").serialize();
		var seq=$item.prop("alt");
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit02.jsp",{
			'type':'edit','investNo':"${sysinfo.INVESTMENT_NO}","seq":seq,"tabsNum":$("#tabs").tabs( "option", "active" )
		}, function(data){
			$( "#audit02hide").html(data);
			$( "#UpdateAudit02").append("<input type='hidden' name='other' value='"+x+"'>");
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
				<c:otherwise>
					<tr <c:if test="${fn:length(opt.auditCode)==2&&opt.auditCode!='01'}"> class="tbuborder"</c:if>>
						<td style="text-align: right;width: 30%;"><c:if test="${fn:length(opt.auditCode)==2}">(${opt.auditCode})</c:if>${opt.description}：</td>
						<td>
							<c:choose>
								<c:when test="${opt.selectName =='YN' && opt.autoDef != '1' && opt.auditCode!='06'}">
									<c:forEach var="r" items="${optmap.YN}">
										<input type="radio" name="audit${opt.auditCode}" value="${r.key}" id="YN${opt.auditCode}${r.key}" <c:if test="${audit[opt.auditCode] eq r.key|| empty audit[opt.auditCode] }">checked="checked"</c:if>><label for="YN${opt.auditCode}${r.key}">${r.value}</label>
									</c:forEach>
								</c:when>
								<c:when test="${opt.auditCode=='04'}">
									${optmap.YN[audit[opt.auditCode]]}
								</c:when>
								<c:when test="${opt.selectName =='YN' && opt.autoDef == '1'}">
									<c:choose>
										<c:when test="${audit[opt.auditCode]=='0'}">${optmap.YN[audit[opt.auditCode]]}</c:when>
										<c:when test="${empty audit[opt.auditCode]}">暫無訪查資料</c:when>
										<c:otherwise>${audit[opt.auditCode]}</c:otherwise>
									</c:choose>							
<%-- 									<c:choose> --%>
<%-- 										<c:when test="${audit[opt.auditCode]=='0'}">${optmap.YN[audit[opt.auditCode]]}</c:when> --%>
<%-- 										<c:when test="${fn:length(audit[opt.auditCode])<=3}">${optmap.YN['1']}&nbsp;（${audit[opt.auditCode]}年）</c:when> --%>
<%-- 										<c:otherwise>${optmap.YN['0']}&nbsp;（${audit[opt.auditCode]}）</c:otherwise> --%>
<%-- 									</c:choose>							 --%>
								</c:when>
								<c:when test="${fn:endsWith(opt.auditCode,'99') || opt.auditCode=='0603'}">
									<textarea name="audit${opt.auditCode}" style="width: 95%;" rows="5">${audit[opt.auditCode]}</textarea>
								</c:when>
								<c:when test="${opt.selectName =='dept'}">
									<select name="audit${opt.auditCode}" id="audit${opt.auditCode}" class="auditSelect" multiple="multiple" style="width: 90%;">
										<c:forEach var="item" items="${deptOpt}" varStatus="i">
											<c:if test="${fn:endsWith(item.key,'00')}">
												<c:if test="${i.index > 0 }">
													</optgroup>
												</c:if>
												<optgroup label="${item.value}">
											</c:if>
											<option value="${item.key}">${item.value}</option>
										</c:forEach>
										</optgroup>
									</select>
								</c:when>
								<c:when test="${opt.selectName =='city'}">
									<select name="audit${opt.auditCode}" id="audit${opt.auditCode}" class="auditSelect" multiple="multiple" style="width: 90%;">
										<c:set value="" var="tmpCity"/>
										<c:forEach var="item" items="${IOLV2}" varStatus="i">
											<c:if test="${fn:substring(item.key,0,5) != tmpCity }">
												<c:if test="${tmpCity!=''}"></optgroup></c:if>
												<optgroup label="${IOLV1[fn:substring(item.key,0,5)]}">
												<c:set value="${fn:substring(item.key,0,5)}" var="tmpCity"/>
											</c:if>
												<option value="${item.key}">${IOLV1[fn:substring(item.key,0,5)]}${item.value}</option>
										</c:forEach>
										</optgroup>
									</select>
								</c:when>
								<c:when test="${opt.auditCode=='06'}">
									將由系統判斷
								</c:when>
								
								
<%-- 								<c:when test="${opt.auditCode=='07'}"> --%>
<%-- 									<c:if test="${empty audit[opt.auditCode]}"> --%>
<%-- 										<c:forEach var="r" items="${optmap.YN}"> --%>
<%-- 											<input type="radio" name="audit${opt.auditCode}" value="${r.key}" id="YN${opt.auditCode}${r.key}" ${r.key=='0' ? 'checked="checked"':''} ><label for="YN${opt.auditCode}${r.key}">${r.value}</label> --%>
<%-- 										</c:forEach> --%>
<%-- 									</c:if> --%>
									
<%-- 									<c:if test="${not empty audit[opt.auditCode]}"> --%>
<%-- 										<c:forEach var="r" items="${optmap.YN}"> --%>
<%-- 											<input type="radio" name="audit${opt.auditCode}" value="${r.key}" id="YN${opt.auditCode}${r.key}" ${audit[opt.auditCode] == r.key ? 'checked="checked"':''} ><label for="YN${opt.auditCode}${r.key}">${r.value}</label> --%>
<%-- 										</c:forEach> --%>
<%-- 									</c:if> --%>
<%-- 								</c:when>		 --%>
													
														
								<c:otherwise><input type="text"  name="audit${opt.auditCode}" value="${audit[opt.auditCode]}"></c:otherwise>
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
								<th>編輯</th>
								<th>刪除</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${audit02}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(item.value['0201'])}</td>
									<td>${item.value['0202']}</td>
									<td>${optmap.YN[item.value['0205']]}</td>
									<td><input class="edit02 btn_class_opener" type="button" value="編輯" alt="${item.key}"></td>
									<td><input class="del02 btn_class_opener" type="button" value="刪除" alt="${item.key}"></td>
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
								<th>編輯</th>
								<th>刪除</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${audit07}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(item.value['0701'])}</td>
									<td>${item.value['0702']}</td>
									<td>${item.value['0703']}</td>
									<td><input class="edit07 btn_class_opener" type="button" value="編輯" alt="${item.key}"></td>
									<td><input class="del07 btn_class_opener" type="button" value="刪除" alt="${item.key}"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
		</c:if>
	</c:forEach>
</table>
<div style="margin: 10px;"></div>

<script type="text/javascript">

function newSeven(){
	var x=$("#UpdateAudit").serialize();
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit07.jsp",
			{
				'type' : 'add',
				'investNo' : "${sysinfo.INVESTMENT_NO}",
				"tabsNum" : $("#tabs").tabs( "option", "active" )
			}, 
			function(data){
						$( "#audit07hide").html(data);
						$( "#UpdateAudit07").append("<input type='hidden' name='other' value='"+x+"'>");
						var $d=
							$("#date07").datepicker({
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
// 		$("#date07").datepicker("disable");
		$("#audit07hide").dialog({
			height: 400,
			width: 650,
			modal: true,
			draggable: false,
			title:"稽核七 委員會核准之重大投資案",
			open:function() {
// 				$("#date07").datepicker("enable");
			},
			close: function() {
				$("#audit07hide").html("");
			}
	});
	},"html");
}


function editSeven($item){
	var x=$("#UpdateAudit").serialize();
	var seq=$item.prop("alt");
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit07.jsp",{
		'type':'edit','investNo':"${sysinfo.INVESTMENT_NO}","seq":seq,"tabsNum":$("#tabs").tabs( "option", "active" )
	}, function(data){
		$( "#audit07hide").html(data);
		$( "#UpdateAudit07").append("<input type='hidden' name='other' value='"+x+"'>");
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
</script>
