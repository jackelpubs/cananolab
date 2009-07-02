package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

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

/**
 * This class allows users to submit chemical association data under sample
 * composition.
 *
 * @author pansu
 */
public class ChemicalAssociationAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");

		if (!validateAssociationFile(request, assocBean)) {
			return mapping.getInputForward();
		}

		ActionMessages msgs = new ActionMessages();
		if (!validateAssociatedElements(assocBean)) {
			ActionMessage msg = new ActionMessage(
					"error.duplicateAssociatedElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return mapping.getInputForward();
		}

		saveAssociation(request, theForm, assocBean);
		ActionMessage msg = new ActionMessage("message.addChemicalAssociation");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect chemical association after returning to the summary page
		request.getSession().setAttribute("onloadJavascript",
				"showSummary('3', 4)");
		return mapping.findForward("success");
	}

	private boolean validateAssociationFile(HttpServletRequest request,
			ChemicalAssociationBean entityBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, msgs, filebean)) {
				return false;
			}
		}
		return true;
	}

	private boolean validateAssociatedElements(ChemicalAssociationBean assocBean)
			throws Exception {
		// validate if composing element is null
		// if (assocBean.getAssociatedElementA().getComposingElement().getId()
		// == null) {
		// ActionMessage msg = new ActionMessage(
		// "error.nullComposingElementAInAssociation");
		// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// saveErrors(request, msgs);
		// noErrors = false;
		// }
		// if (assocBean.getAssociatedElementB().getDomainElement() instanceof
		// ComposingElement
		// && assocBean.getAssociatedElementB().getDomainElement().getId() ==
		// null) {
		// ActionMessage msg = new ActionMessage(
		// "error.nullComposingElementBInAssociation");
		// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// saveErrors(request, msgs);
		// noErrors = false;
		// }
		// if (!noErrors) {
		// return mapping.getInputForward();
		// }
		// validate if the same associated elements are chosen on both sides
		boolean noErrors = true;
		String entityTypeA = assocBean.getAssociatedElementA()
				.getEntityDisplayName();
		String entityIdA = assocBean.getAssociatedElementA().getEntityId();
		String entityTypeB = assocBean.getAssociatedElementB()
				.getEntityDisplayName();
		String entityIdB = assocBean.getAssociatedElementB().getEntityId();
		if (entityTypeA.equals(entityTypeB) && entityIdA.equals(entityIdB)) {
			noErrors = false;
		}
		return noErrors;
	}

	public void saveAssociation(HttpServletRequest request,
			DynaValidatorForm theForm, ChemicalAssociationBean assocBean)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Chemical Association");
		try {
			assocBean.setupDomainAssociation(InitSetup.getInstance()
					.getDisplayNameToClassNameLookup(
							request.getSession().getServletContext()), user
					.getLoginName(), internalUriPath);
		} catch (ClassCastException ex) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = null;
			if (ex.getMessage() != null && ex.getMessage().length() > 0
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msg = new ActionMessage("errors.invalidOtherType", ex
						.getMessage(), "Chemical Association");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", assocBean
						.getType(), "Chemical Association");
				assocBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
		}
		CompositionService compService = new CompositionServiceLocalImpl();
		compService.saveChemicalAssociation(sampleBean, assocBean, user);

		Boolean hasFunctionalizingEntity = (Boolean) request.getSession()
				.getAttribute("hasFunctionalizingEntity");
		InitCompositionSetup.getInstance().persistChemicalAssociationDropdowns(
				request, assocBean, hasFunctionalizingEntity);
	}

	/**
	 * Set up the input form for adding new chemical association
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleId = theForm.getString("sampleId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CompositionService service = new CompositionServiceLocalImpl();
		CompositionBean compositionBean = service.findCompositionBySampleId(
				sampleId, user);
		// if composition doesn't have required information, return to summary
		// view page
		if (!validateComposition(compositionBean, request)) {
			return mapping.findForward("success");
		}
		request.getSession().removeAttribute("compositionForm");
		setLookups(request, compositionBean);
		return mapping.findForward("setup");
	}

	public boolean validateComposition(CompositionBean compositionBean,
			HttpServletRequest request) throws Exception {
		ActionMessages msgs = new ActionMessages();
		// if no composition return to summary view page
		if (compositionBean == null) {
			ActionMessage msg = new ActionMessage("message.nullComposition");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return false;
		}
		// check if sample has the required nanomaterial entities
		if (compositionBean.getNanomaterialEntities().isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"message.emptyMaterialsEntitiesInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return false;
		}
		// check whether nanomaterial entities has composing elements
		int numberOfCE = 0;
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			if (!entityBean.getComposingElements().isEmpty()) {
				numberOfCE += entityBean.getComposingElements().size();
			}
		}
		if (numberOfCE == 0) {
			ActionMessage msg = new ActionMessage(
					"message.emptyComposingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return false;
		}
		// check whether it has a functionalizing entity
		boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getFunctionalizingEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		if (!hasFunctionalizingEntity && numberOfCE == 1) {
			ActionMessage msg = new ActionMessage(
					"message.oneComposingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return false;
		}
		if (!hasFunctionalizingEntity) {
			ActionMessage msg = new ActionMessage(
					"message.emptyFunctionalizingEntityInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return false;
		}
		return true;
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleId = theForm.getString("sampleId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CompositionService service = new CompositionServiceLocalImpl();
		CompositionBean compositionBean = service.findCompositionBySampleId(
				sampleId, user);
		setLookups(request, compositionBean);
		return mapping.findForward("setup");
	}

	private void setLookups(HttpServletRequest request,
			CompositionBean compositionBean) throws Exception {
		// check whether it has a functionalizing entity
		boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getNanomaterialEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		// set entity type and association type
		HttpSession session = request.getSession();
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			entityBean.setType(InitSetup.getInstance().getDisplayName(
					entityBean.getClassName(), session.getServletContext()));
		}
		for (FunctionalizingEntityBean entityBean : compositionBean
				.getFunctionalizingEntities()) {
			entityBean.setType(InitSetup.getInstance().getDisplayName(
					entityBean.getClassName(), session.getServletContext()));
		}
		for (ChemicalAssociationBean assocBean : compositionBean
				.getChemicalAssociations()) {
			assocBean.setType(InitSetup.getInstance().getDisplayName(
					assocBean.getClassName(), session.getServletContext()));
		}
		// use BaseCompositionEntityBean for DWR ajax
		List<BaseCompositionEntityBean> materialEntities = new ArrayList<BaseCompositionEntityBean>();
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			if (!entityBean.getComposingElements().isEmpty()) {
				materialEntities.add(entityBean);
			}
		}
		request.getSession().setAttribute("sampleMaterialEntities",
				materialEntities);
		request.getSession().setAttribute("sampleFunctionalizingEntities",
				compositionBean.getFunctionalizingEntities());
		request.getSession().setAttribute("hasFunctionalizingEntity",
				hasFunctionalizingEntity);
	}

	public void prepareEntityLists(ChemicalAssociationBean assocBean,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		CompositionService service = new CompositionServiceLocalImpl();
		// associated element A
		List<BaseCompositionEntityBean> entityListA = null;
		List<ComposingElementBean> ceListA = new ArrayList<ComposingElementBean>();
		if (assocBean.getAssociatedElementA().getCompositionType().equals(
				"Nanomaterial Entity")) {
			entityListA = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleMaterialEntities"));
			// get composing elements
			NanomaterialEntityBean entityBean = service
					.findNanomaterialEntityById(assocBean
							.getAssociatedElementA().getEntityId(), user);
			ceListA = entityBean.getComposingElements();
		} else {
			entityListA = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleFunctionalizingEntities"));
		}
		session.setAttribute("ceListA", ceListA);
		session.setAttribute("entityListA", entityListA);
		// associated element B
		List<BaseCompositionEntityBean> entityListB = null;
		List<ComposingElementBean> ceListB = new ArrayList<ComposingElementBean>();
		if (assocBean.getAssociatedElementB().getCompositionType().equals(
				"Nanomaterial Entity")) {
			entityListB = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleMaterialEntities"));
			// get composing elements
			NanomaterialEntityBean entityBean = service
					.findNanomaterialEntityById(assocBean
							.getAssociatedElementB().getEntityId(), user);
			ceListB = entityBean.getComposingElements();
		} else {
			entityListB = new ArrayList<BaseCompositionEntityBean>(
					(List<BaseCompositionEntityBean>) session
							.getAttribute("sampleFunctionalizingEntities"));
		}
		session.setAttribute("ceListB", ceListB);
		session.setAttribute("entityListB", entityListB);
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		// set up compositionBean required to set up drop-down
		String sampleId = theForm.getString("sampleId");
		CompositionService compService = new CompositionServiceLocalImpl();
		CompositionBean compositionBean = compService
				.findCompositionBySampleId(sampleId, user);
		setLookups(request, compositionBean);
		String assocId = request.getParameter("dataId");
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssociationById(assocId, user);
		assocBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		prepareEntityLists(assocBean, request);
		theForm.set("assoc", assocBean);

		return mapping.getInputForward();
	}

	public ActionForward saveFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assoc = (ChemicalAssociationBean) theForm
				.get("assoc");
		FileBean theFile = assoc.getTheFile();
		assoc.addFile(theFile);
		// save the association
		saveAssociation(request, theForm, assoc);
		request.setAttribute("anchor", "file");
		return mapping.getInputForward();
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assoc = (ChemicalAssociationBean) theForm
				.get("assoc");
		FileBean theFile = assoc.getTheFile();
		assoc.removeFile(theFile);
		assoc.setTheFile(new FileBean());
		request.setAttribute("anchor", "file");
		// save the association
		saveAssociation(request, theForm, assoc);
		return mapping.getInputForward();
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Chemical Association");
		assocBean.setupDomainAssociation(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName(), internalUriPath);

		CompositionService compService = new CompositionServiceLocalImpl();
		compService.deleteChemicalAssociation(assocBean.getDomainAssociation(),
				user);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteChemicalAssociation");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		return mapping.findForward("success");
	}
}
