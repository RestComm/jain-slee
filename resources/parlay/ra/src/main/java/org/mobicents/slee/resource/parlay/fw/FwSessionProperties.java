package org.mobicents.slee.resource.parlay.fw;

import org.csapi.fw.TpDomainID;

/**
 * Encapsulates all the properties for FwSessionImpl. FwSessionProperties holds
 * data used in both Parlay 3 and Parlay 4 methods.
 */
public class FwSessionProperties {

    /* CONSTANTS */
    private static final String GET_SHARED_SECRET = "org.mobicents.fw.sharedSecret";

    private static final String DEFAULT_SHARED_SECRET = "SLEE / OSA Gateway";

    private static final String GET_AUTHENTICATION_TIMEOUT = "org.mobicents.fw.authenticationSucceededTimeout";

    private static final long DEFAULT_AUTHENTICATION_TIMEOUT = 10000;

    private static final String GET_AUTHENTICATION_HASH = "org.mobicents.fw.authenticationHash";

    private static final String DEFAULT_AUTHENTICATION_HASH = "P_OSA_MD5";

    private static final String GET_PARLAY_FW_VERSION = "org.mobicents.fw.parlayVersion";

    private static final String DEFAULT_PARLAY_FW_VERSION = "P_PARLAY_4";

    private static final String GET_INSTANCE_ID = "org.mobicents.fw.instanceID";

    private static final int DEFAULT_INSTANCE_ID = 0;

    private static final String GET_SIGNING_ALGORITHM_CAPS = "org.mobicents.fw.signingAlgorithmCapabilityList";

    private static final String DEFAULT_SIGNING_ALGORITHM_CAPS = "NULL";

    private static final String GET_CERTIFICATE_VAULT = "org.mobicents.fw.applicationCertificateVault";

    private static final String DEFAULT_CERTIFICATE_VAULT = "."+System.getProperty("file.separator");

    private static final String GET_AUTHENTICATION_CAPS = "org.mobicents.fw.authenticationCapabilityList";

    private static final String DEFAULT_AUTHENTICATION_CAPS = "NULL,P_RSA_512,P_RSA_1024";

    private static final String GET_SSA_TIMEOUT = "org.mobicents.fw.signServiceAgreementTimeout";

    private static final long DEFAULT_SSA_TIMEOUT = 10000;

    private static final String GET_DECRYPTION_CIPHER = "org.mobicents.fw.decryptionCipherAlgorithm";

    private static final String DEFAULT_DECRYPTION_CIPHER = "RSA/ECB/PKCS1Padding";

    private static final String GET_ENCRYPTION_CIPHER = "org.mobicents.fw.encryptionCipherAlgorithm";

    private static final String DEFAULT_ENCRYPTION_CIPHER = "RSA/ECB/PKCS1Padding";

    private static final String GET_RESPONSE_NAME = "org.mobicents.fw.responseName";

    private static final String DEFAULT_RESPONSE_NAME = "mobicents";

    /* VARIABLES */
    private String ipInitialIOR;

    private String fwParlayVersion;

    private String authenticationCapabilityList;

    private String namingServiceIOR;

    private String ipInitialLocation;

    private String ipInitialURL;
    
    private String authenticationHash;

    private String sharedSecret;

    private String certificateVault;

    private String signingAlgorithmCapabilityList;

    private String decryptionCipherAlgorithm;

    private String responseName;

    private String encryptionCipherAlgorithm;

    private TpDomainID domainID;

    private AuthenticationSequence authenticationSequence = AuthenticationSequence.TRUSTED;

    private long ssaTimeout;

    private int instanceID;

    private long authenticationSucceededTimeout;

    /**
     * @return Returns the ipInitialLocation.
     */
    public String getIpInitialLocation() {
        return ipInitialLocation;
    }
    /**
     * @param ipInitialLocation The ipInitialLocation to set.
     */
    public void setIpInitialLocation(String ipInitialLocation) {
        this.ipInitialLocation = ipInitialLocation;
    }
    /**
     * @return Returns the ipInitialURL.
     */
    public String getIpInitialURL() {
        return ipInitialURL;
    }
    /**
     * @param ipInitialURL The ipInitialURL to set.
     */
    public void setIpInitialURL(String ipInitialURL) {
        this.ipInitialURL = ipInitialURL;
    }
    /**
     * @return Returns the namingServiceIOR.
     */
    public String getNamingServiceIOR() {
        return namingServiceIOR;
    }
    /**
     * @param namingServiceIOR The namingServiceIOR to set.
     */
    public void setNamingServiceIOR(String namingServiceIOR) {
        this.namingServiceIOR = namingServiceIOR;
    }


