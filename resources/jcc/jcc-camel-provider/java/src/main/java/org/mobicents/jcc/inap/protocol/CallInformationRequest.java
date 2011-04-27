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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;

/**
 *
 * @author Oleg Kulikov
 */
public class CallInformationRequest extends Operation {
    
	public static final int _TAG = 16;
	public static final int _TAG_CLASS = 0x00;
	public static final boolean _IS_PRIMITIVE = false;
	
    private LegID legID;
    private RequestedInformationTypeList list;
    
    /** Creates a new instance of CallInformationRequest */
    public CallInformationRequest(RequestedInformationTypeList list, LegID legID) {
        this.legID = legID;
        this.list = list;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        //Local operation code
        //bout.write(0x02);       //TAG: class=0 code=2 type=0
        //bout.write(0x01);       //Length...................1
        //bout.write(CALL_INFORMATION_REQUEST);

        byte[] b1 = list.toByteArray();
        
        ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
        bout2.write(0xA3);
        bout2.write(0x03);
        try {
            bout2.write(legID.toByteArray());
        } catch (IOException e) {
        }
        byte[] b2 = bout2.toByteArray();
        
        //call information request
        //bout.write(0x30);       //TAG: class=0 code=16 type=1
        //bout.write(b1.length + b2.length);
        
        try {
            bout.write(b1);
            bout.write(b2);
        } catch (IOException e) {
        }
        
        return bout.toByteArray();
    }
    
}
