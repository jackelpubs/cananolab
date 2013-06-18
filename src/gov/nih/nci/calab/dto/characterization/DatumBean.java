/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Control;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author chand, beasleyj, pansu
 * 
 */
public class DatumBean {

	private String id;

	private String name;

	private String value;

	private String statisticsType;

	private String unit;

	private String category;

	private String isAControl = CaNanoLabConstants.BOOLEAN_NO;

	private ControlBean control = new ControlBean();

	private List<ConditionBean> conditionList = new ArrayList<ConditionBean>();

	public DatumBean() {
	}

	public DatumBean(Datum datum) {
		this.id = datum.getId().toString();
		this.name = datum.getName();

		this.statisticsType = (datum.getValue().getStatisticsType() != null) ? StringUtils
				.convertToString(datum.getValue().getStatisticsType())
				: "";
		if (this.statisticsType.equals("boolean")) {
			if (datum.getValue() != null) {
				if (datum.getValue().getValue() == 1.0) {
					this.value = CaNanoLabConstants.BOOLEAN_YES;
				} else if (datum.getValue().getValue() == 0.0) {
					this.value = CaNanoLabConstants.BOOLEAN_NO;
				}
			}
		} else {
			this.value = (datum.getValue() != null) ? StringUtils
					.convertToString(datum.getValue().getValue()) : "";
		}

		this.unit = (datum.getValue() != null) ? StringUtils
				.convertToString(datum.getValue().getUnitOfMeasurement()) : "";
		this.category = (datum.getDerivedBioAssayDataCategory() != null) ? datum
				.getDerivedBioAssayDataCategory()
				: "";
		// Control controlObj = datum.getControl();
		// if (controlObj != null) {
		// control = new ControlBean();
		// control.setName(controlObj.getName());
		// control.setType(controlObj.getType());
		// isAControl = CaNanoLabConstants.BOOLEAN_YES;
		// } else {
		// isAControl = CaNanoLabConstants.BOOLEAN_NO;
		// }
		// if (datum.getConditionCollection() != null
		// && datum.getConditionCollection().size() > 0) {
		// conditionList = new ArrayList<ConditionBean>();
		// for (Condition conditionData : datum.getConditionCollection()) {
		// ConditionBean cBean = new ConditionBean(conditionData);
		// conditionList.add(cBean);
		// }
		// }
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String type) {
		this.name = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ControlBean getControl() {
		return control;
	}

	public void setControl(ControlBean control) {
		this.control = control;
	}

	public List<ConditionBean> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<ConditionBean> conditionList) {
		this.conditionList = conditionList;
	}

	public void updateDomainObj(Datum doDatum) {
		doDatum.setName(name);
		Measurement measurement = new Measurement();

		if (statisticsType.length() > 0) {
			measurement.setStatisticsType(statisticsType);
			if (statisticsType.equals("boolean")) {
				if (value.equalsIgnoreCase("false")
						|| (value.equalsIgnoreCase("no"))) {
					measurement.setValue(new Float(0));
				} else {
					measurement.setValue(new Float(1));
				}
			} else {
				if (value.length() > 0)
					measurement.setValue(new Float(value));
			}
		} else {
			if (value.length() > 0)
				measurement.setValue(new Float(value));
		}

		measurement.setUnitOfMeasurement(unit);
		doDatum.setValue(measurement);
		if (category.length() > 0)
			doDatum.setDerivedBioAssayDataCategory(category);
		if (this.getConditionList() != null
				&& this.getConditionList().size() > 0) {
			for (ConditionBean condition : this.getConditionList()) {
				doDatum.getConditionCollection().add(condition.getDomainObj());
			}
		}

		if (isAControl.equals(CaNanoLabConstants.BOOLEAN_YES)) {
			Control control = new Control();
			if (getControl() != null) {
				if (getControl().getId() != null)
					control.setId(new Long(getControl().getId()));
				control.setName(getControl().getName());
				control.setType(getControl().getType());
				doDatum.setControl(control);
			}
		}
	}

	public String getIsAControl() {
		return isAControl;
	}

	public void setIsAControl(String isAControl) {
		this.isAControl = isAControl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatisticsType() {
		return statisticsType;
	}

	public void setStatisticsType(String statsType) {
		this.statisticsType = statsType;
	}
}
