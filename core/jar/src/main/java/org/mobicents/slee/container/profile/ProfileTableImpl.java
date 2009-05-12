package org.mobicents.slee.container.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.ProfileTableUsageMBean;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileMBean;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.ReadOnlyProfileException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBeanImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.facilities.MNotificationSource;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Start time:11:20:19 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
public class ProfileTableImpl implements ProfileTableConcrete {

	public static final int _UNICODE_RANGE_START = 0x0020;
	public static final int _UNICODE_RANGE_END = 0x007e;
	public static final int _UNICODE_SLASH = 0x002f;

	private static Logger logger = Logger
			.getLogger(ProfileTableImpl.class);

	private final ProfileSpecificationComponent component;
	private final String profileTableName;
	private final SleeContainer sleeContainer;
	
	private MNotificationSource profileTableNotification = null;
	private ProfileTableUsageMBeanImpl profileTableUsageMBean = null;

	/**
	 * indicates if this table fires events
	 */
	private final boolean fireEvents;
	
	public ProfileTableImpl(String profileTableName, ProfileSpecificationComponent component, SleeContainer sleeContainer) {
		
		ProfileTableImpl.validateProfileTableName(profileTableName);
		if (sleeContainer == null || component == null) {
			throw new NullPointerException();
		}

		this.component = component;
		this.sleeContainer = sleeContainer;
		this.profileTableName = profileTableName;
		
		this.profileTableNotification = new MNotificationSource(
				new ProfileTableNotification(this.profileTableName));
		
		this.fireEvents = component.getDescriptor().getEventsEnabled();
	}

	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	public boolean doesFireEvents() {
		return fireEvents;
	}
	
