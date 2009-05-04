package org.mobicents.slee.container.profile;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileVerificationException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;

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
	 * the context of the profile object
	 */
	private ProfileContextImpl profileContext = null;
	
	/**
	 * if this object is assigned to a mbean 
	 */
	private boolean managementView = false;
		
	/**
	 * This indicates wheather we are service as snapshot, in that case we are
	 * out of sync, and we can not commit. This is used in update, remove
	 * events.
	 */
	private boolean snapshot = false;

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
	private final boolean profileReadOnly;
	
	/**
	 * indicates if the profile is being created or not
	 */
	private boolean profileCreation = false;
	
	/**
	 * the profile name currently assigned to the object
	 */
	private String profileName;
	
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
		this.profileReadOnly = this.profileTableConcrete.getProfileSpecificationComponent().getDescriptor().getReadOnly();
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

	public boolean isProfileCreation() {
		return profileCreation;
	}
	
	/**
	 * 
	 * @param profileName
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
		profileConcrete.setProfileName(profileName);
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
	 * Indicates the object is being used by an mbean
	 * 
	 * @return
	 */
	public boolean isManagementView() {
		return managementView;
	}

	/**
	 * 
	 * @param managementView
	 */
	public void setManagementView(boolean managementView) {
		this.managementView = managementView;
	}

	/**
	 * Indicates if the profile object can be used to modify the persistent state of the profile. 
	 * @return false if the object is currently assigned to an mbean or if it is not read only or if it is default profile
	 */
	public boolean isProfileReadOnly() {
		return profileReadOnly && !isManagementView() && getProfileName() != null;
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
		// TODO alexandre: replace this with copy of state from default profile
		profileInitialize();
		// don't forget to leave this
		setProfileDirty(true);
	}
	
	/**
	 * 
	 */
	public void profileLoad() {
		if(logger.isDebugEnabled())
		{
			logger.debug("[profileLoad] "+this);
		}
		if (this.state != ProfileObjectState.READY) {
			logger.error("Profile load, wrong state: " + this.state + ",on profile unset context operation, for profile: " + this.getProfileName() + ", from profile table: "
					+ this.profileTableConcrete.getProfileTableName() + " with specification: " + this.profileTableConcrete.getProfileSpecificationComponent().getProfileSpecificationID());
		}
		loadCmpFields();
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
		this.profileConcrete.profilePostCreate();
		this.state = ProfileObjectState.READY;
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
			// getting last committed profile in case of update
			ProfileObject profileBeforeUpdate = null;
			/*if (!isProfileCreation()) {
				profileBeforeUpdate = profileTableConcrete.assignAndActivateProfileObject(getProfileName());
			}*/			
			persistCmpFields();
			// Fire a Profile Added or Updated Event
			if (profileBeforeUpdate == null) {
				// creation
				profileTableConcrete.fireProfileAddedEvent(this);
			}
			else {
				profileTableConcrete.fireProfileUpdatedEvent(profileBeforeUpdate, this);
			}
			setProfileDirty(false);
		}
		
	}

	/**
	 * 
	 */
	private void loadCmpFields() {
    //if (logger.isDebugEnabled()) {
    logger.info("Loading "+this);
    
  //}
    this.profileConcrete = JPAUtils.INSTANCE.retrieveProfile(getProfileTableConcrete(),getProfileName());     
	}
	
	/**
	 * 
	 */
	private void persistCmpFields() {
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
		return this.getClass().getSimpleName()+" State["+this.state+"] Snapshot["+this.snapshot+"] Profile["+this.getProfileName()+"] Table["+this.profileTableConcrete.getProfileTableName()+"] ID: "+this.hashCode();
	}

}
