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

/*
 * File Name     : BlockingFilter.java
 *
 * The Java Call Control RA
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */


package org.mobicents.slee.resource.jcc.ra;

import java.io.InputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.HashMap;

import javax.csapi.cc.jcc.EventFilter;

import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.JccCallEvent;
import javax.csapi.cc.jcc.JccConnectionEvent;


/**
 * Implements EventFilter interface wich blocks all non-terminating events.
 * 
 * @author $Author: pavel $
 * @version $Revision: 1.3 $
 */
public class BlockingFilter implements EventFilter {
    
    private HashMap eventNames = new HashMap();
    private Properties props = null;
    
    /**
     * Creates a new instance of BlockingFilter 
     */
    public BlockingFilter() throws IOException {
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE");
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_ADDRESS_COLLECT),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ADDRESS_COLLECT");
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_ALERTING),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_ALERTING");                
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT");        
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_CALL_DELIVERY),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CALL_DELIVERY");        
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_CONNECTED),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CONNECTED");        
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_CREATED),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_CREATED");        
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_DISCONNECTED),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_DISCONNECTED");        
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_FAILED),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_FAILED");        
        eventNames.put(new Integer(JccConnectionEvent.CONNECTION_MID_CALL),
                "javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_MID_CALL");        
        
        eventNames.put(new Integer(JccCallEvent.CALL_ACTIVE),
                "javax.csapi.cc.jcc.JccCallEvent.CALL_ACTIVE");        
        eventNames.put(new Integer(JccCallEvent.CALL_CREATED),
                "javax.csapi.cc.jcc.JccCallEvent.CALL_CREATED");        
        eventNames.put(new Integer(JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED),
                "javax.csapi.cc.jcc.JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED");        
        eventNames.put(new Integer(JccCallEvent.CALL_INVALID),
                "javax.csapi.cc.jcc.JccCallEvent.CALL_INVALID");        
        eventNames.put(new Integer(JccCallEvent.CALL_SUPERVISE_END),
                "javax.csapi.cc.jcc.JccCallEvent.CALL_SUPERVISE_END");        
        eventNames.put(new Integer(JccCallEvent.CALL_SUPERVISE_START),
                "javax.csapi.cc.jcc.JccCallEvent.CALL_SUPERVISE_START");        
                
        props = new Properties();
        InputStream in = getClass().getResourceAsStream("/jcc-ra.properties");
        props.load(in);
    }

    public int getEventDisposition(JccEvent event) {
        Integer id = new Integer(event.getID());
        return getEventAction(id);
    }

    private int getEventAction(Integer id) {
            String eventName = (String) eventNames.get(id);
            String action = (String) props.get(eventName);
            
            if (id.intValue() == JccConnectionEvent.CONNECTION_CREATED) {
                return EventFilter.EVENT_BLOCK;
            }
            
            if (action == null) {
                return EventFilter.EVENT_DISCARD;                
            } 
            if (action.equalsIgnoreCase("block")) {
                return EventFilter.EVENT_BLOCK;
            }
            if (action.equalsIgnoreCase("notify")) {
                return EventFilter.EVENT_NOTIFY;
            }
            return EventFilter.EVENT_DISCARD;        
    }
}
