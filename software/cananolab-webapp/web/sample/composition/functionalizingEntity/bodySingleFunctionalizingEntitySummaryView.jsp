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
<table class="summaryViewNoGrid" width="99%" align="center"
	bgcolor="#F5F5f5">
	<c:if test="${!empty functionalizingEntity.name}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				Name
			</th>
			<td>
				<c:out value="${functionalizingEntity.name}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty functionalizingEntity.pubChemId}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				PubChem ID
			</th>
			<td>
				<a href="${functionalizingEntity.pubChemLink}"
					target="caNanoLab - View PubChem"><c:out value="${pubChemId}" />
				</a> &nbsp;(
				<c:out value="${pubChemDS}" />
				)
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty functionalizingEntity.value}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				Amount
			</th>
			<td>
				<c:out value="${functionalizingEntity.value}" />
				<c:out value="${functionalizingEntity.valueUnit}" />
			</td>
		</tr>
	</c:if>
	<c:if
		test="${!empty functionalizingEntity.molecularFormulaDisplayName}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				Molecular Formula
			</th>
			<td style="word-wrap: break-word; max-width: 280px;">
				<c:out value="${functionalizingEntity.molecularFormulaDisplayName}" />
			</td>
		</tr>
	</c:if>
	<c:choose>
		<c:when test="${!empty entityDetailPage}">
			<tr>
				<th scope="row" class="cellLabel" width="10%">
					Properties
				</th>
				<td>
					<c:set var="functionalizingEntity" value="${functionalizingEntity}"
						scope="session" />
					<jsp:include page="${entityDetailPage}">
						<jsp:param name="summary" value="true" />
					</jsp:include>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:if test="${functionalizingEntity.withProperties }">
				<tr>
					<th scope="row" class="cellLabel" width="10%">
						Properties
					</th>
					<td>
						<%
							String detailPage = gov.nih.nci.cananolab.restful.sample.InitCompositionSetup
																.getInstance().getDetailPage(
																		(String) pageContext
																				.getAttribute("entityType"),
																		"functionalizingEntity");
														pageContext.setAttribute("detailPage", detailPage);
						%>
						<c:set var="functionalizingEntity"
							value="${functionalizingEntity}" scope="session" />
						<jsp:include page="${detailPage}">
							<jsp:param name="summary" value="true" />
						</jsp:include>
					</td>
				</tr>
			</c:if>
		</c:otherwise>
	</c:choose>
	<c:if test="${!empty functionalizingEntity.functions}">
		<tr>
			<th scope="row" class="cellLabel" width="10%" valign="top">
				Function(s)
			</th>
			<td>
				<c:set var="entity" value="${functionalizingEntity }" />
				<%@ include file="bodyFunctionView.jsp"%>
			</td>
		</tr>
	</c:if>
	<c:if
		test="${!empty functionalizingEntity.activationMethodDisplayName}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				Activation Method
			</th>
			<td>
				<c:out value="${functionalizingEntity.activationMethodDisplayName}" />
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty fn:trim(functionalizingEntity.description)}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				Description
			</th>
			<td>
				<c:out value="${functionalizingEntity.descriptionDisplayName}"
					escapeXml="false" />
			</td>
		</tr>
	</c:if>
	<c:if test="${! empty functionalizingEntity.files}">
		<tr>
			<th scope="row" class="cellLabel" width="10%">
				Files
			</th>
			<td>
				<c:set var="files" value="${functionalizingEntity.files }" />
				<c:set var="entityType" value="functionalizing entity" />
				<c:set var="downloadAction" value="composition" />
				<%@include file="../../bodyFileView.jsp"%>
			</td>
		</tr>
	</c:if>
</table>