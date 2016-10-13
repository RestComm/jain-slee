/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.SleeState;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.ReadOnlyProfileException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.resource.ActivityFlags;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jgroups.Address;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.profile.ProfileAttribute;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.management.TraceManagement;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBean;
import org.mobicents.slee.container.profile.entity.ProfileEntity;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.container.util.concurrent.ConcurrentHashSet;
import org.mobicents.slee.runtime.facilities.NotificationSourceWrapperImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandleImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;

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
	private final ProfileManagementImpl profileManagement;
	private final SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	private NotificationSourceWrapperImpl profileTableNotification = null;
	
	/**
	 * 
	 */
	private ProfileTableUsageMBean profileTableUsageMBean = null;
	
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
	public ProfileTableImpl(final String profileTableName, ProfileSpecificationComponent component, ProfileManagementImpl profileManagement) {
		
		ProfileTableImpl.validateProfileTableName(profileTableName);
		if (profileManagement == null || component == null) {
			throw new NullPointerException();
		}

		this.component = component;		
		this.profileManagement = profileManagement;
		this.sleeContainer = profileManagement.getSleeContainer();
		
		this.profileTableName = profileTableName;
		
		this.profileTableNotification = new NotificationSourceWrapperImpl(
				new ProfileTableNotification(this.profileTableName));
		
		this.fireEvents = component.getDescriptor().getEventsEnabled();
		this.transactionView = new ProfileTableTransactionView(this);
	}
	
	private boolean traceRegistred = false;
	
	public void registerTracer() {
		
		if (!traceRegistred) {
			// register tracer
			final TraceManagement traceMBeanImpl = sleeContainer.getTraceManagement();
			traceMBeanImpl.registerNotificationSource(new ProfileTableNotification(profileTableName));
			TransactionalAction action2 = new TransactionalAction() {
				public void execute() {
					// remove notification sources for profile table
					traceMBeanImpl.deregisterNotificationSource(new ProfileTableNotification(profileTableName));
				}
			};
			sleeContainer.getTransactionManager().getTransactionContext().getAfterRollbackActions().add(action2);
			traceRegistred = true;
		}

	}
	
	/**
	 * 
	 * @return
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	/**
	 * @return the profileManagement
	 */
	public ProfileManagementImpl getProfileManagement() {
		return profileManagement;
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
	public NotificationSourceWrapperImpl getProfileTableNotification() {
		return this.profileTableNotification;
	}

	/**
	 * 
	 * @return
	 */
	public ProfileTableUsageMBean getProfileTableUsageMBean() {
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
		ProfileObjectImpl profileObject = createProfile(profileName);
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
				
		ProfileObjectImpl profileObject = getProfile(profileName);
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
		
		return this.removeProfile(profileName,true,false);
	}

	/**
	 * 
	 * @param profileName
	 * @param invokeConcreteSbb
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public boolean removeProfile(String profileName, boolean invokeConcreteSbb, boolean isUninstall)
			throws TransactionRequiredLocalException, SLEEException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this + " Profile[" + profileName
					+ "]");
		}
		
		ProfileObjectImpl profileObject = getProfile(profileName);
		if (profileObject != null) {
			// remove using object
			profileObject.profileRemove(invokeConcreteSbb, isUninstall);
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
		if (logger.isTraceEnabled()) {
			logger.trace("Creating default profile for table "+profileTableName);
		}
		ProfileObjectImpl profileObject = transactionView.createProfile(null);
		profileObject.profileVerify();	
		this.defaultProfileEntity = profileObject.getProfileEntity();
	}
	
	public ProfileObjectImpl createProfile(String newProfileName)
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
	 * 
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
			if (!ProfileTableImpl.PROFILE_ATTR_ALLOWED_TYPES.contains(attributeValue.getClass().getName())) {
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
	 * This method renames Profile Table in backend storage. NOTE: It should not be called directly, use SleeProfileTableManager instead!
	 * @param newProfileTableName
	 */
	public void rename(String newProfileTableName) {
		//we have to do this like that cause once JPA is done, those profiles wont exist, since we do UPDATE of a table name, not a copy
		//thus no profiles will be returned on this call later on. ouch :)
	  Collection<ProfileID> profileIDs = this.getProfiles();
	  component.getProfileEntityFramework().renameProfileTable(this.profileTableName, newProfileTableName);
	  //here we remove beans.
	  for(ProfileID pid: profileIDs)
	  {
		  try{
			AbstractProfileMBeanImpl.close(pid.getProfileTableName(), pid.getProfileName());  
		  }catch(Exception e)
		  {
			 if(logger.isEnabledFor(Level.WARN))
			 {
				 logger.warn("Unexpected behaviour on MBean deregistration.", e);
			 }
		  }
	  }
	  
	}

	/**
	 * Triggers remove operation on this profile table.
	 * 
	 * @throws UnrecognizedProfileTableNameException
	 */
	public void remove(boolean isUninstall) throws SLEEException {

		if (logger.isTraceEnabled()) {
			logger.trace("removeProfileTable: removing profileTable="
					+ profileTableName);
		}
		
		// remove the table profiles, at this stage they may use notification source, lets leave it.
		for (ProfileID profileID : getProfiles()) {
			// don't invoke the profile concrete object, to avoid evil profile lifecycle impls 
			// that rollbacks tx, as Test1110251Test
			this.removeProfile(profileID.getProfileName(), false, isUninstall);
		}

		// remove default profile
		if (getDefaultProfileEntity() != null) {
			this.removeProfile(null, false, false);
		}
		
		// add action after commit to remove tracer and close uncommitted mbeans
		TransactionalAction commitAction = new TransactionalAction() {
			public void execute() {
				// remove notification sources for profile table
				final TraceManagement traceMBeanImpl = sleeContainer.getTraceManagement();
				traceMBeanImpl.deregisterNotificationSource(new ProfileTableNotification(profileTableName));
				// close uncommitted mbeans
				closeUncommittedProfileMBeans();					
			}
		};
		sleeContainer.getTransactionManager().getTransactionContext().getAfterCommitActions().add(commitAction);
		

		if (sleeContainer.getSleeState() == SleeState.RUNNING) {
			endActivity();
		}

		// unregister mbean
		unregisterUsageMBean();

		// remove object pool
		profileManagement.getObjectPoolManagement().removeObjectPool(this, sleeContainer.getTransactionManager());

	}

	/**
	 * 
	 * @param profileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileObjectImpl getProfile(String profileName)
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

	ProfileTableActivityImpl profileTableActivity = null;
	
	/**
	 * 
	 */
	public ProfileTableActivityImpl getActivity() {
		if (profileTableActivity == null) {
			Address clusterLocalAddress = null;
			if (!profileManagement.getJPAConfiguration().isClusteredProfiles()) {
				// special scenario, we may run in a cluster but without clustered
				// profiles, so the activity must be unique for each cluster node
				clusterLocalAddress = sleeContainer.getCluster().getLocalAddress();
			}
			profileTableActivity = new ProfileTableActivityImpl(new ProfileTableActivityHandleImpl(this.profileTableName, clusterLocalAddress));
		}
		return profileTableActivity;
	}
	
	/**
	 * 
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return new ProfileTableActivityContextHandle(getActivity().getHandle());
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivityContext getActivityContext() {
		return sleeContainer.getActivityContextFactory().getActivityContext(getActivityContextHandle());
	}
	
	/**
	 * 
	 */
	public void startActivity() {
		sleeContainer.getActivityContextFactory().createActivityContext(getActivityContextHandle(),ActivityFlags.NO_FLAGS);
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
				this.profileTableUsageMBean = sleeContainer.getUsageParametersManagement().newProfileTableUsageMBean(profileTableName, component);				
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
	
	
	private static final Set<String> PROFILE_ATTR_ALLOWED_TYPES = getAllowedTypes();
	
	private static final Set<String> getAllowedTypes() {	
		Set<String> tmp = new HashSet<String>();
		for (String type : ProfileAttribute.PRIMITIVE_ALLOWED_PROFILE_ATTRIBUTE_TYPES) {
			tmp.add(type);
		}
		for (String type : ProfileAttribute.NON_PRIMITIVE_ALLOWED_PROFILE_ATTRIBUTE_TYPES) {
			tmp.add(type);
		}
		return Collections.unmodifiableSet(tmp);
	}
}
