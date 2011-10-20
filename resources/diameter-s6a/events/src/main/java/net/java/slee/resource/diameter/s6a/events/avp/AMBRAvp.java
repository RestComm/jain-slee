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

package net.java.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the AMBR grouped AVP type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.41 AMBR
 * 
 * The AMBR AVP is of type Grouped.
 * 
 * AVP format
 * AMBR ::= < AVP header: 1435 10415 >
 *          { Max-Requested-Bandwidth-UL }
 *          { Max-Requested-Bandwidth-DL }
 *         *[AVP]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface AMBRAvp extends GroupedAvp {

  public boolean hasMaxRequestedBandwidthUL();
  public long getMaxRequestedBandwidthUL();
  public void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL);

  public boolean hasMaxRequestedBandwidthDL();
  public long getMaxRequestedBandwidthDL();
  public void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL);

}
