package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.ExperimentConfigServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationResultServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
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
 * Base action for characterization actions
 *
 * @author pansu
 *
 */
public class CharacterizationAction extends BaseAnnotationAction {
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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		if (charBean.getAssayCategory().equals(
				"Phsico-Chemical Characterization")) {
			InitCharacterizationSetup.getInstance()
					.persistCharacterizationDropdowns(request, charBean);
			InitCharacterizationSetup
					.getInstance()
					.persistPhysicalCharacterizationDropdowns(request, charBean);
		} else if (charBean.getAssayCategory().equals(
				"Invitro Characterization")) {
			InitCharacterizationSetup.getInstance()
					.persistCharacterizationDropdowns(request, charBean);
			InitCharacterizationSetup.getInstance()
					.persistInvitroCharacterizationDropdowns(request, charBean);
		}
		// TODO::
		// if (!validateDerivedDatum(request, charBean)) {
		// return mapping.getInputForward();
		// }

		saveCharacterization(request, theForm, charBean);

		ActionMessages msgs = new ActionMessages();
		// validate number by javascript filterFloatingNumber
		// validateNumber(request, charBean, msgs);
		ActionMessage msg = new ActionMessage("message.addCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	/**
	 * Set up the input form for adding new characterization
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
		clearForm(theForm);
		// characterizationForm is either physicalCharacterizationForm or
		// invitroCharacterizationForm to use in bodyCharacterization.jsp
		request.getSession().setAttribute("characterizationForm", theForm);
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		// setup other particles from the same source
		this.setOtherParticlesFromTheSameSource("local", request, particleBean,
				user);

		// set charclass
		ServletContext appContext = request.getSession().getServletContext();
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		String submitType = request.getParameter("submitType");
		String charClass = InitSetup.getInstance().getClassName(submitType,
				appContext);
		charBean.setClassName(charClass);
		setLookups(request, charBean);
		return mapping.getInputForward();
	}

	private String setupDetailPage(CharacterizationBean charBean) {
		String includePage = null;
		if (charBean.getClassName().equals("PhysicalState")
				|| charBean.getClassName().equals("Shape")
				|| charBean.getClassName().equals("Solubility")
				|| charBean.getClassName().equals("Surface")) {
			includePage = "/particle/characterization/physical/body"
					+ charBean.getClassName() + "Info.jsp";
		} else if (charBean.getClassName().equals("Cytotoxicity")) {
			includePage = "/particle/characterization/invitro/body"
					+ charBean.getClassName() + "Info.jsp";
		}
		return includePage;
	}

	private void saveToOtherParticles(HttpServletRequest request,
			Characterization copy, UserBean user, String particleSampleName,
			NanoparticleSample[] otherSamples) throws Exception {
		FileService fileService = new FileServiceLocalImpl();
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		for (NanoparticleSample sample : otherSamples) {
			charService.saveCharacterization(sample, copy);
			// update copied filename and save content and set visibility
			// TODO
			// if (copy.getDerivedBioAssayDataCollection() != null) {
			// for (DerivedBioAssayData bioassay : copy
			// .getDerivedBioAssayDataCollection()) {
			// if (bioassay.getFile() != null) {
			// fileService.saveCopiedFileAndSetVisibility(bioassay
			// .getFile(), user, particleSampleName, sample
			// .getName());
			// }
			// }
			// }
		}
	}

	private void setupDomainChar(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean)
			throws Exception {
		// setup domainFile for fileBeans
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
				+ "/"
				+ StringUtils.getOneWordLowerCaseFirstLetter(InitSetup
						.getInstance().getDisplayName(charBean.getClassName(),
								request.getSession().getServletContext()));
		charBean.setupDomainChar(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName(), internalUriPath);
	}

