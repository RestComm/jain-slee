package org.mobicents.slee.container.profile;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.profile.ProfileMBean;
import javax.slee.profile.ProfileVerificationException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
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
	private static final Logger logger = Logger.getLogger(AbstractProfileMBean.class);

	protected final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
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
	 * the transaction being used to edit the underlying profile, if any
	 */
	private Transaction transaction;
	
	/**
	 * 
	 * @param mbeanInterface
	 * @param profileObject
	 * @throws NotCompliantMBeanException
	 */
	public AbstractProfileMBean(Class<?> mbeanInterface, ProfileObject profileObject) throws NotCompliantMBeanException, ManagementException {
	  super(mbeanInterface);
	  this.profileObject = profileObject;
	  profileObject.setManagementView(true);
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
	
	/**
	 * Indicates the mbean is for a new profile
	 * @throws ManagementException 
	 */
	public void createProfile() throws SLEEException, ManagementException {
		if (!isProfileWriteable()) {
			// suspend the transaction
			try {
				transaction = sleeContainer.getTransactionManager().suspend();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
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
		//if(logger.isDebugEnabled()) {
			logger.info("[closeProfile] on: " + this.profileObject.getProfileName() + ", from table:"  +this.profileObject.getProfileTableConcrete().getProfileTableName());
		//}
		
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
			if (this.isProfileWriteable() && this.isProfileDirty())
				throw new InvalidStateException();
			// unregister mbean
			try {
				sleeContainer.getMBeanServer().unregisterMBean(objectName);
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);
			}
			profileObject.setManagementView(false);
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
	public void commitProfile() throws InvalidStateException, ProfileVerificationException, ManagementException {
		
		//if(logger.isDebugEnabled()) {
			logger.info("[commitProfile] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		//}
		
		if (!this.isProfileWriteable())
			throw new IllegalStateException("not in write state");
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());

		final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		
		try	{
			// resume transaction
			try {
				sleeTransactionManager.resume(transaction);
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);
			}
			// if not the default profile then invoke profileVerify()
			if (!this.profileObject.getProfileName().equals(SleeProfileTableManager.DEFAULT_PROFILE_DB_NAME)) {
				this.profileObject.profileVerify();				
			}
			// getting last committed profile in case of update
			ProfileLocalObjectConcrete profileBeforeUpdate = (ProfileLocalObjectConcrete) JPAUtils.INSTANCE.find( this.getProfileTableName(), this.getProfileName() );
			// persist new state
			try {
				this.profileObject.profileStore();
			}
			catch (Exception e) {
				logger.error("Failure trying to store profile.", e);
				if (e instanceof ProfileVerificationException)
					throw (ProfileVerificationException) e;
				throw new ManagementException(e.getMessage());
			}

			// FIXME:THIS SHOULD NOT BE DONE LIKE THAT!!!
			// FIXME: emmartins : wtf is the comment above? 
			// Fire a Profile Added or Updated Event
			Address profileAddress = new Address(AddressPlan.SLEE_PROFILE, getProfileTableName() + "/" + getProfileName());
			ProfileTableActivityContextInterfaceFactoryImpl profileTableActivityContextInterfaceFactory = sleeContainer.getProfileTableActivityContextInterfaceFactory();
			if (profileTableActivityContextInterfaceFactory == null) {
				final String s = "got NULL ProfileTable ACI Factory";
				logger.error(s);
				throw new ManagementException(s);
			}
			
			if (profileBeforeUpdate == null) {
				// FIXME: Alexandre: [DONE] Allocate new instance of PLO
				ProfileLocalObjectConcrete ploc = (ProfileLocalObjectConcrete) JPAUtils.INSTANCE.find( this.getProfileTableName(), this.getProfileName() );
				this.profileObject.getProfileTableConcrete().fireProfileAddedEvent(ploc);
			}
			else {
				// FIXME: Alexandre: [DONE] Allocate PLO
				ProfileLocalObjectConcrete plocAfterUpdate = (ProfileLocalObjectConcrete) JPAUtils.INSTANCE.find( this.getProfileTableName(), this.getProfileName() );
				this.profileObject.getProfileTableConcrete().fireProfileUpdatedEvent( profileBeforeUpdate, plocAfterUpdate);
			}

			// so far so good, time to commit the tx so that the profile is
			// visible in the SLEE
			try {
				sleeTransactionManager.commit();
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);
			}
			/*
			 * If a commit succeeds, the Profile MBean object should move to the
			 * read-only state. The SLEE must also fire a Profile Updated Event
			 * if a Profile has been updated (see Section 1.1). The dirty flag
			 * in the Profile Management object must also be set to false upon a
			 * successful commit.
			 */
			transaction = null;

		}
		finally
		{
			switchContextClassLoader(oldClassLoader);
			
		}
	}

	public void editProfile() throws ManagementException
	{
		//if(logger.isDebugEnabled()) {
			logger.info("Editing profile with name "+this.profileObject.getProfileName()+", from table with name "+this.profileObject.getProfileTableConcrete().getProfileTableName());
		//}
		
		if (!isProfileWriteable()) {
			ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
			try {
				final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
				sleeTransactionManager.begin();				  
				this.profileObject.profileLoad();
				transaction = sleeTransactionManager.suspend();
			} catch (NotSupportedException e) {
				throw new ManagementException(e.getMessage());
			} catch (SystemException e) {
				throw new ManagementException(e.getMessage());
			}
			finally
			{
				switchContextClassLoader(oldClassLoader);
			}
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
		
		return this.profileObject.isProfileDirty() && isProfileWriteable();
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
		return transaction != null;
	}

	public void restoreProfile() throws InvalidStateException, ManagementException
	{
		if(logger.isDebugEnabled()) {
			logger.debug("[restoreProfile] on: "+this.profileObject.getProfileName()+", from table:"+this.profileObject.getProfileTableConcrete().getProfileTableName());
		}

		if (!isProfileWriteable()) {
			throw new InvalidStateException("The restoreProfile method must throw a javax.slee.InvalidStateException if the Profile MBean object is not in the read-write state.");
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try {
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
			final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
			txManager.resume(transaction);
			transaction = null;
			txManager.rollback();

		} catch (InvalidTransactionException e) {
			throw new SLEEException(e.getMessage(),e);
		} catch (IllegalStateException e) {
			throw new SLEEException(e.getMessage(),e);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally
		{
			switchContextClassLoader(oldClassLoader);
		}
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
  
  // static cmp handlers
  
  /**
   * Sets the cmp field through the mbean 
   */
  public static void setCmpField(AbstractProfileMBean mbean, String fieldName, Object value) throws ManagementException {

	  logger.info("setting cmp field with name "+fieldName+" and value "+value+" on profile with name "+mbean.profileObject.getProfileName()+" of table "+mbean.profileObject.getProfileTableConcrete().getProfileTableName());
	  
	  if (mbean.isProfileWriteable()) {
			final SleeTransactionManager txManager = sleeContainer.getTransactionManager();			
			try {
				// resume tx
				txManager.resume(mbean.transaction);
				// set cmp field
				ProfileCmpHandler.setCmpField(mbean.profileObject, fieldName, value);				
				// suspend tx
				txManager.suspend();
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);			
			}			
		}
	}
	
  /**
   * Gets the cmp field value through the mbean
   * @param mbean
   * @param fieldName
   * @return
   * @throws ManagementException
   */
	public static Object getCmpField(AbstractProfileMBean mbean, String fieldName) throws ManagementException {
		
		Object result = null;
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();	
		
		boolean createTx = true;
		try {
			if (mbean.isProfileWriteable()) {
				// resume tx
				txManager.resume(mbean.transaction);
				createTx = false;
			}
			else {
				txManager.begin();
			}

			result = ProfileCmpHandler.getCmpField(mbean.profileObject, fieldName);

			if (createTx) {
				txManager.commit();
			}
			else {
				txManager.suspend();
			}
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);			
		}	
		return result;
	}
}
