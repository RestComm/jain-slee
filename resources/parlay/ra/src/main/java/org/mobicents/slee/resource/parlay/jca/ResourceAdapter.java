package org.mobicents.slee.resource.parlay.jca;

import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;

/**
 *
 **/
public interface ResourceAdapter extends javax.resource.spi.ResourceAdapter {

    /**
     * @param authenticationSequence
     */
    void setAuthenticationSequence(String authenticationSequence);

    /**
     * @param domainID
     */
    void setDomainID(String domainID);

    /**
     * @param ipInitialIOR
     */
    void setIpInitialIOR(String ipInitialIOR);

    /**
     * @param ipInitialLocation The ipInitialLocation to set.
     */
    void setIpInitialLocation(String ipInitialLocation);

    /**
     * @param ipInitialURL The ipInitialURL to set.
     */
    void setIpInitialURL(String ipInitialURL);

    /**
     * @param namingServiceIOR The namingServiceIOR to set.
     */
    void setNamingServiceIOR(String namingServiceIOR);

    /**
     * @param mpccsListener MPCC Listener.
     */
    void setMpccsListener(MpccsListener mpccsListener);
    
    /**
     * @param gccsListener GCC Listener.
     */ 
    void setGccsListener(GccsListener gccsListener);
    
    
    /**
     * @param  uiListener User Interaction Listener
     */ 
    void setUiListener(UiListener uiListener);
    
    
    /**
     * @return Returns the activityManager.
     */
    ActivityManager getActivityManager();

    /**
     * @param activityManager
     *            The activityManager to set.
     */
    void setActivityManager(ActivityManager activityManager);
    
    /**
     * @param parlayVersion
     */
    void setParlayVersion(String parlayVersion);
    
    /**
     * @return parlayVersion
     */
    String getParlayVersion();
    
    /**
     * @param sharedSecret
     */
    void setSharedSecret(String sharedSecret);
    
    /**
     * @return sharedSecret
     */
    String getSharedSecret();

}
