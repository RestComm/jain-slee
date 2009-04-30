package org.mobicents.slee.container.profile;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.profile.ProfileMBean;
import javax.slee.profile.ProfileVerificationException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Start time:12:23:15 2009-03-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This is stub class, that is extended and instrumented to allow access to
 * underlying Profile
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
public abstract class AbstractProfileMBean extends StandardMBean implements ProfileMBean, NotificationSource {

	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(AbstractProfileMBean.class);
	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
  	public static final String _PROFILE_OBJECT = "profileObject";
	public static final String _CHECK_WRITE_ACCESS = "checkWriteAccess();";
	
	/**
	 * the profile object assigned to this mbean
	 */
	protected final ProfileObject profileObject;
	
	/**
	 * the object name used to register this mbean
	 */
	private final ObjectName objectName;
	
	/**
	 * 
	 * @param mbeanInterface
	 * @param profileObject
	 * @throws NotCompliantMBeanException
	 */
	public AbstractProfileMBean(Class<?> mbeanInterface, ProfileObject profileObject) throws NotCompliantMBeanException, ManagementException {
	  super(mbeanInterface);
	  this.profileObject = profileObject;
	  // register the mbean
	  try {
		  this.objectName = ProfileTableImpl.generateProfileMBeanObjectName(profileObject.getProfileTableConcrete().getProfileTableName(), profileObject.getProfileName());
		  sleeContainer.getMBeanServer().registerMBean(this, objectName);
	  } catch (Throwable e) {
		  throw new ManagementException(e.getMessage(),e);
	  }
	}

	/**
	 * Retrieves the object name used to register this mbean.
	 * @return
	 */
	public ObjectName getObjectName() {
		return objectName;
	}
	
