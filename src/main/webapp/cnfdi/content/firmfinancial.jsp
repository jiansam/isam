<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	$( "#faccordion" ).accordion({heightStyle: "content"});
});
</script>
<script>
$(function() {
	var opt=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2,3,4] }],
        "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
		   "iDisplayLength": 10
		   ,"fnDrawCallback": function ( oSettings ) {
				/* Need to redo the counters if filtered or sorted */
				if ( oSettings.bSorted || oSettings.bFiltered ){
					for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
						$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
					}
				}
			}
		},sdInitDataTableSetting(),sdSortChinese(),sdNumberFmt());
	var roTable=$(".refinancial").dataTable(opt);
	$(".RfShow").click(function(){
		var reInvestNo=$(this).prop("alt");
		var active=$("#faccordion").accordion("option", "active");
		var cname=$("#faccordion h3").eq(active).text();
		var reInvestNo=$(this).prop("alt");
		var reportyear =$(this).parents("tr").find("td :eq(1)").text();
		var reportdate =$(this).parents("tr").find("td :eq(2)").text();
		var serno =$(this).parents("tr").find("td :eq(3) span").text();
		var re_setupdate=$(this).parents(".dtParent").find(".re_setupdate").text();
		var re_isNew=$(this).parents(".dtParent").find(".re_isNew").text();
		var x="";
		if(re_isNew==="2"){
			x="新設";
		}
		if(re_setupdate.length>0){
			x+=re_setupdate;
		}
		if(x.length>0){
			x="("+x+"成立)";
		}
		var tt=cname+reportyear+'年度財務資訊簡表'+x;
			$.post( "${pageContext.request.contextPath}/cnfdi/content/newrxfreport.jsp",{
					'type':'show','investNo':"${sysinfo.INVESTMENT_NO}",'serno':serno,'reInvestNo':reInvestNo,'tt':tt
				}, function(data){
				$( "#Rtmp" ).html(data); 
				$( "#Rtmp" ).dialog({
				      height:600,
				      width:980,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'檢視財報_'+cname,
				      close: function( event, ui ) {
				    	  $( "#Rtmp" ).html(""); 
				      }
			});
			},"html");
	});
	$(".RfPdf").click(function(){
		var reInvestNo=$(this).prop("alt");
		var active=$("#faccordion").accordion("option", "active");
		var cname=$("#faccordion h3").eq(active).text();
		var re_setupdate=$(this).parents(".dtParent").find(".re_setupdate").text();
		var re_isNew=$(this).parents(".dtParent").find(".re_isNew").text();
		var x="";
		if(re_isNew==="2"){
			x="新設";
		}
		if(re_setupdate.length>0){
			x+=re_setupdate;
		}
		if(x.length>0){
			x="("+x+"成立)";
		}
		var repYear =$(this).parents("tr").find("td :eq(1)").text();
		var tt=cname+repYear+'年度財務資訊簡表'+x;
		var serno =$(this).next("span").text();
		postUrlByForm('/cnfdi/donwloadrxfs.jsp',{'serno':serno,'investNo':"${sysinfo.INVESTMENT_NO}",'tt':tt,'reInvestNo':reInvestNo});
	});
});
</script>
<div id="Rtmp"></div>
<div id="faccordion" style="font-size: medium;">
	<h3>${sysinfo.COMP_CHTNAME}</h3>
	<div>
		<jsp:include page="/cnfdi/content/firmfinancialmain.jsp" flush="true" />
	</div>	
	<c:forEach var="re" items="${reInvests}">
		<h3>(轉投資)${re.reinvestment}</h3>
		<div>
			<div class='tbtitle' style="margin-bottom: 5px;">財報申報情形</div>
			<div style="height: 5px;"></div>
			<div style="width: 98%;padding-left: 15px;" class="dtParent">
				<span style="display: none;" class="re_setupdate">${ibfn:addSlash(re.setupdate)}</span>
				<span style="display: none;" class="re_isNew">${re.isNew}</span>
				<table style="width: 100%;font-size: 16px;" class="refinancial">
					<thead>
						<tr>
							<th style="width: 15%;">序號</th>
							<th style="width: 20%;">財報年度</th>
							<th style="width: 20%;">填報日期</th>
							<th>檢視</th>
							<th>下載</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="f" items="${refinancial[re.reInvestNo]}">
							<tr>
								<td></td>
								<td>${f.reportyear}</td>
								<td>${ibfn:addSlash(f.reportdate)}</td>
								<td>
									<input type="button" class="RfShow btn_class_opener" value="檢視" alt="${re.reInvestNo}"><span style="display: none;">${f.serno}</span>
								</td>
								<td>
									<input type="button" class="RfPdf btn_class_opener" value="列印" alt="${re.reInvestNo}"><span style="display: none;">${f.serno}</span>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>	
		</div>	
	</c:forEach>
</div>
<div style="margin: 10px;"></div>