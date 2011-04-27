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

package org.jivesoftware.smack;

/**
 * Convenience class to make it easier to connect to the Google Talk IM service.
 * You can also use {@link XMPPConnection} to connect to Google Talk by specifying
 * the server name, service name, and port.<p>
 *
 * After creating the connection, log in in using a Gmail username and password.
 * For the Gmail address "jsmith@gmail.com", the username is "jsmith".
 *
 * @author Matt Tucker
 */
public class GoogleTalkConnection extends XMPPConnection {

    public GoogleTalkConnection() throws XMPPException {
        super("talk.google.com", 5222, "gmail.com");
    }
}
