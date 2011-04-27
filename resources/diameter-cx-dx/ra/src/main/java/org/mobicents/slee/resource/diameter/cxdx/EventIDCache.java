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

package org.mobicents.slee.resource.diameter.cxdx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.FireableEventType;

import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeRequest;
import net.java.slee.resource.diameter.base.events.DeviceWatchdogRequest;
import net.java.slee.resource.diameter.base.events.DisconnectPeerRequest;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequest;
import net.java.slee.resource.diameter.cxdx.events.PushProfileRequest;
import net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationRequest;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentRequest;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;

import org.jdiameter.api.Message;

/**
 * Caches event IDs for the Diameter Cx/Dx RAs.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EventIDCache {

  private static final String BASE_PACKAGE_PREFIX = "net.java.slee.resource.diameter.base.events.";

  private static final String CXDX_PACKAGE_PREFIX = "net.java.slee.resource.diameter.cxdx.events.";

  public static Map<Integer, String> eventNames = new ConcurrentHashMap<Integer, String>();

  static {
    Map<Integer, String> eventsTemp = new HashMap<Integer, String>();

    eventsTemp.put(LocationInfoRequest.COMMAND_CODE, CXDX_PACKAGE_PREFIX + "LocationInfo");
    eventsTemp.put(MultimediaAuthenticationRequest.COMMAND_CODE, CXDX_PACKAGE_PREFIX + "MultimediaAuthentication");
    eventsTemp.put(PushProfileRequest.COMMAND_CODE, CXDX_PACKAGE_PREFIX + "PushProfile");
    eventsTemp.put(RegistrationTerminationRequest.COMMAND_CODE, CXDX_PACKAGE_PREFIX + "RegistrationTermination");
    eventsTemp.put(ServerAssignmentRequest.COMMAND_CODE, CXDX_PACKAGE_PREFIX + "ServerAssignment");
    eventsTemp.put(UserAuthorizationRequest.COMMAND_CODE, CXDX_PACKAGE_PREFIX + "UserAuthorization");

    // We support Base messages too, just in case...
    eventsTemp.put(AbortSessionRequest.commandCode, BASE_PACKAGE_PREFIX + "AbortSession");
    eventsTemp.put(AccountingRequest.commandCode, BASE_PACKAGE_PREFIX + "Accounting");
    eventsTemp.put(CapabilitiesExchangeRequest.commandCode, BASE_PACKAGE_PREFIX + "CapabilitiesExchange");
    eventsTemp.put(DeviceWatchdogRequest.commandCode, BASE_PACKAGE_PREFIX + "DeviceWatchdog");
    eventsTemp.put(DisconnectPeerRequest.commandCode, BASE_PACKAGE_PREFIX + "DisconnectPeer");
    eventsTemp.put(ReAuthRequest.commandCode, BASE_PACKAGE_PREFIX + "ReAuth");
    eventsTemp.put(SessionTerminationRequest.commandCode, BASE_PACKAGE_PREFIX + "SessionTermination");

    eventNames = Collections.unmodifiableMap(eventsTemp);
  }

  public static final String ERROR_ANSWER                = BASE_PACKAGE_PREFIX + "ErrorAnswer";
  public static final String EXTENSION_DIAMETER_MESSAGE  = BASE_PACKAGE_PREFIX + "ExtensionDiameterMessage";

  private static final String VENDOR  = "java.net";
  private static final String VERSION = "0.8";

  private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();

  public EventIDCache() {
  }

  /**
   * 
   * @param eventLookupFacility
   * @param message
   * @return
   */
  public FireableEventType getEventId(EventLookupFacility eventLookupFacility, Message message) {

    FireableEventType eventID = null;

    // Error is always the same.
    if (message.isError()) {
      eventID = getEventId(eventLookupFacility, ERROR_ANSWER);
    }
    else {
      final int commandCode = message.getCommandCode();
      final boolean isRequest = message.isRequest();

      String eventName = eventNames.get(commandCode);

      if(eventName != null) {
        eventID = getEventId(eventLookupFacility, eventName + (isRequest ? "Request" : "Answer"));
      }
      else {
        eventID = getEventId(eventLookupFacility, EXTENSION_DIAMETER_MESSAGE);
      }
    }

    return eventID;
  }

  /**
   * 
   * @param eventLookupFacility
   * @param eventName
   * @return
   */
  private FireableEventType getEventId(EventLookupFacility eventLookupFacility, String eventName) {

    FireableEventType eventType = eventIds.get(eventName);
    if (eventType == null) {
      try {
        eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName,VENDOR,VERSION));
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
      if (eventType != null) {
        eventIds.put(eventName, eventType);
      }
    }
    return eventType;
  }

}
