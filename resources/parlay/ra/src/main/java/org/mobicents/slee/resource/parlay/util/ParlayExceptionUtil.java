package org.mobicents.slee.resource.parlay.util;

import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.P_INVALID_SESSION_ID;



/**
 * Logging utils.
 */
public  class ParlayExceptionUtil {
    
    private ParlayExceptionUtil() {
        super();
    }

    /**
     * Logging util for Parlay idlj type.
     * @param e Parlay idlj type
     * @return a printable concise error (with no stack trace) 
     */
    public final static String stringify(final P_INVALID_INTERFACE_TYPE e) {
        final StringBuffer sb = new StringBuffer(e.toString());
        appendExtraInfo(sb, e.ExtraInformation);        
        return sb.toString();
    }
 
    /**
     * Logging util for Parlay idlj type
     * @param e Parlay idlj type
     * @return a printable concise error (with no stack trace) 
     */
    public final static String stringify(final P_INVALID_SESSION_ID e) {
        final StringBuffer sb = new StringBuffer(e.toString());
        appendExtraInfo(sb, e.ExtraInformation);        
        return sb.toString();
    }

   

    /**
     * @param sb
     * @param e
     */
    private static void appendExtraInfo(final StringBuffer sb, final String extraInformation) {
        sb.append(", ExtraInformation = [");
        sb.append(extraInformation);
        sb.append("]");
    }
      
}
