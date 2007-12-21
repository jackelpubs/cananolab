<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table class="${param.tableStyle}">
	<tr class="titleRow">
		
			<c:choose>
			<c:when test="${!empty charaLeafToCharacterizations[param.charType]}">
				<td class="${param.charTypeLabelStyle}">
				<c:url var="url" value="${param.addAction}.do">
					<c:param name="particleId" value="${particleId}" />
					<c:param name="submitType" value="${param.charType}" />
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="summaryView" />
				</c:url>
				<a href="${url}" class="${param.charTypeStyle}">${param.charType}</a>
				</td>
			</c:when>
			<c:otherwise>
				<td class="${param.noDataLabelStyle}">
				<c:out value="${param.charType}"/>
				</td>
			</c:otherwise>
			</c:choose>

		<td>
			&nbsp;
		</td>
		<c:choose>
			<c:when test="${canCreateNanoparticle eq 'true'}">
				<c:url var="submitUrl"
					value="${param.addAction}.do">
					<c:param name="particleId" value="${particleId}" />
					<c:param name="submitType" value="${param.charType}" />
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="setup" />
				</c:url>
				<td class="${param.addLinkStyle }">
					<a href="${submitUrl}" class="addlink"><img
							src="images/btn_add.gif" border="0" /></a>
				</td>
			</c:when>
			<c:otherwise>
				<td>
					&nbsp;
				</td>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when
				test="${canUserDeleteChars eq 'true' &&
												!empty charaLeafToCharacterizations[param.charType]}">
				<c:url var="deleteUrl" value="deleteAction.do">
					<c:param name="particleId" value="${particleId}" />
					<c:param name="submitType" value="${param.charType}" />
					<c:param name="page" value="0" />
					<c:param name="dispatch" value="setup" />
				</c:url>
				<td>
					&nbsp;
				</td>
				<td class="${param.addLinkStyle}">
					<a href="${deleteUrl}" class="addlink"><img
							src="images/btn_delete.gif" border="0" /></a>
				</td>
			</c:when>
			<c:otherwise>
				<td>
					&nbsp;
				</td>
			</c:otherwise>
		</c:choose>
		<c:if test="${param.tableStyle == 'subTitleTable'}">
			<td width="100%">
				&nbsp;
			</td>
		</c:if>
	</tr>
</table>
