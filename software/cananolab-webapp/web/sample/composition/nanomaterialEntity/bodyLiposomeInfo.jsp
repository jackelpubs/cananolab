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
				<th scope="row" class="cellLabel">
					Is Polymerized
				</th>
				<th scope="row" class="cellLabel">
					Polymer Name
				</th>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.liposome.polymerized}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.liposome.polymerName}"/>
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Liposome Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					<label for="liposomeIsPolymerized">Is Polymerized</label>
				</td>
				<td class="cellLabel">
					<c:choose>
						<c:when
							test="${empty compositionForm.map.nanomaterialEntity.isPolymerized}">
							<c:set var="selectNoneStr" value='selected="selected"' />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${compositionForm.map.nanomaterialEntity.isPolymerized eq 'true'}">
									<c:set var="selectYesStr" value='selected="selected"' />
								</c:when>
								<c:otherwise>
									<c:set var="selectNoStr" value='selected="selected"' />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<select name="nanomaterialEntity.isPolymerized" id="liposomeIsPolymerized">
						<option value="" ${selectNoneStr}></option>
						<option value="true" ${selectYesStr}>
							Yes
						</option>
						<option value="false" ${selectNoStr}>
							No
						</option>
					</select>
				</td>
				<td class="cellLabel">
					<label for="liposomePolymerName">Polymer Name</label>
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.liposome.polymerName" id="liposomePolymerName"
						value="${compositionForm.map.nanomaterialEntity.liposome.polymerName}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>