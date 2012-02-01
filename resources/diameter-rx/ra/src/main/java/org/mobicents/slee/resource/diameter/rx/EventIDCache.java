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

package org.mobicents.slee.resource.diameter.rx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.FireableEventType;

import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.AbortSessionRequest;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;

import org.jdiameter.api.Message;

/**
 * Caches event IDs for the Diameter Rx RA.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class EventIDCache {

  private static final String RX_PACKAGE_PREFIX = "net.java.slee.resource.diameter.rx.events.";
  private static final String BASE_PACKAGE_PREFIX = "net.java.slee.resource.diameter.base.events.";
  public static Map<Integer, String> eventNames = new ConcurrentHashMap<Integer, String>();

  static {
    final Map<Integer, String> eventsTemp = new HashMap<Integer, String>();

    eventsTemp.put(AARequest.commandCode, RX_PACKAGE_PREFIX + "AA");

    // We support Base messages too, just in case...
    eventsTemp.put(AbortSessionRequest.commandCode, RX_PACKAGE_PREFIX + "AbortSession");
    eventsTemp.put(ReAuthRequest.commandCode, RX_PACKAGE_PREFIX + "ReAuth");
    eventsTemp.put(SessionTerminationRequest.commandCode, RX_PACKAGE_PREFIX + "SessionTermination");

    eventNames = Collections.unmodifiableMap(eventsTemp);
  }

  public static final String ERROR_ANSWER = BASE_PACKAGE_PREFIX + "ErrorAnswer";
  public static final String EXTENSION_DIAMETER_MESSAGE = BASE_PACKAGE_PREFIX + "ExtensionDiameterMessage";
  private static final String VENDOR = "java.net";
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
  public FireableEventType getEventId(final EventLookupFacility eventLookupFacility, final Message message) {
    FireableEventType eventID;

    // Error is always the same.
    if (message.isError()) {
      eventID = getEventId(eventLookupFacility, ERROR_ANSWER);
    }
    else {
      final int commandCode = message.getCommandCode();
      final boolean isRequest = message.isRequest();

      final String eventName = eventNames.get(commandCode);

      if (eventName != null) {
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
  private FireableEventType getEventId(final EventLookupFacility eventLookupFacility, final String eventName) {

    FireableEventType eventType = eventIds.get(eventName);
    if (eventType == null) {
      try {
        eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, VENDOR, VERSION));
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
