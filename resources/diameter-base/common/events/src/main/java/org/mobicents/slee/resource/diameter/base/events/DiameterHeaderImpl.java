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

import org.jdiameter.api.Message;
import org.jdiameter.client.impl.parser.MessageImpl;

import net.java.slee.resource.diameter.base.events.DiameterHeader;

/**
 * 
 * <br>
 * Super project: mobicents <br>
 * 3:05:20 PM Jun 20, 2008 <br>
 * 
 * Implements {@link DiameterHeader}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterHeaderImpl implements DiameterHeader {

  private Message msg = null;

  public DiameterHeaderImpl(Message msg) {
    super();
    this.msg = msg;
  }

  public long getApplicationId() {
    return this.msg.getApplicationId();
  }

  public int getCommandCode() {
    return this.msg.getCommandCode();
  }

  public long getEndToEndId() {
    return this.msg.getEndToEndIdentifier();
  }

  public long getHopByHopId() {
    return this.msg.getHopByHopIdentifier();
  }

  public int getMessageLength() {
    return 0;
  }

  public short getVersion() {
    return this.msg.getVersion();
  }

  public boolean isError() {
    return this.msg.isError();
  }

  public boolean isPotentiallyRetransmitted() {
    return this.msg.isReTransmitted();
  }

  public boolean isProxiable() {
    return this.msg.isProxiable();
  }

  public boolean isRequest() {
    return this.msg.isRequest();
  }

  public void setEndToEndId(long etd) {
    ((MessageImpl) this.msg).setEndToEndIdentifier(etd);
  }

  public void setHopByHopId(long hbh) {
    ((MessageImpl) this.msg).setHopByHopIdentifier(hbh);
  }

  @Override
  public Object clone() {
    // Findbugs says this should not be done. Confirm super.clone() works.
    // return new DiameterHeaderImpl(this.msg);
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      return null;
    }
  }

}
