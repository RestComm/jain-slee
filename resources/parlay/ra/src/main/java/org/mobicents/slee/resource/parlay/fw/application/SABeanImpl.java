package org.mobicents.slee.resource.parlay.fw.application;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.TpCommonExceptions;
import org.csapi.fw.P_ACCESS_DENIED;
import org.csapi.fw.P_INVALID_AGREEMENT_TEXT;
import org.csapi.fw.P_INVALID_SERVICE_ID;
import org.csapi.fw.P_INVALID_SERVICE_TOKEN;
import org.csapi.fw.P_INVALID_SIGNATURE;
import org.csapi.fw.P_INVALID_SIGNING_ALGORITHM;
import org.csapi.fw.P_SERVICE_ACCESS_DENIED;
import org.csapi.fw.TpSignatureAndServiceMgr;
import org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement;
import org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement;
import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;
import org.mobicents.slee.resource.parlay.fw.FwSessionException;
import org.mobicents.slee.resource.parlay.fw.ServiceAndToken;
import org.mobicents.slee.resource.parlay.fw.access.TSMBean;
import org.mobicents.slee.resource.parlay.util.Convert;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtil;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtilException;


/**
 * This class represents the framework application service agreement module.
 *
 * The application may use this class to identify which services it wishes to
 * use and sign the corresponding service agreements.
 */
public class SABeanImpl implements java.io.Serializable, SABean {

    
    // CONSTANTS
    // .......................................................
    private static final Log logger =
        LogFactory.getLog(SABeanImpl.class);
    private static final String EXCEPTION_INVOKING = "Exception invoking ";
    private static final String SERVICE_AGREEMENT_MANAGEMENT =
        "org::csapi::jr::fw::fwapplication::service_agreement::IpServiceAgreementManagement";
    private static final String SELECT_SERVICE = ".selectService()";
    private static final String INITIATE_SIGN_SERVICE_AGREEMENT =
        ".initiateSignServiceAgreement()";
    private static final String SIGN_SERVICE_AGREEMENT =
        ".signServiceAgreement()";
    private static final String TERMINATE_SERVICE_AGREEMENT =
        ".terminateServiceAgreement()";
    private static final String INVOKING = "Invoking ";
    private static final String INVOKED = "Invoked ";
    
    private static final String NO_SIGNING_ALGORITHM = "No signing algorithm for token ";

    // VARIABLES
    // .......................................................
    private Hashtable serviceTokenSigningAlgorithmTable;
    private transient Vector SABeanListeners;
    private IpServiceAgreementManagement serviceAgreementManagement = null;
    private IpAppServiceAgreementManagement appServiceAgreementManagement =
        null;

    private ServiceAgreementCallbackFactory factory = null;

    // Flag for when this object has been cleaned
    private boolean clean = false;

    // for thread synchronisation
    private Object serviceAgreementMonitor = new Object();

    // flag set when an agreement is signed
    private boolean isAgreementSigned = false;

    // wait limit for signServiceAgreement
    private long signServiceAgreementTimeout = Integer.MIN_VALUE;
    
    // bean that created this object
    private TSMBean tsmBean = null;

    /**
     * used when a TSMBean has been used to create this object.
     * 
     * @param tsmBean the factory object.
     */
    public SABeanImpl(TSMBean tsmBean) {
        serviceTokenSigningAlgorithmTable = new Hashtable();
        SABeanListeners = new Vector();
        this.tsmBean = tsmBean;

        try {
            signServiceAgreementTimeout =
                tsmBean.getFwProperties().getSsaTimeout();
            //signServiceRetryLimit = tsmBean.getFwProperties().getWaitRetries();
        }
        catch (MissingResourceException e) {
            throw e;
        }

    }
    

