package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleFunctionAction.java,v 1.9.2.1 2007-07-02 17:16:59 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.dto.function.AgentBean;
import gov.nih.nci.calab.dto.function.AgentTargetBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.function.LinkageBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
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

public class NanoparticleFunctionAction extends AbstractDispatchAction {
	/**
	 * Add or update the data to database
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		FunctionBean function = (FunctionBean) theForm.get("function");

		if (function.getId() == null || function.getId() == "") {

			function.setId((String) theForm.get("functionId"));

		}

		request.getSession().setAttribute("newFunctionCreated", "true");
		// TODO save in database
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleFunction(particleType, particleName, function);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addparticle.function");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return forward;
	}

	/**
	 * Set up the input forms for adding data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	private void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();
		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("function", new FunctionBean());
	}

	private void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		InitSessionSetup.getInstance().setAllAgentTypes(session);
		InitSessionSetup.getInstance().setAllAgentTargetTypes(session);
		InitSessionSetup.getInstance().setAllActivationMethods(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		InitSessionSetup.getInstance().setAllSpecies(session);
	}

	/**
	 * Set up the form for updating existing characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);
		String functionId = (String) theForm.get("functionId");

		SearchNanoparticleService service = new SearchNanoparticleService();
		Function aFunc = service.getFunctionBy(functionId);

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		FunctionBean function = new FunctionBean(aFunc);
		theForm.set("function", function);
		return mapping.getInputForward();
	}

	/**
	 * Prepare the form for viewing existing characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		String type = (String) request.getParameter("type");
		String linkageIndex = (String) request.getParameter("linkageIndex");
		FunctionBean function = (FunctionBean) theForm.get("function");

		if (type != null && !type.equals("") && type.equals("linkages")) {
			updateLinkages(function);
		}
		if (type != null && !type.equals("") && type.equals("agentTargets")) {
			updateAgentTargets(function, linkageIndex);
		}
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Update linkages
	 * 
	 * @param function
	 */
	private void updateLinkages(FunctionBean function) {
		int linkageNum = 0;
		if (function.getNumberOfLinkages().length() > 0) {
			linkageNum = new Integer(function.getNumberOfLinkages());
		}
		List<LinkageBean> origLinkages = function.getLinkages();
		int origNum = (origLinkages == null) ? 0 : origLinkages.size();
		List<LinkageBean> linkages = new ArrayList<LinkageBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < linkageNum; i++) {
				LinkageBean linkage = new LinkageBean();
				linkages.add(linkage);
			}
		}
		// use keep original linkage info
		else if (linkageNum <= origNum) {
			for (int i = 0; i < linkageNum; i++) {
				linkages.add((LinkageBean) origLinkages.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				linkages.add((LinkageBean) origLinkages.get(i));
			}
			for (int i = origNum; i < linkageNum; i++) {
				linkages.add(new LinkageBean());
			}
		}
		function.setLinkages(linkages);
	}

	/**
	 * Update agent targets for a given agent of a linkage
	 * 
	 * @param function
	 * @param linkageIndex
	 */
	private void updateAgentTargets(FunctionBean function, String linkageIndex) {
		int linkageInd = (new Integer(linkageIndex)).intValue();
		AgentBean agent = function.getLinkages().get(linkageInd).getAgent();
		int targetNum = 0;
		if (agent.getNumberOfAgentTargets().length() > 0) {
			targetNum = (new Integer(agent.getNumberOfAgentTargets()))
					.intValue();
		}
		List<AgentTargetBean> origTargets = agent.getAgentTargets();
		int origNum = (origTargets == null) ? 0 : origTargets.size();
		List<AgentTargetBean> targets = new ArrayList<AgentTargetBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < targetNum; i++) {
				AgentTargetBean target = new AgentTargetBean();
				targets.add(target);
			}
		}
		// use keep original agent target info
		else if (targetNum <= origNum) {
			for (int i = 0; i < targetNum; i++) {
				targets.add((AgentTargetBean) origTargets.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				targets.add((AgentTargetBean) origTargets.get(i));
			}
			for (int i = origNum; i < targetNum; i++) {
				targets.add(new AgentTargetBean());
			}
		}
		agent.setAgentTargets(targets);
	}

	public boolean loginRequired() {
		return true;
	}
}