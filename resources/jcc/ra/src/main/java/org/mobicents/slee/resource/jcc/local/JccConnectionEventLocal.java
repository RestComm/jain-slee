/*
 * File Name     : JccConnectionEventLocal.java
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

package org.mobicents.slee.resource.jcc.local;

import javax.csapi.cc.jcc.*;

/**
 * Wraps JccConnectionEvent to disallow addConnectionListener, addCallListener methods. 
 * When a disallowed method is invoked, the resource adaptor
 * entity throws a SecurityException.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccConnectionEventLocal extends JccCallEventLocal implements JccConnectionEvent {
    
    private JccConnectionLocal connection;
    
    
    /** Creates a new instance of JccCallEventLocal */
    public JccConnectionEventLocal(JccConnectionLocal connection, int id, 
            int cause, Object source, JccCallLocal call) {
        super(id, cause, source, call);
        this.connection = connection;
    }


    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnectionEvent#getConnection()
     */
    public JccConnection getConnection() {
        return connection;
    }

    
}
