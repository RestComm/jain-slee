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


package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * A last activity IQ for retrieving information about the last activity associated with a Jabber ID.
 * LastActivity (JEP-012) allows for retrieval of how long a particular user has been idle and the
 * message the specified when doing so.
 * To get the last activity of a user, simple send the LastActivity packet to them, as in the
 * following code example:
 * <p/>
 * <pre>
 * XMPPConnection con = new XMPPConnection("jabber.org");
 * con.login("john", "doe");
 * LastActivity activity = LastActivity.getLastActivity(con, "xray@jabber.org");
 * </pre>
 *
 * @author Derek DeMoro
 */
public class LastActivity extends IQ {

    public long lastActivity;
    public String message;

    public LastActivity() {
        setType(IQ.Type.GET);
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:last\"></query>");
        return buf.toString();
    }


    private void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }


    private void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns number of seconds that have passed since the user last logged out.
     * If the user is offline, 0 will be returned.
     *
     * @return the number of seconds that have passed since the user last logged out.
     */
    public long getIdleTime() {
        return lastActivity;
    }


    /**
     * Returns the status message of the last unavailable presence received from the user.
     *
     * @return the status message of the last unavailable presence received from the user
     */
    public String getStatusMessage() {
        return message;
    }


    /**
     * The IQ Provider for LastActivity.
     *
     * @author Derek DeMoro
     */
    public static class Provider implements IQProvider {

        public Provider() {
            super();
        }

        public IQ parseIQ(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }

            LastActivity lastActivity = new LastActivity();
            try {
                String seconds = parser.getAttributeValue("", "seconds");
                String message = parser.nextText();
                if (seconds != null) {
                    long xmlSeconds = new Double(seconds).longValue();
                    lastActivity.setLastActivity((int)xmlSeconds);
                }

                if (message != null) {
                    lastActivity.setMessage(message);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return lastActivity;
        }
    }

    /**
     * Retrieve the last activity of a particular jid.
     * @param con the current XMPPConnection.
     * @param jid the JID of the user.
     * @return the LastActivity packet of the jid.
     * @throws XMPPException thrown if a server error has occured.
     */
    public static LastActivity getLastActivity(XMPPConnection con, String jid) throws XMPPException {
        LastActivity activity = new LastActivity();
        jid = StringUtils.parseBareAddress(jid);
        activity.setTo(jid);

        PacketCollector collector = con.createPacketCollector(new PacketIDFilter(activity.getPacketID()));
        con.sendPacket(activity);

        LastActivity response = (LastActivity) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());

        // Cancel the collector.
        collector.cancel();
        if (response == null) {
            throw new XMPPException("No response from server on status set.");
        }
        if (response.getError() != null) {
            throw new XMPPException(response.getError());
        }
        return response;
    }
}
