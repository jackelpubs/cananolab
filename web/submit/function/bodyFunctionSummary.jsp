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

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Function Summary
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Activation Method</strong>
		</td>
		<td class="label">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:select property="function.activationMethod">
						<option />
							<c:forEach var="method" items="${allActivationMethods}">
								<html:option value="${method}" />
							</c:forEach>
					</html:select>
				</c:when>
				<c:otherwise>
						${nanoparticleFunctionForm.map.function.activationMethod}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
		<td class="label">
			<strong>View Title* </strong>
			<br>
			(text will be truncated after 20 characters)
		</td>
		<td class="rightLabel">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:text property="function.viewTitle" />
				</c:when>
				<c:otherwise>
						${nanoparticleFunctionForm.map.function.viewTitle}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:textarea property="function.description" rows="3" cols="60"/>
				</c:when>
				<c:otherwise>
						${nanoparticleFunctionForm.map.function.description}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>
