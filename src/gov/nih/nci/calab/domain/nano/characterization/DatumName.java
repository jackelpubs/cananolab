/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.characterization;

public class DatumName {
	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private Boolean datumParsed;

	private String name;

	private String characterizationName;

	public Boolean getDatumParsed() {
		return datumParsed;
	}

	public void setDatumParsed(Boolean datumParsed) {
		this.datumParsed = datumParsed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
	}

}
