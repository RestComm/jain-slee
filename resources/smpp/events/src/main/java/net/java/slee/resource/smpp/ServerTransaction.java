/*
 * The Short Message Service resource adaptor type
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
package net.java.slee.resource.smpp;

import java.io.IOException;
import ie.omk.smpp.message.SMPPPacket;

/**
 *
 * @author Oleg Kulikov
 */
public interface ServerTransaction extends Transaction {
    /**
     * Acknowledges this transaction.
     *
     * @param status an integer identified the result of the transaction.
     */
    public void respond(int status) throws IOException;
}
