<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="entityAddUrl" value="nanomaterialEntity.do">
	<c:param name="page" value="0" />
	<c:param name="dispatch" value="setup" />
	<c:param name="location" value="local" />
	<c:param name="sampleId" value="${sampleId}" />
	<c:param name="submitType" value="Nanomaterial Entity" />
</c:url>
<table id="summarySection2" style="display: block;" class="smalltable3" cellpadding="0" cellspacing="0" border="0"
	width="100%">
	<tr>
		<th colspan="4" align="left">
			Functionalizing Entity &nbsp;&nbsp;&nbsp;
			<a href="${entityAddUrl}" class="addlink"><img align="absmiddle"
					src="images/btn_add.gif" border="0" /></a> &nbsp;&nbsp;&nbsp;
			<c:if test="${!empty compositionForm.map.comp.nanomaterialEntities}">
				<a href="${entityAddUrl}" class="addlink"><img align="absmiddle"
						src="images/btn_delete.gif" border="0" /></a>
			</c:if>
		</th>
	</tr>
	<tr>
		<td colspan="4" align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>


	<tr>
		<td>
			<div class="indented4">
				<table class="summarytable" border="0" width="90%">
					<tr>
						<td>
							&nbsp;No data available&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				&nbsp;
			</div>
			&nbsp;
		</td>
	</tr>
	<tr><td><br></td></tr>
</table>
<div id="summarySeparator2"><br></div>



