package org.mobicents.slee.resource.parlay.fw.access;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.IpInterface;
import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.P_INVALID_VERSION;
import org.csapi.TpCommonExceptions;
import org.csapi.fw.P_ACCESS_DENIED;
import org.csapi.fw.P_INVALID_ACCESS_TYPE;
import org.csapi.fw.P_INVALID_AUTH_TYPE;
import org.csapi.fw.P_INVALID_DOMAIN_ID;
import org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM;
import org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY;
import org.csapi.fw.TpAuthDomain;
import org.csapi.fw.fw_access.trust_and_security.IpAPILevelAuthentication;
import org.csapi.fw.fw_access.trust_and_security.IpAPILevelAuthenticationHelper;
import org.csapi.fw.fw_access.trust_and_security.IpAccess;
import org.csapi.fw.fw_access.trust_and_security.IpAccessHelper;
import org.csapi.fw.fw_access.trust_and_security.IpInitial;
import org.mobicents.slee.resource.parlay.util.Convert;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.crypto.CHAPUtil;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtil;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtilException;
import org.omg.CORBA.SystemException;

/**
 * <B> For internal use only. </B>
 * This class is used to perform client side authenticaion with the Parlay
 * framework.
 */
public class AuthenticationHandlerImpl implements AuthenticationHandler {



    // VARIABLES
    // .......................................................
    private TSMBean tsmBean = null;
    private static IpAPILevelAuthentication apiLevelAuthentication = null;
    private CHAPUtil chapUtil = null;

    // Flag for when this object has been cleaned
    private boolean clean = false;

    // CONSTANTS
    // .......................................................
    private static final Log logger =
        LogFactory.getLog(AuthenticationHandlerImpl.class);
    private static final String lineSeparator =
        System.getProperty("line.separator");
    private static final String INVOKING = "Invoking ";
    private static final String INVOKED = "Invoked ";
    private static final String EXCEPTION_INVOKING = "Exception invoking ";
    private static final String INITIAL =
        "org::csapi::fw::fw_access::trust_and_security::IpInitial";
    private static final String APILEVELAUTHENTICATION =
        "org::csapi::fw::fwaccess::trust_and_security::IpAPILevelAuthentication";
    private static final String REQUEST_ACCESS =
        ".requestAccess()";
    private static final String ABORT_AUTHENTICATION = "abortAuthentication";
    private static final String INITIATE_AUTHENTICATION =
        ".initiateAuthentication()";
    private static final String INITIATE_AUTHENTICATION_WITH_VERSION =
        ".initiateAuthenticationWithVersion()";
    private static final String SELECT_ENCRYPTION_METHOD =
        ".selectEncryptionMethod()";
    private static final String SELECT_AUTHENTICATION_MECHANISM =
        ".selectAuthenticationMechanism()";
    private static final String AUTHENTICATE = ".authenticate()";
    private static final String AUTHENTICATION_SUCCEEDED =
        ".authenticationSucceeded()";
    private static final String CHALLENGE = ".challenge()";
    
    private static final int HEADER_SIZE = 4;
    
    private static final int HEADER_TOTAL = HEADER_SIZE +1;
    
    public AuthenticationHandlerImpl(TSMBean tsmBean) {
        this.tsmBean = tsmBean;
        chapUtil = new CHAPUtil();
    }

    /**
     *  Defines a method used to provide the caller with a string 
     *  representation of the class.
     *
     *  @return This is an developer defined representation of the class 
     *          object as a string
     */
    public String toString() {
        StringBuffer value = new StringBuffer("AuthenticationHandlerImpl");
        value.append(lineSeparator);

        return value.toString();
    }

