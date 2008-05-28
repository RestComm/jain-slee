package org.mobicents.slee.resource.parlay.fw.application;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.fw.P_INVALID_SERVICE_TOKEN;
import org.csapi.fw.P_INVALID_SIGNATURE;
import org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagementPOA;
import org.mobicents.slee.resource.parlay.util.Convert;



public class IpAppServiceAgreementManagementImpl extends IpAppServiceAgreementManagementPOA {

    // CONSTANTS
    // .......................................................

    private static final String lineSeparator = System.getProperty("line.separator");
    private static final Log logger =
        LogFactory.getLog(IpAppServiceAgreementManagementImpl.class);
    private static final String P_MD5_RSA_1024 = "P_MD5_RSA_1024";
    private static final String P_MD5_RSA_512 = "P_MD5_RSA_512";
    private static final String RECEIVED = "Received ";
    private static final String EXITING = "Exiting ";
    private static final String APPSERVICEAGREEMENTMANAGEMENT =
        "org::csapi::fw::fwapplication::service_agreement::IpAppServiceAgreementManagement";
    private static final String SIGN_SERVICE_AGREEMENT = ".signServiceAgreement()";
    private static final String TERMINATE_SERVICE_AGREEMENT = ".terminateServiceAgreement()";

    // VARIABLES
    // .......................................................
    private SABean saBean = null;
    // Flag for when this object has been cleaned
    private boolean clean = false;
    boolean isClient = false;
    org.omg.PortableServer.POA _poa = null;

    public IpAppServiceAgreementManagementImpl(org.omg.PortableServer.POA the_poa) {
        _poa = the_poa;
    }

    public static IpAppServiceAgreementManagementImpl _create(org.omg.PortableServer.POA the_poa) {
        return new IpAppServiceAgreementManagementImpl(the_poa);
    }

    /**
     */
    public byte[] signServiceAgreement(String serviceToken, String agreementText, String signingAlgorithm) {
        if (logger.isDebugEnabled()) {
            logger.debug(RECEIVED + APPSERVICEAGREEMENTMANAGEMENT + SIGN_SERVICE_AGREEMENT);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Service Token = " + serviceToken);
            logger.debug("Agreement Text = " + agreementText);
            logger.debug("Signing Algorithm = " + signingAlgorithm);
        }
        byte[] digitalSignature = new byte[0];

        // If the signing algorithm is RSA then generate the digital signature
        if( (signingAlgorithm.equals(P_MD5_RSA_512)) || (signingAlgorithm.equals(P_MD5_RSA_1024)) ){

            digitalSignature = saBean.generateDigitalSignature(agreementText, serviceToken, signingAlgorithm);
        }
        else {
            digitalSignature = new byte[0];
        }

        Object monitor = saBean.getServiceAgreementMonitor();
        synchronized (monitor) {
            // Store the signing algorithm so the SABean can use it to sign or
            // terminate agreements on this token.
            saBean.putServiceTokenSigningAlgorithm(serviceToken, signingAlgorithm);

            if (logger.isDebugEnabled()) {
                logger.debug("Gateway has signed service agreement, notifying application thread");
            }

            // Set the flag so that the SABean can exit the wait loop
            saBean.setIsAgreementSigned(true);

            // Notify the waiting thread to wake up.
            monitor.notify();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Returning digital signature " + Convert.toHexString(digitalSignature));
        }

        if (logger.isDebugEnabled()) {
            logger.debug(EXITING + APPSERVICEAGREEMENTMANAGEMENT + SIGN_SERVICE_AGREEMENT);
        }

        return digitalSignature;

        // on return PCP should place the interface OUT_OF_SERVICE.
    }

    /**
     */
    public void terminateServiceAgreement(String serviceToken, String terminationText, byte[] digitalSignature)
        throws P_INVALID_SERVICE_TOKEN, P_INVALID_SIGNATURE {
        if (logger.isDebugEnabled()) {
            logger.debug(RECEIVED + APPSERVICEAGREEMENTMANAGEMENT + TERMINATE_SERVICE_AGREEMENT);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Service Token = " + serviceToken);
            logger.debug("Termination Text = " + terminationText);
            logger.debug("Digintal Signature = " + Convert.toHexString(digitalSignature));
        }
        // clean up service reference
        String signingAlgorithm = saBean.removeSigningAlgorithm(serviceToken);

        // validate digital signature using signingAlgorithm
        if (signingAlgorithm != null) {
            if (!saBean.verifyDigitalSignature(terminationText, serviceToken, signingAlgorithm, digitalSignature)) {
                throw new P_INVALID_SIGNATURE();
            }
        }
        else {
            throw new P_INVALID_SERVICE_TOKEN();
        }

        // construct event
        TerminateServiceAgreementHandler event =
            new TerminateServiceAgreementHandler(saBean, serviceToken, terminationText);

        try {
            // Put event on events queue
            saBean.getTSMBean().getEventsQueue().execute(event);
        } catch (InterruptedException e) {
            logger.error("Failed to execute TerminatServiceAgreement event.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug(EXITING + APPSERVICEAGREEMENTMANAGEMENT + TERMINATE_SERVICE_AGREEMENT);
        }
    }

    public void setSABean(SABean saBean) {
        this.saBean = saBean;
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
        StringBuffer value = new StringBuffer("IpAppServiceAgreementManagementImpl");
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
            saBean = null;
            clean = true;
        }
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        }
        
        return super._default_POA();
        
    }

} // IpAppServiceAgreementManagementImpl
