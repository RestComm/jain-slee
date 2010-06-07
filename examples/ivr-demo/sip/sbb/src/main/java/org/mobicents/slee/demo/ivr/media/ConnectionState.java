/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr.media;

/**
 *
 * @author kulikov
 */
public enum ConnectionState {
    NULL, 
    CREATING_CONNECTION, 
    CONNECTION_CREATED, 
    CONNECTION_CONNECTED, 
    CONNECTION_FAILED, 
    CALL_CANCELED,
    DELETING_CONNECTION, 
    CONNECTION_DELETED,
    WAITING_FOR_ACKNOWLEDGEMENT,
    MODIFYING_CONNECTION;
}
