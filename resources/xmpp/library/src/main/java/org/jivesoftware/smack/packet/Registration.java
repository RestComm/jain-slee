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

package org.jivesoftware.smack.packet;

import java.util.Map;
import java.util.Iterator;

/**
 * Represents registration packets. An empty GET query will cause the server to return information
 * about it's registration support. SET queries can be used to create accounts or update
 * existing account information. XMPP servers may require a number of attributes to be set
 * when creating a new account. The standard account attributes are as follows:
 * <ul>
 *      <li>name -- the user's name.
 *      <li>first -- the user's first name.
 *      <li>last -- the user's last name.
 *      <li>email -- the user's email address.
 *      <li>city -- the user's city.
 *      <li>state -- the user's state.
 *      <li>zip -- the user's ZIP code.
 *      <li>phone -- the user's phone number.
 *      <li>url -- the user's website.
 *      <li>date -- the date the registration took place.
 *      <li>misc -- other miscellaneous information to associate with the account.
 *      <li>text -- textual information to associate with the account.
 *      <li>remove -- empty flag to remove account.
 * </ul>
 *
 * @author Matt Tucker
 */
public class Registration extends IQ {

    private String instructions = null;
    private Map attributes = null;

    /**
     * Returns the registration instructions, or <tt>null</tt> if no instructions
     * have been set. If present, instructions should be displayed to the end-user
     * that will complete the registration process.
     *
     * @return the registration instructions, or <tt>null</tt> if there are none.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the registration instructions.
     *
     * @param instructions the registration instructions.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Returns the map of String key/value pairs of account attributes.
     *
     * @return the account attributes.
     */
    public Map getAttributes() {
        return attributes;
    }

    /**
     * Sets the account attributes. The map must only contain String key/value pairs.
     *
     * @param attributes the account attributes.
     */
    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:register\">");
        if (instructions != null) {
            buf.append("<instructions>").append(instructions).append("</instructions>");
        }
        if (attributes != null && attributes.size() > 0) {
            Iterator fieldNames = attributes.keySet().iterator();
            while (fieldNames.hasNext()) {
                String name = (String)fieldNames.next();
                String value = (String)attributes.get(name);
                buf.append("<").append(name).append(">");
                buf.append(value);
                buf.append("</").append(name).append(">");
            }
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }
}