    public void initialise() throws SABeanException {
    	//TSMBean will only be null in testing
    	if ( tsmBean != null ){
			factory =
				new ServiceAgreementCallbackFactory(
					tsmBean.getOrbHandler());

			try {
				appServiceAgreementManagement =
					factory.createIpAppServiceAgreementManagement(this);
			}
			catch (FwSessionException ex) {
				logger.warn(
					"Exception activating an appServiceAgreementManagement callback "
						+ ex.toString());
				throw new SABeanException(
					"Exception activating an appServiceAgreementManagement callback ",
					ex);
			}
    	}
    }


//
//    /**
//     *  Defines a method used to provide the caller with a string 
//     *  representation of the class.
//     *
//     *  @return This is an developer defined representation of the class 
//     *          object as a string
//     */
//
//    public String toString() {
//        StringBuffer value = new StringBuffer("SABean");
//        value.append(lineSeparator);
//
//        return value.toString();
//    }

    /**
     * Stores the ServiceAgreementManagement interface to be used by this bean.
     *
     * @param newServiceAgreementManagement the domain ID to be used
     */
    public synchronized void setServiceAgreementManagement(IpServiceAgreementManagement newServiceAgreementManagement) {
        serviceAgreementManagement = newServiceAgreementManagement;
    }

    /**
     * Returns the ServiceAgreementManagement interface.
     *
     * @return the ServiceAgreementManagement interface to be used
     */
    public synchronized IpServiceAgreementManagement getServiceAgreementManagement() {
        return serviceAgreementManagement;
    }

    /**
     * Returns the AppServiceAgreementManagement interface.
     *
     * @return the AppServiceAgreementManagement interface to be used
     */
    public synchronized IpAppServiceAgreementManagement getAppServiceAgreementManagement() {
        return appServiceAgreementManagement;
    }

    /**
     * Returns an object that can be used as a monitor for signing service 
     * agreements.
     *
     * @return the object reference.
     */
    public Object getServiceAgreementMonitor() {
        return serviceAgreementMonitor;
    }

    /**
     * Called to indicate sign service agreement has been called by the 
     * framework.
     *
     * @param value true if it has been called.
     */
    public void setIsAgreementSigned(boolean value) {
        isAgreementSigned = value;
    }

    /**
     *  Removes an application listener from the list of those registered on
     *  this bean.
     *
     *@param  l  The PCP User Application's implementation of the
     *      SABeanListener interface.
     */
    public synchronized void removeSABeanListener(SABeanListener l) {
        if (SABeanListeners != null && SABeanListeners.contains(l)) {
            Vector v = (Vector) SABeanListeners.clone();
            v.removeElement(l);
            SABeanListeners = v;
        }
    }

    /**
     *  Adds an application listener to the list of those registered on this
     *  bean.
     *
     *@param  l  The PCP User Application's implementation of the
     *      SABeanListener interface.
     */
    public synchronized void addSABeanListener(SABeanListener l) {
        Vector v =
            SABeanListeners == null
                ? new Vector(2)
                : (Vector) SABeanListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            SABeanListeners = v;
        }
    }

    /**
     * This method will select a service and sign an agreement for it with the 
     * parlay framework.
     *
     * @exception SABeanException
     *
     * @return the service and service token for the selected service
     */
    public synchronized ServiceAndToken selectAndSignServiceAgreement(
        String serviceID,
        String agreementText)
        throws SABeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("SABean.selectAndSignServiceAgreement() called.");
        }
        ServiceAndToken result = null;

        if (serviceAgreementManagement == null) {
            throw new SABeanException(
                "This bean cannot be used, "
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + " ref missing");
        }

        result = selectAndSignWithGateway(serviceID, agreementText);

        return result;
    }

    private ServiceAndToken selectAndSignWithGateway(
        String serviceID,
        String agreementText)
        throws SABeanException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Selecting and signing with Gateway.");
        }
        ServiceAndToken result;

        // Get the service token via the select service method
        String serviceToken = selectService(serviceID);

        // Get a monitor so that we can create a timed wait on signServiceAgreement
        synchronized (this.getServiceAgreementMonitor()) {
            // initate sign service agreement
            initiateSignServiceAgreement(serviceToken);

            //int loopCount = 0;
            while (!isAgreementSigned) {

                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "Waiting for signServiceAgreement() message.");
                    }
                    this.getServiceAgreementMonitor().wait(
                        signServiceAgreementTimeout);
                }
                catch (InterruptedException ex) {
                    // do nothing
                    if (logger.isDebugEnabled()) {
                        logger.debug("Interrupted.");
                    }
                }

