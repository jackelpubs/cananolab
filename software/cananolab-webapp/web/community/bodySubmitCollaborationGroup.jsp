<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<th colspan="2">
			Collaboration Group Information
		</th>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			Name*
		</td>
		<td>
			<html:text styleId="groupName" property="group.name" size="30"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%">
			Description
		</td>
		<td>
			<html:textarea styleId="groupDescription"
				property="group.description" rows="5" cols="70"/>
		</td>
	</tr>
	<tr>
		<td class="cellLabel" width="15%" id="userLabel">
			User
		</td>
		<td>
			<div id="userSection">
				<a style="display:block" id="addUser" href="javascript:show('newUser');clearUserAccess();">Add</a>
				<br />
				<table id="userTable" class="editTableWithGrid" width="85%"
					style="display: none;">
					<tbody id="userRows">
						<tr id="patternHeader">
							<td width="30%" class="cellLabel">
								Login Name
							</td>
							<td width="30%" class="cellLabel">
								Access to the Group
							</td>
							<td>
							</td>
						</tr>
						<tr id="pattern" style="display: none;">
							<td>
								<span id="rowUserLoginName">login name</span>
							</td>
							<td>
								<span id="rowRoleName">role name</span>
							</td>
							<td>
								<input type="button" class="noBorderButton" id="edit"
									value="Edit" onclick="editUserAccess(this.id);">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</td>
	</tr>

	<tr>
		<td class="cellLabel" width="15%">
		</td>
		<td>
			<div id="newUser" style="display: none">
				<table class="promptbox" width="85%">
					<tr>
						<td class="cellLabel" width="30%">
							User Login Name *
						</td>
						<td>
							<html:text property="group.theUserAccessibility.userBean.loginName"
								styleId="userBean.loginName" onchange=""/>
						</td>
						<td width="5">
									<a href="#userNameField"
										onclick="javascript:showMatchedUserDropdown()"><img
											src="images/icon_browse.jpg" align="middle"
											alt="search existing users" border="0" /></a>
						</td>
						<td width="50%">
							<table class="invisibleTable">
								<tr>
									<td>
										<img src="images/ajax-loader.gif" border="0" class="counts"
											id="loaderImg" style="display: none">
									</td>
									<td>
										<html:select
											property="group.theUserAccessibility.userBean.loginName"
											size="10" styleId="matchedUserNameSelect" style="display: none" onclick="updateUserLoginName()">
										</html:select>
									</td>
									<td><a id="cancelBrowse" style="display:none" href="javascript:cancelBrowseSelect()">Cancel</a></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="cellLabel" width="30%">
							Access to the Group *
						</td>
						<td colspan="2">
							<html:hidden property="group.theUserAccessibility.groupAccess" value="false"/>
							<html:select property="group.theUserAccessibility.roleDisplayName"
								styleId="roleName" onchange="">
								<option></option>
								<html:options collection="csmRoleNames" labelProperty="label" property="value"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<input class="promptButton" style="display: none;" id="deleteUser" type="button"
								value="Remove" onclick="deleteTheUserAccess()">
						</td>
						<td align="right" colspan="3">
							<div align="right">
								<input class="promptButton" type="button" value="Save" onclick="javascript:addUserAccess();" />
								<input class="promptButton" type="button" value="Cancel" onclick="clearUserAccess();closeSubmissionForm('User');">
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td><input style="display: none;" id="deleteCollaborationGroup" type="button"
				value="Remove" onclick="javascript:deleteTheCollaborationGroup()">
		<br></td>
		<td align="right" colspan="3">
			<div align="right">
				<html:submit styleId="submitButton"/>
					<input type="hidden" name="dispatch" value="create">
					<input type="hidden" name="page" value="1">
					<html:hidden styleId="groupId"	property="group.id" />
				<input type="reset" value="Cancel" onclick="javascript:hide('newCollaborationGroup');show('newCollaborationGroupLabel'); show('addCollaborationGroup');" id="resetButton">
			</div>
		</td>
	</tr>
</table>
