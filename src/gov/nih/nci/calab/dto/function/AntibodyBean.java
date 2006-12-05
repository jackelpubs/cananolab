package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Antibody;

public class AntibodyBean extends AgentBean {
	private String name;
	private String species;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public Antibody getDomainObj() {
		Antibody antibody=new Antibody();
		super.updateDomainObj(antibody);
		antibody.setName(name);
		antibody.setSpecies(species);
		return antibody;
	}
}
