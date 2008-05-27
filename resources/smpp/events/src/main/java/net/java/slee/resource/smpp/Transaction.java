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

import java.util.Date;

/**
 * This interface represents a generic transaction interface defining the 
 * methods common between client and server transactions. 
 *
 * @author Oleg Kulikov
 */
public interface Transaction {
    public final static int STATUS_OK = 0;
    public final static int STATUS_INVALID_MESSAGE_LENGTH = 0x00000001;
    public final static int STATUS_SYSERROR = 0x00000008;
    public final static int STATUS_INVSRC = 0x0000000A;
    public final static int STATUS_INVDST = 0x0000000B;
    public final static int STATUS_INVMSGID = 0x0000000C;
    public final static int STATUS_CANCEL_FAILED = 0x00000011;
    public final static int STATUS_REPLACE_FAILED = 0x00000011;
    public final static int STATUS_MSGQFULL = 0x00000014;
    
    /**
     * Returns a unique identifer that identifies this transaction.
     *
     * @returns the unique identifier of this transaction.
     */
    public int getId();
    
    /**
     * Gets the date and time of the last activity of this transaction.
     *
     * @return the date object.
     */
    public Date getLastActivity();
}
