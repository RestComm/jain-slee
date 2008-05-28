package org.mobicents.slee.resource.parlay.fw;

import org.apache.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * Class Description for TestProperties.
 */
public class TestProperties {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(TestProperties.class);

    private static final String BYPASSFW_PROP_NAME = "org.mobicents.slee.resource.parlay.isByPassFwEnabled";

    private static final String MPCCS_MANAGER_PROPERTY = "org.mobicents.slee.resource.parlay.IpMultiPartyCallControlManagerRef";

    private static final String GCCS_MANAGER_PROPERTY = "org.mobicents.slee.resource.parlay.IpCallControlManagerRef";
    
    private static final String UI_MANAGER_PROPERTY = "org.mobicents.slee.resource.parlay.IpUIManagerRef";

    public static TestProperties load() {

        TestProperties result = null;

        try {
            // If test bundle exists may use bypass version
            ResourceBundle bundle = ResourceBundle.getBundle("ParlayRATest");

            result = new TestProperties();

            result.setByPassFwEnabled(bundle.getString(BYPASSFW_PROP_NAME)
                    .equalsIgnoreCase("true") ? true : false);
            
            result.setByPassFwEnabled(true);

            result.setIpMultiPartyCallControlManagerFileName(bundle
                    .getString(MPCCS_MANAGER_PROPERTY));
            
            result.setIpCallControlManagerFileName(bundle.getString(GCCS_MANAGER_PROPERTY));
            
            result.setIpUIManagerFileName(bundle.getString(UI_MANAGER_PROPERTY));

        } catch (MissingResourceException e) {
            //if (logger.isDebugEnabled()) {
                logger.debug("Not using ParlayRATest resource bundle.", e);
            //}
            result = null;
        }
        return result;
    }





    private String ipMultiPartyCallControlManagerFileName;
    
    private String ipCallControlManagerFileName;
    
    private String ipUIManagerFileName;

    private boolean isByPassFwEnabled;

    /**
     * @return Returns the ipMultiPartyCallControlManagerFileName.
     */
    public String getIpMultiPartyCallControlManagerFileName() {
        return ipMultiPartyCallControlManagerFileName;
    }

    /**
     * @param ipMultiPartyCallControlManagerFileName
     *            The ipMultiPartyCallControlManagerFileName to set.
     */
    public void setIpMultiPartyCallControlManagerFileName(
            final String ipMultiPartyCallControlManagerFileName) {
        this.ipMultiPartyCallControlManagerFileName = ipMultiPartyCallControlManagerFileName;
    }

    /**
     * @return Returns the isByPassFwEnabled.
     */
    public boolean isByPassFwEnabled() {
        return isByPassFwEnabled;
    }

    /**
     * @param isByPassFwEnabled
     *            The isByPassFwEnabled to set.
     */
    public void setByPassFwEnabled(final boolean isByPassFwEnabled) {
        this.isByPassFwEnabled = isByPassFwEnabled;
    }
    

    /**
     * @param ipCallControlManagerFileName
     */
    private void setIpCallControlManagerFileName(final String ipCallControlManagerFileName) {
        this.ipCallControlManagerFileName = ipCallControlManagerFileName;
        
    }
    
    private void setIpUIManagerFileName(final String ipUIManagerFileName) {
        this.ipUIManagerFileName = ipUIManagerFileName;
        
    }
    /**
     * @return Returns the ipCallControlManagerFileName.
     */
    public String getIpCallControlManagerFileName() {
        return ipCallControlManagerFileName;
    }
    
    /**
     * @return Returns the ipUIManagerFileName.
     */
    public String getIpUIManagerFileName() {
        return ipUIManagerFileName;
    }

}
