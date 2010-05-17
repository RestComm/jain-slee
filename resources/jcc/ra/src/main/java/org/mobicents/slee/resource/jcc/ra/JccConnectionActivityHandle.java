/*
 * File Name     : JccConnectionActivityHandle.java
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

import javax.slee.resource.ActivityHandle;
import javax.csapi.cc.jcc.JccConnection;

/**
 * Represents the specific activity handle for Java Call Control RA.
 * This activity handle is bound to a connection wich is identified by 
 * JccConnection object.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccConnectionActivityHandle implements ActivityHandle {
    
    private JccConnection connection;
    private String id;
    
    /** Creates a new instance of JccConnectionActivityHandle */
    public JccConnectionActivityHandle(JccConnection connection) {
        this.connection = connection;
    }
    
    public String getID() {
        return connection.toString();
    }
    
    public boolean equals(Object other) {
        if (other instanceof JccConnectionActivityHandle) {
            return (this.connection == ((JccConnectionActivityHandle)other).connection) || 
                    this.connection.equals(((JccConnectionActivityHandle)other).connection);
        }
        return false;
    }
       
    public int hashCode() {
        return connection.hashCode();
    }       
    public String toString()
    {
    	return connection == null?null:connection.toString();
    }
}
