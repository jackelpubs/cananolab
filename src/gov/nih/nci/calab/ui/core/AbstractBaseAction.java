/*
 The caLAB Software License, Version 0.5

 Copyright 2006 SAIC. This software was developed in conjunction with the National 
 Cancer Institute, and so to the extent government employees are co-authors, any 
 rights in such works shall be subject to Title 17 of the United States Code, 
 section 105.

 */
package gov.nih.nci.calab.ui.core;

/**
 * This abstract class is the basis for all other action classes to extend in
 * caLAB. It includes common operations need to be included in all caLAB actions
 * to reduce redundancy.
 * 
 * @author pansu
 */

/* CVS $Id: AbstractBaseAction.java,v 1.11 2006-08-02 16:38:10 pansu Exp $ */

import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.exception.NoAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractBaseAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		if (!loginRequired() || loginRequired() && isUserLoggedIn(request)) {
			if (loginRequired()) {
				boolean accessStatus = canUserExecute(request.getSession());
				if (!accessStatus) {
					throw new NoAccessException(
							"You don't have access to class: "
									+ this.getClass().getName());
				}
			}
			forward = executeTask(mapping, form, request, response);
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

	/**
	 * Provide implementation of this abstract method in each subclass.
	 */
	public abstract ActionForward executeTask(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * Provide implementation of the abstract method in each subclass.
	 */
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