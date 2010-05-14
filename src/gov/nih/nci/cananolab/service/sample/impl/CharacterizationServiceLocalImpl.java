package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.helper.FileServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving local characterizations
 *
 * @author tanq, pansu
 *
 */
public class CharacterizationServiceLocalImpl implements
		CharacterizationService {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceLocalImpl.class);
	private CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
	private FileServiceHelper fileHelper = new FileServiceHelper();
	private ProtocolServiceHelper protocolHelper = new ProtocolServiceHelper();
	private FileServiceLocalImpl fileService = new FileServiceLocalImpl();

	public CharacterizationServiceLocalImpl() {
	}

	public void saveCharacterization(SampleBean sampleBean,
			CharacterizationBean charBean, UserBean user)
			throws CharacterizationException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			Sample sample = sampleBean.getDomain();
			Characterization achar = charBean.getDomainChar();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (achar.getId() != null) {
				Characterization dbChar = (Characterization) appService.load(
						Characterization.class, achar.getId());
				if (dbChar == null) {
					String err = "Object doesn't exist in the database anymore.  Please log in again.";
					logger.error(err);
					throw new CharacterizationException(err);
				}
			}
			// if (sample.getCharacterizationCollection() != null) {
			// sample.getCharacterizationCollection().clear();
			// } else {
			// sample
			// .setCharacterizationCollection(new HashSet<Characterization>());
			// }
			achar.setSample(sample);
			// sample.getCharacterizationCollection().add(achar);

			// save file data to file system and assign visibility
			List<FindingBean> findingBeans = charBean.getFindings();
			if (findingBeans != null && !findingBeans.isEmpty()) {
				FileService fileService = new FileServiceLocalImpl();
				for (FindingBean findingBean : findingBeans) {
					for (FileBean fileBean : findingBean.getFiles()) {
						fileService.prepareSaveFile(fileBean.getDomainFile(),
								user);
						fileService.writeFile(fileBean, user);
					}
				}
			}
			appService.saveOrUpdate(achar);

			// set visibility
			String[] visibleGroups = sampleBean.getVisibilityGroups();
			String owningGroup = sampleBean.getPrimaryPOCBean().getDomain()
					.getOrganization().getName();
			assignVisibility(charBean.getDomainChar(), visibleGroups,
					owningGroup);
		} catch (Exception e) {
			String err = "Problem in saving the characterization.";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public CharacterizationBean findCharacterizationById(String charId,
			UserBean user) throws CharacterizationException, NoAccessException {
		CharacterizationBean charBean = null;
		try {
			Characterization achar = helper.findCharacterizationById(charId,
					user);
			if (achar != null) {
				charBean = new CharacterizationBean(achar);
				loadCharacterizationBean(achar, user);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new CharacterizationException();
		}
		return charBean;
	}

	private CharacterizationBean loadCharacterizationBean(
			Characterization achar, UserBean user) throws Exception {
		CharacterizationBean charBean = new CharacterizationBean(achar);
		ProtocolBean protocolBean = charBean.getProtocolBean();
		// retrieve protocol visibility
		if (protocolBean.getDomain().getId() != null) {
			protocolBean.setVisibilityGroups(protocolHelper
					.retrieveVisibility(protocolBean.getDomain()));
		}
		for (FindingBean findingBean : charBean.getFindings()) {
			if (findingBean.getFiles() != null && user != null) {
				for (FileBean fileBean : findingBean.getFiles()) {
					fileBean.setVisibilityGroups(fileHelper
							.retrieveVisibility(fileBean.getDomainFile()));
				}
			}
		}
		return charBean;
	}

	public List<String> deleteCharacterization(Characterization chara,
			UserBean user, Boolean removeVisibility)
			throws CharacterizationException, NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(chara);
			entries = removeVisibility(chara, removeVisibility);
		} catch (Exception e) {
			String err = "Error deleting characterization " + chara.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
		return entries;
	}

	public List<CharacterizationBean> findCharacterizationsBySampleId(
			String sampleId, UserBean user) throws CharacterizationException {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();
		try {
			List<Characterization> chars = helper
					.findCharacterizationsBySampleId(sampleId, user);
			for (Characterization achar : chars) {
				CharacterizationBean charBean = loadCharacterizationBean(achar,
						user);
				charBeans.add(charBean);
			}
			return charBeans;
		} catch (Exception e) {
			String err = "Error finding characterization by sample ID "
					+ sampleId;
			logger.error(err, e);
			throw new CharacterizationException(err);
		}
	}

	public FindingBean findFindingById(String findingId, UserBean user)
			throws CharacterizationException, NoAccessException {
		FindingBean findingBean = null;
		try {
			Finding finding = helper.findFindingById(findingId, user);
			if (finding != null) {
				findingBean = new FindingBean(finding);
				if (findingBean.getFiles() != null && user != null) {
					for (FileBean fileBean : findingBean.getFiles()) {
						fileBean.setVisibilityGroups(fileHelper
								.retrieveVisibility(fileBean.getDomainFile()));
					}
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Error getting finding of ID " + findingId;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
		return findingBean;
	}

	public void saveFinding(FindingBean finding, UserBean user)
			throws CharacterizationException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			FileService fileService = new FileServiceLocalImpl();
			for (FileBean fileBean : finding.getFiles()) {
				fileService.prepareSaveFile(fileBean.getDomainFile(), user);
			}
			appService.saveOrUpdate(finding.getDomain());
			// save file data to file system and assign visibility
			for (FileBean fileBean : finding.getFiles()) {
				fileService.writeFile(fileBean, user);
			}
			// visibility assignment is handled by saving characterization
		} catch (Exception e) {
			String err = "Error saving characterization result finding. ";
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}

	public List<String> deleteFinding(Finding finding, UserBean user,
			Boolean removeVisibility) throws CharacterizationException,
			NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(finding);
			entries = removeVisibility(finding, removeVisibility);
		} catch (Exception e) {
			String err = "Error deleting finding " + finding.getId();
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
		return entries;
	}

	public void saveExperimentConfig(ExperimentConfigBean configBean,
			UserBean user) throws ExperimentConfigException, NoAccessException {
		if (user == null || !user.isCurator()) {
			throw new NoAccessException();
		}
		try {
			ExperimentConfig config = configBean.getDomain();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = helper.findExperimentConfigById(
						config.getId().toString(), user);
				if (dbConfig != null) {
					// reuse original createdBy if it is not COPY
					if (!dbConfig.getCreatedBy().equals(
							Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
						config.setCreatedBy(dbConfig.getCreatedBy());
					}
					// reuse original created date
					config.setCreatedDate(dbConfig.getCreatedDate());
				}
			}
			Technique technique = config.getTechnique();
			// check if technique already exists;
			Technique dbTechnique = findTechniqueByType(technique.getType());
			if (dbTechnique != null) {
				technique.setId(dbTechnique.getId());
				technique.setCreatedBy(dbTechnique.getCreatedBy());
				technique.setCreatedDate(dbTechnique.getCreatedDate());
			} else {
				technique.setId(null);
				technique.setCreatedBy(config.getCreatedBy());
				technique.setCreatedDate(new Date());
			}
			// check if instrument already exists;
			if (config.getInstrumentCollection() != null) {
				Collection<Instrument> instruments = new HashSet<Instrument>(
						config.getInstrumentCollection());
				config.setInstrumentCollection(new HashSet<Instrument>());
				for (Instrument instrument : instruments) {
					Instrument dbInstrument = findInstrumentBy(instrument
							.getType(), instrument.getManufacturer(),
							instrument.getModelName());
					if (dbInstrument != null) {
						instrument.setId(dbInstrument.getId());
						instrument.setCreatedBy(dbInstrument.getCreatedBy());
						instrument
								.setCreatedDate(dbInstrument.getCreatedDate());
					} else {
						instrument.setId(null);
					}
					config.getInstrumentCollection().add(instrument);
				}
			}
			appService.saveOrUpdate(config);
			// visibility assignment is handled by saving characterization

		} catch (Exception e) {
			String err = "Error in saving the technique and associated instruments.";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
	}

	public List<String> deleteExperimentConfig(ExperimentConfig config,
			UserBean user, Boolean removeVisibility)
			throws ExperimentConfigException, NoAccessException {
		if (user == null || !(user.isCurator() && user.isAdmin())) {
			throw new NoAccessException();
		}
		List<String> entries = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// get existing createdDate and createdBy
			if (config.getId() != null) {
				ExperimentConfig dbConfig = helper.findExperimentConfigById(
						config.getId().toString(), user);
				if (dbConfig != null) {
					config.setCreatedBy(dbConfig.getCreatedBy());
					config.setCreatedDate(dbConfig.getCreatedDate());
				}
			}
			Technique technique = config.getTechnique();
			// check if technique already exists;
			Technique dbTechnique = findTechniqueByType(technique.getType());
			if (dbTechnique != null) {
				technique.setId(dbTechnique.getId());
				technique.setCreatedBy(dbTechnique.getCreatedBy());
				technique.setCreatedDate(dbTechnique.getCreatedDate());
			} else {
				technique.setCreatedBy(config.getCreatedBy());
				technique.setCreatedDate(new Date());
				// need to save the transient object before deleting the
				// experiment config
				appService.saveOrUpdate(technique);
			}
			// check if instrument already exists;
			if (config.getInstrumentCollection() != null) {
				Collection<Instrument> instruments = new HashSet<Instrument>(
						config.getInstrumentCollection());
				config.getInstrumentCollection().clear();
				int i = 0;
				for (Instrument instrument : instruments) {
					Instrument dbInstrument = findInstrumentBy(instrument
							.getType(), instrument.getManufacturer(),
							instrument.getModelName());
					if (dbInstrument != null) {
						instrument.setId(dbInstrument.getId());
						instrument.setCreatedBy(dbInstrument.getCreatedBy());
						instrument
								.setCreatedDate(dbInstrument.getCreatedDate());
					} else {
						instrument.setCreatedBy(config.getCreatedBy());
						instrument.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(i));
						// need to save the transient object before deleting the
						// experiment config
						appService.saveOrUpdate(instrument);
					}
					config.getInstrumentCollection().add(instrument);
				}
			}
			appService.delete(config);
			entries = removeVisibility(config, removeVisibility);
		} catch (Exception e) {
			String err = "Error in deleting the technique and associated instruments";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
		return entries;
	}

	private Technique findTechniqueByType(String type)
			throws ExperimentConfigException {
		Technique technique = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class)
					.add(
							Property.forName("type").eq(new String(type))
									.ignoreCase());
			List results = appService.query(crit);
			for (Object obj : results) {
				technique = (Technique) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve technique by type.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return technique;
	}

	private Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws ExperimentConfigException {
		Instrument instrument = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class);
			crit.add(Restrictions.eq("type", type).ignoreCase());
			crit
					.add(Restrictions.eq("manufacturer", manufacturer)
							.ignoreCase());
			crit.add(Restrictions.eq("modelName", modelName).ignoreCase());
			List results = appService.query(crit);
			for (Object obj : results) {
				instrument = (Instrument) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve instrument by type, manufacturer, and model name.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return instrument;
	}

	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans,
			boolean copyData, UserBean user) throws CharacterizationException,
			NoAccessException {
		try {
			for (SampleBean sampleBean : newSampleBeans) {
				// create a deep copy
				Characterization copy = charBean.getDomainCopy(copyData);
				CharacterizationBean copyBean = new CharacterizationBean(copy);
				// try {
				// // copy file visibility
				// for (FindingBean findingBean : copyBean.getFindings()) {
				// for (FileBean fileBean : findingBean.getFiles()) {
				// fileHelper
				// .retrieveVisibilityAndContentForCopiedFile(
				// fileBean, user);
				// }
				// }
				// } catch (Exception e) {
				// String error = "Error setting visibility of the copy.";
				// throw new CharacterizationException(error, e);
				// }
				/**
				 * Need to save associate Config & Finding in copy bean first,
				 * otherwise will get "transient object" Hibernate exception.
				 */
				List<ExperimentConfigBean> expConfigs = copyBean
						.getExperimentConfigs();
				if (expConfigs != null && !expConfigs.isEmpty()) {
					for (ExperimentConfigBean configBean : expConfigs) {
						this.saveExperimentConfig(configBean, user);
					}
				}
				List<FindingBean> findings = copyBean.getFindings();
				// copy file visibility and replace file URI with new sample
				// name
				if (findings != null && !findings.isEmpty()) {
					for (FindingBean findingBean : findings) {
						for (FileBean fileBean : findingBean.getFiles()) {
							fileService.updateClonedFileInfo(fileBean,
									oldSampleBean.getDomain().getName(),
									sampleBean.getDomain().getName(), user);
						}
						this.saveFinding(findingBean, user);
					}
				}
				this.saveCharacterization(sampleBean, copyBean, user);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving the copy of characterization.";
			throw new CharacterizationException(error, e);
		}
	}

	public int getNumberOfPublicCharacterizations(
			String characterizationClassName) throws CharacterizationException {
		try {
			int count = helper
					.getNumberOfPublicCharacterizations(characterizationClassName);
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public characterizations of type "
					+ characterizationClassName;
			logger.error(err, e);
			throw new CharacterizationException(err, e);

		}
	}

	protected void assignVisibility(Characterization aChar,
			String[] visibleGroups, String owningGroup) throws Exception {
		// characterization
		if (aChar != null && aChar.getId() != null) {
			helper.getAuthService().assignVisibility(aChar.getId().toString(),
					visibleGroups, owningGroup);
			if (aChar.getFindingCollection() != null) {
				for (Finding finding : aChar.getFindingCollection()) {
					if (finding != null) {
						helper.getAuthService().assignVisibility(
								finding.getId().toString(), visibleGroups,
								owningGroup);
					}
					// datum, need to check for null for copy bean.
					if (finding.getDatumCollection() != null) {
						for (Datum datum : finding.getDatumCollection()) {
							if (datum != null && datum.getId() != null) {
								helper.getAuthService().assignVisibility(
										datum.getId().toString(),
										visibleGroups, owningGroup);
							}
							// condition
							if (datum.getConditionCollection() != null) {
								for (Condition condition : datum
										.getConditionCollection()) {
									helper.getAuthService().assignVisibility(
											condition.getId().toString(),
											visibleGroups, owningGroup);
								}
							}
						}
					}
				}
			}
			// ExperimentConfiguration
			if (aChar.getExperimentConfigCollection() != null) {
				for (ExperimentConfig config : aChar
						.getExperimentConfigCollection()) {
					helper.getAuthService().assignVisibility(
							config.getId().toString(), visibleGroups,
							owningGroup);
					// assign instruments and technique to public visibility
					if (config.getTechnique() != null) {
						helper.getAuthService().assignVisibility(
								config.getTechnique().getId().toString(),
								new String[] { Constants.CSM_PUBLIC_GROUP },
								null);
					}
					if (config.getInstrumentCollection() != null) {
						for (Instrument instrument : config
								.getInstrumentCollection()) {
							helper
									.getAuthService()
									.assignVisibility(
											instrument.getId().toString(),
											new String[] { Constants.CSM_PUBLIC_GROUP },
											null);
						}
					}
				}
			}
		}
	}

	protected List<String> removeVisibility(Characterization aChar,
			Boolean remove) throws Exception {
		List<String> entries = new ArrayList<String>();
		// characterization
		if (aChar != null) {
			if (remove == null || remove) {
				helper.getAuthService()
						.removeCSMEntry(aChar.getId().toString());
			}
			entries.add(aChar.getId().toString());
			for (Finding finding : aChar.getFindingCollection()) {
				if (finding != null) {
					entries.addAll(removeVisibility(finding, remove));
				}
			}

			// ExperimentConfiguration
			for (ExperimentConfig config : aChar
					.getExperimentConfigCollection()) {
				entries.addAll(removeVisibility(config, remove));
			}
		}
		return entries;
	}

	private List<String> removeVisibility(ExperimentConfig config,
			Boolean remove) throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove) {
			helper.getAuthService().removeCSMEntry(config.getId().toString());
			helper.getAuthService().removeCSMEntry(
					config.getTechnique().getId().toString());
		}
		entries.add(config.getId().toString());
		entries.add(config.getTechnique().getId().toString());
		if (config.getInstrumentCollection() != null) {
			for (Instrument instrument : config.getInstrumentCollection()) {
				if (remove == null || remove) {
					helper.getAuthService().removeCSMEntry(
							instrument.getId().toString());
				}
				entries.add(instrument.getId().toString());
			}
		}
		return entries;
	}

	private List<String> removeVisibility(Finding finding, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (remove == null || remove) {
			helper.getAuthService().removeCSMEntry(finding.getId().toString());
		}
		entries.add(finding.getId().toString());

		// datum
		if (finding.getDatumCollection() != null) {
			for (Datum datum : finding.getDatumCollection()) {
				if (datum != null) {
					if (remove == null || remove) {
						helper.getAuthService().removeCSMEntry(
								datum.getId().toString());
					}
					entries.add(datum.getId().toString());
				}
				if (datum.getConditionCollection() != null) {
					for (Condition condition : datum.getConditionCollection()) {
						if (remove == null || remove) {
							helper.getAuthService().removeCSMEntry(
									condition.getId().toString());
						}
						entries.add(condition.getId().toString());
					}
				}
			}
		}
		// file
		if (finding.getFileCollection() != null) {
			for (File file : finding.getFileCollection()) {
				if (remove == null || remove) {
					helper.getAuthService().removeCSMEntry(
							file.getId().toString());
				}
				entries.add(file.getId().toString());
			}
		}
		return entries;
	}
}