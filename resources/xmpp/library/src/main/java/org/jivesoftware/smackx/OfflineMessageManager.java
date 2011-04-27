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

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The OfflineMessageManager helps manage offline messages even before the user has sent an
 * available presence. When a user asks for his offline messages before sending an available
 * presence then the server will not send a flood with all the offline messages when the user
 * becomes online. The server will not send a flood with all the offline messages to the session
 * that made the offline messages request or to any other session used by the user that becomes
 * online.<p>
 *
 * Once the session that made the offline messages request has been closed and the user becomes
 * offline in all the resources then the server will resume storing the messages offline and will
 * send all the offline messages to the user when he becomes online. Therefore, the server will
 * flood the user when he becomes online unless the user uses this class to manage his offline
 * messages.
 *
 * @author Gaston Dombiak
 */
public class OfflineMessageManager {

    private final static String namespace = "http://jabber.org/protocol/offline";

    private XMPPConnection connection;

    private PacketFilter packetFilter;

    public OfflineMessageManager(XMPPConnection connection) {
        this.connection = connection;
        packetFilter =
                new AndFilter(new PacketExtensionFilter("offline", namespace),
                        new PacketTypeFilter(Message.class));
    }

    /**
     * Returns true if the server supports Flexible Offline Message Retrieval. When the server
     * supports Flexible Offline Message Retrieval it is possible to get the header of the offline
     * messages, get specific messages, delete specific messages, etc.
     *
     * @return a boolean indicating if the server supports Flexible Offline Message Retrieval.
     * @throws XMPPException If the user is not allowed to make this request.
     */
    public boolean supportsFlexibleRetrieval() throws XMPPException {
        DiscoverInfo info = ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(null);
        return info.containsFeature(namespace);
    }

    /**
     * Returns the number of offline messages for the user of the connection.
     *
     * @return the number of offline messages for the user of the connection.
     * @throws XMPPException If the user is not allowed to make this request or the server does
     *                       not support offline message retrieval.
     */
    public int getMessageCount() throws XMPPException {
        DiscoverInfo info = ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(null,
                namespace);
        Form extendedInfo = Form.getFormFrom(info);
        if (extendedInfo != null) {
            String value = (String) extendedInfo.getField("number_of_messages").getValues().next();
            return Integer.parseInt(value);
        }
        return 0;
    }

    /**
     * Returns an iterator on <tt>OfflineMessageHeader</tt> that keep information about the
     * offline message. The OfflineMessageHeader includes a stamp that could be used to retrieve
     * the complete message or delete the specific message.
     *
     * @return an iterator on <tt>OfflineMessageHeader</tt> that keep information about the offline
     *         message.
     * @throws XMPPException If the user is not allowed to make this request or the server does
     *                       not support offline message retrieval.
     */
    public Iterator getHeaders() throws XMPPException {
        List answer = new ArrayList();
        DiscoverItems items = ServiceDiscoveryManager.getInstanceFor(connection).discoverItems(
                null, namespace);
        for (Iterator it = items.getItems(); it.hasNext();) {
            DiscoverItems.Item item = (DiscoverItems.Item) it.next();
            answer.add(new OfflineMessageHeader(item));
        }
        return answer.iterator();
    }

