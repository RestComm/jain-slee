package org.mobicents.slee.resource.parlay.util.corba;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * 
 * Class Description for ServantActivationHelper
 */
public class ServantActivationHelper {
    private static final String ERROR_GETTING_OBJECT_REFERENCE = "Error getting object reference, this should not happen!";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ServantActivationHelper.class);
    

    public ServantActivationHelper() {
        super();
    }
    /**
     * Activates the provided servant on the specified poa.
     * @param poa
     * @param servant
     * @param oid
     * @return @throws
     *         ServantAlreadyActive
     * @throws ObjectAlreadyActive
     * @throws WrongPolicy
     */
    public static org.omg.CORBA.Object activateServantWithID(final POA poa,
            final Servant servant, final byte[] oid) throws ServantAlreadyActive,
            ObjectAlreadyActive, WrongPolicy {

        org.omg.CORBA.Object corbaObject = null;

        // Activate the object, so it will be ready to receive CORBA method
        // invocations from the gateway
        poa.activate_object_with_id(oid, servant);

        try {
            corbaObject = poa.id_to_reference(oid);
        }
        catch (ObjectNotActive e) {
            // Shouldn't happen as we have just activated it above
            logger.fatal(ERROR_GETTING_OBJECT_REFERENCE);
        }
        catch (WrongPolicy e) {
            // Shouldn't happen as we have just activated it above
            logger.fatal(ERROR_GETTING_OBJECT_REFERENCE);
        }

        return corbaObject;
    }
    

    /**
     * Activates the provided servant on the specified poa.
     * @param poa
     * @param servant
     * @return @throws
     *         ServantAlreadyActive
     * @throws ObjectAlreadyActive
     * @throws WrongPolicy
     */
    public static org.omg.CORBA.Object activateServant(final POA poa,
            final Servant servant) throws ServantAlreadyActive,
            ObjectAlreadyActive, WrongPolicy {

        org.omg.CORBA.Object corbaObject = null;

        // Activate the object, so it will be ready to receive CORBA method
        // invocations from the gateway
        final byte[] oid = poa.activate_object(servant);

        try {
            corbaObject = poa.id_to_reference(oid);
        }
        catch (ObjectNotActive e) {
            // Shouldn't happen as we have just activated it above
            logger.fatal(ERROR_GETTING_OBJECT_REFERENCE);
        }
        catch (WrongPolicy e) {
            // Shouldn't happen as we have just activated it above
            logger.fatal(ERROR_GETTING_OBJECT_REFERENCE);
        }

        return corbaObject;
    }

    /**
     * Deactivates the provided servant.
     * 
     * @param servant
     * @throws ObjectNotActive
     * @throws WrongPolicy
     * @throws ServantNotActive
     */
    public static void deactivateServant(final Servant servant)
            throws ObjectNotActive, WrongPolicy, ServantNotActive {
        servant._default_POA().deactivate_object(servant._default_POA().servant_to_id(servant));

    }

    /**
     * Deactivates the provided servant.
     * 
     * @param poa
     * @param oid
     * @throws ObjectNotActive
     * @throws WrongPolicy
     */
    public static void deactivateServant(final POA poa, final byte[] oid)
            throws ObjectNotActive, WrongPolicy {

        poa.deactivate_object(oid);

    }
}