	public void register() throws SLEEException {
		
		if (component.getUsageParametersInterface() != null) {
			// create resource usage mbean
			try {
				ObjectName objectName = this.getUsageMBeanName();
				if (this.profileTableUsageMBean != null) {
					// FIXME: is this valid? on restart we dont recrete those,
					// dont we ?
					this.profileTableUsageMBean = new ProfileTableUsageMBeanImpl(
							this.profileTableName, component, sleeContainer);
					this.profileTableUsageMBean.setObjectName(objectName);
					sleeContainer.getMBeanServer().registerMBean(
							this.profileTableUsageMBean, objectName);

					// create default usage param set
					this.profileTableUsageMBean.createUsageParameterSet();
				}
			} catch (Throwable t) {
				if (this.profileTableUsageMBean != null) {
					this.profileTableUsageMBean.remove();
				}

				try {
					this.removeProfileTable();
				} catch (Throwable t1) {
					logger.error(t1.getMessage(), t1);
				}

				throw new SLEEException(
						"Failed to create and register Table Usage MBean", t);
			}
		}
		sleeContainer
				.getActivityContextFactory()
				.createActivityContext(
						ActivityContextHandlerFactory
								.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
										profileTableName)));
	}

	public void deregister() {
		try {
			sleeContainer
					.getActivityContextFactory()
					.getActivityContext(
							ActivityContextHandlerFactory
									.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
											profileTableName)), false)
					.endActivity();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (this.profileTableUsageMBean != null) {
			this.profileTableUsageMBean.remove();
		}
	}

	public String getProfileTableName() {
		return this.profileTableName;
	}

	public ProfileSpecificationComponent getProfileSpecificationComponent() {
		return component;
	}

	public MNotificationSource getProfileTableNotification() {
		return this.profileTableNotification;
	}

	public ProfileTableUsageMBeanImpl getProfileTableUsageMBean() {
		return profileTableUsageMBean;
	}

	public void setProfileTableUsageMBean(
			ProfileTableUsageMBeanImpl profileTableUsageMBean) {
		this.profileTableUsageMBean = profileTableUsageMBean;
	}

	public ProfileTableActivity getActivity() {
		return new ProfileTableActivityImpl(new ProfileTableActivityHandle(this.profileTableName));
	}

	public Collection<ProfileID> getProfilesIDs() {
		return ProfileDataSource.INSTANCE.getProfilesIDs(this);
	}

	private void checkProfileSpecIsNotReadOnly() throws ReadOnlyProfileException {
		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException(component.toString());
		}
	}
	
	/**
	 * Create a new profile with the specified name in the profile table
	 * represented by this ProfileTable object. The ProfileLocalObject returned
	 * by this method may be safely typecast to the Profile Local Interface
	 * defined by the profile specification of the profile table. The invoking
	 * client may use the ProfileLocalObject to interact with the new profile in
	 * the same transaction context as it was created, for example, to set the
	 * values of any profile CMP fields prior to the profile creation being
	 * committed.
	 * 
	 * This method is a mandatory transactional method.
	 * 
	 * @param profileName
	 *            - the name of the new profile. The name must be unique within
	 *            the scope of the profile table.
	 * @return a Profile Local Interface object.
	 * @throws java.lang.NullPointerException
	 *             - if profileName is null.
	 * @throws java.lang.IllegalArgumentException
	 *             - if profileName is zero-length or contains illegal
	 *             characters.
	 * @throws TransactionRequiredLocalException
	 *             - if this method is invoked without a valid transaction
	 *             context.
	 * @throws ReadOnlyProfileException
	 *             - if the profile table's profile specification has enforced a
	 *             read-only SLEE component view of profiles.
	 * @throws ProfileAlreadyExistsException
	 *             - if a profile with the same name already exists in the
	 *             profile table.
	 * @throws CreateException
	 *             - if the Profile.profilePostCreate() method of the profile
	 *             object invoked to create the profile throws this exception,
	 *             it is propagated to the caller of this method.
	 * @throws SLEEException
	 *             - if the profile could not be created due to a system-level
	 *             failure.
	 */
	public ProfileLocalObject create(String profileName)
			throws NullPointerException, IllegalArgumentException,
			TransactionRequiredLocalException, ReadOnlyProfileException,
			ProfileAlreadyExistsException, CreateException, SLEEException {

		sleeContainer.getTransactionManager().mandateTransaction();

		checkProfileSpecIsNotReadOnly();
		return  createProfile(profileName).getProfileLocalObject();
	}

	public ProfileLocalObject find(String profileName)
			throws NullPointerException, TransactionRequiredLocalException,
			SLEEException {
		
		if (profileName == null) {
			throw new NullPointerException();
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		try {
			return getProfileLocalObject(profileName);
		} catch (UnrecognizedProfileNameException e) {
			return null;
		}
	}

	

	public Collection<?> findAll() throws TransactionRequiredLocalException,
			SLEEException {
		Collection<ProfileLocalObject> plos = new ArrayList<ProfileLocalObject>();

		List<String> profileNames = (List<String>) getProfileNames();

		for (String profileName : profileNames) {
			try {
				plos.add(getProfileLocalObject(profileName));
			} catch (UnrecognizedProfileNameException e) {
				// profile removed concurrently?
				logger.error(e.getMessage(),e);
			}
		}

		return plos;
	}

	public boolean remove(String profileName) throws NullPointerException,
			ReadOnlyProfileException, TransactionRequiredLocalException,
			SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName
					+ "]");
		}

		if (profileName == null) {
			throw new NullPointerException("Profile name must not be null");
		}

		checkProfileSpecIsNotReadOnly();
		
		return this.removeProfile(profileName);
	}

	private boolean removeProfile(String profileName)
			throws NullPointerException, ReadOnlyProfileException,
			TransactionRequiredLocalException, SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName
					+ "]");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();

		// TODO unregister any existent mbeans for this profile

		try {
			ProfileObject profileObject = borrowProfileObject();
			profileObject.profileActivate(profileName);
			profileObject.profileRemove();	
			return true;
		} catch (UnrecognizedProfileNameException e) {
			return false;
		}															
	}

	public static void validateProfileName(String profileName)
			throws IllegalArgumentException, NullPointerException {
		if (profileName == null) {
			throw new NullPointerException("ProfileName must not be null");
		}
		if (profileName.length() == 0) {
			throw new IllegalArgumentException(
					"Profile name must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}

		for (int i = 0; i < profileName.length(); i++) {
			Character c = profileName.charAt(i);
			if (!(Character.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START <= c && c <= _UNICODE_RANGE_END))) {
				throw new IllegalArgumentException(
						"Profile name contains illegal character, name: "
								+ profileName + ", at index: " + i);
			}
		}
	}

	public static void validateProfileTableName(String profileTableName)
			throws IllegalArgumentException, NullPointerException {
		if (profileTableName == null) {
			throw new NullPointerException("ProfileTableName must not be null");
		}
		if (profileTableName.length() == 0) {
			throw new IllegalArgumentException(
					"ProfileTableName must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}

		for (int i = 0; i < profileTableName.length(); i++) {
			Character c = profileTableName.charAt(i);
			if (!((Character.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START <= c && c <= _UNICODE_RANGE_END)) && c != _UNICODE_SLASH)) {
				throw new IllegalArgumentException(
						"ProfileTableName contains illegal character, name: "
								+ profileTableName + ", at index: " + i);
			}
		}
	}

	// ##################
	// # Helper methods #
	// ##################

	/**
	 * 
	 * Creates a JMX ObjectName for a profile, given its profile name and
	 * profile table name
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return
	 * @throws MalformedObjectNameException
	 */
	public static ObjectName generateProfileMBeanObjectName(
			String profileTableName, String profileName)
			throws MalformedObjectNameException {
		return new ObjectName(ProfileMBean.BASE_OBJECT_NAME + ','
				+ ProfileMBean.PROFILE_TABLE_NAME_KEY + '='
				+ ObjectName.quote(profileTableName) + ','
				+ ProfileMBean.PROFILE_NAME_KEY + '='
				+ ObjectName.quote(profileName != null ? profileName : "")
				+ ",uuid=" + ObjectName.quote(UUID.randomUUID().toString()));
	}

	public void createDefaultProfile() throws CreateException, ProfileVerificationException {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating default profile for table "+profileTableName);
		}
		// lets get an object
		ProfileObject profileObject = borrowProfileObject();
		// invoke lifecycle methods
		profileObject.profileCreate(null);
		profileObject.profileVerify();
		profileObject.profilePassivate();		
	}
	
	public ProfileObject createProfile(String newProfileName)
			throws TransactionRequiredLocalException, NullPointerException,
			SLEEException,
			ProfileAlreadyExistsException, CreateException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Adding profile with name " + newProfileName + " on table with name "
					+ newProfileName);
		}

		validateProfileName(newProfileName);

		// switch the context classloader to the component cl
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		ProfileObject allocated = null;
		
		try {
			
			Thread.currentThread().setContextClassLoader(
					component.getClassLoader());

			if (profileExists(newProfileName)) {
				throw new ProfileAlreadyExistsException("Profile with name '"
						+ newProfileName + "' already exists in table '"
						+ this.getProfileTableName() + "'");
			}
			
			/*
			 * FIXME afaik the default profile doesn't count, let it be till a test fails 
			if (component.getDescriptor().isSingleProfile()) {
				throw new SLEEException(
						"Profile Specification indicates that this is single profile, can not create more than one: "
								+ component);
			}
			*/
			
			allocated = borrowProfileObject();
			allocated.profileCreate(newProfileName);
						
			return allocated;
		} catch (IllegalArgumentException e) {
			throw new SLEEException(e.getMessage(), e);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	public ProfileLocalObject findProfileByAttribute(String attributeName,
			Object attributeValue) throws NullPointerException,
			IllegalArgumentException, TransactionRequiredLocalException,
			SLEEException {
		return findProfileByAttribute(attributeName, attributeValue);
	}

	public Collection findProfilesByAttribute(String attributeName,
			Object attributeValue) throws NullPointerException,
			IllegalArgumentException, TransactionRequiredLocalException,
			SLEEException {
		// FIXME: Alexandre: Implement findProfilesByAttribute(String, Object);
		return null;
	}

	public ProfileID getProfileByIndexedAttribute(String attributeName,
			Object attributeValue) throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException {
		// FIXME: Alexandre: This is only for 1.0. Need to do checks!
		return null;
	}

	public Collection<ProfileID> getProfilesByIndexedAttribute(
			String attributeName, Object attributeValue)
			throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException {
		// FIXME: Alexandre: This is only for 1.0. Need to do checks!
		return null;
	}

	public ProfileObject borrowProfileObject() {
		final ProfileObjectPool pool = sleeContainer.getProfileObjectPoolManagement().getObjectPool(profileTableName);
		return pool.borrowObject();		
	}
	
	/**
	 * Returns object into pooled state
	 */
	public void returnProfileObject(ProfileObject profileObject) {
		if (logger.isDebugEnabled()) {
			logger.debug("[returnProfileObject] on: " + profileObject + "]");
		}		
		ProfileObjectPool pool = sleeContainer.getProfileObjectPoolManagement().getObjectPool(profileTableName);
		if (profileObject.getState() == ProfileObjectState.POOLED) {
			pool.returnObject(profileObject);
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("profile object "+profileObject+" returned without pooled state, invalidating");
			}
			try {
				pool.invalidateObject(profileObject);
			} catch (Exception e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
	}

	public ObjectName getUsageMBeanName() throws IllegalArgumentException {
		try {
			return new ObjectName(ProfileTableUsageMBean.BASE_OBJECT_NAME + ','
					+ ProfileTableUsageMBean.PROFILE_TABLE_NAME_KEY + '='
					+ profileTableName);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Failed to create MBean name, due to some system level error.",
					e);

		}
	}

	public Collection<String> getProfileNames() {
		return ProfileDataSource.INSTANCE.findAllNames(this);
	}

	/**
	 * Determines if profile is in back end storage == visible to other
	 * compoenents than MBean, if null is passed as argumetn it must check for
	 * any other than defualt?
	 */
	public boolean profileExists(String profileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("[isProfileCommitted] on: " + this + " Profile["
					+ profileName + "]");
		}

		return ProfileDataSource.INSTANCE.find(this, profileName);
	}

	/**
	 * Triggers remove operation on this profile table.
	 * 
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void removeProfileTable() throws TransactionRequiredLocalException,
			SLEEException {

		if (logger.isDebugEnabled()) {
			logger.debug("[removeProfileTable] on: " + this);
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		
		boolean terminateTx = sleeTransactionManager.requireTransaction();
		boolean doRollback = true;

		try {

			if (logger.isDebugEnabled()) {
				logger.debug("removeProfileTable: removing profileTable="
						+ profileTableName);
			}

			// remove the table profiles
			for (String profileName : this.getProfileNames()) {
				this.removeProfile(profileName);
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
			deregister();
			sleeContainer.getSleeProfileTableManager().removeProfileTable(this);
			doRollback = false;

		} finally {
			sleeTransactionManager.requireTransactionEnd(terminateTx,
					doRollback);
		}

	}

	public ActivityContext getActivityContext() {
		return 
		sleeContainer
		.getActivityContextFactory()
		.getActivityContext(
				ActivityContextHandlerFactory
						.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
								profileTableName)), false);
	}
	
	private ProfileLocalObject getProfileLocalObject(
			String profileName) throws UnrecognizedProfileNameException {
		ProfileObject profileObject = borrowProfileObject();
		profileObject.profileActivate(profileName);
		return profileObject.getProfileLocalObject();		
	}

	public String toString() {
		return " ProfileTableImpl ( table = "
				+ this.profileTableName + " , component ="
				+ component + " )";
	}
	
}
