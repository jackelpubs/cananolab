<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/calendar2.js"> </script>

<html:form action="/createAssayRun">
	<h2>
		<strong>Create Run for Assay <c:out value="${param.assayName}"/></strong>
	</h2>
	<blockquote>
		<html:errors />
		<logic:messagesPresent message="true">
			<ul>
				<font color="red"> <html:messages id="msg" message="true" bundle="workflow">
						<li>
							<bean:write name="msg" />
						</li>
					</html:messages> </font>
			</ul>
		</logic:messagesPresent>
		<html:hidden property="assayId" value="${param.assayId}"/>
		<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td colspan="2" class="dataTablePrimaryLabel">
					<div align="justify">
						<em>DESCRIPTION</em>
					</div>
				</td>
			</tr>
			<tr>
				<td width="27%" class="formLabelWhite">
					<div align="left">
						<strong>Aliquots</strong>
					</div>
				</td>
				<td width="73%" class="formFieldWhite">
					<strong><span class="mainMenu"> </span></strong>
					<table width="41%" align="left" cellpadding="0" cellspacing="0">
						<tr>
							<td width="28%" height="39" valign="top">
								<div align="center">
									<span class="mainMenu"> <span class="formMessage">Aliquots</span> <html:select multiple="true" property="availableAliquot" size="4">
											<html:options collection="allUnmaskedAliquots" property="aliquotId" labelProperty="aliquotName" />
										</html:select> </span>
								</div>
							</td>
							<td width="10%" align="center" valign="middle">
								<table border="0" cellspacing="0" cellpadding="10">
									<tr>
										<td>
											<input type="button" onClick="assignAliquots(document.createAssayRunForm.availableAliquot, document.createAssayRunForm.assignedAliquot)" value=">>" />
										</td>
									</tr>
								</table>
							</td>
							<td width="62%" valign="top">
								<div align="top">
									<span class="formMessage">Use Aliquots</span>
								</div>
								<html:select multiple="true" property="assignedAliquot" size="4">
									<html:options name="allAssignedAliquots" />
								</html:select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<div align="left">
						<strong>Aliquot Commets </strong>
					</div>
				</td>
				<td class="formLabelWhite">
					<div align="justify">
						<span class="formFieldWhite"><html:textarea property="aliquotComment" cols="40" /></span>
					</div>
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">
					<div align="left">
						<strong>Run By*</strong>
					</div>
				</td>
				<td class="formFieldWhite">
					<html:text property="runBy" size="15" />
				</td>
			</tr>
			<tr>
				<td class="formLabelWhite">

					<div align="left">
						<strong>Run Date* </strong>
					</div>
				</td>
				<td class="formFieldWhite">
					<html:text property="runDate" size="10" />
					<span class="formFieldWhite"> <a href="javascript:cal.popup();"> <img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date"> </a> </span>
				</td>
			</tr>
		</table>
		<br>
		<table width="82%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr>
				<td width="30%">
					<span class="formMessage"> </span>
					<br>
					<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td width="490" height="32">
								<div align="right">
									<div align="right">
										<input type="reset" value="Reset" onclick="resetObject(document.createAssayRunForm.assignedAliquot, document.createAssayRunForm.availableAliquot);">
										<html:submit />
									</div>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<p>
			&nbsp;
		</p>
	</blockquote>
</html:form>
<script language="JavaScript">
<!--//
   /* 
  	 cal is java script variable instantiated from calendar2 function
  	 in script.js file
  */ 
  var cal = new calendar2(document.forms['createAssayRunForm'].elements['runDate']);
  cal.year_scroll = true;
  cal.time_comp = false;
  
  /* assignAliquots function moves the selected aliquots 
     from available aliquot list to the assigned select list box.
  */ 
  function assignAliquots(fromObj,toObj)
  {
  		moveItems(fromObj, toObj);
  		for(i = 0; i < toObj.options.length; i++){		
			toObj.options[i].selected=true;	
		} 	
  }
  
  
  /* 
  	 resetObject function re assigns the items of assignedAliquot select box 
  	 to availableAliquot select box.     
  */ 
  function resetObject(fromObj,toObj) 
  {
		for(i = 0; i < fromObj.options.length; i++){		
		fromObj.options[i].selected=true;	
		}
		moveItems(fromObj,toObj);
  return true;		
  }

//-->
</script>
