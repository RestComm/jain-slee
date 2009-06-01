/*
 * **************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import java.util.Collection;

import javax.slee.TransactionRolledbackLocalException;
import javax.slee.facilities.FacilityException;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * JNDI Location java:comp/env/slee/facilities/profile
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * 
 */
public class ProfileFacilityImpl implements ProfileFacility {

	private static Logger logger = Logger.getLogger(ProfileFacilityImpl.class);

	public static final String JNDI_NAME = "profile";

	private final SleeContainer sleeContainer;

	/**
     *  
     */
	public ProfileFacilityImpl(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
	}

	/**
	 * Get a ProfileTable object for a profile table. The object returned by
	 * this method may be safely typecast to the Profile Table Interface defined
	 * by the profile specification of the profile table if the SBB has the
	 * appropriate classes in its classloader to do so, for example by declaring
	 * a profile-spec-ref in its deployment descriptor for the profile
	 * specification of the Profile Table.
	 * 
	 * This method is a non-transactional method.
	 * 
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @return a ProfileTable object for the profile table.
	 * @throws java.lang.NullPointerException
	 *             - if profileTableName is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws FacilityException
	 *             - if the ProfileTable object could not be obtained due to a
	 *             system-level-failure.
	 */
	public ProfileTable getProfileTable(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			FacilityException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfileTable( profileTableName = "
					+ profileTableName + " )");
		}
				
		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();		
		boolean terminateTx = sleeTransactionManager.requireTransaction();
		try {
			return sleeContainer
			.getSleeProfileTableManager().getProfileTable(profileTableName);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (Throwable e) {
			throw new FacilityException("Failed to obtain profile table.", e);
		} finally {
			// never rollback
			try {
				sleeTransactionManager.requireTransactionEnd(terminateTx,false);	
			} catch (Throwable e) {
				throw new FacilityException(e.getMessage(),e);
			}	
		}

	}

	/**
	 * Get a collection of ProfileID objects that identify all the profiles
	 * contained in the specified profile table. The collection returned is
	 * immutable. Any attempt to modify it, either directly or indirectly, will
	 * result in a java.lang.UnsupportedOperationException being thrown.
	 * 
	 * Note: A profile identifier for the profile table's default profile will
	 * not be included in the collection returned by this method as the default
	 * profile has no such identifier.
	 * 
	 * This method is a required transactional method.
	 * 
	 * @deprecated
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @return a read-only collection of ProfileID objects identifying the
	 *         profiles contained in the specified profile table.
	 * @throws java.lang.NullPointerException
	 *             - if profileTableName is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws TransactionRolledbackLocalException
	 *             - if this method was invoked without a valid transaction
	 *             context and the transaction started by this method failed to
	 *             commit.
	 * @throws FacilityException
	 *             - if the profile identifies could not be obtained due to a
	 *             system-level failure.
	 */
	public Collection<ProfileID> getProfiles(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			TransactionRolledbackLocalException, FacilityException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfiles( profileTableName = "
					+ profileTableName + " )");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();

		try {
			return sleeContainer.getSleeProfileTableManager().getProfileTable(
							profileTableName).getProfiles();			
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;		
		} catch (Throwable e) {
			throw new FacilityException(e.getMessage(), e);
		}
	}

