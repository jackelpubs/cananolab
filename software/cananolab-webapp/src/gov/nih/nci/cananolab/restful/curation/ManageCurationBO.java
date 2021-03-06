package gov.nih.nci.cananolab.restful.curation;

import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ManageCurationBO {
	public void execute(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if (user != null && !user.isCurator()) {
			throw new NoAccessException(
					"You need to be a curator to access the page");
		}
	//	return super.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return true;
	}
}
