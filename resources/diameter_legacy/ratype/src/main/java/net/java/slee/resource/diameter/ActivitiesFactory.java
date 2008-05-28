package net.java.slee.resource.diameter;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import dk.i1.diameter.node.ConnectionKey;




public interface ActivitiesFactory {
	
    /**
     * Creates new Sh Interface Activity and return ACI for this activity
     * @param destHost - destination host of messages originating from this activity.
     * @param destRealm - destination realm of messages originating from this activity.
     * @param sessID - if null session id will be created independently, otherwise this will become part of generated session id.
     * @param authSessionState - Authentcation session state - shoudl it be always set to "no state maintained" ??
     * @param key - connection trough which messages shoudl be send
     * @return ACI for activity
     * @throws IllegalArgumentException - some of parameters are null, or have values out of scope.
     */
    public ActivityContextInterface makeShActivity(String destHost, String destRealm,
             String sessID,
            int authSessionState, ConnectionKey key) throws IllegalArgumentException;
}
