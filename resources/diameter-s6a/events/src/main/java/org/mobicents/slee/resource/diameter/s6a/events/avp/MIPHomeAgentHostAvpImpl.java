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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.MIPHomeAgentHostAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link MIPHomeAgentHostAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class MIPHomeAgentHostAvpImpl extends GroupedAvpImpl implements MIPHomeAgentHostAvp {

  public MIPHomeAgentHostAvpImpl() {
    super();
  }

  public MIPHomeAgentHostAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasDestinationRealm() {
    return hasAvp(DiameterS6aAvpCodes.DESTINATION_REALM);
  }

  public DiameterIdentity getDestinationRealm() {
    return getAvpAsDiameterIdentity(DiameterS6aAvpCodes.DESTINATION_REALM);
  }

  public void setDestinationRealm(DiameterIdentity realm) {
    addAvp(DiameterS6aAvpCodes.DESTINATION_REALM, realm);
  }

  public boolean hasDestinationHost() {
    return hasAvp(DiameterS6aAvpCodes.DESTINATION_HOST);
  }

  public void setDestinationHost(DiameterIdentity host) {
    addAvp(DiameterS6aAvpCodes.DESTINATION_HOST, host);
  }

  public DiameterIdentity getDestinationHost() {
    return getAvpAsDiameterIdentity(DiameterS6aAvpCodes.DESTINATION_HOST);
  }

}
