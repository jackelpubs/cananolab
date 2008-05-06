package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * View bean for DerivedBioAssayData
 * 
 * @author pansu
 * 
 */
public class DerivedBioAssayDataBean {
	private DerivedBioAssayData domainBioAssayData = new DerivedBioAssayData();

	private LabFileBean labFileBean = new LabFileBean();

	private List<DerivedDatum> datumList = new ArrayList<DerivedDatum>();

	public DerivedBioAssayDataBean() {

	}

	public DerivedBioAssayDataBean(DerivedBioAssayData derivedBioAssayData) {
		domainBioAssayData = derivedBioAssayData;
		if (domainBioAssayData.getLabFile() != null) {
			labFileBean = new LabFileBean(domainBioAssayData.getLabFile());
		}
		if (domainBioAssayData.getDerivedDatumCollection() != null) {
			datumList.addAll(domainBioAssayData.getDerivedDatumCollection());
		}
		Collections.sort(datumList,
				new CaNanoLabComparators.DerivedDatumDateComparator());

		// TODO sort the datum list
	}

	public DerivedBioAssayData getDomainBioAssayData() {
		return domainBioAssayData;
	}

	public LabFileBean getLabFileBean() {
		return labFileBean;
	}

	public List<DerivedDatum> getDatumList() {
		if (domainBioAssayData.getDerivedDatumCollection() != null) {
			domainBioAssayData.getDerivedDatumCollection().clear();
		} else {
			domainBioAssayData
					.setDerivedDatumCollection(new HashSet<DerivedDatum>());
		}
		for (DerivedDatum datum : datumList) {
			domainBioAssayData.getDerivedDatumCollection().add(datum);
		}
		return datumList;
	}

	public void addDerivedDatum() {
		datumList.add(new DerivedDatum());
	}

	public void removeDerivedDatum(int ind) {
		datumList.remove(ind);
	}
}
