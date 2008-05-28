
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;

/**
 *
 **/
public class TpCallActivityHandle  implements ActivityHandle, Serializable {
    
    private final transient TpCallIdentifier callIdentifier;
    
    public TpCallActivityHandle(TpCallIdentifier callIdentifier) {
        super();
        this.callIdentifier = callIdentifier;
    }

    /**
     * @return Returns the callIdentifier.
     */
    public TpCallIdentifier getCallIdentifier() {
        return callIdentifier;
    }
    
    /*
    * (non-Javadoc)
    * 
    * @see javax.slee.resource.ActivityHandle#equals(java.lang.Object)
    */
   public boolean equals(final Object obj) {
       if (obj != null && obj.getClass() == this.getClass()) {
           final TpCallActivityHandle rhs = (TpCallActivityHandle) obj;
           if (rhs.callIdentifier != null
                   && rhs.callIdentifier
                           .equals(this.callIdentifier)) {
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
       return callIdentifier.hashCode();
   }

   /*
    * @see java.lang.Object#toString()
    */
   public String toString() {
       StringBuffer result = new StringBuffer("TpCallActivityHandle:");
       result.append("callIdentifier,").append(callIdentifier).append(
               ";");

       return result.toString();
   }
    
}
