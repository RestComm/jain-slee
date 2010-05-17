/*
 * File Name     : JccCallActivityHandle.java
 *
 * The Java Call Control RA
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.slee.resource.jcc.ra;

import javax.csapi.cc.jcc.JccCall;
import javax.slee.resource.ActivityHandle;

/**
 * Represents the specific activity handle for Java Call Control RA.
 * This activity handle is bound to a call wich is identified by JccCall object.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccCallActivityHandle implements ActivityHandle {
    
    private JccCall call;
    
    /** 
     * Creates a new instance of JccCallActivityHandle.
     */
    public JccCallActivityHandle(JccCall call) {
        this.call = call;
    }

    public boolean equals(Object other) {
        if (other instanceof JccCallActivityHandle) {
            return (this.call == ((JccCallActivityHandle)other).call) || 
                    this.call.equals(((JccCallActivityHandle)other).call);
        }
        return false;
    }
       
    public int hashCode() {
        return call.hashCode();
    }      
    public String toString()
    {
    	return call==null?null:call.toString();
    }
}
