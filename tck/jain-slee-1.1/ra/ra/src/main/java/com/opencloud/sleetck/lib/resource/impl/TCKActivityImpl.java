/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.io.Serializable;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;

/**
 * TCKActivityImpl objects delegate to the TCK resource via
 * a remote reference, and may be serialized.
 */
public class TCKActivityImpl implements TCKActivity, Serializable {

    public TCKActivityImpl(TCKActivityID id, TCKResourceActivityInterface remoteResource) {
        this.id = id;
        this.remoteResource = remoteResource;
    }

    // -- Implementation of TCKActivity -- //

    public TCKActivityID getID() {
        return id;
    }

    public void endActivity() throws TCKTestErrorException {
        try {
            remoteResource.endActivity(id);
        } catch (Exception ex) {
            handleException(ex,"endActivity()");
        }
    }

    public boolean isLive() throws TCKTestErrorException {
        try {
            return remoteResource.isLive(id);
        } catch (Exception ex) {
            handleException(ex,"isLive()");
            return false; // never reached
        }
    }

    public void sendSbbMessage(Object payload) throws TCKTestErrorException {
        try {
            remoteResource.sendSbbMessage(payload, id);
        } catch (Exception ex) {
            handleException(ex,"sendSbbMessage()");
        }
    }

    // -- Implementation of java.lang.Object methods -- //

    public boolean equals(Object o) {
        return o != null && o instanceof TCKActivityImpl &&
            ((TCKActivityImpl)o).id.equals(id);
    }

    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return "TCKActivity: id="+id.toString();
    }

    // -- Private methods -- //

    private void handleException(Exception e, String method) throws TCKTestErrorException {
        if(e instanceof TCKTestErrorException) throw (TCKTestErrorException)e;
        throw new TCKTestErrorException("Unexpected exception caught while invoking "+method+
            " from the TCKActivity proxy class",e);
    }

    // -- Private state -- //

    private TCKActivityID id;
    private TCKResourceActivityInterface remoteResource;

}