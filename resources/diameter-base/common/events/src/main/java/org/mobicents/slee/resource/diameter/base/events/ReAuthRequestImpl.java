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

import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

/**
 * 
 * Implementation of {@link ReAuthRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @see DiameterMessageImpl
 */
public class ReAuthRequestImpl extends DiameterMessageImpl implements ReAuthRequest {
  public ReAuthRequestImpl(Message message) {
    super(message);
  }

  public boolean hasReAuthRequestType() {
    return hasAvp(Avp.RE_AUTH_REQUEST_TYPE);
  }

  public ReAuthRequestType getReAuthRequestType() {
    return (ReAuthRequestType) getAvpAsEnumerated(Avp.RE_AUTH_REQUEST_TYPE, ReAuthRequestType.class);
  }

  public void setReAuthRequestType(ReAuthRequestType reAuthRequestType) {
    addAvp(Avp.RE_AUTH_REQUEST_TYPE, reAuthRequestType.getValue());
  }

  @Override
  public String getLongName() {
    return "Re-Auth-Request";
  }

  @Override
  public String getShortName() {
    return "RAR";
  }

}
