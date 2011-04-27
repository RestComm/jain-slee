/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
