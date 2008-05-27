package org.mobicents.slee.resource.parlay.fw.access;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.fw.fw_access.trust_and_security.IpClientAccessPOA;
import org.mobicents.slee.resource.parlay.fw.application.SABean;


/**
 * IpClientAccess interface is offered by the client to the framework to allow it to initiate interactions during the access session. 
 *
 * @version $Revision: 1.3 $
 */
public class IpClientAccessImpl extends IpClientAccessPOA {

    // VARIABLES
    // .......................................................
    private TSMBean tsmBean = null;
    // Flag for when this object has been cleaned
    private boolean clean = false;
    boolean isClient = false;
    org.omg.PortableServer.POA _poa = null;

    // CONSTANTS
    // .......................................................
    private static final String lineSeparator = System.getProperty("line.separator");
    private static final Log logger = LogFactory.getLog(IpClientAccessImpl.class);
    private static final String RECEIVED = "Received ";
    private static final String EXITING = "Exiting ";
    private static final String CLIENTACCESS = "org::csapi::fw::fwaccess::trust_and_security::IpClientAccess";
    private static final String TERMINATE_ACCESS = ".terminateAccess()";
    
    /**
     * @param the_poa
     */
    public IpClientAccessImpl(org.omg.PortableServer.POA the_poa, TSMBean tsmBean) {
        _poa = the_poa;
        this.tsmBean = tsmBean;
    }

    /**
     * @param the_poa
     * @return
     */
    public static IpClientAccessImpl _create(org.omg.PortableServer.POA the_poa, TSMBean tsmBean) {
        return new IpClientAccessImpl(the_poa, tsmBean);
    }


    /* (non-Javadoc)
     * @see org.csapi.fw.fw_access.trust_and_security.IpClientAccessOperations#terminateAccess(java.lang.String, java.lang.String, byte[])
     */
    public void terminateAccess(String terminationText, String signingAlgorithm, byte[] digitalSignature) {
        if (logger.isDebugEnabled()) {
            logger.debug(RECEIVED + CLIENTACCESS + TERMINATE_ACCESS);
        }

        synchronized(tsmBean) {

	        try {
                // Put event on events queue
                tsmBean.getEventsQueue().execute(new TerminateAccessHandler(tsmBean, terminationText, signingAlgorithm, digitalSignature));
            } catch (InterruptedException e) {
                logger.error("Unable to queue TerminateAccessEvent");
            }
	        
	        // tsmBean & saBean are no longer useable.
	        SABean saBean = tsmBean.getSABean();
	        if (saBean != null) {
	            saBean.cleanup();
	        }
	        tsmBean.cleanup();
        }

        // validate digital signature
        if (logger.isDebugEnabled()) {
            logger.debug(EXITING + CLIENTACCESS + TERMINATE_ACCESS);
        }
    }

    /**
     *  Defines a method used to provide the caller with a string 
     *  representation of the class.
     *
     *  @return This is an developer defined representation of the class 
     *          object as a string
     *
     */
    public String toString() {
        StringBuffer value = new StringBuffer("IpClientAccessImpl");
        value.append(lineSeparator);

        return value.toString();
    }

    /**
     * This method will clean up all internal object references.
     */
    public synchronized void cleanup() {
        if (logger.isDebugEnabled()) {
            logger.debug("Cleaning up " + toString());
        }
        if (!clean) {
            tsmBean = null;
            clean = true;
        }
    }

    /**
     * @param tsmBean
     */
    public void setTSMBean(TSMBean tsmBean) {
        this.tsmBean = tsmBean;
    }

    /* (non-Javadoc)
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        }
        
        return super._default_POA();
        
    }
}
