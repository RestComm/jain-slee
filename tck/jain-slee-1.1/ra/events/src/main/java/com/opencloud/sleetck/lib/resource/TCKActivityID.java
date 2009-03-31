/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource;

/**
 * A TCKActivityID object uniquely identifies a TCKActivity object
 * within the TCKResource.
 */
public interface TCKActivityID {

    /**
     * Returns the name of the activity, as passed to the createActivity()
     * method of the resource's test interface or the resource's SBB interface.
     * The name is not guaranteed to be unique.
     */
    public String getName();

    /**
     * TCKActivityIDs are equal only if they represent the same TCKActivity object.
     */
    public boolean equals(Object o);

    /**
     * Hash codes for TCKActivityIDs are guaranteed to be equal only if they
     * represent the same TCKActivity object.
     */
    public int hashCode();

}