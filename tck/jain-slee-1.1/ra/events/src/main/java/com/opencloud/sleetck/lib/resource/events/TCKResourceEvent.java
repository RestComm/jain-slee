/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.events;

import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;

/**
 * Base interface for the TCK resource event types.
 */
public interface TCKResourceEvent {

    /**
     * Returns an ID for the event object, which is unique across the resource.
     */
    public long getEventObjectID();

    /**
     * Returns the name of the event type of this event, eg
     * <code>com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X1</code>
     */
    public String getEventTypeName();

    /**
     * Returns the message passed via this event, which may be null.
     */
    public Object getMessage();

    /**
     * Returns an interface to the activity associated with this event.
     */
    public TCKActivity getActivity();

}