package net.java.slee.resource.diameter.message;

import dk.i1.diameter.Message;
import dk.i1.diameter.node.ConnectionKey;
import dk.i1.diameter.node.Peer;

public interface MessageEvent {
    /**
     * 
     * @return Diameter message.
     */
	public Message getMessage();
    public Object getSource();
    /**
     * 
     * @return Connection identifier through which this message has been received.
     */
    public ConnectionKey getConnectionKey();
    /**
     * 
     * @return Peers which has sent this message to us.
     */
    public Peer getPeer();
}
