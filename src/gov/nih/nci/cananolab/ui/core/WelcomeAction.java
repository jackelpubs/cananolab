/*
 The caNanoLab Software License, Version 1.4

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.core;

/**
 * This class calls the Struts ForwardAction to forward to a page, also extends
 * AbstractBaseAction to inherit the user authentication features.
 *
 * @author pansu
 */

/* CVS $Id: WelcomeAction.java,v 1.1 2008-09-30 18:44:48 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.ui.sample.InitCharacterizationSetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;
import org.apache.struts.util.LabelValueBean;

public class WelcomeAction extends ForwardAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		saveToken(request); // save token to avoid back and refresh on the login
		// page.
		InitSetup.getInstance().getGridNodesInContext(request);
		InitSetup.getInstance().getPublicDataCountsInContext(request);

		// TODO remove tmp code for wireframes
		List<LabelValueBean> physChars = InitCharacterizationSetup
				.getInstance().getDecoratedCharNamesByCharType(request,
						Constants.PHYSICOCHEMICAL_CHARACTERIZATION);
		request.getSession().setAttribute("physicoChars", physChars);
		List<LabelValueBean> invitroChars = InitCharacterizationSetup
				.getInstance().getDecoratedCharNamesByCharType(request,
						Constants.INVITRO_CHARACTERIZATION);
		List<LabelValueBean> invivoChars = InitCharacterizationSetup
				.getInstance().getDecoratedCharNamesByCharType(request,
						Constants.INVIVO_CHARACTERIZATION);
		request.getSession().setAttribute("physicoChars", physChars);

		request.getSession().setAttribute("physicoChars", physChars);
		request.getSession().setAttribute("invitroChars", invitroChars);
		request.getSession().setAttribute("invivoChars", invivoChars);
		return super.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return true;
	}
}
