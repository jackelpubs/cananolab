package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.exception.NoAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public abstract class AbstractDispatchAction extends DispatchAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		// response.setHeader("Cache-Control", "no-cache");

		if (isCancelled(request))
			return mapping.findForward("cancel");

		if (!loginRequired() || loginRequired() && isUserLoggedIn(request)) {
			if (loginRequired()) {
				String dispatch = request.getParameter("dispatch");
				// check whether user have access to the class
				boolean accessStatus = canUserExecute(request.getSession());
				//if have access or if have no access but dispatch is either setupView or download
				//do forward
				if (accessStatus
						|| !accessStatus
						&& dispatch != null
						&& (dispatch.equals("setupView") || dispatch
								.equals("download"))) {
					forward = super.execute(mapping, form, request, response);
				} else {
					request.getSession().removeAttribute("user");
					throw new NoAccessException(
							"You don't have access to class: "
									+ this.getClass().getName());
				}
			}

		} else {
			throw new InvalidSessionException();
		}
		return forward;
	}

	/**
	 * 
	 * @param request
	 * @return whether the user is successfully logged in.
	 */
	private boolean isUserLoggedIn(HttpServletRequest request) {
		boolean isLoggedIn = false;
		if (request.getSession().getAttribute("user") != null) {
			isLoggedIn = true;
		}
		return isLoggedIn;
	}

	public abstract boolean loginRequired();

	/**
	 * Check whether the current user in the session can perform the action
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public boolean canUserExecute(HttpSession session) throws Exception {
		return InitSessionSetup.getInstance().canUserExecuteClass(session,
				this.getClass());
	}
}