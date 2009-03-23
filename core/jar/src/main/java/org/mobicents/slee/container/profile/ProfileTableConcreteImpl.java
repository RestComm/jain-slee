package org.mobicents.slee.container.profile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ReadOnlyProfileException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.SleeProfileManager;
import org.mobicents.slee.container.management.jmx.ProfileProvisioningMBeanImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityType;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * Start time:11:20:19 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * This is wrapper class for defined profiles. Its counter part is Service class
 * which manages SbbEntities. Actual profile with its data is logical SbbEntity.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileTableConcreteImpl implements ProfileTableConcrete {

	public static final int _UNICODE_RANGE_START = 0x0020;
	public static final int _UNICODE_RANGE_END = 0x007e;
	public static final int _UNICODE_SLASH = 0x002f;

	private static Logger logger = Logger.getLogger(ProfileTableConcreteImpl.class);

	private SleeProfileManagement sleeProfileManagement = null;
	private String profileTableName = null;
	private ProfileTableNotification profileTableNotification = null;
	private SleeContainer sleeContainer = null;
	private ProfileSpecificationID profileSpecificationId;

	// Closely related

	private ActivityContext profileTableActivityContext = null;

	public ProfileTableConcreteImpl(SleeProfileManagement sleeProfileManagement, String profileTableName, ProfileSpecificationID profileSpecificationId) {
		super();

		if (sleeProfileManagement == null || profileTableName == null || profileSpecificationId == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		this.sleeProfileManagement = sleeProfileManagement;
		this.profileTableName = profileTableName;
		this.profileTableNotification = new ProfileTableNotification(this.profileTableName);
		this.sleeContainer = this.sleeProfileManagement.getSleeContainer();
		this.profileSpecificationId = profileSpecificationId;

	}

	public void register() {
		this.profileTableActivityContext = sleeContainer.getActivityContextFactory().createActivityContext(
				ActivityContextHandlerFactory.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(profileTableName)));
	}

	public void deregister() {
		this.profileTableActivityContext.end();
	}

	public SleeProfileManagement getProfileManagement() {

		return sleeProfileManagement;
	}

	public String getProfileTableName() {
		return this.profileTableName;
	}

	public ProfileSpecificationComponent getProfileSpecificationComponent() {
		return this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
	}

	public ProfileTableNotification getProfileTableNotification() {
		return this.profileTableNotification;
	}

	public ProfileTableActivity getActivity() {
		return (ProfileTableActivity) this.profileTableActivityContext.getActivityContextHandle().getActivity();
	}

	public Collection<ProfileID> getProfilesIDs() {
		return null;
	}

	public ProfileLocalObject create(String profileName) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, ReadOnlyProfileException,
			ProfileAlreadyExistsException, CreateException, SLEEException {

		// Its transactional method
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();

		validateProfileName(profileName);

		// FIXME add check for profile existency, this can be done only when we
		// have JPA ?
		ProfileSpecificationComponent component = this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
		if (component == null) {
			throw new SLEEException("Could not find profile specification " + this.profileSpecificationId + " for profile table: " + this.profileTableName);
		}
		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException("Profile Specification declares profile to be read only.");
		}

		// Pool p = sleeProfileManagement.getPool(this.profileTableName);
		// ProfileObjec po = p.borrowObject();

		ProfileObject po = new ProfileObject(this, profileSpecificationId);
		// FIXME: those should be done by pool;
		po.setProfileName(profileName);
		po.setProfileContext(new ProfileContextImpl(this));
		po.setState(ProfileObjectState.POOLED);

		po.profilePostCreate();
		po.setState(ProfileObjectState.READY);
		// FIXME: add action to transaction to passivate this object?
		// FIXME: add ProfileLocalObject, somehow

		return null;
	}

	public ProfileLocalObject find(String profielName) throws NullPointerException, TransactionRequiredLocalException, SLEEException {
		return this.find(profielName, false);
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
	public ProfileLocalObject find(String profielName, boolean allowNull) throws NullPointerException, TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection findAll() throws TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(String profileName) throws NullPointerException, ReadOnlyProfileException, TransactionRequiredLocalException, SLEEException {
		if (profileName == null) {
			throw new NullPointerException("Profile name must not be null");
		}

		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();

		ProfileSpecificationComponent component = this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
		if (component == null) {
			throw new SLEEException("No defined profiel specification.");
		}

		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException("Profile specification: " + this.profileSpecificationId + ", is read only.");
		}

		// FIXME: add this
		// if(profileExists)
		{
			// ProfileObject po = new
			// ProfileObject(this,profileSpecificationId);
			// //FIXME: those should be done by pool;
			// po.setProfileName(profileName);
			// po.setProfileContext(new ProfileContextImpl(this));
			// po.setState(ProfileObjectState.POOLED);
			//			
			// po.profileLoad();
			// po.setState(ProfileObjectState.READY);
			// po.profileRemove();
			// po.setState(ProfileObjectState.POOLED);
		}

		return false;
	}

	public static void validateProfileName(String profileName) throws IllegalArgumentException, NullPointerException {
		if (profileName == null) {
			throw new NullPointerException("ProfileName must not be null");
		}
		if (profileName.length() == 0) {
			throw new IllegalArgumentException("Profile name must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}

		for (int i = 0; i < profileName.length(); i++) {
			Character c = profileName.charAt(i);
			int unicodeCode = c.charValue();
			if (c.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START <= c && c <= _UNICODE_RANGE_END)) {
				// fine
			} else {
				throw new IllegalArgumentException("Profile name contains illegal character, name: " + profileName + ", at index: " + i);
			}
		}

	}

	public static void validateProfileTableName(String profileTableName) throws IllegalArgumentException, NullPointerException {
		if (profileTableName == null) {
			throw new NullPointerException("ProfileTableName must not be null");
		}
		if (profileTableName.length() == 0) {
			throw new IllegalArgumentException("ProfileTableName must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}

		for (int i = 0; i < profileTableName.length(); i++) {
			Character c = profileTableName.charAt(i);
			int unicodeCode = c.charValue();
			if ((c.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START <= c && c <= _UNICODE_RANGE_END)) && unicodeCode != _UNICODE_SLASH) {
				// fine
			} else {
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
	public static ObjectName getProfileObjectName(String profileTableName, String profileName) throws MalformedObjectNameException {
		ObjectName objectName;
		String jmxProfileTableObjectName = toValidJmxName(profileTableName);
		String jmxProfileObjectName = toValidJmxName(profileName);
		objectName = new ObjectName("slee:" + "profileTableName=" + jmxProfileTableObjectName + "," + "type=profile," + "profile=" + jmxProfileObjectName);
		return objectName;
	}

	public static ObjectName getDefaultProfileObjectName(String profileTableName) throws MalformedObjectNameException {
		return getProfileObjectName(profileTableName, "defaultProfile");
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

	/**
	 * This method
	 * <ul>
	 * <li>creates profile if it does not exist
	 * <li>creates and registers MBean
	 * </ul>
	 * 
	 * For execution of those task it switches CL to profiel specs CL.
	 */
	public ObjectName addProfile(String newProfileName, boolean isDefault) throws TransactionRequiredLocalException, NullPointerException, SingleProfileException, InvalidArgumentException,
			SLEEException, ProfileAlreadyExistsException {

		// switch the context classloader to the component cl
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		ObjectName objectName = null;
		this.sleeContainer.getTransactionManager().mandateTransaction();
		try {
			ProfileSpecificationComponent component = this.sleeContainer.getComponentRepositoryImpl().getComponentByID(this.profileSpecificationId);
			if (component == null) {
				throw new SLEEException("No such component for: " + this.profileSpecificationId + ", possibly we are beeing removed.");
			}
			Thread.currentThread().setContextClassLoader(component.getClassLoader());

			// FIXME: add check for existncy of profile or remove exception from
			// throws clause

			ProfileObject allocated = this.assignProfileObject(newProfileName, true);

			if (isDefault) {
				allocated.profileInitialize();
				allocated.profileStore();
				if (!component.isSlee11())
					allocated.profileVerify();

			} else {
				// FIXME: copy the attribute values from the default profile

				if (component.getDescriptor().isSingleProfile() && isProfileCommitted(null)) {
					this.deassignProfileObject(allocated);
					throw new SingleProfileException("Profile Specification indicates that this is single profile, can not create more than one: " + profileSpecificationId);
				}
				if (component.isSlee11())
					allocated.profilePostCreate();
			}

			// Add MBean
			Class concreteProfileMBeanClass = component.getProfileMBeanConcreteImplClass();
			Constructor con = concreteProfileMBeanClass.getConstructor(ProfileObject.class);
			// FIXME: this must be StandardMBean ?
			ServiceMBeanSupport profileMBean = (ServiceMBeanSupport) con.newInstance(allocated);
			if (isDefault) {
				objectName = getDefaultProfileObjectName(profileTableName);
			} else {
				objectName = getProfileObjectName(profileTableName, newProfileName);
			}

			if (logger.isDebugEnabled())
				logger.debug("[instantiateProfile]Registering " + "following profile MBean with object name " + objectName);
			sleeContainer.getMBeanServer().registerMBean(profileMBean, objectName);

		} catch (InstanceAlreadyExistsException e) {

			throw new SLEEException("ProfileMBean with name: " + objectName + " already registered.", e);
		} catch (MBeanRegistrationException e) {

			throw new SLEEException("ProfileMBean with name: " + objectName + " already registered.", e);
		} catch (NotCompliantMBeanException e) {
			throw new SLEEException("ProfileMBean with name: " + objectName + " generated class is not correct.", e);
		} catch (MalformedObjectNameException e) {
			throw new SLEEException("ProfileMBean object name: " + objectName + " can not be created, its bad.", e);
		} catch (IllegalArgumentException e) {
			throw new SLEEException("Failed to create ProfileMBean, object name: " + objectName + ".", e);
		} catch (InstantiationException e) {
			throw new SLEEException("Failed to create ProfileMBean, object name: " + objectName + ".", e);
		} catch (IllegalAccessException e) {
			throw new SLEEException("Failed to create ProfileMBean, object name: " + objectName + ".", e);
		} catch (InvocationTargetException e) {
			throw new SLEEException("Failed to create ProfileMBean, object name: " + objectName + ".", e);
		} catch (SecurityException e) {
			throw new SLEEException("Failed to create ProfileMBean, object name: " + objectName + ".", e);
		} catch (NoSuchMethodException e) {
			throw new SLEEException("Failed to create ProfileMBean, object name: " + objectName + ".", e);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

		return null;
	}

	public ProfileLocalObject findProfileByAttribute(String arg0, Object arg1) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection findProfilesByAttribute(String arg0, Object arg1) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public ProfileID getProfileByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException {

		// THis can be invoked only on 1.0 profiles
		return null;
	}

	public Collection<ProfileID> getProfilesByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException, SLEEException {
		// THis can be invoked only on 1.0 profiles
		return null;
	}

	public ProfileObject assignProfileObject(String profileName, boolean create) throws UnrecognizedProfileNameException, ProfileAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns object into pooled state
	 */
	public void deassignProfileObject(ProfileObject profileObject) {

		// FIXME: add pool :)

		profileObject.getProfileConcrete().profilePassivate();
		profileObject.setState(ProfileObjectState.POOLED);
	}

	public Object findProfileMBean(String profileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ObjectName getProfileMBean(String profileName, boolean isDefault) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<String> getProfileNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public ObjectName getUsageMBeanName() throws InvalidArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Determines if profile is in back end storage == visible to other
	 * compoenents than MBean, if null is passed as argumetn it must check for
	 * any other than defualt?
	 */
	public boolean isProfileCommitted(String profileName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Triggers remove operation on this profile table.
	 */
	public void removeProfileTable() {
		// TODO Auto-generated method stub

	}

	public void rename(String newProfileTableName) {
		// TODO Auto-generated method stub

	}

}
