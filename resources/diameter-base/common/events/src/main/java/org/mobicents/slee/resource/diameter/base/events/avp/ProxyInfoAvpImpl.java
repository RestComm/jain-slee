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

package org.mobicents.slee.resource.diameter.base.events.avp;

import static org.jdiameter.api.Avp.PROXY_HOST;
import static org.jdiameter.api.Avp.PROXY_STATE;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;

/**
 * 
 * Implementation of AVP: {@link ProxyInfoAvp}
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ProxyInfoAvpImpl extends GroupedAvpImpl implements ProxyInfoAvp {

  public ProxyInfoAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
    name = "Proxy-Info-Avp";
  }

  public boolean hasProxyHost() {
    return hasAvp(PROXY_HOST);
  }

  public DiameterIdentity getProxyHost() {
    return getAvpAsDiameterIdentity(PROXY_HOST);
  }

  public void setProxyHost(DiameterIdentity proxyHost) {
    addAvp(PROXY_HOST, proxyHost.toString());
  }

  public boolean hasProxyState() {
    return hasAvp(PROXY_STATE);
  }

  public byte[] getProxyState() {
    return getAvpAsOctetString(PROXY_STATE);
  }

  public void setProxyState(byte[] proxyState) {
    addAvp(PROXY_STATE, proxyState);
  }
}
