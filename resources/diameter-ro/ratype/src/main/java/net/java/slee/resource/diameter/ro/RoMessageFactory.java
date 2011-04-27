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

package net.java.slee.resource.diameter.ro;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

/**
 * Used by applications to create Diameter Ro request messages.
 * Ro answer messages can be created using the RoServerSessionActivity.createRoCreditControlAnswer() method. 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RoMessageFactory {

  public static final long _RO_TGPP_VENDOR_ID = 10415L;
  public static final int  _RO_AUTH_APP_ID = 4;

  /**
   * Creates a Credit Control Request message.
   * 
   * @return
   */
  public RoCreditControlRequest createRoCreditControlRequest(/*CcRequestType type*/);

  /**
   * Creates a Credit Control Request message with the Session-Id AVP populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public RoCreditControlRequest createRoCreditControlRequest(String sessionId/*, CcRequestType type*/);

  /**
   * 
   * @return Base Diameter message factory
   */
  public DiameterMessageFactory getBaseMessageFactory();

}
