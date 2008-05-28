package org.mobicents.slee.resource.parlay.session;

import javax.slee.resource.ResourceException;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;

/**
 * Stores state pertaining to a specific service session.
 */
public interface ServiceSession {
    
    int MultiPartyCallControl = 0;
    
    int UserInteraction = 1;
    
    int GenericCallControl = 2;

    /**
     * Return an int identifying the type of this service.
     * 
     * @return
     */
    int getType();
    
    /**
     * Returns an identifier for the underlying service session
     * @return
     */
    TpServiceIdentifier getTpServiceIdentifier();
    
    /**
     * Initialises this session.
     * 
     * @throws ResourceException
     */
    void init() throws ResourceException;
    
    /**
     * Destroys this session
     */
    void destroy();
}
