package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Coagulation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the coagulation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CoagulationBean extends CharacterizationBean {
	public CoagulationBean() {
		super();
		initSetup();
	}
	
	public CoagulationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean coagulationTime=new DatumBean();
			coagulationTime.setType("Coagulation Time");
			table.getDatumList().add(coagulationTime);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean coagulationTime=new DatumBean();
			coagulationTime.setType("Coagulation Time");
			table.getDatumList().add(coagulationTime);
		}
	}
	
	public Coagulation getDomainObj() {
		Coagulation coagulation = new Coagulation();
		super.updateDomainObj(coagulation);
		return coagulation;
	}
}
