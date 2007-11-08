package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up the nanoparticle general information page and allows users to submit/update
 * the general information.  
 *  
 * @author pansu
 */

/* CVS $Id: RemoteNanoparticleGeneralInfoAction.java,v 1.2 2007-11-08 20:41:34 pansu Exp $ */

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.report.SearchReportService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteNanoparticleGeneralInfoAction extends AbstractDispatchAction {

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = theForm.getString("particleName");
		String gridNodeHost = request.getParameter("gridNodeHost");
		GridNodeBean gridNode = gridNodeMap.get(gridNodeHost);

		InitParticleSetup.getInstance().setRemoteSideParticleMenu(request,
				particleName, gridNode);

		ActionForward forward = mapping.findForward("success");
		// force data refresh on the side menu
		request.getSession().setAttribute("newRemoteParticleCreated", "true");
		return forward;
	}

	public ActionForward viewDisclaimer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String particleType = (String) theForm.get("particleType");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		SearchReportService searchReportService = new SearchReportService();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Collection<LabFileBean> reports = searchReportService
				.getReportByParticle(particleName, particleType, user);
		request.setAttribute("particleReports", reports);
		forward = mapping.findForward("viewDisclaimer");
		return forward;
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return true;
	}
}
