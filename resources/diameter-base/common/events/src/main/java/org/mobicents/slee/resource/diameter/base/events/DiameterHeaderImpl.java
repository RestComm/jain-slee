/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
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
