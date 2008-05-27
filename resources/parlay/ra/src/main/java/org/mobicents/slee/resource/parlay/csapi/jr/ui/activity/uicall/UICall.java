package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall;

import org.csapi.ui.IpAppUICallOperations;
import org.csapi.ui.IpUICall;
import org.mobicents.csapi.jr.slee.ui.IpUICallConnection;
import org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI;

/**
 * 
 */
public interface UICall extends AbstractUI, IpUICallConnection,
        IpAppUICallOperations {

    /**
     * Return the underlying gateway interface.
     * 
     * @return
     */
    IpUICall getIpUICall();

    /**
     * @return
     */
    TpUICallIdentifier getTpUICallIdentifier();

}
