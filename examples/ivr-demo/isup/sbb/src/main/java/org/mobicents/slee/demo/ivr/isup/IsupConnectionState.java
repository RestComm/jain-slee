package org.mobicents.slee.demo.ivr.isup;

/**
 *
 * @author amit bhayani
 */
public enum IsupConnectionState {
    NULL, 
    CREATING_CONNECTION,     
    CONNECTION_CONNECTED, 
    CONNECTION_FAILED, 
    CALL_RELEASED,
    DS0_CONNECTION_DELETED,
    IVR_CONNECTION_DELETED;
}
