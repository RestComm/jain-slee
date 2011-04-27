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
package org.mobicents.jcc.inap.protocol.parms;

import java.io.Serializable;

/**
 * Nature Of Address indicator.
 * 
 * @author kulikov
 */
public class NoA implements Serializable {
    public final static int SPARE = 0;
    public final static int SUBSCRIBER = 1;
    public final static int UNKNOWN = 2;
    public final static int NATIONAL = 3;
    public final static int INTERNATIONAL = 4;
    public final static int NETWORK_SPECIFIC = 5;

    private final static String[] IND = new String[] {
        "SPARE", "SUBSCRIBER", "UNKNOWN", "NATIONAL", "INTERNATIONAL", "NETWORK_SPECIFIC"
    };
    
    public static synchronized int parse(String value) {
        String s = value.substring(value.indexOf("=") +  1);
        if (s.equals("subscriber")) {
            return SUBSCRIBER;
        } else if (s.equals("national")) {
            return NATIONAL;
        } else if (s.equals("international")) {
            return INTERNATIONAL;
        } else if (s.equals("unknown")) {
            return UNKNOWN;
        } else {
            return 0;
        }
    }
    
    public static String toString(int code) {
        return IND[code];
    }
    
}
