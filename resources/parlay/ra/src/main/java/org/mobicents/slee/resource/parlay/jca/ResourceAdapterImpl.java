package org.mobicents.slee.resource.parlay.jca;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;

import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.fw.TpDomainID;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.fw.AuthenticationSequence;
import org.mobicents.slee.resource.parlay.fw.FwSessionException;
import org.mobicents.slee.resource.parlay.fw.FwSessionProperties;
import org.mobicents.slee.resource.parlay.session.ParlaySession;
import org.mobicents.slee.resource.parlay.session.ParlaySessionImpl;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;

/**
 * Boootstrap class. Will initialise all resources for binding to gateway
 * including authenticating when started.
 *  
 */
public class ResourceAdapterImpl implements ResourceAdapter {
    /**
     * Commons Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(ResourceAdapterImpl.class);

    public ResourceAdapterImpl() {

    }

    protected transient BootstrapContext bootstrapContext = null;

    private transient ParlaySession parlaySession = null;

    private transient ActivityManager activityManager;

    private transient String ipInitialIOR;

    private transient String domainID;

    private transient String authenticationSequence;

    private transient String namingServiceIOR;

    private transient String ipInitialLocation;

    private transient String ipInitialURL;
    
    private transient String sharedSecret;
    
    private transient MpccsListener mpccsListener;
    
    private transient GccsListener gccsListener;
    
    private transient String parlayVersion;

    private UiListener uiListener;

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapter#start(javax.resource.spi.BootstrapContext)
     */
    public void start(final BootstrapContext arg0)
            throws ResourceAdapterInternalException {

        bootstrapContext = arg0;

        // Construct entity ID
        // Will always be a client domain for parlay apps
        TpDomainID tpDomainID = new TpDomainID();
        tpDomainID.ClientAppID(domainID);

        FwSessionProperties fwSessionProperties = new FwSessionProperties();
        fwSessionProperties.setAuthenticationSequence(AuthenticationSequence
                .getAuthenticationSequence(authenticationSequence));
        fwSessionProperties.setDomainID(tpDomainID);
        fwSessionProperties.setIpInitialIOR(ipInitialIOR);
        fwSessionProperties.setIpInitialLocation(ipInitialLocation);
        fwSessionProperties.setIpInitialURL(ipInitialURL);
        fwSessionProperties.setNamingServiceIOR(namingServiceIOR);
        fwSessionProperties.setFwParlayVersion(parlayVersion);
        fwSessionProperties.setSharedSecret(sharedSecret);

        try {
            // Initialise all Parlay State here
            // i.e. establish gateway connection and authenticate.

            parlaySession = new ParlaySessionImpl(fwSessionProperties,
                    activityManager);

            parlaySession.setMpccsListener(mpccsListener);

            parlaySession.setGccsListener(gccsListener);
            
            parlaySession.setUiListener(uiListener);

            parlaySession.init();

        } catch (FwSessionException e) {
            logger.error("Failed to init parlay session", e);
            if (parlaySession != null) {
                parlaySession.destroy();
                parlaySession = null;
            }

            throw new ResourceAdapterInternalException(
                    "Failed to init parlay session");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapter#stop()
     */
    public void stop() {
        // Destroy all parlay state here

        if (parlaySession != null) {
            parlaySession.destroy();
            parlaySession = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapter#endpointActivation(javax.resource.spi.endpoint.MessageEndpointFactory,
     *      javax.resource.spi.ActivationSpec)
     */
    public void endpointActivation(MessageEndpointFactory arg0,
            ActivationSpec arg1) throws ResourceException {

        // Not needed in SLEE environment
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapter#endpointDeactivation(javax.resource.spi.endpoint.MessageEndpointFactory,
     *      javax.resource.spi.ActivationSpec)
     */
    public void endpointDeactivation(MessageEndpointFactory arg0,
            ActivationSpec arg1) {

        // Not needed in SLEE environment
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapter#getXAResources(javax.resource.spi.ActivationSpec[])
     */
    public XAResource[] getXAResources(ActivationSpec[] arg0)
            throws ResourceException {

        // Not needed in SLEE environment
        return null;
    }

    /**
     * @return Returns the parlaySession.
     */
    public ParlaySession getParlaySession() {
        return parlaySession;
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
    public void setAuthenticationSequence(String authenticationSequence) {
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
    public void setDomainID(String domainID) {
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
    public void setIpInitialIOR(String ipInitialIOR) {
        this.ipInitialIOR = ipInitialIOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        
        if (!(o instanceof ResourceAdapterImpl)) {
            return false;
        }
        ResourceAdapterImpl rhs = (ResourceAdapterImpl) o;
        return new EqualsBuilder().append(authenticationSequence, rhs.authenticationSequence)
                .append(domainID, rhs.domainID)
                    .append(ipInitialIOR, rhs.ipInitialIOR)
                    .append(ipInitialLocation, rhs.ipInitialLocation)
                    .append(ipInitialURL, rhs.ipInitialURL)
                    .append(namingServiceIOR, rhs.namingServiceIOR)
                    .append(parlayVersion, rhs.parlayVersion)
                    .append(sharedSecret, rhs.sharedSecret)
                        .isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(13, 113).append(authenticationSequence)
        		.append(domainID)
                .append(ipInitialIOR)
                .append(ipInitialLocation)
                .append(ipInitialURL)
                .append(namingServiceIOR)
                .append(parlayVersion)
                .append(sharedSecret)
                .toHashCode();
    }

    /**
     * @param parlaySession
     *            The parlaySession to set.
     */
    public void setParlaySession(ParlaySession parlaySession) {
        this.parlaySession = parlaySession;
    }
 

    /**
     * @return Returns the mpccsListener.
     */
    public MpccsListener getMpccsListener() {
        return mpccsListener;
    }
    
    /**
     * @return Returns the gccsListener.
     */
    public GccsListener getGccsListener() {
        return gccsListener;
    }

    /**
     * @param mpccsListener
     *            The mpccsListener to set.
     */
    public void setMpccsListener(MpccsListener mpccsListener) {
        this.mpccsListener = mpccsListener; 
    }

    /**
     * @return Returns the activityManager.
     */
    public ActivityManager getActivityManager() {
        return activityManager;
    }

    /**
     * @param activityManager
     *            The activityManager to set.
     */
    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }
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

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.jca.ResourceAdapter#setGccsListener(org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener)
     */
    public void setGccsListener(GccsListener gccsListener) {
        this.gccsListener = gccsListener;
        
    }

    public void setUiListener(UiListener uiListener) {
        this.uiListener = uiListener;
        
    }
    
    public void setParlayVersion(String parlayVersion) {
        this.parlayVersion = parlayVersion;
        
    }

    public String getParlayVersion() {
        return parlayVersion;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

 
}