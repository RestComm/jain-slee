package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier;

/**
 * 
 */
public class TpUICallActivityHandle implements ActivityHandle, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final transient TpUICallIdentifier uiCallIdentifier;

    public TpUICallActivityHandle(TpUICallIdentifier uiCallIdentifier) {
        super();
        this.uiCallIdentifier = uiCallIdentifier;
    }

    /**
     * @return Returns the uiCallIdentifier.
     */
    public TpUICallIdentifier getUICallIdentifier() {
        return uiCallIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ActivityHandle#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final TpUICallActivityHandle rhs = (TpUICallActivityHandle) obj;
            if (rhs.uiCallIdentifier != null
                    && rhs.uiCallIdentifier.equals(this.uiCallIdentifier)) {
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
        return uiCallIdentifier.hashCode();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuffer result = new StringBuffer("TpUICallActivityHandle:");
        result.append("uiCallIdentifier,").append(uiCallIdentifier).append(';');

        return result.toString();
    }

}
