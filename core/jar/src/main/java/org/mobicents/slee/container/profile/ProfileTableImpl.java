package org.mobicents.slee.container.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.ObjectName;
import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.ProfileTableUsageMBean;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.ReadOnlyProfileException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.profile.ProfileAttribute;
import org.mobicents.slee.container.component.profile.ProfileEntity;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBeanImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.facilities.MNotificationSource;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;
import org.mobicents.slee.util.concurrent.ConcurrentHashSet;

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
public class ProfileTableImpl implements ProfileTable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int _UNICODE_RANGE_START = 0x0020;
	public static final int _UNICODE_RANGE_END = 0x007e;
	public static final int _UNICODE_SLASH = 0x002f;
	
	private static final Logger logger = Logger.getLogger(ProfileTableImpl.class);

	/**
	 * 
	 */
	private final ProfileSpecificationComponent component;
	
	/**
	 * 
	 */
	private final String profileTableName;
	
	/**
	 * 
	 */
	private final SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	private MNotificationSource profileTableNotification = null;
	
	/**
	 * 
	 */
	private ProfileTableUsageMBeanImpl profileTableUsageMBean = null;
	
	/**
	 * indicates if this table fires events
	 */
	private final boolean fireEvents;

	/**
	 * 
	 */
	private final ProfileTableTransactionView transactionView;
	
	/**
	 * the entity with the default profile attribute values
	 */
	private ProfileEntity defaultProfileEntity;
	
	/**
	 * 
	 * @param profileTableName
	 * @param component
	 * @param sleeContainer
	 */
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
		this.transactionView = new ProfileTableTransactionView(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean doesFireEvents() {
		return fireEvents;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProfileTableName() {
		return this.profileTableName;
	}

	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationComponent getProfileSpecificationComponent() {
		return component;
	}

	/**
	 * 
	 * @return
	 */
	public MNotificationSource getProfileTableNotification() {
		return this.profileTableNotification;
	}

	/**
	 * 
	 * @return
	 */
	public ProfileTableUsageMBeanImpl getProfileTableUsageMBean() {
		return profileTableUsageMBean;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<ProfileID> getProfiles() {
		List<ProfileID> result = new ArrayList<ProfileID>();
		for (ProfileEntity profileEntity : component.getProfileEntityFramework().findAll(this.getProfileTableName())) {
			result.add(new ProfileID(profileTableName,profileEntity.getProfileName()));
		}
		return Collections.unmodifiableCollection(result);
	}

	/**
	 * 
	 * @throws ReadOnlyProfileException
	 */
	private void checkProfileSpecIsNotReadOnly() throws ReadOnlyProfileException {
		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException(component.toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileTable#create(java.lang.String)
	 */
	public ProfileLocalObject create(String profileName)
			throws NullPointerException, IllegalArgumentException,
			TransactionRequiredLocalException, ReadOnlyProfileException,
			ProfileAlreadyExistsException, CreateException, SLEEException {

		sleeContainer.getTransactionManager().mandateTransaction();

		checkProfileSpecIsNotReadOnly();
		ProfileObject profileObject = createProfile(profileName);
		profileObject.profilePersist();
		return profileObject.getProfileLocalObject();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileTable#find(java.lang.String)
	 */
	public ProfileLocalObject find(String profileName)
			throws NullPointerException, TransactionRequiredLocalException,
			SLEEException {
		
		if (profileName == null) {
			throw new NullPointerException();
		}
				
		ProfileObject profileObject = getProfile(profileName);
		return profileObject == null ? null : profileObject.getProfileLocalObject();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileTable#findAll()
	 */
	public Collection<ProfileLocalObject> findAll() throws TransactionRequiredLocalException,
			SLEEException {
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		Collection<ProfileLocalObject> result = new ArrayList<ProfileLocalObject>();
		for (ProfileEntity profileEntity : component.getProfileEntityFramework().findAll(this.getProfileTableName())) {
			result.add(transactionView.getProfile(profileEntity).getProfileLocalObject());
		}
		return Collections.unmodifiableCollection(result);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileTable#remove(java.lang.String)
	 */
	public boolean remove(String profileName) throws NullPointerException,
			ReadOnlyProfileException, TransactionRequiredLocalException,
			SLEEException {
		
		if (profileName == null) {
			throw new NullPointerException("Profile name must not be null");
		}

		checkProfileSpecIsNotReadOnly();
		
		return this.removeProfile(profileName,true);
	}

	/**
	 * 
	 * @param profileName
	 * @param invokeConcreteSbb
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public boolean removeProfile(String profileName, boolean invokeConcreteSbb)
			throws TransactionRequiredLocalException, SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName
					+ "]");
		}
		
		ProfileObject profileObject = getProfile(profileName);
		if (profileObject != null) {
			// remove using object
			profileObject.profileRemove(invokeConcreteSbb);
			// close mbean if exists
			AbstractProfileMBeanImpl.close(profileTableName,profileName);	
			return true;
		}
		else {
			return false;
		}
																	
	}

	/**
	 * 
	 * @param profileName
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
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

	/**
	 * 
	 * @param profileTableName
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
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

	/**
	 * 
	 * @return
	 */
	public ProfileEntity getDefaultProfileEntity() {
		return defaultProfileEntity;
	}
	
	/**
	 * 
	 * @throws CreateException
	 * @throws ProfileVerificationException
	 */
	public void createDefaultProfile() throws CreateException, ProfileVerificationException {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating default profile for table "+profileTableName);
		}
		ProfileObject profileObject = transactionView.createProfile(null);
		profileObject.profileVerify();	
		this.defaultProfileEntity = profileObject.getProfileEntity();
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
									
			return transactionView.createProfile(newProfileName);
		} catch (IllegalArgumentException e) {
			throw new SLEEException(e.getMessage(), e);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileTable#findProfileByAttribute(java.lang.String, java.lang.Object)
	 */
	public ProfileLocalObject findProfileByAttribute(String attributeName,
			Object attributeValue) throws NullPointerException,
			IllegalArgumentException, TransactionRequiredLocalException,
			SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("findProfileByAttribute( attributeName = "+attributeName+" , attributeValue = "+attributeValue+" )");
		}
		
		Collection<ProfileLocalObject> plocs = findProfilesByAttribute(attributeName, attributeValue);
	    if(plocs.size() == 0) {
	    	return null;
	    }
	    else {
	    	return plocs.iterator().next();
	    }
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileTable#findProfilesByAttribute(java.lang.String, java.lang.Object)
	 */
	public Collection<ProfileLocalObject> findProfilesByAttribute(String attributeName,
			Object attributeValue) throws NullPointerException,
			IllegalArgumentException, TransactionRequiredLocalException,
			SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("findProfilesByAttribute( attributeName = "+attributeName+" , attributeValue = "+attributeValue+" )");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();

		// We get profile entities
		Collection<ProfileEntity> profileEntities = null;
		try {
			profileEntities = getProfileEntitiesByAttribute(attributeName, attributeValue, true);
		}
		catch (AttributeNotIndexedException e) {
			throw new SLEEException(e.getMessage(),e);
		} catch (UnrecognizedAttributeException e) {
			throw new IllegalArgumentException(e);
		} catch (AttributeTypeMismatchException e) {
			throw new IllegalArgumentException(e);
		}

		// We need ProfileLocalObjects
       ArrayList<ProfileLocalObject> plocs = new ArrayList<ProfileLocalObject>();
       for(ProfileEntity profileEntity : profileEntities) {
    	   plocs.add(transactionView.getProfile(profileEntity).getProfileLocalObject());
      }
      return Collections.unmodifiableCollection(plocs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.profile.ProfileTableConcrete#getProfilesByAttribute(java.lang.String, java.lang.Object, boolean)
	 */
	public Collection<ProfileID> getProfilesByAttribute(
			String attributeName, Object attributeValue, boolean isSlee11)
			throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException {
    
		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByAttribute( attributeName = "+attributeName+" , attributeValue = "+attributeValue+" , isSlee11 = "+isSlee11+" )");
		}
		
		// We get profile entities
		Collection<ProfileEntity> profileEntities = getProfileEntitiesByAttribute(attributeName, attributeValue, isSlee11);

		// We need ProfileIDs
		Collection<ProfileID> profileIDs = new ArrayList<ProfileID>();
		for(ProfileEntity profileEntity : profileEntities) {
			profileIDs.add( new ProfileID(profileEntity.getTableName(), profileEntity.getProfileName()) );    
		}

		return Collections.unmodifiableCollection( profileIDs );
	}	
	
	/**
	 * Retrieves the {@link ProfileEntity}s from the persistent store, matching specified attribute name and value type
	 * @param attributeName
	 * @param attributeValue
	 * @param isSlee11
	 * @return
	 * @throws UnrecognizedAttributeException
	 * @throws AttributeNotIndexedException
	 * @throws AttributeTypeMismatchException
	 * @throws SLEEException
	 */
	private Collection<ProfileEntity> getProfileEntitiesByAttribute(
			String attributeName, Object attributeValue, boolean isSlee11)
			throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException {
    
		if (logger.isDebugEnabled()) {
			logger.debug("getProfileEntitiesByAttribute( attributeName = "+attributeName+" , attributeValue = "+attributeValue+" , isSlee11 = "+isSlee11+" )");
		}
		
		ProfileAttribute profileAttribute = getProfileAttribute(attributeName, attributeValue);
		
		if (isSlee11)  {
			// validate attr value type
			if (!ProfileAttribute.ALLOWED_PROFILE_ATTRIBUTE_TYPES.contains(attributeValue.getClass().getName())) {
				throw new AttributeTypeMismatchException(attributeValue.getClass()+" is not a valid profile attribute value type");
			}
		}
		else {
			if (!profileAttribute.isIndex()) {
				throw new AttributeNotIndexedException(component.toString()+" defines an attribute named "+attributeName+" but not indexed");
			}
		}
		
		return component.getProfileEntityFramework().findProfilesByAttribute(this.getProfileTableName(), profileAttribute, attributeValue);
	}	
	
	/**
	 * Retrieves the {@link ProfileAttribute} from the component, matching specified attribute name and value type
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedAttributeException
	 * @throws AttributeTypeMismatchException
	 */
	private ProfileAttribute getProfileAttribute(String attributeName,
			Object attributeValue) throws NullPointerException, UnrecognizedAttributeException,
			AttributeTypeMismatchException {

		if (attributeName == null) {
			throw new NullPointerException("attribute name is null");
		}
		if (attributeValue == null) {
			throw new NullPointerException("attribute value is null");
		}
		
		ProfileAttribute profileAttribute = component.getProfileAttributes().get(attributeName);
		if (profileAttribute == null) {
			throw new UnrecognizedAttributeException(component.toString()+" does not defines an attribute named "+attributeName);
		}
		else {
			Class<?> allowedProfileAttributeType = profileAttribute.getNonPrimitiveType().isArray() ? profileAttribute.getNonPrimitiveType().getComponentType() : profileAttribute.getNonPrimitiveType() ; 
			if (!allowedProfileAttributeType.getName().equals(attributeValue.getClass().getName())) {
				throw new AttributeTypeMismatchException(component.toString()+" defines an attribute named "+attributeName+" with value type "+profileAttribute.getType()+", the specified value is of type "+attributeValue.getClass());
			}
			else {
				return profileAttribute;
			}
		}
	}
	
	private ObjectName getUsageMBeanName() throws IllegalArgumentException {
		try {
			return new ObjectName(ProfileTableUsageMBean.BASE_OBJECT_NAME + ','
					+ ProfileTableUsageMBean.PROFILE_TABLE_NAME_KEY + '='
					+ ObjectName.quote(profileTableName));
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Failed to create MBean name, due to some system level error.",
					e);

		}
	}

	/**
	 * Determines if profile is in back end storage == visible to other
	 * compoenents than MBean, if null is passed as argumetn it must check for
	 * any other than defualt?
	 */
	public boolean profileExists(String profileName) {
		
		boolean result = component.getProfileEntityFramework().findProfile(this.getProfileTableName(), profileName) != null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Profile named "+profileName+(result ? "" : " does not")+" exists on table named " + this.getProfileTableName());
		} 
		
		return result;
	}

	/**
	 * Triggers remove operation on this profile table.
	 * 
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void remove() throws SLEEException {

		if (logger.isDebugEnabled()) {
			logger.debug("removeProfileTable: removing profileTable="
					+ profileTableName);
		}

		// remove the table profiles
		for (ProfileID profileID : getProfiles()) {
			// don't invoke the profile concrete object, to avoid evil profile lifecycle impls 
			// that rollbacks tx, as Test1110251Test
			this.removeProfile(profileID.getProfileName(), false);
		}

		// remove default profile
		if (getDefaultProfileEntity() != null) {
			this.removeProfile(null, false);
		}

		// add action after commit to close uncommitted mbeans
		TransactionalAction commitAction = new TransactionalAction() {
			public void execute() {
				closeUncommittedProfileMBeans();					
			}
		};

		try {
			sleeContainer.getTransactionManager().addAfterCommitAction(commitAction);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}

		endActivity();

		// unregister mbean
		unregisterUsageMBean();

		// remove object pool
		sleeContainer.getProfileObjectPoolManagement().removeObjectPool(this, sleeContainer.getTransactionManager());

		// FIXME baranowb, where is the tracer notif source unregistred?

	}

	/**
	 * 
	 * @param profileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileObject getProfile(String profileName)
			throws TransactionRequiredLocalException, SLEEException {
		
		return transactionView.getProfile(profileName);
	}
	
	/**
	 * Use this method (and the object it returns) very carefully, if the profile entity is not from the current
	 * transaction and changes are done, those won't be persisted
	 * 
	 * @param profileEntity
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileObject getProfile(ProfileEntity profileEntity)
		throws TransactionRequiredLocalException, SLEEException {
		return transactionView.getProfile(profileEntity);
	}

	public String toString() {
		return " ProfileTableImpl ( table = "
				+ this.profileTableName + " , component ="
				+ component + " )";
	}

	/**
	 * 
	 * @param queryName
	 * @param arguments
	 * @return
	 * @throws InvalidArgumentException 
	 * @throws AttributeTypeMismatchException 
	 * @throws UnrecognizedQueryNameException 
	 * @throws SLEEException 
	 * @throws NullPointerException 
	 */
	public Collection<ProfileLocalObject> getProfilesByStaticQuery(String queryName, Object[] arguments) throws NullPointerException, SLEEException, UnrecognizedQueryNameException, AttributeTypeMismatchException, InvalidArgumentException {
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		Collection<ProfileLocalObject> plocs = new ArrayList<ProfileLocalObject>();

		for(ProfileEntity profileEntity : component.getProfileEntityFramework().getProfilesByStaticQuery( this.getProfileTableName(), queryName, arguments )) {
			plocs.add(transactionView.getProfile(profileEntity).getProfileLocalObject());
		}
      
		return Collections.unmodifiableCollection(plocs);
	}
	
	// ACTIVITY related

	/**
	 * 
	 */
	public ProfileTableActivity getActivity() {
		return new ProfileTableActivityImpl(new ProfileTableActivityHandle(this.profileTableName));
	}
	
	/**
	 * 
	 * @return
	 */
	private ActivityContextHandle getActivityContextHandle() {
		return ActivityContextHandlerFactory
		.createProfileTableActivityContextHandle(new ProfileTableActivityHandle(
				profileTableName));
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivityContext getActivityContext() {
		return sleeContainer.getActivityContextFactory().getActivityContext(getActivityContextHandle(), false);
	}
	
	/**
	 * 
	 */
	public void startActivity() {
		sleeContainer.getActivityContextFactory().createActivityContext(getActivityContextHandle());
	}
	
	/**
	 * 
	 */
	public void endActivity() {
		ActivityContext ac = getActivityContext();
		if (ac != null) {
			ac.endActivity();				
		}				
	}

	// USAGE MBEAN RELATED
	
	/**
	 * 
	 */
	public void registerUsageMBean() {
		if (component.getUsageParametersInterface() != null) {
			// create resource usage mbean
			try {
				ObjectName objectName = this.getUsageMBeanName();
				this.profileTableUsageMBean = new ProfileTableUsageMBeanImpl(
							this.profileTableName, component, sleeContainer);
				this.profileTableUsageMBean.setObjectName(objectName);
				sleeContainer.getMBeanServer().registerMBean(
						this.profileTableUsageMBean, objectName);
				// create default usage param set
				this.profileTableUsageMBean.createUsageParameterSet();
			} catch (Throwable t) {
				if (this.profileTableUsageMBean != null) {
					this.profileTableUsageMBean.remove();
				}
				throw new SLEEException(
						"Failed to create and register Table Usage MBean", t);
			}
		}
	}
	
	/**
	 * 
	 */
	public void unregisterUsageMBean() {
		if (this.profileTableUsageMBean != null) {
			try {
				this.profileTableUsageMBean.remove();
			}
			catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
	}
	
	// OPEN PROFILE MBEANS FOR PROFILES NOT CREATED
	
	/**
	 * 
	 */
	private final Set<AbstractProfileMBean> uncommittedProfileMBeans = new ConcurrentHashSet<AbstractProfileMBean>();
	
	/**
	 * 
	 * @param profileMBean
	 */
	public void addUncommittedProfileMBean(AbstractProfileMBean profileMBean) {
		uncommittedProfileMBeans.add(profileMBean);
	}
	
	/**
	 * 
	 * @param profileMBean
	 */
	public void removeUncommittedProfileMBean(AbstractProfileMBean profileMBean) {
		uncommittedProfileMBeans.remove(profileMBean);
	}
	
	/**
	 * 
	 */
	private void closeUncommittedProfileMBeans() {
		// run it in a new thread to ensure no nested transactions
		Runnable r = new Runnable() {
			public void run() {
				for (AbstractProfileMBean profileMBean : uncommittedProfileMBeans) {
					if (logger.isDebugEnabled()) {
						logger.debug("Closing uncommitted mbean "+profileMBean);
					}
					// just rollback the profile creation, since in that use case there is a rollback action to unregister the bean
					try {
						profileMBean.restoreProfile();
					} catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}			
				}
			}
		};
		new Thread(r).start();
		
	}

	public void setDefaultProfileEntity(ProfileEntity profileEntity) {
		this.defaultProfileEntity = profileEntity;
		
	}
}
