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

package net.java.slee.resource.diameter.rx;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.rx.events.*;

/**
 * Used by applications to create Diameter Rx messages.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface RxMessageFactory {

  public static final long _Rx_TGPP_VENDOR_ID = 10415L;
  public static final int _Rx_AUTH_APP_ID = 16777236;

  public AARequest createAARequest();

  /**
   * Creates an AA-Request message with the Session-Id AVP
   * populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public AARequest createAARequest(String sessionId);

  public AAAnswer createAAAnswer(AARequest request);

  public abstract AbortSessionRequest createAbortSessionRequest(DiameterAvp adiameteravp[]) throws AvpNotAllowedException;

  public abstract AbortSessionRequest createAbortSessionRequest();

  public abstract AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest abortsessionrequest, DiameterAvp adiameteravp[]) throws AvpNotAllowedException;

  public abstract AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest abortsessionrequest);

  public abstract ReAuthRequest createReAuthRequest(DiameterAvp adiameteravp[]) throws AvpNotAllowedException;

  public abstract ReAuthRequest createReAuthRequest();

  public abstract ReAuthAnswer createReAuthAnswer(ReAuthRequest reauthrequest, DiameterAvp adiameteravp[]) throws AvpNotAllowedException;

  public abstract ReAuthAnswer createReAuthAnswer(ReAuthRequest reauthrequest);

  public abstract SessionTerminationRequest createSessionTerminationRequest(DiameterAvp adiameteravp[]) throws AvpNotAllowedException;

  public abstract SessionTerminationRequest createSessionTerminationRequest();

  public abstract SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest sessionterminationrequest, DiameterAvp adiameteravp[]) throws AvpNotAllowedException;

  public abstract SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest sessionterminationrequest);

  /**
   * @return Base Diameter message factory
   */
  public DiameterMessageFactory getBaseMessageFactory();
}
