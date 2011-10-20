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

import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.MMEUserStateAvp;
import net.java.slee.resource.diameter.s6a.events.avp.UserState;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link MMEUserStateAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MMEUserStateAvpImpl extends GroupedAvpImpl implements MMEUserStateAvp {

  public MMEUserStateAvpImpl() {
    super();
  }

  public MMEUserStateAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasUserState() {
    return hasAvp(DiameterS6aAvpCodes.USER_STATE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setUserState(UserState us) {
    addAvp(DiameterS6aAvpCodes.USER_STATE, DiameterS6aAvpCodes.S6A_VENDOR_ID, us.getValue());
  }

  public UserState getUserState() {
    return (UserState) getAvpAsEnumerated(DiameterS6aAvpCodes.USER_STATE, DiameterS6aAvpCodes.S6A_VENDOR_ID, UserState.class);
  }

}
