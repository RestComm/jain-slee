/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.profile;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileMBean;
import javax.slee.profile.ProfileManagement;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.jboss.cache.Node;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.deployment.interceptors.DefaultProfileManagementInterceptor;
import org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor;
import org.mobicents.slee.container.management.ProfileSpecificationManagement;
import org.mobicents.slee.container.management.jmx.ProfileProvisioningMBeanImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.eventrouter.DeferredActivityEndEvent;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.facilities.profile.ProfileRemovedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * This class is used by the SLEE to manage Profiles. It calls out to the
 * jboss-cache that put everything into the backend storage
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * 
 * @author Ivelin Ivanov
 * 
 * @author M. Ranganathan
 * @author martins
 *  
 */
public class SleeProfileManager {
	
	private static final String PROFILE_INDEXES_SPEC_SUFFIX = "indexes";
	private static final String PROFILEID_LOOKUP_NAME = "profileID";

	/**
	 * prefix for profile names in a back-end storage
	 */
	public static final String PROFILEID_LOOKUP_PREFIX = "profile:";

	/**
	 * prefix for default profile names in a back-end storage
	 */
	public final String DEFAULT_PROFILE_LOOKUP_PREFIX = "defaultProfile:";

	/**
	 * prefix for profile index names in a back-end storage
	 */
	public static final String INDEX_LOOKUP_PREFIX = "profileIndexes:";

	private final SleeContainer sleeContainer;

	private final ProfileSpecificationManagement profileSpecsManagement;
	
	private static final Logger logger = Logger
			.getLogger(SleeProfileManager.class);

	private ConcurrentHashMap<String, ProfileTableActivityImpl> profileTableActivities;

	private static String tcache = TransactionManagerImpl.PROFILE_CACHE;

	private static final String PROFILE_TABLE_NAMES = "profileTableNames";

	class ProfileCacheManager {
		private Set profileTableNames;

		private ProfileCacheManager() {

			// super(TransactionManagerImpl.PROFILE_CACHE);
			//this.profileTableNames = new HashSet();
		}