    /**
     * Returns an Iterator with the offline <tt>Messages</tt> whose stamp matches the specified
     * request. The request will include the list of stamps that uniquely identifies
     * the offline messages to retrieve. The returned offline messages will not be deleted
     * from the server. Use {@link #deleteMessages(java.util.List)} to delete the messages.
     *
     * @param nodes the list of stamps that uniquely identifies offline message.
     * @return an Iterator with the offline <tt>Messages</tt> that were received as part of
     *         this request.
     * @throws XMPPException If the user is not allowed to make this request or the server does
     *                       not support offline message retrieval.
     */
    public Iterator getMessages(final List nodes) throws XMPPException {
        List messages = new ArrayList();
        OfflineMessageRequest request = new OfflineMessageRequest();
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            OfflineMessageRequest.Item item = new OfflineMessageRequest.Item((String) it.next());
            item.setAction("view");
            request.addItem(item);
        }
        // Filter packets looking for an answer from the server.
        PacketFilter responseFilter = new PacketIDFilter(request.getPacketID());
        PacketCollector response = connection.createPacketCollector(responseFilter);
        // Filter offline messages that were requested by this request
        PacketFilter messageFilter = new AndFilter(packetFilter, new PacketFilter() {
            public boolean accept(Packet packet) {
                OfflineMessageInfo info = (OfflineMessageInfo) packet.getExtension("offline",
                        namespace);
                return nodes.contains(info.getNode());
            }
        });
        PacketCollector messageCollector = connection.createPacketCollector(messageFilter);
        // Send the retrieval request to the server.
        connection.sendPacket(request);
        // Wait up to a certain number of seconds for a reply.
        IQ answer = (IQ) response.nextResult(SmackConfiguration.getPacketReplyTimeout());
        // Stop queuing results
        response.cancel();

        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }

        // Collect the received offline messages
        Message message = (Message) messageCollector.nextResult(
                SmackConfiguration.getPacketReplyTimeout());
        while (message != null) {
            messages.add(message);
            message =
                    (Message) messageCollector.nextResult(
                            SmackConfiguration.getPacketReplyTimeout());
        }
        // Stop queuing offline messages
        messageCollector.cancel();
        return messages.iterator();
    }

    /**
     * Returns an Iterator with all the offline <tt>Messages</tt> of the user. The returned offline
     * messages will not be deleted from the server. Use {@link #deleteMessages(java.util.List)}
     * to delete the messages.
     *
     * @return an Iterator with all the offline <tt>Messages</tt> of the user.
     * @throws XMPPException If the user is not allowed to make this request or the server does
     *                       not support offline message retrieval.
     */
    public Iterator getMessages() throws XMPPException {
        List messages = new ArrayList();
        OfflineMessageRequest request = new OfflineMessageRequest();
        request.setFetch(true);
        // Filter packets looking for an answer from the server.
        PacketFilter responseFilter = new PacketIDFilter(request.getPacketID());
        PacketCollector response = connection.createPacketCollector(responseFilter);
        // Filter offline messages that were requested by this request
        PacketCollector messageCollector = connection.createPacketCollector(packetFilter);
        // Send the retrieval request to the server.
        connection.sendPacket(request);
        // Wait up to a certain number of seconds for a reply.
        IQ answer = (IQ) response.nextResult(SmackConfiguration.getPacketReplyTimeout());
        // Stop queuing results
        response.cancel();

        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }

        // Collect the received offline messages
        Message message = (Message) messageCollector.nextResult(
                SmackConfiguration.getPacketReplyTimeout());
        while (message != null) {
            messages.add(message);
            message =
                    (Message) messageCollector.nextResult(
                            SmackConfiguration.getPacketReplyTimeout());
        }
        // Stop queuing offline messages
        messageCollector.cancel();
        return messages.iterator();
    }

    /**
     * Deletes the specified list of offline messages. The request will include the list of
     * stamps that uniquely identifies the offline messages to delete.
     *
     * @param nodes the list of stamps that uniquely identifies offline message.
     * @throws XMPPException If the user is not allowed to make this request or the server does
     *                       not support offline message retrieval.
     */
    public void deleteMessages(List nodes) throws XMPPException {
        OfflineMessageRequest request = new OfflineMessageRequest();
        for (Iterator it = nodes.iterator(); it.hasNext();) {
            OfflineMessageRequest.Item item = new OfflineMessageRequest.Item((String) it.next());
            item.setAction("remove");
            request.addItem(item);
        }
        // Filter packets looking for an answer from the server.
        PacketFilter responseFilter = new PacketIDFilter(request.getPacketID());
        PacketCollector response = connection.createPacketCollector(responseFilter);
        // Send the deletion request to the server.
        connection.sendPacket(request);
        // Wait up to a certain number of seconds for a reply.
        IQ answer = (IQ) response.nextResult(SmackConfiguration.getPacketReplyTimeout());
        // Stop queuing results
        response.cancel();

        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }

    /**
     * Deletes all offline messages of the user.
     *
     * @throws XMPPException If the user is not allowed to make this request or the server does
     *                       not support offline message retrieval.
     */
    public void deleteMessages() throws XMPPException {
        OfflineMessageRequest request = new OfflineMessageRequest();
        request.setPurge(true);
        // Filter packets looking for an answer from the server.
        PacketFilter responseFilter = new PacketIDFilter(request.getPacketID());
        PacketCollector response = connection.createPacketCollector(responseFilter);
        // Send the deletion request to the server.
        connection.sendPacket(request);
        // Wait up to a certain number of seconds for a reply.
        IQ answer = (IQ) response.nextResult(SmackConfiguration.getPacketReplyTimeout());
        // Stop queuing results
        response.cancel();

        if (answer == null) {
            throw new XMPPException("No response from server.");
        } else if (answer.getError() != null) {
            throw new XMPPException(answer.getError());
        }
    }
}
