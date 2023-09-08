<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>



<div style="height: 5px;"></div>
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	統一編號：${ofiiooffice.banNo }
</div>
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	公司狀況：${ofiiooffice.status }
</div>
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	公司名稱：${ofiiooffice.compname }
</div>
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	在中華民國境內代表人：${ofiiooffice.agent }
</div>
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	辦事處所在地：${ofiiooffice.location }
</div>
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	核准登記日期：${ofiiooffice.setupdate.substring(0,3)}年${ofiiooffice.setupdate.substring(3,5)}月${ofiiooffice.setupdate.substring(5,7)}日
</div>
<c:if test="${ofiiooffice.sdate != null && ofiiooffice.sdate.trim().length()==7 && ofiiooffice.sdate!= '0000000'}">

<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	停業日期(起)：
		 ${ofiiooffice.sdate.substring(0,3)}年${ofiiooffice.sdate.substring(3,5)}月${ofiiooffice.sdate.substring(5,7)}日
									
</div>
</c:if>
<c:if test="${ofiiooffice.edate != null && ofiiooffice.edate.trim().length()==7&& ofiiooffice.edate!= '0000000'}">
<div style="width: 98%;margin-left: 15px; line-height:2rem;">
	停業日期(迄)：
	 ${ofiiooffice.edate.substring(0,3)}年${ofiiooffice.edate.substring(3,5)}月${ofiiooffice.edate.substring(5,7)}日
								
</div>
</c:if>