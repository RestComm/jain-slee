package org.mobicents.slee.container.profile;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.resource.EventFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;
import org.mobicents.slee.runtime.activity.ActivityContext;
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
	private ProfileConcrete profileConcrete;
	
	/**
	 * a snapshot copy of the current profile concrete, before updates, needed
	 * for profile table events
	 */
	private ProfileConcrete profileConcreteSnapshot;
	
	/**
	 * the context of the profile object
	 */
	private ProfileContextImpl profileContext = null;

	/**
	 * indicates if a profile persistent state was changed since load
	 */
	private boolean profileDirty = false;

	/**
	 * inidcates if this profile can be invoked more than once in a call tree for a single tx, thus exposed to loops
	 */
	private final boolean profileReentrant;
	
	/**
	 * indicates if the persisted state can be modified using the object 
	 */
	private boolean profileReadOnly = false;
	
	/**
	 * indicates if the profile is being created or not
	 */
	private boolean profileCreation = false;
	
	/**
	 * the profile name currently assigned to the object
	 */
	private String profileName;
	
	private boolean invokingProfilePostCreate = false;
	
	/**
	 * 
	 * @param profileTableConcrete
	 * @param profileSpecificationId
	 * @throws NullPointerException
	 * @throws SLEEException
	 */
	public ProfileObject(ProfileTableConcrete profileTableConcrete, ProfileSpecificationID profileSpecificationId) throws NullPointerException, SLEEException {
		
		if (profileTableConcrete == null || profileSpecificationId == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		
		this.profileTableConcrete = profileTableConcrete;
		this.profileReentrant = this.profileTableConcrete.getProfileSpecificationComponent().getDescriptor().getProfileAbstractClass() == null ? false : this.profileTableConcrete.getProfileSpecificationComponent().getDescriptor().getProfileAbstractClass().getReentrant();
		
		// there must be always a profile concrete object in the object, be it assigned or not with a specific profile
		try {
			this.profileConcrete = (ProfileConcrete) this.profileTableConcrete.getProfileSpecificationComponent().getProfileCmpConcreteClass().newInstance();
			this.profileConcrete.setProfileObject(this);
			this.profileConcrete.setTableName(profileTableConcrete.getProfileTableName());
		}
		catch (Throwable e) {
			throw new SLEEException("Unexpected exception creating concrete class for profile from table: " + this.profileTableConcrete.getProfileTableName()
					+ " and specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID(), e);
		}
	}

	public boolean isInvokingProfilePostCreate() {
		return invokingProfilePostCreate;
	}
	
	public boolean isProfileCreation() {
		return profileCreation;
	}
	
	/**
	 * 
	 * @param profileName
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
		if (profileConcrete != null) {
			profileConcrete.setProfileName(profileName);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProfileName() {
		return this.profileName;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationComponent getProfileSpecificationComponent() {
		return profileTableConcrete.getProfileSpecificationComponent();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isProfileDirty() {
		return profileDirty;
	}
	
	/**
	 * 
	 * @param dirty
	 */
	public void setProfileDirty(boolean dirty) {
		this.profileDirty = dirty;
	}

	/**
	 * 
	 * @param profileReadOnly
	 */
	public void setProfileReadOnly(boolean profileReadOnly) {
		this.profileReadOnly = profileReadOnly;
	}

	/**
	 * Indicates if the profile object can be used to modify the persistent state of the profile. 
	 * @return false if the object is currently assigned to an mbean or if it is not read only or if it is default profile
	 */
	public boolean isProfileReadOnly() {
		return profileReadOnly;
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
	public ProfileObjectState getState() {
		return state;
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
	 * @return
	 */
	public ProfileConcrete getProfileConcrete() {
		return profileConcrete;
	}

	/**
	 * 
	 * @return
	 */
	public ProfileContextImpl getProfileContext() {
		return profileContext;
	}

	/**
	 * 
	 */
	public void profileActivate() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileActivate] "+this);
		}
		
		if (this.state != ProfileObjectState.POOLED)
		{
			logger.error("Profile initialize, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		this.profileCreation = false;
		this.profileConcrete.profileActivate();
		this.state = ProfileObjectState.READY;
	}

	/**
	 * 
	 */
	public void profileInitialize() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileInitialize] "+this);
		}
		// this is called for default profile when its created

		if (this.state != ProfileObjectState.POOLED) {
			logger.error("Profile initialize, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		
		// invoke life cycle method on profile
		this.profileConcrete.profileInitialize();
		setProfileDirty(true);
	}

	/**
	 * initialize state from default profile
	 */
	public void loadFromDefaultProfile() {
		try {
			// load the default profile
			this.loadProfileConcrete("");
			// clone it and change it's name
			this.profileConcrete = this.profileConcrete.cl0ne();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		this.profileConcrete.setProfileName(this.profileName);
		// turn on dirty flag
		setProfileDirty(true);
	}
	
	/**
	 * 
	 */
	public void profileLoad() throws UnrecognizedProfileNameException {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileLoad] "+this);
		}
		if (this.state != ProfileObjectState.READY) {
			logger.error("Profile load, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		// load profile concrete from data source
		loadProfileConcrete(this.getProfileName());
		// create a snapshot copy if the profile table fires events
		if (profileTableConcrete.doesFireEvents()) {
			try {
				this.profileConcreteSnapshot = this.profileConcrete.cl0ne();
			} catch (CloneNotSupportedException e) {
				throw new SLEEException(e.getMessage(),e);
			}		
		}
		this.profileConcrete.profileLoad();
		setProfileDirty(false);
	}

	/**
	 * 
	 */
	public void profilePassivate() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profilePassivate] "+this);
		}
		// This must be called
		if (this.state != ProfileObjectState.READY) {
			logger.error("Profile passivate, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}

		this.profileConcrete.profilePassivate();
		this.state = ProfileObjectState.POOLED;

	}

	/**
	 * 
	 * @throws CreateException
	 */
	public void profilePostCreate() throws CreateException {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profilePostCreate] "+this);
		}
		if (this.state != ProfileObjectState.POOLED) {
			logger.error("Profile post create, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		this.profileCreation = true;
		this.invokingProfilePostCreate = true;
		try {
			this.profileConcrete.profilePostCreate();
			this.state = ProfileObjectState.READY;
		}
		finally {
			this.invokingProfilePostCreate = false;
		}
	}

	/**
	 * 
	 */
	public void profileRemove() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileRemove] "+this);
		}
		if (this.state != ProfileObjectState.READY) {
			logger.error("Profile remove, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}

		this.profileConcrete.profileRemove();
		if (profileTableConcrete.doesFireEvents() && profileConcreteSnapshot != null) {
			// fire event
			if (logger.isDebugEnabled()) {
				logger.debug("[fireProfileRemovedEvent]"
						+ " on: " + this);
			}
			AbstractProfileEvent event = new ProfileRemovedEventImpl(profileConcrete);					
			profileTableConcrete.getActivityContext().fireEvent(event.getEventTypeID(), event,
					event.getProfileAddress(), null, EventFlags.NO_FLAGS);
		}
		// TODO remove profile
		this.state = ProfileObjectState.POOLED;
	}

	/**
	 * 
	 */
	public void profileStore() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileStore] "+this);
		}
		if (this.getState() != ProfileObjectState.READY) {
			logger.error("Profile store, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}

		this.profileConcrete.profileStore();
		
		if (isProfileDirty()) {
			
			persistProfileConcrete();
			
			if (profileTableConcrete.doesFireEvents()) {
				// Fire a Profile Added or Updated Event
				ActivityContext ac = profileTableConcrete.getActivityContext();
				AbstractProfileEvent event = null;
				if (profileConcreteSnapshot == null) {
					// creation
					if (logger.isDebugEnabled()) {
						logger.debug("[fireProfileAddedEvent]"
								+ " on: " + this);
					}					
					
					event = new ProfileAddedEventImpl(profileConcrete);					
				}
				else {
					if (logger.isDebugEnabled()) {
						logger.debug("[fireProfileUpdatedEvent]"
								+ " on: " + this);
					}										
					event = new ProfileUpdatedEventImpl(profileConcreteSnapshot,profileConcrete);					
				}
				ac.fireEvent(event.getEventTypeID(), event,
						event.getProfileAddress(), null, EventFlags.NO_FLAGS);
				// reset snapshot clone
				try {
					this.profileConcreteSnapshot = this.profileConcrete.cl0ne();
				} catch (CloneNotSupportedException e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
			
			setProfileDirty(false);
		}
		
	}

	/**
	 * 
	 */
	private void loadProfileConcrete(String profileName) throws UnrecognizedProfileNameException {
		//if (logger.isDebugEnabled()) {
		logger.info("Loading "+this);

		//}
		this.profileConcrete = JPAUtils.INSTANCE.retrieveProfile(getProfileTableConcrete(), profileName);
		if (this.profileConcrete == null) {
			throw new UnrecognizedProfileNameException();
		}
		this.profileConcrete.setProfileObject(this);
	}
	
	/**
	 * 
	 */
	private void persistProfileConcrete() {
		//if (logger.isDebugEnabled()) {
			logger.info("Persisting "+this);
			
		//}
		JPAUtils.INSTANCE.persistProfile(this);			
	}
	
	/**
	 * 
	 * @throws ProfileVerificationException
	 */
	public void profileVerify() throws ProfileVerificationException {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileVerify] "+this);
		}
		if (this.state != ProfileObjectState.READY) {
			logger.error("Profile verify, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}

		this.profileConcrete.profileVerify();

	}

	/**
	 * Invoked from pool.
	 * 
	 * @param profileContext
	 */
	public void setProfileContext(ProfileContextImpl profileContext) {
		if(logger.isDebugEnabled())
		{
			logger.debug("[setProfileContext] "+this);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("setProfileContext, for profile: " + this.getProfileName() + ", from profile table: " + this.profileTableConcrete.getProfileTableName() + " with specification: "
					+ this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}

		if (profileContext == null) {
			throw new NullPointerException("Passed context must not be null.");
		}
		
		if (this.state != ProfileObjectState.DOES_NOT_EXIST) {
			throw new IllegalStateException("Wrong state: " + this.state + ",on profile set context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		this.state = ProfileObjectState.POOLED;
		
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
				this.profileConcrete.setProfileContext(this.profileContext);
				this.profileContext.setProfileObject(this);
			}
			catch (Exception e) {
				if (logger.isDebugEnabled())
					logger.debug("Exception encountered while setting profile context for profile: " + this.getProfileName() + ", from profile table: "
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
	 * Invoked when pool removes object
	 */
	public void unsetProfileContext() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[unsetProfileContext] "+this);
		}

		if (this.state != ProfileObjectState.POOLED) {
			if (logger.isDebugEnabled()) {
				logger.debug("unsetProfileContext, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
						+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
			}
			throw new IllegalStateException("unsetProfileContext, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		this.state = ProfileObjectState.DOES_NOT_EXIST;
		
		final ClassLoader oldClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();

		// FIXME: is this needed ?
		try
		{
			final ClassLoader cl = this.profileTableConcrete.getProfileSpecificationComponent().getClassLoader();
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
						
			this.profileConcrete.unsetProfileContext();
			this.profileContext.setProfileObject(null);
			
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
	
	public String toString()
	{
		return this.getClass().getSimpleName()+" State["+this.state+"] ReadOnly["+this.profileReadOnly+"] Profile["+this.getProfileName()+"] Table["+this.profileTableConcrete.getProfileTableName()+"] ID: "+this.hashCode();
	}

	/**
	 * local representation for this profile object
	 */
	private ProfileLocalObject profileLocalObject = null;
	
	/**
	 * Retrieves the local representation for this profile object
	 * @return
	 */
	public ProfileLocalObject getProfileLocalObject() {
		if (profileLocalObject == null) {
			final Class<?> profileLocalObjectConcreteClass = profileTableConcrete.getProfileSpecificationComponent().getProfileLocalObjectConcreteClass();
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
		}
		return profileLocalObject;
	}

	/**
	 * Replaces the profile concrete object currently bound to the object
	 * @param profileConcrete
	 */
	public void setProfileConcrete(ProfileConcrete profileConcrete) {
		this.profileConcrete = profileConcrete;		
	}

}
