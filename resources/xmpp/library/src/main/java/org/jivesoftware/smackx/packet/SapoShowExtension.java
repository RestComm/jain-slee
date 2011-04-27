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

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * sapo:show packet extension. Ex: <x xmlns="sapo:show"><show>sleeping</show><mood>smile</mood></x>
 * @author Eduardo Martins
 *
 */
public class SapoShowExtension implements PacketExtension {

	public static final String ELEMENT_NAME = "x";
	public static final String ELEMENT_NAMESPACE = "sapo:show";
	
    private String show = null;
    private String mood = null;

    /**
    * Returns the XML element name of the extension sub-packet root element.
    * Always returns "x"
    *
    * @return the XML element name of the packet extension.
    */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /** 
     * Returns the XML namespace of the extension sub-packet root element.
     * According the specification the namespace is always "sapo:show"
     *
     * @return the XML namespace of the packet extension.
     */
    public String getNamespace() {
        return ELEMENT_NAMESPACE;
    }

    /**
     * Returns the XML representation of a sapo:show extension according the specification.
     * 
     */
    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append(
            "\">");
        if (show == null || show.equals(""))
        	buf.append("<show/>");
        else
        	buf.append("<show>"+show+"</show>");
        if (mood == null || mood.equals(""))
        	buf.append("<mood/>");
        else
        	buf.append("<mood>"+mood+"</mood>");
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    public void setMood(String mood) {
		this.mood = mood;
	}
    
    public void setShow(String show) {
		this.show = show;
	}
    
    public String getMood() {
		return mood;
	}
    
    public String getShow() {
		return show;
	}
    
    
	
}
