package org.mobicents.slee.resource.parlay;

import javax.slee.resource.ResourceException;

/**
 * Defines internal provider operations not exposed to Slee container
 * components.
 * 
 */
public interface ParlayProvider extends ParlayResourceAdapterSbbInterface {

    /**
     * Starts the provider.
     * 
     * @throws ResourceException
     */
    void start() throws ResourceException;

    /**
     * Stops the provider.
     * 
     * @throws ResourceException
     */
    void stop() throws ResourceException;
}
