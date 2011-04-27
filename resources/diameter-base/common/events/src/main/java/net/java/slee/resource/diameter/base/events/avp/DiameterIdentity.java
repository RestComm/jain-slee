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

import java.io.Serializable;

/**
 * Java class to represent the DiameterIdentity AVP type.
 * <p/>
 * The DiameterIdentity format is derived from the OctetString AVP Base Format.
 * <p/>
 * DiameterIdentity  = FQDN
 * <p/>
 * DiameterIdentity value is used to uniquely identify a Diameter
 * node for purposes of duplicate connection and routing loop
 * detection.
 * <p/>
 * The contents of the string MUST be the FQDN of the Diameter node.
 * If multiple Diameter nodes run on the same host, each Diameter
 * node MUST be assigned a unique DiameterIdentity.  If a Diameter
 * node can be identified by several FQDNs, a single FQDN should be
 * picked at startup, and used as the only DiameterIdentity for that
 * node, whatever the connection it is sent on.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterIdentity implements Serializable
{
  private static final long serialVersionUID = -7248928425138452437L;

  private String identity;

  public DiameterIdentity(String identity) {
    if(identity == null) {
      throw new NullPointerException("The contents of the string MUST be the FQDN of the Diameter node, it was null.");
    }

    this.identity = identity;
  }

  public String toString() {
    return this.identity;
  }

  @Override
  public int hashCode() {
    return this.identity.hashCode();
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if(that instanceof DiameterIdentity) {
      DiameterIdentity other = (DiameterIdentity) that;

      return this.identity == other.identity || (this.identity != null && this.identity.equals(other.identity));
    }

    return false;
  }
}