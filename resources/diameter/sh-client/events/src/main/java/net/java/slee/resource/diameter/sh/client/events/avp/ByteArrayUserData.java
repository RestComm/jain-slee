/*
 * Diameter Sh Resource Adaptor Type
 *
 * Copyright (C) 2006 Open Cloud Ltd.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of version 2.1 of the GNU Lesser 
 * General Public License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301  USA, or see the FSF site: http://www.fsf.org.
 */
package net.java.slee.resource.diameter.sh.client.events.avp;

import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Simple immutable implementation of UserData interface that is constructed using a byte array (or String).  The encoding
 * and decoding of the XML document representing the user data is therefore the responsibility of the application. 
 * 
 * @author Open Cloud
 */
public class ByteArrayUserData implements UserData {

    public ByteArrayUserData(byte[] data) {
        this.data = data;
    }

    public ByteArrayUserData(String s) {
        this.data = s.getBytes();
    }

    public ByteArrayUserData(String s, String charsetName) throws UnsupportedEncodingException {
        this.data = s.getBytes(charsetName);
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

    public void toStream(OutputStream out) throws IOException {
        out.write(data);
    }

    public String toString() {
        return new StringBuffer().append("UserData[").append(new String(data)).append("]").toString();
    }

    private final byte[] data;
}
