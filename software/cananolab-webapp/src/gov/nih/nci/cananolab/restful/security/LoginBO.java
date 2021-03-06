/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.security;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.restful.RestfulConstants;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * The LoginAction authenticates a user into the system.
 * 
 * @author pansu
 */

public class LoginBO  {
	
	private Logger logger = Logger.getLogger(LoginBO.class);
	
	public String login(String username, String password, HttpServletRequest request) {		
		
		// if not using LDAP, check if the password is the initial password
		// redirect to change password page
		Boolean useLDAP = new Boolean(PropertyUtils.getProperty(
				Constants.CANANOLAB_PROPERTY, "useLDAP"));
		
		UserBean user = new UserBean(username, password);
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME, user);
			// Invalidate the current session and create a new one
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = request.getSession(true);
			session.setAttribute("securityService", service);
			logger.debug("Is user bean null: " + (service.getUserBean() == null));
			session.setAttribute("user", service.getUserBean());
			
		} catch (Exception e) {
			logger.error("Erro while logging in user: " + username + ". " + e.getMessage());
			logger.debug(e.getMessage());
			return "Erro while logging in user: " + username  + ". " + e.getMessage();
		}
		
		return RestfulConstants.SUCCESS;
		
	}
	
	public String updatePassword(String loginId, String password, String newPassword) {

		UserBean user = new UserBean(loginId, password);
		try {
			SecurityService service = new SecurityService(
					AccessibilityBean.CSM_APP_NAME, user);
			if (user != null) {
				service.updatePassword(newPassword);				
//				ActionMessage message = new ActionMessage("message.password");
//				messages.add("message", message);
//				saveMessages(request, messages);
			}
			return RestfulConstants.SUCCESS;
		} catch (Exception e) {
//			ActionMessage msg = new ActionMessage("erros.login.failed");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveErrors(request, messages);
			logger.error("Password change failed. " + e.getMessage());
			return e.getMessage();
		}
	}

}
