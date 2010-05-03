/*
 * The Java Call Control API for CAMEL 2
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

package org.mobicents.jcc.inap;

import javax.csapi.cc.jcc.EventFilter;
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.JccEvent;

/**
 *
 * @author Oleg Kulikov
 */
public class DefaultFilter implements EventFilter {
    
    /** Creates a new instance of DefaultFilter */
    public DefaultFilter() {
    }

    public int getEventDisposition(JccEvent event) {
        switch (event.getID()) {
            case JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT :
            case JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE :
                return EventFilter.EVENT_BLOCK;
            default :
                return EventFilter.EVENT_NOTIFY;
        }
    }
    
}
