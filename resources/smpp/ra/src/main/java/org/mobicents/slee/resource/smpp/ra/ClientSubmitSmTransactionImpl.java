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
package org.mobicents.slee.resource.smpp.ra;

import ie.omk.smpp.Address;
import ie.omk.smpp.BadCommandIDException;
import ie.omk.smpp.message.DataSM;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.tlv.TLVTable;
import ie.omk.smpp.message.tlv.Tag;
import ie.omk.smpp.version.VersionException;
import java.io.IOException;
import java.util.Date;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.ShortMessage;
import net.java.slee.resource.smpp.ClientTransaction;

import ie.omk.smpp.Connection;

/**
 *
 * @author Oleg Kulikov
 */
public class ClientSubmitSmTransactionImpl extends AbstractTransaction implements ClientTransaction {
    
    private Date lastActivity;
    
    /** 
     * Creates a new instance.
     *
     * @int id the identifier of this transaction.
     * @connection the connection to SMSC. 
     */
    public ClientSubmitSmTransactionImpl(int id, SmppDialogImpl dialog) {
        super(id, dialog);
    }
    
    /**
     * (Non Java-doc).
     *
     * @see net.java.slee.resource.smpp.ClientTransaction#send(net.java.slee.resource.smpp.ShortMessage).
     */
    public void send(ShortMessage message) throws IOException {
        SubmitSM sm = null;
        Connection connection = dialog.resourceAdaptor.smscConnection;
        try {
            sm = (SubmitSM) connection.newInstance(SMPPPacket.SUBMIT_SM);
        } catch (VersionException ex) {
            throw new IOException(ex.getMessage());
        } catch (BadCommandIDException ex) {
            //should never happen
        }
        
        sm.setSequenceNum(id);
        System.out.println("Sending message id " + id);
        
        String destination = message.getRecipient();
        String source = message.getOriginator();
        int encoding = message.getEncoding();
        
        
        Address destinationAddress = new Address(1, 1, destination);
        Address sourceAddress = new Address(1, 1, source);
        
        sm.setDestination(destinationAddress);
        sm.setSource(sourceAddress);
        sm.setDataCoding(encoding);
        
        TLVTable table = new TLVTable();
        table.set(Tag.MESSAGE_PAYLOAD, message.getData());
        sm.setTLVTable(table);
        
        connection.sendRequest(sm);
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
