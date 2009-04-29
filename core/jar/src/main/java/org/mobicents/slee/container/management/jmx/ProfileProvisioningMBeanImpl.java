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
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

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
import javax.slee.profile.ProfileMBean;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.profile.AbstractProfileMBean;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableConcrete;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * MBean class for profile provisioning through jmx
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 * 
 */
public class ProfileProvisioningMBeanImpl extends ServiceMBeanSupport implements ProfileProvisioningMBeanImplMBean {

	private static Logger logger;

	// private SleeProfileManager profileManager;
	private SleeProfileTableManager sleeProfileManagement = null;
	private SleeContainer sleeContainer = null;
	private SleeTransactionManager sleeTransactionManagement = null;
	static {
		try {
			logger = Logger.getLogger(ProfileProvisioningMBeanImpl.class);

		} catch (Exception ex) {
			logger.fatal("error initializing profile provisioning mbean");
		}
	}

	public ProfileProvisioningMBeanImpl() throws NotCompliantMBeanException {
		super(ProfileProvisioningMBeanImplMBean.class);
		this.sleeContainer = SleeContainer.lookupFromJndi();
		this.sleeTransactionManagement = this.sleeContainer.getTransactionManager();
		this.sleeProfileManagement = this.sleeContainer.getSleeProfileTableManager();
	}

