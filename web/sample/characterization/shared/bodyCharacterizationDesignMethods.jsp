<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			Design and Methods Description
		</td>
		<td>
			<html:textarea property="achar.description" cols="120" rows="5" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="20%">
			Technique and Instrument
		</td>
		<td>
			<a style="display:block" id="addExperimentConfig"
				href="javascript:clearExperimentConfig();openSubmissionForm('ExperimentConfig');"><img
					align="top" src="images/btn_add.gif" border="0" /></a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<c:if
				test="${! empty characterizationForm.map.achar.experimentConfigs }">
				<c:set var="charBean" value="${characterizationForm.map.achar}" />
				<c:set var="edit" value="true" />
				<%@ include file="bodyExperimentConfigView.jsp"%>
			</c:if>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div id="newExperimentConfig" style="display: none;">
				<jsp:include page="bodySubmitExperimentConfig.jsp" />
			</div>
			&nbsp;
		</td>
	</tr>
</table>
<br>