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

package org.jivesoftware.smack.debugger;

import java.io.*;

import org.jivesoftware.smack.*;

/**
 * Interface that allows for implementing classes to debug XML traffic. That is a GUI window that 
 * displays XML traffic.<p>
 * 
 * Every implementation of this interface <b>must</b> have a public constructor with the following 
 * arguments: XMPPConnection, Writer, Reader.
 * 
 * @author Gaston Dombiak
 */
public interface SmackDebugger {

    /**
     * Called when a user has logged in to the server. The user could be an anonymous user, this 
     * means that the user would be of the form host/resource instead of the form 
     * user@host/resource.
     * 
     * @param user the user@host/resource that has just logged in
     */
    public abstract void userHasLogged(String user);

    /**
     * Returns the special Reader that wraps the main Reader and logs data to the GUI.
     * 
     * @return the special Reader that wraps the main Reader and logs data to the GUI.
     */
    public abstract Reader getReader();

    /**
     * Returns the special Writer that wraps the main Writer and logs data to the GUI.
     * 
     * @return the special Writer that wraps the main Writer and logs data to the GUI.
     */
    public abstract Writer getWriter();

    /**
     * Returns a new special Reader that wraps the new connection Reader. The connection
     * has been secured so the connection is using a new reader and writer. The debugger
     * needs to wrap the new reader and writer to keep being notified of the connection
     * traffic.
     *
     * @return a new special Reader that wraps the new connection Reader.
     */
    public abstract Reader newConnectionReader(Reader reader);

    /**
     * Returns a new special Writer that wraps the new connection Writer. The connection
     * has been secured so the connection is using a new reader and writer. The debugger
     * needs to wrap the new reader and writer to keep being notified of the connection
     * traffic.
     *
     * @return a new special Writer that wraps the new connection Writer.
     */
    public abstract Writer newConnectionWriter(Writer writer);

    /**
     * Returns the thread that will listen for all incoming packets and write them to the GUI. 
     * This is what we call "interpreted" packet data, since it's the packet data as Smack sees 
     * it and not as it's coming in as raw XML.
     * 
     * @return the PacketListener that will listen for all incoming packets and write them to 
     * the GUI
     */
    public abstract PacketListener getReaderListener();

    /**
     * Returns the thread that will listen for all outgoing packets and write them to the GUI. 
     * 
     * @return the PacketListener that will listen for all sent packets and write them to 
     * the GUI
     */
    public abstract PacketListener getWriterListener();
}