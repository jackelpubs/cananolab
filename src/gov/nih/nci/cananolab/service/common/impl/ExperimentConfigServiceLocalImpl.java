package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

public class ExperimentConfigServiceLocalImpl implements
		ExperimentConfigService {
	private static Logger logger = Logger
			.getLogger(ExperimentConfigServiceLocalImpl.class);

	public void saveExperimentConfig(ExperimentConfig config)
			throws ExperimentConfigException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			Technique technique = config.getTechnique();
			// check if technique already exists;
			if (technique.getId() == null) {
				Technique doTechnique = findTechniqueByType(technique.getType());
				technique = doTechnique;
			}
			// check if instrument already exists;
			if (config.getInstrumentCollection() != null) {
				Collection<Instrument> instruments = new HashSet<Instrument>(
						config.getInstrumentCollection());
				config.getInstrumentCollection().clear();
				for (Instrument instrument : instruments) {
					Instrument dbInstrument = findInstrumentBy(instrument
							.getType(), instrument.getManufacturer(),
							instrument.getModelName());
					if (dbInstrument != null) {
						instrument.setId(dbInstrument.getId());
					}
					config.getInstrumentCollection().add(instrument);
				}
			}
			appService.saveOrUpdate(config);
		} catch (Exception e) {
			String err = "Error in saving the technique and associated instruments.";
			logger.error(err, e);
			throw new ExperimentConfigException(err, e);
		}
	}

	public List<Technique> findAllTechniques() throws ExperimentConfigException {
		List<Technique> techniques = new ArrayList<Technique>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class);
			List results = appService.query(crit);
			for (Object obj : results) {
				Technique technique = (Technique) obj;
				techniques.add(technique);
			}
			Collections.sort(techniques,
					new CaNanoLabComparators.TechniqueComparator());
		} catch (Exception e) {
			String err = "Problem to retrieve all techniques.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return techniques;
	}

	public List<String> getAllManufacturers() throws ExperimentConfigException {
		List<String> manufacturers = new ArrayList<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class)
					.setProjection(
							Projections.distinct(Property
									.forName("manufacturer")));
			;
			List results = appService.query(crit);
			for (Object obj : results) {
				String manufacturer = (String) obj;
				manufacturers.add(manufacturer);
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all manufacturers.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		return manufacturers;
	}

	public ExperimentConfigBean findExperimentConfigById(String id)
			throws ExperimentConfigException {
		ExperimentConfig config = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					ExperimentConfig.class).add(
					Property.forName("id").eq(new Long(id)));
			List results = appService.query(crit);
			for (Object obj : results) {
				config = (ExperimentConfig) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all manufacturers.";
			logger.error(err, e);
			throw new ExperimentConfigException(err);
		}
		if (config!=null) {
			return new ExperimentConfigBean(config);
		}else {
			return null;
		}		
	}

	public Technique findTechniqueByType(String type)
			throws ExperimentConfigException {
		Technique technique = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Technique.class)
					.add(Property.forName("type").eq(new Long(type)));
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

	public Instrument findInstrumentBy(String type, String manufacturer,
			String modelName) throws ExperimentConfigException {
		Instrument instrument = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class);
			if (type != null || type.length() > 0) {
				crit.add(Restrictions.eq("type", type));
			}
			if (manufacturer != null || manufacturer.length() > 0) {
				crit.add(Restrictions.eq("manufacturer", manufacturer));
			}
			if (modelName != null || modelName.length() > 0) {
				crit.add(Restrictions.eq("modelName", modelName));
			}
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
}
