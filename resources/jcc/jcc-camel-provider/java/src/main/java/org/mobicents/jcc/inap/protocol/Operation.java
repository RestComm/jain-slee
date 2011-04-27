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
 * The Java Call Control API for CAMEL 2
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

package org.mobicents.jcc.inap.protocol;

import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class Operation implements Serializable {
	
    public final static int INITIAL_DP = 0x00;
    public final static int APPLY_CHARGING = 0x23;
    public final static int CALL_INFORMATION_REQUEST = 0x2D;
    public final static int REQUEST_REPORT_BCSM_EVENT = 0x17;
    public final static int CALL_INFORMATION_REPORT = 44;
    public final static int EVENT_REPORT_BCSM = 24;
    public final static int CONTINUE = 0x1F;
    public final static int CONNECT = 0x14;
    
    
    protected int code;
    
    /** Creates a new instance of Operation */
    public Operation() {
        //this.code = new OperationCode(code);
    }
    
    public Operation(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    public abstract byte[] toByteArray();
}
