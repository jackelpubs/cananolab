/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.characterization.invitro.Targeting;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExcelParser;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class KellyDataLoader {
	private static Logger logger = Logger.getLogger(KellyDataLoader.class);

	public final static String USER_NAME = "SPREAD_SHEET_PARSER_4_KELLY_DATA";

	public final static String SAMPLE_NAME_PREFIX = "MIT_MGH-KKellyIB2009-";

	public final static String ASSAY_TYPE = "immunoassay";

	public final static String CELL_LINE_PREFIX = "CELL  LINE: ";

	public final static String DATUM_NAME = "sample concentration after treatment";

	public final static String DATUM_UNIT = "pM";

	public final static double VALUE_FACTOR = 1000000000000d;

	public final static Map<String, String> DATUM_TYPE_MAP = new HashMap<String, String>();
	static {
		DATUM_TYPE_MAP.put("Mean (M)", "mean");
		DATUM_TYPE_MAP.put("Median (M)", "median");
		DATUM_TYPE_MAP.put("SEM (M)", "standard error of mean");
	}

	// Data map: {MIT_MGH-KKellyIB2009-02, {Aorta 1, {Median (M),
	// 9.02194E-08}}}.
	private SortedMap<String, SortedMap<String, SortedMap<String, Double>>> dataMatrix;

	public KellyDataLoader(
			SortedMap<String, SortedMap<String, SortedMap<String, Double>>> kellyMatrix) {
		this.dataMatrix = new TreeMap<String, SortedMap<String, SortedMap<String, Double>>>();

		// replace sorted key with sample name prefix, "MIT...02" to "MIT...39".
		int count = 2;
		for (String key : kellyMatrix.keySet()) {
			StringBuilder sampleName = new StringBuilder(SAMPLE_NAME_PREFIX);
			if (count < 10) {
				sampleName.append('0');
			}
			sampleName.append(count++);
			SortedMap<String, SortedMap<String, Double>> cellLineMap = kellyMatrix
					.get(key);
			this.dataMatrix.put(sampleName.toString(), cellLineMap);
			if (logger.isDebugEnabled()) {
				logger.debug(key + " ==> " + sampleName);
			}
		}
	}

	protected void saveOtherTypes() throws BaseException {
		Set<String> valueSet = null;
		// 1.find & save assay type.
		valueSet = LookupService.getDefaultAndOtherLookupTypes("targeting",
				"assayType", "otherAssayType");
		if (valueSet != null && !valueSet.contains(ASSAY_TYPE)) {
			LookupService.saveOtherType("targeting", "otherAssayType",
					ASSAY_TYPE);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: targeting, otherAssaytype, "
						+ ASSAY_TYPE);
			}
		}
		// 2.find & save datum name.
		valueSet = LookupService.getDefaultAndOtherLookupTypes("targeting",
				"datumName", "otherDatumName");
		if (valueSet != null && !valueSet.contains(DATUM_NAME)) {
			LookupService.saveOtherType(ASSAY_TYPE, "otherDatumName",
					DATUM_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: " + ASSAY_TYPE
						+ ", otherDatumName, " + DATUM_NAME);
			}
		}
		// 3.find & save datum unit.
		valueSet = LookupService.getDefaultAndOtherLookupTypes(DATUM_NAME,
				"unit", "otherUnit");
		if (valueSet != null && !valueSet.contains(DATUM_UNIT)) {
			LookupService.saveOtherType(DATUM_NAME, "otherUnit", DATUM_UNIT);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: " + DATUM_NAME
						+ ", targeting, otherUnit, " + DATUM_UNIT);
			}
		}
		// 4.iterate dataMatrix map, populate datum value type set.
		Set<String> valueTypeSet = new HashSet<String>();
		for (String sampleName : this.dataMatrix.keySet()) {
			// 2.iterate cellLineMap, create Targeting, Finding & Datum.
			SortedMap<String, SortedMap<String, Double>> cellLineMap = this.dataMatrix
					.get(sampleName);
			for (String cellLine : cellLineMap.keySet()) {
				SortedMap<String, Double> datumMap = cellLineMap.get(cellLine);
				for (String valueType : datumMap.keySet()) {
					valueTypeSet.add(valueType);
				}
			}
		}
		// 5.iterate valueTypeSet map, find & save datum value type.
		for (String valueType : valueTypeSet) {
			valueSet = LookupService.getDefaultAndOtherLookupTypes(
					"datum and condition", "valueType", "otherValueType");
			String datumValueType = DATUM_TYPE_MAP.get(valueType);
			if (valueSet != null && !valueSet.contains(datumValueType)) {
				LookupService.saveOtherType("datum and condition",
						"otherValueType", datumValueType);
				if (logger.isDebugEnabled()) {
					logger
							.debug("Lookup saved: datum and condition, otherValueType, "
									+ datumValueType);
				}
			}
		}
	}

	public void load(UserBean user) throws BaseException {
		this.saveOtherTypes();

		Date currentDate = Calendar.getInstance().getTime();
		SecurityService securityService = new SecurityService(
				AccessibilityBean.CSM_APP_NAME, user);
		SampleService service = new SampleServiceLocalImpl(securityService);
		CharacterizationService charService = new CharacterizationServiceLocalImpl(
				securityService);

		// iterate dataMatrix map, load sample & save Targeting char.
		int count = 0;
		for (String sampleName : this.dataMatrix.keySet()) {
			SampleBean sampleBean = null;
			try {
				// 1.load sampleBean by sample name.
				sampleBean = service.findSampleByName(sampleName);
			} catch (Exception e) {
				logger.error("Error loading Sample for: " + sampleName, e);
				continue;
			}
			if (sampleBean == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Sample not found for: " + sampleName);
				}
				continue;
			}
			// 2.iterate cellLineMap, create Targeting, Finding & Datum.
			SortedMap<String, SortedMap<String, Double>> cellLineMap = this.dataMatrix
					.get(sampleName);
			for (String cellLine : cellLineMap.keySet()) {
				// 2a.create 1 Targeting char object for each CellLine.
				Targeting achar = new Targeting();
				achar.setCreatedBy(USER_NAME);
				achar.setCreatedDate(currentDate);
				achar.setDesignMethodsDescription(CELL_LINE_PREFIX + cellLine);
				achar.setAssayType(ASSAY_TYPE);
				achar.setFindingCollection(new HashSet<Finding>());

				// 2b.create 1 Finding for each CellLine.
				Finding finding = new Finding();
				finding.setCreatedBy(USER_NAME);
				finding.setCreatedDate(currentDate);
				achar.getFindingCollection().add(finding);
				finding.setDatumCollection(new HashSet<Datum>());
				SortedMap<String, Double> datumMap = cellLineMap.get(cellLine);

				// 2c.iterate datumMap & add datum to finding.
				int i = 0;
				for (String valueType : datumMap.keySet()) {
					Datum datum = new Datum();
					datum.setCreatedBy(USER_NAME);
					datum
							.setCreatedDate(DateUtils
									.addSecondsToCurrentDate(i++));
					datum.setName(DATUM_NAME);
					datum
							.setValue((float) (datumMap.get(valueType) * VALUE_FACTOR));
					datum.setValueType(DATUM_TYPE_MAP.get(valueType));
					datum.setValueUnit(DATUM_UNIT);
					finding.getDatumCollection().add(datum);
				}
				// 3.new CharBean, save Finding and the Targeting bean.
				CharacterizationBean charBean = new CharacterizationBean(achar);
				try {
					List<FindingBean> findings = charBean.getFindings();
					for (FindingBean findingBean : findings) {
						charService.saveFinding(findingBean);
					}
					charService.saveCharacterization(sampleBean, charBean);
					if (logger.isDebugEnabled()) {
						logger.debug("charBean saved: " + sampleName + ", "
								+ cellLine);
						count++;
					}
				} catch (Exception e) {
					logger.error("Error saving charBean: " + sampleName + ", "
							+ cellLine, e);
					continue;
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Loading completed, char# saved: " + count);
		}
	}

	protected void erase() throws Exception {
		try {
			CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"delete from gov.nih.nci.cananolab.domain.common.Datum where createdBy = '"
							+ USER_NAME + "'");
			appService.query(crit);

			crit = new HQLCriteria(
					"delete from gov.nih.nci.cananolab.domain.common.Finding where createdBy = '"
							+ USER_NAME + "'");
			appService.query(crit);

			crit = new HQLCriteria(
					"delete from gov.nih.nci.cananolab.domain.common.Characterization where createdBy = '"
							+ USER_NAME + "'");
			appService.query(crit);
		} catch (Exception e) {
			logger.error("Error removing parsed data.", e);
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 3) {
			String loginName = args[0];
			String password = args[1];
			String inputFileName = args[2];
			try {
				UserBean user = new UserBean(loginName, password);
				ExcelParser parser = new ExcelParser();
				SortedMap<String, SortedMap<String, SortedMap<String, Double>>> dataMatrix = parser
						.twoWayParse(inputFileName);
				KellyDataLoader loader = new KellyDataLoader(dataMatrix);
				loader.load(user);
			} catch (SecurityException e) {
				System.out.println("Can't authenticate the login name: "
						+ loginName);
				e.printStackTrace();
				System.exit(2);
			} catch (IOException e) {
				System.out.println("Input file not found.");
				e.printStackTrace();
				System.exit(3);
			} catch (Exception e) {
				System.out.println("Error saving data.");
				e.printStackTrace();
				System.exit(4);
			}
		} else {
			System.out.println("Invalid argument!");
			System.out
					.println("java KellyDataLoader <loginName> <password> <inputFileName>");
		}
		System.exit(0);
	}
}
