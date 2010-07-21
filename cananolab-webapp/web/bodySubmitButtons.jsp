<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" class="invisibleTable">
	<tr>
		<td align="left" width="600">
			<c:if test="${!empty user && !empty updateId && user.admin}">
				<input type="button" value="${deleteButtonName}"
					onclick="${deleteOnclick}" id="deleteButton">
			</c:if>
			<c:if test="${!empty updateId && !empty cloneOnclick }">
				<input type="button" value="Copy" onclick="${cloneOnclick}" id="copyButton">
			</c:if>
		</td>
		<td align="right" width="300">
			<c:set var="submitButtonName" value="Submit" />
			<c:if test="${!empty updateId}">
				<c:set var="submitButtonName" value="Update" />
			</c:if>
			<input type="reset" value="Reset" onclick="${resetOnclick}" id="resetButton"/>
			&nbsp;
			<c:if test="${!empty review && review eq 'true'}">
				<input type="button" value="Submit for Review" id="reviewButton"/>
			</c:if>
			<c:if test="${!empty assignPublic && assignPublic eq 'true'}">
				<input type="button" value="Submit to Public" id="publicButton"/>
			</c:if>
			<c:choose>
				<c:when test="${!empty submitOnclick }">
					<input type="button" value="${submitButtonName}"
						onclick="${submitOnclick}" styleId="submitButton">
				</c:when>
				<c:otherwise>
					<html:submit value="${submitButtonName}" styleId="submitButton" disabled="false"/>
					<input type="hidden" name="dispatch" value="${hiddenDispatch}">
					<input type="hidden" name="page" value="${hiddenPage}">
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
