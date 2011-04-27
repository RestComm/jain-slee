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

package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

/**
 * An interface for parsing custom IQ packets. Each IQProvider must be registered with
 * the ProviderManager class for it to be used. Every implementation of this
 * interface <b>must</b> have a public, no-argument constructor.
 *
 * @author Matt Tucker
 */
public interface IQProvider {

    /**
     * Parse the IQ sub-document and create an IQ instance. Each IQ must have a
     * single child element. At the beginning of the method call, the xml parser
     * will be positioned at the opening tag of the IQ child element. At the end
     * of the method call, the parser <b>must</b> be positioned on the closing tag
     * of the child element.
     *
     * @param parser an XML parser.
     * @return a new IQ instance.
     * @throws Exception if an error occurs parsing the XML.
     */
    public IQ parseIQ(XmlPullParser parser) throws Exception;
}