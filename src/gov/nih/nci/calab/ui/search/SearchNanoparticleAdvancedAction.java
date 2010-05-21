package gov.nih.nci.calab.ui.search;

/**
 * This class searches nanoparticle metadata based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchNanoparticleAdvancedAction.java,v 1.5 2007-02-28 21:55:09 pansu Exp $ */

import gov.nih.nci.calab.dto.common.SearchableBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchNanoparticleAdvancedAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;

		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		SearchableBean size1 = (SearchableBean) theForm.get("size1");
		SearchableBean size2 = (SearchableBean) theForm.get("size2");
		SearchableBean molecularWeight = (SearchableBean) theForm
				.get("molecularWeight");
		SearchableBean cellViability = (SearchableBean) theForm
				.get("cellViability");

		SearchNanoparticleService searchParticleService = new SearchNanoparticleService();
		List<SearchableBean> searchCriteria = new ArrayList<SearchableBean>();
		// don't search if value range is empty
		if (size1.getLowValue().length() > 0
				&& !size1.getLowValue().equals("0")
				|| size1.getHighValue().length() > 0
				&& !size1.getHighValue().equals("0"))
			searchCriteria.add(size1);
		if (size2.getLowValue().length() > 0
				&& !size2.getLowValue().equals("0")
				|| size2.getHighValue().length() > 0
				&& !size2.getHighValue().equals("0"))
			searchCriteria.add(size2);
		if (molecularWeight.getLowValue().length() > 0
				&& !molecularWeight.getLowValue().equals("0")
				|| molecularWeight.getHighValue().length() > 0
				&& !molecularWeight.getHighValue().equals("0"))
			searchCriteria.add(molecularWeight);
		if (cellViability.getLowValue().length() > 0
				&& !cellViability.getLowValue().equals("0")
				|| cellViability.getHighValue().length() > 0
				&& !cellViability.getHighValue().equals("0"))
			searchCriteria.add(cellViability);

		List<ParticleBean> particles = searchParticleService.advancedSearch(
				particleType, functionTypes, searchCriteria, user);

		if (particles != null && !particles.isEmpty()) {
			request.setAttribute("particles", particles);
			forward = mapping.findForward("success");
		} else {

			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchNanoparticleAdvanced.noresult");
			msgs.add("message", msg);
			saveMessages(request, msgs);

			forward = mapping.getInputForward();
		}

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllParticleFunctionTypes(session);
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}