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
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type='text/javascript' src='javascript/SampleManager.js'></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Sample Search Results" />
	<jsp:param name="topic" value="sample_search_results_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Back" />
	<jsp:param name="otherLink" value="searchSample.do?dispatch=setup" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=sample" />

<%--add dispatch when it's missing after coming back from sample summary page --%>
<c:set var="requestURI" value="searchSample.do" />
<c:if test="${empty param.dispatch}">
	<c:set var="requestURI" value="searchSample.do?dispatch=search" />
</c:if>

<display:table name="sessionScope.samples" id="sample" requestURI="${requestURI}"
	pagesize="25" class="displaytable" partialList="true" size="sessionScope.resultSize"
	decorator="gov.nih.nci.cananolab.dto.particle.SampleDecorator">

	<display:column title="" property="detailURL" headerScope="col" />
	<display:column title="Sample Name" property="sampleName"
		sortable="true" escapeXml="true" headerScope="col" />
	<display:column title="Primary<br>Point Of Contact"
		property="pointOfContactName" sortable="true" escapeXml="true"
		headerScope="col" />
	<display:column title="Composition" property="compositionStr"
		sortable="true" headerScope="col" />
	<display:column title="Functions" property="functionStr"
		headerScope="col" />
	<display:column title="Characterizations"
		property="characterizationStr" headerScope="col" />
	<display:column title="Data Availability" headerScope="col">
		<c:choose>
			<c:when test="${sample.dataAvailabilityMetricsScore ==  'N/A' }">${sample.dataAvailabilityMetricsScore}
			</c:when>
			<c:otherwise>
				<div id="details${sample.domain.id}" style="position: relative">
					<a id="detailLink${sample.domain.id}" href="#"
						onclick="showDetailView('${sample.domain.id}', 'sample.do?dispatch=dataAvailabilityView&sampleId=${sample.domain.id}'); return false;">
						<c:out value="${sample.dataAvailabilityMetricsScore}" />
					</a> <img src="images/ajax-loader.gif" border="0" class="counts"
						id="loaderImg${sample.domain.id}" style="display: none" alt="loader icon">
					<table id="detailView${sample.domain.id}" summary="layout"
						style="display: none; position: absolute; left: -550px; top: -150px; z-index: 10; background-color: #FFFFFF"
						border="0">
						<tr>
							<td>
								<div id="content${sample.domain.id}"></div>
							</td>
						</tr>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
	</display:column>
	<display:column title="Created Date" property="domain.createdDate"
		format="{0,date,MM-dd-yyyy}" headerScope="col" sortable="true"/>
</display:table>

<div style="padding:5px;margin:5px; background-color:#fff; border:1px solid #416599;">
	To view the PDF documents, you may need to install the Adobe PDF Reader for your browser. Please click <a target="_new" href="http://get.adobe.com/reader/">here</a> to download this free plug-in.<br/>
</div>