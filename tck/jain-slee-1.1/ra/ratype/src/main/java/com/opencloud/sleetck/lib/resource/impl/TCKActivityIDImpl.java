/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.io.Serializable;
import com.opencloud.sleetck.lib.resource.TCKActivityID;

public class TCKActivityIDImpl implements TCKActivityID, Serializable {

    public TCKActivityIDImpl(long oid, String name) {
        this.oid  = oid;
        this.name = name;
    }

    // -- Implementation of TCKActivityID methods -- //

    public String getName() {
        return name;
    }
    // -- Implementation of java.lang.Object methods -- //

    public boolean equals(Object o) {
        return o != null && o instanceof TCKActivityIDImpl &&
                        ((TCKActivityIDImpl)o).oid == oid;
    }

    public int hashCode() {
        return (int)oid;
    }

    public String toString() {
        return "TCKActivityID: name="+name+", object-id="+oid;
    }

    // -- Private state -- //

    private String name;
    private long oid;

}