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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.solubility.solvent}">
				<table class="summaryViewNoGrid" align="left">
					<tr>
						<th scope="col" class="cellLabel">
							Solvent
						</th>
						<th scope="row" class="cellLabel">
							Is Soluble?
						</th>
						<td class="cellLabel">
							Critical Concentration
						</td>
					</tr>
					<tr>
						<td>
							<c:out value="${charBean.solubility.solvent}"/>
						</td>
						<td>
							<c:out value="${charBean.solubility.isSoluble}"/>
						</td>
						<td>
							<c:out value="${charBean.solubility.criticalConcentration}"/>
							<c:out value="${charBean.solubility.criticalConcentrationUnit}"/>
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>N/A
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView" summary="layout">
			<tr>
				<th colspan="4">
					Solubility Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					<label for="solvent">Solvent</label>
				</td>
				<td>
					<div id="solventPrompt">
						<select name="achar.solubility.solvent" id="solvent"
							onchange="javascript:callPrompt('Solvent', 'solvent', 'solventPrompt');">
							<option value=""></option>
							<c:forEach var="type" items="${solventTypes}">
								<c:choose>
									<c:when
										test="${type eq characterizationForm.map.achar.solubility.solvent}">
										<option value="${type}" selected="selected"><c:out value="${type}"/></option>
									</c:when>
									<c:otherwise>
										<option value="${type}"/><c:out value="${type}"/></option>
									</c:otherwise>
								</c:choose>						
							</c:forEach>
							<option value="other">
								[other]
							</option>
						</select>
					</div>
				</td>
				<td class="cellLabel">
					<label for="isSoluble">Is Soluble</label>
				</td>
				<td>
					<c:choose>
						<c:when
							test="${empty characterizationForm.map.achar.isSoluble}">
							<c:set var="selectNoneStr" value='selected="selected"' />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${characterizationForm.map.achar.isSoluble eq 'true'}">
									<c:set var="selectYesStr" value='selected="selected"' />
								</c:when>
								<c:otherwise>
									<c:set var="selectNoStr" value='selected="selected"' />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<select name="achar.isSoluble" id="isSoluble">
						<option value="" ${selectNoneStr}></option>
						<option value="true" ${selectYesStr}>Yes</option>
						<option value="false" ${selectNoStr}>No</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					<label for="criticalConcentration">Critical Concentration</label>
				</td>
				<td>
					<input type="text" name="achar.solubility.criticalConcentration"
						id="criticalConcentration"
						value="${characterizationForm.map.achar.solubility.criticalConcentration}" />
						<%-- onkeydown="return filterFloatNumber(event)" --%>
					<div id="concentrationUnitPrompt">
						<label for="concentrationUnit">&nbsp;</label>
						<select name="achar.solubility.criticalConcentrationUnit"
							id="concentrationUnit"
							onchange="callPrompt('Concentration Unit', 'concentrationUnit', 'concentrationUnitPrompt')">
							<option value=""></option>
							<c:forEach var="unit" items="${concentrationUnits}">
								<c:choose>
									<c:when
										test="${unit eq characterizationForm.map.achar.solubility.criticalConcentrationUnit}">
										<option value="${unit}" selected="selected">
											<c:out value="${unit}"/>
										</option>
									</c:when>
									<c:otherwise>
										<option value="${unit}">
											<c:out value="${unit}"/>
										</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<option value="other">
								[other]
							</option>
						</select>
					</div>
				</td>
				<td colspan="2">
					&nbsp;
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>
