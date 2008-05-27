/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;
import javax.slee.management.SleeProvider;

import org.jboss.logging.Logger;

/**
 * Implementation of the Slee provider mbean
 *
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 */
public class SleeProviderImpl implements SleeProvider {
    
    public static final String SLEE_MANAGEMENT_OBJECT_NAME_STRING = "slee:service=SleeManagement";
    private static ObjectName SLEE_MANAGEMENT_OBJECT_NAME;	
    private static Logger logger;
    
    static {
        try {
            logger = Logger.getLogger( SleeProviderImpl.class);
            SLEE_MANAGEMENT_OBJECT_NAME =  new ObjectName (SLEE_MANAGEMENT_OBJECT_NAME_STRING);
        } catch (Exception ex) {
            logger.fatal(ex.getMessage());
        } 
    }
 
    /* (non-Javadoc)
     * @see javax.slee.management.SleeProvider#getSleeManagementMBean()
     */
    public ObjectName getSleeManagementMBean() {
        return SLEE_MANAGEMENT_OBJECT_NAME;
    }
    
    public static ObjectName getSleeManagementMBeanObject(){
        return SLEE_MANAGEMENT_OBJECT_NAME;
    }

}

