package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.DerivedDataFile;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents attributes of a lab file to be viewed in a view page.
 * 
 * @author pansu
 * 
 */
public class LabFileBean {
	private String title;

	private String description;

	private String comments;

	private String[] keywords = new String[0];

	private String[] visibilityGroups = new String[0];

	private Date createdDate;

	private String createdBy;

	private String id;

	private String path;

	private String name;

	private String type;

	private String keywordsStr;

	private String visibilityStr;
	
	private String gridNode;

	/*
	 * name to be displayed as a part of the drop-down list
	 */
	private String displayName;

	public LabFileBean() {
	}

	public LabFileBean(LabFile charFile, String fileType) {
		this.id = charFile.getId().toString();
		this.name = charFile.getFilename();
		this.path = charFile.getPath();
		this.title = charFile.getTitle();
		this.description = charFile.getDescription();
		this.createdBy = charFile.getCreatedBy();
		this.createdDate = charFile.getCreatedDate();
		if (charFile instanceof DerivedDataFile) {
			List<String> allkeywords = new ArrayList<String>();
			for (Keyword keyword : ((DerivedDataFile) charFile)
					.getKeywordCollection()) {
				allkeywords.add(keyword.getName());
			}
			keywords=allkeywords.toArray(new String[0]);
		}
		this.type = fileType;
	}

	public LabFileBean(LabFile charFile, String fileType, String gridNodeHost) {
		this(charFile, fileType);
		this.gridNode=gridNodeHost;
	}
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	public LabFile getDomainObject() {
		LabFile labfile = new LabFile();
		if (id != null && id.length() > 0) {
			labfile.setId(new Long(id));
		}
		labfile.setCreatedBy(createdBy);
		labfile.setCreatedDate(createdDate);
		labfile.setDescription(description);
		labfile.setComments(comments);
		labfile.setFilename(name);
		labfile.setPath(path);
		labfile.setTitle(title);
		return labfile;
	}

	public DerivedDataFile getDomainObjectDerivedDataFile() {
		DerivedDataFile dataFile = new DerivedDataFile();
		if (id != null && id.length() > 0) {
			dataFile.setId(new Long(id));
		}
		dataFile.setCreatedBy(createdBy);
		dataFile.setCreatedDate(createdDate);
		dataFile.setDescription(description);
		dataFile.setComments(comments);
		dataFile.setFilename(name);
		dataFile.setPath(path);
		dataFile.setTitle(title);
		for (String keywordValue : keywords) {
			Keyword keyword = new Keyword();
			keyword.setName(keywordValue);
			dataFile.getKeywordCollection().add(keyword);
		}
		return dataFile;
	}

	public String getDisplayName() {
		displayName = path.replaceAll("/decompressedFiles", "");
		return displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeywordsStr() {
		keywordsStr = StringUtils.join(keywords, "\r\n");
		return keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
		if (keywordsStr.length() > 0)
			this.keywords = keywordsStr.split("\r\n");
	}

	public String getVisibilityStr() {
		visibilityStr = StringUtils.join(visibilityGroups, "<br>");
		return visibilityStr;
	}

	public String getGridNode() {
		return gridNode;
	}

	public void setGridNode(String gridNode) {
		this.gridNode = gridNode;
	}
}