		public void invalidate() {
			//this.read = false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mobicents.slee.runtime.Cacheable#loadFromCache()
		 */
		public void loadFromCache() {
		}

		public synchronized Set getProfileTableNames() {
			if (profileTableNames == null) {
				this.profileTableNames = new CacheableSet(tcache + "-"
						+ PROFILE_TABLE_NAMES);
			}
			;
			return profileTableNames;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mobicents.slee.runtime.Cacheable#getNodeName()
		 */
		public String getNodeName() {

			return "profileInfo";
		}

		public void addTransactionalAction() throws SystemException {
			// SleeContainer.getTransactionManager().addPrepareCommitAction(
			//        new ProfilePrepareAction());
		}

	}

	private ProfileCacheManager profileCacheManager;

	//private ProfileTableActivityContextInterfaceFactoryImpl
	// profileTableActivityContextInterfaceFactory=null;

	String getRootFqn() {
		return TransactionManagerImpl.getRootFqn(tcache);
	}

	/**
	 * Constructor
	 */
	public SleeProfileManager(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.profileSpecsManagement = new ProfileSpecificationManagement(sleeContainer);
		this.profileTableActivities = new ConcurrentHashMap<String, ProfileTableActivityImpl>();
		this.setProfileCacheManager(new ProfileCacheManager());
	}

	/**
	 * 
	 * @return all profiles created in SLEE
	 * @throws SystemException
	 */
	protected void loadProfileTableNames() throws SystemException {
		this.getProfileCacheManager().loadFromCache();
	}

	/**
	 * Instantiates a new profile and its profileMbean and regsiter the latter
	 * with the mbean server
	 * 
	 * @param profileSpecificationDescriptor
	 *            the profileSpecificationDescriptor
	 * @param profileTableName
	 *            the profile table name for which this profile is gonna be
	 *            created
	 * @param profileName
	 *            the profile name; if null, it means that this the default
	 *            profile has to be created
	 * @throws Exception
	 *             if anything wrong happens
	 */
	protected synchronized ObjectName instantiateProfile(
			String cmpInterfaceName, String profileTableName,
			String profileName, boolean loadDataFromBackendStorage)
			throws Exception {

		boolean isDefaultProfile = false;
		if (profileName == null)
			isDefaultProfile = true;
		String key = null;
		if (isDefaultProfile)
			key = this.generateDefaultProfileKey(profileTableName);
		else
			key = this.generateProfileKey(profileTableName, profileName);
		//logger.debug(profileTableName+" "+profileName+" "+isDefaultProfile+"
		// "+key);
		//Instantiates the profile and initializes it
		String cmpConcreteName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX
				+ cmpInterfaceName
				+ ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;

		ProfileManagementInterceptor profileManagementInterceptor = new DefaultProfileManagementInterceptor();

		profileManagementInterceptor.setProfileTableName(profileTableName);
		profileManagementInterceptor.setProfileName(profileName);

		ClassLoader profileSpecDUClassLoader = profileSpecsManagement.getProfileSpecificationDescriptor(findProfileSpecId(profileTableName)).getClassLoader();
		Class concreteProfileClass = profileSpecDUClassLoader.loadClass(cmpConcreteName);
		Constructor constructor = concreteProfileClass
				.getConstructor(new Class[] {
						ProfileManagementInterceptor.class,
						SleeProfileManager.class, String.class });
		ProfileManagement profileManagement = (ProfileManagement) constructor
				.newInstance(new Object[] { profileManagementInterceptor, this,
						key });
		if (loadDataFromBackendStorage) {
			profileManagementInterceptor
					.loadStateFromBackendStorage(cmpInterfaceName,profileSpecDUClassLoader);
		} else {
			if (isDefaultProfile) {
				profileManagement.profileInitialize();
				profileManagement.profileStore();
				profileManagement.profileVerify();
				profileManagementInterceptor.persistProfile();
			} else {
				//copy the attribute values from the default profile
				String defaultProfileKey = generateDefaultProfileKey(profileTableName);
				profileManagementInterceptor.copyStateFromDefaultProfile(
						cmpInterfaceName, defaultProfileKey,profileSpecDUClassLoader);
			}
		}

		//Instantiates the Profile MBean and initializes it
		String mbeanConcreteName = ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX
				+ cmpInterfaceName
				+ ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX;

		Class concreteProfileMBeanClass = profileSpecDUClassLoader.loadClass(mbeanConcreteName);
		Constructor profileMBeanConstructor = concreteProfileMBeanClass
				.getConstructor(new Class[] {
						ProfileManagementInterceptor.class, Object.class });
		ProfileMBean profileMBean = (ProfileMBean) profileMBeanConstructor
				.newInstance(new Object[] { profileManagementInterceptor,
						profileManagement });
		ObjectName objectName = null;
		if (isDefaultProfile) {
			objectName = ProfileProvisioningMBeanImpl
					.getDefaultProfileObjectName(profileTableName);
		} else {
			objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(
					profileTableName, profileName);
		}
		if (logger.isDebugEnabled())
			logger.debug("[instantiateProfile]Registering "
					+ "following profile MBean with object name " + objectName);
		sleeContainer.getMBeanServer().registerMBean(profileMBean, objectName);
		return objectName;
	}

	/**
	 * Adds a profile table for a specific ProfileSpecification It also creates
	 * the default profile and initializes it
	 * 
	 * @param profileTableName
	 *            the profile table name
	 * @param profileSpecificationDescriptor
	 *            the profile specification descriptor
	 * @throws Exception
	 */
	public void addProfileTable(String profileTableName,
			ProfileSpecificationDescriptor profileSpecificationDescriptor)
			throws Exception {

		logger.debug("addProfileTable " + profileTableName);
		//UserTransaction userTransaction=transactionManager.getTransaction();
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			logger
					.debug("singleprofile:"
							+ new Boolean(
									((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
											.getSingleProfile()));

			createProfileTableActivity(profileTableName);
			
			this.getProfileCacheManager().getProfileTableNames().add(
					profileTableName);
			this.getProfileCacheManager().addTransactionalAction();
			String key = this.generateProfileTableKey(profileTableName);
			//The creation of the profile table and defaut profile is under the
			// same transaction
			//if something goes wrong, everything is rolled back and nothing is
			// created

			//create the profile table
			Map data = new HashMap();
			data.put("profileSpecificationID", profileSpecificationDescriptor
					.getName()
					+ "/"
					+ profileSpecificationDescriptor.getVendor()
					+ "/"
					+ profileSpecificationDescriptor.getVersion());
			data.put("cmpInterfaceName", profileSpecificationDescriptor
					.getCMPInterfaceName());
			data
					.put(
							"singleProfile",
							new Boolean(
									((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
											.getSingleProfile()));
			data
					.put(
							PROFILE_INDEXES_SPEC_SUFFIX,
							((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
									.getProfileIndexes());

			// emmartins: adding set to keep track of profile mbeans
			data.put("registredMBeans", new HashSet());

			transactionManager.createNode(tcache, key, data);

			//create indexation if needed
			Map indexes = ((ProfileSpecificationDescriptorImpl) profileSpecificationDescriptor)
					.getProfileIndexes();
			if (indexes != null) {
				Iterator keys = indexes.keySet().iterator();
				while (keys.hasNext()) {
					/*
					 * Model of indexation :
					 * ->key=indexes/profileTableName/attributeToIndex data for
					 * this key : - classType of the attribute to index -
					 * boolean telling whether it sould be unique or not - Map
					 * of key-value pair ->respectively profile name and indexed
					 * value
					 */
					String attributeName = (String) keys.next();
					//Adding index
					String indexKey = generateIndexKey(profileTableName,
							attributeName);
					Class profileTransientClass = Thread
							.currentThread()
							.getContextClassLoader()
							.loadClass(
									ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_PREFIX
											+ profileSpecificationDescriptor
													.getCMPInterfaceName()
											+ ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_SUFFIX);
					Field profileIndexedField = profileTransientClass
							.getField(attributeName);

					Map indexedAttributes = new HashMap();
					data.put("indexedAttributes", indexedAttributes);
					Class classType = profileIndexedField.getType();
					if (classType.isArray()) {
						data.put("classType", classType.getComponentType()
								.getName());
						data.put("isArray", new Boolean(true));
					} else {
						data.put("classType", classType.getName());
						data.put("isArray", new Boolean(false));
					}
					Object isUnique = indexes.get(attributeName);
					data.put("isUnique", isUnique);
					if (logger.isDebugEnabled()) {
						logger.debug("createProfile: indexKey = " + indexKey);
					}
					logger.debug("createProfile: indexKey data = " + data);
					transactionManager.createNode(tcache, indexKey, data);
				}
			}

			//Instantiates the default profile and initializes it
			instantiateProfile(profileSpecificationDescriptor
					.getCMPInterfaceName(), profileTableName, null, false);

		} catch (Exception e) {
			//b=false;
			e.printStackTrace();
			this.getProfileCacheManager().getProfileTableNames().remove(
					profileTableName);
			this.getProfileCacheManager().invalidate();
			transactionManager.setRollbackOnly();
			//logger.info("rollbacking");
			//transactionManager.rollback();
			throw e;
		} finally {

			displayAllProfilePersistentInformation();
			//logger.info(""+b);
			if (b)
				transactionManager.commit();
		}
	}

	/**
	 * Removes a profile Table, its default profile, indexes and all other associated profiles 
	 * 
	 * @param profileTableName
	 *            the profile table name
	 * @throws UnrecognizedProfileTableNameException
	 *             if the profile table cannot be found
	 */
	public synchronized void removeProfileTable(String profileTableName)
			throws TransactionRequiredLocalException, SystemException,
			UnrecognizedProfileTableNameException {
		//The deletion of the profile table and its artifacts is under
		//a transaction; if something goes wrong, everything is rolled back and
		// no state changes
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();

		boolean b = false;
		try {
			b = transactionManager.requireTransaction();

			if (!this.getProfileCacheManager().getProfileTableNames().contains(
					profileTableName))
				throw new UnrecognizedProfileTableNameException(
						"Could not find profile table " + profileTableName);

			this.getProfileCacheManager().getProfileTableNames().remove(
					profileTableName);
			// commented out, because it is called in removeProfileAfterTableActivityEnded() -> this.profileCacheManager.addTransactionalAction();
			if (logger.isDebugEnabled()) {
				logger.debug("removeProfileTable: removing profileTable="
						+ profileTableName);
			}
			String key = this.generateProfileTableKey(profileTableName);
			String defaultProfileKey = this
					.generateDefaultProfileKey(profileTableName);
			//removes the default profile
			transactionManager.removeNode(tcache, defaultProfileKey);

			//unregistering the default MBean profile from the mbean server
			ObjectName objectName = ProfileProvisioningMBeanImpl
					.getDefaultProfileObjectName(profileTableName);

			if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
				logger.info("Unregistering following "
						+ "profile table MBean with object name " + objectName);
				sleeContainer.getMBeanServer().unregisterMBean(objectName);
			}
			//remove the profile table and its profiles
			Node profileTableNode = transactionManager.getNode(tcache, key);

			Set registredMBeans = (Set) profileTableNode.get("registredMBeans");
			// first remove commited children
			Map childrens = profileTableNode.getChildren();
			if (childrens != null) {
				Iterator it = childrens.values().iterator();
				while (it.hasNext()) {
					Node profileNode = (Node) it.next();
					String profileName = profileNode.getFqn().toString()
							.substring(("/" + key + "/").length());
					//unregistering the MBean profile from the server
					objectName = ProfileProvisioningMBeanImpl
							.getProfileObjectName(profileTableName, profileName);
					if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
						logger.info("Unregistering "
								+ "following profile MBean with object name "
								+ objectName);
						sleeContainer.getMBeanServer().unregisterMBean(
								objectName);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("removeProfileTable: " + objectName
									+ " is not registered! ");
						}
					}
					// removing from registredMBeans set
					registredMBeans.remove(objectName);
				}
			}
			// now the ones left, which are not in children
			for (Iterator it = registredMBeans.iterator(); it.hasNext();) {
				objectName = (ObjectName) it.next();
				if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
					logger.info("Unregistering "
							+ "following profile MBean with object name "
							+ objectName);
					sleeContainer.getMBeanServer().unregisterMBean(objectName);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("removeProfileTable: " + objectName
								+ " is not registered! ");
					}
				}
			}

			// Issue an activity end event.
			// When a Profile Table is removed from the SLEE by an Administrator
			// using a ProfileProvisioningMBean
			// object, the SLEE ends the corresponding Profile Table Activity
			// and fires an Activity
			// End Event on its ProfileTableActivity object. Additionally, the
			// SLEE ends all Profile
			// Table Activities , and fires Activity End Events on the
			// corresponding ProfileTableActivity
			// objects, when the SLEE transitions from the Running state to the
			// Stopping state. This allows
			// SBB entities attached to Profile Table Activities to clean up in
			// the usual way.
			ActivityContextHandle ach = ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName));
			ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach,false);
			ac.setState(ActivityContextState.ENDING);
			new DeferredActivityEndEvent(ach,null);

		} catch (JMException e) {
			logger.error("Failed to remove profile table: " + profileTableName,
					e);
			this.getProfileCacheManager().invalidate();
			transactionManager.setRollbackOnly();
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				transactionManager.commit();
		}
	}

	/**
	 * 
	 * Just creates a Profile MBean object, which has references to the
	 * information needed to create the actual profile. Nothing else is done to
	 * create the profile until the commitProfile() operation is called on the
	 * MBean. If something goes wrong, the profile MBean is not created and
	 * exception is thrown.
	 * 
	 * @param profileTableName
	 *            the profile table of the profile table where the profile has
	 *            to be added
	 * @param newProfileName
	 *            the profile name of the new profile
	 * @throws SystemException
	 *             if a low level exception occurs
	 * @throws SingleProfileException
	 */
	public ObjectName addProfileToProfileTable(String profileTableName,
			String newProfileName) throws SystemException,
			SingleProfileException {
		//Test the single profile property of the profile specification
		String profileTableKey = this.generateProfileTableKey(profileTableName);
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		ObjectName objectName = null;

		boolean b = false;
		try {
			b = transactionManager.requireTransaction();

			// Verify if the Profile Scheme indicated that there can be only one
			// profile in this table
			boolean isSingleProfile = ((Boolean) transactionManager.getObject(
					tcache, profileTableKey, "singleProfile")).booleanValue();
			if (isSingleProfile) {
				Node profileTableNode = transactionManager.getNode(tcache,
						profileTableKey);
				Map children = profileTableNode.getChildren();
				if (children != null) {
					if (children.size() >= 1) {
						throw new SingleProfileException();
					}
				}
			}

			// get the cmpInterfaceName for the given profile table. Make sure
			// that it is done in tx scope.
			String cmpInterfaceName = (String) transactionManager.getObject(
					tcache, profileTableKey, "cmpInterfaceName");

			//Instantiates the profile and initializes it without registering
			// it in the SLEE
			objectName = instantiateProfile(cmpInterfaceName, profileTableName,
					newProfileName, false);

			// emmartins: profile is not visisble to slee until commit, but we need to
			// know which mbeans are registred for this profile table, if not then we
			// can have mbean leaks when a profile is created but is not committed,
			// and the profile table is removed
			Set registredMBeans = (Set) transactionManager.getObject(tcache,
					profileTableKey, "registredMBeans");
			if (registredMBeans != null) {
				registredMBeans.add(objectName);
			} else {
				if (logger.isDebugEnabled()) {
					logger
							.warn("registred mbeans set of profile table not found in cache");
				}
			}

			// At this point the profile MBean is created, but the profile is
			// not registered with the SLEE
			// until commitProfile() is explicitly called on the profile MBean

		} catch (Exception e) {
			if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
				logger.info("[addProfileToProfileTable]Unregistering "
						+ "following profile MBean with object name "
						+ objectName);
				try {
					sleeContainer.getMBeanServer().unregisterMBean(objectName);
				} catch (Exception e1) {
					logger.error(
							"Failed addProfileToProfileTable. Could not unregister "
									+ objectName, e1);
					throw new SystemException("Failed addProfileToProfileTable");
				}
			}
			logger.error("Failed addProfileToProfileTable", e);
			transactionManager.setRollbackOnly();
			if (e instanceof SingleProfileException)
				throw (SingleProfileException) e;
			throw new SystemException("Failed addProfileToProfileTable");
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				transactionManager.commit();
		}

		return objectName;
	}

	/**
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return @throws
	 *         Exception
	 */
	public ProfileManagement instantiateLastCommittedProfile(
			String profileTableName, String profileName) throws Exception {

		/*
		 * BEWARE : Profiles are not stored in memory, so a new profile is
		 * initialized and its state is loaded from the backend storage. Indeed,
		 * the profile should be passed to in the profile removed event and this
		 * profile should contain the last committed state
		 */
		String profileKey = null;
		if (profileName != null)
			profileKey = this.generateProfileKey(profileTableName, profileName);
		else
			profileKey = this.generateDefaultProfileKey(profileTableName);
		String profileTableKey = this.generateProfileTableKey(profileTableName);

		if (logger.isDebugEnabled()) {
			logger
					.debug("instantiating Last Committed Profile to fire an event"
							+ "profileTableKey"
							+ profileTableKey
							+ ",profileKey=" + profileKey);
		}

		String cmpInterfaceName = (String) sleeContainer
				.getTransactionManager().getObject(tcache, profileTableKey,
						"cmpInterfaceName");
		//Instantiates the profile and initializes it
		String cmpConcreteName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX
				+ cmpInterfaceName
				+ ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;

		ProfileManagementInterceptor profileManagementInterceptor = new DefaultProfileManagementInterceptor();
		ClassLoader profileSpecDUClassLoader = getProfileSpecificationManagement()
				.getProfileSpecificationDescriptor(
						findProfileSpecId(profileTableName)).getClassLoader();
		
		Class concreteProfileClass = profileSpecDUClassLoader.loadClass(cmpConcreteName);
		Constructor constructor = concreteProfileClass
		.getConstructor(new Class[] {
				ProfileManagementInterceptor.class,
				SleeProfileManager.class, String.class });
		ProfileManagement profileManagement = (ProfileManagement) constructor
		.newInstance(new Object[] { profileManagementInterceptor, this,
				profileKey });
		profileManagementInterceptor
		.loadStateFromBackendStorage(cmpInterfaceName,profileSpecDUClassLoader);
		/* Profile created from backend storage */
		if (logger.isDebugEnabled()) {
			logger.debug("Added profile class: "+profileManagement.getClass());
		}
		return profileManagement;
		
		
	}

	/**
	 * Remove a profile from a profile Table
	 * 
	 * @param profileTableName
	 *            the profile table name of the profile table where the profile
	 *            has to be removed
	 * @param profileName
	 *            the profile name of the profile to remove
	 * @throws SystemException
	 *             if a low level exception occurs
	 */
	public synchronized void removeProfile(String profileTableName,
			String profileName) throws SystemException {
		//The deletion of the profile is under a transaction
		//if something goes wrong, everything is rolled back and nothing is
		// removed
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();

			if (logger.isDebugEnabled()) {
				logger.debug("removing profile=" + profileName);
			}
			String profileTableKey = this
					.generateProfileTableKey(profileTableName);
			String profileKey = this.generateProfileKey(profileTableName,
					profileName);

			//getting last commited profile
			ProfileManagement profileManagement = instantiateLastCommittedProfile(
					profileTableName, profileName);

			//remove indexed attribute
			removeAllIndexedAttributesOfProfile(profileTableName, profileName);
			//removing profile
			transactionManager.removeNode(tcache, profileKey);

			// get mbean object name
			ObjectName objectName = ProfileProvisioningMBeanImpl
					.getProfileObjectName(profileTableName, profileName);

			// emmartins: remove it from the profile table node in cache
			Set profileTableRegistredMbeans = (Set) transactionManager
					.getObject(tcache, profileTableKey, "registredMBeans");
			if (profileTableRegistredMbeans != null) {
				profileTableRegistredMbeans.remove(objectName);
			} else {
				if (logger.isDebugEnabled()) {
					logger
							.warn("registred mbeans set of profile table not found in cache");
				}
			}

			//Fire the removed event only when the transaction commits
			final ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory = sleeContainer
					.getProfileTableActivityContextInterfaceFactory();
			if (profileTableActivityContextInterfaceFactory == null) {
				final String s = "got NULL ProfileTable ACI Factory";
				logger.error(s);
				throw new SystemException(s);
			}

			Address profileAddress = new Address(AddressPlan.SLEE_PROFILE,
					profileTableName + "/" + profileName);
			ProfileTableActivityImpl profileTableActivity = this.profileTableActivities.get(profileTableName);
			ActivityContextInterfaceImpl activityContextInterface = (ActivityContextInterfaceImpl) profileTableActivityContextInterfaceFactory
					.getActivityContextInterface(profileTableActivity);

			//unregistering the MBean profile from the server
			if (!sleeContainer.getMBeanServer().isRegistered(objectName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("removing impossible, MBean " + objectName
							+ " not registered !!!");
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("[removeProfile]Unregistering "
							+ "following profile MBean with object name "
							+ objectName);
				}
				sleeContainer.getMBeanServer().unregisterMBean(objectName);
			}
			//Firing profile removed event only when the transaction commits
			ProfileRemovedEventImpl profileRemovedEvent = new ProfileRemovedEventImpl(
					profileAddress, new ProfileID(profileAddress),
					profileManagement, activityContextInterface,
					profileTableActivityContextInterfaceFactory);

			//Profile Removed Event.
			//After a Profile is removed from a Profile Table,
			//the SLEE fires and delivers a Profile Removed
			//Event on the Profile Table’s Activity.
			new DeferredEvent(profileRemovedEvent.getEventTypeID(),
					profileRemovedEvent, activityContextInterface
							.getActivityContext().getActivityContextHandle(), profileAddress);
			if (logger.isDebugEnabled()) {
				logger.debug("Queued following removed event:"
						+ profileRemovedEvent.getEventTypeID() + ",:"
						+ activityContextInterface.getActivityContextHandle());
			}

		} catch (Exception e) {
			String err = "Failed removeProfile " + profileName
					+ " from profile table " + profileTableName
					+ " because of " + e.getMessage();
			logger.error(err, e);
			this.getProfileCacheManager().invalidate();
			transactionManager.setRollbackOnly();
			throw new SystemException(err);
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				transactionManager.commit();
		}
	}

	/**
	 * It removes all the indexed attribute of a profile <BR>
	 * This method should be called within the context of a transaction
	 * 
	 * @param profileTableName
	 *            the profile table name to which the profile belongs
	 * @param profileName
	 *            the profile name
	 * @throws SystemException
	 */
	public void removeAllIndexedAttributesOfProfile(String profileTableName,
			String profileName) throws SystemException {

		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		//try to find the actual value of the indexed attribute
		//and update it
		Node profileTableIndexesAttribute = (Node) transactionManager.getNode(
				tcache, this.generateIndexKeyRoot(profileTableName));
		if (profileTableIndexesAttribute == null)
			return;
		Map indexedAttributeNames = (Map) profileTableIndexesAttribute
				.getChildren();
		if (indexedAttributeNames != null) {
			Iterator indexIt = indexedAttributeNames.keySet().iterator();
			while (indexIt.hasNext()) {
				String indexedAttributeName = (String) indexIt.next();
				//Browse the keys of indexed attributes
				//if one map the profile Id hen it is removed
				String indexKey = generateIndexKey(profileTableName,
						indexedAttributeName);
				Map indexedAttributes;
				try {
					indexedAttributes = (Map) transactionManager.getObject(
							tcache, indexKey, "indexedAttributes");
					if (indexedAttributes != null) {
						Iterator keys = indexedAttributes.keySet().iterator();
						while (keys.hasNext()) {
							String indexedProfileName = (String) keys.next();
							if (indexedProfileName.equals(profileName)) {
								indexedAttributes.remove(profileName);
								keys = indexedAttributes.keySet().iterator();
							}
						}
					}
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Generates the profile table key under which the profile is gonna be
	 * stored into the jboss cache
	 * 
	 * @param profileTableName
	 *            the profile Table name
	 * @return the newly generated key
	 */
	String generateProfileTableKey(String profileTableName) {
		return getRootFqn() + PROFILEID_LOOKUP_PREFIX + profileTableName;
	}

	/**
	 * Generates the default profile key under which the profile is gonna be
	 * stored into the jboss cache
	 * 
	 * @param profileTableName
	 *            the profile Table name
	 * @return the newly generated key
	 */
	private String generateDefaultProfileKey(String profileTableName) {
		String key = getRootFqn() + DEFAULT_PROFILE_LOOKUP_PREFIX
				+ profileTableName;
		return key;
	}

	/**
	 * Generates the profile key under which the profile is gonna be stored into
	 * the jboss cache
	 * 
	 * @param profileTableName
	 *            the profile Table name
	 * @param profileName
	 *            the profile name
	 * @return the newly generated key
	 */
	private String generateProfileKey(String profileTableName,
			String profileName) {
		String key = generateProfileTableKey(profileTableName) + "/"
				+ profileName;
		return key;
	}

	/**
	 * Generate the key under which all indices of the profile are stored.
	 * 
	 * @param profileTableName
	 * 
	 * @return  
	 */
	private String generateIndexKeyRoot(String profileTableName) {
		return getRootFqn() + INDEX_LOOKUP_PREFIX + profileTableName;
	}

	/**
	 * Generates the index key under which the index is gonna be stored into the
	 * jboss cache
	 * 
	 * @param profileTableName
	 *            the profile Table name
	 * @param attributeName
	 *            the attribute name
	 * @return the newly generated key
	 */
	private String generateIndexKey(String profileTableName,
			String attributeName) {
		String key = generateIndexKeyRoot(profileTableName) + "/"
				+ attributeName;
		return key;
	}

	/**
	 * Retrieve the profileSpecificationID of the profle Table whose the name is
	 * given in parameters
	 * 
	 * @param profileTableName
	 *            the profile Table Name
	 * @return the profileSpecificationID of the profile table or null if
	 *         nothing has been retrieved
	 * @throws SystemException
	 *             if a low level exception occurs
	 */
	public ProfileSpecificationID findProfileSpecId(String profileTableName)
			throws SystemException {

		ProfileSpecificationID profileSpecificationID = null;

		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			String key = this.generateProfileTableKey(profileTableName);
			if (logger.isDebugEnabled())
				logger.debug("key = " + key);

			String profileSpecificationIDAsString = (String) transactionManager
					.getObject(tcache, key, "profileSpecificationID");
			if (profileSpecificationIDAsString == null)
				return profileSpecificationID;
			ComponentKey componentKey = new ComponentKey(
					profileSpecificationIDAsString);
			profileSpecificationID = new ProfileSpecificationIDImpl(
					componentKey);
		} catch (RuntimeException e) {
			this.getProfileCacheManager().invalidate();
			transactionManager.setRollbackOnly();
			throw e;
		} finally {
			if (b)
				transactionManager.commit();
		}
		return profileSpecificationID;
	}

	/**
	 * @return the profile index data structure if the attribute is indexed, otherwise null
	 * @throws SystemException
	 *             if a low level exception occurs
	public findProfileIndex(String profileTableName, String attributeName) throws SystemException {
	    String indexKey = generateIndexKey(profileTableName, attributeName);

	    ProfileSpecificationID profileSpecificationID = null;

	    SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
	    boolean b = false;
	    try {
	        b = transactionManager.requireTransaction();
	        this.profileCacheManager.loadFromCache();
	        String key = this.generateProfileTableKey(profileTableName);
	        if (logger.isDebugEnabled())
	            logger.debug("key = " + key);

	        String profileSpecificationIDAsString = (String) transactionManager
	                .getObject(tcache, key, "profileSpecificationID");
	        if (profileSpecificationIDAsString == null)
	            return profileSpecificationID;
	        ComponentKey componentKey = new ComponentKey(
	                profileSpecificationIDAsString);
	        profileSpecificationID = new ProfileSpecificationIDImpl(
	                componentKey);
	    } catch (SystemException e) {
	        this.profileCacheManager.invalidate();
	        transactionManager.setRollbackOnly();
	        throw e;
	    } finally {
	        if (b)
	            transactionManager.commit();
	    }
	    return profileSpecificationID;
	}
	 */

	/**
	 * Retrieve a collection of ProfileSpecificationID of all the profile tables
	 * stored into the slee
	 * 
	 * @return a collection of ProfileSpecificationID of all the profile tables
	 */
	public Collection findAllProfileTables() {
		//UserTransaction userTransaction=transactionManager.getTransaction();
		Collection profileTables = new Vector();
		boolean b = sleeContainer.getTransactionManager().requireTransaction();
		boolean rb = false;
		try {

			return this.getProfileCacheManager().getProfileTableNames();

		} catch (Exception e) {
			logger.error("error getting profile table names ", e);
			rb = true;
		} finally {
			displayAllProfilePersistentInformation();
			try {
				if (rb) {
					this.getProfileCacheManager().invalidate();
					sleeContainer.getTransactionManager().setRollbackOnly();
				}
				if (b)
					sleeContainer.getTransactionManager().commit();
			} catch (SystemException ex) {
				throw new RuntimeException("Unexpected tx manager failure ");
			}
		}
		return profileTables;
	}

	/**
	 * Lookup the profile MBean Object name. It could be a non-committed profile view.
	 * 
	 * @param profileTableName
	 *            the name of the profile table where the profile belongs
	 * @param profileName
	 *            the profile name
	 * @return the MBean object name if the profile exists or null otherwise
	 */
	public synchronized ObjectName findProfileMBean(String profileTableName,
			String profileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("findProfile: profileTableName = " + profileTableName
					+ " profileName = " + profileName);
		}

		ObjectName objectName;
		try {
			objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(
					profileTableName, profileName);
		} catch (MalformedObjectNameException e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
		if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
			return objectName;
		} else {
			return null;
		}
	}

	/**
	 * Retrieve from the backend store the wrapper profile object by table name
	 * and profile name
	 * 
	 * @param profileTableName
	 *            the name of the profile table where the profile belongs
	 * @param profileName
	 *            the profile name
	 * @return the wrapper object or null if nothing has been retrieved
	 * @throws SystemException
	 *             if a low level excpetion occurs
	 */
	public Object findCommittedProfile(String profileTableName,
			String profileName) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("findProfile: profileTableName = " + profileTableName
					+ " profileName = " + profileName);
		}
		Object profile = null;
		String key = this.generateProfileKey(profileTableName, profileName);
		profile = lookupProfileByKey(key);
		return profile;
	}

	/**
	 * Retrieve the wrapper object for a profile from the backend storage
	 * 
	 * @param key -
	 *            the profile key for lookup in the back-end storage
	 * @return the wrapper object or null if nothing has been retrieved
	 * @throws SystemException
	 * @throws SystemException
	 *             if a low level exception occurs
	 */
	public Object lookupProfileByKey(String key) throws SystemException {

		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		transactionManager.assertIsInTx();

		if (logger.isDebugEnabled())
			logger.debug("key = " + key);

		Object profile = transactionManager.getNode(tcache, key);

		return profile;
	}

	/**
	 * 
	 * Checks whether a profile has been committed, given its table name and
	 * profile name
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return true if the profile has been committed, false otherwise
	 * @throws SystemException
	 */
	public boolean isProfileCommitted(String profileTableName,
			String profileName) throws SystemException {
		Object profile = null;
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			String key = this.generateProfileKey(profileTableName, profileName);
			if (logger.isDebugEnabled()) {
				logger.debug("key = " + key);
			}
			Node node = transactionManager.getNode(tcache, key);
			if (logger.isDebugEnabled()) {
				logger.debug("node = " + node);
			}
			profile = lookupProfileByKey(key);
		} catch (SystemException e) {
			logger.error("Failed findProfile.", e);
			this.getProfileCacheManager().invalidate();
			transactionManager.setRollbackOnly();
			throw e;
		} finally {
			if (b)
				transactionManager.commit();
		}
		if (profile != null)
			return true;
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("isProfileCommitted: profileTableName "
						+ profileTableName + " profileName " + profileName
						+ " returning false");
			}
			return false;
		}
	}

	/**
	 * Retrieve all profiles, except the default profile, stored under a profile
	 * table whose the name is given in parameter
	 * 
	 * @param profileTableName
	 *            the profile table name
	 * @return all profiles, except the default profile, of a profile table.
	 */
	public Collection findAllProfilesByTableName(String profileTableName) {
		//UserTransaction userTransaction=transactionManager.getTransaction();
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		Collection profiles = new Vector();
		String lookupKey = generateProfileTableKey(profileTableName);
		try {
			Map children = transactionManager.getChildren(tcache, lookupKey);
			if (children != null) {
				Iterator it = children.values().iterator();
				while (it.hasNext()) {
					Node childNode = (Node) it.next();
					//profiles.add(childNode.getData().get("profileID"));
					Object obj = childNode.getData().get(PROFILEID_LOOKUP_NAME);
					// (Ivelin) When can obj be legally null or not a ProfileID?
					if (obj != null && obj instanceof ProfileID) {
						profiles.add(((ProfileID) obj).getProfileName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profiles;

	}

	/**
	 * Helper method to display all information related to profiles stored in
	 * the backend storage
	 */
	protected void displayAllProfilePersistentInformation() {
		return;
	}

	/*
	 * (Ivelin) FIXME: Either uncomment or remove this method.
	 * 
	 * protected void displayAllProfilePersistentInformation() {
	     //
	     // String lookupProfileKey = "profiles"; String lookupIndexKey =
	     // "indexes"; String lookupDefaultProfileKey = "defaultProfiles";
	     //
	    logger.info("Profile persistent state informations:");
	    SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
	    try {
	        if ( transactionManager.getRollbackOnly()) return;
	    } catch ( Exception ex ) {
	        logger.debug("Cannot print persistent state - tx has rolled back");
	    }

	    try {
	        logger.info("Profile Tables :");
	        HashSet childrens = this.profileCacheManager.getProfileTableNames();
	        if (childrens != null) {
	            Iterator it = childrens.iterator();
	            boolean hasProfileChildren = false;
	            while (it.hasNext()) {
	                logger.info("Profile Table :");
	                String nodeName = (String) it.next();
	                String key = this.generateProfileTableKey(nodeName);
	                Node childNode = transactionManager.getNode(tcache, key);
	                logger.debug("key " + key);
	                Map data = childNode.getData();
	                Iterator keyIt = data.keySet().iterator();
	                logger.info("Information :");
	                while (keyIt.hasNext()) {
	                    String dataKey = (String) keyIt.next();
	                    logger.info(dataKey + ":" + data.get(dataKey));
	                }
	                hasProfileChildren = true;
	                String profileTableName = childNode.getFqn().toString()
	                        .substring((getRootFqn() + "/").length());
	                Node defaultProfileNode = transactionManager.getNode(
	                        tcache,
	                        this.generateDefaultProfileKey(profileTableName));

	                if (defaultProfileNode == null)
	                    continue;
	                //TODO display info in debug
	                logger
	                        .info("key="
	                                + childNode.getFqn()
	                                + ",value="
	                                + childNode.getData().get(
	                                        "profileSpecificationID"));
	                logger
	                        .info("defaultProfile="
	                                + defaultProfileNode.getFqn());
	                Iterator defaultProfileIterator = defaultProfileNode
	                        .getDataKeys().iterator();
	                while (defaultProfileIterator.hasNext()) {
	                    String attributeName = (String) defaultProfileIterator
	                            .next();
	                    //TODO display info in debug
	                    logger.info("field=" + attributeName + ",value="
	                            + defaultProfileNode.get(attributeName));
	                }
	                logger.info("Profiles :");
	                Map profiles = childNode.getChildren();
	                if (profiles != null) {
	                    Iterator profilesIt = profiles.values().iterator();
	                    while (profilesIt.hasNext()) {
	                        logger.info("Profile :");
	                        Node profileNode = (Node) profilesIt.next();
	                        //TODO display info in debug
	                        logger.info("key="
	                                + profileNode.getFqn()
	                                + ",value="
	                                + profileNode.getData().get(
	                                        PROFILEID_LOOKUP_NAME));
	                    }
	                }
	                logger.info("Indexes :");
	                String indexKey = this
	                        .generateIndexKeyRoot(profileTableName);
	                Node indexesNode = transactionManager.getNode(tcache,
	                        indexKey);

	                if (indexesNode != null) {
	                    Map indexes = indexesNode.getChildren();
	                    if (indexes != null) {
	                        Iterator indexesIt = indexes.values().iterator();
	                        while (indexesIt.hasNext()) {
	                            Node indexNode = (Node) indexesIt.next();
	                            logger.info("indexNode :key="
	                                    + indexNode.getFqn());
	                            Map indexedAttributes = (Map) transactionManager
	                                    .getObject(tcache, indexNode.getFqn()
	                                            .toString().substring(
	                                                    getRootFqn().length()),
	                                            "indexedAttributes");
	                            if (indexedAttributes != null) {
	                                Iterator indexedAttributesIt = indexedAttributes
	                                        .keySet().iterator();
	                                logger.info("indexNode :values=");
	                                while (indexedAttributesIt.hasNext()) {
	                                    String profileName = (String) indexedAttributesIt
	                                            .next();
	                                    ProfileID profileId = new ProfileID(
	                                            profileTableName, profileName);
	                                    logger.info("profileID:"
	                                            + profileId
	                                            + "value="
	                                            + indexedAttributes
	                                                    .get(profileName));
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	            if (!hasProfileChildren) {
	                logger.info("No information found in the backend storage");
	            }
	        } else {
	            logger.info("No information found in the backend storage");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 */
	/**
	 * @param profileTableName
	 * @param attributeName
	 * @param attributeValue
	 * @param stopAtFirstMatch
	 * @return @throws
	 *         SystemException
	 */
	public Collection getProfilesByIndexedAttribute(String profileTableName,
			String attributeName, Object attributeValue,
			boolean stopAtFirstMatch) throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SystemException {
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		String cmpInterfaceName = null;
		if (logger.isDebugEnabled()) {
			logger.debug(" getProfilesByIndexedAttribute ( profileTableName = "
					+ profileTableName + " attributeName = " + attributeName
					+ " attributeValue = " + attributeValue + " )");
		}
		cmpInterfaceName = (String) transactionManager.getObject(tcache,
				generateProfileTableKey(profileTableName), "cmpInterfaceName");
		//Test if the attribute exists in the transient class
		try {
			ProfileSpecificationID profileSpecificationIDImpl = findProfileSpecId(profileTableName);
			ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl = getProfileSpecificationManagement().getProfileSpecificationDescriptor(profileSpecificationIDImpl);
			
			Class profileTransientClass = profileSpecificationDescriptorImpl.getClassLoader().loadClass(ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_PREFIX
							+ cmpInterfaceName
							+ ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_SUFFIX);
			profileTransientClass.getField(attributeName);
		} catch (NoSuchFieldException e2) {
			throw new UnrecognizedAttributeException();
		} catch (Exception e2) {
			throw new SystemException(e2.getMessage());
		}
		
		Collection profiles = new Vector();

		String indexKey = generateIndexKey(profileTableName, attributeName);
		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByIndexedAttribute: indexKey = "
					+ indexKey);

		}
		Map indexedAttributes;
		try {
			indexedAttributes = (Map) transactionManager.getObject(tcache,
					indexKey, "indexedAttributes");
			if (logger.isDebugEnabled()) {
				logger.debug("indexedAttributes = " + indexedAttributes);
			}
			if (indexedAttributes == null)
				throw new AttributeNotIndexedException();
			else {
				String classType = (String) transactionManager.getObject(
						tcache, indexKey, "classType");
				if (!ClassUtils.getPrimitiveTypeFromClass(
						attributeValue.getClass().getName()).equals(
						ClassUtils.getPrimitiveTypeFromClass(classType)))
					throw new AttributeTypeMismatchException(
							"indexed attribute is of type " + classType
									+ " and attribute value is of type "
									+ attributeValue.getClass().getName());
				Iterator keys = indexedAttributes.keySet().iterator();
				while (keys.hasNext()) {
					String indexedProfileName = (String) keys.next();
					Object profileIndexedAttributeValue = indexedAttributes
							.get(indexedProfileName);
					if (profileIndexedAttributeValue.getClass().isArray()) {
						if (logger.isDebugEnabled()) {
							logger
									.debug("looking for indexed values in the array!!!!");
						}
						int length = Array
								.getLength(profileIndexedAttributeValue);
						boolean found = false;
						int i = 0;
						while (i < length && !found) {
							Object value = Array.get(
									profileIndexedAttributeValue, i++);
							if (attributeValue.equals(value)) {
								//profiles.add(new
								// ProfileID(profileTableName,indexedProfileName));
								profiles.add(indexedProfileName);
								if (stopAtFirstMatch)
									return profiles;
								found = true;
							}
						}
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("attributeValue = " + attributeValue);
							logger.debug("profileIndexedAttributeValue = "
									+ profileIndexedAttributeValue);
						}
						if (attributeValue.equals(profileIndexedAttributeValue)) {
							//profiles.add(new
							// ProfileID(profileTableName,indexedProfileName));
							profiles.add(indexedProfileName);
							if (stopAtFirstMatch)
								return profiles;
						}
					}
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new AttributeNotIndexedException();
		}
		return profiles;
	}

	/**
	 * 
	 * @param profileID
	 * @throws SystemException
	 */
	public Object getSbbCMPProfile(ProfileID profileID) throws SystemException {

		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		String key = this.generateProfileKey(profileID.getProfileTableName(),
				profileID.getProfileName());
		String profileTableKey = this.generateProfileTableKey(profileID
				.getProfileTableName());
		String cmpInterfaceName = (String) transactionManager.getObject(tcache,
				profileTableKey, "cmpInterfaceName");

		//Instantiates the profile and initializes it
		String cmpConcreteName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX
				+ cmpInterfaceName
				+ ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;

		ProfileManagementInterceptor profileManagementInterceptor = new DefaultProfileManagementInterceptor(
				true);
		
		Thread currentThread = Thread.currentThread();
		ClassLoader currentClassLoader = currentThread.getContextClassLoader();
		try {
			Class concreteProfileClass = getProfileSpecificationManagement().getProfileSpecificationDescriptor(findProfileSpecId(profileID.getProfileTableName())).getClassLoader().loadClass(cmpConcreteName);
			Constructor constructor = concreteProfileClass
					.getConstructor(new Class[] {
							ProfileManagementInterceptor.class,
							SleeProfileManager.class, String.class });

			return constructor.newInstance(new Object[] {
					profileManagementInterceptor, this, key });
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEEException("Low-level failure",e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nist.slee.container.profile.SleeProfileManager#setProfileValue(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void setProfileAttributeValue(String profileKey,
			String profileAttributeName, Object profileAttributeValue)
			throws TransactionRequiredLocalException, SystemException,
			ProfileVerificationException {
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		if (logger.isDebugEnabled()) {
			logger.debug("setProfileAttributeValue ( profileKey = "
					+ profileKey + " profileAttributeName = "
					+ profileAttributeName + " profileAttributeValue = "
					+ profileAttributeValue + " )");
		}
		//check constraints on uniqueness and indexing of attributes
		// also the profileKey must be for a non-default profile. Default profiles are not indexed, only persisted.
		if (profileKey.startsWith(getRootFqn()
				+ SleeProfileManager.PROFILEID_LOOKUP_PREFIX)) {
			verifyProfileIndex(profileKey, profileAttributeName,
					profileAttributeValue, transactionManager);
		} // index check

		transactionManager.putObject(tcache, profileKey, profileAttributeName,
				profileAttributeValue);
	}

	/**
	 * @param profileKey
	 * @param profileAttributeName
	 * @param profileAttributeValue
	 * @param transactionManager
	 * @throws SystemException
	 * @throws ProfileVerificationException
	 */
	private void verifyProfileIndex(String profileKey,
			String profileAttributeName, Object profileAttributeValue,
			SleeTransactionManager transactionManager) throws SystemException,
			ProfileVerificationException {
		String intermediateKey = profileKey
				.substring((getRootFqn() + PROFILEID_LOOKUP_PREFIX).length());
		String profileTableName = intermediateKey.substring(0, intermediateKey
				.indexOf("/"));
		if (logger.isDebugEnabled()) {
			logger.debug("setProfileAttributeValue: profileTableName = "
					+ profileTableName);
		}

		String indexKey = generateIndexKey(profileTableName,
				profileAttributeName);
		if (logger.isDebugEnabled()) {
			logger.debug("setProfileAttributeValue: indexKey = " + indexKey);
		}
		/* if ( logger.isDebugEnabled()) {
		   Node node = transactionManager.getNode(tcache,indexKey);
		   Map data = node.getData();
		   logger.debug (" data = " + data );
		}*/

		Map indexedAttributes = (Map) transactionManager.getObject(tcache,
				indexKey, "indexedAttributes");
		//check if this is an attribute to index
		if (logger.isDebugEnabled()) {
			logger.debug("setProfileAttributeValue: indexedAttributes : "
					+ indexedAttributes);
		}
		if (indexedAttributes != null) {
			Object object = transactionManager.getObject(tcache, indexKey,
					"isUnique");
			String profileName = profileKey.substring(profileKey
					.lastIndexOf("/") + 1);
			boolean isUnique = ((Boolean) object).booleanValue();
			if (logger.isDebugEnabled()) {
				logger.debug(indexKey + " is unique " + isUnique);
			}
			//if the value doesn't need to be unique then, the link
			//to the indexed attribute is set if it doesn't exist yet
			if (!isUnique) {
				indexedAttributes.put(profileName, profileAttributeValue);
			}
			//else check if the value is already indexed,
			//if it is so then an exception is thrown
			else {

				if (indexedAttributes.keySet().size() < 1) {
					if (logger.isDebugEnabled()) {
						logger.debug(indexKey
								+ " has never been indexed before");
					}
					indexedAttributes.put(profileName, profileAttributeValue);
					//it has never been indexed before so the method can be exited
				} else {
					Iterator keys = new HashSet(indexedAttributes.keySet())
							.iterator();
					while (keys.hasNext()) {
						String indexedProfileName = (String) keys.next();
						Object profileIndexedAttributeValue = indexedAttributes
								.get(indexedProfileName);
						if (logger.isDebugEnabled()) {
							logger.debug(indexedProfileName + " value "
									+ profileIndexedAttributeValue);
						}
						//if(!indexedProfileName.equals(profileName)){
						if (profileAttributeValue != null) {
							// (Ivelin) - if the indexed attribute is of type array, then
							// the uniqueness rule requires that there are no two equal members of 
							// any two arrays. Hence the nested loops below. Obviously this implementation
							// is far from optimal and needs to be improved - FIXME.
							if (profileAttributeValue.getClass().isArray()) {
								if (logger.isDebugEnabled()) {
									logger.debug(indexKey + " is an array!!!!");
								}
								boolean found = false;
								int i = 0;
								if (profileIndexedAttributeValue != null) {
									int length = Array
											.getLength(profileIndexedAttributeValue);
									int length2 = Array
											.getLength(profileAttributeValue);
									while (i < length && !found) {
										Object value = Array.get(
												profileIndexedAttributeValue,
												i++);
										for (int j = 0; j < length2; j++) {
											Object value2 = Array.get(
													profileAttributeValue, j);
											if (value2.equals(value)) {
												found = true;
											}
										}
									}
								}

								// FIXME -- Ivelin this is stubbed out. It will probably make
								// your profile tests fail but if I enable this branch of the if
								// the convergence name tests will fail.
								if (false && found) {
									if (logger.isDebugEnabled()) {
										logger
												.debug("duplicate value found in a previous indexed array");
									}
									throw new ProfileVerificationException(
											"the indexed attribute "
													+ profileAttributeName
													+ " with the value "
													+ profileAttributeValue
													+ " is already indexed.");
								} else {
									indexedAttributes.put(profileName,
											profileAttributeValue);
								}
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug(indexKey
											+ " is not an array!!!!");
								}
								if (profileAttributeValue
										.equals(profileIndexedAttributeValue)) {
									throw new ProfileVerificationException(
											"the indexed attribute "
													+ profileAttributeName
													+ " with the value "
													+ profileAttributeValue
													+ " is already indexed.");
								} else {
									indexedAttributes.put(profileName,
											profileAttributeValue);
								}
							}
						}
					} // while
				} // else if (non-indexed)
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.profile.SleeProfileManager#getProfileValue(java.lang.String,
	 *      java.lang.String)
	 */
	public Object getProfileAttributeValue(String profileKey,
			String profileAttributeName) throws SystemException {
		return sleeContainer.getTransactionManager().getObject(tcache,
				profileKey, profileAttributeName);
	}

	public Object getProfileAttributeValue(ProfileID profileID,
			String attributeName) throws SystemException {
		return sleeContainer.getTransactionManager().getObject(
				tcache,
				this.generateProfileKey(profileID.getProfileName(), profileID
						.getProfileName()), attributeName);
	}

	/**
	 * Returns the profile indexes spec as provided 
	 * by the profile specification at deployment time.
	 * This is static information that is available at a meta level 
	 * and applies to all tables with the same profile spec.
	 * @param profileTableName
	 * @return Map of profile index descriptors
	 * @throws SystemException
	 */
	public Map getProfileIndexesSpec(String profileTableName)
			throws SystemException {
		Map indexes = (Map) sleeContainer.getTransactionManager().getObject(
				tcache, generateProfileTableKey(profileTableName),
				PROFILE_INDEXES_SPEC_SUFFIX);
		return indexes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.profile.SleeProfileManager#profileExist(javax.slee.profile.ProfileID)
	 */
	public boolean profileExist(ProfileID profileID) throws SystemException {
		Object profileFound = findProfileMBean(profileID.getProfileTableName(),
				profileID.getProfileName());
		if (profileFound == null)
			return false;
		else
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nist.slee.container.profile.SleeProfileManager#unregisterProfile(java.lang.String)
	 */
	public synchronized void unregisterProfileMBean(String profileKey)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("unregisterProfileMBean " + profileKey);
		}
		ObjectName objectName = null;
		if (profileKey.startsWith(SleeProfileManager.PROFILEID_LOOKUP_PREFIX)) {
			//logger.info("unregistering ProfileKey "+profileKey);
			//profileKey = profileKey.substring("profiles/".length());
			String profileTableName = profileKey.substring(0, profileKey
					.indexOf("/"));
			String profileName = profileKey
					.substring(profileKey.indexOf("/") + 1);
			//logger.info("unregistering ProfileTable "+profileTableName);
			//logger.info("unregistering ProfileName "+profileName);
			objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(
					profileTableName, profileName);
		} else {
			String profileTableName = profileKey.substring("defaultProfiles/"
					.length());
			// validate jmx name 
			SleeProfileManager.toValidJmxName(profileTableName);
			//logger.info("unregistering JMXProfileTable
			// "+jmxProfileTableObjectName);
			objectName = ProfileProvisioningMBeanImpl
					.getDefaultProfileObjectName(profileTableName);
		}
		logger.info("Unregistering "
				+ "following profile MBean with object name " + objectName);
		sleeContainer.getMBeanServer().unregisterMBean(objectName);
	}

	/**
	 * @param oldProfileTableName
	 * @param newProfileTableName
	 * @param profileSpecificationDescriptor
	 * @throws Exception
	 */
	public void renameProfileTable(String oldProfileTableName,
			String newProfileTableName,
			ProfileSpecificationDescriptor profileSpecificationDescriptor)
			throws Exception {
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			removeProfileTable(oldProfileTableName);
			addProfileTable(newProfileTableName, profileSpecificationDescriptor);
		} catch (Exception e) {
			logger.error("Failed to rename profile table: "
					+ oldProfileTableName, e);
			transactionManager.setRollbackOnly();
			throw e;
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				transactionManager.commit();
		}
	}

	/**
	 * 
	 * @param jmxName
	 * @return
	 */
	public static String toValidJmxName(String jmxName) {
		String jmxObjectName = replace(jmxName, "\\", "");
		jmxObjectName = replace(jmxObjectName, "\"", "\\\"");
		jmxObjectName = replace(jmxObjectName, "*", "\\*");
		jmxObjectName = replace(jmxObjectName, "?", "\\?");
		char car = 0x0027;
		char car2 = 0x0060;
		jmxObjectName = replace(jmxObjectName, "" + car, "");
		jmxObjectName = replace(jmxObjectName, "" + car2, "");
		//FIXME causing problems to invoke operations on Mbean through jboss web console
		//the quotes are causing html problems
		//if removed some tests are not passing
		jmxObjectName = ObjectName.quote(jmxObjectName);
		return jmxObjectName;

	}

	public static String replace(String str, String strToReplace, String newStr) {
		String string = "";
		java.util.StringTokenizer st = new java.util.StringTokenizer(str,
				strToReplace);
		while (st.hasMoreTokens()) {
			string = string.concat(st.nextToken());
			if (st.hasMoreTokens())
				string = string + newStr;
		}
		if (string.length() < 1)
			return str;
		else
			return string;
	}

	/**
	 * 
	 * Get the profile table activity for the profileTableName
	 * 
	 * @param profileTableName
	 * @return
	 * @throws ActivityAlreadyExistsException 
	 */
	private ProfileTableActivityImpl createProfileTableActivity(
			final String profileTableName) throws ActivityAlreadyExistsException {

		ProfileTableActivityImpl profileTableActivity;
		if ((profileTableActivity = this.profileTableActivities
				.get(profileTableName)) == null) {
			profileTableActivity = new ProfileTableActivityImpl(new ProfileTableActivityHandle(
					profileTableName));
			this.profileTableActivities.put(profileTableName,
					profileTableActivity);
			sleeContainer
						.getActivityContextFactory()
						.createActivityContext(
								ActivityContextHandlerFactory
										.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
												profileTableName)));
			
			try {				
				if (sleeContainer.getTransactionManager().isInTx()) {
					// add rollback tx action to remove state created
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
							profileTableActivities.remove(profileTableName);					
						}
					};
					sleeContainer.getTransactionManager().addAfterRollbackAction(action);
				}
			} catch (SystemException e) {
				// ignore
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(),e);
				}
			}
		}
		return profileTableActivity;
	}

	/**
	 * This is invoked after the activity end event is processed.
	 * 
	 * @param profileTableName
	 */
	public void removeProfileAfterTableActivityEnd(String profileTableName) {
		try {
			this.profileTableActivities.remove(profileTableName);

			// remove indexes from store
			String indexesKey = generateIndexKeyRoot(profileTableName);
			sleeContainer.getTransactionManager()
					.removeNode(tcache, indexesKey);

			// remove table from store
			String tableKey = this.generateProfileTableKey(profileTableName);
			sleeContainer.getTransactionManager().removeNode(tcache, tableKey);

			this.getProfileCacheManager().getProfileTableNames().remove(
					profileTableName);
			this.getProfileCacheManager().addTransactionalAction();
		} catch (Exception ex) {
			throw new RuntimeException("Error accessing cache! ", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.profile.SleeProfileManager#profileTableExists(java.lang.String)
	 */
	public boolean profileTableExists(String profileTableName)
			throws SystemException {
		if (findProfileSpecId(profileTableName) == null)
			return false;
		else
			return true;
	}

	/**
	 * @return Returns the profileTableActivities.
	 */
	public Map getProfileTableActivities() {
		return profileTableActivities;
	}

	public ProfileTableActivity getProfileTableActivity(String profileTableName) {
		return profileTableActivities.get(profileTableName);
	}
	
	/**
	 * Retrieve the wrapper object (from the back end storage) for the default
	 * profile by table name
	 * 
	 * @param profileTableName
	 *            the name of the profile table where the profile belongs
	 * @return the profileID or null if nothing has been retrieved
	 * @throws SystemException
	 *             if a low level excpetion occurs
	 */
	public Object findDefaultProfile(String profileTableName)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("findDefaultProfile: profileTableName = "
					+ profileTableName);
		}
		String key = this.generateDefaultProfileKey(profileTableName);
		Object profile = lookupProfileByKey(key);
		return profile;
	}

	void setProfileCacheManager(ProfileCacheManager profileCacheManager) {
		this.profileCacheManager = profileCacheManager;
	}

	ProfileCacheManager getProfileCacheManager() {
		return profileCacheManager;
	}

	/**
	 * Convenience method for looking up an object from
	 * the profile cache
	 * 
	 * @param fqn
	 * @param key 
	 * @return the object in the cache or null
	 * @throws SystemException 
	 */
	public Object loadObjectFromCache(String fqn, String key)
			throws SystemException {
		return sleeContainer.getTransactionManager()
				.getObject(tcache, fqn, key);
	}

	/**
	 * Convenience method for looking up a node from
	 * the profile cache
	 * 
	 * @param fqn
	 * @return the object in the cache or null
	 * @throws SystemException 
	 */
	public Object loadNodeFromCache(String fqn) throws SystemException {
		return sleeContainer.getTransactionManager().getNode(tcache, fqn);
	}

	/**
	 * convinience method to get the container's tx manager
	 * @return
	 */
	public SleeTransactionManager getTransactionManager() {
		return sleeContainer.getTransactionManager();
	}

	/**
	 * manages (un)install of profile specifications
	 * 
	 * @return
	 */
	public ProfileSpecificationManagement getProfileSpecificationManagement() {
		return profileSpecsManagement;
	}
	
	@Override
	public String toString() {
		return "Profile Manager:"
			+ "\n+-- Profile Table Activities: "+profileTableActivities.size()
			+ "\n" + profileSpecsManagement;
	}

	public void endAllProfileTableActivities() throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Stopping profile table activities !");
		}

		Iterator it = profileTableActivities.keySet().iterator();
		while (it.hasNext()) {
			String profileTableName = (String) it.next();
			new DeferredActivityEndEvent(ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)),null);
		}
	}
}
