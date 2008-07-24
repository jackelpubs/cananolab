<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<c:set var="action" value="Submit" scope="request" />
<c:if test="${param.dispatch eq 'setupUpdate'}">
	<c:set var="action" value="Update" scope="request" />
</c:if>

<html:form action="/submitPublication" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Submit Publication
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="TODO_nano_publication_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>					
			</td>
		</tr>
		<c:choose>
			<c:when
				test="${empty allUserParticleNames && param.dispatch eq 'setup'}">
				<tr>
					<td colspan="2">
						<font color="blue" size="-1"><b>MESSAGE: </b>There are no
							nanoparticle samples in the database. Please make sure to 
							<html:link page="/submitNanoparticleSample.do?dispatch=setup&page=0&location=${location}" scope="page" >create
							a new nanoparticle sample</html:link> first. </font>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=report" />
						<table class="topBorderOnly" cellspacing="0" cellpadding="3"
							width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="4">
										<div align="justify">
											Publication Information
										</div>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Publication Type*</strong>
									</td>
									<td class="label">
										<html:select property="file.domainFile.category"
											onchange="javascript:callPrompt('Publication Category', 'file.domainFile.category');"
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
									<td class="rightLabel">
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
									</td>
									<td class="label" colspan="3">
										<html:multibox property="file.domainFile.researchArea" value="synthesis"/> synthesis
										<html:multibox property="file.domainFile.researchArea" value="characterization"/> characterization
										<html:multibox property="file.domainFile.researchArea" value="cell line"/> cell line
									</td>	
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>PubMed ID</strong>
									</td>
									<td class="rightLabel" colspan="3">	
										<a
												href="searchReport.do?dispatch=download&amp;fileId=${submitPublicationForm.map.file.domainFile.id}&amp;location=local"
												target="${submitPublicationForm.map.file.urlTarget}">
												Click to look up PubMed Identifier</a>
										<br>									
										<html:text property="file.domainFile.pubMedId" size="30" />
										<html:button property="file.domainFile.pubMedId" value="Auto Populate Fields"/>
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Digital Object ID</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.digitalObjectId" size="30" />
										<html:button property="file.domainFile.digitalObjectId" value="Auto Populate Fields"/>
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Title*
										</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.title" size="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Journal
										</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.domainFile.title" size="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Authors:</strong><br>(TODO)
									</td>
									<td class="rightLabel" colspan="3">
										<html:text property="file.authors" size="80" />
									</td>
								</tr>
								<tr>
									<td class="leftLabel">
										<strong>Year of Publication
										</strong>
									</td>
									<td class="label">
										<html:text property="file.domainFile.year" size="17" 
											onkeydown="return filterInteger(event)"/>
									</td>
									<td class="label">
										<strong>Volume
										</strong>
									</td>
									<td class="rightLabel">
										<html:text property="file.domainFile.volume" size="17"/>
									</td>
								</tr>								
								<tr>
									<td class="leftLabel">
										<strong>Start Page
										</strong>
									</td>
									<td class="label">
										<html:text property="file.domainFile.startPage" size="17" 
											onkeydown="return filterInteger(event)"/>
									</td>
									<td class="label">
										<strong>End Page
										</strong>
									</td>
									<td class="rightLabel">
										<html:text property="file.domainFile.endPage" size="17" 
											onkeydown="return filterInteger(event)"/>
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Keywords<br><em>(one keyword per line)</em></strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.keywordsStr"
											rows="3" cols="60"/>
									</td>
								</tr>
								<tr>
									<td class="leftLabel" valign="top">
										<strong>Description</strong>
									</td>
									<td class="rightLabel" colspan="3">
										<html:textarea property="file.domainFile.description" rows="3"
											cols="60" />
									</td>
								</tr>			
							</tbody>
						</table>

						<br>
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
								<tr>
									<td class="leftLabel" valign="top" width="20%">
										<strong>Copy to other DNT nanoparticle samples</strong>
									</td>
									<td class="rightLabel">
										<html:select property="file.particleNames" multiple="true"
											size="5">
											<html:options name="allUserParticleNames" />
										</html:select>
									</td>
								</tr>
							</tbody>
						</table>
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
											${applicationOwner}_PI are always selected by default.)</i>
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
														<input type="reset" value="Reset" onclick="javascript:window.location.reload()">
														<input type="hidden" name="dispatch" value="create">
														<input type="hidden" name="page" value="2">
														<c:if test="${!empty param.particleId}">
															<input type="hidden" name="particleId"
																value="${param.particleId}">
														</c:if>
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
