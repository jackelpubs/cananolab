/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.util.Constants;

import java.util.Date;

/**
 * View bean for a cell in a table
 *
 * @author pansu
 *
 */
public class TableCell {
	private String value;
	private String datumOrCondition;
	private Datum datum = new Datum();
	private Condition condition = new Condition();

	// FR# 26194, matrix column order.
	private Integer columnOrder;
	private Date createdDate;

	public TableCell() {
	}

	public TableCell(Datum datum) {
		this.datumOrCondition = FindingBean.DATUM_TYPE;
		// display bogus placeholder datum as emtpy string
		if (datum.getValue() == null
				|| datum.getValue() == -1
				&& datum.getCreatedBy().contains(
						Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
			this.value = "";
		}
		// remove .0 from boolean
		else if (datum.getValueType() != null
				&& datum.getValueType().equals("boolean")) {
			if (datum.getValue() == 1) {
				// remove .0 from number
				this.value = "1";
			} else if (datum.getValue() == 0) {
				this.value = "0";
			}
		} else {
			this.value = datum.getValue().toString();
		}
		this.datum = datum;
		this.condition = null;
		this.createdDate = datum.getCreatedDate();
	}

	public TableCell(Condition condition) {
		this.datumOrCondition = "condition";
		if (condition.getValue().contains(
				Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)
				&& condition.getCreatedBy().contains(
						Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
			this.value = "";
		} else {
			this.value = condition.getValue();
		}
		this.condition = condition;
		this.datum = null;
		this.createdDate = condition.getCreatedDate();
	}

	/**
	 * @return the datumOrCondition
	 */
	public String getDatumOrCondition() {
		return datumOrCondition;
	}

	/**
	 * @param datumOrCondition
	 *            the datumOrCondition to set
	 */
	public void setDatumOrCondition(String datumOrCondition) {
		this.datumOrCondition = datumOrCondition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Datum getDatum() {
		return datum;
	}

	public void setDatum(Datum datum) {
		this.datum = datum;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Integer getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Integer columnOrder) {
		this.columnOrder = columnOrder;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
