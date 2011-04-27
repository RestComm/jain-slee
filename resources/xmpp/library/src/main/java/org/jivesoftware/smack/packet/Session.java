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

/**
 * IQ packet that will be sent to the server to establish a session.<p>
 *
 * If a server supports sessions, it MUST include a <i>session</i> element in the
 * stream features it advertises to a client after the completion of stream authentication.
 * Upon being informed that session establishment is required by the server the client MUST
 * establish a session if it desires to engage in instant messaging and presence functionality.<p>
 *
 * For more information refer to the following
 * <a href=http://www.xmpp.org/specs/rfc3921.html#session>link</a>.
 *
 * @author Gaston Dombiak
 */
public class Session extends IQ {

    public Session() {
        setType(IQ.Type.SET);
    }

    public String getChildElementXML() {
        return "<session xmlns=\"urn:ietf:params:xml:ns:xmpp-session\"/>";
    }
}
