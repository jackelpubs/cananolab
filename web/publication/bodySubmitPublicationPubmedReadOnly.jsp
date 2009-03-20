<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/PublicationManager.js"></script>

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>

<html:form action="/publication" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					${action} Publication
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<c:choose>
			<c:when
				test="${empty allParticleNames && param.dispatch eq 'setup'}">
				<tr>
					<td colspan="2">
						<font color="blue" size="-1"><b>MESSAGE: </b>There are no
							samples in the database. Please make sure to <html:link
								page="/sample.do?dispatch=setupNew&page=0&location=${location}"
								scope="page">create
							a new sample</html:link> first. </font>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:if test="${!empty publicationForm.map.file.domainFile.id}">
					<html:hidden property="file.domainFile.id" />
				</c:if>
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=publication" />
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="8">
										<div align="justify">
											Publication Information
										</div>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Publication Type*</strong>
									</td>
									<td class="label" colspan="3">
										<html:select property="file.domainFile.category"
											onchange="javascript:callPrompt('Publication Category', 'file.domainFile.category');
													setupReport(publicationForm, '${param.sampleId}');"
											styleId="file.domainFile.category">
											<option value=""></option>
											<html:options name="publicationCategories" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
									<td class="label">
										<strong>Publication Status*</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:select property="file.domainFile.status"
											onchange="javascript:callPrompt('Publication status', 'file.domainFile.status');"
											styleId="file.domainFile.status">
											<option value=""></option>
											<html:options name="publicationStatuses" />
											<option value="other">
												[Other]
											</option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Research Category</strong>
										<br>
									</td>
									<td class="rightLabel" colspan="7">
										<c:forEach var="data" items="${publicationResearchAreas}">
											<html:multibox property="file.researchAreas">
												${data}
											</html:multibox>${data}
										</c:forEach>
										&nbsp;
									</td>
								</tr>
								<tr id="pubMedRow">
									<td class="leftLabel" valign="top">
										<strong>PubMed ID</strong>
									</td>
									<td class="rightLabel" colspan="7">
										<a
											href="http://www.ncbi.nlm.nih.gov/pubmed/${publicationForm.map.file.domainFile.pubMedId}"
											target="_pubmed"> Click to look up PubMed Identifier</a>
										<br>
										<html:text property="file.domainFile.pubMedId" size="30"
											styleId="pubmedId"
											onchange="javascript:addPubmed(publicationForm, '${param.sampleId}'); return false;" />
										<br>
										<i>After entering a valid PubMed ID and clicking outside
											of the text field, <br>the related fields (DOI, title,
											journal, author, etc) are auto-populated by PubMed and become
											read-only.</i>
									</td>
								</tr>
								<tr id="doiRow">
									<td class="leftLabel" valign="top">
										<strong>Digital Object ID</strong>
									</td>
									<td class="rightLabel" colspan="7">
										${publicationForm.map.file.domainFile.digitalObjectId
										}&nbsp;
										<%--										<html:text property="file.domainFile.digitalObjectId"--%>
										<%--											size="30" />--%>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Title* </strong>
									</td>
									<td class="rightLabel" colspan="7">
										${publicationForm.map.file.domainFile.title }&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Journal </strong>
									</td>
									<td class="rightLabel" colspan="7">
										${publicationForm.map.file.domainFile.journalName}&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Authors</strong>
										<br>
									</td>
									<td class="label" colspan="6" valign="top">
										<table class="smalltable" border="0">
											<tr class="smallTableHeader">
												<th>
													First Name
												</th>
												<th>
													Last Name
												</th>
												<th>
													Initials
												</th>
											<tr>
												<logic:iterate name="publicationForm"
													property="file.authors" id="author" indexId="authorInd">
													<tr>
														<td>
															${publicationForm.map.file.authors[authorInd].firstName}&nbsp;
														</td>
														<td>
															${publicationForm.map.file.authors[authorInd].lastName}&nbsp;
														</td>
														<td>
															${publicationForm.map.file.authors[authorInd].initial}&nbsp;
														</td>
													</tr>
												</logic:iterate>
											</tr>
										</table>
										&nbsp;
									</td>
									<td class="rightLabel" valign="top">
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Year of Publication</strong>
									</td>
									<td class="label">
										${publicationForm.map.file.domainFile.year}&nbsp;
									</td>
									<td class="label" align="right"
										style="padding-left: 3em; padding-right: 0">
										<strong id="volumeTitle">Volume</strong>&nbsp;
									</td>
									<td class="label">
										${publicationForm.map.file.domainFile.volume}&nbsp;
									</td>
									<td class="label" align="right" valign="middle"
										style="padding-left: 5em; padding-right: 0;">
										<strong id="spageTitle">Start Page</strong>&nbsp;
									</td>
									<td class="label">
										${publicationForm.map.file.domainFile.startPage}&nbsp;
									</td>
									<td class="label" align="right">
										<strong id="epageTitle">End Page</strong>&nbsp;
									</td>
									<td class="rightLabel">
										${publicationForm.map.file.domainFile.endPage}&nbsp;
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Keywords<br>
										</strong>
										<i>(one keyword per line)</i>

									</td>
									<td class="rightLabel" colspan="7">
										<html:textarea property="file.keywordsStr" rows="3" cols="70" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Description</strong>
									</td>
									<td class="rightLabel" colspan="7">
										<html:textarea property="file.domainFile.description" rows="3"
											cols="70" />
									</td>
								</tr>
							</tbody>
						</table>

						<br>


						<c:choose>
							<c:when test="${empty param.sampleId}">
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">
									<tbody>
										<tr class="topBorder">
											<td class="formTitle" colspan="4">
												<div align="justify">
													&nbsp;
												</div>
											</td>
										</tr>
										<tr>
											<td class="leftLabel" valign="top" width="20%">
												<strong>Sample Name</strong>
											</td>
											<td class="rightLabel">
												<html:select property="file.sampleNames" multiple="true"
													size="5">
													<html:options name="allParticleNames" />
												</html:select>
											</td>
										</tr>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">
									<tbody>
										<tr class="topBorder">
											<td class="formTitle" colspan="4">
												<div align="justify">
													Copy
												</div>
											</td>
										</tr>
										<c:choose>
											<c:when test="${!empty otherParticleNames}">
												<tr>
													<input type="hidden" name="file.sampleNames"
														value="${sampleName}">
													<td class="leftLabel" valign="top" width="20%">
														<strong>Copy to other ${samplePointOfContact}
															nanoparticle</strong>
													</td>
													<td class="rightLabel">
														<html:select property="file.sampleNames" multiple="true"
															size="5">
															<html:options collection="otherParticleNames"
																property="name" labelProperty="name" />
														</html:select>
													</td>
												</tr>
											</c:when>
											<c:otherwise>
												<tr>
													<td class="completeLabel" colspan="2">
														There are no other particles from source
														${samplePointOfContact} to copy annotation to.
													</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</c:otherwise>
						</c:choose>
						<br>
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="4">
										<div align="justify">
											Visibilities
										</div>
									</td>
								</tr>

								<tr>
									<td class="leftLabel">
										<strong>Visibility</strong>
									</td>
									<td class="rightLabel">
										<html:select property="file.visibilityGroups" multiple="true"
											size="6">
											<html:options name="allVisibilityGroups" />
										</html:select>
										<br>
										<i>(${applicationOwner}_Researcher and
											${applicationOwner}_DataCurator are always selected by
											default.)</i>
									</td>
								</tr>
							</tbody>
						</table>

						<table width="100%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="topBorderOnly" summary="">
							<tr>
								<td width="30%">
									<span class="formMessage"> </span>
									<br>
									<table width="498" height="32" border="0" align="right"
										cellpadding="4" cellspacing="0">
										<tr>
											<td width="490" height="32">
												<div align="right">
													<div align="right">
														<c:set var="dataId"
															value="${publicationForm.map.file.domainFile.id}" />
														<c:set var="origUrl"
															value="publication.do?page=0&dispatch=setupNew&sampleId=${docSampleId }&location=${location}" />
														<c:if test="${!empty dataId}">
															<c:set var="origUrl"
																value="publication.do?page=0&dispatch=setupUpdate&sampleId=${docSampleId }&location=${location}&fileId=${dataId }" />
														</c:if>
														<input type="reset" value="Reset"
															onclick="javascript:window.location.href='${origUrl}'">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="page" value="2">
														<input type="hidden" name="location" value="local">
														<input type="hidden" name="submitType"
															value="publications">
														<%--														<c:if test="${!empty param.sampleId}">--%>
														<%--															<input type="hidden" name="sampleId"--%>
														<%--																value="${param.sampleId}">--%>
														<%--														</c:if>--%>
														<html:submit />
													</div>
												</div>
											</td>
										</tr>
									</table>
									<div align="right"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
</html:form>
