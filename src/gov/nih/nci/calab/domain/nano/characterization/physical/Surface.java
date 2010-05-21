package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationProtocol;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Surface extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String source;

	private String description;

	private String identificationName;

	private String createdBy;

	private Date createdDate;

	private Collection<Nanoparticle> nanoparticleCollection;

	private Collection<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();

	private Instrument instrument;

	private CharacterizationProtocol characterizationProtocol;

	private Measurement surfaceArea;

	private Measurement surfaceCharge;

	// private Measurement zetaPotential;
	private Float zetaPotential;

	private Measurement charge;

	private Boolean isHydrophobic;

	private Collection<SurfaceChemistry> surfaceChemistryCollection = new ArrayList<SurfaceChemistry>();

	private String classification;

	private String name;

	public Surface() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public String getName() {
		return PHYSICAL_SURFACE;
	}

	public void setNanoparticleCollection(Collection<Nanoparticle> particles) {
		this.nanoparticleCollection = particles;
	}

	public Collection<Nanoparticle> getNanoparticleCollection() {
		return this.nanoparticleCollection;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Collection<DerivedBioAssayData> getDerivedBioAssayDataCollection() {
		return derivedBioAssayDataCollection;
	}

	public void setDerivedBioAssayDataCollection(
			Collection<DerivedBioAssayData> derivedBioAssayDataCollection) {
		this.derivedBioAssayDataCollection = derivedBioAssayDataCollection;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public CharacterizationProtocol getCharacterizationProtocol() {
		return characterizationProtocol;
	}

	public void setCharacterizationProtocol(
			CharacterizationProtocol characterizationProtocol) {
		this.characterizationProtocol = characterizationProtocol;
	}

	public Measurement getCharge() {
		return charge;
	}

	public void setCharge(Measurement charge) {
		this.charge = charge;
	}

	public Boolean getIsHydrophobic() {
		return isHydrophobic;
	}

	public void setIsHydrophobic(Boolean isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}

	public Measurement getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(Measurement surfaceArrea) {
		this.surfaceArea = surfaceArrea;
	}

	public Measurement getSurfaceCharge() {
		return surfaceCharge;
	}

	public void setSurfaceCharge(Measurement surfaceCharge) {
		this.surfaceCharge = surfaceCharge;
	}

	/*
	 * public Measurement getZetaPotential() { return zetaPotential; }
	 * 
	 * public void setZetaPotential(Measurement zetaPotential) {
	 * this.zetaPotential = zetaPotential; }
	 */
	public Collection<SurfaceChemistry> getSurfaceChemistryCollection() {
		return surfaceChemistryCollection;
	}

	public void setSurfaceChemistryCollection(
			Collection<SurfaceChemistry> surfaceChemistryCollection) {
		this.surfaceChemistryCollection = surfaceChemistryCollection;
	}

	public Float getZetaPotential() {
		return zetaPotential;
	}

	public void setZetaPotential(Float zetaPotential) {
		this.zetaPotential = zetaPotential;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public void setName(String name) {
		this.name = name;
	}

}