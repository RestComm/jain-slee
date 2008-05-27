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

import java.io.OutputStream;
import java.io.IOException;

/**
 * Placeholder type for the UserData AVP type which is OctetString but contains an XML document with the
 * requested user data in a User-Data-Answer or new user data in a Profile-Update-Request message.  
 * 
 * @author Open Cloud
 */
public interface UserData {

    /**
     * Return the content to put into of the user data AVP.
     */
    byte[] getData();

    /**
     * Return the size of the data if known in advance, or -1 if unknown.
     */
    int getSize();

    /**
     * Write the data to the given output stream.
     */
    void toStream(OutputStream out) throws IOException;

}
