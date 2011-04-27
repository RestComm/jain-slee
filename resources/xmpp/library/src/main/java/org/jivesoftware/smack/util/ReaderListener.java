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

package org.jivesoftware.smack.util;

/**
 * Interface that allows for implementing classes to listen for string reading
 * events. Listeners are registered with ObservableReader objects.
 *
 * @see ObservableReader#addReaderListener
 * @see ObservableReader#removeReaderListener
 * 
 * @author Gaston Dombiak
 */
public interface ReaderListener {

    /**
     * Notification that the Reader has read a new string.
     * 
     * @param str the read String
     */
    public abstract void read(String str);
    
}
