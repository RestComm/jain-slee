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

package org.jivesoftware.smackx;

import java.util.Iterator;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.*;

/**
 * Manages XHTML formatted texts within messages. A XHTMLManager provides a high level access to 
 * get and set XHTML bodies to messages, enable and disable XHTML support and check if remote XMPP
 * clients support XHTML.   
 * 
 * @author Gaston Dombiak
 */
public class XHTMLManager {

    private final static String namespace = "http://jabber.org/protocol/xhtml-im";

    // Enable the XHTML support on every established connection
    // The ServiceDiscoveryManager class should have been already initialized
    static {
        XMPPConnection.addConnectionListener(new ConnectionEstablishedListener() {
            public void connectionEstablished(XMPPConnection connection) {
                XHTMLManager.setServiceEnabled(connection, true);
            }
        });
    }

    /**
     * Returns an Iterator for the XHTML bodies in the message. Returns null if 
     * the message does not contain an XHTML extension.
     *
     * @param message an XHTML message
     * @return an Iterator for the bodies in the message or null if none.
     */
    public static Iterator getBodies(Message message) {
        XHTMLExtension xhtmlExtension = (XHTMLExtension) message.getExtension("html", namespace);
        if (xhtmlExtension != null)
            return xhtmlExtension.getBodies();
        else
            return null;
    }

    /**
     * Adds an XHTML body to the message.
     *
     * @param message the message that will receive the XHTML body
     * @param body the string to add as an XHTML body to the message
     */
    public static void addBody(Message message, String body) {
        XHTMLExtension xhtmlExtension = (XHTMLExtension) message.getExtension("html", namespace);
        if (xhtmlExtension == null) {
            // Create an XHTMLExtension and add it to the message
            xhtmlExtension = new XHTMLExtension();
            message.addExtension(xhtmlExtension);
        }
        // Add the required bodies to the message
        xhtmlExtension.addBody(body);
    }

    /**
     * Returns true if the message contains an XHTML extension.
     *
     * @param message the message to check if contains an XHTML extentsion or not
     * @return a boolean indicating whether the message is an XHTML message
     */
    public static boolean isXHTMLMessage(Message message) {
        return message.getExtension("html", namespace) != null;
    }

    /**
     * Enables or disables the XHTML support on a given connection.<p>
     *  
     * Before starting to send XHTML messages to a user, check that the user can handle XHTML
     * messages. Enable the XHTML support to indicate that this client handles XHTML messages.  
     *
     * @param connection the connection where the service will be enabled or disabled
     * @param enabled indicates if the service will be enabled or disabled 
     */
    public synchronized static void setServiceEnabled(XMPPConnection connection, boolean enabled) {
        if (isServiceEnabled(connection) == enabled)
            return;

        if (enabled) {
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature(namespace);
        }
        else {
            ServiceDiscoveryManager.getInstanceFor(connection).removeFeature(namespace);
        }
    }

    /**
     * Returns true if the XHTML support is enabled for the given connection.
     *
     * @param connection the connection to look for XHTML support
     * @return a boolean indicating if the XHTML support is enabled for the given connection
     */
    public static boolean isServiceEnabled(XMPPConnection connection) {
        return ServiceDiscoveryManager.getInstanceFor(connection).includesFeature(namespace);
    }

    /**
     * Returns true if the specified user handles XHTML messages.
     *
     * @param connection the connection to use to perform the service discovery
     * @param userID the user to check. A fully qualified xmpp ID, e.g. jdoe@example.com
     * @return a boolean indicating whether the specified user handles XHTML messages
     */
    public static boolean isServiceEnabled(XMPPConnection connection, String userID) {
        try {
            DiscoverInfo result =
                ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(userID);
            return result.containsFeature(namespace);
        }
        catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
