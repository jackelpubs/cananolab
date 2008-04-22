package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.SurfaceChemistry;

import java.util.ArrayList;
import java.util.List;

public class SurfaceBean {
	private List<SurfaceChemistry> surfaceChemistryList = new ArrayList<SurfaceChemistry>();

	private Surface domainSurface = new Surface();

	public SurfaceBean() {
	}

	public SurfaceBean(Surface surface) {
		domainSurface = surface;
		for (SurfaceChemistry chem : surface.getSurfaceChemistryCollection()) {
			surfaceChemistryList.add(chem);
		}
	}

	public Surface getDomainSurface() {
		return domainSurface;
	}

	public List<SurfaceChemistry> getSurfaceChemistryList() {
		return surfaceChemistryList;
	}
}
