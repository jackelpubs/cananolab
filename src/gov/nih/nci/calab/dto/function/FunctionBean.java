/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengje
 *
 */
public class FunctionBean {

	private String id;
	private String type;
	private String activationMethod;
	private String description;
	private String viewTitle;
	
	private List<LinkageBean> linkages = new ArrayList<LinkageBean>();;
	private String numberOfLinkages;
	
	/**
	 * 
	 */
	public FunctionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FunctionBean(String id, String type, String viewTitle) {
		this.id=id;		
		this.type=type;
		this.viewTitle=viewTitle;
	}
	
	public FunctionBean(Function function) {
		this.id=function.getId().toString();
		this.type=function.getType();
		this.activationMethod=function.getActivationMethod();
		this.description=function.getDescription();
		this.viewTitle=function.getIdentificationName();
		for(Linkage linkage: function.getLinkageCollection()) {
			linkages.add(new LinkageBean(linkage));
		}
		this.numberOfLinkages=linkages.size()+"";
	}
	
	public String getActivationMethod() {
		return activationMethod;
	}

	public void setActivationMethod(String activationMethod) {
		this.activationMethod = activationMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<LinkageBean> getLinkages() {
		return linkages;
	}

	public void setLinkages(List<LinkageBean> linkages) {
		this.linkages = linkages;
	}

	public String getNumberOfLinkages() {
		return numberOfLinkages;
	}

	public void setNumberOfLinkages(String numberOfLinkages) {
		this.numberOfLinkages = numberOfLinkages;
	}

	public String getViewTitle() {
			// get only the first number of characters of the title
		if (viewTitle!=null &&viewTitle.length() > CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH) {
			return viewTitle.substring(0, CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH);
		}
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Function getDomainObj(){
		Function doFunction = new Function();
		if (getId() != null && getId().length() > 0) {
			doFunction.setId(new Long(getId()));
		}
		doFunction.setActivationMethod(activationMethod);
		doFunction.setDescription(description);
		doFunction.setIdentificationName(viewTitle);
		doFunction.setType(type);
		for (LinkageBean linkage: getLinkages()) {
			doFunction.getLinkageCollection().add(linkage.getDomainObj());
		}
		return doFunction;
	}
}