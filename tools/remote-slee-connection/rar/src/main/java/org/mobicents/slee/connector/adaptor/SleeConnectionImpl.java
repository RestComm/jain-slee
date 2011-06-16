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

package org.mobicents.slee.connector.adaptor;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnection;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;

import org.apache.log4j.Logger;

/**
 * Implementation of the SleeConnection as specified in
 * section F.2 of the JAIN SLEE 1.0 spec.
 * 
 * @author Tim
 */
public class SleeConnectionImpl implements SleeConnection {

	private static Logger log = Logger.getLogger(SleeConnectionImpl.class);
    
    private ManagedConnectionImpl managedConnection;

    SleeConnectionImpl(ManagedConnectionImpl managedConnection) {
        this.managedConnection = managedConnection;
    }

    /*
     * Create a handle to a null activity
     * This method is non-transactional
     * 
     * @see javax.slee.connection.SleeConnection#createActivityHandle()
     */
    public ExternalActivityHandle createActivityHandle()
            throws ResourceException {
    	if (log.isDebugEnabled()) {
    		log.debug("createActivityHandle() called");
    	}
        if (managedConnection == null)
            throw new IllegalStateException("Connection handle is invalid!");        
        return managedConnection.createActivityHandle();        
    }

    /*
     * Get the event type id given the name, vendor, version
     * This method is non-transactional
     * 
     * @see javax.slee.connection.SleeConnection#getEventTypeID(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public EventTypeID getEventTypeID(String name, String vendor, String version)
            throws UnrecognizedEventException, ResourceException {
    	if (log.isDebugEnabled()) {
    		log.debug("getEventTypeId() called");
    	}
        if (managedConnection == null)
            throw new IllegalStateException("Connection handle is invalid!");
        return managedConnection.getEventTypeID(name, vendor, version);
    }

    /*
     * Fire an event on the SLEE
     * This method is transactional
     * 
     * @see javax.slee.connection.SleeConnection#fireEvent(java.lang.Object,
     *      javax.slee.EventTypeID,
     *      javax.slee.connection.ExternalActivityHandle, javax.slee.Address)
     */
    public void fireEvent(Object event, EventTypeID eventType,
            ExternalActivityHandle activityHandle, Address address)
            throws NullPointerException, UnrecognizedActivityException,
            UnrecognizedEventException, ResourceException {
    	if (log.isDebugEnabled()) {
    		log.debug("fireEvent() called");
    	}
        if (managedConnection == null)
            throw new IllegalStateException("Connection handle is invalid!");
        managedConnection.fireEvent(event, eventType, activityHandle, address);
    }

    /*
     * Close a SLEE connection
     * 
     * @see javax.slee.connection.SleeConnection#close()
     */
    public void close() throws ResourceException {
    	if (log.isDebugEnabled()) {
    		log.debug("close() called");
    	}
        if (managedConnection == null)
            throw new IllegalStateException("Connection handle is invalid!");
                       
        managedConnection.handleClosed(this);
        managedConnection = null;
    }
    
    /* Only used for tests */
    public ManagedConnection getMC() {
        return this.getManagedConnection();
    }

    /* Package visibility methods - only to be called by other
     * clases in this adaptor
     */
    
    void invalidate() {
    	if (log.isDebugEnabled()) {
    		log.debug("invalidate() called");
    	}
        managedConnection = null;
    }

    void setManagedConnection(ManagedConnectionImpl managedConnection) {
        this.managedConnection = managedConnection;
    }

    ManagedConnectionImpl getManagedConnection() {
        return managedConnection;
    }

}