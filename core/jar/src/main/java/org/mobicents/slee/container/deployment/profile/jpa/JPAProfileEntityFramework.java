package org.mobicents.slee.container.deployment.profile.jpa;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.ejb.QueryImpl;
import org.jboss.jpa.deployment.PersistenceUnitInfoImpl;
import org.jboss.metadata.jpa.spec.PersistenceUnitMetaData;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.profile.ProfileAttribute;
import org.mobicents.slee.container.component.profile.ProfileEntity;
import org.mobicents.slee.container.component.profile.ProfileEntityFactory;
import org.mobicents.slee.container.component.profile.ProfileEntityFramework;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * The profile entity framework implementation that uses JPA to manage SLEE
 * profile data.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author martins
 */
public class JPAProfileEntityFramework implements ProfileEntityFramework {

	private static Logger logger = Logger
			.getLogger(JPAProfileEntityFramework.class);

	private static final String DEFAULT_PROFILE_NAME = "";

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	/**
	 * the concrete jpa profile entity class of the framework
	 */
	private Class<?> profileEntityClass;

	/**
	 * runtime caching the profile entity class name, used many times in jpa
	 * queries
	 */
	private String profileEntityClassName;

	/**
	 * the concrete jpa profile entity factory class of the framework
	 */
	private Class<?> profileEntityFactoryClass;

	/**
	 * a map containing the concrete jpa profile entity array attr value class
	 * of the framework, per attribute name
	 */
	private Map<String, Class<?>> profileEntityArrayAttrValueClassMap;

	/**
	 * the profile spec component related with the framework
	 */
	private final ProfileSpecificationComponent component;

	/**
	 * the jpa entity manager factory of the framework
	 */
	private EntityManagerFactory entityManagerFactory;

	/**
	 * the shared instance of the profile entity factory of the framework
	 */
	private ProfileEntityFactory profileEntityFactory;

	/**
	 * 
	 * @param component
	 */
	public JPAProfileEntityFramework(ProfileSpecificationComponent component) {
		this.component = component;
		this.component.setProfileEntityFramework(this);
	}

	// GETTERS / SETTERS

	/**
	 * 
	 */
	public Class<?> getProfileEntityClass() {
		return profileEntityClass;
	}

	/**
	 * 
	 * @param profileEntityClass
	 */
	public void setProfileEntityClass(Class<?> profileEntityClass) {
		this.profileEntityClass = profileEntityClass;
	}

	/**
	 * 
	 * @return
	 */
	public String getProfileEntityClassName() {
		return profileEntityClassName;
	}

