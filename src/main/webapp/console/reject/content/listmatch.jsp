<%@page import="com.isam.helper.DataUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,com.isam.ofi.reject.service.*,com.isam.ofi.reject.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	$(".goRej").click(function(){
		postUrlByForm('/console/reject/showform.jsp',{'cNo':$(this).prop("alt"),'isExists':'1'});
	});
	$(".addcname").click(function(){
		postUrlByForm('/console/reject/showform.jsp',{'cNo':""});
	});
	var y=$.extend({
		"aaSorting": [[ 2, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,2,3] },
		                 {"sSortDataType":"chinese", "aTargets": [1]}
		             ]
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#example").dataTable(y);
	if("${userInfo.company != 'cier' && userInfo.company != 'ibtech'}"==="true"){
		 oTable.fnSetColumnVis( 3, false );
	}
});
</script>
<%
	String cName = DataUtil.fmtSearchItem(request.getParameter("cname"), "%");
	String idno = DataUtil.fmtSearchItem(request.getParameter("idno"), "%");
	session.removeAttribute("rejects");
	OFIRejectCompanyService ser= new OFIRejectCompanyService();
	List<OFIRejectCompany> list=ser.getCNameList(cName, idno);
	session.setAttribute("rejects", list);
%>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6;padding-top: 10px;">
			<span style="color:#F30;">[&nbsp;Step 2&nbsp;]</span>
		   	<strong style="color:#222;">&nbsp;選擇國內事業名稱&nbsp;</strong>
		</legend>
		<div style="width: 98%;">
		<c:choose>
			<c:when test="${not empty rejects}">
				<div style="text-align: center;"><strong style="color:#222;">您可於下表點選國內事業並帶入共用資料，或是直接&nbsp;</strong><input type="button" value="新增國內事業名稱" class="btn_class_loaddata addcname"></div>
				<table id="example" class="display" style="width: 100%;"> 
					<thead>
						<tr>
							<th>共用資料</th>
							<th>國內事業名稱</th>
							<th>設立日期</th>
							<th>統一編號</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${rejects}">
							<tr>
								<td><input type="button" class="btn_class_opener goRej" value="帶入" alt="${item.cNo}"/></td>
								<td>${item.cname}</td>
								<td>${ibfn:addSlash(item.setupdate)}</td>
								<td>${item.idno}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
				<div style="text-align: center;"><strong style="color:#222;">查無符合條件名稱，請重新查詢，或&nbsp;</strong><input type="button" value="新增國內事業名稱" class="btn_class_loaddata addcname"></div>
			</c:otherwise>
		</c:choose>
		</div>
	</fieldset>