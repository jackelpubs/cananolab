<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<script type="text/javascript">
<!--//
function refreshAliquots() {
  //window.location.href="preCreateAliquot.do?numberOfAliquots="+document.createAliquotForm.numberOfAliquots.value;
  document.createAliquotForm.action="/calab/preCreateAliquot.do";
  document.createAliquotForm.submit();
}
//-->
</script>
<h2>
	<br>
	Create Aliquot
</h2>
<html:errors />
<blockquote>
	<html:form action="/createAliquot">
		<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td class="dataTablePrimaryLabel">
					<div align="justify">
						<em>DESCRIPTION</em>
					</div>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<div align="justify">
						<strong>Sample ID*<span class="formFieldWhite"> <html:select property="sampleId">
									<option value=""></option>
									<html:options name="allSampleIds" />
								</html:select></span></strong>&nbsp; &nbsp;<strong>Lot ID*<span class="formFieldWhite"> <html:select property="lotId">
									<option value="N/A">
										N/A
									</option>
									<html:options name="allLotIds" />
								</html:select></span></strong>&nbsp; &nbsp; <strong>Aliquot ID<span class="formFieldWhite"> <html:select property="parentAliquotId">
									<option value=""></option>
									<html:options name="allAliquotIds" />
								</html:select></span></strong>
					</div>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<div align="justify">
						<strong><span class="formFieldWhite"> </span>Number of Aliquots *<span class="formFieldWhite"> <html:text property="numberOfAliquots" size="5" onblur="javascript:refreshAliquots();" /> &nbsp; </span></strong>
					</div>
				</td>
			</tr>
		</table>

		<br>
		<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
			<tbody>
				<tr class="topBorder">
					<td class="dataTablePrimaryLabel" width="30%">
						<div align="justify">
							<em>TEMPLATE ALIQUOT</em>
						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Container Type* <span class="formFieldWhite"> <html:select property="template.container.containerType">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="containerTypes" />
									</html:select></span> &nbsp; &nbsp; &nbsp; Other <span class="formFieldWhite"><html:text property="template.container.otherContainerType" size="8" /></span> &nbsp; &nbsp; &nbsp; </strong>
						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabelWhite style1">
						<div align="left">
							<strong>Quantity <span class="formFieldWhite"><html:text size="5" property="template.container.quantity" /></span> <span class="formFieldWhite"> <html:select property="template.container.quantityUnit">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="quantityUnits" />
									</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text size="5" property="template.container.concentration" /></span><span class="formFieldWhite"> <html:select property="template.container.concentrationUnit">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="concentrationUnits" />
									</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text size="5" property="template.container.volume" /></span><span class="formFieldWhite"> <html:select property="template.container.volumeUnit">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="volumeUnits" />
									</html:select></span></strong> &nbsp;&nbsp;&nbsp;
						</div>

						<div align="justify"></div>
					</td>
				</tr>
				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Diluents/Solvent <html:text property="template.container.solvent" size="10" /> &nbsp; &nbsp; &nbsp; How Created <html:select property="template.howCreated">
									<option value=""></option>
									<html:options name="aliquotCreateMethods" />
								</html:select> &nbsp; &nbsp; &nbsp;</strong>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite style1">
						<div align="justify">
							<strong>Storage Conditions <span class="formField"><html:text property="template.container.storageCondition" size="50" /></span></strong>
						</div>

						<div align="justify"></div>
					</td>
				</tr>
				<tr>
					<td class="formLabel">
						<div align="left">
							<strong>Storage Location<br> <br> Room&nbsp; <html:select property="template.container.storageRoom">
									<option value=""></option>
									<html:options name="aliquotContainerInfo" property="storageRooms" />
								</html:select> &nbsp; Freezer&nbsp; <html:select property="template.container.storageFreezer">
									<option value=""></option>
									<html:options name="aliquotContainerInfo" property="storageFreezers" />
								</html:select> &nbsp;Shelf &nbsp; <html:text property="template.container.storageShelf" size="5" /> &nbsp; Box &nbsp; <html:text property="template.container.storageBox" size="5" /> &nbsp;</strong>
						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabelWhite">
						<div align="left">
							<strong>General Comments</strong>
							<br>
							<span class="formField"><span class="formFieldWhite"> <html:textarea property="generalComments" cols="70" /></span></span>
						</div>
					</td>
				</tr>
			</tbody>
		</table>

		<br>
		<logic:present name="aliquotMatrix">
			<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
				<logic:iterate name="aliquotMatrix" id="aliquotRow" type="gov.nih.nci.calab.dto.administration.AliquotBean[]" indexId="rowNum">
					<tr>
						<td class="formLabelBoxWhite">
							<table align="left" border="0" align="center" cellpadding="3" cellspacing="0">
								<tr>
									<logic:iterate name="aliquotRow" id="aliquot" type="gov.nih.nci.calab.dto.administration.AliquotBean" indexId="colNum">
										<td width="85">
											<table border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td class="formLabelBoxWhite">
														<logic:present name="aliquot">
															<%java.util.Map editAliquotParams = new java.util.HashMap();
			editAliquotParams.put("rowNum", rowNum);
			editAliquotParams.put("colNum", colNum);
			pageContext.setAttribute("editAliquotParams", editAliquotParams);%>

															<html:link action="preEditAliquot" name="editAliquotParams">
																<bean:write name="aliquot" property="aliquotId" />
																<br>
																<bean:write name="aliquot" property="container.quantity" />
																<bean:write name="aliquot" property="container.quantityUnit" />
																<br>
																<bean:write name="aliquot" property="container.concentration" />
																<bean:write name="aliquot" property="container.concentrationUnit" />
																<br>
																<bean:write name="aliquot" property="container.volume" />
																<bean:write name="aliquot" property="container.volumeUnit" />
															</html:link>
														</logic:present>
													</td>
												</tr>
											</table>
										</td>
									</logic:iterate>
								</tr>
							</table>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:present>
		<table border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr>
				<td width="30%">
					<span class="formMessage"> Aliquoted by: Jane Doe Aliquoted Date: 02/06/2006</span>
					<table border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td width="198" height="32">
								<div align="right">
									<input type="reset" value="Reset">
									<html:submit />
								</div>
							</td>
						</tr>
					</table>
					<div align="right"></div>
				</td>
			</tr>
		</table>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
	</html:form>
</blockquote>