	//TODO for datum and condition
	// protected boolean validateDerivedDatum(HttpServletRequest request,
	// CharacterizationBean charBean) throws Exception {
	//
	// ActionMessages msgs = new ActionMessages();
	// boolean noErrors = true;
	// for (DerivedBioAssayDataBean derivedBioassayDataBean : charBean
	// .getDerivedBioAssayDataList()) {
	//
	// List<DerivedDatumBean> datumList = derivedBioassayDataBean
	// .getDatumList();
	// FileBean lfBean = derivedBioassayDataBean.getFileBean();
	//
	// // error, if no data input from either the lab file or derived datum
	// boolean noFileError = true;
	// if (datumList == null || datumList.size() == 0) {
	// noFileError = validateFileBean(request, msgs, lfBean);
	// if (!noFileError) {
	// ActionMessage msg = new ActionMessage("errors.required",
	// "If no derived datum entered, the file data");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// this.saveErrors(request, msgs);
	// noErrors = false;
	// }
	// }
	//
	// for (DerivedDatumBean datum : datumList) {
	// // if value field is populated, so does the name field.
	// if (datum.getDomainDerivedDatum().getName().length() == 0) {
	// ActionMessage msg = new ActionMessage("errors.required",
	// "Derived data name");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// this.saveErrors(request, msgs);
	// noErrors = false;
	// }
	// try {
	// Float value = new Float(datum.getValueStr());
	// // for boolean type, the value must be 0/1
	// if (datum.getDomainDerivedDatum().getValueType()
	// .equalsIgnoreCase("boolean")
	// && value != 0.0 && value != 1.0) {
	// ActionMessage msg = new ActionMessage(
	// "error.booleanValue", "Derived data value");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// saveErrors(request, msgs);
	// noErrors = false;
	// }
	// } catch (NumberFormatException e) {
	// // for boolean type, the value must be true/false
	// if (datum.getDomainDerivedDatum().getValueType()
	// .equalsIgnoreCase("boolean")
	// && !datum.getValueStr().equalsIgnoreCase("true")
	// && !datum.getValueStr().equalsIgnoreCase("false")) {
	// ActionMessage msg = new ActionMessage(
	// "error.booleanValue", "Derived data value");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// saveErrors(request, msgs);
	// noErrors = false;
	// } else if (!datum.getDomainDerivedDatum().getValueType()
	// .equalsIgnoreCase("boolean")) {
	// ActionMessage msg = new ActionMessage(
	// "error.derivedDatumValue", "Derived data value");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// saveErrors(request, msgs);
	// noErrors = false;
	// }
	// }
	// }
	// }
	// return noErrors;
	// }

