<!-- bodytop.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<body>
<script type="text/javascript">
  $(function(){
	  $("ul.navigation > li:has(ul) > a").append('<div class="arrow-bottom"></div>');
	  $("ul.navigation > li ul li:has(ul) > a").append('<div class="arrow-right"></div>');
	 $(".nextHref").click(function(){
		 var url =$(this).next().children("li").find("a[href]:first").prop("href");
		 window.location=url;
	 });
  });
</script>
<div id="wrapper">
<div id="mainMenu">
	<div id="leftMenu">
		<div id="menu">
			<ul class="navigation">
			<c:if test="${fn:contains(memberUrls,'R0101')||fn:contains(memberUrls,'R0102')}">
			  <li>
			  	<a class="nextHref">大陸投資核准資料</a>
			    <ul>
			      <li>
			      	<c:choose>
			      		<c:when test="${fn:contains(memberUrls,'R0101')&&fn:contains(memberUrls,'R0102')}">
					      	<a href="<c:url value='/approval/showapproval.jsp'/>">審查總表</a>
			      		</c:when>
			      		<c:when test="${fn:contains(memberUrls,'R0101')&&!fn:contains(memberUrls,'R0102')}">
					      	<a href="<c:url value='/approval/showapproval.jsp?state=01&state=02&com=2'/>">審查總表</a>
			      		</c:when>
			      		<c:when test="${!fn:contains(memberUrls,'R0101')&&fn:contains(memberUrls,'R0102')}">
					      	<a href="<c:url value='/approval/showapproval.jsp?com=1'/>">審查總表</a>
			      		</c:when>
			      	</c:choose>
			      </li>
			      <li>
			      	<a href="<c:url value='/approval/showdownload.jsp'/>">執行情形報表</a>
			      </li>
			    </ul>
			  </li>
			  </c:if>
			  <c:if test="${fn:contains(memberUrls,'R0401')}">
				  <li>
				  	<a class="nextHref">陸資管理</a>
						<ul>
							<li>
								<a class="nextHref">國內事業</a>
							    <ul>
							      <li><a href="<c:url value="/cnfdi/listapproval.jsp"/>">已核准列表</a></li>
							      <li><a href="<c:url value="/reject/showlist.jsp"/>">未核准列表</a></li>
							      <li><a href="<c:url value="/cnfdi/showofficelist.jsp"/>">辦事處列表</a></li>
							      <li><a href="<c:url value="/cnfdi/financial.jsp"/>">財報申報情形</a></li>
							      <li><a href="<c:url value="/cnfdi/getmscorexls.jsp"/>">管理密度報表</a></li>
							      <li><a href="<c:url value="/cnfdi/getalldata.jsp"/>">資料匯出</a></li>
							    </ul>
							</li>
							<li><a href="<c:url value="/cnfdi/listinvestor.jsp"/>">投資人列表</a></li>
							<li><a href="<c:url value="/cnfdi/listinvestcase.jsp"/>">投資案列表</a></li>
						</ul>
				  </li>
			  </c:if>
			  <c:if test="${fn:contains(memberUrls,'R0201')}">
				  <li>
				  	<a class="nextHref">營運狀況調查</a>
				    <ul>
				   <%--    <li><a href="<c:url value="/survey/surveyterms.jsp"/>">問卷查閱</a></li> --%>
				      <li><a href="<c:url value="/survey/filledSurveyFile.management" ><c:param name="doThing" value="front"/></c:url>">問卷下載</a></li>
				    </ul>
				  </li>
			  </c:if>
			  <c:if test="${fn:contains(memberUrls,'R0301')||fn:contains(memberUrls,'R0302')||fn:contains(memberUrls,'R0303')}">
				  <li>
				  	<a class="nextHref">訪查資料</a>
				    <ul>
					  <c:if test="${fn:contains(memberUrls,'R0301')}">
				      	<li><a href="<c:url value="/interviewcier/listInterview.jsp"/>">其他實地訪查</a></li>
					  </c:if>
					  <c:if test="${fn:contains(memberUrls,'R0302')}">
				      	<li><a href="<c:url value="/interviewtwo/listInterview.jsp"/>">大陸投資實地訪查</a></li>
					  </c:if>
					   <c:if test="${fn:contains(memberUrls,'R0303')}">
				      	<li>
					      	<a class="nextHref">陸資實地訪查</a>
						    <ul>
						      <li><a href="<c:url value="/interviewone/showiolist.jsp"/>">訪查列表</a></li>
						     <%--<li><a href="<c:url value="/interviewone/getdatareport.jsp"/>">訪查資料下載</a></li> --%> 
						        <li><a href="<c:url value="/interviewone/datadownload.jsp"/>">訪查資料下載</a></li>
						    </ul>
				      	</li>
				      </c:if>
				    </ul>
				  </li>
			  </c:if>
			      <c:if test="${not empty userInfo}">
			      	<c:if test="${fn:contains(memberUrls,'A0101')||fn:contains(memberUrls,'A0102')||fn:contains(memberUrls,'A0301')||fn:contains(memberUrls,'A0302')||fn:contains(memberUrls,'A0303')||fn:contains(memberUrls,'A0401')}">
