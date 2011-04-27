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

package org.mobicents.jcc.inap.protocol.tcap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Oleg Kulikov
 */
public class Util {
    
    /** Creates a new instance of Util */
    public Util() {
    }
    
    public static synchronized int readLen(InputStream in) throws IOException {
        int b = in.read() & 0xff;
        if ((b & 0x80) != 0x80) {
            return b;
        } else {
            int count = b & 0x7f;
            int length = 0;
            
            for (int i = 0; i < count; i++) {
                length <<= 8;
                length |= (in.read() & 0xff);
            }
            return length;
        }
    }
    
    public static synchronized int readTag(InputStream in) throws IOException {
        int b = in.read() & 0xff;
        
        int tagClass = b & 0xc0;
        boolean isPrimitive = !((b & 0x20)== 0x20);
        
        int code = b & 0x1f;
        if (code == 0x1f) {
            code = 0;
            while (((b = in.read() & 0xff) & 0x80) == 0x80) {
                code <<= 7;
                code |= (b & 0x7f);
            }
            code <<= 7;
            code |= (b & 0x7f);
        }
        return code;
    }
    
    protected static synchronized byte[] intToByteArray(int mask, int shift, int num) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        while ((mask & num) != 0x00) {
            int b = (mask & num);
            out.write((byte) b);
            mask <<= shift;
        }
        
        byte[] buffer = out.toByteArray();
        int len =  buffer.length;
        byte[] data = new byte[len];
        
        for (int i = 0; i < len; i++ ) {
            data[len - (1 + i)] = buffer[i];
        }
        return data;
    }
    
    public static synchronized void encodeLength(int length, OutputStream out) throws IOException {
        if (length < 128) {
            out.write((byte) length);
        } else {
            byte[] buffer = intToByteArray(0xff,  8, length);
            int len = buffer.length;
            
            int b = len | 0x80;
            out.write((byte) b);
            
            for (int i = 0; i < len; i++) {
                out.write(buffer[i]);
            }
        }
    }
    
    public static synchronized void encodeTransactioID(int tag, long tid, OutputStream out) throws IOException {
        out.write(tag);
        out.write(0x4);
        out.write((byte) ((tid & 0xff000000) >> 24));
        out.write((byte) ((tid & 0x00ff0000) >> 16));
        out.write((byte) ((tid & 0x0000ff00) >> 8));
        out.write((byte) (tid & 0x000000ff));
    }
    
    
    
}
