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

import java.io.Serializable;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class ConnectionID implements Serializable {

    private long id;
    private SccpAddress calledPartyAddress;
    private SccpAddress callingPartyAddress;

    /** Creates a new instance of CallID */
    public ConnectionID(long id,
            SccpAddress calledPartyAddress,
            SccpAddress callingPartyAddress) {
        this.id = id;
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
    }

    public long getId() {
        return id;
    }

    public SccpAddress getCalledPartyAddress() {
        return calledPartyAddress;
    }

    public SccpAddress getCallingPartyAddress() {
        return callingPartyAddress;
    }
}
