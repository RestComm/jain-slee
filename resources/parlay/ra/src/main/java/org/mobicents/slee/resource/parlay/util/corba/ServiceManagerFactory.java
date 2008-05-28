package org.mobicents.slee.resource.parlay.util.corba;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.csapi.IpInterface;
import org.csapi.IpInterfaceHelper;
import org.csapi.cc.gccs.IpCallControlManager;
import org.csapi.cc.gccs.IpCallControlManagerHelper;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManager;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManagerHelper;
import org.csapi.ui.IpUIManager;
import org.csapi.ui.IpUIManagerHelper;
import org.omg.CORBA.BAD_PARAM;

/**
 * 
 * Class Description for ServiceManagerFactory.
 */
public class ServiceManagerFactory {

    /**
     * Commons Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(ServiceManagerFactory.class);
    
    private  ServiceManagerFactory() {
        super();
    }
    
    public static void writeReference(final ORBHandler orbHandler,
            final org.omg.CORBA.Object corbaObject,
            final String fileName) throws CorbaUtilException {

        final String stringifiedObject =
                orbHandler.getOrb().object_to_string(corbaObject);

            if (logger.isDebugEnabled()) {
                logger.debug("Writing stringified object reference to: " + fileName);
            }

            try {
                final java.io.FileWriter store = new java.io.FileWriter(fileName);
                store.write(stringifiedObject);
                store.flush();
                store.close();
            } catch (java.io.IOException ex) {
                logger.error(
                    "Failed to write the stringified object reference write to: "
                        + fileName);
                throw new CorbaUtilException("Failed to write the stringified object reference write to: "
                        + fileName);
            }
        }

    public static org.omg.CORBA.Object readReference(final ORBHandler orbHandler,
            final String fileName) throws CorbaUtilException {

        if (logger.isDebugEnabled()) {
            logger.debug("Reading stringified object reference from..."
                    + fileName);
        }

        org.omg.CORBA.Object result = null;

        FileReader retrieve = null;
        try {
            retrieve = new FileReader(fileName);
            final BufferedReader in = new BufferedReader(retrieve);
            final String ref = in.readLine();
            result = orbHandler.getOrb().string_to_object(ref);
        }
        catch (IOException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not read reference from file", ex);
            }
            throw new CorbaUtilException("Failed to read IOR from " + fileName);
        } finally {
            if ( retrieve != null ) {
                try {
                    retrieve.close();
                } catch (IOException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not read reference from file", e);
                    }
                }
            }
        }
        return result;
    }

    public static IpInterface getIpInterfaceFromFile(final ORBHandler orbHandler,
            final String fileName) throws CorbaUtilException {

        IpInterface result = null;

        final org.omg.CORBA.Object ref = readReference(orbHandler, fileName);

        if (ref != null) {
            try {
                result = IpInterfaceHelper.narrow(ref);
            } catch (BAD_PARAM ex) {
                logger.error("Failed to narrow corba object.", ex);
                throw new CorbaUtilException(
                        "Failed to narrow IOR to IpInterface");
            }
        }

        return result;
    }

    /**
     * Reads an IOR from the specified file and attemtps to narrow it to an
     * IpMultiPartyCallControlManager.
     * 
     * @param orbHandler
     * @param fileName
     * @return @throws
     *         CorbaUtilException
     */
    public static IpMultiPartyCallControlManager loadIpMultiPartyCallControlManager(
            final ORBHandler orbHandler, final String fileName) throws CorbaUtilException {
        IpMultiPartyCallControlManager result = null;

        final IpInterface tmpRef = getIpInterfaceFromFile(orbHandler, fileName);
        if (logger.isDebugEnabled()) {
            logger.debug("IOR = " + tmpRef);
        }

        if (tmpRef != null) {
            try {
                result = IpMultiPartyCallControlManagerHelper.narrow(tmpRef);
            } catch (BAD_PARAM e) {
                logger
                        .error("Failed to narrow IOR to IpMultiPartyCallControlManager.");
                throw new CorbaUtilException(
                        "Failed to narrow IOR to IpMultiPartyCallControlManager");
            }
        }

        return result;
    }
    
    /**
     * Reads an IOR from the specified file and attemtps to narrow it to an
     * IpCallControlManager.
     * 
     * @param orbHandler
     * @param fileName
     * @return @throws
     *         CorbaUtilException
     */
    public static IpCallControlManager loadIpCallControlManager(
            final ORBHandler orbHandler, final String fileName) throws CorbaUtilException {
        IpCallControlManager result = null;

        final IpInterface tmpRef = getIpInterfaceFromFile(orbHandler, fileName);
        if (logger.isDebugEnabled()) {
            logger.debug("IOR = " + tmpRef);
        }

        if (tmpRef != null) {
            try {
                result = IpCallControlManagerHelper.narrow(tmpRef);
            } catch (BAD_PARAM e) {
                logger
                        .error("Failed to narrow IOR to IpCallControlManager.");
                throw new CorbaUtilException(
                        "Failed to narrow IOR to IpCallControlManager");
            }
        }

        return result;
    }
    
    
    /**
     * Reads an IOR from the specified file and attemtps to narrow it to an
     * IpCallControlManager.
     * 
     * @param orbHandler
     * @param fileName
     * @return @throws
     *         CorbaUtilException
     */
    public static IpUIManager loadIpUIManager(
            final ORBHandler orbHandler, final String fileName) throws CorbaUtilException {
        IpUIManager result = null;

        final IpInterface tmpRef = getIpInterfaceFromFile(orbHandler, fileName);
        if (logger.isDebugEnabled()) {
            logger.debug("IOR = " + tmpRef);
        }

        if (tmpRef != null) {
            try {
                result = IpUIManagerHelper.narrow(tmpRef);
            } catch (BAD_PARAM e) {
                logger.error("Failed to narrow IOR to IpCallControlManager.");
                throw new CorbaUtilException(
                        "Failed to narrow IOR to IpCallControlManager");
            }
        }

        return result;
    }
}