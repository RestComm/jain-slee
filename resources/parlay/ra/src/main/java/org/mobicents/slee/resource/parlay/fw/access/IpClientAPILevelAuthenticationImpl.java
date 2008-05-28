package org.mobicents.slee.resource.parlay.fw.access;


import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthenticationPOA;
import org.mobicents.slee.resource.parlay.util.Convert;
import org.mobicents.slee.resource.parlay.util.crypto.CHAPUtil;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtil;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtilException;



/**
 * 
 *
 * @version $Revision: 1.8 $
 */
public class IpClientAPILevelAuthenticationImpl
    extends IpClientAPILevelAuthenticationPOA {

    // CONSTANTS
    // .......................................................
    private static final String lineSeparator =
        System.getProperty("line.separator");
    private static final Log logger =
        LogFactory.getLog(IpClientAPILevelAuthenticationImpl.class);
    private static final String RECEIVED = "Received ";
    private static final String EXITING = "Exiting ";
    private static final String CLIENTAPILEVELAUTHENTICATION =
        "org::csapi::fw::fwaccess::trust_and_security::IpClientAPILevelAuthentication";
    private static final String AUTHENTICATE = ".authenticate()";
    private static final String CHALLENGE = ".challenge()";
    private static final String AUTHENTICATION_SUCCEEDED =
        ".authenticationSucceeded()";
    private static final String ABORT_AUTHENTICATION = ".abortAuthentication()";

    // VARIABLES
    // .......................................................
    private TSMBean tsmBean = null;
    // Flag for when this object has been cleaned
    private boolean clean = false;
    boolean isClient = false;
    org.omg.PortableServer.POA _poa = null;
    private CHAPUtil chapUtil = null;
    
    public IpClientAPILevelAuthenticationImpl(
        org.omg.PortableServer.POA the_poa) {
        _poa = the_poa;
        
        chapUtil = new CHAPUtil();
    }

    /**
     * 
     */
    public IpClientAPILevelAuthenticationImpl() {
        chapUtil = new CHAPUtil();
    }

    /**
     * @param the_poa
     * @return
     */
    public static IpClientAPILevelAuthenticationImpl _create(
        org.omg.PortableServer.POA the_poa) {
        return new IpClientAPILevelAuthenticationImpl(the_poa);
    }


    /* (non-Javadoc)
     * @see org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthenticationOperations#authenticate(byte[])
     */
    public byte[] authenticate(byte[] challenge) {
        byte[] response = null;
        if (logger.isDebugEnabled()) {
            logger.debug(RECEIVED + CLIENTAPILEVELAUTHENTICATION + AUTHENTICATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Challenge = <<" + Convert.toHexString(challenge) + ">>");
        }

        try {
            // must obtain lock on TSMBean before processing this request to ensure
            // client side authentication has comlpeted.
            synchronized (tsmBean.getAuthenticationMonitor()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Attempting to decrypt framework authentication challenge");
                }

                // Retrieve previously selected prescribed auth method
                String decryptionMethod = tsmBean.getEncryptionMethod();
                if (logger.isDebugEnabled()) {
                    logger.debug("Decryption method = " + decryptionMethod);
                }

                response = decryptChallenge(challenge, decryptionMethod);
            }
        }
        catch (RuntimeException ex) {
            logger.error("Unexpected exception decrypting challenge.", ex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(EXITING + CLIENTAPILEVELAUTHENTICATION + AUTHENTICATE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Response = <<" + Convert.toHexString(response) + ">>");
        }
        return response;
    }
    
    
    
	/* (non-Javadoc)
	 * @see org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthenticationOperations#challenge(byte[])
	 */
	public byte[] challenge(byte[] chapChallenge) {

		byte[] response = null;
		byte[] challenge = null;
		byte[] hashChallenge = null;
		int VALUE_SIZE = 4;
		
        if (logger.isDebugEnabled()) {
            logger.debug(RECEIVED + CLIENTAPILEVELAUTHENTICATION + CHALLENGE);
            logger.debug("ChapChallenge: "+Convert.toHexString(chapChallenge));
        }
            
        challenge = new byte[chapChallenge[VALUE_SIZE]];
        
        // extract the challenge value from the CHAP Packet
        for(int i = 0; i < chapChallenge[VALUE_SIZE]; i++) {
        	challenge[i] = chapChallenge[i + VALUE_SIZE +1];
        	
        }
        
        
        if (logger.isDebugEnabled()) {
            logger.debug("Challenge = <<" + Convert.toHexString(challenge) + ">>");
        }
        
        try {
            // must obtain lock on TSMBean before processing this request to ensure
            // client side authentication has comlpeted.
            synchronized (tsmBean.getAuthenticationMonitor()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Attempting to decrypt framework authentication challenge");
                }
                
                // Retrieve previously selected prescribed auth method
                String hashMechanism = tsmBean.getFwProperties().getAuthenticationHash();
                if (logger.isDebugEnabled()) {
                    logger.debug("Hash Mechanism = " + hashMechanism);
                }
                
                /* depending on the authentication mechanism generate the hash of the challenge
                THIS WILL BE ADDED TO - AT THE MINUTE THERE IS ONLY ONE AUTHENTICATION MECHANISM */
                if(hashMechanism.equalsIgnoreCase("P_OSA_MD5")) {
             	    // generate an MD5 Hash of the challenge using the secret key
                    hashChallenge = chapUtil.generateMD5HashChallenge(chapChallenge[1], 
                 		tsmBean.getFwProperties().getSharedSecret(), challenge);
                } else {
                    logger.error("The Authentication Mechanism was not recognised, hashing cannot continue!");
             	    //throw new TSMBeanException("The Authentication Mechanism was not recognised, hashing cannot continue!");
                }


                // generate a CHAP packet with the new hashed challenge
                response = chapUtil.generateCHAPResponsePacket(chapChallenge[1], hashChallenge, 
                		tsmBean.getFwProperties().getResponseName().getBytes());
            }
        }
        catch (NoSuchAlgorithmException ex) {
            logger.error("Exception decrypting challenge: Cryptographic algorithm requested not available in the environment.", ex);
            ex.printStackTrace();
        }
        catch (RuntimeException ex) {
            logger.error("Unexpected exception decrypting challenge.", ex);
            ex.printStackTrace();
        } 


        if (logger.isDebugEnabled()) {
            logger.debug(EXITING + CLIENTAPILEVELAUTHENTICATION + CHALLENGE);
            logger.debug("Response = <<" + Convert.toHexString(response) + ">>");
        }
        return response;
	}
	

    /**
     */
    public void abortAuthentication() {
        if (logger.isDebugEnabled()) {
            logger.debug(
                RECEIVED + CLIENTAPILEVELAUTHENTICATION + ABORT_AUTHENTICATION);
        }
        // do nothing

        if (logger.isDebugEnabled()) {
            logger.debug(
                EXITING + CLIENTAPILEVELAUTHENTICATION + ABORT_AUTHENTICATION);
        }
    }

    /**
     */
    public void authenticationSucceeded() {
        if (logger.isDebugEnabled()) {
            logger.debug(
                RECEIVED
                    + CLIENTAPILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED);
        }

        // must obtain lock on TSMBean before processing this request to ensure
        // client side authentication has comlpeted.
        synchronized (tsmBean.getAuthenticationMonitor()) {
            // Indicate to teh TSMBean that the framework has successfully
            // authenticated
            tsmBean.setFwAuthenticationSucceeded(true);

            // Notify the client thread to wake up
            tsmBean.getAuthenticationMonitor().notify();
        }

        if (logger.isDebugEnabled()) {
            logger.debug(
                EXITING
                    + CLIENTAPILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED);
        }
    }

    /**
     * @param tsmBean
     */
    public void setTSMBean(TSMBean tsmBean) {
        this.tsmBean = tsmBean;
    }

    /**
     * @param challenge
     * @param decryptionMethod
     * @return
     */
    private byte[] decryptChallenge(
        byte[] challenge,
        String decryptionMethod) {
        byte[] decryptedChallenge = null;
        if (logger.isDebugEnabled()) {
            logger.debug("Decrypting challenge.");
        }

        int keySize = 0;
        PrivateKey privateKey = null;

        try {
            if (decryptionMethod.startsWith("P_RSA")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("RSA Dencryption.");
                }

                if (decryptionMethod.equals(TSMBeanConstants.P_RSA_1024)) {
                    keySize = 1024;
                }
                else if (decryptionMethod.equals(TSMBeanConstants.P_RSA_512)) {
                    keySize = 512;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Key Size = " + keySize);
                }
                
                privateKey = RSAUtil.getPrivateKey(tsmBean.getClientID());

                decryptedChallenge =
                    RSAUtil.decryptMessage(
                        challenge,
                        privateKey,
                        tsmBean
                            .getFwProperties()
                            .getDecryptionCipherAlgorithm());

            }
            else if (decryptionMethod.equals(TSMBeanConstants.NULL_AUTH)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("No decryption transform needed.");
                }

                decryptedChallenge = challenge;
            }
        }
        catch (RSAUtilException ex) {
            logger.error(
                "Could not decrypt framework challenge" + " " + ex.toString());
        }

        if (logger.isDebugEnabled()) {
            logger.debug(
                "Decrypted challenge :"
                    + new String(decryptedChallenge)
                    + lineSeparator);
        }

        return decryptedChallenge;
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
        StringBuffer value =
            new StringBuffer("ClientAPILevelAuthenticationImpl");
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

    /* (non-Javadoc)
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        }
        
        return super._default_POA();
        
    }
} // IpClientAPILevelAuthenticationImpl
