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
 * File Name     : JccCallEventLocal.java
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

package org.mobicents.slee.resource.jcc.local;

import javax.csapi.cc.jcc.*;

/**
 * Wraps JccCallEvent to disallow addConnectionListener, addCallListener methods. 
 * When a disallowed method is invoked, the resource adaptor
 * entity throws a SecurityException.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccCallEventLocal implements JccCallEvent {
    
    private int id;
    private int cause;
    private Object source;
    private JccCallLocal call;
    
    /** Creates a new instance of JccCallEventLocal */
    public JccCallEventLocal(int id, int cause, Object source, JccCallLocal call) {
        this.id = id;
        this.cause = cause;
        this.source = source;
        this.call = call;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCallEvent#getCall()
     */
    public JccCall getCall() {
        return call;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCallEvent#getCause()
     */
    public int getCause() {
        return cause;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCallEvent#getID()
     */
    public int getID() {
        return id;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCallEvent#getSource()
     */
    public Object getSource() {
        return source;
    }
    
}
