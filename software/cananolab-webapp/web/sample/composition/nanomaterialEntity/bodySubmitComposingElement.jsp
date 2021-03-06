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
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="4" align="left" scope="col">
			Composing Element Info
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			<label for="elementType">Type*</label>
		</td>
		<td>
			<div id="elementTypePrompt">
				<html:select styleId="elementType"
					property="nanomaterialEntity.theComposingElement.domain.type"
					onchange="javascript:callPrompt('Composing Element Type', 'elementType', 'elementTypePrompt');">
					<option />
						<c:choose>
							<c:when test="${entityType ne 'emulsion'}">
								<html:options name="composingElementTypes" />
							</c:when>
							<c:otherwise>
								<html:options name="emulsionComposingElementTypes" />
							</c:otherwise>
						</c:choose>
					<option value="other">
						[other]
					</option>
				</html:select>
			</div>
		</td>
		<td class="cellLabel">
			<label for="elementName">Chemical Name*</label>
		</td>
		<td>
			<html:text
				property="nanomaterialEntity.theComposingElement.domain.name"
				size="30" styleId="elementName" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<label for="pubChemDataSource">PubChem DataSource</label>
		</td>
		<td>
			<div id="pubChemDataSourcePrompt">
				<html:select styleId="pubChemDataSource"
					property="nanomaterialEntity.theComposingElement.domain.pubChemDataSourceName"
					onchange="javascript:callPrompt('PubChem DataSource', 'pubChemDataSource', 'pubChemDataSourcePrompt');">
					<option value="" />
						<html:options name="pubChemDataSources" />
				</html:select>
			</div>
		</td>
		<td class="cellLabel">
			<label for="pubChemId">PubChem Id</label>
		</td>
		<td>
			<html:text
				property="nanomaterialEntity.theComposingElement.domain.pubChemId"
				onkeydown="return filterInteger(event)" size="30"
				styleId="pubChemId" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<label for="elementValue">Amount</label>
		</td>
		<td>
			<html:text
				property="nanomaterialEntity.theComposingElement.domain.value"
				size="30" styleId="elementValue" />
				<%--onkeydown="return filterFloatNumber(event)" --%>
		</td>
		<td class="cellLabel">
			<label for="elementValueUnit">Amount Unit</label>
		</td>
		<td valign="top">
			<div id="elementValueUnitPrompt">
				<html:select styleId="elementValueUnit"
					property="nanomaterialEntity.theComposingElement.domain.valueUnit"
					onchange="javascript:callPrompt('Unit', 'elementValueUnit', 'elementValueUnitPrompt');">
					<option value="" />
						<html:options name="composingElementUnits" />
					<option value="other">
						[other]
					</option>
				</html:select>
			</div>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<label for="molFormulaType">Molecular Formula Type</label>
		</td>
		<td>
			<div id="molFormulaTypePrompt">
				<html:select styleId="molFormulaType"
					property="nanomaterialEntity.theComposingElement.domain.molecularFormulaType"
					onchange="javascript:callPrompt('Molecular Formula Type', 'molFormulaType', 'molFormulaTypePrompt');">
					<option value="" />
						<html:options name="molecularFormulaTypes" />
					<option value="other">
						[other]
					</option>
				</html:select>
			</div>
		</td>
		<td class="cellLabel">
			<label for="molFormula">Molecular Formula</label>
		</td>
		<td>
			<html:textarea
				property="nanomaterialEntity.theComposingElement.domain.molecularFormula"
				rows="2" cols="40" styleId="molFormula" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			<label for="elementDescription">Description</label>
		</td>
		<td colspan="3">
			<html:textarea
				property="nanomaterialEntity.theComposingElement.domain.description"
				rows="3" cols="65" styleId="elementDescription" />
			<html:hidden
				property="nanomaterialEntity.theComposingElement.domain.id"
				styleId="elementId" />
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Inherent Function
		</td>
		<td colspan="3">
			<div id="functionSection" style="position: relative;">
				<a style="display: block" id="addInherentFunction"
					href="javascript:confirmAddNew(null, 'InherentFunction', 'Inherent Function', 'clearInherentFunction()');">Add</a>
				<br>
				<table id="functionTable" class="editTableWithGrid" width="85%"
					style="display: none;">
					<tbody id="functionRows">
						<tr id="patternHeader">
							<td width="30%" class="cellLabel" scope="col">
								Function Type
							</td>
							<td width="30%" class="cellLabel" scope="col">
								<div id="modalityHeader" style="display: none">
									Imaging Modality
								</div>
							</td>
							<td width="30%" class="cellLabel" scope="col">
								Description
							</td>
							<td>
							</td>
						</tr>
						<tr id="pattern" style="display: none;">
							<td>
								<span id="functionTypeValue">Function Type</span>
							</td>
							<td>
								<span id="functionModalityTypeValue" style="display: none">Imaging
									Modality</span>
							</td>
							<td>
								<span id="functionDescriptionValue">Description</span>
							</td>
							<td>
								<input class="noBorderButton" id="edit" type="button"
									value="Edit"
									onclick="editInherentFunction(this.id);" />
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table id="newInherentFunction" style="display: none;" width="85%" class="promptbox">
					<tbody>
						<tr>
							<html:hidden
								property="nanomaterialEntity.theComposingElement.theFunction.id"
								styleId="functionId" />
							<td class="cellLabel">
								<label for="functionType">Function Type</label>
							</td>
							<td>
								<div id="functionTypePrompt">
									<html:select
										property="nanomaterialEntity.theComposingElement.theFunction.type"
										styleId="functionType"
										onchange="javascript:callPrompt('Function Type', 'functionType', 'functionTypePrompt');displayImageModality();">
										<option value=""></option>
										<html:options name="functionTypes" />
										<option value="other">
											[other]
										</option>
									</html:select>
								</div>
							</td>
							<td class="cellLabel">
								<div id="modalityLabel" style="display: none">
									<label for="imagingModality">Imaging Modality Type</label>
								</div>
							</td>
							<td>
								<div id="imagingModalityPrompt" style="display: none">
									<html:select
										property="nanomaterialEntity.theComposingElement.theFunction.imagingFunction.modality"
										onchange="javascript:callPrompt('Modality Type', 'imagingModality', 'imagingModalityPrompt');"
										styleId="imagingModality">
										<option value="" />
											<html:options name="modalityTypes" />
										<option value="other">
											[other]
										</option>
									</html:select>
								</div>
							</td>
						</tr>
						<tr>
							<td class="cellLabel">
								<label for="functionDescription">Description</label>
							</td>
							<td colspan="3">
								<html:textarea
									property="nanomaterialEntity.theComposingElement.theFunction.description"
									cols="50" rows="3" styleId="functionDescription" />
							</td>
						</tr>
						<tr>
							<td>
								<input style="display: none;" class="promptButton"
									id="deleteFunction" type="button" value="Remove"
									onclick="deleteTheInherentFunction()" />
							</td>
							<td colspan="3">
								<div align="right">
									<input class="promptButton" type="button" value="Save"
										onclick="addInherentFunction();show('functionTable');closeSubmissionForm('InherentFunction');" />
									<input class="promptButton" type="button" value="Cancel"
										onclick="clearInherentFunction();closeSubmissionForm('InherentFunction');" />
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<c:if test="${theSample.userDeletable eq 'true'}">
				<input type="button" value="Remove"
					onclick="removeComposingElement('${validate}', 'nanomaterialEntity')"
					id="deleteComposingElement" style="display: none;" />
			</c:if>
		</td>
		<td colspan="3">
			<div align="right">
				<input type="button" value="Save"
					onclick="addComposingElement('${validate}', 'nanomaterialEntity');" />
				<input type="button" value="Cancel"
					onclick="clearComposingElement();closeSubmissionForm('ComposingElement');" />
			</div>
		</td>
	</tr>
</table>
