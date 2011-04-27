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

package net.java.slee.resource.diameter.base.events.avp;

/**
 * Defines an interface representing the Failed-AVP grouped AVP type.
 *
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * <pre>
 * 7.5.  Failed-AVP AVP
 * 
 *    The Failed-AVP AVP (AVP Code 279) is of type Grouped and provides
 *    debugging information in cases where a request is rejected or not
 *    fully processed due to erroneous information in a specific AVP.  The
 *    value of the Result-Code AVP will provide information on the reason
 *    for the Failed-AVP AVP.
 * 
 *    The possible reasons for this AVP are the presence of an improperly
 *    constructed AVP, an unsupported or unrecognized AVP, an invalid AVP
 *    value, the omission of a required AVP, the presence of an explicitly
 *    excluded AVP (see tables in Section 10), or the presence of two or
 *    more occurrences of an AVP which is restricted to 0, 1, or 0-1
 *    occurrences.
 * 
 *    A Diameter message MAY contain one Failed-AVP AVP, containing the
 *    entire AVP that could not be processed successfully.  If the failure
 *    reason is omission of a required AVP, an AVP with the missing AVP
 *    code, the missing vendor id, and a zero filled payload of the minimum
 *    required length for the omitted AVP will be added.
 * 
 *    AVP Format
 * 
 *       &lt;Failed-AVP&gt; ::= &lt; AVP Header: 279 &gt;
 *                     1* {AVP}
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface FailedAvp extends GroupedAvp {

}
