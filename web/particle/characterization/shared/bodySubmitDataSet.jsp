<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	class="smallTable3" style="display: block" id="designDataTable"
	width="90%">
	<tr>
		<td valign="top" colspan="2" class="subformTitle">
			Design Data Table
		</td>
	</tr>
	<tr>
		<td class="leftLabelWithTopNoBottom">
			<strong>Column Type</strong>
			<input type="hidden" id="columnId">
		</td>
		<td class="rightLabelWithTopNoBottom">
			<select id="datumOrCondition" onChange="showDatumConditionInfo(null)">
				<option value="Datum">
					Datum
				</option>
				<option value="Condition">
					Condition
					</ption>
			</select>
		</td>
	</tr>
	
	<tr>
		<td class="leftLabelWithTopNoBottom">
			<strong>Column Name*</strong>
		</td>
		<td class="rightLabelWithTopNoBottom">
			<select id="name" onchange="javascript:callPrompt('Name', 'name');setConditionPropertyOptionsByCharName(null);">
			<option value=""></option>
			<option value="[Other]">[Other]</option>
			</select>			
		</td>
	</tr>
	<tr id="conditionProperty" style="display: none;">
		<td class="leftLabelNoBottom">
			<strong>Property</strong>
		</td>
		<td class="rightLabelNoBottom">
			<select id="property" onchange="javascript:callPrompt('Condition Property', 'property');">
			</select>
		</td>
	</tr>
	

	<tr>
		<td class="leftLabelNoBottom">
			<strong>Column Value Type</strong>
		</td>
		<td class="rightLabelNoBottom" align="left">
			<input type="text" id="valueType">
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom">
			<strong>Column Value Unit</strong>
		</td>
		<td class="rightLabelNoBottom">
			<input type="text" id="valueUnit">
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" colspan="1">
			<strong>Is column value constant?</strong>
			<input type="checkbox">
		</td>
		<td class="rightLabelNoBottom">
			<input type="text" id="value">
		</td>
	</tr>
	<tr>
		<td class="leftLabelNoBottom" colspan="1">
			&nbsp;
		</td>
		<td class="rightLabelNoBottomRightAlign">
			<input class="noBorderButton" type="button" value="New"
				onclick="clearTheDataColumn();" />
			<input class="noBorderButton" type="button" value="Save"
				onclick="addDatumColumn();" />
			<input class="noBorderButton" type="button" value="Delete"
				onclick="deleteDatumColumn()" />
		</td>
	</tr>

	<tr id="datumColumnsDivRowDisplay">
		<td class="leftLabelNoBottom" valign="top" colspan="2">
			<table>
				<tr>
					<td colspan="2">
						<div id="datumColumnsDivDisplay" style="display: block;">
							<table id="datumColumnsTableDisplay" class="smalltable"
								border="1">
								<tbody id="datumColumnsDisplay">
									<tr id="datumColumnPatternRowDisplay">
										<td id="datumColumnPatternDisplay" style="display: none;">
										<input class="noBorderButton" id="datumColumnNameDisplay" type="button"
											size="2" value="datumColumnNameDisplay" onclick="editColumn(this.id)" />
											<span id="columnDisplayName" class="greyFont2" style="display: none;">columnDisplayName</span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td align="right">
						<input class="noBorderButton" type="button"
							value="Populate Data Table" onclick="show('populateDataTable')" />
					</td>
				</tr>
			</table>
		</td>

	</tr>
</table>
<br>
<table border="0" align="center" class="smalltable3"
	id="populateDataTable" style="display: none;" width="90%">
	<tr>
		<td valign="top" colspan="2" class="subformTitle">
			Populate Data Table
		</td>
	</tr>
	<tr id="datumColumnsDivRow" style="display: none;">
		<td class="leftLabelNoBottom" valign="top" colspan="2">
			<div id="datumColumnsDiv" style="display: block;">
				<table id="datumColumnsTable" class="smalltable" border="0">
					<tbody id="datumColumns">
						<tr id="datumColumnPattern" style="display: none;">
							<td>
								<input id="datumColumnId" type="hidden" value="datumColumnId" />
								<input id="datumOrConditionColumn" type="hidden" value="datumOrConditionColumn" />
								<input id="conditionColumnProperty" type="hidden" value="conditionColumnProperty" />
								<input id="datumColumnDataRowId" type="hidden"
									value="datumColumnDataRowId" />
								<input id="datumColumnDataSetId" type="hidden"
									value="datumColumnDataSetId" />
								<span id="datumColumnName" class="greyFont2">datumColumnName</span>
								(<span id="datumColumnValueType" class="greyFont2">ValueType</span>,
								<span id="datumColumnValueUnit" class="greyFont2"><strong>ValueUnit</strong></span>)
							</td>
							<td>
								<input id="datumColumnValue" type="text" size="6"
									value="datumColumnValue" />
							</td>
						</tr>
						<tr id="datumColumnsDivRow2">
							<td>
								&nbsp;
							</td>
							<td class="rightLabelNoBottom" valign="top" align="right"
								colspan="1">
								<div id="addRowButtons" style="display: none;">
									<input class="noBorderButton" type="button" value="New"
										onclick="clearTheDataRow();" />
									<input class="noBorderButton" type="button" value="Save"
										onclick="addRow()" />
									<input class="noBorderButton" type="button" value="Delete"
										onclick="deleteClicked()" />
								</div>
								&nbsp;
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>
	</tr>

	<tr id="datumMatrixDivRow">
		<td class="completeLabelNoTopBottom" valign="top" colspan="2">
			<div id="datumMatrixDiv" style="display: block;">
				<table id="datumMatrixTable" class="smalltable" border="1">
					<tbody id="datumMatrix">
						<tr id="matrixHeader" class="greyFont2" style="display: none;">
						</tr>
						<tr id="datumMatrixPatternRow" class="greyFont2"
							style="display: none;">
						</tr>
					</tbody>
				</table>
			</div>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<input type="button" value="Delete"
				onclick="javascript:submitAction(document.forms[0],
										'characterization', 'deleteDataSet');">
		</td>
		<td class="rightLabel" align="right" colspan="1">
			<div align="right">
				<input type="button" value="Cancel"
					onclick="javascript:cancelDataSet();">
				<input type="button" value="Save"
					onclick="javascript:saveDataSet('characterization');">
			</div>
		</td>
	</tr>

</table>

<html:hidden styleId="dataSetId" property="achar.theDataSet.domain.id" />