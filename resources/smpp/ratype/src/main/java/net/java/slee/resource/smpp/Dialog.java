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
import java.util.Date;

/**
 * A dialog represents a peer-to-peer SMS relationship between two user agents 
 * that persists for some time. 
 *
 * A dialog is identified at each User Agent with a dialog Id, which consists of  
 * local address in E164 format and a remote address in E164 format. 
 *
 *
 * @author Oleg Kulikov
 */
public interface Dialog {
    /**
     * Gets the identifier of this dialog.
     *
     * @return the identifier of this dialog.
     */
    public String getId();
    
    /**
     * Gets the date and time of last activity.
     *
     * @return the date and time of the last activity.
     */
    public Date getLastActivity();
    
    /**
     * Gets the local party address of this dialog.
     *
     * @return local address in E164 address.
     */
    public String getLocalAddress();
    
    /**
     * Gets the address of the remote party.
     *
     * @return the address in E164 format.
     */
    public String getRemoteAddress();
    
    /**
     * Creates new message.
     * 
     * @return the new short message.
     */
    public ShortMessage createMessage();
    
    /**
     * Creates new client transaction.
     *
     * @return the new client transaction object.
     */
    public ClientTransaction createDataSmTransaction();

    /**
     * Creates new client transaction.
     *
     * @return the new client transaction object.
     */
    public ClientTransaction createSubmitSmTransaction();
    
    /**
     * Creates new client transaction.
     *
     * @return the new client transaction object.
     */
    public ClientTransaction createDeliverSmTransaction();
    
    /**
     * Closes this dialog.
     */
    public void close();
}
