package gov.nih.nci.calab.ui.workflow;

/**
 * This Action class saves user entered new Run information and the assigned aliquots
 * into the database.
 * 
 * @author caLAB Team
 */

import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.util.SpecialCharReplacer;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;
import gov.nih.nci.calab.service.workflow.ExecuteWorkflowService;
import gov.nih.nci.calab.ui.core.AbstractBaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class CreateRunAction extends AbstractBaseAction {

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		// TODO fill in details for aliquot information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		// Get Prameters from form elements
		// Run info

		String assayId = (String) theForm.get("assayId");
		String runBy = (String) theForm.get("runBy");
		String runDate = (String) theForm.get("runDate");
		if (!isValidDate(runDate)) {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage("errors.date", "Run Date");
//			msgs.add("error", msg);
//			saveMessages(request, msgs);
//			
//			ActionForward forward = mapping.findForward("input");
//
//			return forward;
			throw new CalabException("Year of run date must be in a four digit format.");
		}

		// get user and date information from session
		String creator = (String) session.getAttribute("creator");
		String creationDate = (String) session.getAttribute("creationDate");
		ExecuteWorkflowService workflowService = new ExecuteWorkflowService();
		RunBean runBean = workflowService.saveRun(assayId, runBy, runDate,
				creator, creationDate);

		// Save Aliquots assigned
		String[] aliquotIds = (String[]) theForm.get("assignedAliquot");
		String comments = (String) theForm.get("aliquotComment");

		workflowService.saveRunAliquots(runBean.getId(), aliquotIds, comments, creator,
				creationDate);

		session.setAttribute("newWorkflowCreated", "true");

		HttpFileUploadSessionData hFileUploadData=new HttpFileUploadSessionData();
		SpecialCharReplacer specialCharReplacer = new SpecialCharReplacer();
		String assayType = specialCharReplacer.getReplacedString(runBean.getAssayBean().getAssayType());
		String assayName = specialCharReplacer.getReplacedString(runBean.getAssayBean().getAssayName());
		String runName = specialCharReplacer.getReplacedString(runBean.getName());

		hFileUploadData.setRunId(runBean.getId());
		hFileUploadData.setAssay(assayName);
		hFileUploadData.setAssayType(assayType);
		hFileUploadData.setRun(runName);
		hFileUploadData.setFromType("upload");
		session.setAttribute("httpFileUploadSessionData", hFileUploadData);

		// set Forward
		ActionForward forward= mapping.findForward("success");
				
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
	
	private boolean isValidDate(String date) {
		String year = date.substring(date.lastIndexOf("/")+1);
		if (year.length()<4){
			return false;
		}
		return true;
	}
	
}
