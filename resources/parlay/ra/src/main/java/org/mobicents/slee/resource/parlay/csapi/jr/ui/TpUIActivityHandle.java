package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;

/**
 * 
 */
public class TpUIActivityHandle implements ActivityHandle, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final transient TpUIIdentifier uiIdentifier;

    public TpUIActivityHandle(TpUIIdentifier uiIdentifier) {
        super();
        this.uiIdentifier = uiIdentifier;
    }

    /**
     * @return Returns the uiIdentifier.
     */
    public TpUIIdentifier getUIIdentifier() {
        return uiIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ActivityHandle#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final TpUIActivityHandle rhs = (TpUIActivityHandle) obj;
            if (rhs.uiIdentifier != null
                    && rhs.uiIdentifier.equals(this.uiIdentifier)) {
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
        return uiIdentifier.hashCode();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuffer result = new StringBuffer("TpUIActivityHandle:");
        result.append("uiIdentifier,").append(uiIdentifier).append(';');

        return result.toString();
    }

}
