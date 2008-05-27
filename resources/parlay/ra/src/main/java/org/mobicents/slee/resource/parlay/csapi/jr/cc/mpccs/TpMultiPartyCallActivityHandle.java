package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;

/**
 * 
 * Class Description for TpCallActivityHandle
 */
public class TpMultiPartyCallActivityHandle implements ActivityHandle, Serializable {

    /**
     * @param multiPartyCallIdentifier
     */
    public TpMultiPartyCallActivityHandle(
            TpMultiPartyCallIdentifier multiPartyCallIdentifier) {
        super();
        this.multiPartyCallIdentifier = multiPartyCallIdentifier;
    }

    private TpMultiPartyCallIdentifier multiPartyCallIdentifier;

    /**
     * @return Returns the multiPartyCallIdentifier.
     */
    public TpMultiPartyCallIdentifier getMultiPartyCallIdentifier() {
        return multiPartyCallIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ActivityHandle#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final TpMultiPartyCallActivityHandle rhs = (TpMultiPartyCallActivityHandle) obj;
            if (rhs.multiPartyCallIdentifier != null
                    && rhs.multiPartyCallIdentifier
                            .equals(this.multiPartyCallIdentifier)) {
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
        return multiPartyCallIdentifier.hashCode();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuffer result = new StringBuffer("TpMultiPartyCallActivityHandle:");
        result.append("multiPartyCallIdentifier,").append(multiPartyCallIdentifier).append(
                ';');

        return result.toString();
    }
}