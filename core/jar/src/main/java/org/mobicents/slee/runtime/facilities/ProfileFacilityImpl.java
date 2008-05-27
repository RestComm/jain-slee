/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileFacilityImpl.java
 * 
 * Created on Oct 16, 2004
 *
 */
package org.mobicents.slee.runtime.facilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.slee.TransactionRolledbackLocalException;
import javax.slee.facilities.FacilityException;
import javax.slee.management.SleeState;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * JNDI Location java:comp/env/slee/facilities/profile
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 *  
 */
public class ProfileFacilityImpl implements ProfileFacility {

    private static Logger logger = Logger.getLogger(ProfileFacilityImpl.class);

    public static final String JNDI_NAME = "profile";

    /**
     *  
     */
    public ProfileFacilityImpl() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.profile.ProfileFacility#getProfiles(java.lang.String)
     */
    public Collection getProfiles(String profileTableName)
            throws NullPointerException, UnrecognizedProfileTableNameException,
            TransactionRolledbackLocalException, FacilityException {
        SleeProfileManager profileManager = SleeProfileManager.getInstance();

        if (profileTableName == null)
            throw new NullPointerException();
        ProfileSpecificationID profileSpecificationID;
        try {
            profileSpecificationID = profileManager
                    .findProfileSpecId(profileTableName);
        } catch (SystemException e) {
            throw new FacilityException("System-level failure");
        }
        if (profileSpecificationID == null)
            throw new UnrecognizedProfileTableNameException();

        //9.6.2 Required transactional methods : All ProfileFacility interface
        // methods.
        SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
        boolean startedTx = txMgr.requireTransaction();

        Collection profiles = profileManager
                .findAllProfilesByTableName(profileTableName);

        Collection profileIDs = new ArrayList();
        Iterator it = profiles.iterator();
        while (it.hasNext()) {
            profileIDs.add(new ProfileID(profileTableName, (String) it.next()));
        }
        Collection immutableCollection = Collections
                .unmodifiableCollection(profileIDs);
        if (startedTx) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("started tx so committing it");
                }
                SleeContainer.getTransactionManager().commit();
            } catch (Exception e) {
                throw new TransactionRolledbackLocalException(
                        "Failed to commit transaction");
            }
        }
        return immutableCollection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.profile.ProfileFacility#getProfilesByIndexedAttribute(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public Collection getProfilesByIndexedAttribute(String profileTableName,
            String attributeName, Object attributeValue)
            throws NullPointerException, UnrecognizedProfileTableNameException,
            UnrecognizedAttributeException, AttributeNotIndexedException,
            AttributeTypeMismatchException,
            TransactionRolledbackLocalException, FacilityException {

        SleeProfileManager profileManager = SleeProfileManager.getInstance();

        if (profileTableName == null)
            throw new NullPointerException();
        if (attributeName == null)
            throw new NullPointerException();
        if (attributeValue == null)
            throw new NullPointerException();

        //9.6.2 Required transactional methods : All ProfileFacility interface
        // methods.
        SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
        boolean startedTx = txMgr.requireTransaction();

        ProfileSpecificationID profileSpecification;
        try {
            profileSpecification = profileManager
                    .findProfileSpecId(profileTableName);
            if (profileSpecification == null)
                throw new UnrecognizedProfileTableNameException();
        } catch (SystemException e) {
            throw new FacilityException("System-level failure");
        }

        Collection profiles = null;
        try {
            profiles = profileManager.getProfilesByIndexedAttribute(
                    profileTableName, attributeName, attributeValue, false);
        } catch (SystemException e1) {
            throw new FacilityException("System-level failure");
        }
        Collection profileIDs = new ArrayList();
        Iterator it = profiles.iterator();
        while (it.hasNext()) {
            profileIDs.add(new ProfileID(profileTableName, (String) it.next()));
        }
        if (startedTx) {
            try {
                logger.debug("started tx so committing it");
                SleeContainer.getTransactionManager().commit();
            } catch (Exception e) {
                throw new TransactionRolledbackLocalException(
                        "Failed to commit transaction");
            }
        }
        return Collections.unmodifiableCollection(profileIDs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.profile.ProfileFacility#getProfileByIndexedAttribute(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public ProfileID getProfileByIndexedAttribute(String profileTableName,
            String attributeName, Object attributeValue)
            throws NullPointerException, UnrecognizedProfileTableNameException,
            UnrecognizedAttributeException, AttributeNotIndexedException,
            AttributeTypeMismatchException,
            TransactionRolledbackLocalException, FacilityException {

        SleeProfileManager profileManager = SleeProfileManager.getInstance();

        if (profileTableName == null)
            throw new NullPointerException();
        if (attributeName == null)
            throw new NullPointerException();
        if (attributeValue == null)
            throw new NullPointerException();

        ProfileSpecificationID profileSpecification;
        try {
            profileSpecification = profileManager
                    .findProfileSpecId(profileTableName);
            if (profileSpecification == null)
                throw new UnrecognizedProfileTableNameException();
        } catch (SystemException e) {
            throw new FacilityException("System-level failure");
        }
        //9.6.2 Required transactional methods : All ProfileFacility interface
        // methods.
        SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
        boolean startedTx = txMgr.requireTransaction();

        Collection profiles = null;
        try {
            profiles = profileManager.getProfilesByIndexedAttribute(
                    profileTableName, attributeName, attributeValue, true);
        } catch (SystemException e1) {
            throw new FacilityException("System-level failure");
        }
        if (startedTx) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("started tx so committing it");
                }
                SleeContainer.getTransactionManager().commit();
            } catch (Exception e) {
                throw new TransactionRolledbackLocalException(
                        "Failed to commit transaction");
            }
        }
        if (profiles.isEmpty())
            return null;
        return new ProfileID(profileTableName, (String) profiles.iterator()
                .next());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.profile.ProfileFacility#getProfileTableActivity(java.lang.String)
     */
    public ProfileTableActivity getProfileTableActivity(String profileTableName)
            throws NullPointerException, UnrecognizedProfileTableNameException,
            TransactionRolledbackLocalException, FacilityException {

        SleeProfileManager sleeProfileManager = SleeProfileManager
                .getInstance();

        if (profileTableName == null)
            throw new NullPointerException();

        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

        if (!sleeContainer.getSleeState().equals(SleeState.RUNNING))
            throw new FacilityException(
                    "Cannot obtain profile activity. Slee container is not in RUNNING state, but "
                            + sleeContainer.getSleeState());
        //9.6.2 Required transactional methods : All ProfileFacility interface
        // methods.
        SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
        boolean startedTx = txMgr.requireTransaction();
        try {
            ProfileSpecificationID profileSpecificationID;

            profileSpecificationID = sleeProfileManager
                    .findProfileSpecId(profileTableName);

            if (profileSpecificationID == null)
                throw new UnrecognizedProfileTableNameException();

            return sleeProfileManager
                    .createProfileTableActivity(profileTableName);
        } catch ( SystemException ex ) {
            throw new FacilityException("Unexpected exception",ex);
        } finally  {
           
            try {
                if (startedTx)
                    SleeContainer.getTransactionManager().commit();
            } catch (SystemException e) {
                throw new FacilityException("error committing tx",e);
            }
           
        }
    }

	public Collection getProfilesByStaticQuery(String arg0, String arg1, Object[] arg2) throws NullPointerException, UnrecognizedProfileTableNameException, UnrecognizedQueryNameException, AttributeTypeMismatchException, TransactionRolledbackLocalException, FacilityException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		// get profile manager
		SleeProfileManager profileManager = SleeProfileManager.getInstance();
		String tableNames = "ProfileTables:";
		for(Iterator i=profileManager.findAllProfileTables().iterator();i.hasNext();) {
			String profileTableName = (String) i.next();
			try {
				tableNames+=profileTableName+"("+getProfiles(profileTableName).size()+") ";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tableNames;
	}

}