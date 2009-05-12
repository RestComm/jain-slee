package org.mobicents.slee.container.profile;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.profile.ProfileImplementationException;
import javax.slee.profile.ProfileMBean;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.ReadOnlyProfileException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
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
	
	private enum State { read, write };
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AbstractProfileMBean.class);

	protected final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	public static final String _PROFILE_OBJECT = "profileObject";
	public static final String _CHECK_WRITE_ACCESS = "checkWriteAccess();";
	
	/**
	 * the profile object assigned to this mbean
	 */
	protected ProfileObject profileObject;
	
	/**
	 * the object name used to register this mbean
	 */
	private final ObjectName objectName;
	
	/**
	 * the transaction being used to edit the underlying profile, if any
	 */
	private Transaction transaction;
	
	/**
	 * the table related with the profile mbean
	 */
	private final ProfileTableConcrete profileTable;
	
	/**
	 * the name of the profile assigned to the mbean
	 */
	private final String profileName;
	
	/**
	 * current state in the mbean
	 */
	private State state = State.read;
	
	/**
	 * 
	 * @param mbeanInterface
	 * @param profileObject
	 * @throws NotCompliantMBeanException
	 */
	public AbstractProfileMBean(Class<?> mbeanInterface, String profileName, ProfileTableConcrete profileTable) throws NotCompliantMBeanException, ManagementException {
	  super(mbeanInterface);
	  this.profileTable = profileTable;
	  this.profileName = profileName;
	  // register the mbean
	  try {
		  this.objectName = ProfileTableImpl.generateProfileMBeanObjectName(profileTable.getProfileTableName(), profileName);
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
	 * Moves to the write mode, using specified object. The current java transaction will be suspended.
	 * @throws ManagementException 
	 */
	public void writeMode(ProfileObject profileObject) throws SLEEException, ManagementException {
		if (!isProfileWriteable()) {
			this.profileObject = profileObject;
			// make it writable
			profileObject.getProfileEntity().setReadOnly(false);
			// suspend the transaction
			try {
				transaction = sleeContainer.getTransactionManager().suspend();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			state = State.write;
		}
	}
	
	public void readMode() {
		state = State.read;
		profileObject = null;
		transaction = null;
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
		if (logger.isDebugEnabled()) {
			  logger.debug("closeProfile() on: " + profileName + ", from table:"  +profileTable.getProfileTableName());
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(profileTable.getProfileSpecificationComponent().getClassLoader());
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
			
			// The closeProfile method must throw a javax.slee.InvalidStateException if the Profile MBean object is in the read-write state.
			if (this.isProfileWriteable())
				throw new InvalidStateException();
			// unregister mbean
			try {
				sleeContainer.getMBeanServer().unregisterMBean(objectName);
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);
			}									
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
		
		if (logger.isDebugEnabled()) {
			logger.debug("commitProfile() on: "+ profileName + ", from table:"  +profileTable.getProfileTableName());
		}
		
		if (!this.isProfileWriteable())
			throw new IllegalStateException("not in write state");
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileTable.getProfileSpecificationComponent().getClassLoader());

		final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		
		try	{
			// resume transaction
			try {
				sleeTransactionManager.resume(transaction);
			} catch (Throwable e) {
				throw new ManagementException(e.getMessage(),e);
			}
			try {
				this.profileObject.profileVerify();	
				this.profileObject.profilePassivate();
			}
			catch (RuntimeException e) {
				// oops, set rollback flag, suspend tx and throw mngmt exception
				try {
					sleeTransactionManager.setRollbackOnly();
					sleeTransactionManager.suspend();
				} catch (Throwable f) {
					logger.error(f.getMessage(),f);
				}
				profileObject.invalidateObject();
				throw new ManagementException(e.getMessage(),e);
			}
			// the profile will now become visible in the SLEE
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
			readMode();
		}
		finally
		{
			switchContextClassLoader(oldClassLoader);
			
		}
	}

	public void editProfile() throws ManagementException
	{
		if(logger.isDebugEnabled()) {
			logger.debug("Editing profile with name "+profileName+", from table with name "+this.profileTable.getProfileTableName());
		}
		
		if (!isProfileWriteable()) {
			ClassLoader oldClassLoader = switchContextClassLoader(this.profileTable.getProfileSpecificationComponent().getClassLoader());
			try {
				final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
				sleeTransactionManager.begin();				  
				ProfileObject profileObject = profileTable.borrowProfileObject();
				try {
					profileObject.profileActivate(profileName);
				}
				catch (RuntimeException e) {
					sleeTransactionManager.rollback();
					profileObject.invalidateObject();
					throw new ManagementException(e.getMessage(),e);
				}
				writeMode(profileObject);
			} catch (NotSupportedException e) {
				throw new ManagementException(e.getMessage());
			} catch (SystemException e) {
				throw new ManagementException(e.getMessage());
			} catch (UnrecognizedProfileNameException e) {
				throw new ManagementException(e.getMessage(),e);
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
			logger.debug("isProfileDirty() on: "+profileName+", from table:"+profileTable.getProfileTableName());
		}
		/*
		 * The isProfileDirty method returns true if the Profile MBean object is
		 * in the read-write state and the dirty flag of the Profile Management
		 * object that caches the persistent state of the Profile returns true.
		 * This method returns false under any other situation.
		 */
		
		return isProfileWriteable() && this.profileObject.getProfileEntity().isDirty();
	}

	public boolean isProfileWriteable() throws ManagementException
	{
		return state == State.write;
	}

	public void restoreProfile() throws InvalidStateException, ManagementException
	{
		if (logger.isDebugEnabled()) {
			  logger.debug("restoreProfile() on: "+profileName+", from table:"+ profileTable.getProfileTableName());
		}

		if (!isProfileWriteable()) {
			throw new InvalidStateException("The restoreProfile method must throw a javax.slee.InvalidStateException if the Profile MBean object is not in the read-write state.");
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileTable.getProfileSpecificationComponent().getClassLoader());
		
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
			profileObject.profilePassivate();
			readMode();
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
		return profileName;
	}

	protected String getProfileTableName() {
		return profileTable.getProfileTableName();
	}

  protected ClassLoader switchContextClassLoader(ClassLoader newClassLoader)
  {
    Thread t = Thread.currentThread();
    ClassLoader oldClassLoader = t.getContextClassLoader();
    t.setContextClassLoader(newClassLoader);
    
    return oldClassLoader;
  }
  
  // cmp accessor helpers

  /**
   * Logic to execute before invoking a cmp setter method on the mbean
   * @return true if the method resumed a transaction
   * @throws ManagementException
   */
  protected boolean beforeSetCmpField() throws ManagementException, InvalidStateException {

	  if (logger.isDebugEnabled()) {
		  logger.debug("beforeSetCmpField() on "+profileObject);
	  }
	  
	  if (isProfileWriteable()) {
		  final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		  try {
			  Transaction activeTransaction = txManager.getTransaction();
			  if (activeTransaction == null) {
				  txManager.resume(transaction);
				  return true;
			  }
			  else {
				  if (activeTransaction.equals(transaction)) {
					  // ok, same tx is already running
					  return false;
				  }
				  else {
					  throw new SLEEException("mbean is in write state bound with a suspended transaction but the tx manager is running another transaction");
				  }
			  }
		  } catch (Throwable e) {
			  throw new ManagementException(e.getMessage(),e);			
		  }			
	  }
	  else {
		  throw new InvalidStateException();
	  }
  }

  /**
   * Logic to execute after invoking a cmp setter method on the mbean
   * @param resumedTransaction if the method beforeSetCmpField() resumed a transaction
   * @throws ManagementException
   */
  protected void afterSetCmpField(boolean resumedTransaction) throws ManagementException {
	  
	  if (logger.isDebugEnabled()) {
		  logger.debug("afterSetCmpField( resumedTransaction = "+resumedTransaction+" ) on "+profileObject);
	  }
	  
	  if (resumedTransaction) {
		  try {
			  sleeContainer.getTransactionManager().suspend();
		  } catch (Throwable e) {
			  throw new ManagementException(e.getMessage(),e);			
		  }			
	  }
  }
  
  /**
   * Logic to execute before invoking a cmp getter method on the mbean
   * @return true if the method initiated or resumed a transaction
   * @throws ManagementException
   */
  protected boolean beforeGetCmpField() throws ManagementException {
	  
	  if (logger.isDebugEnabled()) {
		  logger.debug("beforeGetCmpField() on "+profileObject);
	  }
	  
	  return beforeManagementMethodInvocation();
  }

  /**
   * Logic to execute after invoking a cmp getter method on the mbean
   * @param activatedTransaction if the method beforeGetCmpField() initiated or resumed a transaction
   * @throws ManagementException
   */
  protected void afterGetCmpField(boolean activatedTransaction) throws ManagementException {
	  
	  if (logger.isDebugEnabled()) {
		  logger.debug("afterGetCmpField( activatedTransaction = "+activatedTransaction+" ) on "+profileObject);
	  }
	  
	 afterManagementMethodInvocation(activatedTransaction);
  }

  /**
   * Logic to execute before invoking a management method on the mbean
   * @return true if the method initiated or resumed a transaction
   * @throws ManagementException
   */
  protected boolean beforeManagementMethodInvocation() throws ManagementException {

	  if (logger.isDebugEnabled()) {
		  logger.debug("beforeManagementMethodInvocation() on "+profileObject);
	  }
	  
	  final SleeTransactionManager txManager = sleeContainer.getTransactionManager();	
	  try {
		  Transaction activeTransaction = txManager.getTransaction();
		  if (isProfileWriteable()) {
			  if (activeTransaction == null) {
				  txManager.resume(transaction);
				  return true;
			  }
			  else {
				  if (activeTransaction.equals(transaction)) {
					  // ok, same tx is already running
					  return false;
				  }
				  else {
					  throw new SLEEException("mbean is in write state bound with a suspended transaction but the tx manager is running another transaction");
				  }
			  }
		  }
		  else {
			  if (activeTransaction == null) {
				  txManager.begin();
				  if (profileObject == null) {
					  profileObject = profileTable.borrowProfileObject();
					  profileObject.profileActivate(profileName);
				  }
				  return true;
			  }
			  else {
				 return false;
			  }
		  }
	  } catch (Throwable e) {
		  throw new ManagementException(e.getMessage(),e);			
	  }	
  }

  /**
   * Logic to execute after invoking a management method on the mbean
   * @param activatedTransaction if the method beforeManagementMethodInvocation() initiated or resumed a transaction
   * @throws ManagementException
   */
  protected void afterManagementMethodInvocation(boolean activatedTransaction) throws ManagementException {
	  
	  if (logger.isDebugEnabled()) {
		  logger.debug("afterManagementMethodInvocation( activatedTransaction = "+activatedTransaction+" ) on "+profileObject);
	  }
	  
	  if (activatedTransaction) {
		  final SleeTransactionManager txManager = sleeContainer.getTransactionManager();	
		  try {
			  if (isProfileWriteable()) {
				  txManager.suspend();
			  }
			  else {
				  profileObject.profilePassivate();
				  if (txManager.getRollbackOnly()) {
					  txManager.rollback();	
				  }
				  else {
					  txManager.commit();
				  }
				  profileObject = null;
			  }
		  } catch (Throwable e) {
			  throw new ManagementException(e.getMessage(),e);			
		  }			
	  }
  }

  /**
   * Handles a {@link Throwable}, which was the result of a management method invocation
   * @param t
   * @throws ProfileImplementationException
   * @throws InvalidStateException
   * @throws ManagementException
   */
  protected void throwableOnManagementMethodInvocation(Throwable t) throws ProfileImplementationException, InvalidStateException, ManagementException {
	  if (t instanceof ProfileImplementationException) {
		  throw (ProfileImplementationException)t;
	  }
	  else if (t instanceof InvalidStateException) {
		  throw (InvalidStateException)t;
	  }
	  else if (t instanceof ManagementException) {
		  throw (ManagementException)t;
	  }
	  else if (t instanceof RuntimeException) {		  
		  try {
			  profileObject.invalidateObject();
			  sleeContainer.getTransactionManager().setRollbackOnly();
		  } catch (Throwable e) {
			  throw new ManagementException(e.getMessage(),e);
		  }
		  throw new ProfileImplementationException(t);
	  }
	  else if (t instanceof ReadOnlyProfileException) {
		  throw new InvalidStateException(t.getMessage());
	  }
	  else {
		  // checked exception
		  throw new ProfileImplementationException(t);
	  }
  }
}
