<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatableOld.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script>
$(function() {
    $("#tabs").tabs();
    setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
});

</script>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				
				<div style="height:30px;">
					
					<span  style="float: right ;">
						<a href="<c:url value='/cnfdi/showofficelist.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</div>
				<div id="tabs" style="font-size: 16px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">詳細資料</a></li>
					  
					  </ul>
					  <div id="tabs-1">
							<table style="width: 100%;">
								<tr>
									<td style="text-align: left;width: 10%;">統一編號：${ofiiobean.banNo}</td>
								</tr>
								<tr>
									<td style="text-align: left;width: 10%;">公司狀況：${ofiiobean.status}</td>
								</tr>
						
								<tr>
									<td style="text-align: left;width: 10%;">公司名稱：${ofiiobean.compname}</td>
								</tr>
								<tr>
									<td style="text-align: left;width: 10%;">在中華民國境內代表人：${ofiiobean.agent}</td>
								</tr>
								<tr>
									<td style="text-align: left;width: 10%;">辦事處所在地：${ofiiobean.location}</td>
								</tr>
								<tr>
							
								
									<td style="text-align: left;width: 10%;">	核准登記日期： ${ofiiobean.setupdate.substring(0,3)}年${ofiiobean.setupdate.substring(3,5)}月${ofiiobean.setupdate.substring(5,7)}日</td>
								</tr>
								<c:if test="${ofiiobean.sdate != null  && ofiiobean.sdate.indexOf('0000000')<0  && ofiiobean.sdate.trim().length()>0}">
								<tr>
								
									<td style="text-align: left;width: 10%;">停業日期(起)： 
									 ${ofiiobean.sdate.substring(0,3)}年${ofiiobean.sdate.substring(3,5)}月${ofiiobean.sdate.substring(5,7)}日
									</td>
								</tr>
								</c:if>
								<c:if test="${ofiiobean.edate != null  && ofiiobean.edate.indexOf('0000000')<0  &&  ofiiobean.edate.trim().length()> 0 }">
								<tr>
									<td style="text-align: left;width: 10%;">停業日期(迄)：
									 ${ofiiobean.edate.substring(0,3)}年${ofiiobean.edate.substring(3,5)}月${ofiiobean.edate.substring(5,7)}日
									</td>
								</tr>
								</c:if>
							</table>
					  </div>
					
				</div>
			</fieldset>
		</div>