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

package org.mobicents.examples.media.cnf;

import javax.slee.SbbLocalObject;

/**
 * Represents a conference bridge where for forest sounds.
 * 
 * @author Oleg Kulikov
 */
public interface Forest extends SbbLocalObject {
    /**
     * Starts conversation.
     * 
     * @param endpointName the name of the endpoint to wich user is bound.
     */
    public void enter(String endpointName);
}
