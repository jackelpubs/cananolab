package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSurfaceAction.java,v 1.15 2007-01-04 23:21:58 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceChemistryBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.Date;
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

public class NanoparticleSurfaceAction extends BaseCharacterizationAction {
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
		SurfaceBean surfaceChar = (SurfaceBean) theForm.get("achar");

		if (surfaceChar.getId() == null || surfaceChar.getId() == "") {

			surfaceChar.setId((String) theForm.get("characterizationId"));

		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : surfaceChar
				.getDerivedBioAssayDataList()) {
			LabFileBean fileBean = (LabFileBean) request
					.getSession().getAttribute(
							"characterizationFile" + fileNumber);
			if (fileBean != null) {
				// logger.info("************set fileBean to " + fileNumber);
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		surfaceChar.setCreatedBy(user.getLoginName());
		surfaceChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleSurface(particleType, particleName, surfaceChar);

		ActionMessages msgs = new ActionMessages();
		// ActionMessage msg = new ActionMessage("message.addParticleSize");
		ActionMessage msg = new ActionMessage("message.addParticleSurface");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		InitSessionSetup.getInstance().setAllInstrumentTypeManufacturers(
				session);
		return forward;
	}

	public void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new SurfaceBean());

		cleanSessionAttributes(session);
		// for (Enumeration e = session.getAttributeNames(); e.hasMoreElements()
		// ;) {
		// String element = (String) e.nextElement();
		// if (element.startsWith(CaNanoLabConstants.CHARACTERIZATION_FILE)) {
		// session.removeAttribute(element);
		// }
		// }
	}

	public void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		super.initSetup(request, theForm);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllAreaMeasureUnits(session);
		InitSessionSetup.getInstance().setAllChargeMeasureUnits(session);
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
		SurfaceBean achar = (SurfaceBean) theForm.get("achar");
		String type = (String) request.getParameter("type");
		if (type != null && !type.equals("") && type.equals("charTables")) {
			updateCharacterizationTables(achar);
		}
		if (type != null && !type.equals("") && type.equals("surfaceChemistries")) {
			updateSurfaceChemistries(achar);;
		}				
		theForm.set("achar", achar);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Update surface chemistry for Surface
	 * 
	 * @param particle
	 */
	private void updateSurfaceChemistries(SurfaceBean surface) {
		int surfaceChemistryNum = Integer.parseInt(surface.getNumberOfSurfaceChemistries());
		List<SurfaceChemistryBean> origSurfaceChemistries = surface
				.getSurfaceChemistries();
		int origNum = (origSurfaceChemistries == null) ? 0
				: origSurfaceChemistries.size();
		List<SurfaceChemistryBean> surfaceChemistries = new ArrayList<SurfaceChemistryBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < surfaceChemistryNum; i++) {
				SurfaceChemistryBean surfaceChemistry = new SurfaceChemistryBean();
				surfaceChemistries.add(surfaceChemistry);
			}
		}
		// use keep original surface group info
		else if (surfaceChemistryNum <= origNum) {
			for (int i = 0; i < surfaceChemistryNum; i++) {
				surfaceChemistries
						.add((SurfaceChemistryBean) origSurfaceChemistries
								.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				surfaceChemistries
						.add((SurfaceChemistryBean) origSurfaceChemistries
								.get(i));
			}
			for (int i = origNum; i < surfaceChemistryNum; i++) {
				surfaceChemistries.add(new SurfaceChemistryBean());
			}
		}
		surface.setSurfaceChemistries(surfaceChemistries);
	}

	@Override
	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		SurfaceBean surface = new SurfaceBean((Surface) aChar);
		theForm.set("achar", surface);
	}

	@Override
	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "surface");
		request.setAttribute("loadFileForward", "surfaceInputForm");

	}
}