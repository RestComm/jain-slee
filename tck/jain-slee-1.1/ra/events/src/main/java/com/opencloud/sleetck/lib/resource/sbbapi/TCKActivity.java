/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.sbbapi;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;

/**
 * The TCKActivity interface is the SBB's interface to the
 * singular activity type used by the TCK.
 */
public interface TCKActivity {

    /**
     * Returns the activity's ID.
     */
    public TCKActivityID getID();

    /**
     * An SBB may call this method to end an activity directly.
     * The resource adaptor will inform the SLEE that the activity has ended.
     */
    public void endActivity() throws TCKTestErrorException;

    /**
     * Returns true if the activity is live, or false if the activity has ended.
     */
    public boolean isLive() throws TCKTestErrorException;

    /**
     * Sends an asynchronous message to the test. The test will receive the message
     * along with the ID of the activity via the onSBBMessage() method.
     * @param payload the payload of the message,
     *  as returned by TCKSbbMessage.getMessage()
     */
    public void sendSbbMessage(Object payload) throws TCKTestErrorException;

}
