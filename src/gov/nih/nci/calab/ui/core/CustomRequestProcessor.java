package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;

/**
 * 
 * This class extends the default Tiles RequesetProcessor to check and make sure
 * session is not expired before every request. This is useful for directing
 * requests to the log in page when action forms involve indexed properties and
 * session is expired.
 * 
 * @author pansu
 */
public class CustomRequestProcessor extends TilesRequestProcessor {
	protected ActionForm processActionForm(
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response,
			ActionMapping mapping) {
		HttpSession session = request.getSession();
		String dispatch = request.getParameter("dispatch");

		if (session.isNew()
				&& (dispatch == null || !Arrays.asList(
						CaNanoLabConstants.PUBLIC_DISPATCHES)
						.contains(dispatch))) {
			return null;
		} else {
			return super.processActionForm(request, response, mapping);
		}
	}
}
