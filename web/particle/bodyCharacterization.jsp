<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="/${nanoparticleCharacterizationForm.map.charName}">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					<br>
					${pageTitle} ${nanoparticleCharacterizationForm.map.charName}
				</h4>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${helpName}')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h5 align="center">
					${nanoparticleCharacterizationForm.map.particle.sampleName}
					${nanoparticleCharacterizationForm.map.particle.sampleType}
				</h5>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<jsp:include page="/particle/shared/bodyCharacterizationSummary.jsp" />
				<jsp:include
					page="/particle/shared/bodyCharacterizationInstrument.jsp" />
				<c:choose>
					<c:when test="${!empty detailPage}">
						<jsp:include page="${detailPage}" />
					</c:when>
				</c:choose>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									Characterization File/Derived Data Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="4">
								<table border="0" width="100%">
									<tr>
										<c:choose>
											<c:when test="${canCreateNanoparticle eq 'true'}">
												<td valign="bottom">
													<a href="#"
														onclick="javascript:addCharacterizationFile(nanoparticleCharacterizationForm, '${nanoparticleCharacterizationForm.map.charName}', '${nanoparticleCharacterizationForm.map.charName}')"><span
														class="addLink">Add File/Derived Data</span> </a>
												</td>
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>
										</c:choose>
										<td>
											<logic:iterate name="nanoparticleCharacterizationForm"
												property="achar.derivedBioAssayDataList"
												id="derivedBioAssayData" indexId="fileInd">
												<jsp:include
													page="/particle/shared/bodyCharacterizationFile.jsp">													
													<jsp:param name="fileInd" value="${fileInd}" />
												</jsp:include>
												<br>
											</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</table>
				<br>
				<jsp:include page="/particle/shared/bodyCharacterizationCopy.jsp" />
				<jsp:include page="/particle/shared/bodyCharacterizationSubmit.jsp" />
			</td>
		</tr>
	</table>
</html:form>

