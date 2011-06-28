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

package org.mobicents.slee.resource.diameter.gx.events;


import org.jdiameter.api.Message;

import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;

import org.jdiameter.api.Avp;

/**
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class GxReAuthRequestImpl extends GxReAuthMessageImpl implements GxReAuthRequest {

    /**
     * @param message
     */
    public GxReAuthRequestImpl(Message message) {
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
    return "GxRAR";
  }

    
}
