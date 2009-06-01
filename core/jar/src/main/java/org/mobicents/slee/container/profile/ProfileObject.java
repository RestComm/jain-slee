package org.mobicents.slee.container.profile;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.resource.EventFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.profile.ProfileEntity;
import org.mobicents.slee.container.component.profile.ProfileEntityFramework;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.facilities.profile.AbstractProfileEvent;
import org.mobicents.slee.runtime.facilities.profile.ProfileAddedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileRemovedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileUpdatedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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
public class ProfileObject {

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
	 * @param profileTable
	 * @param profileSpecificationId
	 * @throws NullPointerException
	 * @throws SLEEException
	 */
	public ProfileObject(ProfileTableImpl profileTable) throws NullPointerException, SLEEException {
		
		if (profileTable == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		
		this.profileTable = profileTable;
		
		final ProfileSpecificationComponent component = profileTable.getProfileSpecificationComponent();
		
		this.profileReentrant = component.getDescriptor().getProfileAbstractClass() == null ? false : this.profileTable.getProfileSpecificationComponent().getDescriptor().getProfileAbstractClass().getReentrant();
		this.readOnlyProfileTable = component.getDescriptor().getReadOnly();
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
		
		if(logger.isDebugEnabled()) {
			logger.debug("[setProfileContext] "+this);
		}

		if (profileContext == null) {
			throw new NullPointerException("Passed context must not be null.");
		}
		
		if (state != ProfileObjectState.DOES_NOT_EXIST) {
			throw new IllegalStateException("Wrong state: " + this.state + ",on profile set context operation, for profile table: "
					+ this.profileTable.getProfileTableName() + " with specification: " + this.profileTable.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		
		final ClassLoader oldClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();

		// FIXME: is this needed ?
		try
		{
			final ClassLoader cl = this.profileTable.getProfileSpecificationComponent().getClassLoader();
			
			if (System.getSecurityManager()!=null)
			{
				AccessController.doPrivileged(new PrivilegedAction()
				{
					public Object run()
					{
						Thread.currentThread().setContextClassLoader(cl);
						return null;
					}
				});
			}
			else
			{
				Thread.currentThread().setContextClassLoader(cl);
			}
						
			try
			{
				this.profileContext = profileContext;
				if (isSlee11) {
					try {
						profileConcrete.setProfileContext(profileContext);
					}
					catch (RuntimeException e) {
						runtimeExceptionOnProfileInvocation(e);
					}
				}
				this.profileContext.setProfileObject(this);
				state = ProfileObjectState.POOLED;
			}
			catch (Exception e) {
				if (logger.isDebugEnabled())
					logger.debug("Exception encountered while setting profile context for profile table: "
							+ this.profileTable.getProfileTableName() + " with specification: " + this.profileTable.getProfileSpecificationComponent().getProfileSpecificationID(), e);
			}			
		}
		finally
		{
			if (System.getSecurityManager()!=null)
			{
				AccessController.doPrivileged(new PrivilegedAction()
				{
					public Object run()
					{
						Thread.currentThread().setContextClassLoader(oldClassLoader);
						return null;
					}
				});
			}
			else
			{
				Thread.currentThread().setContextClassLoader(oldClassLoader);
			}
		}
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
		persistProfileConcrete();
	}
	
	/**
	 * initialize state from default profile
	 */
	private void profileInitialize(String profileName) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileInitialize] "+this+" , profileName = "+profileName);
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
				try {
					profileConcrete.profileInitialize();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}				
			}
			else {
				// load the default profile entity
				if (logger.isDebugEnabled()) {
					logger.debug("Copying state from default profile on object "+this);
				}
				try {
					loadProfileEntity(null);
				} catch (UnrecognizedProfileNameException e) {
					throw new SLEEException("the default profile does not exist?!?",e);
				}
				// clone it
				profileEntity = profileEntityFramework.getProfileEntityFactory().cloneInstance(profileName, profileEntity);				
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
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profilePostCreate] "+this);
		}
		
