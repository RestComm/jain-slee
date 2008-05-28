package org.mobicents.slee.resource.parlay;

import java.io.IOException;
import java.util.Properties;

import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Encapsulates all properties for the RA
 */
public class ParlayResourceAdaptorProperties {
    /**
     * Commons Logger for this class
     * 
     */
    private static final Log logger = LogFactory
            .getLog(ParlayResourceAdaptorProperties.class);

    private transient String ipInitialIOR;

    private transient String namingServiceIOR;

    private transient String ipInitialLocation;

    private transient String ipInitialURL;

    private transient String domainID;

    private transient String authenticationSequence;
    
    private transient String parlayVersion;
    
    private transient String sharedSecret;
    
    public ParlayResourceAdaptorProperties() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuffer result = new StringBuffer(
                "ParlayResourceAdaptorProperties:");
        result.append("IpInitialIOR,").append(namingServiceIOR).append(';')
                .append("NamingServiceIOR,").append(ipInitialIOR).append(';')
                .append("IpInitialLocation,").append(ipInitialLocation).append(
                        ';').append("IpInitialURL,").append(ipInitialURL)
                .append(';').append("domainID,").append(domainID).append(';')
                .append("authenticationSequence,").append(
                        authenticationSequence).append(';').append(parlayVersion).append(';').append(sharedSecret);
        return result.toString();
    }

    /**
     * @return Returns the ipInitialLocation.
     */
    public String getIpInitialLocation() {
        return ipInitialLocation;
    }

    /**
     * @param ipInitialLocation
     *            The ipInitialLocation to set.
     */
    public void setIpInitialLocation(final String ipInitialLocation) {
        this.ipInitialLocation = ipInitialLocation;
    }

    /**
     * @return Returns the ipInitialURL.
     */
    public String getIpInitialURL() {
        return ipInitialURL;
    }

    /**
     * @param ipInitialURL
     *            The ipInitialURL to set.
     */
    public void setIpInitialURL(final String ipInitialURL) {
        this.ipInitialURL = ipInitialURL;
    }

    /**
     * @return Returns the namingServiceIOR.
     */
    public String getNamingServiceIOR() {
        return namingServiceIOR;
    }

    /**
     * @param namingServiceIOR
     *            The namingServiceIOR to set.
     */
    public void setNamingServiceIOR(final String namingServiceIOR) {
        this.namingServiceIOR = namingServiceIOR;
    }

    /**
     * Loads default properties from the ParlayResourceAdaptor.properties file.
     * 
     * @throws ResourceException
     *  
     */
    public void loadDefaults() throws ResourceException {
        final Properties properties = new Properties();

        try {
            properties.load(getClass().getResourceAsStream(
                    "ParlayResourceAdaptor.properties"));
        }
        catch (IOException e) {
            logger.error("Failed to load default properties", e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Loading default PARLAY RA properties: " + properties);
        }

        load(properties);
    }

    /**
     * Loads values from the specified properties.
     * 
     * @param properties
     * @throws ResourceException
     */
    public void load(final Properties properties) throws ResourceException {

        setIpInitialIOR(properties
                .getProperty("org.mobicents.slee.resource.parlay.ipInitialIOR"));
        setIpInitialLocation(properties
                .getProperty("org.mobicents.slee.resource.parlay.ipInitialLocation"));
        setIpInitialURL(properties
                .getProperty("org.mobicents.slee.resource.parlay.ipInitialURL"));
        setNamingServiceIOR(properties
                .getProperty("org.mobicents.slee.resource.parlay.namingServiceIOR"));

        if (!isIpInitialConfigValid()) {
            throw new ResourceException("IpInitial configuration is invalid.");
        }

        setDomainID(properties
                .getProperty("org.mobicents.slee.resource.parlay.domainID"));
        setAuthenticationSequence(properties
                .getProperty("org.mobicents.slee.resource.parlay.authenticationSequence"));
        
        setParlayVersion(properties.getProperty("org.mobicents.slee.resource.parlay.fw.parlayVersion"));
        
        setSharedSecret(properties.getProperty("org.mobicents.slee.resource.parlay.fw.sharedSecret"));
    }

    /**
     * @return Returns the authenticationSequence.
     */
    public String getAuthenticationSequence() {
        return authenticationSequence;
    }

    /**
     * @param authenticationSequence
     *            The authenticationSequence to set.
     */
    public void setAuthenticationSequence(final String authenticationSequence) {
        this.authenticationSequence = authenticationSequence;
    }

    /**
     * @return Returns the domainID.
     */
    public String getDomainID() {
        return domainID;
    }

    /**
     * @param domainID
     *            The domainID to set.
     */
    public void setDomainID(final String domainID) {
        this.domainID = domainID;
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
    public void setIpInitialIOR(final String ipInitialIOR) {
        this.ipInitialIOR = ipInitialIOR;
    }

    /**
     * Validates the IpInitial config has been set.
     * 
     * @return
     */
    public boolean isIpInitialConfigValid() {
        if ((ipInitialIOR != null && ipInitialIOR != "")
                || (ipInitialLocation != null && ipInitialLocation != ""
                        && namingServiceIOR != null && namingServiceIOR != "")
                || (ipInitialURL != null && ipInitialURL != "")) {
            return true;
        }

        return false;
        
    }

    /**
     * @return Returns the parlayVersion.
     */
    public String getParlayVersion() {
        return parlayVersion;
    }

    /**
     * @param parlayVersion The parlayVersion to set.
     */
    public void setParlayVersion(final String parlayVersion) {
        this.parlayVersion = parlayVersion;
    }

    /**
     * @return Returns the sharedSecret.
     */
    public String getSharedSecret() {
        return sharedSecret;
    }

    /**
     * @param sharedSecret The sharedSecret to set.
     */
    public void setSharedSecret(final String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
}