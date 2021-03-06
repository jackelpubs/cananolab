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
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/AccessibilityManager.js"></script>
	<script type='text/javascript'
	src='/caNanoLab/dwr/interface/AccessibilityManager.js'></script>
<script type="text/javascript" src="javascript/TransferOwnerManager.js"></script>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Transfer Ownership" />
	<jsp:param name="topic" value="transfer_data_ownership_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/transferOwner" styleId="transferOwnerForm">
	<jsp:include page="/bodyMessage.jsp" />
	<table width="100%" align="center" class="submissionView" summary="layout">
		<tr>
			<td class="cellLabel" width="30%">
				<label for="currentOwner">Current Owner Login Name *</label>
			</td>
			<td>
				<html:text styleId="currentOwner" property="currentOwner" onchange="" />
			</td>
			<td>
						<a href="#currentOwnerField"
							onclick="javascript:showMatchedUserDropdown('currentOwner','matchedCurrentOwnerSelect','cancelCurrentOwnerBrowse')"><img
								src="images/icon_browse.jpg" align="middle"
								alt="search existing users" border="0" /></a>
			</td>
			<td width="50%">
				<table class="invisibleTable" summary="layout">
					<tr>
						<td>
							<img src="images/ajax-loader.gif" border="0" class="counts"
								id="loaderImg" style="display: none" alt="load existing users for current owner">
						</td>
						<td><label for="matchedCurrentOwnerSelect">&nbsp;</label>
							<html:select
								property="currentOwner"
								size="10" styleId="matchedCurrentOwnerSelect" style="display: none" onclick="updateOwnerLoginName('currentOwner','matchedCurrentOwnerSelect','cancelCurrentOwnerBrowse')">
							</html:select>
						</td>
						<td><a id="cancelCurrentOwnerBrowse" style="display:none" href="javascript:cancelBrowseSelect('matchedCurrentOwnerSelect','cancelCurrentOwnerBrowse')">Cancel</a></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="newOwner">New Owner Login Name *</label>
			</td>
			<td>
				<html:text styleId="newOwner" property="newOwner" onchange=""/>
			</td>
			<td>
						<a href="#newOwnerField"
							onclick="javascript:showMatchedUserDropdown('newOwner','matchedNewOwnerSelect','cancelNewOwnerBrowse')"><img
								src="images/icon_browse.jpg" align="middle"
								alt="search existing users" border="0" /></a>
			</td>
			<td width="50%">
				<table class="invisibleTable" summary="layout">
					<tr>
						<td>
							<img src="images/ajax-loader.gif" border="0" class="counts"
								id="loaderImg" style="display: none" alt="load existing users for new owner">
						</td>
						<td><label for="matchedNewOwnerSelect">&nbsp;</label>
							<html:select
								property="newOwner"
								size="10" styleId="matchedNewOwnerSelect" style="display: none" onclick="updateOwnerLoginName('newOwner','matchedNewOwnerSelect','cancelNewOwnerBrowse')">
							</html:select>
						</td>
						<td><a id="cancelNewOwnerBrowse" style="display:none" href="javascript:cancelBrowseSelect('matchedNewOwnerSelect','cancelNewOwnerBrowse')">Cancel</a></td>
					</tr>
				</table>
			</td>

		</tr>
		<tr>
			<td class="cellLabel">
				<label for="dataType">Data Type *</label>
			</td>
			<td colspan="3">
				<html:select styleId="dataType" property="dataType" >
					<option value=""></option>
					<html:option value="Sample" />
					<html:option value="Publication" />
					<html:option value="Protocol" />
					<html:option value="Collaboration Group" />
				</html:select>
			</td>
		</tr>
	</table>
	<br>
	<c:set var="hiddenDispatch" value="transfer" />
	<c:set var="hiddenPage" value="1" />
	<c:set var="resetOnclick"
		value="javascript:location.href='transferOwner.do?dispatch=setupNew&page=0'" />
	<%@include file="../bodySubmitButtons.jsp"%>
</html:form>