	/**
	 * Get a ProfileTableActivity object for a profile table.
	 * 
	 * This method is a required transactional method.
	 * 
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @return a ProfileTableActivity object for the profile table.
	 * @throws java.lang.NullPointerException
	 *             - if profileTableName is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws TransactionRolledbackLocalException
	 *             - if this method was invoked without a valid transaction
	 *             context and the transaction started by this method failed to
	 *             commit.
	 * @throws FacilityException
	 *             - if the activity could not be obtained due to a system-level
	 *             failure. This exception is also thrown if the method is
	 *             invoked on a ProfileFacility object provided to a resource
	 *             adaptor via its ResourceAdaptorContext.
	 */
	public ProfileTableActivity getProfileTableActivity(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			TransactionRolledbackLocalException, FacilityException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfileTableActivity( profileTableName = "
					+ profileTableName + " )");
		}
				
		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();		
		boolean terminateTx = sleeTransactionManager.requireTransaction();
		try {
			return sleeContainer
			.getSleeProfileTableManager().getProfileTable(profileTableName).getActivity();
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (Throwable e) {
			throw new FacilityException("Failed to obtain profile table.", e);
		} finally {
			// never rollback
			try {
				sleeTransactionManager.requireTransactionEnd(terminateTx,false);	
			} catch (Throwable e) {
				throw new FacilityException(e.getMessage(),e);
			}
		}		
	}

	/**
	 * Get a ProfileID object that identifies the profile contained in the
	 * specified profile table, where the specified profile attribute is set to
	 * the specified value. In the case of a profile attribute of an array type,
	 * the type of the specified value must be the base component type of the
	 * array, not the array type itself, and the SLEE will return the profile
	 * identifier of any profile that contains the value within the array.
	 * 
	 * Note: The profile table's default profile is not considered when
	 * determining matching profiles as it has no profile identifier that can be
	 * returned by this method.
	 * 
	 * This method is a required transactional method.
	 * 
	 * This method can only be invoked against profile tables created from SLEE
	 * 1.0 profile specifications. Attempting to invoke it on a profile table
	 * created from a SLEE 1.1 profile specification causes a FacilityException
	 * to be thrown.
	 * 
	 * @deprecated
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @param attributeName
	 *            - the name of the profile's attribute to check.
	 * @param attributeValue
	 *            - the value to compare the attribute with. Returns: the
	 *            profile identifier for the first matching profile, or null if
	 *            no matching profile was found.
	 * @throws java.lang.NullPointerException
	 *             - if any attribute is null.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if a profile table with the specified name does not exist.
	 * @throws UnrecognizedAttributeException
	 *             - if an attribute with the specified name is not defined in
	 *             the profile specification for the specified profile table.
	 * @throws AttributeNotIndexedException
	 *             - if the specified attribute is not indexed in the profile
	 *             specification for the specified profile table.
	 * @throws AttributeTypeMismatchException
	 *             - if the type of the supplied attribute value does not match
	 *             the type of the specified indexed attribute.
	 * @throws TransactionRolledbackLocalException
	 *             - if this method was invoked without a valid transaction
	 *             context and the transaction started by this method failed to
	 *             commit.
	 * @throws FacilityException
	 *             - if the profile identifier could not be obtained due to a
	 *             system-level failure.
	 */
	public ProfileID getProfileByIndexedAttribute(
			java.lang.String profileTableName, java.lang.String attributeName,
			java.lang.Object attributeValue) throws NullPointerException,
			UnrecognizedProfileTableNameException,
			UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException,
			TransactionRolledbackLocalException, FacilityException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfileByIndexedAttribute( profileTableName = "
					+ profileTableName + " , attributeName = " + attributeName
					+ " , attributeValue = " + attributeValue + " )");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();