	/**
	 * 
	 * @param profileEntityClassName
	 */
	public void setProfileEntityClassName(String profileEntityClassName) {
		this.profileEntityClassName = profileEntityClassName;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Class<?>> getProfileEntityArrayAttrValueClassMap() {
		return profileEntityArrayAttrValueClassMap;
	}

	/**
	 * 
	 * @param profileEntityArrayAttrValueClassMap
	 */
	public void setProfileEntityArrayAttrValueClassMap(
			Map<String, Class<?>> profileEntityArrayAttrValueClassMap) {
		this.profileEntityArrayAttrValueClassMap = profileEntityArrayAttrValueClassMap;
	}

	/**
	 * 
	 * @return
	 */
	public Class<?> getProfileEntityFactoryClass() {
		return profileEntityFactoryClass;
	}

	// ProfileEntityFramework IMPL

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #getProfileEntityFactory()
	 */
	public ProfileEntityFactory getProfileEntityFactory() {
		return profileEntityFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #findAll(java.lang.String)
	 */
	public Collection<ProfileEntity> findAll(String profileTable) {
		return findProfilesByAttribute(profileTable, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #findProfilesByAttribute(java.lang.String,
	 * org.mobicents.slee.container.component.profile.ProfileAttribute,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Collection<ProfileEntity> findProfilesByAttribute(
			String profileTable, ProfileAttribute profileAttribute,
			Object attributeValue) {

		if (logger.isDebugEnabled()) {
			logger.debug("findProfilesByAttribute( profileTable = "
					+ profileTable + " , profileAttribute = "
					+ profileAttribute + " , attributeValue = "
					+ attributeValue + " )");
		}

		EntityManager em = getEntityManager();
		Query query = null;
		if (profileAttribute == null) {
			query = em
					.createQuery(
							"SELECT x FROM "
									+ profileEntityClassName
									+ " x WHERE x.tableName = :tableName")
					.setParameter("tableName", profileTable);
		} else {
			if (profileAttribute.isArray()) {
				query = em
						.createQuery(
								"SELECT x FROM "
										+ profileEntityClassName
										+ " x , IN (x.c"
										+ profileAttribute.getName()
										+ ") y WHERE x.tableName = :tableName AND y.string = :attrValue")
						.setParameter("tableName", profileTable).setParameter(
								"attrValue", attributeValue.toString());
			} else {
				// TODO handle Address objects in this use case, they can't be
				// binary for search
				query = em
						.createQuery(
								"SELECT x FROM "
										+ profileEntityClassName
										+ " x WHERE x.tableName = :tableName AND x.c"
										+ profileAttribute.getName()
										+ " = :attrValue").setParameter(
								"tableName", profileTable).setParameter(
								"attrValue", attributeValue);
			}
		}

		Collection<ProfileEntity> result = query.getResultList();
		if (logger.isDebugEnabled()) {
			logger.debug("findProfilesByAttribute : query = "
					+ ((QueryImpl) query).getHibernateQuery().getQueryString()
					+ " , result = " + result);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #findProfile(java.lang.String, java.lang.String)
	 */
	public ProfileEntity findProfile(String profileTable, String profileName) {

		EntityManager em = getEntityManager();
		Query query = em.createQuery(
				"FROM " + profileEntityClassName
						+ " WHERE tableName = ?1 AND profileName = ?2")
				.setParameter(1, profileTable).setParameter(2, profileName);

		List<?> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return (ProfileEntity) resultList.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #getProfilesByStaticQuery(java.lang.String, java.lang.String,
	 * java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Collection<ProfileEntity> getProfilesByStaticQuery(
			String profileTable, String queryName, Object[] parameters)
			throws NullPointerException, UnrecognizedQueryNameException,
			AttributeTypeMismatchException, InvalidArgumentException {

		// TODO check for exceptions

		QueryWrapper wQuery = JPAQueryBuilder.getQuery(queryName);

		EntityManager em = getEntityManager();

		Query staticQuery = em.createQuery(wQuery
				.getQuerySQL(profileEntityClassName));

		if (wQuery.getMaxMatches() > 0)
			staticQuery.setMaxResults((int) wQuery.getMaxMatches());

		for (int i = 0; i < parameters.length; i++) {
			try {
				staticQuery.setParameter(i + 1, parameters[i]);
			} catch (Exception ignore) {
				// We don't care, it's because there's no such parameter.
			}
		}

		return staticQuery.getResultList();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #getProfilesByDynamicQuery(java.lang.String,
	 * javax.slee.profile.query.QueryExpression)
	 */
	@SuppressWarnings("unchecked")
	public Collection<ProfileEntity> getProfilesByDynamicQuery(
			String profileTable, QueryExpression expr)
			throws UnrecognizedAttributeException,
			AttributeTypeMismatchException {

		// TODO check for exceptions

		QueryWrapper wQuery = JPAQueryBuilder.parseDynamicQuery(expr);

		EntityManager em = getEntityManager();
		Query dynamicQuery = em.createQuery(wQuery
				.getQuerySQL(profileEntityClassName));

		int i = 1;
		for (Object param : wQuery.getDynamicParameters()) {
			dynamicQuery.setParameter(i++, param);
		}

		if (wQuery.getMaxMatches() > 0)
			dynamicQuery.setMaxResults((int) wQuery.getMaxMatches());

		return dynamicQuery.getResultList();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #persistProfile
	 * (org.mobicents.slee.container.component.profile.ProfileEntity)
	 */
	public void persistProfile(ProfileEntity profileEntity) {
		EntityManager em = null;

		// if(checkUniqueFields(profileObject))
		// {
		em = getEntityManager();
		em.persist(profileEntity);
		/*
		 * } else { // FIXME: We need to throw this PVException! //throw new
		 * ProfileVerificationException
		 * ("Failed to persist profile due to uniqueness constraint."); throw
		 * new
		 * SLEEException("Failed to persist profile due to uniqueness constraint."
		 * ); }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #retrieveProfile(java.lang.String, java.lang.String)
	 */
	public ProfileEntity retrieveProfile(String profileTable, String profileName) {

		if (logger.isDebugEnabled()) {
			logger.debug("retrieveProfile( profileTableName = " + profileTable
					+ " , profileName = " + profileName + " )");
		}

		if (profileName == null) {
			profileName = DEFAULT_PROFILE_NAME;
		}

		EntityManager em = getEntityManager();

		Query q = em.createQuery(
				"FROM " + profileEntityClassName
						+ " WHERE tableName = ?1 AND profileName = ?2")
				.setParameter(1, profileTable).setParameter(2, profileName);

		List<?> resultList = q.getResultList();
		if (resultList.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("ProfileEntity retrieved -> " + resultList.get(0));
			}
			return (ProfileEntity) resultList.get(0);
		} else {
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #removeprofile
	 * (org.mobicents.slee.container.component.profile.ProfileEntity)
	 */
	public void removeprofile(ProfileEntity profileEntity) {
		EntityManager em = getEntityManager();
		em.remove(profileEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #install()
	 */
	public void install() {
		synchronized (this) {
			// generate profile entity & related classes
			new ConcreteProfileEntityGenerator(component, this)
					.generateClasses();

			// this one is just a runtime optimization for faster query building
			// now, later to use named queries
			profileEntityClassName = profileEntityClass.getName();

			profileEntityFactoryClass = new ConcreteProfileEntityFactoryGenerator(
					component, profileEntityClass,
					profileEntityArrayAttrValueClassMap).generateClass();
			try {
				profileEntityFactory = (ProfileEntityFactory) profileEntityFactoryClass
						.newInstance();
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(), e);
			}
			// 1. Generate CMP Interface Impl with JPA Annotations
			component.setProfileCmpConcreteClass(new ConcreteProfileGenerator(
					component).generateConcreteProfile());
			// 2. Create the corresponding JPA PU -- FIXME: Should be somewhere
			// else?
			createPersistenceUnit(component);

			new JPAQueryBuilder(component).parseStaticQueries();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.profile.ProfileEntityFramework
	 * #uninstall()
	 */
	public void uninstall() {
		synchronized (this) {
			SleeTransactionManager sleeTransactionManager = sleeContainer
					.getTransactionManager();
			Transaction tx = null;
			try {
				tx = sleeTransactionManager.suspend();
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(), e);
			}
			if (entityManagerFactory != null) {
				entityManagerFactory.close();
			}
			try {
				sleeTransactionManager.resume(tx);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(), e);
			}
			component.setProfileEntityFramework(null);
		}
	}

	// AUX METHODS

	/**
	 * 
	 * @param profileComponent
	 */
	@SuppressWarnings("unchecked")
	private void createPersistenceUnit(
			ProfileSpecificationComponent profileComponent) {
		try {
			HibernatePersistence hp = new HibernatePersistence();
			PersistenceUnitMetaData pumd = new PersistenceUnitMetaData();

			pumd.setProvider("org.hibernate.ejb.HibernatePersistence");
			pumd.setJtaDataSource("java:/DefaultDS");
			pumd.setExcludeUnlistedClasses(false);

			Map pumdProps = new HashMap();
			pumdProps.put(Environment.HBM2DDL_AUTO, "create-drop");
			pumdProps.put(Environment.DIALECT,
					"org.hibernate.dialect.HSQLDialect");

			pumd.setProperties(pumdProps);
			pumd.setName("JSLEEProfiles"
					+ profileComponent.getComponentID().hashCode());

			Set classes = new HashSet<String>();
			classes.add(profileEntityClass.getName());
			for (Class<?> clazz : profileEntityArrayAttrValueClassMap.values()) {
				classes.add(clazz.getName());
			}

			pumd.setClasses(classes);

			Properties properties = new Properties();

			properties.setProperty(Environment.DATASOURCE, "java:/DefaultDS");
			properties
					.setProperty(Environment.TRANSACTION_STRATEGY,
							"org.hibernate.ejb.transaction.JoinableCMTTransactionFactory");
			properties
					.setProperty(Environment.CONNECTION_PROVIDER,
							"org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider");
			properties.setProperty(
					"hibernate.jndi.java.naming.factory.url.pkgs",
					"org.jboss.naming:org.jnp.interfaces");
			properties.setProperty(Environment.CACHE_PROVIDER,
					"org.hibernate.cache.HashtableCacheProvider");
			properties.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY,
					"org.hibernate.transaction.JBossTransactionManagerLookup");
			properties.setProperty(
					"hibernate.jndi.java.naming.factory.initial",
					"org.jnp.interfaces.NamingContextFactory");
			properties.setProperty(Environment.DIALECT,
					"org.hibernate.dialect.HSQLDialect");
			// FIXME: Should be Environment.JACC_CONTEXTID but it's
			// hibernate.jacc_context_id vs hibernate.jacc.ctx.id. Bug?
			properties.setProperty("hibernate.jacc.ctx.id", "persistence.xml");
			properties.setProperty(Environment.CACHE_REGION_PREFIX,
					"persistence.unit:unitName=#" + pumd.getName());
			properties.setProperty(Environment.SESSION_FACTORY_NAME,
					"persistence.unit:unitName=#" + pumd.getName());
			properties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
			properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER,
					"false");
			properties.setProperty(Environment.BYTECODE_PROVIDER, "javassist");
			properties.setProperty(Environment.STATEMENT_BATCH_SIZE, "0");
			// properties.setProperty(Environment.SHOW_SQL, "true");
			properties.setProperty(Environment.FORMAT_SQL, "true");

			ClassLoader newClassLoader = profileComponent.getClassLoader();
			Thread.currentThread().setContextClassLoader(newClassLoader);

			for (String className : pumd.getClasses()) {
				Thread.currentThread().getContextClassLoader().loadClass(
						className);
			}

			PersistenceUnitInfoImpl pi = new PersistenceUnitInfoImpl(pumd,
					properties, Thread.currentThread().getContextClassLoader(),
					ClassLoader.getSystemResource("."), new ArrayList<URL>(),
					new InitialContext());

			Transaction tx = null;

			try {
				tx = sleeContainer.getTransactionManager().suspend();
				this.entityManagerFactory = hp
						.createContainerEntityManagerFactory(pi, null);
			} catch (Exception e) {
				logger.error("Failure creating Persistence Unit.", e);
			} catch (Throwable t) {
				logger.error("Failure creating Persistence Unit.", t);
			} finally {
				if (tx != null)
					sleeContainer.getTransactionManager().resume(tx);
			}

		} catch (Exception e) {
			logger.error("Failure creating Persistence Unit.", e);
		}

	}

	/**
	 * the string key used to store entity manager of this profile spec in tx
	 * data
	 */
	private String txDataKey = null;

	/**
	 * Retrieves the entity manager for the current tx and the framework profile
	 * spec
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private EntityManager getEntityManager() {

		if (txDataKey == null) {
			txDataKey = "jpapef.em."
					+ component.getProfileSpecificationID().toString();
		}

		// look in tx
		Map transactionContextData = null;
		try {
			transactionContextData = sleeContainer.getTransactionManager()
					.getTransactionContext().getData();
		} catch (Throwable e1) {
			throw new SLEEException(e1.getMessage(), e1);
		}

		EntityManager result = (EntityManager) transactionContextData
				.get(txDataKey);
		if (result == null) {
			// create using factory
			result = entityManagerFactory.createEntityManager();
			// store in tx context data
			transactionContextData.put(txDataKey, result);
			// add a tx action to close it before tx commits
			final EntityManager em = result;
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						em.close();
					} catch (Throwable e) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			try {
				sleeContainer.getTransactionManager().addBeforeCommitAction(
						action);
			} catch (Throwable e1) {
				throw new SLEEException(e1.getMessage(), e1);
			}
		}
		return result;
	}
}
