package org.mobicents.slee.resource.parlay.csapi.jr;

/**
 * Classes using a ParlayConnectionProxy should implement this interface.
 */
public interface ParlayConnectionProxyAssociation {

    /**
     * @param connectionProxy
     */
    void setParlayConnectionProxy(ParlayConnectionProxy connectionProxy);
    
    /**
     * @return
     */
    ParlayConnectionProxy getParlayConnectionProxy();
}
