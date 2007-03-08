package gov.nih.nci.calab.ui.search;

/**
 * This class remotely searches nanoparticle report across the grid based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: RemoteSearchReportAction.java,v 1.2 2007-03-08 16:49:14 pansu Exp $ */

import gov.nih.nci.calab.dto.common.GridNodeBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.search.GridSearchService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class RemoteSearchReportAction extends AbstractDispatchAction {
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String reportTitle = (String) theForm.get("reportTitle");
		String reportType = (String) theForm.get("reportType");
		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");

		String[] gridNodeHosts = (String[]) theForm.get("gridNodes");
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		GridNodeBean[] gridNodes = GridService.getGridNodesFromHostNames(
				gridNodeMap, gridNodeHosts);
		GridSearchService searchService = new GridSearchService();
		List<LabFileBean> reports = searchService.getRemoteReports(reportType,
				reportTitle, particleType, functionTypes, gridNodes);

		if (reports != null && !reports.isEmpty()) {
			request.setAttribute("remoteReports", reports);
			forward = mapping.findForward("success");
		} else {

			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchReport.noresult");
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

		Map<String, GridNodeBean> gridNodes = GridService.discoverServices(
				CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
				CaNanoLabConstants.DOMAIN_MODEL_NAME);
		request.getSession().setAttribute("allGridNodes", gridNodes);
		InitSessionSetup.getInstance().setAllParticleFunctionTypes(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setStaticDropdowns(session);
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}

	/**
	 * Download action to handle report downloading and viewing
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		String fileName = request.getParameter("fileName");
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>(
				(Map<? extends String, ? extends GridNodeBean>) request
						.getSession().getAttribute("allGridNodes"));
		GridNodeBean gridNode = gridNodeMap.get(CaNanoLabConstants.APP_OWNER);
		GridSearchService searchService = new GridSearchService();
		try {
			byte[] fileData = searchService.getRemoteFileContent(fileId,
					gridNode);

			if (fileData != null) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition",
						"attachment;filename=" + fileName);
				response.setHeader("Cache-Control", "no-cache");
				java.io.OutputStream out = response.getOutputStream();
				out.write(fileData);
				out.close();
			} else {
				throw new CalabException(
						"File to download doesn't exist on the server");
			}
		} catch (Exception e) {
			throw new CalabException("Error retrieving remote file");
		}
		return null;
	}
}
