/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;

import java.util.ArrayList;
import java.util.List;

/**
 * Publication view bean
 * 
 * @author tanq
 * 
 */
public class PublicationBean extends LabFileBean {
	private static final String delimiter = ";";
	
	private String[] particleNames;
	private String[] researchAreas;
	private List<DocumentAuthor> authors;

	/**
	 * 
	 */
	public PublicationBean() {
		super();
		domainFile = new Publication();
		domainFile.setUriExternal(false);
	}

	public PublicationBean(Publication publication) {
		this(publication, true);
	}

	public PublicationBean(Publication publication, boolean loadSamples) {
		super(publication);
		System.out.println("############ PublicationBean");
		if (loadSamples) {
			particleNames = new String[publication.getNanoparticleSampleCollection()
					.size()];
			int i = 0;
			for (NanoparticleSample particle : publication
					.getNanoparticleSampleCollection()) {
				particleNames[i] = particle.getName();
				i++;
			}
		}
		authors = new ArrayList<DocumentAuthor>();
		if (publication.getDocumentAuthorCollection()!=null &&
				publication.getDocumentAuthorCollection().size()>0) {
			int i = 0;			
			for (DocumentAuthor author : publication
					.getDocumentAuthorCollection()) {
				authors.add(author);
				i++;
			}
		}
		//TODO not sure??
		//publication.setDocumentAuthorCollection(authors);
		String researchAreasStr = 
			publication.getResearchArea();
		if (researchAreasStr!=null && researchAreasStr.length()>0) {
			researchAreas = researchAreasStr.split(delimiter);
		}else {
			researchAreas = null;
		}
		this.domainFile = publication;
	}
	
	private void setupDocumentAuthors() {
//		if (authorsStr != null) {			
//			Publication publication = (Publication)domainFile;
//			if (publication.getDocumentAuthorCollection() != null) {
//				publication.getDocumentAuthorCollection().clear();
//			} else {
//				publication.setDocumentAuthorCollection(new HashSet<DocumentAuthor>());
//			}
//			//TODO, XXXXX author split to firstname, lastname, initial
//			String[] strs = authorsStr.split(delimiter);
//			String[] names = null;
//			for (String str : strs) {
//				// change to upper case
//				DocumentAuthor author = new DocumentAuthor();
//				names = str.split(" ");
//				author.setFirstName(names[0].toUpperCase());
//				author.setLastName(names[1].toUpperCase());
//				publication.getDocumentAuthorCollection().add(author);
//			}
//		}
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof PublicationBean) {
			PublicationBean c = (PublicationBean) obj;
			Long thisId = domainFile.getId();
			if (thisId != null && thisId.equals(c.getDomainFile().getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public String[] getParticleNames() {
		return particleNames;
	}

	public void setParticleNames(String[] particleNames) {
		this.particleNames = particleNames;
	}
	/**
	 * @return the researchAreas
	 */
	public String[] getResearchAreas() {
		return researchAreas;
	}

	/**
	 * @param researchAreas the researchAreas to set
	 */
	public void setResearchAreas(String[] researchAreas) {
		this.researchAreas = researchAreas;
	}

	/**
	 * @return the authors
	 */
	public List<DocumentAuthor> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<DocumentAuthor> authors) {
		this.authors = authors;
	}

//	/**
//	 * @return the authorsStr
//	 */
//	public String getAuthorsStr() {
//		return authorsStr;
//	}
//
//	/**
//	 * @param authorsStr the authorsStr to set
//	 */
//	public void setAuthorsStr(String authorsStr) {
//		this.authorsStr = authorsStr;
//	}
}
