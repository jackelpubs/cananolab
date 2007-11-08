package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up input form for Solubility characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSolubilityAction.java,v 1.2 2007-11-08 20:41:34 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.service.particle.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleSolubilityAction extends BaseCharacterizationAction {
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
		CharacterizationBean charBean = super.prepareCreate(request, theForm);
		SolubilityBean propBean = (SolubilityBean) theForm.get("solubility");
		SolubilityBean solubilityBean = new SolubilityBean(propBean, charBean);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleSolubility(solubilityBean);
		CharacterizationBean[] otherChars = super.prepareCopy(request, theForm,
				service);
		for (CharacterizationBean acharBean : otherChars) {
			SolubilityBean aSolubilityBean = new SolubilityBean(propBean,
					acharBean);
			service.addParticleSolubility(aSolubilityBean);
		}
		super.postCreate(request, theForm);
		request.getSession().setAttribute("newSolubilityCreated", "true");
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleSolubility");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}
}
