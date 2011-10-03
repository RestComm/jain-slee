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

package org.mobicents.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;
import net.java.slee.resource.diameter.cxdx.events.avp.ServerCapabilities;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * <br>Project: mobicents-diameter-server
 * <br>3:01:19 AM Jun 3, 2009 
 * <br>
 *
 * ServerCapabilitiesImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServerCapabilitiesImpl extends GroupedAvpImpl implements ServerCapabilities {

  public ServerCapabilitiesImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public ServerCapabilitiesImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#getMandatoryCapability()
   */
  public long[] getMandatoryCapabilities() { 
    return getAvpsAsUnsigned32(DiameterCxDxAvpCodes.MANDATORY_CAPABILITY, DiameterCxDxAvpCodes.CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#getOptionalCapability()
   */
  public long[] getOptionalCapabilities() { 
    return getAvpsAsUnsigned32(DiameterCxDxAvpCodes.OPTIONAL_CAPABILITY, DiameterCxDxAvpCodes.CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#getServerName()
   */
  public String[] getServerNames() { 
    return getAvpsAsUTF8String(DiameterCxDxAvpCodes.SERVER_NAME, DiameterCxDxAvpCodes.CXDX_VENDOR_ID);
  }
   
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#setMandatoryCapability(long) 
   */
  public void setMandatoryCapability( long mandatoryCapability ) { 
    addAvp(DiameterCxDxAvpCodes.MANDATORY_CAPABILITY, DiameterCxDxAvpCodes.CXDX_VENDOR_ID, mandatoryCapability);
  }
   
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#setMandatoryCapabilities(long[]) 
   */
  public void setMandatoryCapabilities( long[] mandatoryCapabilities ) { 
	  for(long mandatoryCapability : mandatoryCapabilities) {
		  setMandatoryCapability(mandatoryCapability);
	    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#setOptionalCapability(long[]) 
   */
  public void setOptionalCapability( long optionalCapability ) { 
    addAvp(DiameterCxDxAvpCodes.OPTIONAL_CAPABILITY, DiameterCxDxAvpCodes.CXDX_VENDOR_ID, optionalCapability);
  }
   
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#setOptionalCapabilities(long[]) 
   */
  public void setOptionalCapabilities( long[] optionalCapabilities ) {
	  for(long optionalCapability : optionalCapabilities) { 
		  setOptionalCapability(optionalCapability);
	    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#setServerName(java.lang.String)
   */
  public void setServerName( String serverName ) {
    addAvp(DiameterCxDxAvpCodes.SERVER_NAME, DiameterCxDxAvpCodes.CXDX_VENDOR_ID, serverName);
  }
   
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ServerCapabilities#setServerNames(java.lang.String[])
   */
  public void setServerNames( String[] serverNames ) {
	  for(String serverName : serverNames) {
		  setServerName(serverName);
	    }
  }

}