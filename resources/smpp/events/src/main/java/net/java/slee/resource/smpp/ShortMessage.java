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

import java.io.Serializable;

/**
 * Generic SMS interface.
 *
 * @author Oleg Kulikov
 */
public interface ShortMessage extends Serializable {
    
    /**
     * Gets the address of the origination party.
     *
     * @return the address of the origination party in E164 format.
     */ 
    public String getOriginator();
    
    /**
     * Modify the address of the origination party.
     *
     * @param origination the address of the originated party in E164 format.
     */
    public void setOriginator(String originator);

    /**
     * Gets address of the recipent of this message.
     *
     * @return the address of the recipient in E164 format.
     */
    public String getRecipient();
    
    /**
     * Modify address of the recipient.
     *
     * @param recepient the address of the recipient in E164 format.
     */
    public void setRecipient(String recipient);
    
    /**
     * Gets encdong scheme of this message.
     *
     * @return constant value which indicates the encoding scheme.
     */
    public int getEncoding();
    
    /**
     * Modify encoding scheme of this message.
     * 
     * @param encoding the constant which indicates the encoding scheme.
     */
    public void setEncoding(int encoding);
    
    /**
     * Gets text representation of the payload.
     *
     * @return payload as simple text.
     */
    public String getText();
    
    /**
     * Modify payload of this message.
     *
     * @param text the new payload as text.
     */
    public void setText(String text);
    
    /**
     * Gets binary representation of the payload of this message.
     *
     * @return the payload of this message.
     */
    public byte[] getData();
    
    /**
     * Modify payload of this message.
     *
     * @param data the payload of this message.
     */
    public void setData(byte[] data);
}
