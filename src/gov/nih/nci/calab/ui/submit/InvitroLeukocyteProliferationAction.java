package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for InVitro LeukocyteProliferation characterization. 
 *  
 * @author beasleyj
 */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.LeukocyteProliferation;
import gov.nih.nci.calab.dto.characterization.ConditionBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.LeukocyteProliferationBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroLeukocyteProliferationAction extends
		BaseCharacterizationAction {

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
		LeukocyteProliferationBean leukocyteProliferationChar = (LeukocyteProliferationBean) theForm
				.get("achar");

		if (leukocyteProliferationChar.getId() == null
				|| leukocyteProliferationChar.getId() == "") {
			leukocyteProliferationChar.setId((String) theForm
					.get("characterizationId"));
		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : leukocyteProliferationChar
				.getDerivedBioAssayDataList()) {

			// Vaidate the the nested data point entries
			for (DatumBean dataPoint : obj.getDatumList()) {
				try {
					Float.parseFloat(dataPoint.getValue());
				} catch (NumberFormatException nfe) {
					Exception dataPointException = new Exception(PropertyReader
							.getProperty(
									CaNanoLabConstants.SUBMISSION_PROPERTY,
									"leukocyteProliferationPercentage"));
					throw dataPointException;
				}

				try {
					if (dataPoint.getIsAControl().equals(
							CaNanoLabConstants.BOOLEAN_NO)) {
						for (ConditionBean condition : dataPoint
								.getConditionList()) {
							Float.parseFloat(condition.getValue());
						}
					}
				} catch (NumberFormatException nfe) {
					Exception conditionsException = new Exception(
							PropertyReader.getProperty(
									CaNanoLabConstants.SUBMISSION_PROPERTY,
									"conditionValues"));
					throw conditionsException;
				}
			}

			LabFileBean fileBean = (LabFileBean) request.getSession()
					.getAttribute("characterizationFile" + fileNumber);
			if (fileBean != null) {
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		leukocyteProliferationChar.setCreatedBy(user.getLoginName());
		leukocyteProliferationChar.setCreatedDate(date);

		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addLeukocyteProliferation(particleType, particleName,
				leukocyteProliferationChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addInvitroLeukocyteProliferation");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		super.postCreate(request, theForm);

		return forward;
	}

	protected void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new LeukocyteProliferationBean());

		cleanSessionAttributes(session);
	}

	@Override
	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		LeukocyteProliferationBean charBean = new LeukocyteProliferationBean(
				(LeukocyteProliferation) aChar);
		theForm.set("achar", charBean);

	}

	@Override
	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "leukocyteProliferation");
		request.setAttribute("loadFileForward",
				"invitroLeukocyteProliferationForm");
	}

}
