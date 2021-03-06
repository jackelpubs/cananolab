<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="editTableWithGrid" width="95%" align="center">
	<tr>
		<th colspan="3" height="5"></th>
	</tr>
	<c:forEach var="composingElement" items="${entity.composingElements}">
		<tr>
			<th class="cellLabel" colspan="2" scope="row">
				<c:out value="${composingElement.displayName}" />
			</th>
			<td align="right" width="3%">
				<a href="#submitComposingElement"
					onclick="javascript:setTheComposingElement(${composingElement.domain.id});">Edit</a>&nbsp;
			</td>
		</tr>
		<tr>
			<th scope="row">
				PubChem ID
			</th>
			<td>
				<c:choose>
					<c:when test="${!empty composingElement.pubChemLink}">
						<a href="${composingElement.pubChemLink}"
							target="caNanoLab - View PubChem"><c:out
								value="${composingElement.domain.pubChemId}" />
						</a>
						&nbsp;(<c:out
							value="${composingElement.domain.pubChemDataSourceName}" />)
				</c:when>
					<c:otherwise>N/A
				</c:otherwise>
				</c:choose>
			</td>
			<td></td>
		</tr>
		<tr>
			<th scope="row">
				Molecular Formula
			</th>
			<c:choose>
				<c:when
					test="${!empty composingElement.molecularFormulaDisplayName}">
					<td style="word-wrap: break-word; max-width: 280px;">
						<c:out value="${composingElement.molecularFormulaDisplayName}" />
					</td>
				</c:when>
				<c:otherwise>
					<td>
						N/A
					</td>
				</c:otherwise>
			</c:choose>
			<td></td>
		</tr>
		<tr>
			<th scope="row">
				Description
			</th>
			<td>
				<c:choose>
					<c:when test="${!empty composingElement.domain.description}">
						<c:out value="${composingElement.description}" escapeXml="false" />
					</c:when>
					<c:otherwise>N/A</c:otherwise>
				</c:choose>
			</td>
			<td></td>
		</tr>
		<tr>
			<th scope="row">
				Function
			</th>
			<td>
				<c:choose>
					<c:when test="${!empty composingElement.functionDisplayNames}">
						<c:forEach var="function"
							items="${composingElement.functionDisplayNames}" varStatus="ind">
							<c:out value="${function}" />
							<c:if
								test="${ind.count !=fn:length(composingElement.functionDisplayNames)}">
;&nbsp;</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>N/A
					</c:otherwise>
				</c:choose>
			</td>
			<td></td>
		</tr>
		<tr>
			<td class="cellLabel" colspan="3" height="5"></td>
		</tr>
	</c:forEach>
</table>
