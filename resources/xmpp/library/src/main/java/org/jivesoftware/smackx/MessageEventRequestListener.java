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
 * A listener that is fired anytime a message event request is received.
 * Message event requests are received when the received message includes an extension 
 * like this:
 * 
 * <pre>
 * &lt;x xmlns='jabber:x:event'&gt;
 *  &lt;offline/&gt;
 *  &lt;delivered/&gt;
 *  &lt;composing/&gt;
 * &lt;/x&gt;
 * </pre>
 * 
 * In this example you can see that the sender of the message requests to be notified
 * when the user couldn't receive the message because he/she is offline, the message 
 * was delivered or when the receiver of the message is composing a reply. 
 *
 * @author Gaston Dombiak
 */
public interface MessageEventRequestListener {

    /**
     * Called when a request for message delivered notification is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     * @param messageEventManager the messageEventManager that fired the listener.
     */
    public void deliveredNotificationRequested(String from, String packetID,
            MessageEventManager messageEventManager);

    /**
     * Called when a request for message displayed notification is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     * @param messageEventManager the messageEventManager that fired the listener.
     */
    public void displayedNotificationRequested(String from, String packetID,
            MessageEventManager messageEventManager);

    /**
     * Called when a request that the receiver of the message is composing a reply notification is 
     * received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     * @param messageEventManager the messageEventManager that fired the listener.
     */
    public void composingNotificationRequested(String from, String packetID,
                MessageEventManager messageEventManager);

    /**
     * Called when a request that the receiver of the message is offline is received.
     *  
     * @param from the user that sent the notification.
     * @param packetID the id of the message that was sent.
     * @param messageEventManager the messageEventManager that fired the listener.
     */
    public void offlineNotificationRequested(String from, String packetID,
            MessageEventManager messageEventManager);

}
