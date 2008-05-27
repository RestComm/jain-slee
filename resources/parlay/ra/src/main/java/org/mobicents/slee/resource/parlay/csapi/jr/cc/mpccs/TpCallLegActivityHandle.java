package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;

/**
 *
 * Class Description for TpCallLegActivityHandle
 */
public class TpCallLegActivityHandle implements ActivityHandle, Serializable {

    /**
     * @param multiPartyCallIdentifier
     */
    public TpCallLegActivityHandle(
            TpCallLegIdentifier multiPartyCallIdentifier) {
        super();
        this.callLegIdentifier = multiPartyCallIdentifier;
    }

    private final transient TpCallLegIdentifier callLegIdentifier;

    /**
     * @return Returns the multiPartyCallIdentifier.
     */
    public TpCallLegIdentifier getCallLegIdentifier() {
        return callLegIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ActivityHandle#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            TpCallLegActivityHandle rhs = (TpCallLegActivityHandle) obj;
            if (rhs.callLegIdentifier != null
                    && rhs.callLegIdentifier
                            .equals(this.callLegIdentifier)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ActivityHandle#hashCode()
     */
    public int hashCode() {
        return callLegIdentifier.hashCode();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuffer result = new StringBuffer("TpCallLegActivityHandle:");
        result.append("callLegIdentifier,").append(callLegIdentifier).append(
                ';');

        return result.toString();
    }

}
