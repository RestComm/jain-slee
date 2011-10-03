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

package net.java.slee.resource.diameter.cxdx.events.avp;

/**
 * Java class to represent the Adaptations enumerated type.
 * 
 * 6.3.4 Server-Capabilities AVP
 * The Server-Capabilities AVP is of type Grouped. This AVP contains information 
 * to assist the I-CSCF in the selection of an S-CSCF.
 * 
 * AVP format
 * Server-Capabilities ::= <AVP header: 603 10415>
 *   *[Mandatory-Capability]
 *   *[Optional-Capability]
 *   *[Server-Name]
 *   *[AVP]
 *   
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ServerCapabilities extends net.java.slee.resource.diameter.base.events.avp.GroupedAvp {

  /**
   * Returns the value of the Mandatory-Capability AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long[] getMandatoryCapabilities(); 

  /**
   * Returns the value of the Optional-Capability AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long[] getOptionalCapabilities(); 

  /**
   * Returns the value of the Server-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String[] getServerNames(); 

  /**
   * Sets the value of the Mandatory-Capability AVP, of type Unsigned32. 
   */
  abstract void setMandatoryCapability(long mandatoryCapability); 
  
  /**
   * Sets the value of the Mandatory-Capability AVP, of type Unsigned32.
   */
  abstract void setMandatoryCapabilities(long[] mandatoryCapabilities); 

  /**
   * Sets the value of the Optional-Capability AVP, of type Unsigned32.
   */
  abstract void setOptionalCapability(long optionalCapability);
  
  /** 
   * Sets the value of the Optional-Capability AVP, of type Unsigned32.
   */
  abstract void setOptionalCapabilities(long[] optionalCapabilities);

  /**
   * Returns the value of the Server-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract void setServerName(String serverName);
  
  /** 
   * Returns the value of the Server-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract void setServerNames(String[] serverNames);
}
