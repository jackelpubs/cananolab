package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for InVitro Caspase3Activation characterization. 
 *  
 * @author beasleyj
 */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Caspase3Activation;
import gov.nih.nci.calab.dto.characterization.ConditionBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.Caspase3ActivationBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
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

public class InvitroCaspase3ActivationAction extends BaseCharacterizationAction {

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
		Caspase3ActivationBean caspase3ActivityChar = (Caspase3ActivationBean) theForm
				.get("achar");

		if (caspase3ActivityChar.getId() == null
				|| caspase3ActivityChar.getId() == "") {
			caspase3ActivityChar.setId((String) theForm
					.get("characterizationId"));
		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : caspase3ActivityChar
				.getDerivedBioAssayDataList()) {
			
			// Vaidate the the nested data point entries
			for ( DatumBean dataPoint : obj.getDatumList() ) {
				if ( dataPoint.getValue() != null && dataPoint.getValue().equals("") ) {
					Exception updateConditionsException = new Exception(PropertyReader.getProperty(
							CalabConstants.SUBMISSION_PROPERTY, "capase3ActivationPercentageRequired"));							
					throw updateConditionsException;
				}
				else {
					if ( !StringUtils.isDouble(dataPoint.getValue()) ) {
						Exception updateConditionsException = new Exception(PropertyReader.getProperty(
							CalabConstants.SUBMISSION_PROPERTY, "capase3ActivationPercentageDouble"));							
						throw updateConditionsException;
					}
				}
				
				if ( dataPoint.getIsAControl().equals(CananoConstants.BOOLEAN_NO) ) {
					for ( ConditionBean condition : dataPoint.getConditionList() ) {
						if ( !StringUtils.isInteger(condition.getValue()) ) {
							Exception updateConditionsException = new Exception(PropertyReader.getProperty(
									CalabConstants.SUBMISSION_PROPERTY, "conditionValues"));							
							throw updateConditionsException;
						}
					}
				} 
			}
			
			LabFileBean fileBean = (LabFileBean) request
					.getSession().getAttribute(
							"characterizationFile" + fileNumber);
			if (fileBean != null) {
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		caspase3ActivityChar.setCreatedBy(user.getLoginName());
		caspase3ActivityChar.setCreatedDate(date);
		// Update the other cellLine in the session variable
		if (caspase3ActivityChar.getCellLine().equals(CananoConstants.OTHER)) {
			InitSessionSetup.getInstance().addSessionAttributeElement(
					request.getSession(), "allCellLines",
					caspase3ActivityChar.getOtherCellLine());
		}
		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addCaspase3Activation(particleType, particleName,
				caspase3ActivityChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addInvitroCaspase3Activation");
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

	protected void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new Caspase3ActivationBean());

		cleanSessionAttributes(session);
	}

	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		super.initSetup(request, theForm);
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllCellLines(session);
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
	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "caspase3Activation");
		request
				.setAttribute("loadFileForward",
						"invitroCaspase3ActivationForm");
	}

	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		Caspase3ActivationBean charBean = new Caspase3ActivationBean(
				(Caspase3Activation) aChar);
		theForm.set("achar", charBean);
	}
}
