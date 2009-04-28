package org.mobicents.slee.container.profile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
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
import javax.slee.profile.ProfileSpecificationID;
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
import org.mobicents.slee.runtime.facilities.profile.ProfileUpdatedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

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
public class ProfileTableConcreteImpl implements ProfileTableConcrete {

	public static final int _UNICODE_RANGE_START = 0x0020;
	public static final int _UNICODE_RANGE_END = 0x007e;
	public static final int _UNICODE_SLASH = 0x002f;

	private static Logger logger = Logger.getLogger(ProfileTableConcreteImpl.class);

  private ProfileSpecificationID profileSpecificationId;

  private String profileTableName = null;
	private MNotificationSource profileTableNotification = null;
	private ProfileTableUsageMBeanImpl profileTableUsageMBean = null;

  private SleeContainer sleeContainer = null;
  private SleeTransactionManager sleeTransactionManager = null;
  private SleeProfileTableManager sleeProfileManagement = null;

  // Closely related

	// private ActivityContext profileTableActivityContext = null;

	public ProfileTableConcreteImpl(SleeProfileTableManager sleeProfileManagement, String profileTableName, ProfileSpecificationID profileSpecificationId)
	{
		super();

		ProfileTableConcreteImpl.validateProfileTableName(profileTableName);
		if (sleeProfileManagement == null || profileSpecificationId == null) {
			throw new NullPointerException("Parameters must not be null");
		}

    this.profileSpecificationId = profileSpecificationId;

    this.profileTableName = profileTableName;
    this.profileTableNotification = new MNotificationSource(new ProfileTableNotification(this.profileTableName));

		this.sleeProfileManagement = sleeProfileManagement;
    this.sleeContainer = this.sleeProfileManagement.getSleeContainer();
    this.sleeTransactionManager = this.sleeContainer.getTransactionManager();
		// Akward, but we do that all the time.
	}

