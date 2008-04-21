<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:form action="/nanoparticleEntity">
<table width="100%" align="center">
	<tr>
		<td>
			<h4>
				Nanoparticle Sample Comosition - Particle Entity
			</h4>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=composition_help')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<h5 align="center">
				Entity#1:Dendrimer
			</h5>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table width="100%" border="0" align="center" cellpadding="3"
				cellspacing="0" class="topBorderOnly" summary="" id="summary">
				<tr>
				<tr class="topBorder">
					<td class="formTitle" colspan="4">
						<div align="justify">
							Summary
						</div>
					</td>
				</tr>
				<tr>
					<td class="leftLabel">
						<strong>Particle Entity Type</strong>
					</td>
					<td class="rightLabel">
						${nanoparticleEntityForm.map.entity.type}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="leftLabel" valign="top">
						<strong>Description</strong>
					</td>
					<td class="rightLabel" colspan="3">
						${nanoparticleEntityForm.map.entity.description}&nbsp;
					</td>
				</tr>
			</table>
			<br>
			<table class="topBorderOnly" cellspacing="0" cellpadding="3"
				width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify" id="compEleInfoTitle">
								Composing Element Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="completeLabel" colspan="4">
							<table border="0" width="100%">
								<tr>
									<td></td>
									<td>
										<logic:iterate name="nanoparticleEntityForm"
											property="entity.composingElements"
											id="composingElement" indexId="ind">
											<table class="topBorderOnly" cellspacing="0" cellpadding="3"
												width="100%" align="center" summary="" border="0">
												<tbody>
													<tr>
														<td class="formSubTitle" colspan="4">
															Composing Element #${ind+1}
														</td>
													</tr>
													<tr>
														<td class="leftLabelWithTop" valign="top">
															<strong>Composing Element Type</strong>
														</td>
														<td class="labelWithTop">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.type}
														</td>
														<td class="labelWithTop" valign="top">
															<strong>Chemical Name</strong>
														</td>
														<td class="rightLabelWithTop">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.name}
														</td>
													</tr>
													<tr>
														<td class="leftLabel" valign="top">
															<strong>Molecular Formula Type</strong>
														</td>
														<td class="labelWithTop" valign="top">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.molecularFormulaType}
														</td>
														<td class="labelWithTop" valign="top">
															<strong>Molecular Formula</strong>
														</td>
														<td class="rightLabelWithTop" valign="top">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.molecularFormula}
														</td>
													</tr>
													<tr>
														<td class="leftLabel" valign="top">
															<strong>Value</strong>
														</td>
														<td class="labelWithTop" valign="top">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.value}
														</td>
														<td class="labelWithTop" valign="top">
															<strong>Unit</strong>
														</td>
														<td class="rightLabelWithTop" valign="top">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.valueUnit}
														</td>
													</tr>
													<tr>
														<td class="leftLabel" valign="top" colspan="1">
															<strong>Description</strong>
														</td>
														<td class="rightLabel" colspan="3">
															${nanoparticleEntityForm.map.entity.composingElements[ind].domainComposingElement.description}
														</td>
													</tr>
												</tbody>
											</table>
											<br>
										</logic:iterate>
									</td>
								</tr>
							</table>
						</td>
					</tr>
			</table>
			<br>
			<table class="topBorderOnly" cellspacing="0" cellpadding="3"
				width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify" id="peFileTitle">
								Particle Entity File Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="completeLabel" colspan="4">

							<table border="0" width="100%">
								<tr>
									<td></td>
									<td>
										<logic:iterate name="nanoparticleEntityForm"
											property="entity.composingElements"
											id="composingElement" indexId="ind">
											<table class="topBorderOnly" cellspacing="0" cellpadding="3"
												width="100%" align="center" summary="" border="0">
												<tbody>
													<tr>
														<td class="formSubTitle" colspan="4">
															Composing Element #${ind+1}
														</td>
													</tr>
													<tr>
														<td class="leftLabelWithTop" valign="top">
															<strong>File Name</strong>
														</td>
														<td class="rightLabelWithTop">
															test file
														</td>
													</tr>
													<tr>
														<td class="leftLabel" valign="top">
															<strong>File Type</strong>
														</td>
														<td class="rightLabel" valign="top">
															image test
														</td>
													</tr>
												</tbody>
											</table>
											<br>
										</logic:iterate>
									</td>
								</tr>
							</table>
						</td>
					</tr>
			</table>
		</td>
	</tr>
</table>
</html:form>