	private void saveCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean)
			throws Exception {
		// setup domainFile for fileBeans
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		setupDomainChar(request, theForm, charBean);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		charService.saveCharacterization(
				particleBean.getDomainParticleSample(), charBean
						.getDomainChar());

		// set public visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = authService.getAccessibleGroups(
				particleBean.getDomainParticleSample().getName(),
				CaNanoLabConstants.CSM_READ_PRIVILEGE);
		if (accessibleGroups != null
				&& accessibleGroups
						.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
			charService.assignPublicVisibility(authService, charBean
					.getDomainChar());
		}

		// save file data to file system and set visibility
		List<FileBean> files = new ArrayList<FileBean>();
		// TODO::
		// for (DerivedBioAssayDataBean bioassay : charBean
		// .getDerivedBioAssayDataList()) {
		// if (bioassay.getFileBean() != null) {
		// files.add(bioassay.getFileBean());
		// }
		// }
		saveFilesToFileSystem(files);

		// save to other particles
		NanoparticleSample[] otherSamples = prepareCopy(request, theForm);
		if (otherSamples != null) {
			Boolean copyData = (Boolean) theForm.get("copyData");
			Characterization copy = charBean.getDomainCopy(copyData);
			saveToOtherParticles(request, copy, user, particleBean
					.getDomainParticleSample().getName(), otherSamples);
		}
		particleBean = setupParticle(theForm, request, "local");
		setupDataTree(particleBean, request);
	}

	private void deleteCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean,
			String createdBy) throws Exception {
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
				+ "/"
				+ StringUtils.getOneWordLowerCaseFirstLetter(InitSetup
						.getInstance().getDisplayName(charBean.getClassName(),
								request.getSession().getServletContext()));
		charBean.setupDomainChar(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), createdBy,
				internalUriPath);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		charService.deleteCharacterization(charBean.getDomainChar());
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		deleteCharacterization(request, theForm, charBean, user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		String particleId = theForm.getString("particleId");
		NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
		ParticleBean particleBean = sampleService
				.findNanoparticleSampleById(particleId);
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		return forward;
	}

	private void clearForm(DynaValidatorForm theForm) {
		theForm.set("achar", new CharacterizationBean());
	}

	private void setLookups(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		InitNanoparticleSetup.getInstance().setSharedDropdowns(request);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, charBean.getClassName());
		InitExperimentConfigSetup.getInstance().setExperimentConfigDropDowns(
				request);
		InitProtocolSetup.getInstance().getProtocolFilesByChar(request,
				charBean);
		InitCharacterizationSetup.getInstance()
				.setPhysicalCharacterizationDropdowns(request);
		InitCharacterizationSetup.getInstance()
				.setInvitroCharacterizationDropdowns(request);

		String detailPage = setupDetailPage(charBean);
		request.getSession().setAttribute("characterizationDetailPage",
				detailPage);
	}

	public void validateNumber(HttpServletRequest request,
			CharacterizationBean charBean, ActionMessages msgs)
			throws Exception {
		if (charBean.getSolubility().getCriticalConcentration() == 0.0) {
			ActionMessage msg = new ActionMessage("message.invalidNumber");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		}
	}

	private CharacterizationBean getCharacterizationBean(
			DynaValidatorForm theForm, Characterization chara, UserBean user,
			String location) throws Exception {
		CharacterizationBean charBean = new CharacterizationBean(chara);
		if (location.equals("local")) {
			// set file visibility
			NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
			charService.retrieveVisiblity(charBean, user);
		}
		theForm.set("achar", charBean);
		return charBean;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request, "local");

		request.getSession().setAttribute("characterizationForm", theForm);
		return mapping.findForward("summaryView");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request, "local");

		request.getSession().setAttribute("characterizationForm", theForm);
		Characterization chara = prepareCharacterization(theForm, request,
				"local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = getCharacterizationBean(theForm, chara,
				user, "local");
		setLookups(request, charBean);
		this.setOtherParticlesFromTheSameSource("local", request, particleBean,
				user);
		// clear copy to otherParticles
		theForm.set("otherParticles", new String[0]);
		theForm.set("copyData", false);

		return mapping.findForward("summaryView");
	}

	// used in many dispatch methods
	private Characterization prepareCharacterization(DynaValidatorForm theForm,
			HttpServletRequest request, String location) throws Exception {
		String charId = request.getParameter("dataId");
		String charClass = request.getParameter("dataClassName");
		NanoparticleCharacterizationService charService = null;
		if (location.equals("local")) {
			charService = new NanoparticleCharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			charService = new NanoparticleCharacterizationServiceRemoteImpl(
					serviceUrl);
		}
		Characterization chara = charService.findCharacterizationById(charId,
				charClass);
		request.getSession().setAttribute("characterizationForm", theForm);
		return chara;
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		setupParticle(theForm, request, location);
		Characterization chara = prepareCharacterization(theForm, request,
				location);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getCharacterizationBean(theForm, chara, user, location);
		String particleId = request.getParameter("particleId");
		String characterizationId = request.getParameter("dataId");
		String className = request.getParameter("dataClassName");
		String submitType = request.getParameter("submitType");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl
				+ "?page=0&dispatch=printDetailView&particleId=" + particleId
				+ "&dataId=" + characterizationId + "&dataClassName="
				+ className + "&submitType=" + submitType + "&location="
				+ location;
		request.getSession().setAttribute("printDetailViewLinkURL",
				printLinkURL);
		return mapping.findForward("detailView");
	}

	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		Characterization chara = prepareCharacterization(theForm, request,
				location);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		getCharacterizationBean(theForm, chara, user, location);
		return mapping.findForward("detailPrintView");
	}

	private void setCharacterizationFileFullPath(HttpServletRequest request,
			CharacterizationBean charBean, String location) throws Exception {
		if (location.equals("local")) {
			// TODO::
			// set file full path
			// for (DerivedBioAssayDataBean bioassayBean : charBean
			// .getDerivedBioAssayDataList()) {
			// if (bioassayBean.getFileBean() != null) {
			// FileBean fileBean = bioassayBean.getFileBean();
			// if (!fileBean.getDomainFile().getUriExternal()) {
			// String fileRoot = PropertyReader.getProperty(
			// CaNanoLabConstants.FILEUPLOAD_PROPERTY,
			// "fileRepositoryDir");
			// fileBean.setFullPath(fileRoot + File.separator
			// + fileBean.getDomainFile().getUri());
			// } else {
			// fileBean.setFullPath(fileBean.getDomainFile().getUri());
			// }
			// }
			// }
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);

			URL localURL = new URL(request.getRequestURL().toString());
			String actionPath = localURL.getPath();
			URL remoteUrl = new URL(serviceUrl);
			String remoteServerHostUrl = remoteUrl.getProtocol() + "://"
					+ remoteUrl.getHost() + ":" + remoteUrl.getPort();
			String remoteDownloadUrlBase = remoteServerHostUrl + actionPath
					+ "?dispatch=download&location=local&fileId=";
			// TODO::
			// for (DerivedBioAssayDataBean bioassayBean : charBean
			// .getDerivedBioAssayDataList()) {
			// if (bioassayBean.getFileBean() != null) {
			// FileBean fileBean = bioassayBean.getFileBean();
			// String remoteDownloadUrl = remoteDownloadUrlBase
			// + fileBean.getDomainFile().getId().toString();
			// fileBean.setFullPath(remoteDownloadUrl);
			// }
			// }
		}
	}

	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		ParticleBean particleBean = setupParticle(theForm, request, location);
		Characterization chara = prepareCharacterization(theForm, request,
				location);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = getCharacterizationBean(theForm, chara,
				user, location);
		String fileName = this.getExportFileName(particleBean
				.getDomainParticleSample().getName(), "detailView", charBean
				.getClassName());
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename=\""
				+ fileName + ".xls\"");
		NanoparticleCharacterizationService service = null;
		setCharacterizationFileFullPath(request, charBean, location);
		if (location.equals("local")) {
			service = new NanoparticleCharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new NanoparticleCharacterizationServiceRemoteImpl(
					serviceUrl);
		}
		service.exportDetail(charBean, response.getOutputStream());

		return null;
	}

	private CharacterizationSummaryBean setupCharSummary(
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {

		String submitType = request.getParameter("submitType");
		String className = InitSetup.getInstance().getClassName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();
		String location = request.getParameter("location");
		ParticleBean particleBean = setupParticle(theForm, request, location);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCharacterizationService service = null;
		if (location.equals("local")) {
			service = new NanoparticleCharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new NanoparticleCharacterizationServiceRemoteImpl(
					serviceUrl);
		}
		CharacterizationSummaryBean charSummary = service
				.getParticleCharacterizationSummaryByClass(particleBean
						.getDomainParticleSample().getName(), fullClassName,
						user);
		request.setAttribute("charSummary", charSummary);
		return charSummary;

	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		//setupCharSummary(theForm, request);

		String location = request.getParameter("location");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl + "?page=0&particleId=" + particleId
				+ "&submitType=" + "&dispatch=printSummaryView" + "&location="
				+ location;
		String printAllLinkURL = requestUrl + "?page=0&particleId="
				+ particleId + "&dispatch=printFullSummaryView" + "&location="
				+ location;
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		request.getSession().setAttribute("printFullSummaryViewLinkURL",
				printAllLinkURL);
		return mapping.findForward("summaryView");
	}

	public ActionForward printSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupCharSummary(theForm, request);
		return mapping.findForward("summaryPrintView");
	}

	public ActionForward printFullSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupCharSummary(theForm, request);
		return mapping.findForward("summaryPrintAllView");
	}

	public ActionForward exportSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		ParticleBean particleBean = setupParticle(theForm, request, location);
		CharacterizationSummaryBean charSummaryBean = setupCharSummary(theForm,
				request);
		String fileName = getExportFileName(particleBean
				.getDomainParticleSample().getName(), "summaryView",
				charSummaryBean.getCharBeans().get(0).getClassName());
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename=\""
				+ fileName + ".xls\"");
		NanoparticleCharacterizationService service = null;
		if (location.equals("local")) {
			service = new NanoparticleCharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new NanoparticleCharacterizationServiceRemoteImpl(
					serviceUrl);
		}
		service.exportSummary(charSummaryBean, response.getOutputStream());
		return null;
	}

	public ActionForward exportFullSummary(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		ParticleBean particleBean = setupParticle(theForm, request, location);
		CharacterizationSummaryBean charSummaryBean = setupCharSummary(theForm,
				request);
		String fileName = getExportFileName(particleBean
				.getDomainParticleSample().getName(), "summaryView",
				charSummaryBean.getCharBeans().get(0).getClassName());
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename=\""
				+ fileName + ".xls\"");
		NanoparticleCharacterizationService service = null;
		for (CharacterizationBean charBean : charSummaryBean.getCharBeans()) {
			setCharacterizationFileFullPath(request, charBean, location);
		}
		if (location.equals("local")) {
			service = new NanoparticleCharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new NanoparticleCharacterizationServiceRemoteImpl(
					serviceUrl);
		}
		service.exportFullSummary(charSummaryBean, response.getOutputStream());

		return null;
	}

	private String getExportFileName(String particleName, String viewType,
			String charClass) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(particleName);
		nameParts.add(charClass);
		nameParts.add(viewType);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}

	public ActionForward saveExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		setupDomainChar(request, theForm, achar);
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		configBean.setupDomain(user.getLoginName());
		ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();
		service.saveExperimentConfig(configBean.getDomain());
		achar.addExperimentConfig(configBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		InitExperimentConfigSetup.getInstance()
				.persistExperimentConfigDropdowns(request, configBean);

		return mapping.getInputForward();
	}

	public ActionForward saveDataSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		setupDomainChar(request, theForm, achar);
		DataSetBean dataSetBean = achar.getTheDataSet();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		dataSetBean.setupDomain(user.getLoginName());
		NanoparticleCharacterizationResultService service = new NanoparticleCharacterizationResultServiceLocalImpl();
		service.saveData(dataSetBean.getData());
		achar.addDataSet(dataSetBean);

		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		return mapping.getInputForward();
	}

	public ActionForward deleteExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		setupDomainChar(request, theForm, achar);
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();
		service.deleteExperimentConfig(configBean.getDomain());
		achar.removeExperimentConfig(configBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		return mapping.getInputForward();
	}
}
