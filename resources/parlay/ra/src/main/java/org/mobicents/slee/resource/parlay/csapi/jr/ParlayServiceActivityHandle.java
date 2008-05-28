package org.mobicents.slee.resource.parlay.csapi.jr;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;

/**
 * Represents an activity handle for a Parlay service.
 */
public class ParlayServiceActivityHandle implements ActivityHandle, Serializable {

    /**
     * @param serviceIdentifier
     */
    public ParlayServiceActivityHandle(TpServiceIdentifier serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }

    private TpServiceIdentifier serviceIdentifier;

    /**
     * @return Returns the serviceIdentifier.
     */
    public TpServiceIdentifier getServiceIdentifier() {
        return serviceIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ActivityHandle#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            ParlayServiceActivityHandle rhs = (ParlayServiceActivityHandle) obj;
            if (rhs.serviceIdentifier != null
                    && rhs.serviceIdentifier.equals(this.serviceIdentifier)) {
                return true;
            }
        }
        return false;
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ActivityHandle#hashCode()
     */
    public int hashCode() {
        return serviceIdentifier.hashCode();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer result = new StringBuffer("ParlayServiceActivityHandle:");
        result.append("serviceIdentifier").append(serviceIdentifier).append(
                ";");

        return result.toString();
    }
}