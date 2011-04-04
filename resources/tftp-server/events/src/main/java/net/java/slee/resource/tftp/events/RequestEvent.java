package net.java.slee.resource.tftp.events;

import org.apache.commons.net.tftp.TFTPPacket;

/**
 * A wrapper event around a TFTP request.
 */
public interface RequestEvent {

	/**
	 * @return the Tftp Request which is associated with the event. There is always one.
	 */
	public TFTPPacket getRequest(); 

    /**
     * Return description of the current tftp packet type.
     * @return	
     */
    public String getTypeDescr();

    /**
     * Returns an event ID, unique in respect to the VM where it was generated 
     */
    public String getId();
}