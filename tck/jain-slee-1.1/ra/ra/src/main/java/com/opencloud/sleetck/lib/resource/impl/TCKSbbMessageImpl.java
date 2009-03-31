/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import java.io.Serializable;

public class TCKSbbMessageImpl implements TCKSbbMessage, Serializable {

    public TCKSbbMessageImpl(Object payload) {
        this.payload = payload;
    }

    public Object getMessage() {
        return payload;
    }

    public boolean equals(Object o) {
        if(o != null && o instanceof TCKSbbMessageImpl) {
            TCKSbbMessageImpl other = (TCKSbbMessageImpl)o;
            return (other.payload == null ? payload == null : other.payload.equals(payload));
        } else return false;
    }

    public int hashCode() {
        return payload == null ? 0 : payload.hashCode();
    }

    public String toString() {
        return "TCKSbbMessage: payload="+payload;
    }

    private Object payload;

}