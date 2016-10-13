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

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.management.SleeState;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedProfileNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.component.profile.ProfileConcreteClassInfo;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.profile.entity.ProfileEntity;
import org.mobicents.slee.container.profile.entity.ProfileEntityFactory;
import org.mobicents.slee.container.profile.entity.ProfileEntityFramework;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.runtime.facilities.profile.AbstractProfileEvent;
import org.mobicents.slee.runtime.facilities.profile.ProfileAddedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileRemovedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileUpdatedEventImpl;

/**
 * Start time:16:46:52 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * Class representing Profile Object - this object servers as place holder for
 * selected profiles. ProfileObject can belong to only one profile table during
 * its lifecycle, ever.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
public class ProfileObjectImpl implements ProfileObject {

	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(ProfileObject.class);

	/**
	 * the state of the profile object
	 */
	private ProfileObjectState state = ProfileObjectState.DOES_NOT_EXIST;
	
	/**
	 * the profile table the profile is related
	 */
	private final ProfileTableImpl profileTable;
	
	/**
	 * the instance of the concrete implementation of the profile spec 
	 */
	private final ProfileConcrete profileConcrete;
	
	/**
	 * a snapshot copy of the current profile pojo, before updates, needed
	 * for profile table events
	 */
	private ProfileEntity profileEntitySnapshot;
	
	/**
	 * the context of the profile object
	 */
	private ProfileContextImpl profileContext = null;

	/**
	 * inidcates if this profile can be invoked more than once in a call tree for a single tx, thus exposed to loops
	 */
	private final boolean profileReentrant;
	
	/**
	 * indicates if the object is related with a slee 1.1 profile spec or not
	 */
	private final boolean isSlee11;
	
	/**
	 * the entity holding cmp data of currently assigned profile
	 */
	private ProfileEntity profileEntity;
	
	/**
	 * 
	 */
	private final boolean readOnlyProfileTable;
	
	/**
	 * 
	 */
	private final ProfileEntityFramework profileEntityFramework;
	
	/**
	 *
	 */
	private boolean persisted;
	
	/**
	 * used to filter invocations to profile concrete
	 */
	private final ProfileConcreteClassInfo profileConcreteClassInfo;
	
	/**
	 * 
	 * @param profileTable
	 * @param profileSpecificationId
	 * @throws NullPointerException
	 * @throws SLEEException
	 */
	public ProfileObjectImpl(ProfileTableImpl profileTable) throws NullPointerException, SLEEException {
		
		if (profileTable == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		
		this.profileTable = profileTable;
		
		final ProfileSpecificationComponent component = profileTable.getProfileSpecificationComponent();
		
		this.profileReentrant = component.getDescriptor().getProfileAbstractClass() == null ? false : this.profileTable.getProfileSpecificationComponent().getDescriptor().getProfileAbstractClass().getReentrant();
		this.readOnlyProfileTable = component.getDescriptor().getReadOnly();
		this.profileConcreteClassInfo = component.getProfileConcreteClassInfo();
		
		this.isSlee11 = profileTable.getProfileSpecificationComponent().isSlee11();
		
		try {
			this.profileConcrete = (ProfileConcrete) component.getProfileCmpConcreteClass().newInstance();
			this.profileConcrete.setProfileObject(this);		
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
		}
		
		this.profileEntityFramework = component.getProfileEntityFramework();
		
	}

	/**
	 * Invoked from pool.
	 * 
	 * @param profileContext
	 */
	public void setProfileContext(ProfileContextImpl profileContext) {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[setProfileContext] "+this);
		}

		if (profileContext == null) {
			throw new NullPointerException("Passed context must not be null.");
		}
		
		if (state != ProfileObjectState.DOES_NOT_EXIST) {
			throw new IllegalStateException("Wrong state: " + this.state + ",on profile set context operation, for profile table: "
					+ this.profileTable.getProfileTableName() + " with specification: " + this.profileTable.getProfileSpecificationComponent().getProfileSpecificationID());
		}

		this.profileContext = profileContext;
		this.profileContext.setProfileObject(this);
		
		if (profileConcreteClassInfo.isInvokeSetProfileContext()) {
			final ClassLoader oldClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();

			try {
				final ClassLoader cl = this.profileTable.getProfileSpecificationComponent().getClassLoader();

				if (System.getSecurityManager()!=null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							Thread.currentThread().setContextClassLoader(cl);
							return null;
						}
					});
				}
				else {
					Thread.currentThread().setContextClassLoader(cl);
				}

				try {
					if (isSlee11) {
						try {
							profileConcrete.setProfileContext(profileContext);
						}
						catch (RuntimeException e) {
							runtimeExceptionOnProfileInvocation(e);
						}
					}
				}
				catch (Exception e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception encountered while setting profile context for profile table: "
								+ this.profileTable.getProfileTableName() + " with specification: " + this.profileTable.getProfileSpecificationComponent().getProfileSpecificationID(), e);
				}			
			}
			finally {
				if (System.getSecurityManager()!=null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							Thread.currentThread().setContextClassLoader(oldClassLoader);
							return null;
						}
					});
				}
				else {
					Thread.currentThread().setContextClassLoader(oldClassLoader);
				}
			}
		}
		
		state = ProfileObjectState.POOLED;

	}
	
	/**
	 * Creation of the profile with the specified name
	 * @param profileName
	 * @throws CreateException
	 */
	public void profileCreate(String profileName) throws CreateException {
		profileInitialize(profileName);
		profilePostCreate();
		profileStore();
		profilePersist();
	}
	
	public void profilePersist() {
		persistProfileConcrete();
		persisted = true;
	}
	
	/**
	 * initialize state from default profile
	 */
	private void profileInitialize(String profileName) {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profileInitialize] "+this+" , profileName = "+profileName);
		}
				
		if (this.state != ProfileObjectState.POOLED) {
			throw new SLEEException(this.toString());
		}
		
		if (profileName == null) {
			// default profile creation
			// create instance of entity
			profileEntity = profileEntityFramework.getProfileEntityFactory().newInstance(profileTable.getProfileTableName(), null);
			// change state
			this.state = ProfileObjectState.PROFILE_INITIALIZATION;
			// invoke life cycle method on profile
			if (profileConcreteClassInfo.isInvokeProfileInitialize()) {
				try {
					profileConcrete.profileInitialize();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}				
			}
		}
		else {
			// load the default profile entity
			if (logger.isTraceEnabled()) {
				logger.trace("Copying state from default profile on object "+this);
			}
			profileEntity = cloneEntity(profileTable.getDefaultProfileEntity());
			profileEntity.setProfileName(profileName);
		}
				
		// mark entity as dirty and for creation
		profileEntity.create();
		profileEntity.setDirty(true);
	}
	
	/**
	 * 
	 * @throws CreateException
	 */
	private void profilePostCreate() throws CreateException {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profilePostCreate] "+this);
		}
		
		if (this.state != ProfileObjectState.POOLED && this.state != ProfileObjectState.PROFILE_INITIALIZATION) {
			throw new SLEEException(this.toString());
		}
		
		this.state = ProfileObjectState.READY;
		
		if (isSlee11) {
			if (profileConcreteClassInfo.isInvokeProfilePostCreate()) {
				try {
					this.profileConcrete.profilePostCreate();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}	
			}
		}		
	}
	
	/**
	 * 
	 * Activates the profile object and use a specific snapshot of a profile entity
	 * @param snapshot
	 */
	public void profileActivate(ProfileEntity profileEntity) {		
		profileActivate();	
		profileLoad(profileEntity);
	}
		
	/**
	 * Activates the profile object and loads its persistent state
	 * @param profileName
	 * @throws UnrecognizedProfileNameException
	 */
	public void profileActivate(String profileName) throws UnrecognizedProfileNameException {
		profileActivate();
		profileLoad(profileName);		
	}
	
	/**
	 * 
	 */
	private void profileActivate() {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profileActivate] "+this);
		}
		
		if (state != ProfileObjectState.POOLED) {
			throw new SLEEException(this.toString());
		}
		if (isSlee11) {
			if (profileConcreteClassInfo.isInvokeProfileActivate()) {
				try {
					profileConcrete.profileActivate();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}	
			}
		}
		this.state = ProfileObjectState.READY;
	}
	
	/**
	 * 
	 */
	private void profileLoad(String profileName) throws UnrecognizedProfileNameException {
				
		if(logger.isTraceEnabled()) {
			logger.trace("[profileLoad] "+this+" , profileName = "+profileName);
		}
		
		ProfileEntity profileEntity = loadProfileEntity(profileName);
		profileEntity.setReadOnly(readOnlyProfileTable);
		profileLoad(profileEntity);				
	}
	
	/**
	 * 
	 * @param profileEntity
	 */
	private void profileLoad(ProfileEntity profileEntity) {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profileLoad] "+this+" , profileEntity = "+profileEntity);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}
		
		this.profileEntity = profileEntity;
		
		// create a snapshot copy if the profile table fires events
		if (profileTable.doesFireEvents()) {
			profileEntitySnapshot = cloneEntity(profileEntity);
			profileEntitySnapshot.setReadOnly(true);;						
		}
		if (profileConcreteClassInfo.isInvokeProfileLoad()) {
			try {
				this.profileConcrete.profileLoad();
			}
			catch (RuntimeException e) {
				runtimeExceptionOnProfileInvocation(e);
			}	
		}
	}

	/**
	 * 
	 * @throws ProfileVerificationException
	 */
	public void profileVerify() throws ProfileVerificationException {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profileVerify] "+this);
		}
		
		profileStore();
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}

		if(!isSlee11 || profileEntity.getProfileName() != null) {
			// only invoke when it is a slee 1.0 profile or a non default slee 1.1 profile
			if (profileConcreteClassInfo.isInvokeProfileVerify()) {
				try {
					profileConcrete.profileVerify();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}
			}
		}
	}
	
	private void runtimeExceptionOnProfileInvocation(RuntimeException e) {
		logger.error("Runtime exception when invoking concrete profile! Setting tx for rollback and invalidating object",e);
		final SleeTransactionManager txManager = profileTable.getSleeContainer().getTransactionManager();
		try {
			if (txManager.getTransaction() != null) {
				txManager.setRollbackOnly();
			}			
		} catch (Throwable f) {
			logger.error(f.getMessage(),f);
		}
		invalidateObject();
		throw e;
	}
	
	/**
	 * 
	 */
	public void profileStore() {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profileStore] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}

		if (profileEntity.isReadOnly()) {
			return;
		}
		
		if (profileConcreteClassInfo.isInvokeProfileStore()) {
			try {
				profileConcrete.profileStore();
			}
			catch (RuntimeException e) {
				runtimeExceptionOnProfileInvocation(e);
			}
		}
	}
	
	/**
	 * 
	 */
	public void profilePassivate() {
		
		if (state == ProfileObjectState.READY) {

			// FIXME tests/profiles/lifecycle/Test1110227Test.xml enforces profileStore to be called before every profilePassivate(), doing this here to enfore it happens
			profileStore();
			
			if(logger.isTraceEnabled()) {
				logger.trace("[profilePassivate] "+this);
			}

			if (this.profileEntity.getProfileName() != null) {
				state = ProfileObjectState.POOLED;			
			}
			else {
				// FIXME due to tests/profiles/profileabstractclass/Test1110251Test.xml we need to get ridden of the default profile object
				state = ProfileObjectState.DOES_NOT_EXIST;
			}
			
			if (profileConcreteClassInfo.isInvokeProfilePassivate()) {
				if (isSlee11) {
					try {
						profileConcrete.profilePassivate();
					}
					catch (RuntimeException e) {
						runtimeExceptionOnProfileInvocation(e);
					}
				}
			}
			
		}
		
		this.profileEntity = null;
		this.profileEntitySnapshot = null;
		
	}

	/**
	 * 
	 */
	public void profileRemove(boolean invokeConcreteSbb, boolean isUninstall) {
		
		if(logger.isTraceEnabled()) {
			logger.trace("[profileRemove] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}
				
		if (isSlee11 && invokeConcreteSbb) {
			if (profileConcreteClassInfo.isInvokeProfileRemove()) {
				try {
					profileConcrete.profileRemove();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}			
			}
		}
		
		if (profileTable.doesFireEvents() && profileEntity.getProfileName() != null && profileEntitySnapshot != null && profileTable.getSleeContainer().getSleeState() == SleeState.RUNNING) {
			// fire event
			AbstractProfileEvent event = new ProfileRemovedEventImpl(profileEntity,profileTable.getProfileManagement());
			if (logger.isTraceEnabled()) {
				logger.trace("firing profile removed event for profile named "+profileEntity);
			}
			profileTable.getActivityContext().fireEvent(event.getEventTypeID(), event,
					event.getProfileAddress(), null, null,null,null);
		}
		
		if(profileEntity.getProfileName() != null && !isUninstall) {
			profileEntityFramework.removeprofile(profileEntity);
		}
		else {
			// the default profile entity is not stored in the framework
			profileTable.setDefaultProfileEntity(null);
		}
		
		state = ProfileObjectState.POOLED;
		
		this.profileEntity = null;
		this.profileEntitySnapshot = null;
					
	}

	/**
	 * Invoked when pool removes object
	 */
	public void unsetProfileContext() {

		if(logger.isTraceEnabled()) {
			logger.trace("[unsetProfileContext] "+this);
		}

		if (state == ProfileObjectState.POOLED  && profileConcreteClassInfo.isInvokeUnsetProfileContext()) {

			final ClassLoader oldClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();
			try	{
				final ClassLoader cl = profileTable.getProfileSpecificationComponent().getClassLoader();
				if (System.getSecurityManager()!=null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run()	{
							Thread.currentThread().setContextClassLoader(cl);
							return null;
						}
					});
				}
				else {
					Thread.currentThread().setContextClassLoader(cl);
				}

				if (isSlee11) {
					try {
						profileConcrete.unsetProfileContext();
					}
					catch (RuntimeException e) {
						runtimeExceptionOnProfileInvocation(e);
					}	
				}
				profileContext.setProfileObject(null);
				state = ProfileObjectState.DOES_NOT_EXIST;
			}
			finally {
				if (System.getSecurityManager()!=null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							Thread.currentThread().setContextClassLoader(oldClassLoader);
							return null;
						}
					});
				}
				else {
					Thread.currentThread().setContextClassLoader(oldClassLoader);
				}
			}
		}
	}
	
	// ------------
	
	/**
	 * 	
	 */
	public ProfileConcrete getProfileConcrete() {
		return profileConcrete;
	}
	
	/**
	 * 
	 */
	public ProfileEntity getProfileEntity() {
		return profileEntity;
	}	
	
	/**
	 * Retrieves the local representation for this profile object
	 * @return
	 */
	public ProfileLocalObject getProfileLocalObject() {
		final Class<?> profileLocalObjectConcreteClass = profileTable.getProfileSpecificationComponent().getProfileLocalObjectConcreteClass();
		ProfileLocalObject profileLocalObject = null;
		if (profileLocalObjectConcreteClass == null) {
			profileLocalObject = new ProfileLocalObjectImpl(this);
		}
		else {
			try {
				profileLocalObject = (ProfileLocalObject) profileLocalObjectConcreteClass.getConstructor(ProfileObjectImpl.class).newInstance(this);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		
		return profileLocalObject;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProfileObjectState getState() {
		return state;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isProfileReentrant() {
		return profileReentrant;
	}

	/**
	 * 
	 * @return
	 */
	public ProfileTableImpl getProfileTable() {
		return profileTable;
	}

	private ProfileEntity cloneEntity(final ProfileEntity source) {
		final ProfileEntityFactory profileEntityFactory = profileTable.getProfileSpecificationComponent().getProfileEntityFramework().getProfileEntityFactory();
		final ProfileEntity result = profileEntityFactory.newInstance(source.getTableName(), source.getProfileName());
		if(System.getSecurityManager()==null)
		{
			profileEntityFactory.copyAttributes(source, result);
		}else
		{
			AccessController.doPrivileged(new PrivilegedAction<Object>(){

				public Object run() {
					profileEntityFactory.copyAttributes(source, result);
					return null;
				}});
		}
		return result;
	}
	
	/**
	 * 
	 */
	private ProfileEntity loadProfileEntity(String profileName) throws UnrecognizedProfileNameException {
		
		if (profileName != null) {
			ProfileEntity profileEntity = profileEntityFramework.retrieveProfile(profileTable.getProfileTableName(), profileName);
			if (profileEntity == null) {
				throw new UnrecognizedProfileNameException();
			}
			return profileEntity;
		}
		else {
			// clone default profile entity 
			final ProfileEntity profileEntity = cloneEntity(profileTable.getDefaultProfileEntity());
			// add a tx action after commit to update the state on the one stored in the table
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					profileTable.setDefaultProfileEntity(cloneEntity(profileEntity));
				}
			};
			profileTable.getSleeContainer().getTransactionManager().getTransactionContext().getAfterCommitActions().add(action);				
			return profileEntity;
		}		
	}
	
	/**
	 * 
	 */
	private void persistProfileConcrete() {
		if (profileEntity.isCreate()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Persisting "+this);
				
			}			
			if (profileEntity.getProfileName() != null) {
				// the default profile entity is not persisted using the framework
				profileEntityFramework.persistProfile(profileEntity);		
			}	
			else {
				profileTable.setDefaultProfileEntity(cloneEntity(profileEntity));
			}
		}		
	}
	
	public String toString() {
		return "ProfileObject( table= "+profileTable.getProfileTableName()+" , state = "+state+" , entity = "+ profileEntity+" )";
	}

	/**
	 * use this method to move the object to {@link ProfileObjectState#DOES_NOT_EXIST} state and indicate to the pool that the object should be discarded
	 */
	public void invalidateObject() {
		state = ProfileObjectState.DOES_NOT_EXIST;		
	}

	/**
	 * Fires a profile added or updated event if the profile object state is ready and the persistent state is dirty 
	 */
	public void fireAddOrUpdatedEventIfNeeded() {
		
		if (state == ProfileObjectState.READY) {
			if (profileEntity.isDirty()) {
				// check the table fires events and the object is not assigned to a default profile
				if (profileTable.doesFireEvents() && profileEntity.getProfileName() != null && profileTable.getSleeContainer().getSleeState() == SleeState.RUNNING) {
					// Fire a Profile Added or Updated Event
					ActivityContext ac = profileTable.getActivityContext();
					AbstractProfileEvent event = null;
					if (profileEntity.isCreate()) {
						if (persisted) {
							event = new ProfileAddedEventImpl(profileEntity,profileTable.getProfileManagement());
							persisted = false;
							if (logger.isTraceEnabled()) {
								logger.trace("firing profile added event for profile named "+profileEntity);
							}
						}
						else {
							return;
						}
					}
					else {
						event = new ProfileUpdatedEventImpl(profileEntitySnapshot,profileEntity,profileTable.getProfileManagement());		
						if (logger.isTraceEnabled()) {
							logger.trace("firing profile updated event for profile named "+profileEntity);
						}
					}
					ac.fireEvent(event.getEventTypeID(), event,
							event.getProfileAddress(), null, null,null,null);
				}
			}
		}
		
	}
	
	/**
	 * the profile cmp slee 1.0 wrapper for this profile object
	 */
	private AbstractProfileCmpSlee10Wrapper profileCmpSlee10Wrapper;
	
	/**
	 * Retrieves the profile cmp slee 1.0 wrapper for this profile object
	 * @return
	 */
	public AbstractProfileCmpSlee10Wrapper getProfileCmpSlee10Wrapper() {
		if (profileCmpSlee10Wrapper == null) {
			try {
				profileCmpSlee10Wrapper = (AbstractProfileCmpSlee10Wrapper) profileTable.getProfileSpecificationComponent().getProfileCmpSlee10WrapperClass().getConstructor(ProfileObjectImpl.class).newInstance(this);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		return profileCmpSlee10Wrapper;
	}

}
