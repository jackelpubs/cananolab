package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.ROS;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the ros characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ROSBean extends CharacterizationBean {
	public ROSBean() {
		super();
		initSetup();
	}
	
	public ROSBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentROS=new DatumBean();
			percentROS.setType("Percent ROS");
			percentROS.setValueUnit("%");
			table.getDatumList().add(percentROS);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentROS=new DatumBean();
			percentROS.setType("Percent ROS");
			percentROS.setValueUnit("%");
			table.getDatumList().add(percentROS);
		}
	}
	
	public ROS getDomainObj() {
		ROS ros = new ROS();
		super.updateDomainObj(ros);
		return ros;
	}
}