	public void register() throws SLEEException
	{
		ProfileSpecificationComponent component = this.sleeProfileManagement.getProfileSpecificationComponent(profileSpecificationId);
		
		if (component.getUsageParametersInterface() != null)
		{
			// create resource usage mbean
			try
			{
				ObjectName objectName = this.getUsageMBeanName();
				if (this.profileTableUsageMBean != null)
				{
					// FIXME: is this valid? on restart we dont recrete those, dont we ?
					this.profileTableUsageMBean = new ProfileTableUsageMBeanImpl(this.profileTableName, component, sleeContainer);
					this.profileTableUsageMBean.setObjectName(objectName);
					sleeContainer.getMBeanServer().registerMBean(this.profileTableUsageMBean, objectName);

					// create default usage param set
					this.profileTableUsageMBean.createUsageParameterSet();
				}
			}
			catch (Throwable t) {
				if (this.profileTableUsageMBean != null)
				{
					this.profileTableUsageMBean.remove();					
				}

				try {
					this.removeProfileTable();
				}
				catch (Throwable t1) {
					logger.error(t1.getMessage(), t1);
				}
				
				throw new SLEEException("Failed to create and register Table Usage MBean", t);
			}
		}
		sleeContainer.getActivityContextFactory().createActivityContext(ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)));
	}

	public void deregister()
	{
		try {
			sleeContainer.getActivityContextFactory()
					.getActivityContext(ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)), false).endActivity();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		if (this.profileTableUsageMBean != null)
		{
			this.profileTableUsageMBean.remove();			
		}
	}

	public SleeProfileTableManager getProfileManagement() {
		return sleeProfileManagement;
	}

	public String getProfileTableName() {
		return this.profileTableName;
	}

	public ProfileSpecificationComponent getProfileSpecificationComponent() {
		return this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
	}

	public MNotificationSource getProfileTableNotification() {
		return this.profileTableNotification;
	}

	public ProfileTableUsageMBeanImpl getProfileTableUsageMBean() {
		return profileTableUsageMBean;
	}

	public void setProfileTableUsageMBean(ProfileTableUsageMBeanImpl profileTableUsageMBean) {
		this.profileTableUsageMBean = profileTableUsageMBean;
	}

	public ProfileTableActivity getActivity() {
		return (ProfileTableActivity) sleeContainer.getActivityContextFactory().getActivityContext(
				ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)), false).getActivityContextHandle().getActivity();
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
	public ProfileLocalObject create(String profileName) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, ReadOnlyProfileException, ProfileAlreadyExistsException, CreateException, SLEEException
	{
		// Its transactional method
	  sleeTransactionManager.mandateTransaction();

		validateProfileName(profileName);

		// FIXME Alexandre: Check if profile already exists
		boolean doRollBack = true;
		try
		{
			addProfile(profileName, false);
			ProfileObject po = this.assignProfileObject(profileName, false);
			this.deassignProfileObject(po, false);
			ProfileLocalObjectConcreteImpl plo = new ProfileLocalObjectConcreteImpl(this.profileSpecificationId, this.profileTableName, profileName, this.sleeProfileManagement, false);
			doRollBack = false;
			return plo;
		}
		catch (UnrecognizedProfileNameException e) {
			// This wont happen
			throw new SLEEException("Report a bug, please", e);
		}
		catch (SingleProfileException e) {
			throw new SLEEException("Failed to add new profile.", e);
		}
		catch (ProfileVerificationException e) {
			throw new SLEEException("Failed to add new profile.", e);
		}
		catch (ReadOnlyProfileException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SLEEException("Failed to add new profile.", e);
		}
		finally {
		  if(doRollBack)
		  {
	      // FIXME: Alexandre: determine if we should rollback here.
		  }
		}
	}

	public ProfileLocalObject find(String profileName) throws NullPointerException, TransactionRequiredLocalException, SLEEException {
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
	public ProfileLocalObject find(String profileName, boolean allowNull) throws NullPointerException, TransactionRequiredLocalException, SLEEException
	{
	  if( profileName != null && (allowNull || (!allowNull && !profileName.equals("null"))) && JPAUtils.INSTANCE.find(this, profileName))
	  {
	    return getProfileLocalObjectConcrete(profileName);
	  }
	  
	  return null;
	}

	public Collection findAll() throws TransactionRequiredLocalException, SLEEException
	{
	  Collection<ProfileLocalObject> plos = new ArrayList<ProfileLocalObject>();
	  
		List<String> profileNames = JPAUtils.INSTANCE.findAll(this);
		
		for(String profileName : profileNames)
		{
		  plos.add( getProfileLocalObjectConcrete(profileName) );
		}
		
		return plos;
	}

	public boolean remove(String profileName) throws NullPointerException, ReadOnlyProfileException, TransactionRequiredLocalException, SLEEException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName + "]");
		}
		
		return this.remove(profileName, false);
	}

	public boolean remove(String profileName, boolean isDefault) throws NullPointerException, ReadOnlyProfileException, TransactionRequiredLocalException, SLEEException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName + "] isDefault[" + isDefault + "]");
		}

		if (profileName == null && !isDefault) {
			throw new NullPointerException("Profile name must not be null");
		}

		sleeTransactionManager.mandateTransaction();
		boolean doRollBack = true;

		ProfileSpecificationComponent component = this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
		if (component == null) {
			throw new SLEEException("No defined profile specification.");
		}

		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException("Profile specification: " + this.profileSpecificationId + ", is read only.");
		}
		ObjectName objectName = null;
		try {
			objectName = getProfileMBean(profileName, isDefault);
		} catch (UnrecognizedProfileNameException e) {
			throw new SLEEException("Failed to system level failure, please report, should not happen.", e);
		}
		
		try {
			// Fire the removed event only when the transaction commits
			final ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory = sleeContainer.getProfileTableActivityContextInterfaceFactory();
			if (profileTableActivityContextInterfaceFactory == null) {
				final String s = "got NULL ProfileTable ACI Factory";
				logger.error(s);
				throw new SystemException(s);
			}
			ProfileObject allocated = assignProfileObject(profileName, false);
			// This will call remove
			this.deassignProfileObject(allocated, true);

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

      // FIXME: Alexandre: Remove and Fetch profile for event 
			ProfileLocalObjectConcrete ploc = null;

      // Profile Removed Event.
      // After a Profile is removed from a Profile Table, the SLEE fires and delivers a Profile Removed Event on the Profile Table’s Activity.
			fireProfileRemovedEvent( ploc );

			doRollBack = false;
		}
		catch (Exception e) {
			String err = "Failed removeProfile " + profileName + " from profile table " + profileTableName + " because of " + e.getMessage();
			logger.error(err, e);

			throw new SLEEException(err);
		}
		finally
		{
			// FIXME?
			if (doRollBack) {
				try {
				  sleeTransactionManager.setRollbackOnly();
				}
				catch (IllegalStateException ise) {
		      logger.error("", ise);
				}
				catch (SystemException se) {
          logger.error("", se);
				}
			}
		}
		
		return false;
	}

	public static void validateProfileName(String profileName) throws IllegalArgumentException, NullPointerException
	{
		if (profileName == null) {
			throw new NullPointerException("ProfileName must not be null");
		}
		if (profileName.length() == 0) {
			throw new IllegalArgumentException("Profile name must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}

		for (int i = 0; i < profileName.length(); i++)
		{
			Character c = profileName.charAt(i);
			if (! (Character.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START <= c && c <= _UNICODE_RANGE_END)) )
			{
				throw new IllegalArgumentException("Profile name contains illegal character, name: " + profileName + ", at index: " + i);
			}
		}
	}

	public static void validateProfileTableName(String profileTableName) throws IllegalArgumentException, NullPointerException
	{
		if (profileTableName == null) {
			throw new NullPointerException("ProfileTableName must not be null");
		}
		if (profileTableName.length() == 0) {
			throw new IllegalArgumentException("ProfileTableName must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}

		for (int i = 0; i < profileTableName.length(); i++)
		{
			Character c = profileTableName.charAt(i);
			if (! ((Character.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START <= c && c <= _UNICODE_RANGE_END)) && c != _UNICODE_SLASH) )
			{
				throw new IllegalArgumentException("ProfileTableName contains illegal character, name: " + profileTableName + ", at index: " + i);
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
	public static ObjectName getProfileObjectName(String profileTableName, String profileName) throws MalformedObjectNameException
	{
		ObjectName objectName;
		String jmxProfileTableObjectName = toValidJmxName(profileTableName);
		String jmxProfileObjectName = toValidJmxName(profileName);
		objectName = new ObjectName("slee:" + "profileTableName=" + jmxProfileTableObjectName + "," + "type=profile," + "profile=" + jmxProfileObjectName);
		return objectName;
	}

	public static ObjectName getDefaultProfileObjectName(String profileTableName) throws MalformedObjectNameException {
		return getProfileObjectName(profileTableName, "");
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
		// FIXME causing problems to invoke operations on Mbean through jboss web console
		// the quotes are causing html problems if removed some tests are not passing
		jmxObjectName = ObjectName.quote(jmxObjectName);
		return jmxObjectName;

	}

	// FIXME: Why do we need this method and now use simply replaceAll ?
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
	public ObjectName addProfile(String newProfileName, boolean isDefault) throws TransactionRequiredLocalException, NullPointerException, SingleProfileException, SLEEException,
			ProfileAlreadyExistsException, CreateException, ProfileVerificationException, ReadOnlyProfileException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[addProfile] on: " + this + " Profile[" + newProfileName + "] isDefault[" + isDefault + "]");
		}
		
		// switch the context classloader to the component cl
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		
		// FIXME: Should we handle rollbacks here as well?
		sleeTransactionManager.mandateTransaction();
		ProfileObject allocated = null;
		boolean success = false;
		try
		{
			ProfileSpecificationComponent component = this.sleeContainer.getComponentRepositoryImpl().getComponentByID(this.profileSpecificationId);
			if (component == null) {
				throw new SLEEException("No such component for: " + this.profileSpecificationId + ", possibly we are beeing removed.");
			}
			Thread.currentThread().setContextClassLoader(component.getClassLoader());

      // FIXME: Alexandre: [DONE] add check for existency of profile
			if(JPAUtils.INSTANCE.find(this, newProfileName))
			{
			  throw new ProfileAlreadyExistsException("Profile with name '" + newProfileName + "' already exists in table '" + this.getProfileTableName() + "'");
			}
			
			allocated = this.assignProfileObject(newProfileName, true);

			if (isDefault)
			{
				allocated.profileInitialize();
				allocated.profileStore();
				// See section 10.13.1.10 of JSLEE 1.1
				if (!component.isSlee11())
				{
					allocated.profileVerify();
				}
			}
			else
			{
				if (component.getDescriptor().getReadOnly())
				{
					throw new ReadOnlyProfileException("Profile Specification declares profile to be read only.");
				}
				
				// FIXME: Alexandre: Copy the attribute values from the default profile
				
				if (component.getDescriptor().isSingleProfile() && isProfileCommitted(null))
				{
					this.deassignProfileObject(allocated, false);
					throw new SingleProfileException("Profile Specification indicates that this is single profile, can not create more than one: " + profileSpecificationId);
				}
				
				if (component.isSlee11())
				{
					allocated.profilePostCreate();
				}
			}

			// Add MBean
			Class<?> concreteProfileMBeanClass = component.getProfileMBeanConcreteImplClass();
			Constructor<?> con = concreteProfileMBeanClass.getConstructor(Class.class, ProfileObject.class);
			Object profileMBean = con.newInstance(component.getProfileMBeanConcreteInterfaceClass(), allocated);
			final ObjectName objectName = isDefault ? getDefaultProfileObjectName(profileTableName) : getProfileObjectName(profileTableName, newProfileName);
			
			if (logger.isDebugEnabled())
				logger.debug("Registering ProfileMBean with object name " + objectName);

			sleeContainer.getMBeanServer().registerMBean(profileMBean, objectName);
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						logger.info("Unregistring profile mbean "+objectName+" due to tx rollback");
						sleeContainer.getMBeanServer().unregisterMBean(objectName);
					} catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}					
				}
			};
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
			
			success = true;
			return objectName;
		}
		catch (InstanceAlreadyExistsException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (MBeanRegistrationException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (NotCompliantMBeanException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (MalformedObjectNameException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (IllegalArgumentException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (InstantiationException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (IllegalAccessException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (InvocationTargetException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (SecurityException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (NoSuchMethodException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		catch (UnrecognizedProfileNameException e) {
			throw new SLEEException(e.getMessage(),e);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally
		{
			if (!success && allocated != null)
			{
				try {
					this.deassignProfileObject(allocated, true);
				}
				catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			}
			
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	public ProfileLocalObject findProfileByAttribute(String attributeName, Object attributeValue) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, SLEEException {
		return findProfileByAttribute(attributeName, attributeValue);
	}

	public Collection findProfilesByAttribute(String attributeName, Object attributeValue) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, SLEEException {
    // FIXME: Alexandre: Implement findProfilesByAttribute(String, Object);
		return null;
	}

	public ProfileID getProfileByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException, SLEEException {
    // FIXME: Alexandre: This is only for 1.0. Need to do checks!
		return null;
	}

	public Collection<ProfileID> getProfilesByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException, SLEEException {
    // FIXME: Alexandre: This is only for 1.0. Need to do checks!
		return null;
	}

	public ProfileObject assignProfileObject(String profileName, boolean create) throws UnrecognizedProfileNameException, ProfileAlreadyExistsException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[assignProfileObject] on: " + this + " Profile[" + profileName + "] Create[" + create + "]");
		}
		
		ProfileObject profileObject = null;
		profileObject = new ProfileObject(this, this.profileSpecificationId);
		ProfileContextImpl context = new ProfileContextImpl(this);
		profileObject.setProfileContext(context);
		profileObject.setState(ProfileObjectState.POOLED);
		profileObject.setProfileName(profileName);
		
		if (create)
		{
			//if (getProfileMBean(profileName, profileName == null) != null)
			//{
			//	throw new ProfileAlreadyExistsException("Profile with name: " + profileName + ", already exists in profile table: " + this.profileTableName + ", its not commited yet.");
			//}
			// see if the profile MBean was closed/removed, but the profile itself is still visible in SLEE
			if (isProfileCommitted(profileName)) {
				throw new ProfileAlreadyExistsException("Profile with name: " + profileName + ", already exists in profile table: " + this.profileTableName);
			}
			// FIXME: Alex is there anything else to do?
		}
		else
		{
			// FIXME: Alex is there anything else to do?
			// FIXME: do we care about MBean?
			if (!isProfileCommitted(profileName)) {
				throw new UnrecognizedProfileNameException("Profile with name: " + profileName + ", does not exist in profile table(not commited): " + this.profileTableName);
			}
			profileObject.profileActivate();
			profileObject.setState(ProfileObjectState.READY);
			profileObject.profileLoad();
		}

		return profileObject;
	}

	/**
	 * Returns object into pooled state
	 */
	public void deassignProfileObject(ProfileObject profileObject, boolean removed) {
		if (logger.isDebugEnabled()) {
			logger.debug("[deassignProfileObject] on: " + this + " ProfileObject[" + profileObject + "]");
		}
		// FIXME: add pool :)
		if (!removed) {
			profileObject.getProfileConcrete().profilePassivate();
		} else {
			profileObject.profileRemove();
		}
		profileObject.setState(ProfileObjectState.POOLED);
	}

	public ObjectName getProfileMBean(String profileName, boolean isDefault) throws UnrecognizedProfileNameException {
		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileMBean] on: " + this);
		}
		// FIXME: Alex add check here?
		if (!isProfileCommitted(isDefault ? "" : profileName)) {
			throw new UnrecognizedProfileNameException("Profile: " + profileName + ", does not exist in profile table: " + profileTableName);

		}
		try {
			if (isDefault) {

				return getDefaultProfileObjectName(profileTableName);

			} else {
				return getProfileObjectName(profileTableName, profileName);
			}

		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public ObjectName getUsageMBeanName() throws IllegalArgumentException {
		try {
			return new ObjectName(ProfileTableUsageMBean.BASE_OBJECT_NAME + ',' + ProfileTableUsageMBean.PROFILE_TABLE_NAME_KEY + '=' + profileTableName);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to create MBean name, due to some system level error.", e);

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
	public boolean isProfileCommitted(String profileName)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[isProfileCommitted] on: " + this + " Profile[" + profileName + "]");
		}
		
		return JPAUtils.INSTANCE.find( this, profileName );
	}

	/**
	 * Triggers remove operation on this profile table.
	 * 
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void removeProfileTable() throws TransactionRequiredLocalException, SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("[removeProfileTable] on: " + this);
		}
		
		boolean terminateTx = sleeTransactionManager.requireTransaction();
		boolean doRollback = true;
		
		try {

			if (logger.isDebugEnabled()) {
				logger.debug("removeProfileTable: removing profileTable=" + profileTableName);
			}

			// remove default profile
			//this.remove(null, true);
			// unregistering the default MBean profile from the mbean server
			ObjectName defaultProfileObjectName = getDefaultProfileObjectName(profileTableName);

			if (sleeContainer.getMBeanServer().isRegistered(defaultProfileObjectName)) {
				logger.info("Unregistering following " + "profile table MBean with object name " + defaultProfileObjectName);
				sleeContainer.getMBeanServer().unregisterMBean(defaultProfileObjectName);
			}

			// remove the table profiles
			for (String profileName : this.getProfileNames()) {
				// unregistering the MBean profile from the server
				ObjectName objectName = getProfileObjectName(profileTableName, profileName);
				if (sleeContainer.getMBeanServer().isRegistered(objectName)) {
					logger.info("Unregistering " + "following profile MBean with object name " + objectName);
					sleeContainer.getMBeanServer().unregisterMBean(objectName);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("removeProfileTable: " + objectName + " is not registered! ");
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
			deregister();
			this.sleeProfileManagement.removeProfileTable(this);
			doRollback = false;
			
		} catch (MalformedObjectNameException e) {
			throw new SLEEException("Failed to remove profile table", e);
		} catch (InstanceNotFoundException e) {
			throw new SLEEException("Failed to remove profile table", e);
		} catch (MBeanRegistrationException e) {
			throw new SLEEException("Failed to remove profile table", e);
		} finally {
			sleeTransactionManager.requireTransactionEnd(terminateTx, doRollback);
		}

	}
	
	public Object getSbbCMPProfile(String profileName) throws SLEEException, UnrecognizedProfileNameException {
		if (logger.isDebugEnabled()) {
			logger.debug("[getSbbCMPProfile] on: " + this + " Profile[" + profileName + "]");
		}
		// FIXME: add check for name?
		try {
			ProfileObject po = assignProfileObject(profileName, false);
			// FIXME: is this ok ?
			// FIXME: Alex ?
			return po.getProfileConcrete();
		} catch (ProfileAlreadyExistsException e) {

			throw new SLEEException("Report this, its a bug?", e);
		}
	}

	public void fireProfileAddedEvent(ProfileLocalObjectConcrete profileLocaObject) throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[fireProfileAddedEvent][" + checkFireCondition() + "] on: " + this + " ProfileLocalObject:" + profileLocaObject);
		}
		if (!checkFireCondition()) {
			if (logger.isDebugEnabled()) {

				logger.debug("Events are not enabled for specification: " + this.profileSpecificationId + ", not firinig ProfileAddedEvent on: " + profileTableName + "/"
						+ profileLocaObject.getProfileName());
			}
			return;
		}
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(
				ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)), false);

		Address address = getProfileaddress(profileLocaObject.getProfileName());

		ProfileID profileID = new ProfileID(address);

		ProfileAddedEventImpl profileAddedEvent = new ProfileAddedEventImpl(address, profileID, profileLocaObject, ac);

		ac.fireEvent(ProfileAddedEventImpl.EVENT_TYPE_ID, profileAddedEvent, address, null, 0);
	}

	public void fireProfileRemovedEvent(ProfileLocalObjectConcrete profileLocalObject) throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[fireProfileRemovedEvent][" + checkFireCondition() + "] on: " + this + " ProfileLocalObject:" + profileLocalObject);
		}
		if (!checkFireCondition()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Events are not enabled for specification: " + this.profileSpecificationId + ", not firinig ProfileRemovedEvent on: " + profileTableName + "/"
						+ profileLocalObject.getProfileName());
			}
			return;
		}

		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(
				ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)), false);

		Address address = getProfileaddress(profileLocalObject.getProfileName());
		ProfileID profileID = new ProfileID(address);

		ProfileRemovedEventImpl profileRemovedEventImpl = new ProfileRemovedEventImpl(address, profileID, profileLocalObject, ac);

		ac.fireEvent(ProfileRemovedEventImpl.EVENT_TYPE_ID, profileRemovedEventImpl, address, null, 0);
	}

	public void fireProfileUpdatedEvent(ProfileLocalObjectConcrete profileLocalObjectBeforeAction, ProfileLocalObjectConcrete profileLocalObjectAfterAction) throws SLEEException {

		if (logger.isDebugEnabled()) {
			logger.debug("[fireProfileUpdatedEvent][" + checkFireCondition() + "] on: " + this + " ProfileLocalObject before:" + profileLocalObjectBeforeAction + ", ProfileLocalObject after:"
					+ profileLocalObjectAfterAction);
		}

		if (!checkFireCondition()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Events are not enabled for specification: " + this.profileSpecificationId + ", not firinig ProfileUpdatedEvent on: " + profileTableName + "/"
						+ profileLocalObjectBeforeAction.getProfileName());
			}
			return;
		}
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(
				ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)), false);

		Address address = getProfileaddress(profileLocalObjectBeforeAction.getProfileName());

		ProfileID profileID = new ProfileID(address);

		ProfileUpdatedEventImpl profileUpdatedEventImpl = new ProfileUpdatedEventImpl(address, profileID, profileLocalObjectBeforeAction, profileLocalObjectAfterAction, ac);

		ac.fireEvent(ProfileUpdatedEventImpl.EVENT_TYPE_ID, profileUpdatedEventImpl, address, null, 0);
	}

	private Address getProfileaddress(String profileName) {
		return new Address(AddressPlan.SLEE_PROFILE, profileTableName + "/" + profileName);
	}

	private ProfileLocalObjectConcrete getProfileLocalObjectConcrete(String profileName) {
		ProfileLocalObjectConcrete ploc = new ProfileLocalObjectConcreteImpl(this.profileSpecificationId, this.profileTableName, profileName, this.sleeProfileManagement, false);
		return ploc;

	}

	private boolean checkFireCondition() {
		return this.sleeProfileManagement.getProfileSpecificationComponent(profileSpecificationId).getDescriptor().getEventsEnabled();

	}

	public String toString() {
		return this.getClass().getSimpleName() + " Table[" + this.profileTableName + "] Specification[" + this.profileSpecificationId + "]";
	}

	public void activityEnded()
	{
	  logger.debug( "activityEnded called for Profile Table [" + this.getProfileTableName() + "]" );
	   //FIXME emmartins: the activity may be restarted on server stop/start, and profile table not removed  this.sleeProfileManagement.removeProfileTable(this);
	}
}
