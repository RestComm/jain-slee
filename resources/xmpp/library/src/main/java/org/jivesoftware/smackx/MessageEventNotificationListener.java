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

/**
 *
 * A listener that is fired anytime a message event notification is received.
 * Message event notifications are received as a consequence of the request
 * to receive notifications when sending a message.
 *
 * @author Gaston Dombiak
 */
public interface MessageEventNotificationListener {

    /**
     * Called when a notification of message delivered is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     */
    public void deliveredNotification(String from, String packetID);

    /**
     * Called when a notification of message displayed is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     */
    public void displayedNotification(String from, String packetID);

    /**
     * Called when a notification that the receiver of the message is composing a reply is 
     * received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     */
    public void composingNotification(String from, String packetID);

    /**
     * Called when a notification that the receiver of the message is offline is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     */
    public void offlineNotification(String from, String packetID);

    /**
     * Called when a notification that the receiver of the message cancelled the reply 
     * is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     */
    public void cancelledNotification(String from, String packetID);
}
