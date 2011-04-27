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

import org.jivesoftware.smackx.packet.MultipleAddresses;

import java.util.List;

/**
 * MultipleRecipientInfo keeps information about the multiple recipients extension included
 * in a received packet. Among the information we can find the list of TO and CC addresses.
 *
 * @author Gaston Dombiak
 */
public class MultipleRecipientInfo {

    MultipleAddresses extension;

    MultipleRecipientInfo(MultipleAddresses extension) {
        this.extension = extension;
    }

    /**
     * Returns the list of {@link org.jivesoftware.smackx.packet.MultipleAddresses.Address}
     * that were the primary recipients of the packet.
     *
     * @return list of primary recipients of the packet.
     */
    public List getTOAddresses() {
        return extension.getAddressesOfType(MultipleAddresses.TO);
    }

    /**
     * Returns the list of {@link org.jivesoftware.smackx.packet.MultipleAddresses.Address}
     * that were the secondary recipients of the packet.
     *
     * @return list of secondary recipients of the packet.
     */
    public List getCCAddresses() {
        return extension.getAddressesOfType(MultipleAddresses.CC);
    }

    /**
     * Returns the JID of a MUC room to which responses should be sent or <tt>null</tt>  if
     * no specific address was provided. When no specific address was provided then the reply
     * can be sent to any or all recipients. Otherwise, the user should join the specified room
     * and send the reply to the room.
     *
     * @return the JID of a MUC room to which responses should be sent or <tt>null</tt>  if
     *         no specific address was provided.
     */
    public String getReplyRoom() {
        List replyRoom = extension.getAddressesOfType(MultipleAddresses.REPLY_ROOM);
        return replyRoom.isEmpty() ? null : ((MultipleAddresses.Address) replyRoom.get(0)).getJid();
    }

    /**
     * Returns true if the received packet should not be replied. Use
     * {@link MultipleRecipientManager#reply(org.jivesoftware.smack.XMPPConnection, org.jivesoftware.smack.packet.Message, org.jivesoftware.smack.packet.Message)}
     * to send replies. 
     *
     * @return true if the received packet should not be replied.
     */
    public boolean shouldNotReply() {
        return !extension.getAddressesOfType(MultipleAddresses.NO_REPLY).isEmpty();
    }

    /**
     * Returns the address to which all replies are requested to be sent or <tt>null</tt> if
     * no specific address was provided. When no specific address was provided then the reply
     * can be sent to any or all recipients.
     *
     * @return the address to which all replies are requested to be sent or <tt>null</tt> if
     *         no specific address was provided.
     */
    public MultipleAddresses.Address getReplyAddress() {
        List replyTo = extension.getAddressesOfType(MultipleAddresses.REPLY_TO);
        return replyTo.isEmpty() ? null : (MultipleAddresses.Address) replyTo.get(0);
    }
}