  // #################
  // # MBean methods #
  // #################

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileMBean#closeProfile()
	 */
	public void closeProfile() throws InvalidStateException, ManagementException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("[closeProfile] on: " + this.profileObject.getProfileName() + ", from table:"  +this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try
		{
			/*
			 * SLEE 1.1 spec, 10.26.3.4 closeProfile method
       *
			 * The Administrator invokes the closeProfile method when the
			 * Administrator no longer requires access to the Profile MBean
			 * object. The implementation of this method is free to deregister
			 * the Profile MBean object from the MBean Server. ( but if you do
			 * this then test # 4386 will fail! )
			 */
			if (logger.isDebugEnabled()) {
				logger.debug("closeProfile called (profile =" + this.profileObject.getProfileTableConcrete().getProfileTableName() + "/" + this.profileObject.getProfileName() + ")");
				logger.debug("profileWriteable " + this.isProfileWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
			}
			
			// The closeProfile method must throw a javax.slee.InvalidStateException if the Profile MBean object is in the read-write state.
			if (this.profileObject.isWriteable() && this.isProfileDirty())
				throw new InvalidStateException();
			// unregister mbean
			try {
				sleeContainer.getMBeanServer().unregisterMBean(objectName);
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);
			}
			// passivate profile object
			profileObject.getProfileTableConcrete().deassignProfileObject(profileObject,true);						
		}
		finally {
		  switchContextClassLoader(oldClassLoader);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileMBean#commitProfile()
	 * 
	 * This method commits profile. See descritpion - profile becomes visible for slee ONLY when its commited.
	 */
	public void commitProfile() throws InvalidStateException, ProfileVerificationException, ManagementException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("[commitProfile] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());

		/*
		 * 10.26.3.2 commitProfile method
		 */
		SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		boolean txCreated = sleeTransactionManager.requireTransaction();

		try
		{
			checkWriteAccess();

			if (logger.isDebugEnabled()) {
				logger.debug("commitProfile called (profile =" + getProfileTableName() + "/" + getProfileName() + ")");
				logger.debug("profileWriteable " + this.isProfileWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
			}

			// not storing default profile
			// getting last commited profile in case of update
			
			// FIXME: Alexandre: [DONE] Get profile from backend storage?
			ProfileLocalObjectConcrete profileBeforeUpdate = (ProfileLocalObjectConcrete) JPAUtils.INSTANCE.find( this.getProfileTableName(), this.getProfileName() );

			boolean wasProfileInBackEndStorage = false;
			try {
				wasProfileInBackEndStorage = this.profileObject.getProfileTableConcrete().profileExists(this.profileObject.getProfileName());
			}
			catch (Exception e) {
				throw new ManagementException("Unable to verify if profile is in Back-end storage.", e);
			}
			//
			// if
			// (this.profileObject.getProfileConcrete().isProfileInBackEndStorage())
			// {
			// wasProfileInBackEndStorage = true;
			// try {
			// ProfileTableConcreteImpl profileTable =
			// (ProfileTableConcreteImpl)
			// this.sleeProfileManagement.getProfileTable(this.getProfileTableName(),
			// this.getProfileSpecificationComponent()
			// .getProfileSpecificationID());
			// profileBeforeUpdate = (ProfileLocalObjectConcrete)
			// profileTable.find(this.getProfileName(), true);
			// profileBeforeUpdate.setSnapshot();
			//
			// } catch (Exception e1) {
			// throw new
			// ManagementException("Failed instantiateLastCommittedProfile ",
			// e1);
			// }
			// }
			/*
			 * The implementation of this method must also verify that the
			 * constraints specified by the Profile Specification?s deployment
			 * descriptor, such as the uniqueness constraints placed on indexed
			 * attributes. The SLEE verifies these constraints after it invokes
			 * the profileVerify method of the Profile Management object. If any
			 * constraint is violated, then this method throws a javax.slee.
			 * profile.ProfileVerificationException, the commit attempt fails,
			 * and the Profile MBean object must remain in the read-write state.
			 */

			try {
				this.profileObject.profileStore();
			}
			catch (Exception e) {
				logger.error("Failure trying to store profile.", e);
				if (e instanceof ProfileVerificationException)
					throw (ProfileVerificationException) e;
				throw new ManagementException(e.getMessage());
			}

			/*
			 * The implementation of this method must invoke the profileVerify
			 * method of the Profile Management object that caches the
			 * persistent state of the Profile, and only commit the changes if
			 * the profileVerify method returns without throwing an exception.
			 * If the profileVerify method throws a
			 * javax.slee.profile.ProfileVerificationException, the commit
			 * attempt should fail, the exception is forwarded back to the
			 * management client, and the Profile MBean object must remain in
			 * the read-write state.
			 */
			// FIXME: add different check?
			if (this.profileObject.getProfileName() != null)
			{
				try
				{
					this.profileObject.profileVerify();
				}
				catch (Exception e) {
					throw new ProfileVerificationException(e.getMessage());
				}
			}
			/*
			 * if(profileVerificationException!=null){ removeException=true;
			 * throw profileVerificationException; }
			 */

			try {
				// persist transient state
				this.profileObject.getProfileConcrete().commitChanges();
			}
			catch (Exception e) {
				logger.error("Failed commitProfile, profileStore()", e);
				if (e instanceof ProfileVerificationException)
					throw (ProfileVerificationException) e;
				else
					throw new ManagementException("Failed commitProfile, profileStore()", e);
			}

			// FIXME:THIS SHOULD NOT BE DONE LIKE THAT!!!
			// Fire a Profile Added or Updated Event

			Address profileAddress = new Address(AddressPlan.SLEE_PROFILE, getProfileTableName() + "/" + getProfileName());
			ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory;

			profileTableActivityContextInterfaceFactory = sleeContainer.getProfileTableActivityContextInterfaceFactory();
			if (profileTableActivityContextInterfaceFactory == null) {
				final String s = "got NULL ProfileTable ACI Factory";
				logger.error(s);
				throw new ManagementException(s);
			}

			ProfileTableActivityImpl profileTableActivity = new ProfileTableActivityImpl(new ProfileTableActivityHandle(getProfileTableName()));
			ActivityContextInterfaceImpl activityContextInterface;
			try
			{
				activityContextInterface = (ActivityContextInterfaceImpl) profileTableActivityContextInterfaceFactory.getActivityContextInterface(profileTableActivity);
			}
			catch (Exception e) {
				throw new ManagementException("Failed committing profile", e);
			}
			
			if (!wasProfileInBackEndStorage)
			{
				// FIXME: Alexandre: [DONE] Allocate new instance of PLO
				ProfileLocalObjectConcrete ploc = (ProfileLocalObjectConcrete) JPAUtils.INSTANCE.find( this.getProfileTableName(), this.getProfileName() );
				this.profileObject.getProfileTableConcrete().fireProfileAddedEvent(ploc);
			}
			else
			{
				// FIXME: Alexandre: [DONE] Allocate PLO
				ProfileLocalObjectConcrete plocAfterUpdate = (ProfileLocalObjectConcrete) JPAUtils.INSTANCE.find( this.getProfileTableName(), this.getProfileName() );
				this.profileObject.getProfileTableConcrete().fireProfileUpdatedEvent( profileBeforeUpdate, plocAfterUpdate);
			}

			// so far so good, time to commit the tx so that the profile is
			// visible in the SLEE
			try {
				if (txCreated)
					sleeTransactionManager.commit();
			}
			/*
			 * If a commit fails due to a system-level failure, the
			 * implementation of this method should throw a
			 * javax.slee.management.ManagementException to report the
			 * exceptional situation to the management client. The Profile MBean
			 * object should continue to remain in the read-write state.
			 */
			catch (Exception e) {
				logger.error("Failed committing profile", e);
				
				try
				{
				  sleeTransactionManager.rollback();
				}
				catch (SystemException se) {
					logger.error("Failed rolling back profile: " + getProfileTableName() + "/" + getProfileName(), se);
					throw new ManagementException(e.getMessage());
				}
				
				throw new ManagementException(e.getMessage());
			}
			/*
			 * If a commit succeeds, the Profile MBean object should move to the
			 * read-only state. The SLEE must also fire a Profile Updated Event
			 * if a Profile has been updated (see Section 1.1). The dirty flag
			 * in the Profile Management object must also be set to false upon a
			 * successful commit.
			 */
			this.profileObject.setWriteable(false);
			// its done elsewhere
			// this.modified/dirty = false;

			if (logger.isDebugEnabled()) {
				logger.debug("profileWriteable " + this.profileObject.isWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
				logger.debug("commitProfile call ended");
			}

		}
		finally
		{
			// FIXME: is this ok to set it befiore rollback?
			switchContextClassLoader(oldClassLoader);
			
			try
			{
				// if the tx was not completed by now, then there was an
				// exception and it should roll back
				if (txCreated && sleeTransactionManager.getTransaction() != null)
				{
				  sleeTransactionManager.rollback();
				}
			}
			catch (SystemException se) {
				logger.error("Failed completing profile commit due to TX access problem. Profile : " + getProfileTableName() + "/" + getProfileName(), se);
				throw new ManagementException(se.getMessage());
			}
		}
	}

	public void editProfile() throws ManagementException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("[editProfile] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try
		{
			/*
			 * The Administrator invokes the editProfile method to obtain
			 * read-write access to the Profile MBean object (if the
			 * Administrator currently has read-only access to the Profile MBean
			 * object).
			 */
			if (logger.isDebugEnabled()) {
				logger.debug("editProfile called (profile =" + this.profileObject.getProfileTableConcrete().getProfileTableName() + "/" + this.profileObject.getProfileName() + ")");
				logger.debug("profileWriteable " + this.profileObject.isWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
			}

			if (!this.profileObject.isWriteable())
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("starting new Transaction and editing profile");
				}
				/*
				 * The implementation of this method should start a new
				 * transaction for the editing session, or perform the
				 * equivalent function.
				 */
				// sleeProfileManager.startTransaction(profileKey);
				// sleeProfileManager.startTransaction();
				// boolean b = txManager.requireTransaction();
				this.profileObject.setWriteable(true);
				this.profileObject.profileLoad();
				// if ( b ) txManager.commit();
			}
			/*
			 * If the Profile MBean object is already in the read-write state
			 * when this method is invoked, this method has no further effect
			 * and returns silently.
			 */
			else
			{
        if (logger.isDebugEnabled())
        {
				  logger.debug("profile already in the read/write state");
        }
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("profileWriteable " + this.profileObject.isWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
				logger.debug("editProfile call ended");
			}
		}
		finally
		{
			switchContextClassLoader(oldClassLoader);
		}
	}

	public boolean isProfileDirty() throws ManagementException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("[isProfileDirty] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		/*
		 * The isProfileDirty method returns true if the Profile MBean object is
		 * in the read-write state and the dirty flag of the Profile Management
		 * object that caches the persistent state of the Profile returns true.
		 * This method returns false under any other situation.
		 */
		if (logger.isDebugEnabled())
		{
			logger.debug("isProfileDirty called (profile=" + this.profileObject.getProfileTableConcrete().getProfileTableName() + "/" + this.profileObject.getProfileName() + ")");
		}
		
		return (this.profileObject.isWriteable() && this.profileObject.getProfileConcrete().isProfileDirty());
	}

	public boolean isProfileWriteable() throws ManagementException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("[isProfileWriteable] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		/*
		 * The isProfileWriteable method returns true if the Profile MBean
		 * object is in the read-write state, or false if in the read-only
		 * state.
		 */
		return this.profileObject.isWriteable();
	}

	public void restoreProfile() throws InvalidStateException, ManagementException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("[restoreProfile] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		}

		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try
		{
			/*
			 * The Administrator invokes the restoreProfile method if the
			 * Administrator wishes to discard changes made to the Profile
			 * Management object that caches the persistent state of a Profile.
			 * The implementation of this method rolls back any changes that
			 * have been made to the Profile Management object since the Profile
			 * MBean object entered the read-write state and moves the Profile
			 * MBean object to the read-only state. If the Profile MBean object
			 * was returned by the createProfile method (see Section 14.11),
			 * then no new Profile is created since the transaction will not
			 * commit The execution of this method must begin in the same
			 * transaction context as that begun by the createProfile or
			 * editProfile invocation that initiated the editing session, but
			 * must roll back the transaction before returning.
			 */
			if (logger.isDebugEnabled()) {
				logger.debug("restoreProfile called (profile =" + this.profileObject.getProfileTableConcrete().getProfileTableName() + "/" + this.profileObject.getProfileName() + ")");
				logger.debug("profileWriteable " + this.profileObject.isWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
			}
			/*
			 * The restoreProfile method must throw a
			 * javax.slee.InvalidStateException if the Profile MBean object is
			 * not in the read-write state.
			 */
			if (!this.profileObject.isWriteable())
			{
				throw new InvalidStateException("The restoreProfile method must throw a javax.slee.InvalidStateException if the Profile MBean object is not in the read-write state.");
			}

			// FIXME: Alexandre: What to do here?
			// if(!commited)
			// {
			// REMOVE
			// }

			this.profileObject.profileLoad();
			this.profileObject.setWriteable(false);
			
			if (logger.isDebugEnabled()) {
				logger.debug("profileWriteable " + this.profileObject.isWriteable());
				logger.debug("dirtyFlag " + this.isProfileDirty());
				logger.debug("restoreProfile call ended");
			}
		}
		finally
		{
			switchContextClassLoader(oldClassLoader);
		}
	}

	protected void checkWriteAccess() throws IllegalStateException {
		if (!this.profileObject.isWriteable())
			throw new IllegalStateException("ProfileObject is not in write state");
	}

	protected String getProfileName() {
		return this.profileObject.getProfileName();
	}

	protected String getProfileTableName() {
		return this.profileObject.getProfileTableConcrete().getProfileTableName();
	}

  protected ClassLoader switchContextClassLoader(ClassLoader newClassLoader)
  {
    Thread t = Thread.currentThread();
    ClassLoader oldClassLoader = t.getContextClassLoader();
    t.setContextClassLoader(newClassLoader);
    
    return oldClassLoader;
  }
}
