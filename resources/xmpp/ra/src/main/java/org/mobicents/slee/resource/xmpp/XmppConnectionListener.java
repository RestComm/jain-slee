package org.mobicents.slee.resource.xmpp;

import javax.slee.facilities.Tracer;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

public class XmppConnectionListener implements PacketListener, ConnectionListener {
	
	private final XmppResourceAdaptor ra;
	private final Tracer tracer;
	private final String connectionID;
	private final XmppActivityHandle handle;
	
	/**
	 * @param ra
	 * @param connectionID
	 * @param handle
	 */
	public XmppConnectionListener(XmppResourceAdaptor ra, String connectionID,
			XmppActivityHandle handle) {
		this.ra = ra;
		this.tracer = ra.getTracer();
		this.connectionID = connectionID;
		this.handle = handle;
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	public void processPacket(Packet packet) {
		
		if (tracer.isFineEnabled()) {
			tracer.fine(connectionID+" received packet: "+packet.toXML());			
		}
		ra.processEvent(packet,handle); 
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
	 */
	public void connectionClosed() {
		if (tracer.isInfoEnabled()) {
			tracer.info("Got notification that connection with id="+ connectionID+" closed. Requesting ActivityEndEvent.");
		}
		ra.endActivity(handle);
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java.lang.Exception)
	 */
	public void connectionClosedOnError(Exception e) {
		if (tracer.isInfoEnabled()) {
			tracer.info("Got notification that connection with id="+ connectionID+" closed on error. Requesting ActivityEndEvent.",e);
		}
		ra.endActivity(handle);
	}

}
