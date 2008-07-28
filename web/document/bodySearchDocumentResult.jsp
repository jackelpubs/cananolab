<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				Nanoparticle Document Search Results
			</h3>
		</td>
		<td align="right" width="25%">
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="search_documents_results_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>				
			<a href="searchDocument.do?dispatch=setup" class="helpText">Back</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=document" />
			<c:choose>
				<c:when test="${canCreateDocument eq 'true'}">
					<c:set var="link" value="editDocumentURL" />
				</c:when>
				<c:otherwise>
					<c:set var="link" value="domainFile.title" />
				</c:otherwise>
			</c:choose>
			<display:table name="sessionScope.documents" id="document"
				requestURI="searchDocument.do" pagesize="25" class="displaytable"
				decorator="gov.nih.nci.cananolab.dto.common.DocumentDecorator">
				<display:column title="Category" property="publicationOrReport"
					sortable="true" />
				<display:column title="Title" property="${link}"
					sortable="true" />
				<display:column title="Type"
					property="domainFile.category" sortable="true" />
				<display:column title="Document Link"
					property="downloadURL" sortable="true" />
				<display:column title="Keywords"
					property="keywordsStr" sortable="true" />
				<display:column title="Document Description"
					property="domainFile.description" sortable="true" />
				<display:column title="Associated Particle<br>Sample Names"
					property="particleNames" sortable="true" />
				<display:column title="Created Date"
					property="domainFile.createdDate" sortable="true"
					format="{0,date,MM-dd-yyyy}" />
				<display:column title="Location" property="location" sortable="true" />
			</display:table>
		</td>
	</tr>
</table>

