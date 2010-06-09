package org.mobicents.slee.demo.ivr.isup;

/**
 *
 * @author amit bhayani
 */
public enum IsupConnectionState {
    NULL, 
    CREATING_CONNECTION, 
    CONNECTION_CREATED, 
    CONNECTION_CONNECTED, 
    CONNECTION_FAILED, 
    CALL_CANCELED,
    DELETING_CONNECTION, 
    CONNECTION_DELETED,
    MODIFYING_CONNECTION;
}
