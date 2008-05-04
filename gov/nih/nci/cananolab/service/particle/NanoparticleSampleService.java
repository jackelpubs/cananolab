package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.SortableName;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving nanoparticle samples
 * 
 * @author pansu
 * 
 */
public class NanoparticleSampleService {
	private static Logger logger = Logger
			.getLogger(NanoparticleSampleService.class);

	/**
	 * 
	 * @return all particle sources
	 */
	public SortedSet<Source> findAllParticleSources() throws ParticleException {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(Source.class);
			for (Object obj : results) {
				sampleSources.add((Source) obj);
			}
			return sampleSources;
		} catch (Exception e) {
			String err = "Error in retrieving all nanoparticle sample sources";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * 
	 * @return all particle sources visible to user
	 */
	public SortedSet<Source> findAllParticleSources(UserBean user)
			throws ParticleException {
		SortedSet<Source> sampleSources = new TreeSet<Source>(
				new CaNanoLabComparators.ParticleSourceComparator());
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Source.class);
			crit.setFetchMode("nanoparticleSampleCollection", FetchMode.JOIN);
			List results = appService.query(crit);
			for (Object obj : results) {
				Source source = (Source) obj;
				// if user can access at least one particle from the source, set
				// access to true
				List<String> particleNames = new ArrayList<String>();
				for (NanoparticleSample sample : source
						.getNanoparticleSampleCollection()) {
					particleNames.add(sample.getName());
				}
				if (auth.isAllowedAtLeastOne(auth, particleNames, user)) {
					sampleSources.add((Source) obj);
				}
			}
			return sampleSources;
		} catch (Exception e) {
			String err = "Error in retrieving all nanoparticle sample sources for a user";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * Persist a new nanoparticle sample or update an existing nanoparticle
	 * sample
	 * 
	 * @param particleSample
	 * @throws Exception
	 */
	public void saveNanoparticleSample(NanoparticleSample particleSample)
			throws ParticleException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			NanoparticleSample dbParticle = (NanoparticleSample) appService
					.getObject(NanoparticleSample.class, "name", particleSample
							.getName());
			if (dbParticle != null
					&& !dbParticle.getId().equals(particleSample.getId())) {
				throw new DuplicateEntriesException(
						"This nanoparticle sample ID has already been used.  Please use a different one");
			}
			Source dbSource = (Source) appService.getObject(Source.class,
					"organizationName", particleSample.getSource()
							.getOrganizationName());
			if (dbSource != null) {
				particleSample.getSource().setId(dbSource.getId());
			}

			for (Keyword keyword : particleSample.getKeywordCollection()) {
				Keyword dbKeyword = (Keyword) appService.getObject(
						Keyword.class, "name", keyword.getName());
				if (dbKeyword != null) {
					keyword.setId(dbKeyword.getId());
				}
			}
			appService.saveOrUpdate(particleSample);
		} catch (Exception e) {
			String err = "Error in saving the nanoparticle sample.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * 
	 * @param particleSource
	 * @param nanoparticleEntityClassNames
	 * @param functionalizingEntityClassNames
	 * @param functionClassNames
	 * @param characterizationClassNames
	 * @param keywords
	 * @param summaries
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public List<ParticleBean> findNanoparticleSamplesBy(
			String[] particleSources, String[] nanoparticleEntityClassNames,
			String[] functionalizingEntityClassNames,
			String[] functionClassNames, String[] characterizationClassNames,
			String[] wordList) throws ParticleException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		try {
			DetachedCriteria crit = DetachedCriteria
					.forClass(NanoparticleSample.class);
			if (particleSources != null && particleSources.length > 0) {
				crit.createAlias("source", "source",
						CriteriaSpecification.LEFT_JOIN).add(
						Restrictions.in("source.organizationName",
								particleSources));
			}

			if (wordList != null && wordList.length > 0) {
				// turn words into upper case before searching keywords
				String[] upperKeywords = new String[wordList.length];
				for (int i = 0; i < wordList.length; i++) {
					upperKeywords[i] = wordList[i].toUpperCase();
				}
				Disjunction disjunction = Restrictions.disjunction();
				crit.createAlias("keywordCollection", "keyword1",
						CriteriaSpecification.LEFT_JOIN);
				for (String keyword : upperKeywords) {
					Criterion keywordCrit1 = Restrictions.like("keyword1.name",
							keyword, MatchMode.ANYWHERE);
					disjunction.add(keywordCrit1);
				}
				crit.createAlias("characterizationCollection", "chara",
						CriteriaSpecification.LEFT_JOIN).createAlias(
						"chara.derivedBioAssayDataCollection", "derived",
						CriteriaSpecification.LEFT_JOIN).createAlias(
						"derived.labFile", "charFile",
						CriteriaSpecification.LEFT_JOIN).createAlias(
						"charFile.keywordCollection", "keyword2",
						CriteriaSpecification.LEFT_JOIN);
				;
				for (String keyword : upperKeywords) {
					Criterion keywordCrit2 = Restrictions.like("keyword2.name",
							keyword, MatchMode.ANYWHERE);
					disjunction.add(keywordCrit2);
				}
				for (String word : wordList) {
					Criterion summaryCrit1 = Restrictions.ilike(
							"chara.description", word, MatchMode.ANYWHERE);
					Criterion summaryCrit2 = Restrictions.ilike(
							"charFile.description", word, MatchMode.ANYWHERE);
					Criterion summaryCrit = Restrictions.or(summaryCrit1,
							summaryCrit2);
					disjunction.add(summaryCrit);
				}
				crit.add(disjunction);
			}
			crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.nanoparticleEntityCollection.composingElementCollection",
							FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.nanoparticleEntityCollection.composingElementCollection.inherentFunctionCollection",
							FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.functionalizingEntityCollection",
					FetchMode.JOIN);
			crit
					.setFetchMode(
							"sampleComposition.functionalizingEntityCollection.functionCollection",
							FetchMode.JOIN);
			crit.setFetchMode("reportCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List results = appService.query(crit);
			for (Object obj : results) {
				NanoparticleSample particle = (NanoparticleSample) obj;
				particles.add(new ParticleBean(particle));
			}
			List<ParticleBean> functionFilteredParticles = filterByFunctions(
					functionClassNames, particles);
			List<ParticleBean> characterizationFilteredParticles = filterByCharacterizations(
					characterizationClassNames, functionFilteredParticles);
			List<ParticleBean> theParticles = filterByCompositions(
					nanoparticleEntityClassNames,
					functionalizingEntityClassNames,
					characterizationFilteredParticles);
			Collections.sort(theParticles,
					new CaNanoLabComparators.ParticleBeanComparator());
			return theParticles;
		} catch (Exception e) {
			String err = "Problem finding particles with the given search parameters.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	private List<ParticleBean> filterByFunctions(String[] functionClassNames,
			List<ParticleBean> particles) {
		if (functionClassNames != null && functionClassNames.length > 0) {
			List<ParticleBean> filteredList = new ArrayList<ParticleBean>();
			for (ParticleBean particle : particles) {
				SortedSet<String> storedFunctions = getStoredFunctionClassNames(particle
						.getDomainParticleSample());
				for (String func : functionClassNames) {
					// if at least one function type matches, keep the particle
					if (storedFunctions.contains(func)) {
						filteredList.add(particle);
						break;
					}
				}
			}
			return filteredList;
		} else {
			return particles;
		}
	}

	private List<ParticleBean> filterByCharacterizations(
			String[] characterizationClassNames, List<ParticleBean> particles) {
		if (characterizationClassNames != null
				&& characterizationClassNames.length > 0) {
			List<ParticleBean> filteredList = new ArrayList<ParticleBean>();
			for (ParticleBean particle : particles) {
				SortedSet<String> storedChars = getStoredCharacterizationClassNames(particle);
				for (String func : characterizationClassNames) {
					// if at least one characterization type matches, keep the
					// particle
					if (storedChars.contains(func)) {
						filteredList.add(particle);
						break;
					}
				}
			}
			return filteredList;
		} else {
			return particles;
		}
	}

	private List<ParticleBean> filterByCompositions(
			String[] nanoparticleEntityClassNames,
			String[] functionalizingEntityClassNames,
			List<ParticleBean> particles) {

		List<ParticleBean> filteredList1 = new ArrayList<ParticleBean>();
		if (nanoparticleEntityClassNames != null
				&& nanoparticleEntityClassNames.length > 0) {
			for (ParticleBean particle : particles) {
				SortedSet<String> storedEntities = getStoredNanoparticleEntityClassNames(particle
						.getDomainParticleSample());
				for (String entity : nanoparticleEntityClassNames) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(entity)) {
						filteredList1.add(particle);
						break;
					}
				}
			}
		} else {
			filteredList1 = particles;
		}
		List<ParticleBean> filteredList2 = new ArrayList<ParticleBean>();
		if (functionalizingEntityClassNames != null
				&& functionalizingEntityClassNames.length > 0) {
			for (ParticleBean particle : particles) {
				SortedSet<String> storedEntities = getStoredFunctionalizingEntityClassNames(particle
						.getDomainParticleSample());
				for (String entity : functionalizingEntityClassNames) {
					// if at least one function type matches, keep the particle
					if (storedEntities.contains(entity)) {
						filteredList2.add(particle);
						break;
					}
				}
			}
		} else {
			filteredList2 = particles;
		}
		if (filteredList1.size() > filteredList2.size()) {
			filteredList1.retainAll(filteredList2);
			return filteredList1;
		} else {
			filteredList2.retainAll(filteredList1);
			return filteredList2;
		}
	}

	public ParticleBean findNanoparticleSampleById(String particleId)
			throws ParticleException {
		ParticleBean particleBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleSample.class).add(
					Property.forName("id").eq(new Long(particleId)));
			crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.functionalizingEntityCollection",
					FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.labFileCollection",
					FetchMode.JOIN);
			crit.setFetchMode("reportCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				NanoparticleSample particleSample = (NanoparticleSample) result
						.get(0);
				particleBean = new ParticleBean(particleSample);
			}
			return particleBean;
		} catch (Exception e) {
			String err = "Problem finding the particle by id: " + particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws ParticleException {
		NanoparticleSample particleSample = null;
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleSample.class).add(
					Property.forName("name").eq(particleName));
			crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.nanoparticleEntityCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.chemicalAssociationCollection",
					FetchMode.JOIN);
			crit.setFetchMode(
					"sampleComposition.functionalizingEntityCollection",
					FetchMode.JOIN);
			crit.setFetchMode("sampleComposition.labFileCollection",
					FetchMode.JOIN);
			crit.setFetchMode("reportCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				particleSample = (NanoparticleSample) result.get(0);
			}
			return particleSample;
		} catch (Exception e) {
			String err = "Problem finding the particle by name: "
					+ particleName;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	/**
	 * Get other particles from the given particle source
	 * 
	 * @param particleSource
	 * @param particleName
	 * @param user
	 * @return
	 * @throws ParticleException
	 * @throws CaNanoLabSecurityException
	 */
	public SortedSet<SortableName> findOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException {
		SortedSet<SortableName> otherParticles = new TreeSet<SortableName>();
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);

			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(NanoparticleSample.class);
			crit.add(Restrictions.ne("name", particleName));
			crit.createAlias("source", "source").add(
					Restrictions.eq("source.organizationName", particleSource));

			List results = appService.query(crit);
			for (Object obj : results) {
				NanoparticleSample particle = (NanoparticleSample) obj;
				if (auth.isUserAllowed(particle.getName(), user)) {
					otherParticles.add(new SortableName(particle.getName()));
				}
			}
			return otherParticles;
		} catch (Exception e) {
			String err = "Error in retrieving other particles from source "
					+ particleSource;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public List<ParticleBean> getAllowedParticles(AuthorizationService auth,
			List<ParticleBean> particles, UserBean user)
			throws ParticleException {
		try {
			List<String> publicData = auth.getPublicData();
			List<ParticleBean> allowedParticles = new ArrayList<ParticleBean>();
			for (ParticleBean particle : particles) {
				if (publicData.contains(particle.getDomainParticleSample()
						.getName())) {
					allowedParticles.add(particle);
				} else if (user != null) {
					if (auth.checkReadPermission(user, particle
							.getDomainParticleSample().getName())) {
						allowedParticles.add(particle);
					}
				}
			}
			return allowedParticles;
		} catch (Exception e) {
			String err = "Error in retrieving allowed particles for a user.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public SortedSet<String> getStoredCharacterizationClassNames(
			ParticleBean particle) {
		SortedSet<String> storedChars = new TreeSet<String>();
		if (particle.getDomainParticleSample().getCharacterizationCollection() != null) {
			for (Characterization achar : particle.getDomainParticleSample()
					.getCharacterizationCollection()) {
				storedChars.add(ClassUtils.getShortClassName(achar.getClass()
						.getCanonicalName()));
			}
		}
		return storedChars;
	}

	public SortedSet<String> getStoredFunctionalizingEntityClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : particleSample
					.getSampleComposition()
					.getFunctionalizingEntityCollection()) {
				storedEntities.add(ClassUtils.getShortClassName(entity
						.getClass().getCanonicalName()));
			}
		}
		return storedEntities;
	}

	public SortedSet<String> getStoredFunctionClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedFunctions = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null) {
			if (particleSample.getSampleComposition()
					.getNanoparticleEntityCollection() != null) {
				for (NanoparticleEntity entity : particleSample
						.getSampleComposition()
						.getNanoparticleEntityCollection()) {
					for (ComposingElement element : entity
							.getComposingElementCollection()) {
						for (Function function : element
								.getInherentFunctionCollection()) {
							storedFunctions.add(ClassUtils
									.getShortClassName(function.getClass()
											.getCanonicalName()));
						}
					}
				}
			}
			if (particleSample.getSampleComposition()
					.getFunctionalizingEntityCollection() != null) {
				for (FunctionalizingEntity entity : particleSample
						.getSampleComposition()
						.getFunctionalizingEntityCollection()) {
					for (Function function : entity.getFunctionCollection()) {
						storedFunctions.add(ClassUtils
								.getShortClassName(function.getClass()
										.getCanonicalName()));
					}
				}
			}
		}
		return storedFunctions;
	}

	public SortedSet<String> getStoredNanoparticleEntityClassNames(
			NanoparticleSample particleSample) {
		SortedSet<String> storedEntities = new TreeSet<String>();

		if (particleSample.getSampleComposition() != null
				&& particleSample.getSampleComposition()
						.getNanoparticleEntityCollection() != null) {
			for (NanoparticleEntity entity : particleSample
					.getSampleComposition().getNanoparticleEntityCollection()) {
				storedEntities.add(ClassUtils.getShortClassName(entity
						.getClass().getCanonicalName()));
			}
		}
		return storedEntities;
	}

	public void retrieveVisibility(ParticleBean particleBean, UserBean user)
			throws ParticleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			if (auth.isUserAllowed(particleBean.getDomainParticleSample()
					.getName(), user)) {
				particleBean.setHidden(false);
				// get assigned visible groups
				List<String> accessibleGroups = auth.getAccessibleGroups(
						particleBean.getDomainParticleSample().getName(),
						CaNanoLabConstants.CSM_READ_ROLE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				particleBean.setVisibilityGroups(visibilityGroups);
			} else {
				particleBean.setHidden(true);
			}
		} catch (Exception e) {
			String err = "Error in setting visibility groups for particle sample "
					+ particleBean.getDomainParticleSample().getName();
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public void deleteAnnotationById(String className, Long dataId)
			throws ParticleException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			if (className.endsWith("NanoparticleEntity")) {
				NanoparticleCompositionService compService = new NanoparticleCompositionService();
				NanoparticleEntityBean entityBean = compService
						.findNanoparticleEntityById(dataId.toString());
				compService.deleteNanoparticleEntity(entityBean
						.getDomainEntity());
			} else if (className.endsWith("FunctionalizingEntity")) {
				NanoparticleCompositionService compService = new NanoparticleCompositionService();
				FunctionalizingEntityBean entityBean = compService
						.findFunctionalizingEntityById(dataId.toString());
				compService.deleteFunctionalizingEntity(entityBean
						.getDomainEntity());

			} else {
				appService.deleteById(Class.forName(className), dataId);
			}
		} catch (Exception e) {
			String err = "Error deleting annotation of class " + className
					+ "by ID " + dataId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public SortedSet<String> findAllNanoparticleSampleNames(UserBean user)
			throws ParticleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);

			SortedSet<String> names = new TreeSet<String>();
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select sample.name from gov.nih.nci.cananolab.domain.particle.NanoparticleSample sample");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = (String) obj;
				if (auth.isUserAllowed(name, user)) {
					names.add(name);
				}
			}
			return names;
		} catch (Exception e) {
			String err = "Error finding samples for " + user.getLoginName();
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}
}
