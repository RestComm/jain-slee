/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.media.demo;

import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import javax.slee.SbbLocalObject;

/**
 *
 * @author kulikov
 */
public interface InteractiveVoiceResponse extends SbbLocalObject {
    public void start(CallIdentifier callID, String packetRelayName);
}