<%-- 			      	<c:if test="${fn:contains(memberUrls,'E0101')||fn:contains(memberUrls,'E0102')||fn:contains(memberUrls,'E0301')||fn:contains(memberUrls,'E0302')}"> --%>
				      	<li>
				      		<a class="nextHref">後台管理</a>
				      		<ul>
				      			<c:if test="${fn:contains(memberUrls,'A0101')||fn:contains(memberUrls,'A0102')}">
						      	<li>
						      		<a class="nextHref">大陸投資核准資料</a>
						      		<ul>
						      			<c:if test="${fn:contains(memberUrls,'A0101')}">
									      <li><a  class="nextHref">專案審查</a>
									      		<ul>
												  <li><a href="<c:url value="/console/project/showproject.jsp"/>">專案維護</a></li>
												  <li><a href="<c:url value="/console/project/dbdifferdownload.jsp"/>">下載差異金額</a></li>
											    </ul>
									      </li>
									      </c:if>
						      			<c:if test="${fn:contains(memberUrls,'A0102')}">
									      <li><a href='<c:url value="/console/commit/showcommit.jsp"/>'>承諾事項</a>
									      </li>
									       </c:if>
			      					</ul>
			      				</li>
			      				</c:if>
						       <c:if test="${fn:contains(memberUrls,'A0301')||fn:contains(memberUrls,'A0302')||fn:contains(memberUrls,'A0303')}">
							      <li><a class="nextHref">訪查資料</a>
							      	<ul>
								       <c:if test="${fn:contains(memberUrls,'A0301')}">
									      <li><a href="<c:url value="/console/interviewcier/listInterview.jsp"/>">其他實地訪查</a></li>
								       </c:if>
								       <c:if test="${fn:contains(memberUrls,'A0302')}">
									      <li><a href="<c:url value="/console/interviewtwo/listInterview.jsp"/>">大陸投資實地訪查</a></li>
								       </c:if>
								       <c:if test="${fn:contains(memberUrls,'A0303')}">
									      <li>
										      <a class="nextHref">陸資實地訪查</a>
											    <ul>
											      <li><a href="<c:url value="/console/interviewone/showiolist.jsp"/>">訪查列表</a></li>
											            <li><a href="<c:url value="/console/interviewone/getdatareport.jsp"/>">訪查資料匯出</a></li>
											     
											      <%-- <li><a href="<c:url value="/console/interviewone/getdatareport.jsp"/>">資料匯出</a></li>--%>
											    </ul>
									      </li>
								    	</c:if>
								    </ul>
							      </li>
						        </c:if>
						        <c:if test="${fn:contains(memberUrls,'A0401')}">
							      <li><a  class="nextHref">陸資管理</a>
							      		<ul>
										  <li><a href="<c:url value="/console/listnotfilled.jsp"/>">待確認列表</a></li>
										  <li><a href="<c:url value="/console/cnfdi/listapproval.jsp?fbtype=b"/>">管理國內事業</a></li>
										  <li><a href="<c:url value="/console/cnfdi/listinvestor.jsp?fbtype=b"/>">管理投資人</a></li>
										  <li><a href="<c:url value="/console/reject/showlist.jsp"/>">管理未核准案件</a></li>
										  <li><a href="<c:url value="/console/cnfdi/importoffice.jsp"/>">匯入陸資辦事處資料</a></li>
										  <li><a href="<c:url value="/console/cnfdi/twsic.jsp"/>">管理營業項目</a></li>
									    </ul>
							      </li>
						     	</c:if>
						        <c:if test="${fn:contains(memberUrls,'A0201')||fn:contains(memberUrls,'E0201')}">
							      <li><a  class="nextHref">問卷管理</a>
							      		<ul>
										  <li><a href="<c:url value="/console/survey/surveyFile.view?"/>">上傳空白問卷</a></li>
										  	  <li><a href="<c:url value="/console/survey/filledSurveyFile.management?"/>">管理問卷資料</a></li>
									    </ul>
							      </li>
						     	</c:if>
						    </ul>
				      	</li>
			      </c:if>
			       </c:if>
			    </ul>
		</div>
	</div>
	<div id="rightMenu" style="text-align: right;">
		<div><img style="margin-right: 10px;margin-bottom: 5px;margin-top: -10px;"  alt="" src='<c:url value="/images/WebsiteLogo.png"/>'></div>
		<div style="float: right;margin-top: -15px;margin-right:18px;z-index: 200;">
		<c:if test="${userInfo.groupId eq 'super'}">
			<a href="<c:url value="/userlist.jsp"/>" class="btn_class_opener">使用者管理</a>
		</c:if>
			<a href="<c:url value="/useredit.jsp"/>" class="btn_class_opener">修改密碼</a>
			<a href="<c:url value="/logout.jsp"/>" class="btn_class_opener">登出</a>
		</div>
		<div style="clear: both;"></div>
	</div>
	<div  class="ui-widget-header" style="height: 100px;background-color: #6699CC;"></div>
</div>
<jsp:include page="/includes/navigation.jsp" flush="true" />