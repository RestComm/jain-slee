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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Represents timestamp information about data stored for later delivery. A DelayInformation will 
 * always includes the timestamp when the packet was originally sent and may include more 
 * information such as the JID of the entity that originally sent the packet as well as the reason
 * for the dealy.<p>
 * 
 * For more information see <a href="http://www.jabber.org/jeps/jep-0091.html">JEP-91</a>.
 * 
 * @author Gaston Dombiak
 */
public class DelayInformation implements PacketExtension {

    public static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    /**
     * New date format based on JEP-82 that some clients may use when sending delayed dates.
     * JEP-91 is using a SHOULD other servers or clients may be using this format instead of the
     * old UTC format.
     */
    public static SimpleDateFormat NEW_UTC_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    static {
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        NEW_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Date stamp;
    private String from;
    private String reason;

    /**
     * Creates a new instance with the specified timestamp. 
     */
    public DelayInformation(Date stamp) {
        super();
        this.stamp = stamp;
    }

    /**
     * Returns the JID of the entity that originally sent the packet or that delayed the 
     * delivery of the packet or <tt>null</tt> if this information is not available.
     * 
     * @return the JID of the entity that originally sent the packet or that delayed the 
     *         delivery of the packet.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the JID of the entity that originally sent the packet or that delayed the 
     * delivery of the packet or <tt>null</tt> if this information is not available.
     * 
     * @param from the JID of the entity that originally sent the packet.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the timstamp when the packet was originally sent. The returned Date is 
     * be understood as UTC.
     * 
     * @return the timstamp when the packet was originally sent.
     */
    public Date getStamp() {
        return stamp;
    }

    /**
     * Returns a natural-language description of the reason for the delay or <tt>null</tt> if 
     * this information is not available.
     * 
     * @return a natural-language description of the reason for the delay or <tt>null</tt>.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets a natural-language description of the reason for the delay or <tt>null</tt> if 
     * this information is not available.
     * 
     * @param reason a natural-language description of the reason for the delay or <tt>null</tt>.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getElementName() {
        return "x";
    }

    public String getNamespace() {
        return "jabber:x:delay";
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append(
                "\"");
        buf.append(" stamp=\"");
        synchronized (UTC_FORMAT) {
            buf.append(UTC_FORMAT.format(stamp));
        }
        buf.append("\"");
        if (from != null && from.length() > 0) {
            buf.append(" from=\"").append(from).append("\"");
        }
        buf.append(">");
        if (reason != null && reason.length() > 0) {
            buf.append(reason);
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

}
