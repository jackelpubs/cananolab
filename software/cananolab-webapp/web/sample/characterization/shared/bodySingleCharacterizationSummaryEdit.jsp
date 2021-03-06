<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/sample/bodyHideSearchDetailView.jsp"%>
<c:set var="charObj" value="${charBean.domainChar}" />
<c:set var="charName" value="${charBean.characterizationName}" />
<c:set var="charType" value="${charBean.characterizationType}" />

<div class="editButton">
	<a
		href="characterization.do?dispatch=setupUpdate&sampleId=${sampleId}&charId=${charBean.domainChar.id}&charClassName=${charBean.className}&charType=${charBean.characterizationType}">Edit</a>
</div>
<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<tr>
		<td class="cellLabel" width="10%">
			Assay Type
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charObj.assayType}">
					<c:out value="${charObj.assayType}" />
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when
							test="${charBean.characterizationType eq 'physico chemical characterization'}">
							<c:out value="${charName}" />
						</c:when>
						<c:otherwise>N/A</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Point of Contact
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charBean.pocBean.displayName}">
					<c:out value="${charBean.pocBean.displayName}" />
				</c:when>
				<c:otherwise>
					N/A
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Date
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charBean.dateString}">
					<c:out value="${charBean.dateString}" />
				</c:when>
				<c:otherwise>
						N/A
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Protocol
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charBean.protocolBean.displayName}">
					<c:out value="${charBean.protocolBean.displayName}" />
				</c:when>
				<c:otherwise>
						N/A
				</c:otherwise>
			</c:choose>

		</td>
	</tr>
	<c:if test="${charBean.withProperties }">
		<tr>
			<td class="cellLabel" width="10%">
				Properties
			</td>
			<td>
				<%
					String detailPage = gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup
										.getInstance().getDetailPage(
												(String) pageContext.getAttribute("charType"),
												(String) pageContext.getAttribute("charName"));
								pageContext.setAttribute("detailPage", detailPage);
				%>
				<c:set var="charBean" value="${charBean}" scope="session" />
				<jsp:include page="${detailPage}">
					<jsp:param name="summary" value="true" />
				</jsp:include>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="cellLabel" width="10%">
			Design Description
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty fn:trim(charObj.designMethodsDescription)}">
					<c:out value="${charBean.descriptionDisplayName}" escapeXml="false" />
				</c:when>
				<c:otherwise>N/A
				</c:otherwise>
			</c:choose>
		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="10%">
			Techniques and Instruments
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charBean.experimentConfigs}">
					<%@ include file="bodyExperimentConfigView.jsp"%>
				</c:when>
				<c:otherwise>N/A
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Characterization Results
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charBean.findings}">
					<%@ include file="bodyFindingView.jsp"%>
				</c:when>
				<c:otherwise>N/A</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="10%">
			Analysis and Conclusion
		</td>
		<td>
			<c:choose>
				<c:when test="${!empty charBean.conclusion}">
					<c:out value="${charBean.conclusion}" />
				</c:when>
				<c:otherwise>N/A</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>