	/**
	 * Create a new profile with the specified name in the specified profile
	 * table. The ObjectName returned by this method provides the management
	 * client with the name of a Profile MBean for the created profile. This
	 * Profile MBean is in the read-write state allowing the management client a
	 * chance to configure the initial values for the profile attributes before
	 * it is added to the profile table. The new profile is not visible in the
	 * profile table until the Profile MBean state is committed using the
	 * ProfileMBean.commitProfile() method. If the ProfileMBean.restoreProfile()
	 * method is invoked on the Profile MBean before its state is committed,
	 * creation of the profile is rolled back and the profile is considered
	 * never to have been created successfully.
	 * 
	 * The JMX Object name of the Profile MBean for the created profile is
	 * composed of at least:
	 * 
	 * <br>*
	 * a base name specifying the domain and type of the MBean <br>*
	 * the profile table name property, with a value equal to profileTableName <br>*
	 * the profile name property, with a value equal to newProfileName.
	 * 
	 * @param profileTableName
	 *            - the name of the profile table to create the profile in.
	 * @param newProfileName
	 *            - the name of the new profile. The name must be unique within
	 *            the scope of the profile table.
	 * @return the Object Name of the new profile.
	 * @throws java.lang.NullPointerException
	 *             - if either argument is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws InvalidArgumentException
	 *             - if newProfileName is zero-length or contains illegal
	 *             characters.
	 * @throws ProfileAlreadyExistsException
	 *             - if a profile with the same name already exists in the
	 *             profile table.
	 * @throws ManagementException
	 *             - if the profile could not be created due to a system-level
	 *             failure.
	 */
	public ObjectName createProfile(java.lang.String profileTableName, java.lang.String newProfileName) throws java.lang.NullPointerException, UnrecognizedProfileTableNameException,
			InvalidArgumentException, ProfileAlreadyExistsException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("Creating profile with name "+newProfileName+" in table "+profileTableName);
		}

		ProfileTableImpl.validateProfileName(newProfileName);
		ProfileTableImpl.validateProfileTableName(profileTableName);

		boolean terminateTx = sleeTransactionManagement.requireTransaction();
		boolean doRollback = true;
		try {

			// This checks if profile table exists - throws SLEEException in
			// case of system level and UnrecognizedProfileTableNameException in
			// case of no such table
			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			ProfileObject profileObject =  profileTable.createProfile(newProfileName);
			ObjectName objectName = createAndRegisterProfileMBean(profileObject);
			if (logger.isDebugEnabled()) {
				logger.debug("Profile with name "+newProfileName+" in table "+profileTableName+" created, returning mbean name "+objectName);
			}
			doRollback = false;
			return objectName;		
		} catch (TransactionRequiredLocalException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (SLEEException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (CreateException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (ProfileVerificationException e) {
			throw new ManagementException(e.getMessage(),e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(terminateTx,doRollback);
		}
	}

	/**
	 * Creates and registers a profile mbean for the specified object.
	 * @param profileObject
	 * @return
	 * @throws ManagementException
	 */
	private ObjectName createAndRegisterProfileMBean(ProfileObject profileObject) throws ManagementException {
		
		try {
			ProfileSpecificationComponent component = profileObject.getProfileSpecificationComponent();
			Constructor<?> constructor = component.getProfileMBeanConcreteImplClass().getConstructor(Class.class, ProfileObject.class);
			final AbstractProfileMBean profileMBean = (AbstractProfileMBean) constructor.newInstance(component.getProfileMBeanConcreteInterfaceClass(), profileObject);
			// add a rollback action to close the mbean
			TransactionalAction rollbackAction = new TransactionalAction() {
				public void execute() {
					try {
						if (logger.isDebugEnabled()) {
							logger.debug("Removing profile mbean "+profileMBean.getObjectName()+" due to tx rollback");
						}
						if (profileMBean.isProfileWriteable()) {
							profileMBean.restoreProfile();
						}
						profileMBean.closeProfile();
					} catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}					
				}
			};
			sleeContainer.getTransactionManager().addAfterRollbackAction(rollbackAction);
			// TODO add profile mbean name in profile table to control idleness or make mbean server queries work (preferable) :)
			return profileMBean.getObjectName();
			
		} catch (SecurityException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (NoSuchMethodException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (InstantiationException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			throw new ManagementException(e.getMessage(),e);
		} catch (SystemException e) {
			throw new ManagementException(e.getMessage(),e);
		}
	}
	
	
	/**
	 * Create a new profile table from a profile specification.
	 * 
	 * @param id
	 *            - the component identifier of the profile specification that
	 *            the profile table should be created from.
	 * @param newProfileTableName
	 *            - the name of the profile table to create. The name cannot
	 *            include the '/' character.
	 * @throws java.lang.NullPointerException
	 *             - if newProfileTableName is null.
	 * @throws UnrecognizedProfileSpecificationException
	 *             - if id is not a recognizable ProfileSpecificationID for the
	 *             SLEE or it does not correspond with a profile specification
	 *             installed in the SLEE.
	 * @throws InvalidArgumentException
	 *             - if newProfileTableName is zero-length or contains a '/'
	 *             character.
	 * @throws ProfileTableAlreadyExistsException
	 *             - if a profile table with the same name already exists.
	 * @throws ManagementException
	 *             - if the profile table could not be created due to a
	 *             system-level failure.
	 */
	public void createProfileTable(ProfileSpecificationID specificationID, String profileTableName) throws NullPointerException, UnrecognizedProfileSpecificationException, InvalidArgumentException,
			ProfileTableAlreadyExistsException, ManagementException {
		if (sleeContainer.getSleeState() != SleeState.RUNNING)
			return;

		/*
		 * if(profileManager==null){ this.profileManager = new
		 * SleeProfileManager(serviceContainer.getMBeanServer());
		 * SleeContainer.registerFacilityWithJndi(SleeProfileManager.JNDI_NAME,
		 * profileManager); }
		 */
		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		boolean b = transactionManager.requireTransaction();
		boolean rb = true;
		try {
			logger.debug("creating new Profile Table " + profileTableName + " ...");
			logger.debug("profile specification ID :" + specificationID.toString());

			//FIXME: Its easier to handle this here + plus it hadnles NPE
			ProfileTableImpl.validateProfileTableName(profileTableName);

			ProfileTableConcrete profileTable = null;
			try {
				profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
				if (profileTable != null)
					throw new ProfileTableAlreadyExistsException("Failed creating Profile Table " + profileTableName + " in " + specificationID);
			} catch (SLEEException e1) {
				String err = "Failed to create profile table, because of failure in findProfuleTable(" + profileTableName + "). System Exception.";
				logger.error(err, e1);
				throw new ManagementException(err, e1);
			} catch (UnrecognizedProfileTableNameException e) {
				// we ignore :)
			}

			logger.debug("Profile table does not exist -- creating one  " + specificationID.toString());

			// get the profile specification descriptor from the deployment
			// manager class
			ProfileSpecificationComponent component = sleeContainer.getComponentRepositoryImpl().getComponentByID(specificationID);

			if (component == null)
				throw new UnrecognizedProfileSpecificationException();

			try {
				Thread.currentThread().setContextClassLoader(component.getClassLoader());

				profileTable = this.sleeProfileManagement.addProfileTable(profileTableName,component);
				//profileTable.addProfile(null, true);
			} catch (TransactionRequiredLocalException e) {
				throw new ManagementException("Transaction Manager Failure", e);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			} catch (ClassNotFoundException e) {
				throw new ManagementException("Profile Specification has not been correctly deployed", e);
			} catch (Exception e) {
				throw new ManagementException(e.getMessage(), e);
			} finally {
				//
			}
			logger.debug("new Profile Table " + profileTableName + " created");
			rb =false;
		} catch (Exception x) {
			try {
				transactionManager.setRollbackOnly();
			} catch (SystemException e) {
				logger.error("System Exception", e);
				throw new ManagementException("System Exception", e);
			}
			if (x instanceof NullPointerException)
				throw (NullPointerException) x;
			else if (x instanceof UnrecognizedProfileSpecificationException)
				throw (UnrecognizedProfileSpecificationException) x;
			else if (x instanceof InvalidArgumentException)
				throw (InvalidArgumentException) x;
			else if (x instanceof ProfileTableAlreadyExistsException)
				throw (ProfileTableAlreadyExistsException) x;
			else if (x instanceof ManagementException)
				throw (ManagementException) x;
			else
				throw new ManagementException("Failed createProfileTable", x);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

	}

	public ObjectName getDefaultProfile(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		try {
			return _getProfile(profileTableName,"");
		} catch (UnrecognizedProfileNameException e) {
			// can't happen
			throw new ManagementException(e.getMessage(),e);
		}		
	}

	public ObjectName getProfile(java.lang.String profileTableName, java.lang.String profileName) throws NullPointerException, UnrecognizedProfileTableNameException, UnrecognizedProfileNameException,
			ManagementException {
		ProfileTableImpl.validateProfileName(profileName);
		return _getProfile(profileTableName, profileName);
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

		ProfileTableImpl.validateProfileTableName(profileTableName);

		boolean b = this.sleeTransactionManagement.requireTransaction();
		boolean rb = true;
		try {
			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			ProfileObject profileObject = profileTable.assignProfileObject(profileName);
			ObjectName objectName = createAndRegisterProfileMBean(profileObject);
			rb = false;
			return objectName;		
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

	}

	/**
	 * Get the component identifier of the profile specification that a profile
	 * table was created with.
	 * 
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @return the component identifier of the profile specification that the
	 *         profile table was created with.
	 * @throws java.lang.NullPointerException
	 *             - if profileTableName is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws ManagementException
	 *             - if the component identifier could not be obtained due to a
	 *             system-level failure.
	 */
	public ProfileSpecificationID getProfileSpecification(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		logger.debug("trying to get the profile specification for " + profileTableName + " ...");
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must not be null");

		boolean b = false;
		try {
			b = this.sleeTransactionManagement.requireTransaction();

			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
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

	/**
	 * Get the JMX Object Name of a ProfileTableUsageMBean object for a profile
	 * table.
	 * 
	 * The JMX Object name of the Profile Table Usage MBean is composed of at
	 * least:
	 * 
	 * <br>  *
	 * the base name which specifies the domain and type of the MBean <br>  *
	 * the ProfileTableUsageMBean.PROFILE_TABLE_NAME_KEY property, with a value
	 * equal to profileTableName
	 * 
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @return the Object Name of a ProfileTableUsageMBean object for the
	 *         specified profile table.
	 * @throws java.lang.NullPointerException
	 *             - if profileTableName is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws InvalidArgumentException
	 *             - if the profile specification component that the specified
	 *             profile table was created from does not define a usage
	 *             parameters interface.
	 * @throws ManagementException
	 *             - if the Object Name could not be obtained due to a
	 *             system-level failure.
	 * @since SLEE 1.1
	 */
	public ObjectName getProfileTableUsageMBean(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, InvalidArgumentException, ManagementException {
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must not be null");
		// FIXME: maybe this does not requrie TX
		boolean b = false;
		try {
			b = this.sleeTransactionManagement.requireTransaction();

			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			return profileTable.getUsageMBeanName();
		} catch (SLEEException e) {
			throw new ManagementException("Failed to obtain ProfileSpecID name for ProfileTable: " + profileTableName, e);
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;

		} catch (InvalidArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new ManagementException("Failed to obtain ProfileSpecID name for ProfileTable: " + profileTableName, e);
		} finally {
			// never rollbacks
			sleeTransactionManagement.requireTransactionEnd(b,false);
		}

	}

	/**
	 * Get a collection of java.lang.String objects that identify the names of
	 * all the profile tables that have been created in the SLEE.
	 * 
	 * @return a collection of java.lang.String objects identifying the names of
	 *         all the profile tables that have been created in the SLEE.
	 * @throws ManagementException
	 *             - if the profile table names could not be obtained due to a
	 *             system-level failure.
	 */
	public Collection getProfileTables() throws ManagementException {

		Collection<String> tablesName = new ArrayList<String>();
		boolean b = false;
		boolean rb = true;
		try {
			b = this.sleeTransactionManagement.requireTransaction();

			tablesName = this.sleeProfileManagement.getDeclaredProfileTableNames();
			rb = false;
		} catch (Exception x) {

			if (x instanceof ManagementException)
				throw (ManagementException) x;
			else
				throw new ManagementException("Failed getProfileTable", x);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

		return tablesName;

	}

	/**
	 * Get a collection of java.lang.String objects that identify the names of
	 * the profile tables that have been created from the specified profile
	 * specification.
	 * 
	 * @param id
	 *            - the component identifier of the profile specification.
	 * @return a collection of java.lang.String objects identifying the names of
	 *         the profile profile tables that have been created from the
	 *         specified profile specification.
	 * @throws java.lang.NullPointerException
	 *             - if id is null.
	 * @throws UnrecognizedProfileSpecificationException
	 *             - if id is not a recognizable ProfileSpecificationID for the
	 *             SLEE or it does not correspond with a profile specification
	 *             installed in the SLEE.
	 * @throws ManagementException
	 *             - if the profile table names could not be obtained due to a
	 *             system-level failure.
	 * @since SLEE 1.1
	 */
	public Collection getProfileTables(ProfileSpecificationID id) throws java.lang.NullPointerException, UnrecognizedProfileSpecificationException, ManagementException {
		Collection<String> tablesName = new ArrayList<String>();
		boolean b = false;
		boolean rb = true;
		try {
			b = this.sleeTransactionManagement.requireTransaction();

			tablesName = this.sleeProfileManagement.getDeclaredProfileTableNames(id);
			rb = false;
		} catch (UnrecognizedProfileSpecificationException x) {

			throw x;
		} catch (Exception x) {

			if (x instanceof ManagementException)
				throw (ManagementException) x;
			else
				throw new ManagementException("Failed createProfileTable", x);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

		return tablesName;
	}

	public Collection getProfiles(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		boolean b = false;
		boolean rb = true;
		Collection names = null;
		if (profileTableName == null)
			throw new NullPointerException("Argument[] must not be null");
		try {
			b = this.sleeTransactionManagement.requireTransaction();

			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			names = profileTable.getProfileNames();
			rb = false;
		} catch (UnrecognizedProfileTableNameException e) {

			throw e;

		} catch (Exception e) {
			if (e instanceof ManagementException)
				throw (ManagementException) e;
			throw new ManagementException("Failed to obtain ProfileNames for ProfileTable: " + profileTableName, e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

		return names;
	}

	public Collection getProfilesByAttribute(String arg0, String arg1, Object arg2) throws NullPointerException, UnrecognizedProfileTableNameException, UnrecognizedAttributeException,
			InvalidArgumentException, AttributeTypeMismatchException, ManagementException {
		// XXX: alex?
		return null;
	}

	public Collection getProfilesByDynamicQuery(String arg0, QueryExpression query) throws NullPointerException, UnrecognizedProfileTableNameException, UnrecognizedAttributeException,
			AttributeTypeMismatchException, ManagementException {
		// XXX: alex?
		return null;
	}

	public Collection getProfilesByIndexedAttribute(String arg0, String arg1, Object arg2) throws NullPointerException, UnrecognizedProfileTableNameException, UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException, ManagementException {
		// XXX: alex?
		return null;
	}

	/**
	 * Get a collection of ProfileID objects that identify the profiles
	 * contained in the specified profile table where the profiles satisfy a
	 * particular search criteria. The queryName argument identifies the search
	 * criteria by naming a static query predefined in the deployment descriptor
	 * of the profile specification from which the profile table was created.
	 * 
	 * Note: The profile table's default profile is not considered when
	 * determining matching profiles as it has no profile identifier that can be
	 * included in the collection returned by this method.
	 * 
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @param queryName
	 *            - the name of a static query defined in the profile table's
	 *            profile specification deployment descriptor.
	 * @param parameters
	 *            - an array of parameter values to apply to parameters in the
	 *            query. May only be null if the static query takes no
	 *            arguments.
	 * @return a collection of ProfileID objects identifying the profiles
	 *         contained in the specified profile table, where the profiles
	 *         match the search criteria defined in the named query.
	 * @throws java.lang.NullPointerException
	 *             - if either profileTable or queryName is null, if parameters
	 *             is null and the query requires parameters, or if any of the
	 *             provided parameter values are null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws UnrecognizedQueryNameException
	 *             - if the profile specification deployment descriptor of the
	 *             profile table does not declare a static query of the
	 *             specified query name
	 * @throws InvalidArgumentException
	 *             - if parameters is not null and its length does not match the
	 *             number of parameters defined by the query.
	 * @throws AttributeTypeMismatchException
	 *             - if the type of an attribute value included in the query
	 *             does not match the type of the attribute.
	 * @throws ManagementException
	 *             - if the profile identifiers could not be obtained due to a
	 *             system-level failure. Since: SLEE 1.1
	 */
	public Collection getProfilesByStaticQuery(java.lang.String profileTableName, java.lang.String queryName, java.lang.Object[] parameters) throws java.lang.NullPointerException,
			UnrecognizedProfileTableNameException, UnrecognizedQueryNameException, InvalidArgumentException, AttributeTypeMismatchException, ManagementException {
		boolean b = false;
		boolean rb = true;
		Collection profileIDs = null;
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must not be null");
		if (queryName == null)
			throw new NullPointerException("Argument[QueryName] must not be null");

		// Other NPE checks are done in handlers ?

		try {
			b = this.sleeTransactionManagement.requireTransaction();

			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);

			rb = false;
		} catch (UnrecognizedProfileTableNameException e) {

			throw e;

		} catch (Exception e) {
			if (e instanceof ManagementException)
				throw (ManagementException) e;
			throw new ManagementException("Failed to obtain ProfileNames for ProfileTable: " + profileTableName, e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

		return profileIDs;
	}

	/**
	 * Remove a profile from a profile table.
	 * 
	 * @param profileTableName
	 *            - the name of the profile table to remove the profile from.
	 * @param profileName
	 *            - the name of the profile to remove.
	 * @throws java.lang.NullPointerException
	 *             - if either argument is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws UnrecognizedProfileNameException
	 *             - if a profile with the specified name does not exist in the
	 *             profile table.
	 * @throws ManagementException
	 *             - if the profile could not be removed due to a system-level
	 *             failure.
	 */
	public void removeProfile(java.lang.String profileTableName, java.lang.String profileName) throws java.lang.NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException, ManagementException {

		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must nto be null");
		if (profileName == null)
			throw new NullPointerException("Argument[ProfileName] must nto be null");

		boolean b = false;
		boolean rb = true;
		try {
			b = this.sleeTransactionManagement.requireTransaction();
			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);
			if (profileTable.profileExists(profileName)) {
				throw new UnrecognizedProfileNameException("There is no such profile: " + profileName + ", in profile table: " + profileTableName);
			}

			profileTable.remove(profileName);
			rb = false;
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

	public void removeProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException {
		if (profileTableName == null)
			throw new NullPointerException("Argument[ProfileTableName] must nto be null");

		boolean b = false;
		boolean rb = true;
		try {
			b = this.sleeTransactionManagement.requireTransaction();
			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(profileTableName);

			profileTable.removeProfileTable();
			rb = false;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (Exception e) {
			throw new ManagementException("Failed to remove due to system level failure.", e);
		} finally {
			sleeTransactionManagement.requireTransactionEnd(b,rb);
		}

	}

	/**
	 * Rename a profile table.
	 * 
	 * @param oldProfileTableName
	 *            - the name of the profile table to rename.
	 * @param newProfileTableName
	 *            - the new name for the profile table.
	 * @throws java.lang.NullPointerException
	 *             - if either argument is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the name oldProfileTableName does
	 *             not exist.
	 * @throws InvalidArgumentException
	 *             - if newProfileTableName is zero-length or contains a '/'
	 *             character.
	 * @throws ProfileTableAlreadyExistsException
	 *             - if a profile table with the same name as
	 *             newProfileTableName already exists.
	 * @throws ManagementException
	 *             - if the profile table could not be renamed due to a
	 *             system-level failure.
	 */
	public void renameProfileTable(java.lang.String oldProfileTableName, java.lang.String newProfileTableName) throws java.lang.NullPointerException, UnrecognizedProfileTableNameException,
			InvalidArgumentException, ProfileTableAlreadyExistsException, ManagementException {

		if (oldProfileTableName == null)
			throw new NullPointerException("Argument[OldProfileTableName] must nto be null");
		if (newProfileTableName == null)
			throw new NullPointerException("Argument[NewProfileTableName] must nto be null");

		// Double check for null, also it will propably be done inside methods
		// invoked lower in this method.
		ProfileTableImpl.validateProfileTableName(newProfileTableName);

		boolean b = false;
		boolean rb = true;
		try {
			b = this.sleeTransactionManagement.requireTransaction();
			ProfileTableConcrete profileTable = this.sleeProfileManagement.getProfileTable(oldProfileTableName);
			try {
				this.sleeProfileManagement.getProfileTable(newProfileTableName);
				if (true)
					throw new ProfileTableAlreadyExistsException("ProfileTable with name: " + newProfileTableName);
			} catch (UnrecognizedProfileTableNameException e) {
				// its ok :)
			}
			profileTable.removeProfileTable();
			createProfileTable(profileTable.getProfileSpecificationComponent().getProfileSpecificationID(), newProfileTableName);
			rb = false;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (Exception e) {
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