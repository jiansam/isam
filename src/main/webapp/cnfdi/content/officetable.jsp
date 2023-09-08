<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
var contextPath = "${pageContext.request.contextPath}";
$(function() {
	$(".iTooltip").tooltip();
	var y=$.extend({
		"aaSorting" : [[ 4, "desc" ]],
		"aoColumnDefs" : [
							{ "sClass":"center" , "aTargets":[0] },
		                 	{ "sSortDataType":"chinese" , "aTargets":[2,3]}],
		"aoColumns" : [
                          { "sWidth": "8%" }, 
                          { "sWidth": "10%" }, 
                          { "sWidth": "32%" }, 
                          { "sWidth": "15%" },
                          { "sWidth": "15%" }
                        
                      ],
		"aLengthMenu" : [[10, 20, 50,100], [10, 20, 50,100]],
		"iDisplayLength" : 10,
		"fnDrawCallback": function ( oSettings ) {
								/* Need to redo the counters if filtered or sorted */
								if ( oSettings.bSorted || oSettings.bFiltered ){
									for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
										$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
									}
								}
				  		  }
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#example").dataTable(y);
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var local = nTds.index($(this));
		var investorseq = $("td[data-investorseq]",ntr).data('investorseq');
		console.log('local',local);
		if(local == 5){
			if($(this).text().trim() == "無"){
				return;
			}
// 			ajaxByDataToDialog($(this))
			opendialog($(this))
		}else{
		//	postUrlByForm('/showinvestor.jsp',{'investorSeq':investorseq});
	 		postUrlByForm('/showinvestoffice.jsp',{'banno':$('td:eq(1)',ntr).text()});
		}
	});
});
</script>

<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件列表&nbsp;</strong>&nbsp;
		</legend>
		<div>
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>序號</th>
					<th>統一編號</th>
					<th>公司名稱</th>
					<th>公司狀況</th>
					<th>核准登記日期</th>

				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${offices}">
					<tr>
					<td></td>
					<td>${item.Ban_No}</td>
					<td>${item.COMP_CHTNAME}</td>
					<td style="text-align:center;">
						
						<c:choose>
						    <c:when test="${item.Status.indexOf('廢止')>=0}">
						        解散或撤銷
						    </c:when>
						    <c:when test="${item.Status.indexOf('撤回')>=0}">
						        解散或撤銷
						    </c:when>
						    <c:when test="${item.Status.indexOf('清算完結')>=0}">
						        解散或撤銷
						    </c:when>
						    <c:when test="${item.Status.indexOf('解散')>=0}">
						        解散或撤銷
						    </c:when>
						    <c:when test="${item.Status.indexOf('撤銷')>=0}">
						        解散或撤銷
						    </c:when>
						    <c:when test="${item.Status.indexOf('註銷')>=0}">
						        解散或撤銷
						    </c:when>
						    				
						     <c:when test="${item.sdate != null && item.sdate.indexOf('0000000')<0 && item.sdate.trim().length()>0}">
						        停業中
						    
						    </c:when>
						    <c:otherwise>
						     	${item.Status}
						    </c:otherwise>
						</c:choose>
					
					</td>
					
					<td style="text-align:center;"> ${item.setupdate.substring(0,3)}年${item.setupdate.substring(3,5)}月${item.setupdate.substring(5,7)}日</td>
					
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
	<div id="listDialog"></div>
</div>
