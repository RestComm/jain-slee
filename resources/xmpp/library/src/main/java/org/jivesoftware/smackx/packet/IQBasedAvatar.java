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

package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class IQBasedAvatar extends IQ {

    private Data data;
  
    public Data getData() {
        return data;
    }
   
    public void setData(Data data) {
        this.data = data;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:avatar\">");
        if (getData() != null) {
            buf.append(getData().toXML());            
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }

    public static class Data {
    	
    	private String data;
    	private String mimetype;
    	
    	
    	public Data(String data, String mimetype) {
    		this.data = data;
    		this.mimetype = mimetype;
    	}
    	
    	public String getData() {
    		return data;
    	}
    	
    	public String getMimetype() {
    		return mimetype;
    	}
    	
    	public String toXML() {
    		StringBuffer buf = new StringBuffer();
    		buf.append("<data mimetype=\"").append(mimetype).append("\">").append(data).append("</data>");
    		return buf.toString();
    	}
    }
}