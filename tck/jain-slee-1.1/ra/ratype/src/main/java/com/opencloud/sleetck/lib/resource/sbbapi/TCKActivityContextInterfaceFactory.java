/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.sbbapi;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * The SBB's interface for getting activity context interfaces
 * for TCKActivity objects.
 */
public interface TCKActivityContextInterfaceFactory {

    /**
     * Returns an activity context interface for the given TCKActivity
     */
    public ActivityContextInterface getActivityContextInterface(TCKActivity activity)
        throws UnrecognizedActivityException, FactoryException;

}