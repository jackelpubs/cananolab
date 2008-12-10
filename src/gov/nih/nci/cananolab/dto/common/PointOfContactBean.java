/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;


/**
 * PointOfContact view bean
 * 
 * @author tanq, cais
 * 
 */

//TODO: need to revise PointOfContactBean, copy from OrganizationBean

public class PointOfContactBean{
	private PointOfContact domain;
	private String POCName;
	private Organization organization;
	private String[] visibilityGroups = new String[0];
	private boolean hidden = false;
	private String address;
	//TODO: need info for nanoparticleSample??
	
	
	public PointOfContactBean() {
		super();
		domain = new PointOfContact();
		POCName = "";
	}

	public PointOfContactBean(PointOfContact pointOfContact) {
		domain = pointOfContact;
		organization =
			pointOfContact.getOrganization();
		String firstName = domain.getFirstName();
		POCName = "";
		if (firstName!=null) {
			POCName = firstName +" ";
		}
		String lastName = domain.getLastName();
		if (lastName!=null) {
			POCName+=lastName;
		}
	}
	
	/**
	 * @return the domain
	 */
	public PointOfContact getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(PointOfContact domain) {
		this.domain = domain;
	}

	/**
	 * @return the pocs
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param pocs the pocs to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the visibilityGroups
	 */
	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	/**
	 * @param visibilityGroups the visibilityGroups to set
	 */
	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the pOCName
	 */
	public String getPOCName() {
		return POCName;
	}

	/**
	 * @param name the pOCName to set
	 */
	public void setPOCName(String name) {
		POCName = name;
	}	
	
	
	
}
