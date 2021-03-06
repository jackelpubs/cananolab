/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * This action logs user out of the current session
 *
 * @author pansn
 */

public class LogoutBO {
	public String logout(HttpServletRequest request) {
	
		HttpSession session = request.getSession(false);
		if (session != null) {
			// invalidate the old one
			session.invalidate();
		}
//		ActionMessages msgs = new ActionMessages();
//		ActionMessage message = new ActionMessage("msg.logout");
//		msgs.add(ActionMessages.GLOBAL_MESSAGE, message);
//		saveMessages(request, msgs);
//
//		forward = mapping.findForward("success");
//		resetToken(request);
//
//		//FR# 26489,Visitor Count: set marker in request to avoid counting.
//		request.setAttribute("justLogout", Boolean.TRUE);
		return "Logout successful";
	}
}
