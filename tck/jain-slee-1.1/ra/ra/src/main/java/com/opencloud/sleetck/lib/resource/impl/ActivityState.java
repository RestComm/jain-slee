/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;

/**
 * Used by TCKResourceImpl to represent the state of an activity.
 */
public class ActivityState {

    /**
     * Constructs an ActivityState object for the activity
     * represented by the given interface.
     */
    ActivityState(TCKActivity activityInterface) {
        this.activityInterface = activityInterface;
    }

    /**
     * Returns true until the activity has been marked as ended,
     * i.e. until end() has been called.
     */
    public boolean isLive() {
        return isLive;
    }

    /**
     * Marks the activity as ended.
     */
    public void end() {
        isLive = false;
    }

    /**
     * Returns the SBB interface to the activity.
     */
    public TCKActivity getActivityInterface() {
        return activityInterface;
    }

    private TCKActivity activityInterface;
    private boolean isLive = true;
}
