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

package org.mobicents.jcc.inap.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class EventReportBCSM extends Operation {
    
    private int eventType;
    
    /** Creates a new instance of EventReportBCSM */
    public EventReportBCSM() {
        this.code = Operation.EVENT_REPORT_BCSM;
    }

    public EventReportBCSM(byte[] bin) throws IOException {
        this.code = Operation.EVENT_REPORT_BCSM;
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        in.skip(2);
        eventType = in.read() & 0xff;
    }
    
    public int getEventType() {
        return eventType;
    }
    
    public byte[] toByteArray() {
        return null;
    }
    
}
