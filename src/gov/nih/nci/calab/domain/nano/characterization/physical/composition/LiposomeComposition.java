package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;
import java.util.HashSet;

public class LiposomeComposition implements Composition {
	private Long id;
	private String source;
	private String classification;
	private Collection<Nanoparticle> nanoparticleCollection;
	private Collection<ComposingElement> composingElementCollection = new HashSet<ComposingElement>();

	private Boolean isPolymerized;
	private String polymerName;
	
	public LiposomeComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Boolean getIsPolymerized() {
		return isPolymerized;
	}

	public void setIsPolymerized(Boolean isPolymerized) {
		this.isPolymerized = isPolymerized;
	}

	public String getPolymerName() {
		return polymerName;
	}

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassification() {
		return this.classification;
	}

	public void setNanoparticleCollection(Collection<Nanoparticle> particleCollection) {
		this.nanoparticleCollection = particleCollection;
	}

	public Collection<Nanoparticle> getNanoparticleCollection() {
		return this.nanoparticleCollection;
	}
	
	public void setComposingElementCollection(Collection<ComposingElement> element){
		this.composingElementCollection = element;
	}
	
	public Collection<ComposingElement> getComposingElementCollection(){
		return this.composingElementCollection;
	}
}
