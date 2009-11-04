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

import net.java.slee.resource.smpp.ShortMessage;

/**
 *
 * @author Oleg Kulikov
 */
public class SmppMessageImpl implements ShortMessage {
    
    private String originator;
    private String recipient;
    private int encoding;
    private byte[] data;
	//soowk: add status to keep Result for SUBMIT_SM_RESP
	private int status;
    
    /** Creates a new instance of SmppMessageImpl */
    public SmppMessageImpl(String originator, String recipient) {
        this.originator = originator;
        this.recipient = recipient;
    }

    public SmppMessageImpl(int status) {
		//soowk: save status information
		this.status = status;
    }
    
    public String getOriginator() {
		//soowk: bug fix
		//return recipient;
		return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int encoding) {
        this.encoding = encoding;
    }

    public String getText() {
        return new String(data);
    }

    public void setText(String text) {
        this.data = text.getBytes();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

	//soowk: retrieve status information
	//0 = no error
	public int getStatus()
	{
		return status;
	}

}