    /**
     *  Calls Initial.initiateAuthentication() using the Initial interface held
     *  by the TSMBean.
     */
    public void initiateAuthentication(TpAuthDomain clientAuthDomain)
        throws TSMBeanException {

        if (logger.isDebugEnabled()) {
            logger.debug("Initiating authentication");
        }
        // The result will be the framework's auth domain
        TpAuthDomain result = null;

        IpInitial initial = tsmBean.getInitial();

        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + INITIAL + INITIATE_AUTHENTICATION);
                logger.debug(
                    "Authentication Type = " + TSMBeanImpl.AUTHENTICATION_TYPE);
            }    

            result =
                initial.initiateAuthentication(
                    clientAuthDomain,
                    TSMBeanImpl.AUTHENTICATION_TYPE);

            if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + INITIAL + INITIATE_AUTHENTICATION);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + INITIAL
                    + INITIATE_AUTHENTICATION
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION,
                ex);
        }
        catch (P_INVALID_DOMAIN_ID ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + INITIAL
                    + INITIATE_AUTHENTICATION
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION,
                ex);
        }
        catch (P_INVALID_INTERFACE_TYPE ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + INITIAL
                    + INITIATE_AUTHENTICATION
                    + " "
                    + ParlayExceptionUtil.stringify(ex));
            throw new TSMBeanException(
                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION,
                ex);
        }
        catch (P_INVALID_AUTH_TYPE ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + INITIAL
                    + INITIATE_AUTHENTICATION
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + INITIAL
                    + INITIATE_AUTHENTICATION
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION,
                ex);
        }

        // Store the returned auth interface in this class
        apiLevelAuthentication =
            IpAPILevelAuthenticationHelper.narrow(result.AuthInterface);

    }
    
    
    /**
     *  Calls Initial.initiateAuthenticationWithVersion() using the Initial interface held
     *  by the TSMBean.
     */
	public void initiateAuthenticationWithVersion(TpAuthDomain clientAuthDomain, String version) throws TSMBeanException {
		if (logger.isDebugEnabled()) {
            logger.debug("Initiating Authentication With Version");
        }		
		// The result will be the framework's auth domain
        TpAuthDomain result = null;

        IpInitial initial = tsmBean.getInitial();

        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION);
                logger.debug(
                    "Authentication Type = " + TSMBeanImpl.AUTHENTICATION_TYPE);
            }   
			result =
			    initial.initiateAuthenticationWithVersion(
			        clientAuthDomain,
			        TSMBeanImpl.AUTHENTICATION_TYPE, 
					version);
			if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION);
            }
			
		} catch (TpCommonExceptions ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + INITIAL
	                    + INITIATE_AUTHENTICATION_WITH_VERSION
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION,
	                ex);
		} catch (P_INVALID_DOMAIN_ID ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + INITIAL
	                    + INITIATE_AUTHENTICATION_WITH_VERSION
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION,
	                ex);
		} catch (P_INVALID_INTERFACE_TYPE ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + INITIAL
	                    + INITIATE_AUTHENTICATION_WITH_VERSION
	                    + " "
	                    + ParlayExceptionUtil.stringify(ex));
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION,
	                ex);
		} catch (P_INVALID_AUTH_TYPE ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + INITIAL
	                    + INITIATE_AUTHENTICATION_WITH_VERSION
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION,
	                ex);
		} catch (P_INVALID_VERSION ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + INITIAL
	                    + INITIATE_AUTHENTICATION_WITH_VERSION
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION,
	                ex);
		} catch (SystemException ex) {
            logger.warn(
                    EXCEPTION_INVOKING
                        + INITIAL
                        + INITIATE_AUTHENTICATION_WITH_VERSION
                        + " "
                        + ex.toString());
                throw new TSMBeanException(
                    EXCEPTION_INVOKING + INITIAL + INITIATE_AUTHENTICATION_WITH_VERSION,
                    ex);
        }
		
