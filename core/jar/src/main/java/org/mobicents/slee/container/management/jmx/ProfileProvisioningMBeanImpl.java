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

import java.util.Collection;
import java.util.HashSet;

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ManagementException;
import javax.slee.management.SleeState;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.search.SearchExpression;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.profile.SingleProfileException;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * MBean class for profile provisioning through jmx
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * 
 */
public class ProfileProvisioningMBeanImpl extends ServiceMBeanSupport implements
		ProfileProvisioningMBeanImplMBean {

	private static Logger logger;

	// private SleeProfileManager profileManager;

	static {
		try {
			logger = Logger.getLogger(ProfileProvisioningMBeanImpl.class);

		} catch (Exception ex) {
			logger.fatal("error initializing profile provisioning mbean");
		}
	}

	public ProfileProvisioningMBeanImpl() throws NotCompliantMBeanException {
		super(ProfileProvisioningMBeanImplMBean.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#createProfileTable(javax.slee.profile.ProfileSpecificationID,
	 *      java.lang.String)
	 */
	public void createProfileTable(
			ProfileSpecificationID profileSpecificationID,
			String newProfileTableName) throws NullPointerException,
			UnrecognizedProfileSpecificationException,
			InvalidArgumentException, ProfileTableAlreadyExistsException,
			ManagementException {
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// check mandated by SLEE TCK test CreateActivityWhileStoppingTest
		if (!sleeContainer.getSleeState().equals(SleeState.RUNNING))
			return;

		/*
		 * if(profileManager==null){ this.profileManager = new
		 * SleeProfileManager(serviceContainer.getMBeanServer());
		 * SleeContainer.registerFacilityWithJndi(SleeProfileManager.JNDI_NAME,
		 * profileManager); }
		 */
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			logger.debug("creating new Profile Table " + newProfileTableName
					+ " ...");
			logger.debug("profile specification ID :"
					+ profileSpecificationID.toString());
			if (newProfileTableName == null)
				throw new NullPointerException();
			if (newProfileTableName.length() < 1) {
				log.error("InvaidArgument: " + newProfileTableName);
				throw new InvalidArgumentException();
			}
			if (newProfileTableName.indexOf('/') != -1) {
				log.error("InvaidArgument: " + newProfileTableName);
				throw new InvalidArgumentException();
			}
			if (profileTableHasInvalidCharacters(newProfileTableName)) {
				log.error("InvaidArgument: " + newProfileTableName);
				throw new InvalidArgumentException();
			}

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			ProfileSpecificationID profileSpecification;
			try {
				profileSpecification = profileManager
						.findProfileSpecId(newProfileTableName);
				if (profileSpecification != null)
					throw new ProfileTableAlreadyExistsException(
							"Failed creating Profile Table "
									+ newProfileTableName + " in "
									+ profileSpecificationID);
			} catch (SystemException e1) {
				String err = "Failed to create profile table, because of failure in findProfuleTable("
						+ newProfileTableName + "). System Exception.";
				logger.error(err, e1);
				throw new ManagementException(err, e1);
			}

			logger.debug("Profile table does not exist -- creating one  "
					+ profileSpecificationID.toString());

			// get the profile specification descriptor from the deployment
			// manager class
			ProfileSpecificationDescriptorImpl profileSpecificationDescriptor = profileManager
					.getProfileSpecificationManagement()
					.getProfileSpecificationDescriptor(profileSpecificationID);
			if (profileSpecificationDescriptor == null)
				throw new UnrecognizedProfileSpecificationException();

			// switch the context classloader to the DU cl
			ClassLoader oldClassLoader = Thread.currentThread()
					.getContextClassLoader();

			try {
				Thread.currentThread().setContextClassLoader(
						profileSpecificationDescriptor.getClassLoader());

				profileManager.addProfileTable(newProfileTableName,
						profileSpecificationDescriptor);
			} catch (TransactionRequiredLocalException e) {
				throw new ManagementException("Transaction Manager Failure", e);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			} catch (ClassNotFoundException e) {
				throw new ManagementException(
						"Profile Specification has not been correctly deployed",
						e);
			} catch (Exception e) {
				throw new ManagementException(e.getMessage(), e);
			} finally {
				Thread.currentThread().setContextClassLoader(oldClassLoader);
			}
			logger.debug("new Profile Table " + newProfileTableName
					+ " created");
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
			if (b)
				try {
					transactionManager.commit();
				} catch (SystemException e) {
					logger.error("System Exception", e);
					throw new ManagementException("System Exception", e);
				}
		}
	}

	/**
	 * Check if some invalid characters exists in the profile table name
	 * 
	 * @param profileTableName
	 *            the profile table name to check
	 * @return true if an invalid character is found in the profile table name
	 */
	private boolean profileTableHasInvalidCharacters(String profileTableName) {
		for (char invalidChar = 0x0000; invalidChar < 0x001f; invalidChar++) {
			if (profileTableName.indexOf(invalidChar) != -1) {
				logger.debug("Profile Table Name " + profileTableName + " "
						+ "contains the following invalid character "
						+ invalidChar);
				return true;
			}
		}
		if (profileTableName.indexOf('/') != -1) {
			logger.debug("Profile Table Name " + profileTableName + ""
					+ " contains the following invalid character /");
			return true;
		}
		if (profileTableName.indexOf('\u007f') != -1) {
			logger.debug("Profile Table Name " + profileTableName + " "
					+ "contains the following invalid character \u007f");
			return true;
		}
		return false;
	}

	/**
	 * Check if some invalid characters exists in the profile name
	 * 
	 * @param profileName
	 *            the profile name to check
	 * @return true if an invalid character is found in the profile name
	 */
	private boolean profileHasInvalidCharacters(String profileName) {
		for (char invalidChar = 0x0000; invalidChar < 0x001f; invalidChar++) {
			if (profileName.indexOf(invalidChar) != -1) {
				logger.debug("Profile Name " + profileName + " "
						+ "contains the following invalid character "
						+ invalidChar);
				return true;
			}
		}
		if (profileName.indexOf('\u007f') != -1) {
			logger.debug("Profile Name " + profileName + " "
					+ "contains the following invalid character \u007f");
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#removeProfileTable(java.lang.String)
	 */
	public void removeProfileTable(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			ManagementException {
		logger.debug("removing Profile Table " + profileTableName + " ...");
		if (profileTableName == null) {
			logger
					.error("Cannot remove profile table when passed name equals null!!!");
			throw new NullPointerException();
		}
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		boolean rb = true;
		try {
			b = transactionManager.requireTransaction();

			// check mandated by SLEE TCK test CreateActivityWhileStoppingTest
			if (!sleeContainer.getSleeState().equals(SleeState.RUNNING))
				return;

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			;
			ProfileSpecificationID profileSpecificationID;
			try {
				profileSpecificationID = profileManager
						.findProfileSpecId(profileTableName);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			}
			if (profileSpecificationID == null)
				throw new UnrecognizedProfileTableNameException(
						"Failed removing Profile Table " + profileTableName);

			try {
				profileManager.removeProfileTable(profileTableName);
			} catch (TransactionRequiredLocalException e) {
				throw new ManagementException("Transaction Manager Failure", e);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			}
			logger.debug("Removed Profile Table " + profileTableName);

			rb = false;
		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				throw new ManagementException("Failed removeProfileTable( "
						+ profileTableName + ") due to tx commit error", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfileSpecification(java.lang.String)
	 */
	public ProfileSpecificationID getProfileSpecification(
			String profileTableName) throws NullPointerException,
			UnrecognizedProfileTableNameException, ManagementException {
		logger.debug("trying to get the profile specification for "
				+ profileTableName + " ...");
		if (profileTableName == null)
			throw new NullPointerException();
		SleeProfileManager profileManager = SleeContainer.lookupFromJndi()
				.getSleeProfileManager();
		ProfileSpecificationID profileSpecificationID;
		try {
			profileSpecificationID = profileManager
					.findProfileSpecId(profileTableName);
		} catch (SystemException e) {
			throw new ManagementException("System-level failure", e);
		}
		if (profileSpecificationID == null)
			throw new UnrecognizedProfileTableNameException();
		logger.debug("profile specification for " + profileTableName
				+ " found :" + profileSpecificationID);

		return profileSpecificationID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#renameProfileTable(java.lang.String,
	 *      java.lang.String)
	 */
	public void renameProfileTable(String oldProfileTableName,
			String newProfileTableName) throws NullPointerException,
			UnrecognizedProfileTableNameException, InvalidArgumentException,
			ProfileTableAlreadyExistsException, ManagementException {
		logger.debug("Profile Table " + oldProfileTableName
				+ " to be renamed to " + newProfileTableName);
		if (newProfileTableName == null)
			throw new NullPointerException();
		if (newProfileTableName.length() < 1)
			throw new InvalidArgumentException();
		if (newProfileTableName.indexOf('/') != -1)
			throw new InvalidArgumentException();

		ProfileSpecificationID profileSpecificationID = getProfileSpecification(oldProfileTableName);
		// get the profile specification descriptor from the deployment manager
		// class
		ProfileSpecificationDescriptorImpl profileSpecificationDescriptor = null;
		SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
		SleeProfileManager profileManager = serviceContainer
				.getSleeProfileManager();
		profileSpecificationDescriptor = profileManager
				.getProfileSpecificationManagement()
				.getProfileSpecificationDescriptor(profileSpecificationID);
		if (profileSpecificationDescriptor == null)
			throw new UnrecognizedProfileTableNameException();

		ProfileSpecificationID profileSpecification;
		try {
			profileSpecification = profileManager
					.findProfileSpecId(newProfileTableName);
			if (profileSpecification != null)
				throw new ProfileTableAlreadyExistsException();
		} catch (SystemException e1) {
			e1.printStackTrace();
			throw new ManagementException("System-level failure");
		}

		try {
			profileManager.renameProfileTable(oldProfileTableName,
					newProfileTableName, profileSpecificationDescriptor);
		} catch (TransactionRequiredLocalException e) {
			throw new ManagementException("System-level failure", e);
		} catch (SystemException e) {
			throw new ManagementException("System-level failure", e);
		} catch (ClassNotFoundException e) {
			throw new ManagementException("System-level failure", e);
		} catch (Exception e) {
			throw new ManagementException(e.getMessage(), e);
		}
		/*
		 * removeProfileTable(oldProfileTableName); try {
		 * profileManager.addProfileTable( newProfileTableName,
		 * profileSpecificationDescriptor); } catch
		 * (TransactionRequiredLocalException e) { throw new
		 * ManagementException("Transaction Manager Failure"); } catch
		 * (SystemException e) { throw new ManagementException("System-level
		 * failure"); }
		 */
		logger.debug("Profile Table " + oldProfileTableName + " renamed to "
				+ newProfileTableName);
	}

	/**
	 * @return ObjectName - The default profile MBean object name if the profile
	 *         table has been created. Otherwise null.
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#getDefaultProfile(java.lang.String)
	 */
	public ObjectName getDefaultProfile(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			ManagementException {

		if (profileTableName == null)
			throw new NullPointerException();

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		boolean rb = true;
		try {
			b = transactionManager.requireTransaction();

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			;
			try {
				Object profile = profileManager
						.findDefaultProfile(profileTableName);
				if (profile == null)
					throw new UnrecognizedProfileTableNameException();
				rb = false;
				ObjectName on = getDefaultProfileObjectName(profileTableName);
				return on;
			} catch (MalformedObjectNameException e) {
				throw new ManagementException("System-level failure", e);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			}

		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				throw new ManagementException("Failed removeProfileTable( "
						+ profileTableName + ") due to tx commit error", e);
			}
		}

	}

	/**
	 * SLEE 1.0 spec, section 14.11: This method gets the JMX Object Name of a
	 * modifiable Profile MBean object. When the Profile MBean object commits
	 * successfully, the SLEE creates and adds a Profile with the name specified
	 * by the newProfileName argument to the Profile Table specified by the
	 * profileTableName argument. Before the Profile MBean object commits, the
	 * Profile name specifed by the newProfileName argument does not exist and
	 * cannot be accessed by SBBs or through the ProfileProvisioningMBean
	 * interface.
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#createProfile(java.lang.String,
	 *      java.lang.String)
	 */
	public ObjectName createProfile(String profileTableName,
			String newProfileName) throws NullPointerException,
			UnrecognizedProfileTableNameException, InvalidArgumentException,
			ProfileAlreadyExistsException, ManagementException {

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		boolean rb = true;

		ObjectName objectName;
		try {

			b = transactionManager.requireTransaction();

			String infoStr = "Creating new Profile " + newProfileName
					+ " in profile table " + profileTableName;
			String errorStr = "Failed " + infoStr;
			logger.debug(infoStr);
			if (profileTableName == null)
				throw new NullPointerException();
			if (newProfileName == null)
				throw new NullPointerException();
			if (newProfileName.length() < 1)
				throw new InvalidArgumentException();
			if (profileHasInvalidCharacters(newProfileName))
				throw new InvalidArgumentException();
			// FIXME As the profile is stored under tree cache the character /
			// is important so it is replace by a space in the profile name,
			// find a workaround
			newProfileName = newProfileName.replace('/', ' ');

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			ProfileSpecificationID profileSpecificationID;
			ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl;
			try {
				profileSpecificationID = profileManager
						.findProfileSpecId(profileTableName);
				profileSpecificationDescriptorImpl = profileManager
						.getProfileSpecificationManagement()
						.getProfileSpecificationDescriptor(
								profileSpecificationID);
			} catch (SystemException e) {
				throw new ManagementException(errorStr, e);
			}
			if (profileSpecificationID == null)
				throw new UnrecognizedProfileTableNameException(errorStr);
			Object profile;
			try {
				// see if the profile MBean was created, but not committed
				profile = profileManager.findProfileMBean(profileTableName,
						newProfileName);
				if (profile != null)
					throw new ProfileAlreadyExistsException(errorStr);
				// see if the profile MBean was closed/removed, but the profile
				// itself is still visible in SLEE
				if (profileManager.isProfileCommitted(profileTableName,
						newProfileName)) {
					throw new ProfileAlreadyExistsException(errorStr);
				}
			} catch (SystemException e1) {
				throw new ManagementException(errorStr, e1);
			}
			objectName = null;

			// store class loader
			Thread currentThread = Thread.currentThread();
			ClassLoader currentClassLoader = currentThread
					.getContextClassLoader();
			try {
				// change class loader
				currentThread
						.setContextClassLoader(profileSpecificationDescriptorImpl
								.getClassLoader());
				// since all validation checks pass, lets try to create the
				// profile
				objectName = profileManager.addProfileToProfileTable(
						profileTableName, newProfileName);
			} catch (TransactionRequiredLocalException e) {
				throw new ManagementException(errorStr, e);
			} catch (SystemException e) {
				throw new ManagementException(errorStr, e);
			} catch (SingleProfileException e) {
				throw new ManagementException(
						"This profile specification defines "
								+ "that it can exists only a single profile for it and "
								+ "there is already a profile for this specification."
								+ errorStr, e);
			} finally {
				// restore class loader
				currentThread.setContextClassLoader(currentClassLoader);
			}
			logger.debug(infoStr
					+ "DONE. The profile has the following JMX Object Name "
					+ objectName);
			rb = false;
		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				logger.error("Failed getProfiles.", e);
				throw new ManagementException("Failed getProfiles.", e);
			}
		}
		return objectName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#removeProfile(java.lang.String,
	 *      java.lang.String)
	 */
	public void removeProfile(String profileTableName, String profileName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException, ManagementException {

		logger.debug("Removing Profile " + profileName + " from profile table "
				+ profileTableName);
		if (profileTableName == null)
			throw new NullPointerException();
		if (profileName == null)
			throw new NullPointerException();
		// FIXME As the profile is stored under jboss cache the character / is
		// important so it is replace by a space in the profile name, find a
		// workaround
		profileName = profileName.replace('/', ' ');

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		boolean rb = true;
		try {
			b = transactionManager.requireTransaction();

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			ProfileSpecificationID profileSpecificationID;
			try {
				profileSpecificationID = profileManager
						.findProfileSpecId(profileTableName);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure");
			}
			if (profileSpecificationID == null)
				throw new UnrecognizedProfileTableNameException(
						"Failed removing Profile " + profileName
								+ " from Profile Table " + profileTableName);
			Object profileMBeanName;
			profileMBeanName = profileManager.findProfileMBean(
					profileTableName, profileName);
			if (profileMBeanName == null)
				throw new UnrecognizedProfileNameException(
						"Failed removing Profile " + profileName
								+ " from Profile Table " + profileTableName);

			try {
				profileManager.removeProfile(profileTableName, profileName);
			} catch (TransactionRequiredLocalException e) {
				throw new ManagementException("Transaction Manager Failure", e);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			}
			logger.debug("Profile " + profileName
					+ " removed from profile table " + profileTableName);

			rb = false;

		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				throw new ManagementException("Failed removeProfileTable( "
						+ profileTableName + ") due to tx commit error", e);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfile(java.lang.String,
	 *      java.lang.String)
	 */
	public ObjectName getProfile(String profileTableName, String profileName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException, ManagementException {

		logger.debug("Getting Profile " + profileName + " from profile table "
				+ profileTableName);
		if (profileTableName == null)
			throw new NullPointerException();
		if (profileName == null)
			throw new NullPointerException();
		// FIXME As the profile is stored under jboss cache the character / is
		// important so it is replace by a space in the profile name, find a
		// workaround
		profileName = profileName.replace('/', ' ');

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();
		boolean b = false;
		boolean rb = true;
		try {
			b = transactionManager.requireTransaction();

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();

			ProfileSpecificationID profileSpecificationID;
			try {
				profileSpecificationID = profileManager
						.findProfileSpecId(profileTableName);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure", e);
			}
			if (profileSpecificationID == null)
				throw new UnrecognizedProfileTableNameException();

			Object profile;
			try {
				profile = profileManager.findProfileMBean(profileTableName,
						profileName);
				if (profile == null)
					throw new UnrecognizedProfileNameException(
							"Profile MBean does not exist.");

				if (!profileManager.isProfileCommitted(profileTableName,
						profileName)) {
					throw new UnrecognizedProfileNameException(
							"Profile has been created but not committed.");
				}
			} catch (SystemException e1) {
				throw new ManagementException("System-level failure", e1);
			}

			ObjectName objectName = null;
			try {
				objectName = getProfileObjectName(profileTableName, profileName);
			} catch (MalformedObjectNameException e2) {
				throw new ManagementException("System-level failure", e2);
			} catch (NullPointerException e2) {
				// should never be caught
				throw new ManagementException("System-level failure", e2);
			}
			logger.debug("Profile " + profileName + " from profile table "
					+ profileTableName + " has the following JMX Object Name "
					+ objectName);

			rb = false;

			return objectName;

		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				throw new ManagementException("Failed removeProfileTable( "
						+ profileTableName + ") due to tx commit error", e);
			}
		}

	}

	/**
	 * 
	 * Creates a JMX ObjectName for a profile, given its profile name and
	 * profile table name
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return
	 * @throws MalformedObjectNameException
	 */
	public static ObjectName getProfileObjectName(String profileTableName,
			String profileName) throws MalformedObjectNameException {
		ObjectName objectName;
		String jmxProfileTableObjectName = SleeProfileManager
				.toValidJmxName(profileTableName);
		String jmxProfileObjectName = SleeProfileManager
				.toValidJmxName(profileName);
		objectName = new ObjectName("slee:" + "profileTableName="
				+ jmxProfileTableObjectName + "," + "type=profile,"
				+ "profile=" + jmxProfileObjectName);
		return objectName;
	}

	public static ObjectName getDefaultProfileObjectName(String profileTableName)
			throws MalformedObjectNameException {
		return getProfileObjectName(profileTableName, "defaultProfile");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfileTables()
	 */
	public Collection getProfileTables() throws ManagementException {

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();

		Collection profileTables = null;
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			profileTables = profileManager.findAllProfileTables();
			profileTables = new HashSet(profileTables);
		} catch (Exception x) {
			try {
				transactionManager.setRollbackOnly();
			} catch (SystemException e) {
				logger.error("System Exception", e);
				throw new ManagementException("System Exception", e);
			}
			if (x instanceof ManagementException)
				throw (ManagementException) x;
			else
				throw new ManagementException("Failed createProfileTable", x);
		} finally {
			if (b)
				try {
					transactionManager.commit();
				} catch (SystemException e) {
					logger.error("System Exception", e);
					throw new ManagementException("System Exception", e);
				}
		}
		// a serializable copy is needed. the original reference has ties to the
		// distributed cache
		return profileTables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfiles(java.lang.String)
	 */
	public Collection getProfiles(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			ManagementException {

		Collection profiles = null;

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();

		boolean b = false;
		boolean rb = true;
		try {
			b = transactionManager.requireTransaction();
			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			if (profileTableName == null)
				throw new NullPointerException();
			ProfileSpecificationID profileSpecificationID;
			try {
				profileSpecificationID = profileManager
						.findProfileSpecId(profileTableName);
			} catch (SystemException e) {
				throw new ManagementException("System-level failure");
			}
			if (profileSpecificationID == null)
				throw new UnrecognizedProfileTableNameException();
			profiles = profileManager
					.findAllProfilesByTableName(profileTableName);

			rb = false;
		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				logger.error("Failed getProfiles.", e);
				throw new ManagementException("Failed getProfiles.", e);
			}
		}
		return profiles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ProfileProvisioningMBean#getProfilesByIndexedAttribute(java.lang.String,
	 *      java.lang.String, java.lang.Object)
	 */
	public Collection getProfilesByIndexedAttribute(String profileTableName,
			String attributeName, Object attributeValue)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException, ManagementException {

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager transactionManager = sleeContainer
				.getTransactionManager();

		Collection profiles = null;

		boolean b = false;
		boolean rb = true;
		try {
			b = transactionManager.requireTransaction();

			if (profileTableName == null)
				throw new NullPointerException();
			if (attributeName == null)
				throw new NullPointerException();
			if (attributeValue == null)
				throw new NullPointerException();

			SleeProfileManager profileManager = sleeContainer
					.getSleeProfileManager();
			ProfileSpecificationID profileSpecification;
			try {
				profileSpecification = profileManager
						.findProfileSpecId(profileTableName);
				if (profileSpecification == null)
					throw new UnrecognizedProfileTableNameException();
			} catch (SystemException e) {
				throw new ManagementException("System-level failure");
			}

			try {
				profiles = profileManager.getProfilesByIndexedAttribute(
						profileTableName, attributeName, attributeValue, false);
			} catch (SystemException e1) {
				throw new ManagementException("System-level failure");
			}
			;

			rb = false;
		} finally {
			try {
				if (rb)
					transactionManager.setRollbackOnly();
				if (b)
					transactionManager.commit();
			} catch (SystemException e) {
				logger.error("Failed getProfiles.", e);
				throw new ManagementException("Failed getProfiles.", e);
			}
		}
		return profiles;
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

	public Collection getProfilesByDynamicQuery(String arg0,
			SearchExpression arg1) throws NullPointerException,
			UnrecognizedProfileTableNameException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getProfilesByStaticQuery(String arg0, String arg1,
			Object[] arg2) throws NullPointerException,
			UnrecognizedProfileTableNameException,
			UnrecognizedQueryNameException, AttributeTypeMismatchException,
			ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	public void snapshot() throws ManagementException {
		// TODO Auto-generated method stub

	}

	public void snapshot(String arg0)
			throws UnrecognizedProfileTableNameException, ManagementException {
		// TODO Auto-generated method stub

	}

}