    public FwSessionProperties() {
        setProperties();
    }

    /**
     * Initialises all properties that can't be setup by the user. Such
     * properties are included here so that they can be eaily opened up at a
     * later date instead of being hard-coded. All properties can be set using
     * system properties. This method can also be used to reinitialise
     * properties if such an operation is required during run time.
     */
    public void setProperties() {
        setAuthenticationCapabilityList();
        setAuthenticationHash();
        setAuthenticationSucceededTimeout();
        setCertificateVault();
        setFwParlayVersion();
        setInstanceID();
        setResponseName();
        setSharedSecret();
        setSigningAlgorithmCapabilityList();
        setSsaTimeout();
        setEncryptionCipherAlgorithm();
        setDecryptionCipherAlgorithm();
    }

    /**
     * @return Returns the ipInitialIOR.
     */
    public String getIpInitialIOR() {
        return ipInitialIOR;
    }

    /**
     * @param ipInitialIOR
     *            The ipInitialIOR to set.
     */
    public void setIpInitialIOR(String ipInitialIOR) {
        this.ipInitialIOR = ipInitialIOR;
    }

    /**
     * @return Returns the authenticationSequence.
     */
    public AuthenticationSequence getAuthenticationSequence() {
        return authenticationSequence;
    }

    /**
     * @param authenticationSequence
     *            The authenticationSequence to set. <br>
     *            e.g. ONE_WAY, TWO_WAY etc.
     */
    public void setAuthenticationSequence(
            AuthenticationSequence authenticationSequence) {
        this.authenticationSequence = authenticationSequence;
    }

    /**
     * @return Returns the domainID.
     */
    public TpDomainID getDomainID() {
        return domainID;
    }

    /**
     * @param domainID
     *            The domainID to set.
     */
    public void setDomainID(TpDomainID domainID) {
        this.domainID = domainID;
    }

    /**
     * @return Returns the saTimeout.
     */
    public long getSsaTimeout() {
        return ssaTimeout;
    }

    /**
     * Intialises the signServiceAgreement timeout.
     */
    private void setSsaTimeout() {
        this.ssaTimeout = System.getProperty(GET_SSA_TIMEOUT) != null ? Long
                .getLong(System.getProperty(GET_SSA_TIMEOUT)).longValue()
                : DEFAULT_SSA_TIMEOUT;
    }

    /**
     * @return instanceID
     */
    public int getInstanceID() {
        return instanceID;
    }

    /**
     *  
     */
    private void setInstanceID() {

        String iD = System.getProperty(GET_INSTANCE_ID);
        if (iD != null) {
            try {
                instanceID = Integer.parseInt(iD);
            } catch (NumberFormatException ex) {
                instanceID = DEFAULT_INSTANCE_ID;
            }
        }
    }

    /**
     * @return Returns the fwParlayVersion.
     */
    public String getFwParlayVersion() {
        return fwParlayVersion;
    }

    /**
     * Establishes the Parlay Version the Framework will use. <br>
     * This will affect the authentication sequence used.
     */
    private void setFwParlayVersion() {

        this.fwParlayVersion = System.getProperty(GET_PARLAY_FW_VERSION) != null ? System.getProperty(GET_PARLAY_FW_VERSION)
                : DEFAULT_PARLAY_FW_VERSION;
    }

    /**
     * @return Returns the authenticationCapabilityList.
     */
    public String getAuthenticationCapabilityList() {
        return authenticationCapabilityList;
    }

    /**
     * Establishes the authentication capability list. It is a comma seperated
     * list of different authentication encryption methods that <br>
     * will be passed to <blockquote>OSA Gateway </blockquote> in
     * <code>IpAPILevelAuthentication.selectEncryptionMethod</code>.
     */
    private void setAuthenticationCapabilityList() {
        this.authenticationCapabilityList = System.getProperty(GET_AUTHENTICATION_CAPS) != null ? System.getProperty(GET_AUTHENTICATION_CAPS)
                : DEFAULT_AUTHENTICATION_CAPS;
    }

    /**
     * @return Returns the authenticationHash.
     */
    public String getAuthenticationHash() {
        return authenticationHash;
    }

