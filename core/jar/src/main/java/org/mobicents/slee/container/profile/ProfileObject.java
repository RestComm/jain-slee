package org.mobicents.slee.container.profile;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.resource.EventFlags;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.facilities.profile.AbstractProfileEvent;
import org.mobicents.slee.runtime.facilities.profile.ProfileAddedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileRemovedEventImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileUpdatedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

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
	private final ProfileTableConcrete profileTableConcrete;
	
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
	 * 
	 */
	private boolean invokingProfilePostCreate = false;
	
	/**
	 * indicates if the object is related with a slee 1.1 profile spec or not
	 */
	private final boolean isSlee11;
	
	/**
	 * the entity holding cmp data of currently assigned profile
	 */
	private ProfileEntity profileEntity;
	
	/**
	 * local representation for this profile object
	 */
	private ProfileLocalObject profileLocalObject = null;
	
	/**
	 * 
	 */
	private final boolean readOnlyProfileTable;
	
	/**
	 * 
	 */
	private boolean addedTxActionsToReleaseObject = false;
	
	/**
	 * 
	 * @param profileTableConcrete
	 * @param profileSpecificationId
	 * @throws NullPointerException
	 * @throws SLEEException
	 */
	public ProfileObject(ProfileTableConcrete profileTableConcrete) throws NullPointerException, SLEEException {
		
		if (profileTableConcrete == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		
		this.profileTableConcrete = profileTableConcrete;
		
		final ProfileSpecificationComponent component = profileTableConcrete.getProfileSpecificationComponent();
		
		this.profileReentrant = component.getDescriptor().getProfileAbstractClass() == null ? false : this.profileTableConcrete.getProfileSpecificationComponent().getDescriptor().getProfileAbstractClass().getReentrant();
		this.readOnlyProfileTable = component.getDescriptor().getReadOnly();
		this.isSlee11 = profileTableConcrete.getProfileSpecificationComponent().isSlee11();
		
		try {
			this.profileConcrete = (ProfileConcrete) component.getProfileCmpConcreteClass().newInstance();
			this.profileConcrete.setProfileObject(this);		
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
		}
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
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		
		final ClassLoader oldClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();

		// FIXME: is this needed ?
		try
		{
			final ClassLoader cl = this.profileTableConcrete.getProfileSpecificationComponent().getClassLoader();
			
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
					profileConcrete.setProfileContext(profileContext);
				}
				this.profileContext.setProfileObject(this);
				state = ProfileObjectState.POOLED;
			}
			catch (Exception e) {
				if (logger.isDebugEnabled())
					logger.debug("Exception encountered while setting profile context for profile table: "
							+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID(), e);
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
		
		try {
			if (profileName == null) {
				// default profile creation
				// create instance of entity
				profileEntity = (ProfileEntity) profileTableConcrete.getProfileSpecificationComponent().getProfileEntityClass().newInstance();
				profileEntity.setProfileName(null);
				profileEntity.setTableName(profileTableConcrete.getProfileTableName());
				// invoke life cycle method on profile
				profileConcrete.profileInitialize();
			}
			else {
				// load the default profile entity
				loadProfileEntity(null);
				// clone it and change it's name
				profileEntity = profileEntity.cl0ne();
				profileEntity.setProfileName(profileName);				
			}
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
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
		
		if (this.state != ProfileObjectState.POOLED) {
			throw new SLEEException(this.toString());
		}
				
		if (isSlee11) {
			this.invokingProfilePostCreate = true;
			try {
				this.profileConcrete.profilePostCreate();
			}
			finally {
				this.invokingProfilePostCreate = false;
			}
		}
		
		this.state = ProfileObjectState.READY;
	}
	
	/**
	 * 
	 * Activates the profile object and use a specific snapshot of a profile entity
	 * @param snapshot
	 */
	public void profileActivate(ProfileEntity snapshot) {
		if (!snapshot.isReadOnly()) {
			throw new SLEEException("can only assign profile entities without creation or load when read only");
		}
		profileActivate();
		this.profileEntity = snapshot;
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
			profileConcrete.profileActivate();
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
		loadProfileEntity(profileName);
		// create a snapshot copy if the profile table fires events
		if (profileTableConcrete.doesFireEvents()) {
			profileEntitySnapshot = profileEntity.cl0ne();
			profileEntitySnapshot.setReadOnly(true);						
		}
		this.profileConcrete.profileLoad();
	}

	/**
	 * 
	 * @throws ProfileVerificationException
	 */
	public void profileVerify() throws ProfileVerificationException {
		if(logger.isDebugEnabled()) {
			logger.debug("[profileVerify] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}

		if(!isSlee11 || profileEntity.getProfileName() != null) {
			// only invoke when it is a slee 1.0 profile or a non default slee 1.1 profile
			profileConcrete.profileVerify();
		}
	}
	
	/**
	 * 
	 */
	private void profileStore() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileStore] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}

		profileConcrete.profileStore();
		
		if (profileEntity.isDirty()) {
			
			persistProfileConcrete();
			// check the table fires events and the object is not assigned to a default profile
			if (profileTableConcrete.doesFireEvents() && profileEntity.getProfileName() != null) {
				// Fire a Profile Added or Updated Event
				ActivityContext ac = profileTableConcrete.getActivityContext();
				AbstractProfileEvent event = null;
				if (profileEntity.isCreate()) {
					event = new ProfileAddedEventImpl(profileEntity);					
				}
				else {
					event = new ProfileUpdatedEventImpl(profileEntitySnapshot,profileEntity);					
				}
				ac.fireEvent(event.getEventTypeID(), event,
						event.getProfileAddress(), null, EventFlags.NO_FLAGS);
				// reset snapshot clone
				profileEntitySnapshot = profileEntity.cl0ne();	
				profileEntitySnapshot.setReadOnly(true);
			}
			profileEntity.setDirty(false);
		}
	}
	
	/**
	 * 
	 */
	public void profilePassivate() {
		
		if (state != ProfileObjectState.DOES_NOT_EXIST) {
			
			profileStore();

			if(logger.isDebugEnabled()) {
				logger.debug("[profilePassivate] "+this);
			}

			if (state != ProfileObjectState.READY) {
				throw new SLEEException(this.toString());
			}		

			if (isSlee11) {
				this.profileConcrete.profilePassivate();
			}
		}
		returnToProfileTable();
	}

	/**
	 * 
	 */
	public void profileRemove() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("[profileRemove] "+this);
		}
		
		if (state != ProfileObjectState.READY) {
			throw new SLEEException(this.toString());
		}
				
		if (isSlee11) {
			profileConcrete.profileRemove();
		}
		
		if (profileTableConcrete.doesFireEvents() && profileEntity.getProfileName() != null && profileEntitySnapshot != null) {
			// fire event
			AbstractProfileEvent event = new ProfileRemovedEventImpl(profileEntity);					
			profileTableConcrete.getActivityContext().fireEvent(event.getEventTypeID(), event,
					event.getProfileAddress(), null, EventFlags.NO_FLAGS);
		}
		
		ProfileDataSource.INSTANCE.removeprofile(this);
		
		returnToProfileTable();
			
	}
	
	private void returnToProfileTable() {
		
		this.profileEntity = null;
		this.profileEntitySnapshot = null;
		this.profileLocalObject = null;
		this.addedTxActionsToReleaseObject = false;
		
		if (state == ProfileObjectState.READY) {
			state = ProfileObjectState.POOLED;			
		}
		profileTableConcrete.returnProfileObject(this);
		
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
				final ClassLoader cl = profileTableConcrete.getProfileSpecificationComponent().getClassLoader();
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
					profileConcrete.unsetProfileContext();
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
	 * 
	 */
	public void releaseProfileObjectOnTxEnd() {
		
		if (!addedTxActionsToReleaseObject) {
			TransactionalAction action1 = new TransactionalAction() {
				public void execute() {
					if (state == ProfileObjectState.READY) {
						if (profileEntity.isReadOnly()) {
							if (profileEntity.isRemove()) {
								profileRemove();
							}
							else {
								profilePassivate();
							}
						}
						else {
							profilePassivate();
						}
					}
				}
			};
			SleeTransactionManager txManager = profileTableConcrete.getSleeContainer().getTransactionManager();
			try {
				txManager.addBeforeCommitPriorityAction(action1);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			// an action to passivate only if rollback occurs (if commit was invoked action 1 already was executed)
			TransactionalAction action2 = new TransactionalAction() {
				public void execute() {
					if (state == ProfileObjectState.READY) {
						profilePassivate();
					}
				}
			};
			try {
				txManager.addAfterRollbackAction(action2);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}	
			addedTxActionsToReleaseObject = true;
		}
	}
	
	/**
	 * Retrieves the local representation for this profile object
	 * @return
	 */
	public ProfileLocalObject getProfileLocalObject() {
		if (profileLocalObject == null) {
			final Class<?> profileLocalObjectConcreteClass = profileTableConcrete.getProfileSpecificationComponent().getProfileLocalObjectConcreteClass();
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
			// lets ensure the profile object is released
			releaseProfileObjectOnTxEnd();
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
	public boolean isInvokingProfilePostCreate() {
		return invokingProfilePostCreate;
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
	public ProfileTableConcrete getProfileTableConcrete() {
		return profileTableConcrete;
	}

	/**
	 * 
	 */
	private void loadProfileEntity(String profileName) throws UnrecognizedProfileNameException {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading profile with name "+profileName+" on object "+this);

		}
		profileEntity = ProfileDataSource.INSTANCE.retrieveProfile(getProfileTableConcrete(), profileName);
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
			ProfileDataSource.INSTANCE.persistProfile(this);			
		}		
	}
	
	public String toString() {
		return "ProfileObject( table= "+profileTableConcrete.getProfileTableName()+" , state = "+state+" , entity = "+ profileEntity+" )";
	}

	/**
	 * use this method to move the object to {@link ProfileObjectState#DOES_NOT_EXIST} state and indicate to the pool that the object should be discarded
	 */
	public void invalidateObject() {
		state = ProfileObjectState.DOES_NOT_EXIST;		
	}
}
