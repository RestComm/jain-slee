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

package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

/**
 * A listener that is fired anytime an invitation to join a MUC room is received.
 * 
 * @author Gaston Dombiak
 */
public interface InvitationListener {

    /**
     * Called when the an invitation to join a MUC room is received.<p>
     * 
     * If the room is password-protected, the invitee will receive a password to use to join
     * the room. If the room is members-only, the the invitee may be added to the member list.
     * 
     * @param conn the XMPPConnection that received the invitation.
     * @param room the room that invitation refers to.
     * @param inviter the inviter that sent the invitation. (e.g. crone1@shakespeare.lit).
     * @param reason the reason why the inviter sent the invitation.
     * @param password the password to use when joining the room.
     * @param message the message used by the inviter to send the invitation.
     */
    public abstract void invitationReceived(XMPPConnection conn, String room, String inviter, String reason,
                                            String password, Message message);

}
