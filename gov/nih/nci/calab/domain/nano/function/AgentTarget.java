package gov.nih.nci.calab.domain.nano.function;

public class AgentTarget implements java.io.Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String description;

	public AgentTarget() {
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
