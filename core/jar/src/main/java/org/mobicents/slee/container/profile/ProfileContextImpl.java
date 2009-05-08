package org.mobicents.slee.container.profile;

import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.Tracer;
import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.runtime.facilities.TracerImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Start time:17:11:23 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This class represents ProfileContext. Note that profile context object MUST
 * belong to one profile table during its life time, profile(object) can change. <br>
 * The ProfileContext interface provides a Profile object with access to
 * SLEE-managed state that is dependent on the Profile objects's currently
 * executing context.
 * 
 * A ProfileContext object is given to a Profile object after the Profile object
 * is created via the setProfileContext method. The ProfileContext object
 * remains associated with the Profile object for the lifetime of that Profile
 * object. Note that the information that the Profile object obtains from the
 * ProfileContext object may change as the SLEE assigns the Profile object to
 * different profiles during the Profile object's lifecycle.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileContextImpl implements ProfileContext {

	private ProfileTableConcrete profileTable = null;
	private ProfileObject profileObject = null;

	private final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	public ProfileContextImpl(ProfileTableConcrete profileTable)
	{
		super();
		
		if (profileTable == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		 
		this.profileTable = profileTable;
		
    // register tracer
		try {
    final String tableName = profileTable.getProfileTableName();
    
    TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceFacility().getTraceMBeanImpl();
    traceMBeanImpl.registerNotificationSource(new ProfileTableNotification(tableName));
    
    TransactionalAction action = new TransactionalAction() {
      public void execute() {
        // remove notification sources for profile table
        TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceFacility().getTraceMBeanImpl();
        traceMBeanImpl.deregisterNotificationSource(new ProfileTableNotification(tableName));
      }
    };
    sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		}
		catch (SystemException e) {
		  throw new SLEEException("Failure to register Tracer", e);
    }
	}

	public void setProfileObject(ProfileObject profileObject)
	{
		this.profileObject = profileObject;
	}

	// #################################
	// # Striclty slee defined methods #
	// #################################

	public ProfileLocalObject getProfileLocalObject() throws IllegalStateException, SLEEException
	{
		// check state
		if (profileObject.getState() != ProfileObjectState.READY && !profileObject.isInvokingProfilePostCreate()) {
			throw new IllegalStateException();
		}
		// check if it is default profile
		if (profileObject.getProfileEntity().getProfileName() == null) {
			throw new IllegalStateException();
		}
		return profileObject.getProfileLocalObject();
	}

	public String getProfileName() throws IllegalStateException, SLEEException
	{
		doGeneralChecks();
		
		if (this.profileObject == null || this.profileObject.getState() != ProfileObjectState.READY) {
			throw new IllegalStateException("Profile not assigned or");
		}
		
		try {
			return this.profileObject.getProfileEntity().getProfileName();
		}
		catch (Exception e) {
			throw new SLEEException("Failed with profile table ;[", e);
		}
	}

	public ProfileTable getProfileTable() throws SLEEException
	{
		doGeneralChecks();
		return this.profileTable;
	}

	public ProfileTable getProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, SLEEException
	{
		if (profileTableName == null) {
			throw new NullPointerException("ProfileTableName must not be null.");
		}

		doGeneralChecks();
		
		try {
			return this.profileTable.getSleeContainer().getSleeProfileTableManager().getProfileTable(profileTableName);
		}
		catch (Exception e) {
			throw new SLEEException("Failed to obtain profile table.", e);
		}
	}

	public String getProfileTableName() throws SLEEException
	{
		doGeneralChecks();
		
		try {
			return this.profileTable.getProfileTableName();
		}
		catch (Exception e) {
			throw new SLEEException("Operaion failed.", e);
		}
	}

	/**
	 * Test if the current transaction has been marked for rollback only. A
	 * Profile object invokes this method while executing within a transaction
	 * to determine if the transaction has been marked for rollback.
	 * 
	 * This method is a mandatory transactional method.
	 * 
	 * @return true if the current transaction has been marked for rollback,
	 *         false otherwise.
	 * @throw TransactionRequiredLocalException - if this method is invoked
	 *        without a valid transaction context.
	 * @throw SLEEException - if the current state of the transaction could not
	 *        be obtained due to a system-level failure.
	 */
	public boolean getRollbackOnly() throws TransactionRequiredLocalException, SLEEException
	{
		doGeneralChecks();

		final SleeTransactionManager txMgr = profileTable.getSleeContainer().getTransactionManager();
		txMgr.mandateTransaction();

		try {
			return txMgr.getRollbackOnly();
		}
		catch (SystemException e) {
			throw new SLEEException("Problem with the tx manager!");
		}
	}

	/**
   * Mark the current transaction for rollback. The transaction will become
   * permanently marked for rollback. A transaction marked for rollback can
   * never commit.
   * 
   * A Profile object invokes this method when it does not want the current
   * transaction to commit.
   * 
   * This method is a mandatory transactional method.
   * 
   * @throw TransactionRequiredLocalException - if this method is invoked
   *        without a valid transaction context.
   * @throw SLEEException - if the current transaction could not be marked for
   *        rollback due to a system-level failure.
   */
  public void setRollbackOnly() throws TransactionRequiredLocalException, SLEEException
  {
  	doGeneralChecks();
  
  	final SleeTransactionManager txMgr = profileTable.getSleeContainer().getTransactionManager();
  	txMgr.mandateTransaction();
  
  	try {
  		txMgr.setRollbackOnly();
  	}
  	catch (SystemException e) {
  		throw new SLEEException("Problem with the tx manager!");
  	}
  }

  /**
	 * 
	 * Get a tracer for the specified tracer name. The notification source used
	 * by the tracer is a ProfileTableNotification that contains the profile
	 * table name as identified by getProfileTableName().
	 * 
	 * Refer Tracer for a complete discussion on tracers and tracer names.
	 * 
	 * Trace notifications generated by a tracer obtained using this method are
	 * of the type ProfileTableNotification.TRACE_NOTIFICATION_TYPE.
	 * 
	 * This method is a non-transactional method.
	 * 
	 * 
	 * @parameter tracerName - the name of the tracer.
	 * @return a tracer for the specified tracer name. Trace messages generated
	 *         by this tracer will contain a notification source that is a
	 *         ProfileTableNotification object containing a profile table name
	 *         equal to that obtained from the getProfileTableName() method on
	 *         this ProfileContext.
	 * @throws java.lang.NullPointerException
	 *             - if tracerName is null.
	 * @throws java.lang.IllegalArgumentException
	 *             - if tracerName is an invalid name. Name components within a
	 *             tracer name must have at least one character. For example,
	 *             "com.mycompany" is a valid tracer name, whereas
	 *             "com..mycompany" is not.
	 * @throws SLEEException
	 *             - if the Tracer could not be obtained due to a system-level
	 *             failure.
	 */
	public Tracer getTracer(String tracerName) throws NullPointerException, IllegalArgumentException, SLEEException
	{
		if (tracerName == null) {
			throw new NullPointerException("TracerName must nto be null");
		}
		
		doGeneralChecks();
		
		try {
			TracerImpl.checkTracerName(tracerName, this.profileTable.getProfileTableNotification().getNotificationSource());
		}
		catch (InvalidArgumentException e1) {
			throw new IllegalArgumentException(e1);
		}

		try {
			return profileTable.getSleeContainer().getTraceFacility().getTraceMBeanImpl().createTracer(this.profileTable.getProfileTableNotification().getNotificationSource(), tracerName, true);
		}
		catch (Exception e) {
			throw new SLEEException("Failed to obtain tracer due to ");
		}
	}

	private void doGeneralChecks()
	{
		if (this.profileTable == null)
			throw new SLEEException("Profile table has not been set.");

		if (this.profileTable.getProfileTableNotification() == null)
			throw new SLEEException("Profile table has no notification source.");
	}

}
