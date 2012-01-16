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

package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.SessionTerminationMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

/**
 * 
 * Implementation of {@link SessionTerminationMessage}. Its super class for STR
 * and STA, it implements common methods.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see DiameterMessageImpl
 */
public abstract class SessionTerminationMessageImpl extends DiameterMessageImpl implements SessionTerminationMessage {

  /**
   * 
   * @param message
   */
  public SessionTerminationMessageImpl(Message message) {
    super(message);
  }

  public byte[][] getClassAvps() {
    return getAvpsAsOctetString(Avp.CLASS);
  }

  public void setClassAvp(byte[] classAvp) {
    addAvp(Avp.CLASS, classAvp);
  }

  public void setClassAvps(byte[][] classAvps) {
    DiameterAvp[] values = new DiameterAvp[classAvps.length];

    for(int index = 0; index < classAvps.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.CLASS, classAvps[index]);
    }

    this.message.getAvps().removeAvp(Avp.CLASS);
    this.setExtensionAvps(values);
  }

}
