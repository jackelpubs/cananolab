package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.SurfaceGroup;

/**
 * This class represents properties of a SurfaceGroup to be shown in the view
 * page.
 * 
 * @author pansu
 * 
 */
public class SurfaceGroupBean {
	private String id;

	private String name;

	private String modifier;

	public SurfaceGroupBean() {

	}

	public SurfaceGroupBean(SurfaceGroup surfaceGroup) {
		this.id = surfaceGroup.getId().toString();
		this.name = surfaceGroup.getName();
		this.modifier = surfaceGroup.getModifier();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SurfaceGroup getDomainObj() {
		SurfaceGroup surfaceGroup = new SurfaceGroup();

		surfaceGroup.setName(this.name);

		surfaceGroup.setModifier(this.modifier);
		if (getId() != null && getId().length() > 0) {
			surfaceGroup.setId(new Long(getId()));
		}
		return surfaceGroup;
	}
}
