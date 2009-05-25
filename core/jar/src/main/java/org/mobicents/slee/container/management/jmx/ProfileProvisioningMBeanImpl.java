/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileProvisioningMBeanImpl.java
 * 
 * Created on 12 nov. 2004
 *
 */
package org.mobicents.slee.container.management.jmx;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ManagementException;
import javax.slee.management.SleeState;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.profile.AbstractProfileMBeanImpl;
import org.mobicents.slee.container.profile.ProfileDataSource;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * MBean class for profile provisioning through jmx
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 * @author martins
 */
public class ProfileProvisioningMBeanImpl extends ServiceMBeanSupport implements ProfileProvisioningMBeanImplMBean {

	private static final Logger logger = Logger.getLogger(ProfileProvisioningMBeanImpl.class);

	// private SleeProfileManager profileManager;
	private SleeProfileTableManager sleeProfileManagement = null;
	private SleeContainer sleeContainer = null;
	private SleeTransactionManager sleeTransactionManagement = null;
	
	/**
	 * 
	 * @throws NotCompliantMBeanException
	 */
	public ProfileProvisioningMBeanImpl() throws NotCompliantMBeanException {
		super(ProfileProvisioningMBeanImplMBean.class);
		this.sleeContainer = SleeContainer.lookupFromJndi();
		this.sleeTransactionManagement = this.sleeContainer.getTransactionManager();
		this.sleeProfileManagement = this.sleeContainer.getSleeProfileTableManager();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#createProfile(java.lang.String, java.lang.String)
	 */
	public ObjectName createProfile(java.lang.String profileTableName, java.lang.String profileName) throws java.lang.NullPointerException, UnrecognizedProfileTableNameException,
			InvalidArgumentException, ProfileAlreadyExistsException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("createProfile( profileTable = "+profileTableName+" , profile = "+profileName+" )");
		}

		try {
			ProfileTableImpl.validateProfileName(profileName);
			ProfileTableImpl.validateProfileTableName(profileTableName);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidArgumentException(e.getMessage());
		}
		
