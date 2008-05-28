package org.mobicents.slee.resource.parlay.fw;

import java.io.IOException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.gccs.IpCallControlManager;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManager;
import org.csapi.fw.TpProperty;
import org.csapi.fw.TpServiceProperty;
import org.csapi.ui.IpUIManager;
import org.mobicents.slee.resource.parlay.util.corba.CorbaUtilException;
import org.mobicents.slee.resource.parlay.util.corba.ORBHandler;
import org.mobicents.slee.resource.parlay.util.corba.ServiceManagerFactory;
import org.omg.CORBA.ORB;
import org.omg.CORBA.UserException;
import org.omg.PortableServer.POA;

/**
 * This version of the FwSession will be used in "bypassFw" mode for testing
 * against harnesses that don't implement framework functionality.
 *  
 */
public class BypassedFwSession implements FwSession {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(BypassedFwSession.class);

    public BypassedFwSession(final FwSessionProperties properties,
            final TestProperties testProperties) {
        if (logger.isDebugEnabled()) {
            logger.debug("BypassedFwSession created");
        }
        fwSessionProperties = properties;
        this.testProperties = testProperties;
        fwSessionListeners = new Vector(2);
    }

    private final transient FwSessionProperties fwSessionProperties;

    private final transient Vector fwSessionListeners;
    
    private final transient TestProperties testProperties;
    
    private ORBHandler handler;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#init()
     */
    public void init() throws FwSessionException {
        try {
            handler = ORBHandler.getInstance();
        }
        catch (IOException e) {
            logger.error("Failed to create ORBHandler.");
            throw new FwSessionException("Failed to create ORBHandler.", e);
        }
        synchronized (handler) {
            try {
                handler.init();
            }
            catch (UserException e) {
                logger.error("Failed to initialise corba server.", e);
                throw new FwSessionException("Failed to initialise corba server.", e);
            }
            // Test every 2 seconds
            int numberOfRetries = 20;
            while (!handler.getIsServerReady() && numberOfRetries > 0) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Waiting for orb to initialise ...");
                    }
                    handler.wait(2000);
                }
                catch (InterruptedException e1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Wait interrupted.");
                    }
                }
                numberOfRetries--;
            }
        }
        if (!handler.getIsServerReady()) {
            throw new FwSessionException("Failed to initialise ORB.");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#addFwSessionListener(org.mobicents.slee.resource.parlay.fw.FwSessionListener)
     */
    public void addFwSessionListener(FwSessionListener listener) {
        if (!fwSessionListeners.contains(listener)) {
            fwSessionListeners.addElement(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#removeFwSessionListener(org.mobicents.slee.resource.parlay.fw.FwSessionListener)
     */
    public void removeFwSessionListener(FwSessionListener listener) {
        if (fwSessionListeners.contains(listener)) {
            fwSessionListeners.removeElement(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#authenticate()
     */
    public void authenticate() throws FwSessionException {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#endAccess(org.csapi.fw.TpProperty[])
     */
    public void endAccess(TpProperty[] endAccessProperties)
            throws FwSessionException {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#releaseService(org.mobicents.slee.resource.parlay.fw.ServiceAndToken)
     */
    public void releaseService(ServiceAndToken service)
            throws FwSessionException {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getService(java.lang.String,
     *      org.csapi.fw.TpServiceProperty[])
     */
    public ServiceAndToken getService(String serviceTypeName,
            TpServiceProperty[] serviceProperties) throws FwSessionException {

        ServiceAndToken result = null;

        if (serviceTypeName.equals("P_MULTI_PARTY_CALL_CONTROL")) {
            try {
                IpMultiPartyCallControlManager manager = ServiceManagerFactory
                        .loadIpMultiPartyCallControlManager(handler, testProperties.getIpMultiPartyCallControlManagerFileName());

                result = new ServiceAndToken(manager, serviceTypeName);
            } catch (CorbaUtilException e) {
                throw new FwSessionException(e);
            }
        } else if (serviceTypeName.equals("P_GENERIC_CALL_CONTROL")) {
            try {
                IpCallControlManager manager = ServiceManagerFactory
                        .loadIpCallControlManager(handler, testProperties.getIpCallControlManagerFileName());

                result = new ServiceAndToken(manager, serviceTypeName);
            } catch (CorbaUtilException e) {
                throw new FwSessionException(e);
            }
        } else if (serviceTypeName.equals("P_USER_INTERACTION")) {
            try {
                IpUIManager manager = ServiceManagerFactory
                        .loadIpUIManager(handler, testProperties.getIpUIManagerFileName());

                result = new ServiceAndToken(manager, serviceTypeName);
            } catch (CorbaUtilException e) {
                throw new FwSessionException(e);
            }
        } else {
            throw new IllegalArgumentException("Service type "
                    + serviceTypeName + " is not supported in bypassFw mode.");
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#shutdown()
     */
    public void shutdown() {
        try {
	        if (handler != null) {
	            handler.shutdown();
	            handler = null;
	        }
        }
        catch(Exception e) {
            logger.error("Failed to shut down Bypassed FwSession.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getORB()
     */
    public ORB getORB() {
        return handler != null ? handler.getOrb() : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getFwSessionProperties()
     */
    public FwSessionProperties getFwSessionProperties() {
        return fwSessionProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getRootPOA()
     */
    public POA getRootPOA() {
        return handler != null ? handler.getRootPOA() : null;
    }

}