    /**
     * The authentication Hash is the hash algorithm used to encrypte the data
     * sent in the <br>
     * <code>IpClientAPILevelAuthentication.challenge()</code> parameter.
     */
    private void setAuthenticationHash() {
        this.authenticationHash = System.getProperty(GET_AUTHENTICATION_HASH) != null ? System.getProperty(GET_AUTHENTICATION_HASH)
                : DEFAULT_AUTHENTICATION_HASH;
    }

    /**
     * @return Returns the authenticationTimeout.
     */
    public long getAuthenticationSucceededTimeout() {
        return authenticationSucceededTimeout;
    }

    /**
     * The time in milliseconds the mobicents-parlay-ra will wait on an
     * <code>authenticationSucceeded</code><br>
     * method invocation.
     */
    private void setAuthenticationSucceededTimeout() {
        this.authenticationSucceededTimeout = System.getProperty(GET_AUTHENTICATION_TIMEOUT) != null ? Long
                .getLong(System.getProperty(GET_AUTHENTICATION_TIMEOUT))
                .longValue()
                : DEFAULT_AUTHENTICATION_TIMEOUT;
    }

    /**
     * @return Returns the sharedSecret.
     */
    public String getSharedSecret() {
        return sharedSecret;
    }

    /**
     * SharedSecret is an ascii String used as the basis for the challenge
     * encryption process.
     */
    private void setSharedSecret() {

        this.sharedSecret = System.getProperty(GET_SHARED_SECRET) != null ? System.getProperty(GET_SHARED_SECRET)
                : DEFAULT_SHARED_SECRET;

    }
    
    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    /**
     * @return Returns the gatewayCertificateVault.
     */
    public String getCertificateVault() {
        return certificateVault;
    }

    /**
     * @return Returns the signingAlgorithmCapabilityList.
     */
    public String getSigningAlgorithmCapabilityList() {
        return signingAlgorithmCapabilityList;
    }

    /**
     * The signingAlgorithmCapabilityList is a list of possible signing
     * algorithms supported by <br>
     * the mobicents-parlay-ra which the OSA gateway can choose from.
     */
    private void setSigningAlgorithmCapabilityList() {

        this.signingAlgorithmCapabilityList = System.getProperty(GET_SIGNING_ALGORITHM_CAPS) != null ? System.getProperty(GET_SIGNING_ALGORITHM_CAPS)
                : DEFAULT_SIGNING_ALGORITHM_CAPS;
    }

    private void setCertificateVault() {

        this.certificateVault = System.getProperty(GET_CERTIFICATE_VAULT) != null ? System.getProperty(GET_CERTIFICATE_VAULT)
                : DEFAULT_CERTIFICATE_VAULT;
    }

    /**
     * @return Returns the decryptionCipherAlgorithm.
     */
    public String getDecryptionCipherAlgorithm() {
        return decryptionCipherAlgorithm;
    }

    /**
     *  
     */
    private void setDecryptionCipherAlgorithm() {
        this.decryptionCipherAlgorithm = System.getProperty(GET_DECRYPTION_CIPHER) != null ? System.getProperty(GET_DECRYPTION_CIPHER)
                : DEFAULT_DECRYPTION_CIPHER;
    }

    /**
     * @return Returns the responseName.
     */
    public String getResponseName() {
        return responseName;
    }

    /**
     *  
     */
    private void setResponseName() {
        this.responseName = System.getProperty(GET_RESPONSE_NAME) != null ? System.getProperty(GET_RESPONSE_NAME)
                : DEFAULT_RESPONSE_NAME;
    }

    /**
     * @param fwParlayVersion
     *            The fwParlayVersion to set.
     */
    public void setFwParlayVersion(String fwParlayVersion) {
        this.fwParlayVersion = fwParlayVersion;
    }

    /**
     * @return Returns the encryptionCipherAlgorithm.
     */
    public String getEncryptionCipherAlgorithm() {
        return encryptionCipherAlgorithm;
    }

    /**
     * Specifies the ciper used to encode the message sent in <br>
     * <code>IpAPILevelAuthentication.authenticate()</code> method.
     */
    private void setEncryptionCipherAlgorithm() {
        this.encryptionCipherAlgorithm = System.getProperty(GET_ENCRYPTION_CIPHER) != null ? System.getProperty(GET_ENCRYPTION_CIPHER)
                : DEFAULT_ENCRYPTION_CIPHER;
    }
}