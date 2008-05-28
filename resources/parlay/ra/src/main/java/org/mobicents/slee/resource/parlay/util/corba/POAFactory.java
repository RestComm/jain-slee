package org.mobicents.slee.resource.parlay.util.corba;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.InvalidPolicy;

/**
 * 
 * Class Description for POAFactory
 */
public class POAFactory {
    
    public POAFactory() {
        super();
    }
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(POAFactory.class);

    /**
     * Creates a new POA instance based on the supplied information.
     * 
     * @param parentPOA
     * @param adapterName
     * @param manager
     * @param policies
     * @return
     * @throws AdapterAlreadyExists
     * @throws InvalidPolicy
     */
    public static POA createPOA(final POA parentPOA, final String adapterName,
            final POAManager manager, final org.omg.CORBA.Policy[] policies)
            throws AdapterAlreadyExists, InvalidPolicy {

        POA poa = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Creating POA " + adapterName);
        }

        poa = parentPOA.create_POA(adapterName, manager, policies);

        return poa;
    }
    
    /**
     * Destroys the specified POA. All children of this POA
     * will also be destroyed.
     * 
     * @param poa
     */
    public static void destroyPOA(final POA poa) {
        poa.destroy(false, false);
    }

}