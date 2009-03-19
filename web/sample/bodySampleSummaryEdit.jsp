<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='javascript/POCManager.js'></script>

<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/SampleManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<html:form action="/sample">
	<c:choose>
		<c:when
			test="${!empty sampleForm.map.sampleBean.pocBean.domain.id}">
			<c:set var="pocDetailDisplay" value="display: inline;" />
			<c:if
				test="${submitPOCProcessing eq 'true'}">
				<c:set var="pocDetailDisplay" value="display: none;" />
			</c:if>
		</c:when>
		<c:otherwise>
			<c:set var="pocDetailDisplay" value="display: none;" />
		</c:otherwise>
	</c:choose>
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Update Sample
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_nano_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="2">
								<div align="justify">
									Sample Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Sample Name *</strong>
							</td>
							<td class="rightLabel">
								<html:text
									property="sampleBean.domain.name"
									size="50" />
								<c:if
									test="${!empty sampleForm.map.sampleBean.domain.id}">
									<html:hidden styleId="sampleId"
										property="sampleBean.domain.id"
										value="${sampleForm.map.sampleBean.domain.id}" />
								</c:if>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Primary Point of Contact *</strong>
							</td>
							<td class="rightLabel">
								<html:select property="sampleBean.pocBean.domain.id"
									styleId="primaryPOCList"
									onchange="javascript:setupPOC(sampleForm, 'primaryPOCList');
												setPOCDetailLink('primaryPOCList', 'pocDetail');
												removeOrgVisibility('primaryPOCList');">
									<option />
										<c:if test="${!empty allPointOfContacts}">
											<html:options collection="allPointOfContacts"
												labelProperty="displayName" property="domain.id" />
										</c:if>
									<option value="other">
										[Other]
									</option>
								</html:select>
								&nbsp;
								<a style="${pocDetailDisplay}" id="pocDetail" href="#"
									onclick="javascript:submitAction(sampleForm, 'sample', 'pointOfContactDetailView');">
									<span class="addLink2">View Detail</span> </a>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Keywords</strong>
								<i>(one keyword per line)</i>
							</td>
							<td class="rightLabel">
								<html:textarea property="sampleBean.keywordsStr"
									rows="6" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<html:select property="sampleBean.visibilityGroups"
									styleId="visibilityGroup" multiple="true" size="6">
									<html:options name="allVisibilityGroupsNoOrg" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and
									${applicationOwner}_DataCurator are always selected by
									default.)</i>
							</td>
						</tr>
					</tbody>
				</table>
				<br>
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
												<c:set var="origUrl"
													value="anoparticleSample.do?page=0&sampleId=${sampleId}&dispatch=${param.dispatch}&location=local" />
												<input type="reset" value="Reset"
													onclick="javascript:window.location.href='${origUrl}'">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="1">
												<html:hidden property="sampleBean.createdBy"
													value="${user.loginName }" />
												<html:submit value="create" />
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
	</table>
</html:form>

