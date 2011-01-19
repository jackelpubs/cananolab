<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Samples for Study ${studyName}"/>
	<jsp:param name="topic" value="study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>

<jsp:include page="/bodyMessage.jsp?bundle=study" />

<table width="100%" align="center" class="summaryViewNoGrid"
	bgcolor="#dbdbdb">
	<tr>
		<th valign="top" align="left" height="6">
		</th>
	</tr>
	
	<c:forEach var="sampleBean" items="${studySamples}">
	<tr>
		<td>
			<table width="99%" align="center" class="summaryViewNoGrid"
				bgcolor="#F5F5f5">				
				<tr>
					<td></td><td></td>
					<td width="5%" align="right">
						<a href="sample.do?dispatch=summaryView&page=0&sampleId=${sampleBean.domain.id}">View</a>
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td colspan="2">
						${sampleBean.domain.name}
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Composition Summary
					</td>
					<td colspan="2">
						<c:if test="${sampleBean.hasComposition}">
						<c:out value="${sampleBean.compositionSummary }" escapeXml="false"	/>
						</c:if>		
					</td>
				</tr>
			</table>
	</td>
	</tr>
	<tr>
		<th valign="top" align="left" height="6">
		</th>
	</tr>
	</c:forEach>
	
	
</table>
