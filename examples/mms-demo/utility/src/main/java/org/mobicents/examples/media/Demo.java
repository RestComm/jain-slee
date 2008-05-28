/*
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.examples.media;

import javax.slee.ActivityContextInterface;
import javax.slee.SbbLocalObject;

/**
 * Represents a demo for a media "sub-service".
 * Each demo has one or more dialogs.
 * 
 * @author Oleg Kulikov
 */
public interface Demo extends SbbLocalObject {
    /**
     * Starts demo
     * 
     * @param endpointName the name of the user's endpoint.
     * @param activity the user's activity.
     */
    public void startDemo(String endpointName);
}
