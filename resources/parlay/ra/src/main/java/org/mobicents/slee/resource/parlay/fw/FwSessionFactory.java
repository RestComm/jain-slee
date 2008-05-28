package org.mobicents.slee.resource.parlay.fw;
 
/**
 * 
 * Class Description for FwSessionFactory.
 */
public final class FwSessionFactory {
    
    

    
    private FwSessionFactory() { };

    /**
     * Creates a new FwSession instance. If the system property <CODE>bypassFw
     * </CODE> is set then a version that bypasses the parlay fw and goes
     * directly to the service instance will be returned.
     * 
     * @param properties
     * @return
     */
    public static FwSession createFwSession(final FwSessionProperties properties) {
        FwSession result = null;

        TestProperties testProperties = TestProperties.load();
 
        if (testProperties == null || !testProperties.isByPassFwEnabled()) {
            result = new FwSessionImpl(properties);
        } else {
            result = new BypassedFwSession(properties, testProperties);
        }

        return result;
    }
}
