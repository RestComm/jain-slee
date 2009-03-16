/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.DeploymentException;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileIndex;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.deployment.SbbClassCodeGenerator;
import org.mobicents.slee.container.deployment.interceptors.DefaultProfileManagementInterceptor;
import org.mobicents.slee.container.deployment.interceptors.ProfileManagementInterceptor;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.mobicents.slee.container.management.jmx.ProfileProvisioningMBeanImpl;
import org.mobicents.slee.container.profile.SingleProfileException;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.cache.ProfileManagerCacheData;
import org.mobicents.slee.runtime.cache.ProfileTableCacheData;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.facilities.profile.ProfileRemovedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * This class is used by the SLEE to manage Profiles. It calls out to the
 * jboss-cache that put everything into the backend storage. Changed to
 * SleeProfileManager in management class. task stay the same but class has been
 * adopted to new deployment procedure
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * @depraceted
 * @author Ivelin Ivanov
 * @author M. Ranganathan
 * @author martins
 * @author baranowb
 * 
 */
public class SleeProfileManager {

	private static final Logger logger = Logger.getLogger(SleeProfileManager.class);
	// the container
	private final SleeContainer sleeContainer;
	// the profile specs manager

	private final ProfileManagerCacheData cacheData;

	private final static SleeProfileClassCodeGenerator sleeProfileClassCodeGenerator = new SleeProfileClassCodeGenerator();

	/**
	 * Constructor
	 */
	public SleeProfileManager(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;

		cacheData = sleeContainer.getCache().getProfileManagerCacheData();
		cacheData.create();
	}

	public void installProfile(ProfileSpecificationComponent component) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		// change classloader
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(component.getClassLoader());
			// Set up the comp/env naming context for the Sbb.

			// generate class code for the sbb
			this.sleeProfileClassCodeGenerator.process(component);

			// FIXME: deploy
			// FIXME:
			// initializePersistedProfiles(profileSpecificationDescriptor.getCMPInterfaceName());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	public void uninstallProfile(ProfileSpecificationComponent component) {
		// TODO Auto-generated method stub

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
	public void addProfileTable(String profileTableName, ProfileSpecificationComponent component) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("addProfileTable " + profileTableName);
		}

		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		boolean b = transactionManager.requireTransaction();

		try {
			// create profile table activity context
			sleeContainer.getActivityContextFactory().createActivityContext(ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)));

