/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceEventHandler;

/**
 * The TCK resource's interface to its event delegator.
 */
public interface EventDelegator extends TCKResourceEventHandler {

    /**
     * The TCK resource calls this method to add an event handler.
     */
    public void addEventHandler(TCKResourceEventHandler eventHandler);

    /**
     * Called to indicate that the given event handler is deactivating
     */
    public void eventHandlerDeactivating(TCKResourceEventHandler eventHandler);

    /**
     * The TCK resource calls this method to remove an event handler.
     */
    public void removeEventHandler(TCKResourceEventHandler eventHandler);

    /**
     * This method must be called when the test calls clearActivities(), to
     * allow the EventDelegator to clear its activity table.
     */
    public void onActivitiesCleared();

}