//		 Store the returned auth interface in this class
        apiLevelAuthentication =
            IpAPILevelAuthenticationHelper.narrow(result.AuthInterface);          
        
	}
	

    /**
     *  Calls Authentication.selectEncryptionMethod() using the Authentication 
     *  interface held by the TSMBean.
     *
     *  @param authCapabilityList, the clients auth capability list.
     *
     *  @return the framework's chosen authentication method.
     */
    public String selectEncryptionMethod(String authCapabilityList)
        throws TSMBeanException {
        String result = null;

        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING
                        + APILEVELAUTHENTICATION
                        + SELECT_ENCRYPTION_METHOD);
                logger.debug(
                    "Authentication Capability List = " + authCapabilityList);
            }
            result =
                apiLevelAuthentication.selectEncryptionMethod(
                    authCapabilityList);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED
                        + APILEVELAUTHENTICATION
                        + SELECT_ENCRYPTION_METHOD);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD,
                ex);
        }
        catch (P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + SELECT_ENCRYPTION_METHOD,
                ex);
        }

        return result;
    }
    
    /**
     *  Calls Authentication.selectAuthenticationMechanism() using the Authentication 
     *  interface held by the TSMBean.
     *
     *  @param authMechanismList, the clients auth mechanism list.
     *
     *  @return the framework's chosen authentication method.
     */
	public String selectAuthenticationMechanism(String authMechanismList) throws TSMBeanException {
        
		String result = null;
		
		// Make the parlay call
		try {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING
                        + APILEVELAUTHENTICATION
                        + SELECT_AUTHENTICATION_MECHANISM);
                logger.debug(
                    "Authentication Capability List = " + authMechanismList);
            }
           
			result =
			    apiLevelAuthentication.selectAuthenticationMechanism(
			    		authMechanismList);
			
			if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED
                        + APILEVELAUTHENTICATION
                        + SELECT_AUTHENTICATION_MECHANISM);
            }
		} catch (TpCommonExceptions ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + APILEVELAUTHENTICATION
	                    + SELECT_AUTHENTICATION_MECHANISM
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING
	                    + APILEVELAUTHENTICATION
	                    + SELECT_AUTHENTICATION_MECHANISM,
	                ex);
		} catch (P_ACCESS_DENIED ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + APILEVELAUTHENTICATION
	                    + SELECT_AUTHENTICATION_MECHANISM
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING
	                    + APILEVELAUTHENTICATION
	                    + SELECT_AUTHENTICATION_MECHANISM,
	                ex);
		} catch (P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM ex) {
			logger.warn(
	                EXCEPTION_INVOKING
	                    + APILEVELAUTHENTICATION
	                    + SELECT_AUTHENTICATION_MECHANISM
	                    + " "
	                    + ex.toString());
	            throw new TSMBeanException(
	                EXCEPTION_INVOKING
	                    + APILEVELAUTHENTICATION
	                    + SELECT_AUTHENTICATION_MECHANISM,
	                ex);
		}
            
        return result;
	}


    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.fw.access.AuthenticationHandler#authenticate(java.lang.String)
     */
    public void authenticate(String encryptionMethod) throws TSMBeanException {
        byte[] challenge = null;
        // generate a random challenge
        challenge = generateRandomChallenge();

        if (logger.isDebugEnabled()) {
            logger.debug(
                "Challenge = <<" + Convert.toHexString(challenge) + ">>");
        }

        // for storing the gateway response
        byte[] response = null;

        // For generating an encrypted challenge
        byte[] encryptedChallenge = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Chosen encryption method =" + encryptionMethod);
        }

        encryptedChallenge = encryptChallenge(challenge, encryptionMethod);

        if (logger.isDebugEnabled()) {
            logger.debug(
                "Encrypted Challenge = <<"
                    + Convert.toHexString(encryptedChallenge)
                    + ">>");
        }

        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + APILEVELAUTHENTICATION + AUTHENTICATE);
            }
            response = apiLevelAuthentication.authenticate(encryptedChallenge);
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + APILEVELAUTHENTICATION + AUTHENTICATE);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(
                    "Response = <<" + Convert.toHexString(response) + ">>");
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATE
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + APILEVELAUTHENTICATION + AUTHENTICATE,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATE
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + APILEVELAUTHENTICATION + AUTHENTICATE,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATE
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + APILEVELAUTHENTICATION + AUTHENTICATE,
                ex);
        }

        if (!Convert.assertEquals(challenge, response)) {
            logger.warn("Gateway response does not match original challenge");
            throw new TSMBeanException("Gateway response does not match original challenge");
        }
    }

        
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.fw.access.AuthenticationHandler#challenge(java.lang.String)
     */
    public void challenge(String authMechanism) throws TSMBeanException {
        byte[] challenge = null;
        byte[] hashChallenge = null;
        
        // generate a random challenge
        challenge = generateRandomChallenge();

        if (logger.isDebugEnabled()) {
            logger.debug(
                "Challenge = <<" + Convert.toHexString(challenge) + ">>");
        }

        // for storing the gateway response
        byte[] response = null;

        // For generating an encrypted challenge
        byte[] chapChallenge = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Chosen authentication mechanism =" + authMechanism);
        }
     
        chapChallenge = chapUtil.generateCHAPRequestPacket(challenge);
        
        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + APILEVELAUTHENTICATION + CHALLENGE);
            }
            response = apiLevelAuthentication.challenge(chapChallenge);
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + APILEVELAUTHENTICATION + CHALLENGE);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(
                    "Response = <<" + Convert.toHexString(response) + ">>");
            }

	        // compare the response MD5 hash to the MD5 Hash of the original challenge 
	        byte[] responseValue = new byte[response[HEADER_SIZE]];
	        // get the response value in MD5 Hash format
	        for(int i = 0; i < responseValue.length; i++) {
	        	responseValue[i] = response[i+HEADER_TOTAL];
	        }
	        
	        /* depending on the authentication mechanism generate the hash of the challenge
	           THIS WILL BE ADDED TO - AT THE MINUTE THERE IS ONLY ONE AUTHENTICATION MECHANISM */
	        if(authMechanism.equalsIgnoreCase("P_OSA_MD5")) {
	        	// generate an MD5 Hash of the challenge using the secret key
	            hashChallenge = chapUtil.generateMD5HashChallenge(chapChallenge[1], 
	            		tsmBean.getFwProperties().getSharedSecret(), challenge);
	
	        } else {
	            logger.error("The Authentication Mechanism was not recognised, hashing cannot continue!");
	        	throw new TSMBeanException("The Authentication Mechanism was not recognised, hashing cannot continue!");
	        }
	
	        
	        // compare the original hash and the response hash to ensure they are equal
	        if (!Convert.assertEquals(responseValue, hashChallenge)) {
	
	            logger.warn("Gateway response does not match original MD5 Hash challenge");
	            throw new TSMBeanException("Gateway response does not match original MD5 Hash challenge");
	        }
        
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + CHALLENGE
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + APILEVELAUTHENTICATION + CHALLENGE,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + CHALLENGE
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + APILEVELAUTHENTICATION + CHALLENGE,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + CHALLENGE
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING + APILEVELAUTHENTICATION + CHALLENGE,
                ex);
        } catch (NoSuchAlgorithmException e) {
            logger.warn(
                    "Error generating MD5 Challenge"
                        + " "
                        + e.toString());
                throw new TSMBeanException(
                        "Error generating MD5 Challenge",
                    e);
        }

    }
    
    
    /**
     *  Calls Authentication.authenticationSucceeded() using the Authentication 
     *  interface held by the TSMBean.
     */
    public void authenticationSucceeded() throws TSMBeanException {
        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKING
                        + APILEVELAUTHENTICATION
                        + AUTHENTICATION_SUCCEEDED);
            }
            apiLevelAuthentication.authenticationSucceeded();
            if (logger.isDebugEnabled()) {
                logger.debug(
                    INVOKED
                        + APILEVELAUTHENTICATION
                        + AUTHENTICATION_SUCCEEDED);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED,
                ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED,
                ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED
                    + " "
                    + ex.toString());
            throw new TSMBeanException(
                EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION
                    + AUTHENTICATION_SUCCEEDED,
                ex);
        }
    }
    
    /**
     * Invokes <CODE>requestAccess()</CODE> agianst the gateway.
     * 
     * @throws TSMBeanException
     */
    public IpAccess requestAccess(IpInterface clientAccess) throws TSMBeanException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Requesting access");
        }
        IpAccess result;
        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + APILEVELAUTHENTICATION + REQUEST_ACCESS);
                logger.debug("Access Type = " + TSMBeanImpl.ACCESS_TYPE);
            }
            IpInterface ipInterface = apiLevelAuthentication.requestAccess(
                    TSMBeanImpl.ACCESS_TYPE, clientAccess);
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + APILEVELAUTHENTICATION + REQUEST_ACCESS);
            }
            result = IpAccessHelper.narrow(ipInterface);
        } catch (TpCommonExceptions ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + REQUEST_ACCESS + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION + REQUEST_ACCESS, ex);
        } catch (P_ACCESS_DENIED ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + REQUEST_ACCESS + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION + REQUEST_ACCESS, ex);
        } catch (P_INVALID_ACCESS_TYPE ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + REQUEST_ACCESS + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION + REQUEST_ACCESS, ex);
        } catch (P_INVALID_INTERFACE_TYPE ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + REQUEST_ACCESS + " " + ParlayExceptionUtil.stringify(ex));
            throw new TSMBeanException(EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION + REQUEST_ACCESS, ex);
        } catch (org.omg.CORBA.SystemException ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + REQUEST_ACCESS + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING
                    + APILEVELAUTHENTICATION + REQUEST_ACCESS, ex);
        }
        return result;
    }

    public synchronized void cleanup() {
        if (logger.isDebugEnabled()) {
            logger.debug("Cleaning up " + toString());
        }
        if (!clean) {
            // null auth interface reference
            apiLevelAuthentication = null;
            tsmBean = null;
            clean = true;
        }
    }

    /**
     * Generates a 50 byte random array
     * 
     * @return the random array
     */
    protected byte[] generateRandomChallenge() {
        if (logger.isDebugEnabled()) {
            logger.debug("Generating a random challenge.");
        }
        byte[] result = new byte[50];

        Random random = new Random();

        random.nextBytes(result);

        return result;
    }

    /**
     * Encrypts the specified challenge according to the specified method.
     * 
     * @param challenge
     * @param encryptionMethod
     * @return encrypted challenge
     * @throws TSMBeanException
     */
    protected byte[] encryptChallenge(
        byte[] challenge,
        String encryptionMethod)
        throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("Encrypting challenge");
        }
        byte[] encryptedChallenge = null;

        if (encryptionMethod.startsWith("P_RSA")) {
            if (logger.isDebugEnabled()) {
                logger.debug("RSA Encryption.");
            }
            try {

                encryptedChallenge =
                    RSAUtil.encryptMessage(
                        challenge,
                        getAppRSAPublicKey(encryptionMethod),
                        tsmBean
                            .getFwProperties()
                            .getEncryptionCipherAlgorithm());
            }
            catch (RSAUtilException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("RSA Utility Exception " + ex.toString());
                }
                throw new TSMBeanException("RSA Utility Exception", ex);
            }
        }
        else if (encryptionMethod.equals(TSMBeanConstants.NULL_AUTH)) {
            if (logger.isDebugEnabled()) {
                logger.debug("No encryption transform needed.");
            }

            encryptedChallenge = challenge;
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("Framework chose an unsupported encryption method");
            }
            throw new TSMBeanException("Framework chose an unsupported encryption method");
        }

        return encryptedChallenge;
    }

    
    /**
     * Returns a RSA public key for the appropriate encryption method from the 
     * application vault.
     * 
     * @param encryptionMethod
     * @return PublickKey
     * @throws RSAUtilException
     * @throws TSMBeanException
     */
    protected RSAPublicKey getAppRSAPublicKey(String encryptionMethod)
        throws RSAUtilException, TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting application RSA public key.");
        }
        RSAPublicKey publicKey = null;
        int keySize;

        if (encryptionMethod.equals(TSMBeanConstants.P_RSA_1024)) {
            keySize = 1024;
        }
        else if (encryptionMethod.equals(TSMBeanConstants.P_RSA_512)) {
            keySize = 512;
        }
        else {
            throw new TSMBeanException(
                encryptionMethod
                    + " is not a supported RSA encryption method.");
        }

        String filename =
            getAppRSAPublicKeyFilename(tsmBean.getClientID(), keySize);
        	
            publicKey = RSAUtil.getPublicKey(filename, tsmBean.getClientID());

        if (logger.isDebugEnabled()) {
            logger.debug("Key Size = " + keySize);
        }

        return publicKey;
    }

    /**
     * Generates the appropriate filename for the application RSA public key file.
     * Depending on the algorithm specified in Fw.propeties it will be either an X.509
     * Certificate stored in a pem file or a hex encoding of the PublicKeyInfo format
     * stored directly in a file.
     * 
     * @param clientID
     * @param keySize
     * @return fileName
     */
    protected String getAppRSAPublicKeyFilename(String clientID, int keySize) {
        String result = null;
        String fileLocation =
            tsmBean.getFwProperties().getCertificateVault();

        if (logger.isDebugEnabled()) {
            logger.debug("Using X509 Certificate file.");
        }
        result =
            fileLocation + clientID + "CSWAY" + keySize + ".pem";
            //fileLocation + clientID + "RSACERTIFICATE" + keySize + ".pem";

        if (logger.isDebugEnabled()) {
            logger.debug("Key filename = " + result);
        }

        return result;
    }	

    /**
     * Invokes abortAuthentication() against the gateway.
     */
    public void abortAuthentication() {
        if (logger.isDebugEnabled()) {
            logger.debug("Aborting authentication");
        }
        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + APILEVELAUTHENTICATION
                        + ABORT_AUTHENTICATION);
            }
            apiLevelAuthentication.abortAuthentication();
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + APILEVELAUTHENTICATION
                        + ABORT_AUTHENTICATION);
            }
        } catch (TpCommonExceptions ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + ABORT_AUTHENTICATION + " " + ex.toString());
            // don't rethrow as this call is made in response to an earlier
            // exception which is the one
            // that will be propogated to the application.
        } catch (P_ACCESS_DENIED ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + ABORT_AUTHENTICATION + " " + ex.toString());
            // don't rethrow as this call is made in response to an earlier
            // exception which is the one
            // that will be propogated to the application.
        } catch (org.omg.CORBA.SystemException ex) {
            logger.warn(EXCEPTION_INVOKING + APILEVELAUTHENTICATION
                    + ABORT_AUTHENTICATION + " " + ex.toString());
            // don't rethrow as this call is made in response to an earlier
            // exception which is the one
            // that will be propogated to the application.
        }
}

    /**
     * @param apiLevelAuthentication The apiLevelAuthentication to set.
     */
    public static void setApiLevelAuthentication(
            IpAPILevelAuthentication apiLevelAuthentication) {
        AuthenticationHandlerImpl.apiLevelAuthentication = apiLevelAuthentication;
    }
}
