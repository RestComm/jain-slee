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

/**
 * A listener that is fired anytime your participant's status in a room is changed, such as the 
 * user being kicked, banned, or granted admin permissions.
 * 
 * @author Gaston Dombiak
 */
public interface UserStatusListener {

    /**
     * Called when a moderator kicked your user from the room. This means that you are no longer
     * participanting in the room.
     * 
     * @param actor the moderator that kicked your user from the room (e.g. user@host.org).
     * @param reason the reason provided by the actor to kick you from the room.
     */
    public abstract void kicked(String actor, String reason);

    /**
     * Called when a moderator grants voice to your user. This means that you were a visitor in 
     * the moderated room before and now you can participate in the room by sending messages to 
     * all occupants.
     * 
     */
    public abstract void voiceGranted();

    /**
     * Called when a moderator revokes voice from your user. This means that you were a 
     * participant in the room able to speak and now you are a visitor that can't send 
     * messages to the room occupants.
     * 
     */
    public abstract void voiceRevoked();

    /**
     * Called when an administrator or owner banned your user from the room. This means that you 
     * will no longer be able to join the room unless the ban has been removed.
     * 
     * @param actor the administrator that banned your user (e.g. user@host.org).
     * @param reason the reason provided by the administrator to banned you.
     */
    public abstract void banned(String actor, String reason);

    /**
     * Called when an administrator grants your user membership to the room. This means that you 
     * will be able to join the members-only room. 
     * 
     */
    public abstract void membershipGranted();

    /**
     * Called when an administrator revokes your user membership to the room. This means that you 
     * will not be able to join the members-only room.
     * 
     */
    public abstract void membershipRevoked();

    /**
     * Called when an administrator grants moderator privileges to your user. This means that you 
     * will be able to kick users, grant and revoke voice, invite other users, modify room's 
     * subject plus all the partcipants privileges.
     * 
     */
    public abstract void moderatorGranted();

    /**
     * Called when an administrator revokes moderator privileges from your user. This means that 
     * you will no longer be able to kick users, grant and revoke voice, invite other users, 
     * modify room's subject plus all the partcipants privileges.
     * 
     */
    public abstract void moderatorRevoked();

    /**
     * Called when an owner grants to your user ownership on the room. This means that you 
     * will be able to change defining room features as well as perform all administrative 
     * functions.
     * 
     */
    public abstract void ownershipGranted();

    /**
     * Called when an owner revokes from your user ownership on the room. This means that you 
     * will no longer be able to change defining room features as well as perform all 
     * administrative functions.
     * 
     */
    public abstract void ownershipRevoked();

    /**
     * Called when an owner grants administrator privileges to your user. This means that you 
     * will be able to perform administrative functions such as banning users and edit moderator 
     * list.
     * 
     */
    public abstract void adminGranted();

    /**
     * Called when an owner revokes administrator privileges from your user. This means that you 
     * will no longer be able to perform administrative functions such as banning users and edit 
     * moderator list.
     * 
     */
    public abstract void adminRevoked();

}
