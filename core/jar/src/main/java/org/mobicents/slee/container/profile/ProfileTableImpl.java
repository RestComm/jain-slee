package org.mobicents.slee.container.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.Address;
import javax.slee.AddressPlan;
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
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBeanImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.facilities.MNotificationSource;
import org.mobicents.slee.runtime.facilities.profile.ProfileAddedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileRemovedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileUpdatedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * Start time:11:20:19 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This is wrapper class for defined profiles. Its counter part is Service class
 * which manages SbbEntities. Actual profile with its data is logical SbbEntity.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
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
	}

	public SleeContainer getSleeContainer() {
		return sleeContainer;
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
		return JPAUtils.INSTANCE.getProfilesIDs(this);
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

		validateProfileName(profileName);

		try {
			createProfile(profileName);
		} catch (ProfileVerificationException e) {
			// SLEE 1.0 exception in a 1.1 method
			throw new SLEEException(e.getMessage(),e);
		}
		ProfileLocalObjectImpl plo = new ProfileLocalObjectImpl(
					component.getProfileSpecificationID(), this.profileTableName,
					profileName, sleeContainer.getSleeProfileTableManager(), false);
			
		return plo;		
	}

	public ProfileLocalObject find(String profileName)
			throws NullPointerException, TransactionRequiredLocalException,
			SLEEException {
		return this.find(profileName, false);
	}

	/**
	 * 
	 * @param profielName
	 * @param allowNull
	 *            - this is used only for default profile.
	 * @return
	 * @throws NullPointerException
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileLocalObject find(String profileName, boolean allowNull)
			throws NullPointerException, TransactionRequiredLocalException,
			SLEEException {
		if (profileName != null
				&& (allowNull || (!allowNull && !profileName.equals("null")))
				&& JPAUtils.INSTANCE.find(this, profileName)) {
			return getProfileLocalObjectConcrete(profileName);
		}

		return null;
	}

	public Collection findAll() throws TransactionRequiredLocalException,
			SLEEException {
		Collection<ProfileLocalObject> plos = new ArrayList<ProfileLocalObject>();

		List<String> profileNames = JPAUtils.INSTANCE.findAll(this);

		for (String profileName : profileNames) {
			plos.add(getProfileLocalObjectConcrete(profileName));
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

		return this.removeProfile(profileName);
	}

	public boolean removeProfile(String profileName)
			throws NullPointerException, ReadOnlyProfileException,
			TransactionRequiredLocalException, SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName
					+ "]");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();

		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException(component.toString() + " is read only.");
		}

		// TODO unregister any existent mbeans for this profile
		
		// Fire the removed event only when the transaction commits
		final ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory = sleeContainer
		.getProfileTableActivityContextInterfaceFactory();
		if (profileTableActivityContextInterfaceFactory == null) {
			final String s = "got NULL ProfileTable ACI Factory";
			logger.error(s);
			throw new SLEEException(s);
		}
		ProfileObject allocated = assignAndActivateProfileObject(profileName);
		// This will call remove
		this.deassignProfileObject(allocated, true);

		// FIXME: Alexandre: Remove and Fetch profile for event
		ProfileLocalObjectConcrete ploc = null;

		// Profile Removed Event.
		// After a Profile is removed from a Profile Table, the SLEE fires
		// and delivers a Profile Removed Event on the Profile Table’s
		// Activity.
		// FIXME emmartins : ploc is null ?!?!?
		fireProfileRemovedEvent(ploc);

		return true;
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

	/**
	 * This method
	 * <ul>
	 * <li>creates profile if it does not exist
	 * <li>creates and registers MBean
	 * </ul>
	 * 
	 * For execution of those task it switches CL to profiel specs CL.
	 * 
	 * @throws CreateException
	 * @throws ProfileVerificationException
	 */
	public ProfileObject createProfile(String newProfileName)
			throws TransactionRequiredLocalException, NullPointerException,
			SLEEException,
			ProfileAlreadyExistsException, CreateException,
			ProfileVerificationException, ReadOnlyProfileException {
		if (logger.isDebugEnabled()) {
			logger.debug("[addProfile] on: " + this + " Profile["
					+ newProfileName + "]");
		}

		boolean isDefault = false;
		if (newProfileName == null) {
			isDefault = true;
			newProfileName = "";
		}

		// switch the context classloader to the component cl
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();

		ProfileObject allocated = null;
		boolean success = false;
		try {
			
			Thread.currentThread().setContextClassLoader(
					component.getClassLoader());

			if (profileExists(newProfileName)) {
				throw new ProfileAlreadyExistsException("Profile with name '"
						+ newProfileName + "' already exists in table '"
						+ this.getProfileTableName() + "'");
			}

			if (!isDefault) {
				if (component.getDescriptor().getReadOnly()) {
					throw new ReadOnlyProfileException(
							"Profile Specification declares profile to be read only.");
				}
				if (component.getDescriptor().isSingleProfile()
						&& profileExists(SleeProfileTableManager.DEFAULT_PROFILE_DB_NAME)) {
					throw new SLEEException(
							"Profile Specification indicates that this is single profile, can not create more than one: "
									+ component);
				}
			}
			
			allocated = this.assignProfileObject(newProfileName);

			if (isDefault) {
				allocated.profileInitialize();
			}
			
			if (component.isSlee11()) {
				allocated.profilePostCreate();
			}
			
			if (isDefault) {
				// TODO confirm this
				allocated.profileStore();
				// See section 10.13.1.10 of JSLEE 1.1
				if (!component.isSlee11()) {
					allocated.profileVerify();
				}
			} 
			
			success = true;
			return allocated;
		} catch (IllegalArgumentException e) {
			throw new SLEEException(e.getMessage(), e);
		} finally {
			if (!success && allocated != null) {
				try {
					this.deassignProfileObject(allocated, true);
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

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

	private ProfileObject assignProfileObject(String profileName) {

		if (logger.isDebugEnabled()) {
			logger.debug("[assignProfileObject] on: " + this + " Profile["
					+ profileName + "]");
		}

		ProfileObject profileObject = new ProfileObject(this,
				component.getProfileSpecificationID());
		ProfileContextImpl context = new ProfileContextImpl(this);
		profileObject.setProfileContext(context);
		profileObject.setProfileName(profileName);

		return profileObject;
	}

	public ProfileObject assignAndActivateProfileObject(String profileName) {

		if (logger.isDebugEnabled()) {
			logger.debug("[assignAndActivateProfileObject] on: " + this + " Profile["
					+ profileName + "]");
		}

		ProfileObject profileObject = assignProfileObject(profileName);
		profileObject.profileActivate();
		profileObject.profileLoad();

		return profileObject;
	}
	
	/**
	 * Returns object into pooled state
	 */
	public void deassignProfileObject(ProfileObject profileObject,
			boolean remove) {
		if (logger.isDebugEnabled()) {
			logger.debug("[deassignProfileObject] on: " + this
					+ " ProfileObject[" + profileObject + "]");
		}
		if (!remove) {
			profileObject.getProfileConcrete().profilePassivate();
		} else {
			profileObject.profileRemove();
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
		return JPAUtils.INSTANCE.findAll(this);
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

		return JPAUtils.INSTANCE.find(this, profileName);
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
				remove(profileName);
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

	public void fireProfileAddedEvent(
			ProfileLocalObjectConcrete profileLocaObject) throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[fireProfileAddedEvent][" + checkFireCondition()
					+ "] on: " + this + " ProfileLocalObject:"
					+ profileLocaObject);
		}
		if (!checkFireCondition()) {
			if (logger.isDebugEnabled()) {

				logger.debug("Events are not enabled for specification: "
						+ component
						+ ", not firinig ProfileAddedEvent on: "
						+ profileTableName + "/"
						+ profileLocaObject.getProfileName());
			}
			return;
		}
		ActivityContext ac = sleeContainer
				.getActivityContextFactory()
				.getActivityContext(
						ActivityContextHandlerFactory
								.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
										profileTableName)), false);

		Address address = getProfileaddress(profileLocaObject.getProfileName());

		ProfileID profileID = new ProfileID(address);

		ProfileAddedEventImpl profileAddedEvent = new ProfileAddedEventImpl(
				address, profileID, profileLocaObject, ac);

		ac.fireEvent(ProfileAddedEventImpl.EVENT_TYPE_ID, profileAddedEvent,
				address, null, 0);
	}

	public void fireProfileRemovedEvent(
			ProfileLocalObjectConcrete profileLocalObject) throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[fireProfileRemovedEvent][" + checkFireCondition()
					+ "] on: " + this + " ProfileLocalObject:"
					+ profileLocalObject);
		}
		if (!checkFireCondition()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Events are not enabled for specification: "
						+ component
						+ ", not firinig ProfileRemovedEvent on: "
						+ profileTableName + "/"
						+ profileLocalObject.getProfileName());
			}
			return;
		}

		ActivityContext ac = sleeContainer
				.getActivityContextFactory()
				.getActivityContext(
						ActivityContextHandlerFactory
								.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
										profileTableName)), false);

		Address address = getProfileaddress(profileLocalObject.getProfileName());
		ProfileID profileID = new ProfileID(address);

		ProfileRemovedEventImpl profileRemovedEventImpl = new ProfileRemovedEventImpl(
				address, profileID, profileLocalObject, ac);

		ac.fireEvent(ProfileRemovedEventImpl.EVENT_TYPE_ID,
				profileRemovedEventImpl, address, null, 0);
	}

	public void fireProfileUpdatedEvent(
			ProfileLocalObjectConcrete profileLocalObjectBeforeAction,
			ProfileLocalObjectConcrete profileLocalObjectAfterAction)
			throws SLEEException {

		if (logger.isDebugEnabled()) {
			logger.debug("[fireProfileUpdatedEvent][" + checkFireCondition()
					+ "] on: " + this + " ProfileLocalObject before:"
					+ profileLocalObjectBeforeAction
					+ ", ProfileLocalObject after:"
					+ profileLocalObjectAfterAction);
		}

		if (!checkFireCondition()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Events are not enabled for specification: "
						+ component
						+ ", not firinig ProfileUpdatedEvent on: "
						+ profileTableName + "/"
						+ profileLocalObjectBeforeAction.getProfileName());
			}
			return;
		}
		ActivityContext ac = sleeContainer
				.getActivityContextFactory()
				.getActivityContext(
						ActivityContextHandlerFactory
								.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
										profileTableName)), false);

		Address address = getProfileaddress(profileLocalObjectBeforeAction
				.getProfileName());

		ProfileID profileID = new ProfileID(address);

		ProfileUpdatedEventImpl profileUpdatedEventImpl = new ProfileUpdatedEventImpl(
				address, profileID, profileLocalObjectBeforeAction,
				profileLocalObjectAfterAction, ac);

		ac.fireEvent(ProfileUpdatedEventImpl.EVENT_TYPE_ID,
				profileUpdatedEventImpl, address, null, 0);
	}

	private Address getProfileaddress(String profileName) {
		return new Address(AddressPlan.SLEE_PROFILE, profileTableName + "/"
				+ profileName);
	}

	private ProfileLocalObjectConcrete getProfileLocalObjectConcrete(
			String profileName) {
		ProfileLocalObjectConcrete ploc = new ProfileLocalObjectImpl(
				component.getProfileSpecificationID(), this.profileTableName,
				profileName, sleeContainer.getSleeProfileTableManager(), false);
		return ploc;

	}

	private boolean checkFireCondition() {
		return component.getDescriptor().getEventsEnabled();

	}

	public String toString() {
		return " ProfileTableImpl ( table = "
				+ this.profileTableName + " , component ="
				+ component + " )";
	}
}