		Transaction transaction = null;
		boolean rollback = true;
		try {
			// begin tx
			sleeTransactionManagement.begin();
			transaction = sleeTransactionManagement.getTransaction();
			// This checks if profile table exists - throws SLEEException in
			// case of system level and UnrecognizedProfileTableNameException in
			// case of no such table
			final ProfileTableImpl profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			// create profile
			profileTable.createProfile(profileName);
			if (sleeTransactionManagement.getRollbackOnly()) {
				throw new ManagementException("Transaction used in profile creation rolled back");
			}
			else {
				// create mbean and registers it
				final AbstractProfileMBeanImpl profileMBean = createAndRegisterProfileMBean(profileName,profileTable);
				// keep track of the mbean existence
				profileTable.addUncommittedProfileMBean(profileMBean);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						profileTable.removeUncommittedProfileMBean(profileMBean);						
					}
				};
				sleeTransactionManagement.addAfterCommitAction(action);
				sleeTransactionManagement.addAfterRollbackAction(action);
				// indicate profile creation
				profileMBean.createProfile();
				rollback = false;
				if (logger.isDebugEnabled()) {
					logger.debug("createProfile( profileTable = "+profileTableName+" , profile = "+profileName+" ) result is "+profileMBean.getObjectName());
				}
				return profileMBean.getObjectName();
			}								
		} catch (TransactionRequiredLocalException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (SLEEException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (CreateException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (NotSupportedException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (SystemException e) {
			throw new ManagementException(e.getMessage(),e);
		} finally {
			if(rollback) {
				try {
					if (sleeTransactionManagement.getTransaction() == null) {
						// the tx was suspended, resume it
						sleeTransactionManagement.resume(transaction);
					}
					sleeTransactionManagement.rollback();
				}
				catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}				
			}
		}
	}

	/**
	 * Creates and registers a profile mbean for the specified object.
	 * @param profileObject
	 * @return
	 * @throws ManagementException
	 */
	private AbstractProfileMBeanImpl createAndRegisterProfileMBean(String profileName, ProfileTableImpl profileTable) throws ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("createAndRegisterProfileMBean( profileTable = "+profileTable+" , profileName = "+profileName+" )");
		}
		
		try {
			ProfileSpecificationComponent component = profileTable.getProfileSpecificationComponent();
			Constructor<?> constructor = component.getProfileMBeanConcreteImplClass().getConstructor(Class.class, String.class, ProfileTableImpl.class);
			final AbstractProfileMBeanImpl profileMBean = (AbstractProfileMBeanImpl) constructor.newInstance(component.getProfileMBeanConcreteInterfaceClass(), profileName, profileTable);
			profileMBean.register();
			// add a rollback action to unregister the mbean
			TransactionalAction rollbackAction = new TransactionalAction() {
				public void execute() {
					try {
						profileMBean.unregister();								
					} catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}									
				}
			};
			sleeContainer.getTransactionManager().addAfterRollbackAction(rollbackAction);				
			return profileMBean;			
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);
		} 
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#createProfileTable(javax.slee.profile.ProfileSpecificationID, java.lang.String)
	 */
	public void createProfileTable(ProfileSpecificationID specificationID,
			String profileTableName) throws NullPointerException,
			UnrecognizedProfileSpecificationException,
			InvalidArgumentException, ProfileTableAlreadyExistsException,
			ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("createProfileTable( profileTable = "+profileTableName+" , specificationID = "+specificationID+" )");
		}
		
		try {
			ProfileTableImpl.validateProfileTableName(profileTableName);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidArgumentException(e.getMessage());
		}
		
		if (sleeContainer.getSleeState() != SleeState.RUNNING)
			return;

		ProfileSpecificationComponent component = sleeContainer.getComponentRepositoryImpl().getComponentByID(specificationID);
		if (component == null)
			throw new UnrecognizedProfileSpecificationException();

		final SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		
		boolean terminateTx = transactionManager.requireTransaction();
		boolean doRollback = true;		
		try {
			Thread.currentThread().setContextClassLoader(component.getClassLoader());
			sleeProfileManagement.addProfileTable(profileTableName,component);
			doRollback =false;
		} catch (ProfileTableAlreadyExistsException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);
		} finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
			sleeTransactionManagement.requireTransactionEnd(terminateTx,doRollback);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getDefaultProfile(java.lang.String)
	 */
	public ObjectName getDefaultProfile(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getDefaultProfile( profileTable = "+profileTableName+" )");
		}
		
		try {
			return _getProfile(profileTableName,null);
		} catch (UnrecognizedProfileNameException e) {
			// can't happen
			throw new ManagementException(e.getMessage(),e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfile(java.lang.String, java.lang.String)
	 */
	public ObjectName getProfile(java.lang.String profileTableName, java.lang.String profileName) throws NullPointerException, UnrecognizedProfileTableNameException, UnrecognizedProfileNameException,
			ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getProfile( profileTable = "+profileTableName+" , profile = "+profileName+" )");
		}
		
		ProfileTableImpl.validateProfileName(profileName);
		ObjectName objectName = _getProfile(profileTableName, profileName);
		if (logger.isDebugEnabled()) {
			logger.debug("getProfile( profileTable = "+profileTableName+" , profile = "+profileName+" ) result is "+objectName);
		}
		return objectName;
	}

	/**
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedProfileTableNameException
	 * @throws ManagementException
	 * @throws UnrecognizedProfileNameException
	 * @throws ManagementException
	 */
	private ObjectName _getProfile(java.lang.String profileTableName, java.lang.String profileName) throws NullPointerException, UnrecognizedProfileTableNameException,
			ManagementException, UnrecognizedProfileNameException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("_getProfile( profileTable = "+profileTableName+" , profile = "+profileName+" )");
		}
		
		ProfileTableImpl.validateProfileTableName(profileTableName);
				
		boolean b = this.sleeTransactionManagement.requireTransaction();
		boolean rb = true;
		ProfileTableImpl profileTable = null;
		try {
			profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			if (profileName != null && !profileTable.profileExists(profileName)) {
				throw new UnrecognizedProfileNameException(profileName);
			}
			ObjectName objectName = AbstractProfileMBeanImpl.getObjectName(profileTable.getProfileTableName(), profileName);
			if (!sleeContainer.getMBeanServer().isRegistered(objectName)) {
				createAndRegisterProfileMBean(profileName, profileTable);				
			}
			rb = false;
			return objectName;		
		} finally {			
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfileSpecification(java.lang.String)
	 */
	public ProfileSpecificationID getProfileSpecification(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getProfileSpecification( profileTableName = "
					+ profileTableName +" )");
		}
		
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must not be null");

		boolean b = false;
		try {
			b = this.sleeTransactionManagement.requireTransaction();

			ProfileTableImpl profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			return profileTable.getProfileSpecificationComponent().getProfileSpecificationID();
		} catch (SLEEException e) {
			throw new ManagementException("Failed to obtain ProfileSpecID name for ProfileTable: " + profileTableName, e);
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (Exception e) {
			throw new ManagementException("Failed to obtain ProfileSpecID name for ProfileTable: " + profileTableName, e);
		} finally {
			// never rollbacks
			sleeTransactionManagement.requireTransactionEnd(b,false);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfileTableUsageMBean(java.lang.String)
	 */
	public ObjectName getProfileTableUsageMBean(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, InvalidArgumentException, ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getProfileTableUsageMBean( profileTableName = "
					+ profileTableName +" )");
		}
		
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must not be null");
		
		boolean b = this.sleeTransactionManagement.requireTransaction();
		try {
			ProfileTableUsageMBeanImpl usageMBeanImpl = this.sleeProfileManagement.getProfileTable(profileTableName).getProfileTableUsageMBean();
			if (usageMBeanImpl == null) {
				throw new InvalidArgumentException();
			}
			else {
				return usageMBeanImpl.getObjectName();
			}			
		} catch (SLEEException e) {
			throw new ManagementException("Failed to obtain ProfileSpecID name for ProfileTable: " + profileTableName, e);
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (InvalidArgumentException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException("Failed to obtain ProfileSpecID name for ProfileTable: " + profileTableName, e);
		} finally {
			// never rollbacks
			sleeTransactionManagement.requireTransactionEnd(b,false);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfileTables()
	 */
	public Collection<String> getProfileTables() throws ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfileTables()");
		}
		
		boolean b = sleeTransactionManagement.requireTransaction();
		try {
			return sleeProfileManagement.getDeclaredProfileTableNames();			
		} catch (Exception x) {
			if (x instanceof ManagementException)
				throw (ManagementException) x;
			else
				throw new ManagementException("Failed getProfileTable", x);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfileTables(javax.slee.profile.ProfileSpecificationID)
	 */
	public Collection<String> getProfileTables(ProfileSpecificationID id) throws java.lang.NullPointerException, UnrecognizedProfileSpecificationException, ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getProfileTables( id = "+ id + " )");
		}
		
		if (id == null) {
			throw new NullPointerException("null profile spec id");
		}
		
		boolean b = sleeTransactionManagement.requireTransaction();
		try {
			return sleeProfileManagement.getDeclaredProfileTableNames(id);
		} catch (UnrecognizedProfileSpecificationException x) {
			throw x;		
		} catch (Throwable x) {
			throw new ManagementException("Failed createProfileTable", x);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfiles(java.lang.String)
	 */
	public Collection<ProfileID> getProfiles(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getProfiles( profileTableName = "
					+ profileTableName + " )");
		}
		
		boolean terminateTx = sleeTransactionManagement.requireTransaction();
		try {		
			return sleeProfileManagement.getProfileTable(profileTableName).getProfiles();			
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;		
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);		 
		} finally {
			sleeTransactionManagement.requireTransactionEnd(terminateTx, false);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfilesByAttribute(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public Collection<ProfileID> getProfilesByAttribute(String profileTableName,
			String attributeName, Object attributeValue)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedAttributeException, AttributeTypeMismatchException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByAttribute( profileTableName = "
					+ profileTableName + " , attributeName = " + attributeName
					+ " , attributeValue = " + attributeValue + " )");
		}
		
		boolean terminateTx = sleeTransactionManagement.requireTransaction();
		try {		
			ProfileTableImpl profileTable = sleeProfileManagement.getProfileTable(profileTableName);
			if (!profileTable.getProfileSpecificationComponent().isSlee11()) {
				throw new UnsupportedOperationException("JAIN SLEE 1.1 Specs forbiddens the usage of this method on SLEE 1.0 Profile Tables");
			}
			else {
				return profileTable.getProfilesByAttribute(attributeName,attributeValue,true);
			}			
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedAttributeException e) {
			throw e;
		} catch (AttributeTypeMismatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);		 
		} finally {
			sleeTransactionManagement.requireTransactionEnd(terminateTx, false);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfilesByDynamicQuery(java.lang.String, javax.slee.profile.query.QueryExpression)
	 */
	public Collection<ProfileID> getProfilesByDynamicQuery(String profileTableName,
			QueryExpression queryExpression) throws NullPointerException,
			UnrecognizedProfileTableNameException,
			UnrecognizedAttributeException, AttributeTypeMismatchException,
			ManagementException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByDynamicQuery( profileTableName = "
					+ profileTableName + " , queryExpression = " + queryExpression
					+ " )");
		}
		
		if (queryExpression == null) {
			throw new NullPointerException("queryExpression is null");
		}
		
		boolean terminateTx = sleeTransactionManagement.requireTransaction();

		Collection<ProfileID> profileIDs = new ArrayList<ProfileID>();
		try {
			ProfileTableImpl profileTable = sleeProfileManagement.getProfileTable(profileTableName);
			if (!profileTable.getProfileSpecificationComponent().isSlee11()) {
				throw new UnsupportedOperationException("JAIN SLEE 1.1 Specs forbiddens the usage of this method on SLEE 1.0 Profile Tables");
			}
			for (ProfileEntity profileEntity : ProfileDataSource.INSTANCE.getProfilesByDynamicQuery(profileTable,queryExpression)) {
				profileIDs.add(new ProfileID(profileEntity.getTableName(),profileEntity.getProfileName()));
			}
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedAttributeException e) {
			throw e;
		} catch (AttributeTypeMismatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException("Failed to obtain ProfileNames for ProfileTable: " + profileTableName, e);
		}
		finally {
			sleeTransactionManagement.requireTransactionEnd(terminateTx,false);
		}

		return profileIDs;
		
	}
	

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfilesByIndexedAttribute(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public Collection<?> getProfilesByIndexedAttribute(String profileTableName,
			String attributeName, Object attributeValue)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByIndexedAttribute( profileTableName = "
					+ profileTableName + " , attributeName = " + attributeName
					+ " , attributeValue = " + attributeValue + " )");
		}
		
		boolean terminateTx = sleeTransactionManagement.requireTransaction();
		try {		
			ProfileTableImpl profileTable = sleeProfileManagement.getProfileTable(profileTableName);
			if (profileTable.getProfileSpecificationComponent().isSlee11()) {
				throw new UnsupportedOperationException("JAIN SLEE 1.1 Specs forbiddens the usage of this method on SLEE 1.1 Profile Tables");
			}
			else {
				return profileTable.getProfilesByAttribute(attributeName,attributeValue,false);
			}			
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedAttributeException e) {
			throw e;
		} catch (AttributeTypeMismatchException e) {
			throw e;
		} catch (AttributeNotIndexedException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);		 
		} finally {
			sleeTransactionManagement.requireTransactionEnd(terminateTx, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfilesByStaticQuery(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	public Collection<ProfileID> getProfilesByStaticQuery(String profileTableName, String queryName, Object[] parameters) throws NullPointerException,
		UnrecognizedProfileTableNameException,
		UnrecognizedQueryNameException, InvalidArgumentException,
		AttributeTypeMismatchException, ManagementException {
	
		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByStaticQuery( profileTableName = "
					+ profileTableName + " , queryName = " + queryName
					+ " , parameters = " + Arrays.asList(parameters) + " )");
		}
		
	   if (queryName == null) {
			throw new NullPointerException("queryName is null");
		}
		
		boolean terminateTx = sleeTransactionManagement.requireTransaction();

		Collection<ProfileID> profileIDs = new ArrayList<ProfileID>();
		try {
			ProfileTableImpl profileTable = sleeProfileManagement.getProfileTable(profileTableName);
			if (!profileTable.getProfileSpecificationComponent().isSlee11()) {
				throw new UnsupportedOperationException("JAIN SLEE 1.1 Specs forbiddens the usage of this method on SLEE 1.0 Profile Tables");
			}
			for (ProfileEntity profileEntity : ProfileDataSource.INSTANCE.getProfilesByStaticQuery( profileTable, queryName, parameters )) {
				profileIDs.add(new ProfileID(profileEntity.getTableName(),profileEntity.getProfileName()));
			}
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedQueryNameException e) {
			throw e;
		} catch (InvalidArgumentException e) {
			throw e;
		} catch (AttributeTypeMismatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException("Failed to obtain ProfileNames for ProfileTable: " + profileTableName, e);
		}
		finally {
			sleeTransactionManagement.requireTransactionEnd(terminateTx,false);
		}

		return profileIDs;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#removeProfile(java.lang.String, java.lang.String)
	 */
	public void removeProfile(java.lang.String profileTableName, java.lang.String profileName) throws java.lang.NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("removeProfile( profileTableName = "
					+ profileTableName + " , profileName = " + profileName
					+ " )");
		}
		
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must nto be null");
		if (profileName == null)
			throw new NullPointerException("Argument[ProfileName] must nto be null");

		boolean b = this.sleeTransactionManagement.requireTransaction();
		boolean rb = true;
		try {
			ProfileTableImpl profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			if (!profileTable.profileExists(profileName)) {
				throw new UnrecognizedProfileNameException("There is no such profile: " + profileName + ", in profile table: " + profileTableName);
			}
			profileTable.removeProfile(profileName, true);
			if(!sleeTransactionManagement.getRollbackOnly()) {
				rb = false;
			}
			else {
				throw new ManagementException("Transaction used in profile removal rolled back");
			}
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedProfileNameException e) {
			throw e;
		} catch (Exception e) {
			throw new ManagementException("Failed to remove due to system level failure.", e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);			
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#removeProfileTable(java.lang.String)
	 */
	public void removeProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("removeProfileTable( profileTableName = "
					+ profileTableName + " )");
		}
		
		if (profileTableName == null)
			throw new NullPointerException("profileTableName is null");

		boolean b = this.sleeTransactionManagement.requireTransaction();
		boolean rb = false;
		try {
			this.sleeProfileManagement.removeProfileTable(profileTableName);			
		} catch (UnrecognizedProfileTableNameException e) {
			rb = true;
			throw e;
		} catch (Throwable e) {
			rb = true;
			throw new ManagementException("Failed to remove due to system level failure.", e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.ProfileProvisioningMBean#renameProfileTable(java.lang.String, java.lang.String)
	 */
	public void renameProfileTable(java.lang.String oldProfileTableName, java.lang.String newProfileTableName) throws java.lang.NullPointerException, UnrecognizedProfileTableNameException,
			InvalidArgumentException, ProfileTableAlreadyExistsException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("renameProfileTable( oldProfileTableName = "
					+ oldProfileTableName + " , newProfileTableName = "
					+ newProfileTableName + " )");
		}
		
		if (oldProfileTableName == null)
			throw new NullPointerException("Argument[OldProfileTableName] must nto be null");
		if (newProfileTableName == null)
			throw new NullPointerException("Argument[NewProfileTableName] must nto be null");

		ProfileTableImpl.validateProfileTableName(newProfileTableName);

		boolean b = this.sleeTransactionManagement.requireTransaction();
		boolean rb = true;
		try {
			this.sleeProfileManagement.renameProfileTable(oldProfileTableName,newProfileTableName);
			rb = false;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (ProfileTableAlreadyExistsException e) {
			throw e;
		} catch (Throwable e) {
			throw new ManagementException("Failed to remove due to system level failure.", e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}
	}

	/**
	 * 
	 * start MBean service lifecycle method
	 * 
	 */
	protected void startService() throws Exception {
		// this.profileManager =
		// (SleeProfileManager)SleeProfileManager.getInstance();
		logger.info("ProfileProvisioningMBean has been started");
		// register service in JNDI
		// SleeContainer.registerFacilityWithJndi(SleeProfileManager.JNDI_NAME,
		// profileManager);
	}

	/**
	 * 
	 * stop MBean service lifecycle method
	 * 
	 */
	protected void stopService() throws Exception {

		// unregister the SleeProfileManager service with JNDI
		// SleeContainer.unregisterFacilityWithJndi(SleeProfileManager.JNDI_NAME);

		// this.profileManager = null;
	}

}