//                loopCount++;
//                if (loopCount > signServiceRetryLimit) {
//                    logger.warn(
//                        "Framework did not sign service agreement for token :"
//                            + serviceToken);
//                    throw new SABeanException(
//                        "Framework did not sign service agreement for token :"
//                            + serviceToken);
//                }
            }

            // reset the flag
            setIsAgreementSigned(false);

        }
        // retrieve the signing algorithm
        String signingAlgorithm = getSigningAlgorithm(serviceToken);
        
        // If algorithm is null then we cannot sign the agreement
        // This shouldn't happen unless this object is misused by the 
        // application.
        if (signingAlgorithm == null) {
            logger.warn(NO_SIGNING_ALGORITHM + serviceToken);
            throw new SABeanException(
                NO_SIGNING_ALGORITHM + serviceToken);
        }

        // sign the agreement
        TpSignatureAndServiceMgr signatureAndServiceMgr =
            signServiceAgreement(serviceToken, agreementText, signingAlgorithm);

        // validate signature
        // ...

        result =
            new ServiceAndToken(
                signatureAndServiceMgr.ServiceMgrInterface,
                serviceToken);

        if (logger.isDebugEnabled()) {
            logger.debug("Returning :" + result);
        }
        return result;
    }

    /**
     * Method selectService.
     * @param serviceID
     * @return String
     * @throws SABeanException
     */
    private String selectService(String serviceID) throws SABeanException {
        String serviceToken = null;

        try {
            // Make the parlay call
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING + SERVICE_AGREEMENT_MANAGEMENT + SELECT_SERVICE);
                logger.debug("Service ID = " + serviceID);
            }
            serviceToken = serviceAgreementManagement.selectService(serviceID);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED + SERVICE_AGREEMENT_MANAGEMENT + SELECT_SERVICE);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE,
                ex);
        }
        catch (P_INVALID_SERVICE_ID ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE,
                ex);
        }
        catch (P_SERVICE_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SELECT_SERVICE,
                ex);
        }
        return serviceToken;
    }

    /**
     * Method initiateSignServiceAgreement.
     * @param serviceToken
     * @throws SABeanException
     */
    private void initiateSignServiceAgreement(String serviceToken)
        throws SABeanException {
        try {
            // Make the parlay call
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING
                        + SERVICE_AGREEMENT_MANAGEMENT
                        + INITIATE_SIGN_SERVICE_AGREEMENT);
                logger.debug("Service Token = " + serviceToken);
            }
            serviceAgreementManagement.initiateSignServiceAgreement(
                serviceToken);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED
                        + SERVICE_AGREEMENT_MANAGEMENT
                        + INITIATE_SIGN_SERVICE_AGREEMENT);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_INVALID_SERVICE_TOKEN ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_SERVICE_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + INITIATE_SIGN_SERVICE_AGREEMENT,
                ex);
        }
    }

    /**
     * Method signServiceAgreement.
     * @param serviceToken
     * @param agreementText
     * @param signingAlgorithm
     * @return TpSignatureAndServiceMgr
     * @throws SABeanException
     */
    private TpSignatureAndServiceMgr signServiceAgreement(
        String serviceToken,
        String agreementText,
        String signingAlgorithm)
        throws SABeanException {
        TpSignatureAndServiceMgr signatureAndServiceMgr = null;

        try {
            // Make the parlay call
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING
                        + SERVICE_AGREEMENT_MANAGEMENT
                        + SIGN_SERVICE_AGREEMENT);
                logger.debug("Service Token = " + serviceToken);
                logger.debug("Agreement Text = " + agreementText);
                logger.debug("Signing Algorithm = " + signingAlgorithm);
            }

            signatureAndServiceMgr =
                serviceAgreementManagement.signServiceAgreement(
                    serviceToken,
                    agreementText,
                    signingAlgorithm);

            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED
                        + SERVICE_AGREEMENT_MANAGEMENT
                        + SIGN_SERVICE_AGREEMENT);
            }
            verifyDigitalSignature(
                agreementText,
                serviceToken,
                signingAlgorithm,
                signatureAndServiceMgr.DigitalSignature);
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_INVALID_AGREEMENT_TEXT ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_INVALID_SERVICE_TOKEN ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_INVALID_SIGNING_ALGORITHM ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_SERVICE_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + SIGN_SERVICE_AGREEMENT,
                ex);
        }

        return signatureAndServiceMgr;
    }

    /**
     * This method will terminate a service agreement with the 
     * parlay framework.
     *
     * @exception SABeanException
     *
     */
    public synchronized void terminateServiceAgreement(
        String serviceToken,
        String terminationText)
        throws SABeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("SABean.terminateServiceAgreement() called.");
        }

        // Get the signing algorithm
        String signingAlgorithm = getSigningAlgorithm(serviceToken);

        // If it is unknown we cannot terminate a service agreement for the given
        // token
        if (signingAlgorithm == null) {
            logger.warn(NO_SIGNING_ALGORITHM + serviceToken);
            throw new SABeanException(
                NO_SIGNING_ALGORITHM + serviceToken);
        }

        // create the digital signature
        byte[] digitalSignature =
            generateDigitalSignature(
                terminationText,
                serviceToken,
                signingAlgorithm);

        terminateServiceAgreement(
            serviceToken,
            terminationText,
            digitalSignature);

        // Remove token from table as this service instance no longer exists.
        removeSigningAlgorithm(serviceToken);
    }

    private void terminateServiceAgreement(
        String serviceToken,
        String terminationText,
        byte[] digitalSignature)
        throws SABeanException {
        try {
            // Make the parlay call
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING
                        + SERVICE_AGREEMENT_MANAGEMENT
                        + TERMINATE_SERVICE_AGREEMENT);
                logger.debug("Service Token = " + serviceToken);
                logger.debug("Termination Text = " + terminationText);
                logger.debug(
                    "Digintal Signature = "
                        + Convert.toHexString(digitalSignature));
            }

            serviceAgreementManagement.terminateServiceAgreement(
                serviceToken,
                terminationText,
                digitalSignature);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED
                        + SERVICE_AGREEMENT_MANAGEMENT
                        + TERMINATE_SERVICE_AGREEMENT);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_INVALID_SERVICE_TOKEN ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT,
                ex);
        }
        catch (P_INVALID_SIGNATURE ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT
                    + " "
                    + ex.toString());
            throw new SABeanException(
                EXCEPTION_INVOKING
                    + SERVICE_AGREEMENT_MANAGEMENT
                    + TERMINATE_SERVICE_AGREEMENT,
                ex);
        }
    }

    public void fireTerminateServiceAgreement(TerminateServiceAgreementEvent e) {
        if (SABeanListeners != null) {
            Vector listeners = SABeanListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                try {
                    (
                        (SABeanListener) listeners.elementAt(
                            i)).terminateServiceAgreement(
                        e);
                }
                catch (RuntimeException ex) {
                    logger.error(
                        "Caught exception invoking application SABeanListener"
                            + " "
                            + ex.toString());
                }
            }
        }
    }

    /**
     * This method will clean up all internal object references with the exception
     * of registered listeners which are the responsibility of the application.
     * The PCP will use this when the object is no longer to be used.
     */
    public synchronized void cleanup() {
        if (logger.isDebugEnabled()) {
            logger.debug("Cleaning up " + toString());
        }
        if (!clean) {
            // object references
            serviceAgreementManagement = null;
            try {
                factory.deactivateIpAppServiceAgreementManagement();
            }
            catch (FwSessionException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Exception deactivating IpAppServiceAgreementManagement"
                            + e);
                }
            }
            appServiceAgreementManagement = null;
            serviceTokenSigningAlgorithmTable.clear();

            factory.destroy();

            tsmBean = null;

            clean = true;
        }
    }

    public String removeSigningAlgorithm(String serviceToken) {
        return (String) serviceTokenSigningAlgorithmTable.remove(serviceToken);
    }

    private String getSigningAlgorithm(String serviceToken) {
        return (String) serviceTokenSigningAlgorithmTable.get(serviceToken);
    }

    public void putServiceTokenSigningAlgorithm(
        String serviceToken,
        String signingAlgorithm) {
        serviceTokenSigningAlgorithmTable.put(serviceToken, signingAlgorithm);
    }

    public byte[] generateDigitalSignature(
        String text,
        String serviceToken,
        String signingAlgorithm) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generating a digital signature.");
            logger.debug("Text = " + text);
            logger.debug("Token = " + serviceToken);
            logger.debug("Algorithm = " + signingAlgorithm);
        }

        PrivateKey privateKey = null;
        //String fileLocation =
          //  tsmBean.getFwProperties().getCertificateVault();
        //int keySize = 0;
        byte[] result = null;
        Signature signature = null;

        //If signing algorithm is RSA then generate digital signature
        if (((signingAlgorithm.equals("P_MD5_RSA_512"))
            || (signingAlgorithm.equals("P_MD5_RSA_1024"))) &&
            (tsmBean.getFwProperties().getAuthenticationSequence().getType().equals("TWO_WAY") ||
            tsmBean.getFwProperties().getAuthenticationSequence().getType().equals("ONE_WAY"))) {
/*
            if (signingAlgorithm.equals("P_MD5_RSA_512")) {
                keySize = 512;
            }
            else if (signingAlgorithm.equals("P_MD5_RSA_1024")) {
                keySize = 1024;
            }

            String filename =
                fileLocation
                    + tsmBean.getClientID()
                    + "RSAPRIVATE"
                    + keySize
                    + ".pem";*/

            try {
                privateKey = RSAUtil.getPrivateKey(tsmBean.getClientID());

            }
            catch (RSAUtilException ex) {
                logger.error("Exception caught obtaining private key.", ex);
            }


                if (logger.isDebugEnabled()) {
                    logger.debug("Using DigestInfo to generate Digital Signature");
                }
                try {
                    signature = Signature.getInstance("MD5withRSA");
                    signature.initSign(privateKey);
                    signature.update(text.getBytes("ISO-8859-1"));
                    signature.update(serviceToken.getBytes("ISO-8859-1"));
                    result = signature.sign();

                }
                catch (NoSuchAlgorithmException ex) {
                    logger.error(
                        "NoSuchAlgorithmException caught:",
                        ex.fillInStackTrace());
                }
                catch (InvalidKeyException ex) {
                    logger.error(
                        "InvalidKeyException caught:",
                        ex.fillInStackTrace());
                }
                catch (UnsupportedEncodingException ex) {
                    logger.error(
                        "UnsupportedEncodingException caught:",
                        ex.fillInStackTrace());
                }
                catch (SignatureException ex) {
                    logger.error(
                        "SignatureException caught:",
                        ex.fillInStackTrace());
                }
            } else {
            // signing algorithm is not RSA, i.e. could be "" or NULL, etc
            try {
                result = signingAlgorithm.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                logger.error(
                    "UnsupportedEncodingException caught generating digital signature.",
                    e);
            }
        }

        //return null;
        return result;
    }

    public boolean verifyDigitalSignature(
        String text,
        String serviceToken,
        String signingAlgorithm,
        byte[] digitalSignature) {
        if (logger.isDebugEnabled()) {
            logger.debug("Verifying a digital signature.");
            logger.debug("Text = " + text);
            logger.debug("Token = " + serviceToken);
            logger.debug("Algorithm = " + signingAlgorithm);
        }
        
        return false;
    }

    /**
     * Returns the tsmBean.
     * @return TSMBean
     */
    public TSMBean getTSMBean() {
        return tsmBean;
    }

}