//			 create profile table cache data
			ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileTableName);
			profileTableCacheData.create();
			// fill descriptor info
			ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl =  component.getDescriptor();
			//profile hints == single profile?
			profileTableCacheData.setProfileTableData(profileSpecificationDescriptorImpl.getProfileSpecificationID(), profileSpecificationDescriptorImpl.getProfileCMPInterface().getProfileCmpInterfaceName(),
					profileSpecificationDescriptorImpl.getProfileHints(), profileSpecificationDescriptorImpl.getIndexedAttributes());
			if (logger.isDebugEnabled()) {
				logger.debug("created cache data for profile table " + profileTableName);
			}

			 //add indexed attributes
			Set<MProfileIndex> indexes = profileSpecificationDescriptorImpl.getIndexedAttributes();
			for (MProfileIndex index : indexes) {
				String attributeName = index.getName();
//				Class profileTransientClass = Thread.currentThread().getContextClassLoader().loadClass(
//						ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_PREFIX + component.getDescriptor().getProfileCMPInterface().getProfileCmpInterfaceName()
//								+ ConcreteClassGeneratorUtils.PROFILE_TRANSIENT_CLASS_NAME_SUFFIX);
				
				Field profileIndexedField = component.getProfilePersistanceTransientStateConcreteClass().getField(attributeName);
				Class classType = profileIndexedField.getType();
				String classTypeString = null;
				Boolean isArray = null;
				Boolean isUnique = (Boolean) index.getUnique();
				if (classType.isArray()) {
					classTypeString = classType.getComponentType().getName();
					isArray = Boolean.TRUE;
				} else {
					classTypeString = classType.getName();
					isArray = Boolean.FALSE;
				}
				profileTableCacheData.addIndexedAttribute(attributeName, classTypeString, isArray, isUnique);
				if (logger.isDebugEnabled()) {
					logger.debug("Created indexed attribute node " + attributeName + " for profile table node " + profileTableName);
				}
			}
			 //Instantiates the default profile and initializes it
			instantiateProfile(component.getProfileSpecificationID(), profileTableName, null, false, profileTableCacheData);
			if (logger.isDebugEnabled()) {
				logger.debug("added profile table " + profileTableName);
			}
		} catch (Exception e) {
			transactionManager.setRollbackOnly();
			throw e;
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				transactionManager.commit();
		}
	}

	/**
	 * Removes a profile Table, its default profile, indexes and all other
	 * associated profiles
	 * 
	 * @param profileTableName
	 *            the profile table name
	 * @throws UnrecognizedProfileTableNameException
	 *             if the profile table cannot be found
	 */
	public void removeProfileTable(String profileTableName) throws TransactionRequiredLocalException, SystemException, UnrecognizedProfileTableNameException {

		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();

		boolean b = transactionManager.requireTransaction();
		try {

			if (logger.isDebugEnabled()) {
				logger.debug("removeProfileTable: removing profileTable=" + profileTableName);
			}

			ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileTableName);
			if (!profileTableCacheData.exists()) {
				throw new UnrecognizedProfileTableNameException("Could not find profile table " + profileTableName);
			}

			// remove default profile
			profileTableCacheData.removeProfile(null);
			// unregistering the default MBean profile from the mbean server
			ObjectName defaultProfileObjectName = ProfileProvisioningMBeanImpl.getDefaultProfileObjectName(profileTableName);

			if (sleeContainer.getMBeanServer().isRegistered(defaultProfileObjectName)) {
				logger.info("Unregistering following " + "profile table MBean with object name " + defaultProfileObjectName);
				sleeContainer.getMBeanServer().unregisterMBean(defaultProfileObjectName);
			}

			// remove the table profiles
			for (Object profileName : profileTableCacheData.getAndRemoveProfiles()) {
				// unregistering the MBean profile from the server
				ObjectName objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(profileTableName, (String) profileName);
				if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
					logger.info("Unregistering " + "following profile MBean with object name " + objectName);
					sleeContainer.getMBeanServer().unregisterMBean(objectName);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("removeProfileTable: " + objectName + " is not registered! ");
					}
				}
			}

			profileTableCacheData.remove();
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
			ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach, false);
			ac.end();

		} catch (Exception e) {
			logger.error("Failed to remove profile table: " + profileTableName, e);
			transactionManager.setRollbackOnly();
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				try {
					transactionManager.commit();
				} catch (Exception e) {
					throw new SLEEException("Failure.",e);
				} 
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
	public ObjectName addProfileToProfileTable(String profileTableName, String newProfileName) throws SystemException, SingleProfileException {

		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();

		ObjectName objectName = null;

		boolean b = transactionManager.requireTransaction();
		try {

			// get profile table cache data
			ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileTableName);

			// Verify if the Profile Scheme indicated that there can be only one
			// profile in this table
			boolean isSingleProfile = profileTableCacheData.isSingleProfile().booleanValue();
			if (isSingleProfile && profileTableCacheData.hasCommittedProfiles()) {
				throw new SingleProfileException();
			}

			// Instantiates the profile
			objectName = instantiateProfile(profileTableCacheData.getProfileSpecId(), profileTableName, newProfileName, false, profileTableCacheData);

			// At this point the profile MBean is created, but the profile is
			// not registered with the SLEE
			// until commitProfile() is explicitly called on the profile MBean

		} catch (Exception e) {
			if (objectName != null) {
				if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
					logger.info("[addProfileToProfileTable] Unregistering " + "following profile MBean with object name " + objectName);
					try {
						sleeContainer.getMBeanServer().unregisterMBean(objectName);
					} catch (Exception e1) {
						logger.error("Failed addProfileToProfileTable. Could not unregister " + objectName, e1);
						throw new SystemException("Failed addProfileToProfileTable. Error: " + e1.getMessage());
					}
				}
			}
			transactionManager.setRollbackOnly();
			if (e instanceof SingleProfileException) {
				throw (SingleProfileException) e;
			} else {
				logger.error(e.getMessage(), e);
				throw new SystemException("Failed addProfileToProfileTable. Error: " + e.getMessage());
			}

		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				try {
					transactionManager.commit();
				} catch (Exception e) {
					throw new SLEEException("Failure.",e);
				} 
		}

		return objectName;
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
	protected ObjectName instantiateProfile(ProfileSpecificationID specId, String profileTableName, String profileName, boolean loadDataFromBackendStorage, ProfileTableCacheData profileTableCacheData)
			throws Exception {

		boolean isDefaultProfile = profileName == null;

		profileTableCacheData.addProfile(profileName);

		// FIXME: remove this
		// Instantiates the profile and initializes it
		String cmpInterfaceName = profileTableCacheData.getCmpInterfaceName();
		

		ProfileManagementInterceptor profileManagementInterceptor = new DefaultProfileManagementInterceptor();

		profileManagementInterceptor.setProfileTableName(profileTableName);
		profileManagementInterceptor.setProfileName(profileName);

		ProfileSpecificationID profileSpecificationID = findProfileSpecId(profileTableName);
		ProfileSpecificationComponent component = this.sleeContainer.getComponentRepositoryImpl().getComponentByID(profileSpecificationID);

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {

			Thread.currentThread().setContextClassLoader(component.getClassLoader());
			Class concreteProfileClass = component.getProfileCmpConcreteClass();
			Constructor constructor = concreteProfileClass.getConstructor(new Class[] { ProfileManagementInterceptor.class, SleeProfileManager.class, String.class, String.class });
			ProfileManagement profileManagement = (ProfileManagement) constructor.newInstance(new Object[] { profileManagementInterceptor, this, profileTableName, profileName });
			if (loadDataFromBackendStorage) {
				profileManagementInterceptor.loadStateFromBackendStorage(cmpInterfaceName, component.getClassLoader());
			} else {
				if (isDefaultProfile) {
					profileManagement.profileInitialize();
					profileManagement.profileStore();
					profileManagement.profileVerify();
					profileManagementInterceptor.persistProfile();
				} else {
					// copy the attribute values from the default profile
					profileManagementInterceptor.copyStateFromDefaultProfile(cmpInterfaceName, component.getClassLoader());
				}
			}

			// Instantiates the Profile MBean and initializes it
			String mbeanConcreteName = ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX + cmpInterfaceName + ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX;

			Class concreteProfileMBeanClass = component.getProfileMBeanConcreteImplClass();
			Constructor profileMBeanConstructor = concreteProfileMBeanClass.getConstructor(new Class[] { ProfileManagementInterceptor.class, Object.class });
			ProfileMBean profileMBean = (ProfileMBean) profileMBeanConstructor.newInstance(new Object[] { profileManagementInterceptor, profileManagement });
			ObjectName objectName = null;
			if (isDefaultProfile) {
				objectName = ProfileProvisioningMBeanImpl.getDefaultProfileObjectName(profileTableName);
			} else {
				objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(profileTableName, profileName);
			}
			if (logger.isDebugEnabled())
				logger.debug("[instantiateProfile]Registering " + "following profile MBean with object name " + objectName);
			sleeContainer.getMBeanServer().registerMBean(profileMBean, objectName);
			return objectName;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void commitProfile(ProfileID profileID) {
		// get profile table cache data
		sleeContainer.getCache().getProfileTableCacheData(profileID.getProfileTableName()).setProfileAsCommitted(profileID.getProfileName());
	}

	/**
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return @throws Exception
	 */
	public ProfileManagement instantiateLastCommittedProfile(String profileTableName, String profileName) throws Exception {

		/*
		 * BEWARE : Profiles are not stored in memory, so a new profile is
		 * initialized and its state is loaded from the backend storage. Indeed,
		 * the profile should be passed to in the profile removed event and this
		 * profile should contain the last committed state
		 */

		// get profile table cache node
		ProfileTableCacheData profileTableCacheNode = sleeContainer.getCache().getProfileTableCacheData(profileTableName);
		// get profile cache node absolute name
		boolean isDefaultProfile = profileName == null;

		if (logger.isDebugEnabled()) {
			logger.debug("instantiating Last Committed Profile to fire an event" + "profileTable" + profileTableName + ",profileName=" + profileName);
		}

		// FIXME: remove this
		String cmpInterfaceName = profileTableCacheNode.getCmpInterfaceName();

		// Instantiates the profile and initializes it
		//String cmpConcreteName = ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_PREFIX + cmpInterfaceName + ConcreteClassGeneratorUtils.PROFILE_CONCRETE_CLASS_NAME_SUFFIX;
		ProfileSpecificationID profileSpecificationID = findProfileSpecId(profileTableName);
		ProfileSpecificationComponent component = this.sleeContainer.getComponentRepositoryImpl().getComponentByID(profileSpecificationID);
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			ProfileManagementInterceptor profileManagementInterceptor = new DefaultProfileManagementInterceptor();
			Thread.currentThread().setContextClassLoader(component.getClassLoader());

			Class concreteProfileClass = component.getProfileCmpConcreteClass();
			Constructor constructor = concreteProfileClass.getConstructor(new Class[] { ProfileManagementInterceptor.class, SleeProfileManager.class, String.class, String.class });
			ProfileManagement profileManagement = (ProfileManagement) constructor.newInstance(new Object[] { profileManagementInterceptor, this, profileTableName, profileName });
			profileManagementInterceptor.loadStateFromBackendStorage(component.getProfileCmpConcreteClass().getName(), component.getClassLoader());
			/* Profile created from backend storage */
			if (logger.isDebugEnabled()) {
				logger.debug("Added profile class: " + profileManagement.getClass());
			}
			return profileManagement;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

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
	public void removeProfile(String profileTableName, String profileName) throws SystemException {

		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		boolean b = transactionManager.requireTransaction();

		try {

			if (logger.isDebugEnabled()) {
				logger.debug("removing profile " + profileName + " from profile table " + profileTableName);
			}

			if (profileName == null) {
				throw new IllegalArgumentException("can't remove default profile");
			}

			// get profile table cache node
			ProfileTableCacheData profileTableCacheNode = sleeContainer.getCache().getProfileTableCacheData(profileTableName);

			// FIXME emmartins : is this really needed???
			// getting last commited profile
			ProfileManagement profileManagement = instantiateLastCommittedProfile(profileTableName, profileName);

			// remove from parent
			profileTableCacheNode.removeProfile(profileName);

			// get mbean object name
			ObjectName objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(profileTableName, profileName);

			// Fire the removed event only when the transaction commits
			final ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory = sleeContainer.getProfileTableActivityContextInterfaceFactory();
			if (profileTableActivityContextInterfaceFactory == null) {
				final String s = "got NULL ProfileTable ACI Factory";
				logger.error(s);
				throw new SystemException(s);
			}

			Address profileAddress = new Address(AddressPlan.SLEE_PROFILE, profileTableName + "/" + profileName);

			// unregistering the MBean profile from the server
			if (!sleeContainer.getMBeanServer().isRegistered(objectName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("removing impossible, MBean " + objectName + " not registered !!!");
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("[removeProfile]Unregistering " + "following profile MBean with object name " + objectName);
				}
				sleeContainer.getMBeanServer().unregisterMBean(objectName);
			}
			// Firing profile removed event only when the transaction commits
			ProfileRemovedEventImpl profileRemovedEvent = new ProfileRemovedEventImpl(profileAddress, new ProfileID(profileAddress), profileManagement, profileTableActivityContextInterfaceFactory);

			// Profile Removed Event.
			// After a Profile is removed from a Profile Table,
			// the SLEE fires and delivers a Profile Removed
			// Event on the Profile Table’s Activity.
			ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(
					ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)), false);
			ac.fireEvent(new DeferredEvent(profileRemovedEvent.getEventTypeID(), profileRemovedEvent, ac, profileAddress));

			if (logger.isDebugEnabled()) {
				logger.debug("Queued following removed event:" + profileRemovedEvent.getEventTypeID() + ",:" + ac.getActivityContextId());
			}

		} catch (Exception e) {
			String err = "Failed removeProfile " + profileName + " from profile table " + profileTableName + " because of " + e.getMessage();
			logger.error(err, e);
			transactionManager.setRollbackOnly();
			throw new SystemException(err);
		} finally {
			displayAllProfilePersistentInformation();
			if (b)
				try {
					transactionManager.commit();
				} catch (Exception e) {
					throw new SLEEException("Failure.",e);
				} 
		}
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
	public ProfileSpecificationID findProfileSpecId(String profileTableName) throws SystemException {

		ProfileSpecificationID profileSpecificationID = null;

		boolean b = sleeContainer.getTransactionManager().requireTransaction();
		try {
			// get profile table cache node
			ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileTableName);
			if (profileTableCacheData.exists()) {
				profileSpecificationID = profileTableCacheData.getProfileSpecId();
			}
		} catch (RuntimeException e) {
			sleeContainer.getTransactionManager().setRollbackOnly();
			throw e;
		} finally {
			if (b)
				try {
					sleeContainer.getTransactionManager().commit();
				} catch (Exception e) {
					throw new SLEEException("Failure.",e);
				} 
		}
		if (logger.isDebugEnabled()) {
			logger.debug("findProfileSpecId: found profile spec id " + profileSpecificationID + " for profile table = " + profileTableName);
		}
		return profileSpecificationID;
	}

	/**
	 * Retrieve all profiles tables
	 * 
	 */
	public Set<String> getProfileTables() {

		Set<String> result = null;
		boolean b = sleeContainer.getTransactionManager().requireTransaction();
		boolean rb = true;
		try {
			if (cacheData.exists()) {
				result = cacheData.getProfileTables();
			}
			rb = false;
		} catch (Exception e) {
			logger.error("error getting profile table names ", e);
		} finally {
			displayAllProfilePersistentInformation();
			try {
				if (rb) {
					sleeContainer.getTransactionManager().setRollbackOnly();
				}
				if (b)
					try {
						sleeContainer.getTransactionManager().commit();
					} catch (Exception e) {
						throw new SLEEException("Failure.",e);
					} 
			} catch (SystemException ex) {
				throw new RuntimeException("Unexpected tx manager failure ", ex);
			}
		}
		if (result == null) {
			result = Collections.EMPTY_SET;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getProfileTables() result is " + result);
		}

		return result;
	}

	/**
	 * Lookup the profile MBean Object name. It could be a non-committed profile
	 * view.
	 * 
	 * @param profileTableName
	 *            the name of the profile table where the profile belongs
	 * @param profileName
	 *            the profile name
	 * @return the MBean object name if the profile exists or null otherwise
	 */
	public ObjectName findProfileMBean(String profileTableName, String profileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("findProfile: profileTableName = " + profileTableName + " profileName = " + profileName);
		}

		ObjectName objectName = null;
		try {
			if (profileName == null) {
				objectName = ProfileProvisioningMBeanImpl.getDefaultProfileObjectName(profileTableName);
			} else {
				objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(profileTableName, profileName);
			}
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
	 * 
	 * Checks whether a profile has been committed, given its table name and
	 * profile name
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return true if the profile has been committed, false otherwise
	 * @throws SystemException
	 */
	public boolean isProfileCommitted(String profileTableName, String profileName) throws Exception {
		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		boolean b = transactionManager.requireTransaction();
		try {
			return sleeContainer.getCache().getProfileTableCacheData(profileTableName).isProfileCommitted(profileName);
		} catch (Exception e) {
			transactionManager.setRollbackOnly();
			throw e;
		} finally {
			if (b)
				transactionManager.commit();
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
	public Set getProfiles(String profileTableName) {

		return sleeContainer.getCache().getProfileTableCacheData(profileTableName).getProfiles();

	}

	/**
	 * Helper method to display all information related to profiles stored in
	 * the backend storage
	 */
	protected void displayAllProfilePersistentInformation() {
		return;
	}

	/**
	 * @param profileTableName
	 * @param attributeName
	 * @param attributeValue
	 * @param stopAtFirstMatch
	 * @return @throws SystemException
	 */
	public Set<String> getProfilesByIndexedAttribute(String profileTableName, String attributeName, Object attributeValue, boolean stopAtFirstMatch) throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException, SystemException {

		//SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();

		ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileTableName);
		

		if (logger.isDebugEnabled()) {
			logger.debug(" getProfilesByIndexedAttribute ( profileTableName = " + profileTableName + " attributeName = " + attributeName + " attributeValue = " + attributeValue + " )");
		}

		// Test if the attribute exists in the transient class
		// FIXME: do wee need to switch?
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			try {
				ProfileSpecificationID profileSpecificationIDImpl = findProfileSpecId(profileTableName);
				ProfileSpecificationComponent component = this.sleeContainer.getComponentRepositoryImpl().getComponentByID(profileSpecificationIDImpl);
				Thread.currentThread().setContextClassLoader(component.getClassLoader());
				ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl = component.getDescriptor();
				Class profileTransientClass = component.getProfilePersistanceTransientStateConcreteClass();
				profileTransientClass.getField(attributeName);
			} catch (NoSuchFieldException e2) {
				throw new UnrecognizedAttributeException();
			} catch (Exception e2) {
				throw new SystemException(e2.getMessage());
			}

			Set<String> profiles = new HashSet<String>();
			try {
				if (!profileTableCacheData.isIndexedAttribute(attributeName)) {
					throw new AttributeNotIndexedException();
				} else {
					String classType = profileTableCacheData.getIndexedAttributeClassType(attributeName);

					if (!ClassUtils.getPrimitiveTypeFromClass(attributeValue.getClass().getName()).equals(ClassUtils.getPrimitiveTypeFromClass(classType)))
						throw new AttributeTypeMismatchException("indexed attribute is of type " + classType + " and attribute value is of type " + attributeValue.getClass().getName());

					Map indexMap = profileTableCacheData.getIndexedAttributeIndexMap(attributeName);
					if (indexMap != null) {
						for (Object obj : indexMap.keySet()) {

							String indexedProfileName = (String) obj;
							Object profileIndexedAttributeValue = indexMap.get(indexedProfileName);
							if (profileIndexedAttributeValue.getClass().isArray()) {
								if (logger.isDebugEnabled()) {
									logger.debug("looking for indexed values in the array!!!!");
								}
								int length = Array.getLength(profileIndexedAttributeValue);
								boolean found = false;
								int i = 0;
								while (i < length && !found) {
									Object value = Array.get(profileIndexedAttributeValue, i++);
									if (attributeValue.equals(value)) {
										// profiles.add(new
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
									logger.debug("profileIndexedAttributeValue = " + profileIndexedAttributeValue);
								}
								if (attributeValue.equals(profileIndexedAttributeValue)) {
									// profiles.add(new
									// ProfileID(profileTableName,indexedProfileName));
									profiles.add(indexedProfileName);
									if (stopAtFirstMatch)
										return profiles;
								}
							}

						}
					}
				}
			} catch (RuntimeException e) {
				logger.error(e.getMessage(), e);
				throw new AttributeNotIndexedException();
			}
			return profiles;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	/**
	 * 
	 * @param profileID
	 * @throws SystemException
	 */
	public Object getSbbCMPProfile(ProfileID profileID) throws SystemException {

		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();

		ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileID.getProfileTableName());
		

		// Instantiates the profile and initializes it
		

		ProfileManagementInterceptor profileManagementInterceptor = new DefaultProfileManagementInterceptor(true);

		Thread currentThread = Thread.currentThread();
		ClassLoader oldClassLoader = currentThread.getContextClassLoader();
		//ClassLoader currentClassLoader = currentThread.getContextClassLoader();
		try {
			ProfileSpecificationComponent componenet = this.sleeContainer.getComponentRepositoryImpl().getComponentByID(profileTableCacheData.getProfileSpecId());
			currentThread.setContextClassLoader(componenet.getClassLoader());
			Class concreteProfileClass = componenet.getProfileCmpConcreteClass();
			Constructor constructor = concreteProfileClass.getConstructor(new Class[] { ProfileManagementInterceptor.class, SleeProfileManager.class, String.class, String.class });

			return constructor.newInstance(new Object[] { profileManagementInterceptor, this, profileID.getProfileTableName(), profileID.getProfileName() });
		} catch (Exception e) {
			throw new SLEEException("Low-level failure", e);
		} finally {
			currentThread.setContextClassLoader(oldClassLoader);
		}
	}

	public void setProfileAttributeValue(String profileTableName, String profileName, String profileAttributeName, Object profileAttributeValue) throws TransactionRequiredLocalException,
			SystemException, ProfileVerificationException {

		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();

		if (logger.isDebugEnabled()) {
			logger.debug("setProfileAttributeValue ( profileTable = " + profileTableName + " profileName = " + profileName + " profileAttributeName = " + profileAttributeName
					+ " profileAttributeValue = " + profileAttributeValue + " )");
		}

		ProfileTableCacheData profileTableCacheData = sleeContainer.getCache().getProfileTableCacheData(profileTableName);

		// check constraints on uniqueness and indexing of attributes
		// also the profile name must be for a non-default profile. Default
		// profiles are not indexed, only persisted.
		if (profileName != null) {
			verifyProfileIndex(profileTableName, profileName, profileAttributeName, profileAttributeValue, transactionManager, profileTableCacheData);
		}

		profileTableCacheData.setProfileAttributeValue(profileName, profileAttributeName, profileAttributeValue);
	}

	/**
	 * 
	 * @param profileAbsoluteCacheNodeName
	 * @param profileAttributeName
	 * @param profileAttributeValue
	 * @param transactionManager
	 * @throws SystemException
	 * @throws ProfileVerificationException
	 */
	private void verifyProfileIndex(String profileTable, String profileName, String profileAttributeName, Object profileAttributeValue, SleeTransactionManager transactionManager,
			ProfileTableCacheData profileTableCacheData) throws SystemException, ProfileVerificationException {

		if (logger.isDebugEnabled()) {
			logger.debug("verifyProfileIndex: profileTable = " + profileTable + " , profileName = " + profileName + " , attributeName = " + profileAttributeName + " , attributeValue = "
					+ profileAttributeValue);
		}

		if (profileTableCacheData.isIndexedAttribute(profileAttributeName)) {

			if (logger.isDebugEnabled()) {
				logger.debug("Found indexed attr profileTable = " + profileTable + " , profileName = " + profileName + " , attributeName = " + profileAttributeName);
			}

			Map indexMap = profileTableCacheData.getIndexedAttributeIndexMap(profileAttributeName);

			boolean isUnique = profileTableCacheData.isUniqueIndexedAttribute(profileAttributeName).booleanValue();

			// if the value doesn't need to be unique then, the link
			// to the indexed attribute is set if it doesn't exist yet
			if (!isUnique) {
				indexMap.put(profileName, profileAttributeValue);
			}
			// else check if the value is already indexed,
			// if it is so then an exception is thrown
			else {

				if (indexMap.keySet().size() < 1) {
					if (logger.isDebugEnabled()) {
						logger.debug("indexed attr profileTable = " + profileTable + " , profileName = " + profileName + " , attributeName = " + profileAttributeName
								+ " has never been indexed before");
					}
					indexMap.put(profileName, profileAttributeValue);
					// it has never been indexed before so the method can be
					// exited
				} else {
					Iterator keys = new HashSet(indexMap.keySet()).iterator();
					while (keys.hasNext()) {
						String indexedProfileName = (String) keys.next();
						Object profileIndexedAttributeValue = indexMap.get(indexedProfileName);
						if (logger.isDebugEnabled()) {
							logger.debug(indexedProfileName + " value " + profileIndexedAttributeValue);
						}
						// if(!indexedProfileName.equals(profileName)){
						if (profileAttributeValue != null) {
							// (Ivelin) - if the indexed attribute is of type
							// array, then
							// the uniqueness rule requires that there are no
							// two equal members of
							// any two arrays. Hence the nested loops below.
							// Obviously this implementation
							// is far from optimal and needs to be improved -
							// FIXME.
							if (profileAttributeValue.getClass().isArray()) {
								logger.debug("indexed attr profileTable = " + profileTable + " , profileName = " + profileName + " , attributeName = " + profileAttributeName + " is an array");
								boolean found = false;
								int i = 0;
								if (profileIndexedAttributeValue != null) {
									int length = Array.getLength(profileIndexedAttributeValue);
									int length2 = Array.getLength(profileAttributeValue);
									while (i < length && !found) {
										Object value = Array.get(profileIndexedAttributeValue, i++);
										for (int j = 0; j < length2; j++) {
											Object value2 = Array.get(profileAttributeValue, j);
											if (value2.equals(value)) {
												found = true;
											}
										}
									}
								}

								// FIXME -- Ivelin this is stubbed out. It will
								// probably make
								// your profile tests fail but if I enable this
								// branch of the if
								// the convergence name tests will fail.
								if (false && found) {
									if (logger.isDebugEnabled()) {
										logger.debug("duplicate value found in a previous indexed array");
									}
									throw new ProfileVerificationException("the indexed attribute " + profileAttributeName + " with the value " + profileAttributeValue + " is already indexed.");
								} else {
									indexMap.put(profileName, profileAttributeValue);
								}
							} else {
								logger.debug("indexed attr profileTable = " + profileTable + " , profileName = " + profileName + " , attributeName = " + profileAttributeName + " is not an array");
								if (profileAttributeValue.equals(profileIndexedAttributeValue)) {
									throw new ProfileVerificationException("the indexed attribute " + profileAttributeName + " with the value " + profileAttributeValue + " is already indexed.");
								} else {
									indexMap.put(profileName, profileAttributeValue);
								}
							}
						}
					} // while
				} // else if (non-indexed)
			}
		}
	}

	public Object getProfileAttributeValue(String profileTableName, String profileName, String profileAttributeName) throws SystemException {
		return sleeContainer.getCache().getProfileTableCacheData(profileTableName).getProfileAttributeValue(profileName, profileAttributeName);
	}

	/**
	 * Returns the profile indexes spec as provided by the profile specification
	 * at deployment time. This is static information that is available at a
	 * meta level and applies to all tables with the same profile spec.
	 * 
	 * @param profileTableName
	 * @return Map of profile index descriptors
	 * @throws SystemException
	 */
	public Map getProfileIndexesSpec(String profileTableName) throws SystemException {

		return sleeContainer.getCache().getProfileTableCacheData(profileTableName).getProfileIndexes();

	}

	public boolean profileExist(ProfileID profileID) throws SystemException {
		Object profileFound = findProfileMBean(profileID.getProfileTableName(), profileID.getProfileName());
		if (profileFound == null)
			return false;
		else
			return true;
	}

	public void unregisterProfileMBean(String profileTableName, String profileName) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("unregisterProfileMBean profileTableName=" + profileTableName + " profileName=" + profileName);
		}

		ObjectName objectName = null;
		if (profileName == null) {
			// validate jmx name
			SleeProfileManager.toValidJmxName(profileTableName);
			// logger.info("unregistering JMXProfileTable
			// "+jmxProfileTableObjectName);
			objectName = ProfileProvisioningMBeanImpl.getDefaultProfileObjectName(profileTableName);
		} else {
			objectName = ProfileProvisioningMBeanImpl.getProfileObjectName(profileTableName, profileName);
		}

		logger.info("Unregistering " + "following profile MBean with object name " + objectName);
		sleeContainer.getMBeanServer().unregisterMBean(objectName);
	}

	/**
	 * @param oldProfileTableName
	 * @param newProfileTableName
	 * @param profileSpecificationDescriptor
	 * @throws Exception
	 */
	public void renameProfileTable(String oldProfileTableName, String newProfileTableName, ProfileSpecificationComponent component) throws Exception {
		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		boolean b = transactionManager.requireTransaction();
		try {
			removeProfileTable(oldProfileTableName);
			addProfileTable(newProfileTableName, component);
		} catch (Exception e) {
			logger.error("Failed to rename profile table: " + oldProfileTableName, e);
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
		// FIXME causing problems to invoke operations on Mbean through jboss
		// web console
		// the quotes are causing html problems
		// if removed some tests are not passing
		jmxObjectName = ObjectName.quote(jmxObjectName);
		return jmxObjectName;

	}

	public static String replace(String str, String strToReplace, String newStr) {
		String string = "";
		java.util.StringTokenizer st = new java.util.StringTokenizer(str, strToReplace);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.profile.SleeProfileManager#profileTableExists
	 * (java.lang.String)
	 */
	public boolean profileTableExists(String profileTableName) throws SystemException {

		return sleeContainer.getCache().getProfileTableCacheData(profileTableName).exists();
	}

	public ProfileTableActivity getProfileTableActivity(String profileTableName) {

		try {
			if (profileTableExists(profileTableName)) {
				return new ProfileTableActivityImpl(new ProfileTableActivityHandle(profileTableName));
			}
		} catch (SystemException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * convinience method to get the container's tx manager
	 * 
	 * @return
	 */
	public SleeTransactionManager getTransactionManager() {
		return sleeContainer.getTransactionManager();
	}

	@Override
	public String toString() {
		return "Profile Manager:" + "\n+-- Profile Tables: " + getProfileTables();
	}

	public void endAllProfileTableActivities() throws SystemException {

		for (Object obj : getProfileTables()) {
			logger.info("ending activity of profile table " + obj);
			ActivityContextHandle ach = ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle((String) obj));
			ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach, false);
			ac.end();
		}
	}

	public void startAllProfileTableActivities() throws ActivityAlreadyExistsException {

		for (Object obj : getProfileTables()) {
			logger.info("starting activity for profile table " + obj);
			// create profile table activity context
			sleeContainer.getActivityContextFactory().createActivityContext(ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle((String) obj)));
		}

	}

}
