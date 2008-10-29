/*
 * This code is derived from the Open Cloud SLEE TCK.
 *The JAIN SLEE TCK 1.0 is licensed under the OPEN CLOUD COMMUNITY SOURCE LICENSE available at http://www.opencloud.com/software/communitysource
 */
package org.mobicents.slee.test.suite;

import com.opencloud.sleetck.lib.infra.SleeTCKComponentConstants;
import com.opencloud.sleetck.lib.testutils.jmx.ProfileProvisioningMBeanProxy;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.InvalidArgumentException;
import javax.slee.UnrecognizedComponentException;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import javax.management.ObjectName;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

/**
 * A utility class for profile related functions.
 */
public class ProfileUtils {

    public ProfileUtils(JUnitSleeTestUtils utils) {
        this.utils=utils;
    }

    /**
     * Returns a proxy to the profile provisioning MBean
     */
    public ProfileProvisioningMBeanProxy getProfileProvisioningProxy() throws TCKTestErrorException {
        if(profileProvisioningProxy == null) {
            ObjectName profileProvisioningName = utils.getSleeManagementMBeanProxy().getProfileProvisioningMBean();
            profileProvisioningProxy = utils.getMBeanProxyFactory().createProfileProvisioningMBeanProxy(profileProvisioningName);
            //profileProvisioningProxy = utils.getProfileProvisioningProxy();
        }
        return profileProvisioningProxy;
    }

    /**
     * Creates an address profile table using the standard address profile specification
     * provided by the SLEE.
     * @param tableName The name of the profile table to create. Must begin with "tck."
     */
    public void createStandardAddressProfileTable(String tableName) throws TCKTestErrorException, ManagementException,
                UnrecognizedProfileSpecificationException, InvalidArgumentException,
                ProfileTableAlreadyExistsException, UnrecognizedComponentException {
        String tckPrefix = SleeTCKComponentConstants.TCK_ADDRESS_PROFILE_TABLE_PREFIX;
        if(!tableName.startsWith(tckPrefix)) throw new TCKTestErrorException(
            "Standard address profiles created by the tck must start with the \""+tckPrefix+"\" prefix");
        getProfileProvisioningProxy().createProfileTable(getStdAddressProfileSpecID(),tableName);
    }

    /**
     * Removes the given profile table
     */
    public void removeProfileTable(String tableName) throws TCKTestErrorException,
                        UnrecognizedProfileTableNameException, ManagementException {
        getProfileProvisioningProxy().removeProfileTable(tableName);
    }

    /**
     * Returns the ProfileSpecificationID of the standard address profile specification
     * provided by the SLEE.
     */
    public ProfileSpecificationID getStdAddressProfileSpecID() throws TCKTestErrorException,
                                            ManagementException, UnrecognizedComponentException {
        if(stdAddressProfileSpecID == null) {
            stdAddressProfileSpecID = new ComponentIDLookup(utils).lookupProfileSpecificationID(
                "AddressProfileSpec","javax.slee","1.0");
            if(stdAddressProfileSpecID == null) throw new TCKTestErrorException("Standard address profile specification not found");
        }
        return stdAddressProfileSpecID;
    }

    /**
     * Returns the List of names of address profile tables which use
     * the standard address profile specification, and which begin
     * with the prefix "tck.".
     */
    public List getTCKStdAddressProfileTables() throws TCKTestErrorException, ManagementException,
                    UnrecognizedProfileTableNameException, UnrecognizedComponentException {
        Vector rTableNames = new Vector();
        String tckPrefix = SleeTCKComponentConstants.TCK_ADDRESS_PROFILE_TABLE_PREFIX;

	Iterator iter = getProfileProvisioningProxy().getProfileTables().iterator();
	while (iter.hasNext()) {
	    String tableName = (String) iter.next();
	    ProfileSpecificationID profSpecID = getProfileProvisioningProxy().getProfileSpecification(tableName);
	    if(profSpecID.equals(getStdAddressProfileSpecID()) &&
	       tableName.startsWith(tckPrefix)) {
		rTableNames.addElement(tableName);
	    }
	}

        return rTableNames;
    }

    // Private state

    private JUnitSleeTestUtils utils;
    private ProfileProvisioningMBeanProxy profileProvisioningProxy;
    private ProfileSpecificationID stdAddressProfileSpecID;

}