		try {
			ProfileTableImpl profileTable = sleeContainer
					.getSleeProfileTableManager().getProfileTable(
							profileTableName);
			if (profileTable.getProfileSpecificationComponent().isSlee11()) {
				throw new FacilityException(
						"JAIN SLEE 1.1 Specs forbidden the usage of this method on SLEE 1.1 Profile Tables");
			}
			Collection<ProfileID> profileIDs = profileTable.getProfilesByAttribute(attributeName,attributeValue,false);
			if (profileIDs.isEmpty()) {
				return null;
			}
			else {
				return profileIDs.iterator().next();
			}
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedAttributeException e) {
			throw e;
		} catch (AttributeNotIndexedException e) {
			throw e;
		} catch (AttributeTypeMismatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new FacilityException(e.getMessage(), e);
		}
	}

	/**
	 * Get a collection of ProfileID objects that identify the profiles
	 * contained in the specified profile table where the specified profile
	 * attribute is set to the specified value. In the case of a profile
	 * attribute of an array type, the type of the specified value must be the
	 * base component type of the array, not the array type itself, and the SLEE
	 * will return the profile identifier of any profile that contains the value
	 * within the array.
	 * 
	 * The collection returned is immutable. Any attempt to modify it, either
	 * directly or indirectly, will result in a
	 * java.lang.UnsupportedOperationException being thrown.
	 * 
	 * Note: The profile table's default profile is not considered when
	 * determining matching profiles as it has no profile identifier that can be
	 * included in the collection returned by this method.
	 * 
	 * This method is a required transactional method.
	 * 
	 * This method can only be invoked against profile tables created from SLEE
	 * 1.0 profile specifications. Attempting to invoke it on a profile table
	 * created from a SLEE 1.1 profile specification causes a FacilityException
	 * to be thrown.
	 * 
	 * @deprecated
	 * @param profileTableName
	 *            - the name of the profile table.
	 * @param attributeName
	 *            - the name of the profile's attribute to check.
	 * @param attributeValue
	 *            - the value to compare the attribute with.
	 * @return a read-only collection of ProfileID objects identifying the
	 *         profiles contained in the specified profile table, where the
	 *         specified attribute of each profile equals the specified value.
	 * @throw java.lang.NullPointerException - if any argument is null.
	 * @throw UnrecognizedProfileTableNameException - if a profile table with
	 *        the specified name does not exist.
	 * @throw UnrecognizedAttributeException - if an attribute with the
	 *        specified name is not defined in the profile specification for the
	 *        specified profile table.
	 * @throw AttributeNotIndexedException - if the specified attribute is not
	 *        indexed in the profile specification for the specified profile
	 *        table.
	 * @throw AttributeTypeMismatchException - if the type of the supplied
	 *        attribute value does not match the type of the specified indexed
	 *        attribute.
	 * @throw TransactionRolledbackLocalException - if this method was invoked
	 *        without a valid transaction context and the transaction started by
	 *        this method failed to commit.
	 * @throw FacilityException - if the profile identifiers could not be
	 *        obtained due to a system-level failure.
	 */
	public Collection<ProfileID> getProfilesByIndexedAttribute(
			java.lang.String profileTableName, java.lang.String attributeName,
			java.lang.Object attributeValue)
			throws java.lang.NullPointerException,
			UnrecognizedProfileTableNameException,
			UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException,
			TransactionRolledbackLocalException, FacilityException {

		if (logger.isDebugEnabled()) {
			logger.debug("getProfilesByIndexedAttribute( profileTableName = "
					+ profileTableName + " , attributeName = " + attributeName
					+ " , attributeValue = " + attributeValue + " )");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();

		try {
			ProfileTableImpl profileTable = sleeContainer
					.getSleeProfileTableManager().getProfileTable(
							profileTableName);
			if (profileTable.getProfileSpecificationComponent().isSlee11()) {
				throw new FacilityException(
						"JAIN SLEE 1.1 Specs forbidden the usage of this method on SLEE 1.1 Profile Tables");
			}
			return profileTable.getProfilesByAttribute(attributeName,attributeValue,false);
		} catch (NullPointerException e) {
			throw e;
		} catch (UnrecognizedProfileTableNameException e) {
			throw e;
		} catch (UnrecognizedAttributeException e) {
			throw e;
		} catch (AttributeNotIndexedException e) {
			throw e;
		} catch (AttributeTypeMismatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new FacilityException(e.getMessage(), e);
		}
	}

	public String _toString() {
		// get profile manager
		// FIXME:
		String tableNames = "";
		return "Profile Facility: " + "\n+-- Tables: " + tableNames;
	}

}