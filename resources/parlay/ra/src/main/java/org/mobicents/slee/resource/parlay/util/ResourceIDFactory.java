package org.mobicents.slee.resource.parlay.util;

/**
 * Returns IDs to be used in the RA API.
 * 
 * TODO this class uses an in memory counter. Should use some sort of back end sequence
 * to avoid ID repetition and persistence across a failover.
 * May also refactor to a factory pattern and create one per service but that doesn't matter yet.
 * 
 */
public class ResourceIDFactory {
    
    public ResourceIDFactory() {
        super();
    }

    public static int getNextID() {
        return ++currentID;
    }
    
    public static int getCurrentID() {
        return currentID;
    }
    
    private static int currentID = Integer.MIN_VALUE;
}
