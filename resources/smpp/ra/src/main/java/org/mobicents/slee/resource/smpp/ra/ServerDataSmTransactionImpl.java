/*
 * The SMPP resource adaptor.
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
package org.mobicents.slee.resource.smpp.ra;

import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.message.DataSMResp;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.version.VersionException;

import java.io.IOException;
import java.util.Date;

import net.java.slee.resource.smpp.ServerTransaction;
import net.java.slee.resource.smpp.ShortMessage;

/**
 *
 * @author Oleg Kulikov
 */
public class ServerDataSmTransactionImpl extends AbstractTransaction implements ServerTransaction {
    
    private Date lastActivity;
    private ShortMessage message;
    
    /** 
     * Creates a new instance.
     *
     * @int id the identifier of this transaction.
     * @connection the connection to SMSC. 
     */
    public ServerDataSmTransactionImpl(int id, SmppDialogImpl dialog, ShortMessage message) {
        super(id, dialog);
        this.message = message;
        lastActivity = new Date();
    }

    /**
     * (Non Java-doc).
     *
     * @see net.java.slee.resource.smpp.ClientTransaction#respond(int).
     */
    public void respond(int status) throws IOException {
        DataSMResp resp = null;
        try {
            resp = (DataSMResp) dialog.resourceAdaptor.smscConnection.newInstance(SMPPPacket.DATA_SM_RESP);
        } catch (VersionException e) {
            throw new IOException(e.getMessage());
        } catch (BadCommandIDException ex) {
            //should never happen
        }
        
        resp.setSequenceNum(id);
        resp.setCommandStatus(status);
        
        dialog.resourceAdaptor.smscConnection.sendResponse(resp);
        
        dialog.resourceAdaptor.fireEvent("net.java.slee.resource.smpp.MESSAGE", dialog, new RequestEventImpl(this, message));
        dialog.terminate(this);
        
        lastActivity = new Date();
   }

    /**
     * (Non-javadoc).
     *
     * @see net.java.slee.resource.smpp.Transaction#getLastActivity
     */
    public Date getLastActivity() {
        return lastActivity;
    }
    
}
