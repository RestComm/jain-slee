/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor11;

import javax.slee.resource.ActivityHandle;

public class TCKActivityHandleImpl implements ActivityHandle {
    public TCKActivityHandleImpl(long vmStart, int sequenceID) {
        this(vmStart, sequenceID, null);
    }

    public TCKActivityHandleImpl(long vmStart, int sequenceID, String debugInfo) {
        this.vmStart = vmStart;
        this.sequenceID = sequenceID;
        this.debugInfo = debugInfo;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TCKActivityHandleImpl))
            return false;

        TCKActivityHandleImpl that = (TCKActivityHandleImpl) obj;
        if (this.vmStart == that.vmStart && this.sequenceID == that.sequenceID)
            return true;
        return false;
    }

    public int hashCode() {
        return (int) (vmStart + sequenceID);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("TCKActivityHandle[");
        buf.append("vmStart=").append(vmStart).append(",");
        buf.append("sequenceID=").append(sequenceID);
        if (debugInfo != null)
            buf.append(",debugInfo=\"").append(debugInfo).append("\"");
        buf.append("]");
        return buf.toString();
    }

    private final long vmStart;
    private final int sequenceID;
    private final String debugInfo;
}
