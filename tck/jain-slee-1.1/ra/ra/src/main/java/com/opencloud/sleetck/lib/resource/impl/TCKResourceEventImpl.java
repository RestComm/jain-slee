/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.io.Serializable;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;

/**
 * An implementation of the two TCK resource events.
 */
public class TCKResourceEventImpl implements TCKResourceEventX, TCKResourceEventY, Serializable {

    public TCKResourceEventImpl(long oid, String type, Object message, TCKActivity activity) {
        this.oid=oid;
        this.type=type;
        this.message=message;
        this.activity=activity;
    }

    // -- Implementation of TCKResourceEvent methods -- //

    public long getEventObjectID() {
        return oid;
    }

    public String getEventTypeName() {
        return type;
    }

    public Object getMessage() {
        return message;
    }

    public TCKActivity getActivity() {
        return activity;
    }

    // -- Implementation of java.lang.Object methods -- //

    public boolean equals(Object o) {
        return o != null && o instanceof TCKResourceEventImpl &&
                        ((TCKResourceEventImpl)o).oid == oid;
    }

    public int hashCode() {
        return (int)oid;
    }

    public String toString() {
        return "TCKResourceEvent: type="+type+",oid="+oid+
                ",message="+message+",activity="+activity;
    }

    // -- Private state -- //

    private long oid;
    private String type;
    private Object message;
    private TCKActivity activity;

}