		if (this.state != ProfileObjectState.POOLED && this.state != ProfileObjectState.PROFILE_INITIALIZATION) {
			throw new SLEEException(this.toString());
		}
		
		this.state = ProfileObjectState.READY;
		
		if (isSlee11) {
			try {
				this.profileConcrete.profilePostCreate();
			}
			catch (RuntimeException e) {
				runtimeExceptionOnProfileInvocation(e);
			}	
		}		
		
	}
	
	/**
	 * 
	 * Activates the profile object and use a specific snapshot of a profile entity
	 * @param snapshot
	 */
	public void profileActivate(ProfileEntity profileEntity) {
		this.profileEntity = profileEntity;
		if (profileTable.doesFireEvents()) {
			profileEntitySnapshot = profileEntityFramework.getProfileEntityFactory().cloneInstance(profileEntity.getProfileName(), profileEntity);
			profileEntitySnapshot.setReadOnly(true);						
		}
		profileActivate();		
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
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileActivate] "+this);
		}
		
		if (state != ProfileObjectState.POOLED) {
			throw new SLEEException(this.toString());
		}
		if (isSlee11) {
			try {
				profileConcrete.profileActivate();
			}
			catch (RuntimeException e) {
				runtimeExceptionOnProfileInvocation(e);
			}	
		}
		this.state = ProfileObjectState.READY;
	}
	
	/**
	 * 
	 */
	private void profileLoad(String profileName) throws UnrecognizedProfileNameException {
				
		if(logger.isDebugEnabled()) {
			logger.debug("[profileLoad] "+this+" , profileName = "+profileName);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}
		
		// load profile concrete from data source
		if (logger.isDebugEnabled()) {
			logger.debug("Loading persisted state for profile named "+profileName+" on object "+this);
		}
		loadProfileEntity(profileName);
		// create a snapshot copy if the profile table fires events
		if (profileTable.doesFireEvents()) {
			profileEntitySnapshot = profileEntityFramework.getProfileEntityFactory().cloneInstance(profileName, profileEntity);
			profileEntitySnapshot.setReadOnly(true);						
		}
		try {
			this.profileConcrete.profileLoad();
		}
		catch (RuntimeException e) {
			runtimeExceptionOnProfileInvocation(e);
		}	
	}

	/**
	 * 
	 * @throws ProfileVerificationException
	 */
	public void profileVerify() throws ProfileVerificationException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileVerify] "+this);
		}
		
		profileStore();
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}

		if(!isSlee11 || profileEntity.getProfileName() != null) {
			// only invoke when it is a slee 1.0 profile or a non default slee 1.1 profile
			try {
				profileConcrete.profileVerify();
			}
			catch (RuntimeException e) {
				runtimeExceptionOnProfileInvocation(e);
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
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileStore] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}

		if (profileEntity.isReadOnly()) {
			return;
		}
		
		try {
			profileConcrete.profileStore();
		}
		catch (RuntimeException e) {
			runtimeExceptionOnProfileInvocation(e);
		}		
	}
	
	/**
	 * 
	 */
	public void profilePassivate() {
		
		if (state == ProfileObjectState.READY) {

			// FIXME tests/profiles/lifecycle/Test1110227Test.xml enforces profileStore to be called before every profilePassivate(), doing this here to enfore it happens
			profileStore();
			
			if(logger.isDebugEnabled()) {
				logger.debug("[profilePassivate] "+this);
			}

			if (this.profileEntity.getProfileName() != null) {
				state = ProfileObjectState.POOLED;			
			}
			else {
				// FIXME due to tests/profiles/profileabstractclass/Test1110251Test.xml we need to get ridden of the default profile object
				state = ProfileObjectState.DOES_NOT_EXIST;
			}
			
			if (isSlee11) {
				try {
					profileConcrete.profilePassivate();
				}
				catch (RuntimeException e) {
					runtimeExceptionOnProfileInvocation(e);
				}
			}
			
		}
		
		this.profileEntity = null;
		this.profileEntitySnapshot = null;
		
	}

	/**
	 * 
	 */
	public void profileRemove(boolean invokeConcreteSbb) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileRemove] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}
				
		if (isSlee11 && invokeConcreteSbb) {
			try {
				profileConcrete.profileRemove();
			}
			catch (RuntimeException e) {
				runtimeExceptionOnProfileInvocation(e);
			}			
		}
		
		if (profileTable.doesFireEvents() && profileEntity.getProfileName() != null && profileEntitySnapshot != null) {
			// fire event
			AbstractProfileEvent event = new ProfileRemovedEventImpl(profileEntity);
			if (logger.isDebugEnabled()) {
				logger.debug("firing profile removed event for profile named "+profileEntity);
			}
			profileTable.getActivityContext().fireEvent(event.getEventTypeID(), event,
					event.getProfileAddress(), null, EventFlags.NO_FLAGS);
		}
		
		profileEntityFramework.removeprofile(profileEntity);
		
		state = ProfileObjectState.POOLED;
		
		this.profileEntity = null;
		this.profileEntitySnapshot = null;
					
	}

	/**
	 * Invoked when pool removes object
	 */
	public void unsetProfileContext() {

		if(logger.isDebugEnabled())	{
			logger.debug("[unsetProfileContext] "+this);
		}

		if (state == ProfileObjectState.POOLED) {

			final ClassLoader oldClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();

			// FIXME: is this needed ?
			try
			{
				final ClassLoader cl = profileTable.getProfileSpecificationComponent().getClassLoader();
				if (System.getSecurityManager()!=null)
				{
					AccessController.doPrivileged(new PrivilegedAction() {
						public Object run()
						{
							Thread.currentThread().setContextClassLoader(cl);
							return null;
						}
					});
				}
				else
				{
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
			finally
			{
				if (System.getSecurityManager()!=null)
				{
					AccessController.doPrivileged(new PrivilegedAction() {
						public Object run()
						{
							Thread.currentThread().setContextClassLoader(oldClassLoader);
							return null;
						}
					});
				}
				else
				{
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
				profileLocalObject = (ProfileLocalObject) profileLocalObjectConcreteClass.getConstructor(ProfileObject.class).newInstance(this);
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

	/**
	 * 
	 */
	private void loadProfileEntity(String profileName) throws UnrecognizedProfileNameException {
		profileEntity = profileEntityFramework.retrieveProfile(profileTable.getProfileTableName(), profileName);
		if (profileEntity == null) {
			throw new UnrecognizedProfileNameException();
		}		
		profileEntity.setReadOnly(readOnlyProfileTable);		
	}
	
	/**
	 * 
	 */
	private void persistProfileConcrete() {
		if (profileEntity.isCreate()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Persisting "+this);
				
			}			
			profileEntityFramework.persistProfile(profileEntity);			
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
				if (profileTable.doesFireEvents() && profileEntity.getProfileName() != null) {
					// Fire a Profile Added or Updated Event
					ActivityContext ac = profileTable.getActivityContext();
					AbstractProfileEvent event = null;
					if (profileEntity.isCreate()) {
						event = new ProfileAddedEventImpl(profileEntity);	
						if (logger.isDebugEnabled()) {
							logger.debug("firing profile added event for profile named "+profileEntity);
						}
					}
					else {
						event = new ProfileUpdatedEventImpl(profileEntitySnapshot,profileEntity);		
						if (logger.isDebugEnabled()) {
							logger.debug("firing profile updated event for profile named "+profileEntity);
						}
					}
					ac.fireEvent(event.getEventTypeID(), event,
							event.getProfileAddress(), null, EventFlags.NO_FLAGS);
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
				profileCmpSlee10Wrapper = (AbstractProfileCmpSlee10Wrapper) profileTable.getProfileSpecificationComponent().getProfileCmpSlee10WrapperClass().getConstructor(ProfileObject.class).newInstance(this);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		return profileCmpSlee10Wrapper;
	}
	
}
