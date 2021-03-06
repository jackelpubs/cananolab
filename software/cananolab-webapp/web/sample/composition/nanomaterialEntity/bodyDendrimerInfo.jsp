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
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewNoGrid" align="left">
			<tr>
				<th scope="col" class="cellLabel">
					Branch
				</th>
				<th scope="col" class="cellLabel">
					Generation
				</th>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.dendrimer.branch}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.dendrimer.generation}"/>
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Dendrimer Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					<label for="dendrimerBranch">Branch</label>
				</td>
				<td class="cellLabel">
					<input id="dendrimerBranch" type="text" name="nanomaterialEntity.dendrimer.branch"
						value="${compositionForm.map.nanomaterialEntity.dendrimer.branch}" />
				</td>
				<td class="cellLabel">
					<label for="dendrimerGeneration">Generation</label>
				</td>
				<td class="cellLabel">
					<input id="dendrimerGeneration" type="text" name="nanomaterialEntity.dendrimer.generation"
						value="${compositionForm.map.nanomaterialEntity.dendrimer.generation}" />
				</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
<br>